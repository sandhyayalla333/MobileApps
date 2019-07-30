package com.example.sandhyayalla.datafromfragment;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements aFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.linearcontainer,new aFragment(),"first").addToBackStack(null).commit();
        RadioGroup rg=(RadioGroup)findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedid) {
                aFragment f=(aFragment)getSupportFragmentManager().findFragmentByTag("first");

                if(checkedid==R.id.rbred)
                {
                    f.changecolor(Color.RED);
                }
                else if (checkedid==R.id.rbblue)
                {
                    f.changecolor(Color.BLUE);
                }
                else if(checkedid==R.id.rbgreen)
                {
                    f.changecolor(Color.GREEN);
                }
            }
        });
    }

    @Override
    public void OntextChanged(String text) {

       TextView tvname=(TextView) findViewById(R.id.txtdisplay);
        Log.d("demo","textinactivity"+text);
       tvname.setText(text);

    }
}
