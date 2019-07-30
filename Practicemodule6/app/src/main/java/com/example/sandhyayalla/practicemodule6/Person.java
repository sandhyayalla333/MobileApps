package com.example.sandhyayalla.practicemodule6;

public class Person {
    String name;
    long id;
    int age;
    Address address;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", address=" + address +
                '}';
    }

    public Person()
    {}
}
