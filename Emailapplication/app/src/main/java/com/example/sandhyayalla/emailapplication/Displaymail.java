package com.example.sandhyayalla.emailapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Displaymail extends AppCompatActivity {

    TextView tvname,tvsubject,tvmessage,tvcreated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaymail);
        setTitle("Displaymail");
            findViewById(R.id.btnfinish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        if(getIntent()!=null && getIntent().getExtras()!=null)
        {
            EmailItem item=(EmailItem)getIntent().getSerializableExtra("display");
            if(!item.id.isEmpty()) {
                tvname = (TextView) findViewById(R.id.tvdisplayname);
                tvname.setText(item.firstname + item.lastname);
                tvsubject = (TextView) findViewById(R.id.tvdisplaysubject);
                tvsubject.setText(item.subject);
                tvmessage = (TextView) findViewById(R.id.tvdisplaymessage);
                tvmessage.setText(item.message);
                tvcreated = (TextView) findViewById(R.id.tvdisplaycreatedtime);
                tvcreated.setText(item.Created_at);
            }

        }
    }
}
