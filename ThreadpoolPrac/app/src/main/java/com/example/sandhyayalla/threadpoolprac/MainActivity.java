package com.example.sandhyayalla.threadpoolprac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ExecutorService threadpool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        threadpool = Executors.newFixedThreadPool(4);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadpool.execute(new Dowork());
            }
        });

    }

    class Dowork implements Runnable
    {
        @Override
        public void run() {

            Log.d("demo", "started work ");
            for(int i=0;i<1000000;i++)
            {
                for(int j=0;j<1000000000;j++)
                {}
            }
            Log.d("demo","Ended work");

        }
    }
}
