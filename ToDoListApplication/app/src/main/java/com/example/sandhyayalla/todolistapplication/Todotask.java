package com.example.sandhyayalla.todolistapplication;

public class Todotask {


    String name,datetime,key;
    int priority,completed;
    public Todotask()
    {}


    public Todotask(String name, String datetime, int priority, int completed,String key) {
        this.name = name;
        this.datetime = datetime;
        this.priority = priority;
        this.completed = completed;
        this.key=key;
    }

    @Override
    public String toString() {
        return "Todotask{" +
                "name='" + name + '\'' +
                ", datetime='" + datetime + '\'' +
                ", key='" + key + '\'' +
                ", priority=" + priority +
                ", completed=" + completed +
                '}';
    }
}
