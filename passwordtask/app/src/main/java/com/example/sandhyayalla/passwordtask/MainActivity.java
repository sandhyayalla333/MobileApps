package com.example.sandhyayalla.passwordtask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.Log;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MainActivity extends AppCompatActivity {

    TextView tv_pwdcount;
    TextView tv_pwdlength;
    TextView tv_result;
    SeekBar sb_pwdcount;
    SeekBar sb_pwdlength;
    ExecutorService threadpool;
    Button btn_Threadpool;
    Button btn_asynctask;
    Handler handler;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    ProgressDialog asynprogressDialog;
    AlertDialog.Builder alertbuilder;
    String[] pwdlist;
    String[] Asycpwdlist;
    int count=1;
    int length=8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_pwdcount=(TextView)findViewById(R.id.tvdisplaycount);
        tv_pwdlength=(TextView)findViewById(R.id.tvdisplaylength);
        tv_result=(TextView)findViewById(R.id.tvresult);
        sb_pwdcount=(SeekBar)findViewById(R.id.sbpwdCount);
        sb_pwdlength=(SeekBar)findViewById(R.id.sbpwdlength);
        btn_Threadpool=(Button)findViewById(R.id.btnThreadPool) ;
        btn_asynctask=(Button)findViewById(R.id.btnasync);

        String mincount=String.valueOf(1);
        tv_pwdcount.setText(mincount);
        tv_pwdlength.setText("8");

        sb_pwdcount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sb_pwdcount.setProgress(progress);
                tv_pwdcount.setText(progress+"");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_pwdlength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sb_pwdlength.setProgress(progress);
                tv_pwdlength.setText(String.valueOf(progress));
                Log.d("demo",progress+"lengthprogress");
               // length=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        count=Integer.parseInt(tv_pwdcount.getText().toString());
        length=Integer.parseInt(tv_pwdlength.getText().toString());
        // handler class
        handler =new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what)
                {
                    case Dopassword.Start_status:
                        progressDialog=new ProgressDialog(MainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setMax(Integer.parseInt(tv_pwdcount.getText().toString()));
                        progressDialog.show();


                        break;
                        case Dopassword.Progress_Status:
                            Log.d("demo",msg.getData().getInt(Dopassword.progress_key)+"");
                            Log.d("demo",Dopassword.Progress_Status+"");

                            //progressDialog.setProgress(msg.getData().getBundle().getInt(Dopassword.progress_key));
                            progressDialog.setProgress(msg.getData().getInt(Dopassword.progress_key));
                            progressDialog.show();

                            break;
                            case Dopassword.Stop_status:
                                //progressBar.setVisibility(ProgressBar.INVISIBLE);
                                progressDialog.dismiss();
                                alertbuilder=new AlertDialog.Builder(MainActivity.this);
                                alertbuilder.setTitle("select");
                                pwdlist=msg.getData().getStringArray(Dopassword.passwordllist_key);
                                //Log.d("demo",pwdlist[0].toString()+"1stone"+pwdlist[4]+"lastone");



                                break;
                }
                return false;
            }
        });


        //Thread pool on button click
        btn_Threadpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=Integer.parseInt(tv_pwdcount.getText().toString());
                length=Integer.parseInt(tv_pwdlength.getText().toString());
                threadpool= Executors.newFixedThreadPool(2);
                threadpool.execute(new Dopassword(length,count));

            }
        });
        //asyn taskcall
        btn_asynctask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=Integer.parseInt(tv_pwdcount.getText().toString());
                length=Integer.parseInt(tv_pwdlength.getText().toString());
                new DoPasswordasync().execute(count,length);
            }
        });

    }
    //async task
    class DoPasswordasync extends AsyncTask<Integer,Integer,String[]>
    {
        int pwdcount=0;
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            asynprogressDialog=new ProgressDialog(MainActivity.this);
            asynprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asynprogressDialog.setMax(count);
            asynprogressDialog.show();
        }

        @Override
        protected void onPostExecute(String[] s) {
            asynprogressDialog.dismiss();

            for(String str:s)
            {
                Log.d("demo","THe password are " + str);
            }
            Asycpwdlist=s;
            alertbuilder=new AlertDialog.Builder(MainActivity.this);
            alertbuilder.setTitle("select");
            alertbuilder.setSingleChoiceItems(Asycpwdlist, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    tv_result.setText(Asycpwdlist[i]);
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialogaync=alertbuilder.create();
            alertDialogaync.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("demo","progress"+values[0]);
                asynprogressDialog.setProgress(values[0]);
        }

        @Override
        protected String[] doInBackground(Integer... integers) {
                String[] pwdoutput=new String[count];
            // Util util=new Util();
            for(int i=0;i<count;i++) {
                String output = Util.getPassword(length);
                pwdoutput[i]=output;
                int j=i+1;
                publishProgress(j);

            }



            return pwdoutput;
        }
    }
//Thread pool
    class Dopassword implements  Runnable
    {
        int length=0;
        int count=0;


        final static int Start_status=100;
        final  static int Stop_status=200;
        final  static int Progress_Status=300;
        final  static String progress_key="Progresskey";
        final static String passwordllist_key="Passwordlist";

        public  Dopassword(int length,int count)
        {
            this.length=length;
            this.count=count;
        }
        @Override
        public void run() {

            String[] passwordlist=new String[count];
            Message startmessage=new Message();
            startmessage.what=Start_status;
            handler.sendMessage(startmessage);
            Log.d("demo",count+"");
            Log.d("demo",length+"");


           // Util util=new Util();
            for(int i=0;i<count;i++) {
             String output=Util.getPassword(length);
             Log.d("demo","password is"+output+"");
             passwordlist[i]=output;
                Log.d("demo", "password in list "+passwordlist[i-1]);
             Message progressmessge=new Message();
             Bundle bundle=new Bundle();
             int j=i+1;
             bundle.putInt(progress_key,(Integer)i);
             progressmessge.setData(bundle);
             progressmessge.what=Progress_Status;
             handler.sendMessage(progressmessge);

            }
            Message stopmessage=new Message();
            stopmessage.what=Stop_status;
            Bundle bundle1=new Bundle();
            //bundle1.putCharSequenceArray(passwordllist_key,passwordlist);
            bundle1.putStringArray(passwordllist_key,passwordlist);
            stopmessage.setData(bundle1);
            handler.sendMessage(stopmessage);

        }
    }
}
