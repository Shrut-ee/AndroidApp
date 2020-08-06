package com.example.finalprojectapp.lyricssearchshruti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.finalprojectapp.R;

import java.util.ArrayList;

public class LyricsAdapter extends BaseAdapter {
    ArrayList<Information> infoList;
    Context context;

    public LyricsAdapter(Context context, ArrayList<Information> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Information getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Information information = infoList.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.activity_llistview, parent, false);
        TextView songName = convertView.findViewById(R.id.songName);
        TextView artistName = convertView.findViewById(R.id.artistName);
        songName.setText(information.getTitle());
        artistName.setText(information.getArtist());

        return convertView;
    }
}