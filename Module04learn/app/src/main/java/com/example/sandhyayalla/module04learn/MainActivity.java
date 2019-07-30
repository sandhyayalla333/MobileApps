package com.example.sandhyayalla.module04learn;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    Handler handler=new Handler();
    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Updating Progress");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case DoWork.Start_Status:
                        progressDialog.setProgress(0);
                        progressDialog.show();
                        break;

                        case DoWork.Progress_Status:
                            progressDialog.setProgress(msg.getData().getInt(DoWork.Progress_key));
                            progressDialog.show();
                            break;
                            case DoWork.Stop_Status:
                                progressDialog.dismiss();
                                break;
                }
                return false;
            }
        });
       new Thread(new DoWork()).start();
    }
     class DoWork implements  Runnable
    {
        final static  String Progress_key="Progresskey";
        final  static int Start_Status=0x00;
        final static  int Stop_Status=0x01;
        final static int Progress_Status=0x02;
        Message startMessage=new Message();


        @Override
        public void run() {
                startMessage.what=Start_Status;
            handler.sendMessage(startMessage);
            for(int i=0;i<100;i++) {
                for(int j=0;j<10000000;j++)
                {

                }
                Bundle bundle=new Bundle();
                bundle.putInt(Progress_key,(Integer)i);

                Message progressMessage=new Message();
                progressMessage.what=Progress_Status;
                progressMessage.setData(bundle);
                handler.sendMessage(progressMessage);
            }
            Message stopMessage=new Message();
            stopMessage.what=Stop_Status;
            handler.sendMessage(stopMessage);
            }
        }
    }

