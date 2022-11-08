package com.example.trotot.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trotot.Database.ConnectDatabase;
import com.example.trotot.Model.User;
import com.example.trotot.R;
import com.example.trotot.RegisterActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ResetPasswordFragment extends Fragment {
    View view;
    Bundle bundle;
    User user;

    EditText ed_username, ed_oldPass, ed_newPass, ed_confirmPass;
    Button btnChange, btnCancel;
    String oldPass, newPass, confirmPass;
    Connection connection;
    ProgressDialog progressDialog;
    Statement st;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        if (bundle != null) {
            user = (User) bundle.get("User_Reset");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        // Init View
        InitView(view);

        // Set default value
        ed_username.setText(user.getUsername());

        // Confirm Change Password
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChangePassword(user.getUser_id());
            }
        });

        // Call back fragment
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return view;
    }

    private void handleChangePassword(int userId) {
        oldPass = ed_oldPass.getText().toString();
        newPass = ed_newPass.getText().toString();
        confirmPass = ed_confirmPass.getText().toString();
        boolean validate = validation();
        if (validate) {
            BCrypt.Result result = BCrypt.verifyer().verify(oldPass.toCharArray(), user.getPassword());
            if (result.verified) {
                if (newPass.equals(confirmPass)) {
                    boolean resultUpdate =  UpdatePassword(newPass, userId);
                    if(resultUpdate){
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack();
                    }
                } else {
                    ed_confirmPass.requestFocus();
                    ed_confirmPass.setError("Re-entered password is not the same");
                }
            } else {
                ed_oldPass.requestFocus();
                ed_oldPass.setError("The password is not the same as your password");
            }
        }
    }

    private boolean validation() {
        if (oldPass.length() <= 8) {
            ed_oldPass.requestFocus();
            ed_oldPass.setError("Password must be greater than 8 character");
            return false;
        } else if (!oldPass.matches("[a-zA-Z0-9]+")) {
            ed_oldPass.requestFocus();
            ed_oldPass.setError("Password only alpha character and number");
            return false;
        } else if (newPass.length() <= 8) {
            ed_newPass.requestFocus();
            ed_newPass.setError("Password must be greater than 8 character");
            return false;
        } else if (!newPass.matches("[a-zA-Z0-9]+")) {
            ed_newPass.requestFocus();
            ed_newPass.setError("Password only alpha character and number");
            return false;
        } else if (confirmPass.length() <= 8) {
            ed_confirmPass.requestFocus();
            ed_confirmPass.setError("Password must be greater than 8 character");
            return false;
        } else if (!confirmPass.matches("[a-zA-Z0-9]+")) {
            ed_confirmPass.requestFocus();
            ed_confirmPass.setError("Password only alpha character and number");
            return false;
        } else {
            return true;
        }
    }

    private void InitView(View view) {
        ed_username = view.findViewById(R.id.profile_reset_userName);
        ed_oldPass = view.findViewById(R.id.profile_reset_oldPass);
        ed_newPass = view.findViewById(R.id.profile_reset_newPass);
        ed_confirmPass = view.findViewById(R.id.profile_reset_confirmPass);

        btnChange = view.findViewById(R.id.profile_reset_btnConfirm);
        btnCancel = view.findViewById(R.id.profile_reset_btnCancel);
    }

    private boolean UpdatePassword(String password, int userId) {
        try {
            ConnectDatabase connectDatabase = new ConnectDatabase();
            connection = connectDatabase.ConnectToDatabase();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Update post ....");
            progressDialog.show();
            String hashPass = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            String updateQuery = "Update [User] set password = '"+hashPass+"' where user_id = "+userId+";";
            st = connection.createStatement();
            st.executeUpdate(updateQuery);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(getActivity(), "Update Password Success", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }
        return false;
    }

}