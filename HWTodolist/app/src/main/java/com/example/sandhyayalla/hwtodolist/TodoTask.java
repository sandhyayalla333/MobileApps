package com.example.sandhyayalla.hwtodolist;

public class TodoTask {
    String name,datetime,key;
    int completed,priority;

    @Override
    public String toString() {
        return "TodoTask{" +
                "name='" + name + '\'' +
                ", datetime='" + datetime + '\'' +
                ", key='" + key + '\'' +
                ", status=" + completed +
                '}';
    }

    public TodoTask(String name, String datetime, String key, int completed) {
        this.name = name;
        this.datetime = datetime;
        this.key = key;
        this.completed = completed;
    }
    public TodoTask()
    {

    }
}
