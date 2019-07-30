package com.example.finalexam.utils;

import java.io.Serializable;

/**
 * Created by mshehab on 5/6/18.
 */

public class Person implements Serializable{
   public String name;
    public int totalBudget;
    public int totalBought;
    public  int giftCount;
    public String id;

    public Person(String name, int totalBudget, int totalBought, int giftCount, String id) {
        this.name = name;
        this.totalBudget = totalBudget;
        this.totalBought = totalBought;
        this.giftCount = giftCount;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", totalBudget=" + totalBudget +
                ", totalBought=" + totalBought +
                ", giftCount=" + giftCount +
                ", id='" + id + '\'' +
                '}';
    }

    public Person() {
    }

}
