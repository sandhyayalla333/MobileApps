package com.example.sandhyayalla.mod8;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private  RecyclerView.LayoutManager mlayoutmanager;
    private RecyclerView.Adapter madapter;
    private  RecyclerView mrecyclerView;
    ArrayList<Source> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        if(getIntent()!=null) {

            data=(ArrayList<Source>) getIntent().getBundleExtra("bundle").getSerializable("sources");
            Log.d("demo","datacount"+data.size());
            mrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mrecyclerView.setHasFixedSize(true);
            //use linear layout manager
            mlayoutmanager = new LinearLayoutManager(this);
            mrecyclerView.setLayoutManager(mlayoutmanager);
            madapter = new newsadapter(data);
            mrecyclerView.setAdapter(madapter);
        }



    }
}
