package com.example.sandhyayalla.mod8;

import java.io.Serializable;

public class Source implements Serializable{
    String id, name;

    public Source() {
    }

    @Override
    public String toString() {
        return "Source{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}