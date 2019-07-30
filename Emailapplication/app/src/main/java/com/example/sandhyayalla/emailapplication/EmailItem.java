package com.example.sandhyayalla.emailapplication;

import java.io.Serializable;

public class EmailItem implements Serializable{

    String firstname,lastname,id,sender_id,receiver_id,message,subject,Created_at,updated_at;

    @Override
    public String toString() {
        return "EmailItem{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", id='" + id + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", receiver_id='" + receiver_id + '\'' +
                ", message='" + message + '\'' +
                ", subject='" + subject + '\'' +
                ", Created_at='" + Created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    //"sender_fname": "Alice",
    //            "sender_lname": "Smith",
    //            "id": "4",
    //            "sender_id": "2",
    //            "receiver_id": "2",
    //            "message": "Hi",
    //            "subject": "Hello",
    //            "created_at": "2018-10-20 02:43:07",
    //            "updated_at": "2018-10-20 02:43:07"
}
