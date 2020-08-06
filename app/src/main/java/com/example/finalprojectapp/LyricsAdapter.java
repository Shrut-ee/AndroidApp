package com.example.finalprojectapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LyricsAdapter extends ArrayAdapter<Information>{
    ArrayList<Information> infoList;
    Context context;
    int s;

    public LyricsAdapter(Context context, int i, ArrayList<Information> DefinitionList) {
        super(context, i, infoList);
        this.context = context;
        this.s = s;
        this.infoList = infoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Information information = infoList.get(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.activity_llistview, parent, false);
        TextView title = convertView.findViewById(R.id.LyricsTitle);
        TextView info = convertView.findViewById(R.id.info);
        title.setText(information.getTitle());
        Information.setText(information.getInformation());

        return convertView;
    }
}