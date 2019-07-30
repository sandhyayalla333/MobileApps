package com.mad.moviedatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddMovieActivity extends AppCompatActivity {

    Movie movie = new Movie();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        setTitle("Add Movie");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.darkGray));

        //seekbar
        SeekBar seekRating = findViewById(R.id.sbRating);

        final TextView sbValue = findViewById(R.id.tvSeekValue);
        seekRating.setProgress(0);


        seekRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //Log.d("Demo", String.valueOf(progress));
                sbValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //seekbar end

        //Spinner
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setPrompt("Select");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int indexSelected, long l) {

                    movie.genre = String.valueOf(adapterView.getItemAtPosition(indexSelected));
                    Log.d("Demo", String.valueOf(adapterView.getItemAtPosition(indexSelected)));



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Spinner end

        //Add  movie details to Movie Object and pass it to main activity
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText etName = findViewById(R.id.etName);
                EditText etDesc = findViewById(R.id.etMultiline);
                TextView tvRating = findViewById(R.id.tvSeekValue);
                EditText year = findViewById(R.id.etYear);
                EditText imdb = findViewById(R.id.etImdb);
                if(year.getText().toString().isEmpty()||etName.getText().toString().isEmpty()||imdb.getText().toString().isEmpty()||etDesc.getText().toString().isEmpty()) {
                    Toast.makeText(AddMovieActivity.this, "Enter all the details", Toast.LENGTH_SHORT).show();
                }
                else {


                    movie.name = etName.getText().toString();
                    movie.desc = etDesc.getText().toString();
                    movie.rating = tvRating.getText().toString();
                    movie.year = Integer.parseInt(year.getText().toString());
                    Log.d("demo",movie.year+"");
                    movie.imdb = imdb.getText().toString();
                    Intent saveMovieIntent = new Intent();
                    saveMovieIntent.putExtra(MainActivity.MOVIE_KEY, movie);
                    setResult(1, saveMovieIntent);

                    finish();
                }

                //startActivity(saveMovieIntent);
            }
        });




    }
}
