package com.example.sandhyayalla.studentprofile;
//Inclass Assignement 07
//Group28- naga Sandhya yalla,Ashika Shivakote Annegowda

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements selectimagefrag.OnFragmentInteractionListener,myprofile.OnFragmentInteractionListener,DisplayProfileFragment.OnFragmentInteractionListener {
    ImageView iv;
    EditText et_name,et_lastname,et_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportFragmentManager().beginTransaction().add(R.id.container,new myprofile(),"myprofile").addToBackStack(null).commit();

    }

    @Override
    public void updateimage(int i) {
        myprofile myprofilefragment=(myprofile)getSupportFragmentManager().findFragmentByTag("myprofile");
        Bundle args = new Bundle();
        args.putInt("imageNumber", i);
        myprofilefragment.setArguments(args);

       // myprofilefragment.updateimagetoprofile(i);

    }

    @Override
    public void loadimagefragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new selectimagefrag(),"selectimage").addToBackStack(null).commit();
    }

    @Override
    public void onsaveclicked(Bundle bundle) {

        DisplayProfileFragment displayFrag = new DisplayProfileFragment();
        displayFrag.setArguments(bundle);
         getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, displayFrag, "tag_displayFragment")
                            .addToBackStack("tag_profileFragment").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void Oneditclicked(Bundle bundle) {

        myprofile myprofilefragment=(myprofile)getSupportFragmentManager().findFragmentByTag("myprofile");

        myprofilefragment.setArguments(bundle);
    }
}
