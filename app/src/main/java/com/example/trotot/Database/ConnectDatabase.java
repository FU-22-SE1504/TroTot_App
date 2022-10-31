package com.example.trotot.Database;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;

import com.example.trotot.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectDatabase {
    Connection con;
    String username, pass, port, ip, database;
    @SuppressLint("NewApi")
    public Connection ConnectToDatabase() {
        // SET CONNECTIONSTRING
        username = "sa";
        pass = "123456";
        port = "1433";
        //Coffe ip
//        ip = "10.0.40.250";
        //Home
//        ip = "192.168.1.75";
        // Class
        ip = "10.66.163.207";
        database = "TroTot";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String ConnectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user="+username+";password="+pass+";";
            con = DriverManager.getConnection(ConnectionURL);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return con;
    }
}
