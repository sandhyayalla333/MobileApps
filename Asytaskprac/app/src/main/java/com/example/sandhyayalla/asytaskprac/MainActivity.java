package com.example.sandhyayalla.asytaskprac;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DoWorkAsync().execute(1000000);
    }

    class DoWorkAsync extends AsyncTask<Integer,Integer,Double>
    {

        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Double aDouble) {
            Log.d("demo","The final value is "+aDouble);
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected Double doInBackground(Integer... integers) {

            Random RAND=new Random();
            int count =0;
            Double sum=0.0;
            for(int i=0;i<100;i++)
            {
                for(int j=0;j<integers[0];j++)
                {
                    count++;
                    sum=RAND.nextDouble()+sum;
                }
                publishProgress(i);
            }


            return sum/count;
        }
    }
}
