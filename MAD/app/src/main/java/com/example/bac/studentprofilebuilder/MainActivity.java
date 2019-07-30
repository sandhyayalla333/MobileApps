package com.example.bac.studentprofilebuilder;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    final static int Req_code=100;
    final static int REQ_CODE_DISPLAY=101;
    public String dep;
    public int imagenumber=0;
    // final String Image_key="";
    static String USER_KEY = "USER";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv_image=(ImageView)findViewById(R.id.iv_selectImage);
        ImageView iv=(ImageView)findViewById(R.id.iv_selectImage);
        UserProfile user1=new UserProfile();
        EditText et_name=(EditText)findViewById(R.id.et_name);
        EditText et_lastname=(EditText)findViewById(R.id.et_lastName);
        EditText et_id=(EditText)findViewById(R.id.et_Id);
        RadioGroup rbDept1 = (RadioGroup) findViewById(R.id.rgDept);
        RadioButton rb_1=(RadioButton)findViewById(R.id.rb_1) ;
        RadioButton rb_2=(RadioButton)findViewById(R.id.rb_2) ;
        RadioButton rb_3=(RadioButton)findViewById(R.id.rb_3) ;
        RadioButton rb_4=(RadioButton)findViewById(R.id.rb_4) ;

        if(getIntent().getExtras()!=null)
        {
            user1= (UserProfile)getIntent().getExtras().getSerializable(USER_KEY);
            Log.d("demo",user1.imagename+": ");
            int j=user1.imagename;
            if(j==1)
            {
                Log.d("demo","imagedisplayed");
                iv_image.setImageResource(R.drawable.avatar_f_1);
            }
            else  if(user1.imagename==2)
            {
                iv_image.setImageResource(R.drawable.avatar_f_2);
            }
            else  if(user1.imagename==3)
            {
                iv_image.setImageResource(R.drawable.avatar_f_3);
            }
            else  if(user1.imagename==4)
            {
                iv_image.setImageResource(R.drawable.avatar_m_1);
            }
            else  if(user1.imagename==5)
            {
                iv_image.setImageResource(R.drawable.avatar_m_2);
            }
            else  if(user1.imagename==6)
            {
                iv_image.setImageResource(R.drawable.avatar_m_3);
            }
            et_name.setText(user1.fName);
            et_lastname.setText(user1.Lname);
            long id=(long) user1.studentId;
            et_id.setText(id+"");
            if(user1.dept=="CS")
            {
                rb_1.setChecked(true);
            }
            else if(user1.dept=="SIS")
            {
                rb_2.setChecked(true);
            }
            else
            if(user1.dept=="BIO")
            {
                rb_3.setChecked(true);
            }
            else
            {
                rb_4.setChecked(true);
            }


        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _intent=new Intent(MainActivity.this,selectimageActivity.class);
                startActivityForResult(_intent,Req_code);

            }

        });

// save bt
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etFName = findViewById(R.id.et_name);
                EditText etLName = findViewById(R.id.et_lastName);
                EditText etId = findViewById(R.id.et_Id);

                RadioGroup rbDept = (RadioGroup) findViewById(R.id.rgDept);
                final RadioButton rb_1=(RadioButton)findViewById(R.id.rb_1) ;

                ImageView image1=(ImageView)findViewById(R.id.iv_selectImage);
                String dep = ((RadioButton)findViewById(rbDept.getCheckedRadioButtonId())).getText().toString();
                Log.d("demo", dep+"");
                rbDept.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rb=    (RadioButton)findViewById(checkedId);
                        Log.d("demo", checkedId+"");


                    }
                });

                if (etFName.getText()== null){
                    etFName.setError("Enter first name");
                }else if (etLName.getText()== null){
                    etLName.setError("Enter last name");
                }else if (etId.getText()== null || etId.getText().length()!=9){
                    etId.setError("Enter 9 digit ID");
                }else {
                    //change class name
                    Intent intentForDisplay = new Intent(MainActivity.this, DisplayMyProfile.class);

                    UserProfile user = new UserProfile(etFName.getText().toString(), etLName.getText().toString(),dep, Long.parseLong(String.valueOf(etId.getText())),imagenumber);
                    Log.d("demo", user.toString());
                    intentForDisplay.putExtra(USER_KEY, user);
                    //startActivityForResult(intentForDisplay, REQ_CODE_DISPLAY);
                    startActivity(intentForDisplay);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Req_code) {
            imagenumber=resultCode;
            ImageView pickimage=(ImageView)findViewById(R.id.iv_selectImage);
            if(resultCode ==1){
                pickimage.setImageResource(R.drawable.avatar_f_1);
            }
            else if(resultCode ==2){
                pickimage.setImageResource(R.drawable.avatar_f_2);
            }
            else if(resultCode ==3){
                pickimage.setImageResource(R.drawable.avatar_f_3);
            }
            else if(resultCode ==4){
                pickimage.setImageResource(R.drawable.avatar_m_1);
            }
            else if(resultCode ==5){
                pickimage.setImageResource(R.drawable.avatar_m_2);
            }
            else if(resultCode ==6){
                pickimage.setImageResource(R.drawable.avatar_m_3);
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }
}
