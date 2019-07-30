package com.mad.newsapp;

import java.io.Serializable;

public class Source implements Serializable{
    String id, name;

    public Source() {
    }

    @Override
    public String toString() {
        return name;
    }
}
