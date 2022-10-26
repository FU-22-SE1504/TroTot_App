package com.example.trotot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trotot.Database.ConnectDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    TextView signUp;
    EditText edtUsername, edtPassword;
    String username, password;
    String ConnectionResult = "";
    Connection connection;
    Button btnSignIn;
    // Session
    private SharedPreferences prefs;
    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";


    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        InitView();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Binding form edit text to string
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                boolean validate = validationLogin(username, password);
                if(validate){
                    try{
                        ConnectDatabase connectDatabase = new ConnectDatabase();
                        connection = connectDatabase.ConnectToDatabase();
                        if(connection != null){
                            // Check username is valid in database
                            String query = "select * from [User] where username = '"+username+"';";
                            Statement st = connection.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            if(rs.next()){
                                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), rs.getString(3));
                                if(result.verified){
                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    user = new User();
                                    user.setUser_id(rs.getInt(1));
                                    prefs.edit().putInt("user_id", user.getUser_id()).commit();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));

                                }else{
                                    edtPassword.setText("");
                                    edtPassword.setError("Wrong password");
                                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                edtPassword.setText("");
                                edtUsername.requestFocus();
                                edtUsername.setError("Wrong username");
                                Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ConnectionResult = "Check Connection";
                        }
                    }catch (Exception e){
                        Log.e("Fail", e.getMessage());
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Data is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    public void InitView(){
        edtUsername = (EditText) findViewById(R.id.edt_Login_Username);
        edtPassword = (EditText) findViewById(R.id.edt_Login_Password);
        btnSignIn = (Button) findViewById(R.id.btn_Login_SignIn);
        signUp = (TextView) findViewById(R.id.btn_Login_SignUp);
    }

    // Check validation
    public boolean validationLogin(String username, String password) {
        if (username.length() <= 8) {
            edtUsername.requestFocus();
            edtUsername.setError("Username must be greater than 8 character");
            return false;
        } else if (!username.matches("[a-zA-Z0-9]+")) {
            edtUsername.requestFocus();
            edtUsername.setError("Username only alpha character and number");
            return false;
        } else if (password.length() <= 8) {
            edtPassword.requestFocus();
            edtPassword.setError("Password must be greater than 8 character");
            return false;
        } else if (!password.matches("[a-zA-Z0-9]+")) {
            edtPassword.requestFocus();
            edtPassword.setError("Password only alpha character and number");
            return false;
        } else {
            return true;
        }
    }
}