package com.example.trotot.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.MainActivity;
import com.example.trotot.R;
import com.example.trotot.User;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {
    User user;
    String email, fullName, phoneNumber;
    Button btnConfirm;
    EditText edtUsername, edtFullName, edtEmail, edtPhoneNumber;
    CircleImageView uploadImg;
    //Session
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    //Connection
    Connection connection;
    String ConnectionResult = "";
    Integer user_id;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        //Set session
        prefs = this.getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        InitView(view);
        user_id = prefs.getInt("user_id", 0);
        try {
            ConnectDatabase connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();
            if (connection != null) {
                // Get User by user id
                String query = "select * from [User] where user_id = " + user_id + ";";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                if (rs.next()) {
                    user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getDate(7), rs.getInt(8), rs.getString(9));
                    edtUsername.setText(user.getUsername());
                    edtEmail.setText(user.getEmail());
                    edtPhoneNumber.setText(user.getPhone_number());
                    edtFullName.setText(user.getFull_name());
                    //Set image
                    byte[] bytes = Base64.decode(user.getAvatar(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    uploadImg.setImageBitmap(bitmap);
                    // Update info
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Get String
                            email = edtEmail.getText().toString();
                            fullName = edtFullName.getText().toString();
                            phoneNumber = edtPhoneNumber.getText().toString();
                            if (validationUpdateProfile(email, fullName, phoneNumber)) {
                                new sendData().execute();
                            } else {
                                Toast.makeText(getActivity(), "Error data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (Exception e) {
            Log.e("Fail", e.getMessage());
        }

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
                ImagePicker.Companion.with(getActivity())
                        .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                        .galleryOnly()
                        .galleryMimeTypes(mimeTypes)
                        .crop()
                        .compress(768)
                        .maxResultSize(1920, 1916)
                        .createIntent(intent -> {
                            startForMediaPickerResult.launch(intent);
                            return null;
                        });
            }
        });
        return view;
    }

    final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    uploadImg.setImageURI(imageUri);
                } else {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }
            });


    public class sendData extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String message = "fail";
            try {
                String image = saveImage();
                String update = "Update [User] set email = '" + email + "', full_name = '" + fullName + "', phone_number = '" + phoneNumber + "', avatar = '"+image+"'  where user_id = " + user_id + "";
                Statement st = connection.createStatement();
                int rs = st.executeUpdate(update);
                if (rs == -1) {
                    message = "success";
                    Toast.makeText(getActivity(), "Update Fail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Update Success", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return message;
        }
    }

    public String saveImage() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) uploadImg.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String image = Base64.encodeToString(bytes, Base64.DEFAULT);
        return  image;
    }


    public void InitView(View view) {
        edtEmail = view.findViewById(R.id.EditProfile_Email);
        edtFullName = view.findViewById(R.id.EditProfile_FullName);
        edtUsername = view.findViewById(R.id.EditProfile_Username);
        edtPhoneNumber = view.findViewById(R.id.EditProfile_PhoneNumber);

        btnConfirm = view.findViewById(R.id.EditProfile_btnConfirm);
        uploadImg = view.findViewById(R.id.EditProfile_AvatarView);
    }

    // Check validation
    public boolean validationUpdateProfile(String email, String fullName, String phoneNumber) {
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