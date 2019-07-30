package com.example.bac.studentprofilebuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMyProfile extends AppCompatActivity {
    UserProfile user=new UserProfile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_profile);
        TextView tv_name=(TextView)findViewById(R.id.nameValue);
        TextView tv_idvalue=(TextView)findViewById(R.id.tv_IdValue);
        TextView tv_dep=(TextView)findViewById(R.id.deptVal);
        ImageView image=(ImageView)findViewById(R.id.pickedimage);


        user=(UserProfile) getIntent().getSerializableExtra(MainActivity.USER_KEY);
        //Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
        Log.d("DemoProfile", user.toString());

       if(getIntent()!=null && getIntent().getExtras()!=null)
       {
           user=(UserProfile) getIntent().getSerializableExtra(MainActivity.USER_KEY);
           Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();
           Log.d("DemoProfile", user.toString());
            tv_name.setText(user.fName+" "+user.Lname);
            tv_idvalue.setText(user.studentId+"");
            tv_dep.setText(user.dept);
            if(user.imagename==1)
            {
                image.setImageResource(R.drawable.avatar_f_1);
            }
           else if(user.imagename==2)
           {
               image.setImageResource(R.drawable.avatar_f_2);
           }
          else  if(user.imagename==3)
           {
               image.setImageResource(R.drawable.avatar_f_3);
           }
           else if(user.imagename==4)
           {
               image.setImageResource(R.drawable.avatar_m_1);
           }
          else  if(user.imagename==5)
           {
               image.setImageResource(R.drawable.avatar_m_2);
           }
           else if(user.imagename==6)
           {
               image.setImageResource(R.drawable.avatar_m_3);
           }

       }

        Button btn_edit=(Button)findViewById(R.id.btnEdit);
       btn_edit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(DisplayMyProfile.this,MainActivity.class);

               intent.putExtra(MainActivity.USER_KEY,user);
               Log.d("demouser",user.toString());
              // setResult(1);
               startActivity(intent);

           }
       });
    }
}
