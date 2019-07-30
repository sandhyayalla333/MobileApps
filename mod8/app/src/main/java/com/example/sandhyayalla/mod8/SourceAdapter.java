package com.example.sandhyayalla.mod8;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SourceAdapter extends ArrayAdapter<Source> {

    public SourceAdapter(@NonNull Context context, int resource, @NonNull List<Source> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.d("demo","we are here at getview");

        Source source=getItem(position);
       if(convertView==null)
       {
           convertView= LayoutInflater.from(getContext()).inflate(R.layout.source_item,parent,false);
       }
        TextView tv_id=(TextView)convertView.findViewById(R.id.tvid);
       TextView tv_name=(TextView) convertView.findViewById(R.id.tvname);
       tv_id.setText(source.id.toString());
       tv_name.setText(source.name.toString());
        Log.d("demo","Source" + source.toString());
        //Log.d("demo","id : ",);
       return  convertView;
    }
}
