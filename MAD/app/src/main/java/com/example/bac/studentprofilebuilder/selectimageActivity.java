package com.example.bac.studentprofilebuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class selectimageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectimage);
        final ImageView avatarf1=(ImageView)findViewById(R.id.imageView);
        final ImageView avatarf2=(ImageView)findViewById(R.id.imageView2);
        final ImageView avatarf3=(ImageView)findViewById(R.id.imageView3);
        final ImageView avatarm1=(ImageView)findViewById(R.id.imageView4);
        final ImageView avatarm2=(ImageView)findViewById(R.id.imageView5);
        final ImageView avatarm3=(ImageView)findViewById(R.id.imageView6);
        final String Imagename = String.valueOf(avatarf1.getTag());
        final Intent intent=new Intent();
        avatarf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             // intent.putExtra("Imagekey",Imagename);
               // intent.putExtra("Imagekey",1);
                setResult(1);
                Log.d("demoimage", "1 ");
                finish();



            }
        });
        //imageview2
        avatarf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intent.putExtra("Imagekey",Imagename);
               // intent.putExtra("Imagekey",2);
                setResult(2);
                finish();

            }
        });
        avatarf3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intent.putExtra("Imagekey",Imagename);
                //intent.putExtra("Imagekey",1);
                setResult(3);
                finish();

            }
        });
        avatarm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intent.putExtra("Imagekey",Imagename);
                intent.putExtra("Imagekey",1);
                setResult(4);
                finish();

            }
        });

        avatarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intent.putExtra("Imagekey",Imagename);
                //intent.putExtra("Imagekey",1);
                setResult(5);
                finish();

            }
        });
        avatarm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intent.putExtra("Imagekey",Imagename);
                //intent.putExtra("Imagekey",1);
                setResult(6);
                finish();

            }
        });

    }
}
