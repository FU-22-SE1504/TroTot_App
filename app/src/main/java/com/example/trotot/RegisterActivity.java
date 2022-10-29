package com.example.trotot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trotot.Database.ConnectDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {
    TextView status, signIn;
    EditText edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    String username, password, email, confirmPass;
    String ConnectionResult = "";
    Connection connection;
    Button btnSignUp;

    Statement statement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitView();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Binding form edit text to string
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                confirmPass = edtConfirmPassword.getText().toString();
                email = edtEmail.getText().toString();
                //Validation input value
                boolean validate = validationRegister(username, password, email, confirmPass);
//                boolean validate = true;
                if(validate){
                    try{
                        ConnectDatabase connectDatabase = new ConnectDatabase();
                        connection = connectDatabase.ConnectToDatabase();
                        if(connection != null){
                            // Check username is valid in database
                            String query = "select * from [User] where username = '"+ username +"';";
                            Statement st = connection.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            if(rs.isBeforeFirst()){
                                edtUsername.requestFocus();
                                edtUsername.setError("Username is valid in database");
                                Toast.makeText(RegisterActivity.this, "Username is valid in database", Toast.LENGTH_SHORT).show();
                            }else {
                                String hashPass = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                                query = "INSERT INTO [User] (username, password, email) " +
                                        "VALUES ('"+username+"', '"+hashPass+"', '"+email+"');";
                                st = connection.createStatement();
                                st.executeUpdate(query);
                                Toast.makeText(RegisterActivity.this, "Create successful", Toast.LENGTH_SHORT).show();
                                //Clear text when create success
                                ClearText();

                            }
                        }else{
                            ConnectionResult = "Check Connection";
                        }
                    }catch (Exception e){
                        Log.e("Fail", e.getMessage());
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Data is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    public void InitView(){
        edtUsername = (EditText) findViewById(R.id.edt_Register_Username);
        edtEmail = (EditText) findViewById(R.id.edt_Register_Email);
        edtPassword = (EditText) findViewById(R.id.edt_Register_Password);
        edtConfirmPassword = (EditText) findViewById(R.id.edt_Register_ConfirmPassword);
        btnSignUp = (Button) findViewById(R.id.btn_Register_SignUp);
        signIn = (TextView) findViewById(R.id.btn_Register_SignIn);
    }

    public void ClearText(){
        edtUsername.setText("");
        edtConfirmPassword.setText("");
        edtEmail.setText("");
        edtPassword.setText("");
    }

    // Check validation
    public boolean validationRegister(String username, String password, String email, String confirmPass){
        if(username.length() <= 8 ){
            edtUsername.requestFocus();
            edtUsername.setError("Username must be greater than 8 character");
            return false;
        }else if(!username.matches("[a-zA-Z0-9]+")) {
            edtUsername.requestFocus();
            edtUsername.setError("Username only alpha character and number");
            return false;
        }else if(email.length() <= 8){
            edtEmail.requestFocus();
            edtEmail.setError("Email must be greater than 8 character");
            return false;
        }else if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            edtEmail.requestFocus();
            edtEmail.setError("Email is not valid");
            return false;
        }else if(password.length() <= 8){
            edtPassword.requestFocus();
            edtPassword.setError("Password must be greater than 8 character");
            return false;
        }else if(!password.matches("[a-zA-Z0-9]+")) {
            edtPassword.requestFocus();
            edtPassword.setError("Password only alpha character and number");
            return false;
        }else if(confirmPass.length() <= 8){
            edtConfirmPassword.requestFocus();
            edtConfirmPassword.setError("Password must be greater than 8 character");
            return false;
        }else if(!confirmPass.matches("[a-zA-Z0-9]+")) {
            edtConfirmPassword.requestFocus();
            edtConfirmPassword.setError("Password only alpha character and number");
            return false;
        }else if(!password.equals(confirmPass)){
            edtConfirmPassword.requestFocus();
            edtConfirmPassword.setError("Confirm password not match");
            return false;
        }else{
            return true;
        }
    }
}