package com.example.sandhyayalla.firebaseexample;

import java.io.Serializable;

public class Message implements Serializable {
    String message,time,firstname,lastname,imagename,downloadablelink,key;

    public Message(String message, String time, String firstname, String lastname, String downloadablelink,String key) {
        this.message = message;
        this.time = time;
        this.firstname = firstname;
        this.lastname = lastname;
        this.downloadablelink = downloadablelink;
        this.key=key;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", imagename='" + imagename + '\'' +
                ", downloadablelink='" + downloadablelink + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
    public Message()
    {

    }
}
