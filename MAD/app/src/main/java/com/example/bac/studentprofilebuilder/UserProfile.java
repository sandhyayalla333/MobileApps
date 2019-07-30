package com.example.bac.studentprofilebuilder;

import java.io.Serializable;

public class UserProfile implements Serializable {
    String fName;
    String Lname;
    String dept;
    long studentId;
    int imagename;
public UserProfile()
{}
    public UserProfile(String fn, String ln,String dept, long id,int imagename) {
        this.fName = fn;
        this.Lname = ln;
        this.dept=dept;
        this.studentId = id;
        this.imagename=imagename;
    }

    @Override
    public String toString() {
        return fName + " " + Lname + " "+dept+"" + studentId+""+imagename;
    }
}
