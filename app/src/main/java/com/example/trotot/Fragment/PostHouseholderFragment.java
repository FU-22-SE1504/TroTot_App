    package com.example.trotot.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trotot.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

    public class PostHouseholderFragment extends Fragment {
        View view;
        ImageView imagePoster;
        Button btnConfirm;

        //Image file
        Uri uri;
        StorageReference storageReference;
        ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_householder, container, false);
        InitView();

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
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading File....");
            progressDialog.show();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd", Locale.ROOT);
            Date now = new Date();
            String fileExtension = GetFileExtension(uri);
            String fileName = formatter.format(now);
            storageReference = FirebaseStorage.getInstance().getReference("UserAvatar/"+fileName + "." + fileExtension);
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
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

        private void selectImage() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startForMediaPickerResult.launch(intent);
        }

        private void InitView() {
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

        // Get Extension
        public String GetFileExtension(Uri uri)
        {
            ContentResolver contentResolver = getActivity().getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

            // Return file Extension
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }
    }