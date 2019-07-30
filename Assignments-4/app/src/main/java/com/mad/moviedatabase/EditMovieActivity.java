package com.mad.moviedatabase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.genre_array)){
                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View v = null;

                    if(position==0){
                        TextView tv = new TextView(getContext());
                        tv.setVisibility(v.GONE);
                        v = tv;
                    }else {
                        v = super.getDropDownView(position, null, parent);
                    }
                    parent.setVerticalScrollBarEnabled(false);
                    return v;
                }
            };

            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinner.setAdapter(spinnerArrayAdapter);

            spinner.setSelection(spinnerArrayAdapter.getPosition(movie.genre));
        }

        //seekbar

        seekRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
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

                Pattern reg1=Pattern.compile("^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$");

                // Pattern regex1= Patterns.WEB_URL;
                Matcher m=reg1.matcher(imdb.getText().toString().toLowerCase());

                if(year.getText().toString().isEmpty()||etName.getText().toString().isEmpty()||imdb.getText().toString().isEmpty()||etDesc.getText().toString().isEmpty()) {
                    Toast.makeText(EditMovieActivity.this, "Enter all the details", Toast.LENGTH_SHORT).show();
                }
                else if (year.getText().toString().length() <4 && year.getText().toString().length()>0){
                    Toast.makeText(EditMovieActivity.this, "Please enter 4 digits for year", Toast.LENGTH_SHORT).show();
                }
                else if(!m.matches())
                {
                    Toast.makeText(EditMovieActivity.this, "Enter Valid url(eg:https://www.google.com)", Toast.LENGTH_SHORT).show();
                }
                else {
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
