package com.mad.newsapp;

import java.text.ParseException;

public class DateFormatter {

    static String getFormattedDate(String inputDate) throws ParseException {
         String[] date = inputDate.split("T");
        return date[0];
    }
}
