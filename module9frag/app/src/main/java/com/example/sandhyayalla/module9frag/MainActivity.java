package com.example.sandhyayalla.module9frag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v4.app.Fragment;

public class MainActivity extends AppCompatActivity implements aFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.container,new aFragment(),"first").commit();
    }

    @Override
    public void gotoNextfragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new secondfragment(),"second").addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()>0)
        {
            getSupportFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
