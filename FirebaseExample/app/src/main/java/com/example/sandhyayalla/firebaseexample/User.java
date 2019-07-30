package com.example.sandhyayalla.firebaseexample;

import java.io.Serializable;

public class User implements Serializable{
    public String username;
    public String lastname;
    public String email;
    public String password;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username,String lastname, String email,String password) {
        this.username = username;
        this.lastname=lastname;
        this.email = email;
        this.password=password;
    }

    public User(String email,String password)
    {
        this.email = email;
        this.password=password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
