package com.example.finalexam.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finalexam.R;

import java.util.List;

/**
 * Created by mshehab on 5/6/18.
 */

public class PersonsAdapter extends ArrayAdapter<Person>{

    public PersonsAdapter(Context context, int resource, List<Person> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){ //if no view to re-use then inflate a new one
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.person_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            viewHolder.textViewPriceBudget = (TextView) convertView.findViewById(R.id.textViewPriceBudget);
            viewHolder.textViewGifts = (TextView) convertView.findViewById(R.id.textViewGifts);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //set the data from the person object
        viewHolder.textViewName.setText(person.name);
            int totalbudget=person.totalBudget;
            int totalBought=person.totalBought;
            if(totalBought<totalbudget)
            {
                //viewHolder.textViewPriceBudget.setTextColor(getResources().getColor(R.color.red));
            }
            else if(totalBought==totalbudget)
            {

            }

        viewHolder.textViewPriceBudget.setText("$"+totalBought+"/$"+totalbudget); //setup the text and colors
            int giftcount=person.giftCount;
            String giftcnt=String.valueOf(giftcount);
        viewHolder.textViewGifts.setText(giftcnt+" Gifts Bought"); //setup the text


        return convertView;
    }

    //View Holder to cache the views
    private static class ViewHolder{
        TextView textViewName;
        TextView textViewPriceBudget;
        TextView textViewGifts;

    }
}
