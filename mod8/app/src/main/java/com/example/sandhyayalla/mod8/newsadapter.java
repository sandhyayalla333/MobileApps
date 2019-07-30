package com.example.sandhyayalla.mod8;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class newsadapter extends RecyclerView.Adapter<newsadapter.ViewHolder>{

    ArrayList<Source> mData;

    public newsadapter(ArrayList<Source> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.source_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Source source=mData.get(position);
        holder.tvid.setText(source.id.toString());
        holder.tvname.setText(source.name);
        //access to source
        holder.source=source;


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
   {
       TextView tvid;
       TextView tvname;
       Source source;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           this.source=source;
           tvid=(TextView)itemView.findViewById(R.id.tvid);
           tvname=(TextView)itemView.findViewById(R.id.tvname);

           itemView.findViewById(R.id.btmndelete).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Log.d("demo","buttonclicked");
               }
           });

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Log.d("demo","clicked"+source.id);
               }
           });

       }
   }


}
