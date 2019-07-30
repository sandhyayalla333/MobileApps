package com.example.sandhyayalla.todolistapplication;
//In ClassAssignment08
//Main Activity
//Group28-Naga Sandhyadevi,Ashika
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.ocpsoft.prettytime.PrettyTime;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;


public class MainActivity extends AppCompatActivity {

    EditText etname;
    Button btnAdd;
    String selectedpriority;
    int priority=0;
    int completed=0;
    DatabaseReference myRef;
    ArrayList<Todotask> tasklist=new ArrayList<>();
    ArrayList<Taskdo> todolist=null;
    ListView taskview;
    MessageAdapter adapter;
    String key;
    AlertDialog.Builder builder;
    int positiontodelete=0;
     Spinner spinner;
    ArrayAdapter<String> spinnerArrayAdapter;
    private Realm mRealm;
    int nextid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.black)));
        setTitle("TODO List");
        etname=(EditText)findViewById(R.id.et_addtext);
        btnAdd=(Button)findViewById(R.id.btnadd);
        //realm
        Realm.init(this);


        mRealm=Realm.getDefaultInstance();



        RealmResults<Taskdo> list1=mRealm.where(Taskdo.class).findAll();
       List<Taskdo> arraylist1 = mRealm.copyFromRealm(list1);
       todolist=new ArrayList<Taskdo>(arraylist1);
        Log.d("demo","realm"
                +arraylist1.toString() + "size"+arraylist1.size());


        //end realm



        myRef = FirebaseDatabase.getInstance().getReference().child("tasks");
        taskview = (ListView) findViewById(R.id.listview);
      //  adapter = new MessageAdapter(MainActivity.this, R.layout.activity_textitem, tasklist);
        adapter = new MessageAdapter(MainActivity.this, R.layout.activity_textitem, todolist);
        taskview.setAdapter(adapter);
        //myRef.child("tasks").


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etname.getText().toString().isEmpty())
                {
                    etname.setError("Please enter name");
                }
                else if(priority==0)
                {
                    Toast.makeText(MainActivity.this, "Please select Priority", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => "+c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    //realm
                    mRealm.beginTransaction();
                    Taskdo todo = mRealm.createObject(Taskdo.class);

                    Number currentid=mRealm.where(Taskdo.class).max("id");

                    if(currentid==null)
                    {
                        nextid=1;
                        Log.d("demo","id for firsat"+nextid);
                    }
                    else
                    {
                        nextid=currentid.intValue()+1;
                    }
                    todo.setId(nextid);
                   todo.setName(etname.getText().toString());
                   todo.setDatetime(formattedDate);
                   todo.setPriority(priority);
                   todo.setCompleted(completed);

                    mRealm.commitTransaction();


                    RealmResults<Taskdo> list1=mRealm.where(Taskdo.class).findAll();
                    List<Taskdo> arraylist1 = mRealm.copyFromRealm(list1);
                    todolist=new ArrayList<Taskdo>(arraylist1);
                    adapter = new MessageAdapter(MainActivity.this, R.layout.activity_textitem, todolist);
                    taskview.setAdapter(adapter);

                    //end realm

                    etname.setText("");


                }
            }
        });

        //Spinner code

        //Spinner
       spinner = findViewById(R.id.spinner);

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.genre_array)){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = null;

                if(position==0){
                    TextView tv = new TextView(getContext());
                    tv.setVisibility(v.GONE);
                    v = tv;
                }else {
                    v = super.getDropDownView(position, null, parent);
                }
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int indexSelected, long l) {
                if(indexSelected!=0){
                   // movie.genre = String.valueOf(adapterView.getItemAtPosition(indexSelected));
                     selectedpriority=String.valueOf(adapterView.getItemAtPosition(indexSelected));
                     Log.d("demo","priority"+selectedpriority);
                     if(selectedpriority.equals("High"))
                     {
                         priority=1;
                     }
                     else if(selectedpriority.equals("Medium"))
                     {
                         priority=2;
                     }
                     else if(selectedpriority.equals("Low"))
                     {
                         priority=3;
                     }
                }else {
                   // movie.genre = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Spinner end

        //list of task



        taskview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    positiontodelete=i;
                //alert dialog
                builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to delete?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        dialog.dismiss();

                        Taskdo task = todolist.get(positiontodelete);

                            mRealm.beginTransaction();
                            RealmResults<Taskdo> books = mRealm.where(Taskdo.class).equalTo("id",task.getId()).findAll();
                           // Log.d("demo","books to detel",books.toString())
                            books.deleteAllFromRealm();
                            mRealm.commitTransaction();

                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });


                //end alert dialog


                AlertDialog alert = builder.create();
                alert.show();


                return false;
            }
        });




    }

    //
    //Message Adapter
    public class MessageAdapter extends ArrayAdapter<Taskdo> {
        public MessageAdapter(@NonNull Context context, int resource, @NonNull List<Taskdo> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final Taskdo task1 = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_textitem, parent, false);
            }
            TextView tv_text = (TextView) convertView.findViewById(R.id.tvname);
            tv_text.setText(task1.getName().toString());
            TextView tv_priority = (TextView) convertView.findViewById(R.id.tvpriority);
            final CheckBox checkBox1=(CheckBox)convertView.findViewById(R.id.checkBox);
            TextView tv_time = (TextView) convertView.findViewById(R.id.tvtime);
            String formattedtime = formatdate(task1.getDatetime());
            tv_time.setText(formattedtime);

            if(task1.getPriority()==1)
            {
               tv_priority.setText("High Priority");
            }
            else if(task1.getPriority()==2)
            {
                tv_priority.setText("Medium Priority");
            }
            else if(task1.getPriority()==3)
            {
                tv_priority.setText("Low Priority");
            }

            checkBox1.setTag(position);
            checkBox1.setOnCheckedChangeListener(null);
            if(task1.getCompleted()==1)
            {
                Log.d("demo","completed as 1");
                checkBox1.setChecked(true);
            }
            else
            {
                checkBox1.setChecked(false);
            }
           //checkBox1.OnCheckedChangeListener

            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d("demo","checked"+b);
                    int i=(int)checkBox1.getTag();
                    Log.d("demo","position"+i);

                    Taskdo todotask=getItem(position);

                    Log.d("demo","checkedtask"+todotask.toString());
                    if(todotask.getCompleted()==0 && b==true)
                    {

                       // todotask.completed=1;
                        todotask.setCompleted(1);


                        adapter.notifyDataSetChanged();
                    }
                    else if(task1.getCompleted()==1 && b==false)
                    {
                       // todotask.completed=0;
                        todotask.setCompleted(0);

                        adapter.notifyDataSetChanged();
                    }
                    Log.d("demo","todotask"+todotask.toString());




                }
            });

           // checkBox1.setOnCheckedChangeListener(new OnCh);








            return convertView;
        }
    }

    //end message adapter

    //format date
    public static String formatdate(String dateString) {

        // String dateString="2015-09-25 15:00:47";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

        Date convertedDate = new Date();
        //TimeZone estTime = TimeZone.getTimeZone(convertedDate);

        try {

            convertedDate = dateFormat.parse(dateString);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Locale locale=Locale.US;

        Calendar cal = Calendar.getInstance();
        cal.setTime(convertedDate);
        cal.add(Calendar.HOUR, -4);  // API response is in GMT hence converting it to EDT

        Date actualDate = cal.getTime();

        PrettyTime p = new PrettyTime();

        String datetime = p.format(actualDate);
        //Log.d("demo", "created" + datetime);


        return datetime;
    }
    //end format date

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ArrayList<Todotask> pendinglist=new ArrayList<Todotask>();
        ArrayList<Todotask> completedlist=new ArrayList<Todotask>();

        for(Todotask t1:tasklist)
        {
            if(t1.completed==0)
            {
                pendinglist.add(t1);
            }
            else if(t1.completed==1)
            {
                completedlist.add(t1);
            }
        }

        if(item.getItemId() == R.id.all){
           // Toast.makeText(MainActivity.this, "You clicked: Show All", Toast.LENGTH_SHORT).show();
            adapter = new MessageAdapter(MainActivity.this, R.layout.activity_textitem, todolist);
            taskview.setAdapter(adapter);
        }else if(item.getItemId() == R.id.completed){
            //Toast.makeText(MainActivity.this, "You clicked: Show Completed", Toast.LENGTH_SHORT).show();
            adapter = new MessageAdapter(MainActivity.this, R.layout.activity_textitem, todolist);
            taskview.setAdapter(adapter);
        }else if(item.getItemId() == R.id.pending){
            //Toast.makeText(MainActivity.this, "You clicked: Show Pending", Toast.LENGTH_SHORT).show();
            adapter = new MessageAdapter(MainActivity.this, R.layout.activity_textitem, todolist);
            taskview.setAdapter(adapter);
        }else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

