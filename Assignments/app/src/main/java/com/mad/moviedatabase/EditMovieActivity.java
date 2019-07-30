package com.mad.moviedatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditMovieActivity extends AppCompatActivity {

    Movie movie = new Movie();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        setTitle("Edit Movie");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.darkGray));

        final EditText etName = findViewById(R.id.etName);
        final EditText etDesc = findViewById(R.id.etMultiline);
        final TextView tvRating = findViewById(R.id.tvSeekValue);
        final EditText year = findViewById(R.id.etYear);
        final EditText imdb = findViewById(R.id.etImdb);
        SeekBar seekRating = findViewById(R.id.sbRating);
        Spinner spinner = findViewById(R.id.spinner);

        if(getIntent().getExtras()!=null){
            movie = getIntent().getExtras().getParcelable(MainActivity.MOVIE_KEY);

            etName.setText(movie.name);
            etDesc.setText(movie.desc);
            tvRating.setText(movie.rating);
            year.setText(String.valueOf(movie.year));
            imdb.setText(movie.imdb);
            seekRating.setProgress(Integer.parseInt(movie.rating));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genre_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setSelection(adapter.getPosition(movie.genre));

        }

        //seekbar

        seekRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                //Log.d("Demo", String.valueOf(progress));
                tvRating.setText(String.valueOf(progress));
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

        //save changes
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(year.getText().toString().isEmpty()||etName.getText().toString().isEmpty()||imdb.getText().toString().isEmpty()||etDesc.getText().toString().isEmpty()) {
                    Toast.makeText(EditMovieActivity.this, "Enter all the details", Toast.LENGTH_SHORT).show();
                }else {
                    movie.name = etName.getText().toString();
                    movie.desc = etDesc.getText().toString();
                    movie.rating = tvRating.getText().toString();
                    movie.year = Integer.parseInt(year.getText().toString());
                    movie.imdb = imdb.getText().toString();
                    Intent saveIntent = new Intent(EditMovieActivity.this, MainActivity.class);
                    saveIntent.putExtra(MainActivity.MOVIE_KEY, movie);
                    setResult(RESULT_OK, saveIntent);

                    finish();
                }
            }
        });
    }
}
