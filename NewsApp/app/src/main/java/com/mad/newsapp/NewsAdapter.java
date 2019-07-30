package com.mad.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewAuthor = convertView.findViewById(R.id.textViewAuthor);
            viewHolder.textViewTitle = convertView.findViewById(R.id.textViewTitle);
            viewHolder.textViewPublishedAt = convertView.findViewById(R.id.textViewPublishedAt);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(news.author.equals("null") || news.author.equals(""))
            viewHolder.textViewAuthor.setText("No Author");
        else
            viewHolder.textViewAuthor.setText(news.author);

        viewHolder.textViewTitle.setText(news.title);
        try {
            viewHolder.textViewPublishedAt.setText(DateFormatter.getFormattedDate(news.publishedAt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Picasso.get().load(news.urlToImage).into(viewHolder.imageView);

        return convertView;
    }

    private static class ViewHolder{
        TextView textViewAuthor;
        TextView textViewTitle;
        TextView textViewPublishedAt;
        ImageView imageView;
    }
}
