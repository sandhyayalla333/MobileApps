package com.example.sandhyayalla.hwtodolist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class todofragment extends Fragment {

    private  ListView todolistView;
    private  ArrayList<TodoTask> tasklist=new ArrayList<>();
    private MessageAdapter adapter;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("tasks");


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public todofragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment todofragment.
     */
    // TODO: Rename and change types and number of parameters
    public static todofragment newInstance(String param1, String param2) {
        todofragment fragment = new todofragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.todo_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        todolistView=(ListView)view.findViewById(R.id.todolist) ;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tasklist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TodoTask task = snapshot.getValue(TodoTask.class);
                    tasklist.add(task);
                    adapter = new MessageAdapter(getContext(), R.layout.item_todolist, tasklist);
                    todolistView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    //Message Adapter
    public class MessageAdapter extends ArrayAdapter<TodoTask> {
        public MessageAdapter(@NonNull Context context, int resource, @NonNull List<TodoTask> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final TodoTask task1 = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todolist, parent, false);
            }
            TextView tv_text = (TextView) convertView.findViewById(R.id.tvname);
            tv_text.setText(task1.name.toString());

            final ImageView ivprogress=(ImageView) convertView.findViewById(R.id.ivprogress);
            TextView tv_time = (TextView) convertView.findViewById(R.id.tvtime);
            String formattedtime = task1.datetime;
            tv_time.setText(formattedtime);
            ivprogress.setTag(position);
            ivprogress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i=(int)ivprogress.getTag();
                    Log.d("demo","position"+i);

                    TodoTask todotask=getItem(position);
                    todotask.completed=1;
                    myRef.child(todotask.key).setValue(todotask).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.d("demo","changed status from todo to doing");

                        }
                    });
                    adapter.notifyDataSetChanged();

                }
            });




            return convertView;
        }
    }

    //end message adapter

}
