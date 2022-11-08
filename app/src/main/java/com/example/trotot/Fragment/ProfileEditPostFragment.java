package com.example.trotot.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.trotot.Model.Post;
import com.example.trotot.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfileEditPostFragment extends Fragment {
    View view;
    Bitmap bitmap;
    EditText ed_title, ed_description, ed_address, ed_contact, ed_price;
    TextInputLayout address, price;
    ImageView posterImg;
    FloatingActionButton btnChangeImg;
    Button btnConfirm, btnCancel;
    Uri uri;

    // Data
    Post post;
    int householderType = 1;
    String poster;

    // Data
    ProgressDialog progressDialog;
    ConnectDatabase connectDatabase;
    Connection connection;
    Statement st;
    ResultSet rs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            post = (Post) bundle.get("Post_Edit");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_edit_post, container, false);

        // Init view
        InitView(view);

        // Set text
        SetText(post);

        // Change image poster
        if (post.getType_id() == householderType) {
            btnChangeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage();
                }
            });
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update data
                UpdatePost(post.getPost_id(), post.getType_id());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.body_container, new ProfileFragment());
                ft.commit();
            }
        });

        return view;
    }

    private String UpdateImagePoster() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) posterImg.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String image = Base64.encodeToString(bytes, Base64.DEFAULT);
        return image;
    }

    final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    uri = data.getData();
                    posterImg.setImageURI(uri);
                } else {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }
            });

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startForMediaPickerResult.launch(intent);
    }

    private void UpdatePost(int postId, int postType) {
        String title, description, price, address, contact;
        title = ed_title.getText().toString();
        description = ed_description.getText().toString();
        price = ed_price.getText().toString();
        address = ed_address.getText().toString();
        contact = ed_contact.getText().toString();
        boolean validate = validationUpdateProfile(title, description, price, address, contact, post.getType_id());
        if (validate) {
            connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();
            try {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Update post ....");
                progressDialog.show();
                String updateQuery = "";
                if (postType == householderType) {
                    poster = UpdateImagePoster();
                    updateQuery = "Update [Post] set [title] = N'" + title + "', [description] = N'" + description +
                            "', [price] = N'" + price + "', [address] = N'" + address + "', [contact] = N'" + contact + "', [poster] = N'" + poster + "' where post_id = " + postId;
                } else {
                    updateQuery = "Update [Post] set [title] = N'" + title + "', [description] = N'" + description + "', [contact] = N'" + contact + "' where post_id = " + postId;
                }
                st = connection.createStatement();
                st.executeUpdate(updateQuery);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(getActivity(), "Update information successful", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("Fail upload", e.getMessage());
            }
        }

    }

    private void SetText(Post post) {
        // Set text
        ed_title.setText(post.getTitle());
        ed_description.setText(post.getDescription());
        ed_contact.setText(post.getContact());

        // Set value for householder post
        if (post.getType_id() == 1) {
            ed_address.setText(post.getAddress());
            ed_price.setText(post.getPrice());
            // Set poster image
            if (post.getPoster() != null) {
                byte[] bytes = Base64.decode(post.getPoster(), Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // Set image
                posterImg.setImageBitmap(bitmap);
            }
        } else {
            address.setVisibility(View.GONE);
            price.setVisibility(View.GONE);
            posterImg.setVisibility(View.GONE);
            btnChangeImg.setVisibility(View.GONE);
        }
    }

    public void InitView(View view) {
        ed_title = view.findViewById(R.id.edit_post_title);
        ed_description = view.findViewById(R.id.edit_post_description);
        ed_address = view.findViewById(R.id.edit_post_address);
        ed_price = view.findViewById(R.id.edit_post_price);
        ed_contact = view.findViewById(R.id.edit_post_contact);

        posterImg = view.findViewById(R.id.edit_post_posterImg);
        btnChangeImg = view.findViewById(R.id.edit_post_edit_posterImg);
        btnConfirm = view.findViewById(R.id.edit_post_btnConfirm);
        btnCancel = view.findViewById(R.id.edit_post_cancel);

        address = view.findViewById(R.id.edit_post_text_layout_address);
        price = view.findViewById(R.id.edit_post_text_layout_price);
    }

    // Check validation
    public boolean validationUpdateProfile(@NonNull String title, String description, String address, String price, String contact, int type_id) {
        if (title.isEmpty()) {
            ed_title.requestFocus();
            ed_title.setError("Title must not be empty");
            return false;
        } else if (description.isEmpty()) {
            ed_description.requestFocus();
            ed_description.setError("Description must not be empty");
            return false;
        } else if (contact.isEmpty()) {
            ed_contact.requestFocus();
            ed_contact.setError("Contact must not be empty");
            return false;
        }
        // Check householder post value
        if (type_id == 1) {
            if (price.isEmpty()) {
                ed_price.requestFocus();
                ed_price.setError("Price must not be empty");
                return false;
            } else if (address.isEmpty()) {
                ed_address.requestFocus();
                ed_address.setError("Address must not be empty");
                return false;
            }
        } else {
            return true;
        }
        return true;
    }
}