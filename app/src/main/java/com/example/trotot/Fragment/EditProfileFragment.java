package com.example.trotot.Fragment;

import static com.google.android.gms.tasks.Tasks.await;

import android.app.Activity;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private View view;
    private CircleImageView imgAvatar;
    private Button btnCancel, btnConfirm;
    private TextView progressText;
    private ProgressBar progressBar;
    private EditText edtEmail, edtFullName, edtPhoneNumber;

    //Image
    private Uri uri;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("UserAvatar/");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        InitView();
        setUserInformation();

        progressBar.setVisibility(View.GONE);
        progressText.setVisibility(View.GONE);


        // Handle avatar image change
        imgAvatar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startForMediaPickerResult.launch(intent);
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

    private void onClickUpdateProfile(Uri uri) {
        String downloadURL;
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        UploadTask uploadTask = fileRef.putFile(uri);
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        User user = new User(uri.toString());
                        String userId = root.push().getKey();
                        root.child(userId).setValue(user);
                        Log.i("img", "upload success");
                        Toast.makeText(getActivity(), "Update Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressBar.setProgress((int)progress);
                progressText.setText(progress + "");
                Toast.makeText(getActivity(), "On Progress", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload image fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Get image extension
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(mUri));
    }

    private void setUserInformation() {
    }


    public void InitView() {
        edtEmail = view.findViewById(R.id.EditProfile_Email);
        edtFullName = view.findViewById(R.id.EditProfile_FullName);
        edtPhoneNumber = view.findViewById(R.id.EditProfile_PhoneNumber);

        progressText = view.findViewById(R.id.text_progress_bar);
        progressBar = view.findViewById(R.id.progress_bar);

        btnConfirm = view.findViewById(R.id.EditProfile_btnConfirm);
        btnCancel = view.findViewById(R.id.EditProfile_btnCancel);
        imgAvatar = view.findViewById(R.id.EditProfile_AvatarView);
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




    // Check validation
//    public boolean validationUpdateProfile(@NonNull String email, String fullName, String phoneNumber) {
//        if (email.length() <= 8) {
//            edtEmail.requestFocus();
//            edtEmail.setError("Email must be greater than 8 character");
//            return false;
//        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
//            edtEmail.requestFocus();
//            edtEmail.setError("Email is not valid");
//            return false;
//        } else if (fullName.length() <= 0) {
//            edtFullName.requestFocus();
//            edtFullName.setError("Full name can not be empty");
//            return false;
//        } else if (!fullName.matches("[a-zA-Z x]+")) {
//            edtFullName.requestFocus();
//            edtFullName.setError("Full name only alpha character");
//            return false;
//        } else if (phoneNumber.length() <= 9) {
//            edtPhoneNumber.requestFocus();
//            edtPhoneNumber.setError("Phone number must be 10 number");
//            return false;
//        } else if (!phoneNumber.matches("[0-9]+")) {
//            edtPhoneNumber.requestFocus();
//            edtPhoneNumber.setError("Phone number must be a number");
//            return false;
//        } else {
//            return true;
//        }
//    }




}