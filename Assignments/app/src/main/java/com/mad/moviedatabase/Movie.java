package com.mad.moviedatabase;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    String name;
    String desc;
    String genre;
    String rating;
    int year;
    String imdb;

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.name = in.readString();
        this.desc = in.readString();
        this.genre = in.readString();
        this.rating = in.readString();
        this.year = in.readInt();
        this.imdb = in.readString();
    }

    public Movie(String name, String desc, String genre, String rating, int year, String imdb) {
        this.name = name;
        this.desc = desc;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return name+" "+ desc+ " "+ genre+" "+rating+" "+year+" "+imdb;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.genre);
        dest.writeString(this.rating);
        dest.writeInt(this.year);
        dest.writeString(this.imdb);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
