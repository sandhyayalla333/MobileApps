package com.mad.moviedatabase;

import android.content.Intent;
import android.service.autofill.RegexValidator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
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
        final Spinner spinner = findViewById(R.id.spinner);

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int indexSelected, long l) {
                if(indexSelected!=0){
                    movie.genre = String.valueOf(adapterView.getItemAtPosition(indexSelected));
                }else {
                    movie.genre = "";
                }

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
                //Pattern reg=Pattern.compile("(([\\\\w]+:)?//)?(([\\\\d\\\\w]|%[a-fA-f\\\\d]{2,2})+(:([\\\\d\\\\w]|%[a-fA-f\\\\d]{2,2})+)?@)?([\\\\d\\\\w][-\\\\d\\\\w]{0,253}[\\\\d\\\\w]\\\\.)+[\\\\w]{2,4}(:[\\\\d]+)?(/([-+_~.\\\\d\\\\w]|%[a-fA-f\\\\d]{2,2})*)*(\\\\?(&?([-+_~.\\\\d\\\\w]|%[a-fA-f\\\\d]{2,2})=?)*)?(#([-+_~.\\\\d\\\\w]|%[a-fA-f\\\\d]{2,2})*)?");
                Pattern reg1=Pattern.compile("^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$");
                //Pattern regex1= Patterns.WEB_URL;
                Matcher m=reg1.matcher(imdb.getText().toString().toLowerCase());



                Log.d("Demo", movie.genre);

                if(year.getText().toString().isEmpty() || etName.getText().toString().isEmpty()||imdb.getText().toString().isEmpty()||etDesc.getText().toString().isEmpty()) {
                    Toast.makeText(AddMovieActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }else if (year.getText().toString().length() <4 && year.getText().toString().length()>0){
                    Toast.makeText(AddMovieActivity.this, "Please enter 4 digits for year", Toast.LENGTH_SHORT).show();
                }else if (movie.genre==""){
                    Toast.makeText(AddMovieActivity.this, "Please select the Genre", Toast.LENGTH_SHORT).show();
                }
                else if(!m.matches())
                {
                    Toast.makeText(AddMovieActivity.this, "Enter Valid url(eg:https://www.google.com", Toast.LENGTH_SHORT).show();
                }
                else {
                    movie.name = etName.getText().toString();
                    movie.desc = etDesc.getText().toString();
                    movie.rating = tvRating.getText().toString();
                    movie.year = Integer.parseInt(year.getText().toString());
                    movie.imdb = imdb.getText().toString();

                    Intent saveMovieIntent = new Intent(AddMovieActivity.this, MainActivity.class);
                    saveMovieIntent.putExtra(MainActivity.MOVIE_KEY, movie);
                    setResult(RESULT_OK, saveMovieIntent);

                    finish();
                }
            }
        });




    }
}
