package com.example.sandhyayalla.mod06sax;

public class Address {
    String line1,city,state,zip;
    public  Address()
    {}

    @Override
    public String toString() {
        return "Address{" +
                "line1='" + line1 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
