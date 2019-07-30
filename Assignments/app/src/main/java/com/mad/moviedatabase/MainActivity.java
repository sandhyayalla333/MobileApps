package com.mad.moviedatabase;
//Main Activity
//Homework03
//Naga Sandhyadevi Yalla,Ashikaß

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Movie> movieList = new ArrayList<Movie>();
    final static int REQ_CODE = 100;
    static String MOVIE_KEY = "MOVIE";
    public static final int REQ_CODE_EDIT = 101;
    String[] movieNames;
    int tempIndex;
    Movie movie = new Movie();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("My Favorite Movies");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.darkGray));

        //add movie to the list
        if (getIntent().getExtras() != null) {
            movie = (Movie) getIntent().getExtras().getSerializable(MOVIE_KEY);
            movieList.add(movie);
            Log.d("Demo", movie.toString());
        }

        //add movie functionality
        findViewById(R.id.btnAddMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addMovieIntent = new Intent(MainActivity.this, AddMovieActivity.class);
                //startActivity(addMovieIntent);
                startActivityForResult(addMovieIntent, REQ_CODE);
            }
        });
        //added by Sandhya
        //To get the data from add movie

        //call movieby year activity
        findViewById(R.id.btnYearList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent yearintent=new Intent(MainActivity.this,MoviesByYear.class);
                Intent yearintent = new Intent(Intent.ACTION_SEND);
                yearintent.putParcelableArrayListExtra("MovieList", movieList);
                startActivity(yearintent);

            }
        });
        //call moviebyrating activity
        findViewById(R.id.btnRatingList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent ratingIntent = new Intent(MainActivity.this, MovieByRating.class);
                Intent ratingIntent = new Intent("com.mad.moviedatabase.intent.action.VIEW");
                ratingIntent.putParcelableArrayListExtra("MovieList", movieList);
                startActivity(ratingIntent);

            }
        });


        //added by Sandhya
        //added by ashika
        //Edit movie functionality
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                movieNames = new String[movieList.size()];
                if(movieNames.length==0)
                {
                    Toast.makeText(MainActivity.this, "Please add movie", Toast.LENGTH_SHORT).show();
                }
                else {
                    for (int i = 0; i < movieList.size(); i++) {
                        movieNames[i] = movieList.get(i).name;
                    }
                    //create alert for displaying movie list
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("Pick a Movie")
                            .setItems(movieNames, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Log.d("Demo","Clicked: "+ movieNames[i]);
                                    tempIndex = i;
                                    Intent editIntent = new Intent(MainActivity.this, EditMovieActivity.class);
                                    editIntent.putExtra(MOVIE_KEY, movieList.get(i));
                                    startActivityForResult(editIntent, REQ_CODE_EDIT);
                                }
                            });

                    final AlertDialog alertMovieList = builder1.create();
                    alertMovieList.show();
                }
            }
        });

        //Delete movie functionality
        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                movieNames = new String[movieList.size()];
                for (int i = 0; i < movieList.size(); i++) {
                    movieNames[i] = movieList.get(i).name;
                }

                AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);

                builder2.setTitle("Pick a Movie")
                        .setItems(movieNames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                movieList.remove(i);
                                Toast.makeText(MainActivity.this, movieNames[i] + " movie deleted!", Toast.LENGTH_LONG).show();
                            }
                        });

                AlertDialog deleteMovieList = builder2.create();
                deleteMovieList.show();
            }
        });
        //ended by Ashika
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE) {
            Movie mov1 = new Movie();
            if (resultCode == 1) {
                if (data != null) {
                    mov1 = (Movie) data.getExtras().getParcelable(MOVIE_KEY);
                    movieList.add(mov1);
                    Log.d("demo", mov1.toString());
                }
            }
        } else if (requestCode == REQ_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                movie = data.getExtras().getParcelable(MOVIE_KEY);
                //movieList.remove(tempIndex);
                //movieList.add(tempIndex,movie);
                movieList.set(tempIndex,movie);
            }
        }
    }
}
