package com.example.finalprojectapp.vrajsoccer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.finalprojectapp.R;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<Soccer> array;

    public MyListAdapter(LayoutInflater layoutInflater, ArrayList<Soccer> array) {
        this.layoutInflater = layoutInflater;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Soccer getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.activity_game_title, parent, false);
        TextView title = view.findViewById(R.id.title);
        title.setText(getItem(position).getTitle());
        return view;
    }


}