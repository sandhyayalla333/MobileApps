package com.example.sandhyayalla.emailapplication;

import java.io.Serializable;

public class Login implements Serializable{

    String username;
    String password;

    @Override
    public String toString() {
        return "Login{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
