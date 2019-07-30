package com.mad.moviedatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MoviesByYear extends AppCompatActivity {
    public ArrayList<Movie> moviesbyYear = new ArrayList<Movie>();
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_by_year);
        setTitle("Movie by Year");
        final TextView tv_movie=findViewById(R.id.tvMovie);
        final TextView tv_desc=findViewById(R.id.tvDesc);
        final TextView tv_genre=findViewById(R.id.tvGenre);
        final TextView tv_rating=findViewById(R.id.tvRatingval);
        final TextView tv_year=findViewById(R.id.tvYearvalue);
        final TextView tv_imdb=findViewById(R.id.tvIMDBvalue);

        ImageView ivfirst=(ImageView)findViewById(R.id.ivFirst);
        ImageView ivprev=(ImageView)findViewById(R.id.ivPrevious);
        ImageView ivnext=(ImageView)findViewById(R.id.ivNext);
        ImageView ivlast=(ImageView)findViewById(R.id.ivLast);


        Log.d( "demo: ","page");
        if(getIntent()!=null && getIntent().getExtras()!=null)
        {   Log.d( "demo: ","cametomovie");
            ArrayList<Movie> ListofMovies = new ArrayList<Movie>();
            ListofMovies= getIntent().getExtras().getParcelableArrayList("MovieList");
            Log.d( "demo: ","cametomovie");
            Log.d("demo",ListofMovies.size()+"");
           // ArrayList<Movie> MoviesbyYear = new ArrayList<Movie>();
            moviesbyYear=ListofMovies;
            Collections.sort(moviesbyYear, new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    //return (o1.year-o2.year);
                    return (o1.year-o2.year);
                }
            });
            //on loading -display the first record
            if(moviesbyYear.size()!=0)
            {
                index=0;
                tv_movie.setText(moviesbyYear.get(0).name.toString());
                tv_desc.setText(moviesbyYear.get(0).desc.toString());
                tv_genre.setText(moviesbyYear.get(0).genre.toString());
                tv_rating.setText(moviesbyYear.get(0).rating.toString()+"/5");
                String year=String.valueOf(moviesbyYear.get(0).year);
                tv_year.setText(year);
                Log.d("demo",moviesbyYear.get(0).year+"");
                tv_imdb.setText(moviesbyYear.get(0).imdb.toString());
            }


        }
        ivfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moviesbyYear.size()!=0)
                {
                    index=0;
                  tv_movie.setText(moviesbyYear.get(0).name.toString());
                  tv_desc.setText(moviesbyYear.get(0).desc.toString());
                  tv_genre.setText(moviesbyYear.get(0).genre.toString());
                  tv_rating.setText(moviesbyYear.get(0).rating.toString()+"/5");
                  tv_year.setText(moviesbyYear.get(0).year+"");
                  tv_imdb.setText(moviesbyYear.get(0).imdb.toString());
                }
                else
                {
                    Toast.makeText(MoviesByYear.this, "No Records found", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //last record
        ivlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moviesbyYear.size()!=0)
                {   int i =moviesbyYear.size();
                    index=i-1;
                    tv_movie.setText(moviesbyYear.get(i-1).name.toString());
                    tv_desc.setText(moviesbyYear.get(i-1).desc.toString());
                    tv_genre.setText(moviesbyYear.get(i-1).genre.toString());
                    tv_rating.setText(moviesbyYear.get(i-1).rating.toString()+"/5");
                    tv_year.setText(moviesbyYear.get(i-1).year+"");
                    tv_imdb.setText(moviesbyYear.get(i-1).imdb.toString());
                }
                else
                {
                    Toast.makeText(MoviesByYear.this, "This is the last record", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Previous record
        ivprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    index = index - 1;
                    if(index>=0 && index<moviesbyYear.size()) {

                        tv_movie.setText(moviesbyYear.get(index).name.toString());
                        tv_desc.setText(moviesbyYear.get(index).desc.toString());
                        tv_genre.setText(moviesbyYear.get(index).genre.toString());
                        tv_rating.setText(moviesbyYear.get(index).rating.toString() + "/5");
                        tv_year.setText(moviesbyYear.get(index).year + "");

                        tv_imdb.setText(moviesbyYear.get(index).imdb.toString());
                    }
                    else
                    {
                        Toast.makeText(MoviesByYear.this, "This is the first record", Toast.LENGTH_SHORT).show();
                    }

                }

        });
        //Next Record
        ivnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if(moviesbyYear.size()!=0 && moviesbyYear.size()>1)
               // {
                index = index + 1;
                    if(index<moviesbyYear.size()) {
                        tv_movie.setText(moviesbyYear.get(index).name.toString());
                        tv_desc.setText(moviesbyYear.get(index).desc.toString());
                        tv_genre.setText(moviesbyYear.get(index).genre.toString());
                        tv_rating.setText(moviesbyYear.get(index).rating.toString() + "/5");
                        tv_year.setText(moviesbyYear.get(index).year+"");
                        tv_imdb.setText(moviesbyYear.get(index).imdb.toString());
                    }
                    else if(index>=moviesbyYear.size())
                    {
                        Toast.makeText(MoviesByYear.this, "This is the last record", Toast.LENGTH_SHORT).show();
                    }
               // }
            }
        });
        //finishing the job
        findViewById(R.id.btnFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
