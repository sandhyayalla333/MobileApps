package com.example.sandhyayalla.emailapplication;

import android.util.MalformedJsonException;

import org.json.JSONObject;

import java.io.IOException;

public class AuthenticationParser {

    static UserAuthentication userAuthentication = new UserAuthentication();

    static UserAuthentication parseAuthenticationDetails(String response) throws MalformedJsonException, IOException {
        try {
            JSONObject newsJson = new JSONObject(response);

            userAuthentication.token = newsJson.getString("token");
            userAuthentication.userEmail = newsJson.getString("user_email");
            userAuthentication.userFname = newsJson.getString("user_fname");
            userAuthentication.userLname = newsJson.getString("user_lname");
            userAuthentication.userRole = newsJson.getString("user_role");
            userAuthentication.userId = newsJson.getString("user_id");

        } catch (Exception e) {

        } finally {

        }
        return userAuthentication;
    }
}
