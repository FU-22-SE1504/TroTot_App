    package com.example.trotot.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.R;
import com.example.trotot.RegisterActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;
import java.util.prefs.Preferences;

    public class PostHouseholderFragment extends Fragment {
        View view;
        ImageView imagePoster;
        Button btnConfirm;
        EditText edtTitle, edtDescription, edtAddress, edtPrice, edtContact;
        String title, description, address, price, contact;

        //Session
        SharedPreferences prefs;
        public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
        int user_id;

        // Data
        ConnectDatabase connectDatabase;
        Connection connection;

        //Image file
        Uri uri;
        ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_householder, container, false);
        InitView();

        prefs = this.getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        // Get image url and set image
        imagePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        // Check validate and save data
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOnClickConfirm();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

        private void handleOnClickConfirm() {
            title = edtTitle.getText().toString();
            description = edtDescription.getText().toString();
            address = edtAddress.getText().toString();
            price = edtPrice.getText().toString();
            contact = edtContact.getText().toString();
            if(uri != null){
                if(validationUpdateProfile(title, description, address, price, contact)){
                    connectDatabase = new ConnectDatabase();
                    connection = connectDatabase.ConnectToDatabase();
                    int postType = 1;
                    try {
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Uploading File....");
                        progressDialog.show();
                        String poster = saveImage();
                        String insertQuery = "insert into Post (user_id, title, description, address, price, contact, poster, type_id)" +
                                " values ("+user_id+", '"+title+"', '"+description+"', '"+address+"', '"+price+"', '"+contact+"', '"+poster+"', "+postType+");";

                        Statement st = connection.createStatement();
                        st.executeUpdate(insertQuery);
                        Toast.makeText(getActivity(), "Create new post successful", Toast.LENGTH_SHORT).show();
                        //Clear text when create success
                        clearText();
                    }catch (Exception e){
                        Log.e("Fail upload", e.getMessage());
                    }
                }else {
                    Toast.makeText(getContext(), "Check data", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
            }


        }

        private void clearText() {
            edtTitle.setText("");
            edtDescription.setText("");
            edtAddress.setText("");
            edtPrice.setText("");
            edtContact.setText("");
            if (progressDialog.isShowing())
                progressDialog.dismiss();
//            imagePoster.setImageResource(R.drawable.ic);
        }

        private void selectImage() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startForMediaPickerResult.launch(intent);
        }

        private void InitView() {
            edtTitle = view.findViewById(R.id.post_householder_title);
            edtDescription = view.findViewById(R.id.post_householder_description);
            edtAddress = view.findViewById(R.id.post_householder_address);
            edtPrice = view.findViewById(R.id.post_householder_price);
            edtContact = view.findViewById(R.id.post_householder_contact);

            imagePoster = view.findViewById(R.id.post_householder_poster);

            btnConfirm = view.findViewById(R.id.post_householder_btn_confirm);
        }

        final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                        uri = data.getData();
                        imagePoster.setImageURI(uri);
                    } else {
                        Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                    }
                });

        // Encode image uri to byte array
        public String saveImage() {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imagePoster.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String image = Base64.encodeToString(bytes, Base64.DEFAULT);
            return  image;
        }
        // Check validation
        public boolean validationUpdateProfile(@NonNull String title, String description, String address, String price, String contact) {
            if (title.isEmpty()) {
                edtTitle.requestFocus();
                edtTitle.setError("Title must not be empty");
                return false;
            } else if (description.isEmpty()) {
                edtDescription.requestFocus();
                edtDescription.setError("Description must not be empty");
                return false;
            }else if (address.isEmpty()) {
                edtAddress.requestFocus();
                edtAddress.setError("Address must not be empty");
                return false;
            } else if (price.isEmpty()) {
                edtPrice.requestFocus();
                edtPrice.setError("Price must not be empty");
                return false;
            } else if (contact.isEmpty()) {
                edtContact.requestFocus();
                edtContact.setError("Contact must not be empty");
                return false;
            }
            else {
                return true;
            }
        }

    }