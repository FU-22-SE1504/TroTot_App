package com.example.trotot.Fragment;

import static com.google.android.gms.tasks.Tasks.await;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.R;
import com.example.trotot.Model.User;
import com.example.trotot.RegisterActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private View view;
    private CircleImageView imgAvatar;
    private Button btnCancel, btnConfirm;
    private TextView progressText;
    private ProgressBar progressBar;
    private EditText edtEmail, edtFullName, edtPhoneNumber, edtUsername;
    private String email, fullName, phoneNumber;

    // Data
    User user;
    ConnectDatabase connectDatabase;
    Connection connection;
    Statement st;
    ResultSet rs;


    //Image
    private Uri uri;
    private ProgressDialog progressDialog;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        user = new User();
        InitView();

        // Fetch data
        getUserData(user.getUser_id());

        // Handle avatar image change
        imgAvatar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectImage();
            }
        });

        //Handle confirm onclick
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri != null){
                    onClickUpdateProfile(uri);
                }else{
                    Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void getUserData(int userID) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();

            if (connection != null){
                String selectQuery = "Select * from [User] where user_id = " + userID;

                st = connection.createStatement();
                rs = st.executeQuery(selectQuery);
                if(rs.next()){
                    user = new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getDate(7),
                            rs.getInt(8), rs.getString(9));
                    edtUsername.setText(user.getUsername());
                    edtEmail.setText(user.getEmail());
                    edtPhoneNumber.setText(user.getPhone_number());
                    edtFullName.setText(user.getFull_name());
                    // Set image
                    String imageID = user.getAvatar();
                    StorageReference imageRef = storageReference.child("UserAvatar/" + imageID);
                    long MAXBYTES = 1024*1024;
                    imageRef.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Convert byte[] to bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imgAvatar.setImageBitmap(bitmap);
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Fetching fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    private void onClickUpdateProfile(Uri uri) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();
        String fileExtension = GetFileExtension(uri);
        storageReference = FirebaseStorage.getInstance().getReference("UserAvatar/" + "Avatar_userID_" + user.getUser_id() + fileExtension);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                try {
                    updateQueryData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(getActivity(), "Failed to Upload", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQueryData() throws SQLException {
        if(validationUpdateProfile(email, fullName, phoneNumber))
        connectDatabase = new ConnectDatabase();
        connection = connectDatabase.ConnectToDatabase();

        if (connection != null){
            String queryUpdate = "Update [User] set [email] = " + email + ", [phone_number] = " + phoneNumber +
                    ", [full_name] =" + fullName + ", [avatar] = " + imgAvatar.toString() + " where user_id = " + user.getUser_id();
            st = connection.createStatement();
            st.executeUpdate(queryUpdate);
            Toast.makeText(getActivity(), "Create successful", Toast.LENGTH_SHORT).show();
        }
    }

    //Get image extension
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(mUri));
    }

    public void InitView() {
        edtUsername = view.findViewById(R.id.EditProfile_Username);
        edtEmail = view.findViewById(R.id.EditProfile_Email);
        edtFullName = view.findViewById(R.id.EditProfile_FullName);
        edtPhoneNumber = view.findViewById(R.id.EditProfile_PhoneNumber);

        email = edtEmail.getText().toString();
        fullName = edtFullName.getText().toString();
        phoneNumber = edtPhoneNumber.getText().toString();

        progressText = view.findViewById(R.id.text_progress_bar);
        progressBar = view.findViewById(R.id.progress_bar);

        btnConfirm = view.findViewById(R.id.EditProfile_btnConfirm);
        btnCancel = view.findViewById(R.id.EditProfile_btnCancel);
        imgAvatar = view.findViewById(R.id.EditProfile_AvatarView);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

      final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                        uri = data.getData();
                        imgAvatar.setImageURI(uri);
                        progressText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                    }
                });

    // Get Extension
    public String GetFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startForMediaPickerResult.launch(intent);
    }


    // Check validation
    public boolean validationUpdateProfile(@NonNull String email, String fullName, String phoneNumber) {
        if (email.length() <= 8) {
            edtEmail.requestFocus();
            edtEmail.setError("Email must be greater than 8 character");
            return false;
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            edtEmail.requestFocus();
            edtEmail.setError("Email is not valid");
            return false;
        } else if (fullName.length() <= 0) {
            edtFullName.requestFocus();
            edtFullName.setError("Full name can not be empty");
            return false;
        } else if (!fullName.matches("[a-zA-Z x]+")) {
            edtFullName.requestFocus();
            edtFullName.setError("Full name only alpha character");
            return false;
        } else if (phoneNumber.length() <= 9) {
            edtPhoneNumber.requestFocus();
            edtPhoneNumber.setError("Phone number must be 10 number");
            return false;
        } else if (!phoneNumber.matches("[0-9]+")) {
            edtPhoneNumber.requestFocus();
            edtPhoneNumber.setError("Phone number must be a number");
            return false;
        } else {
            return true;
        }
    }




}