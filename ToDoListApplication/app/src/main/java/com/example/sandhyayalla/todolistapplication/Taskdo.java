package com.example.sandhyayalla.todolistapplication;

import io.realm.RealmObject;

public class Taskdo extends RealmObject {
    private String name,datetime;
    private int priority,completed,id;

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getPriority() {
        return priority;
    }

    public int getCompleted() {
        return completed;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public Taskdo(int id,String name, String datetime, int priority, int completed) {
        this.id=id;
        this.name = name;
        this.datetime = datetime;
        this.priority = priority;
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "name='" + name + '\'' +
                ", datetime='" + datetime + '\'' +
                ", priority=" + priority +
                ", completed=" + completed +
                ", id=" + id +
                '}';
    }

    public Taskdo() {
    }
}
