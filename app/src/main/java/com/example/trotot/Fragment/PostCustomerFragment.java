package com.example.trotot.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Statement;

public class PostCustomerFragment extends Fragment {
    View view;
    Button btnConfirm;
    FloatingActionButton btnUploadImage;
    EditText edtTitle, edtDescription, edtContact;
    String title, description, contact;

    //Session
    SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    int user_id;

    // Data
    ConnectDatabase connectDatabase;
    Connection connection;

    //Image file
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_customer, container, false);
        InitView();

        prefs = this.getActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

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
        contact = edtContact.getText().toString();
        if (validationUpdateProfile(title, description, contact)) {
            connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();
            // type of customer
            int postType = 2;
            try {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading File....");
                progressDialog.show();
                String insertQuery = "insert into Post (user_id, title, description, contact, type_id)" +
                        " values (" + user_id + ", N'" + title + "', N'" + description + "', N'" + contact + "', " + postType + ");";

                Statement st = connection.createStatement();
                st.executeUpdate(insertQuery);
                Toast.makeText(getActivity(), "Create new post successful", Toast.LENGTH_SHORT).show();
                //Clear text when create success
                clearText();
            } catch (Exception e) {
                Log.e("Fail upload", e.getMessage());
            }
        } else {
            Toast.makeText(getContext(), "Check data", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearText() {
        edtTitle.setText("");
        edtDescription.setText("");
        edtContact.setText("");
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    private void InitView() {
        edtTitle = view.findViewById(R.id.post_customer_title);
        edtDescription = view.findViewById(R.id.post_customer_description);
        edtContact = view.findViewById(R.id.post_customer_contact);

        btnConfirm = view.findViewById(R.id.post_customer_btn_confirm);
    }

    // Check validation
    public boolean validationUpdateProfile(@NonNull String title, String description, String contact) {
        if (title.isEmpty()) {
            edtTitle.requestFocus();
            edtTitle.setError("Title must not be empty");
            return false;
        } else if (description.isEmpty()) {
            edtDescription.requestFocus();
            edtDescription.setError("Description must not be empty");
            return false;
        } else if (contact.isEmpty()) {
            edtContact.requestFocus();
            edtContact.setError("Contact must not be empty");
            return false;
        } else {
            return true;
        }
    }
}