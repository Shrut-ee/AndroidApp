package com.example.finalprojectapp.vrajsoccer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalprojectapp.HighlightsActivity;
import com.example.finalprojectapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SoccerDetails extends AppCompatActivity {
    TextView date_tv,side1_tv,side2_tv;
    Button url_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_details);
        date_tv = findViewById(R.id.date_tv);
        side1_tv = findViewById(R.id.team1_tv);
        side2_tv = findViewById(R.id.team2_tv);
        url_b= findViewById(R.id.url_b);

        Intent intent =getIntent();
        String date=intent.getStringExtra("date");
        Log.e("temp", "side1: " +date);
        String side1=intent.getStringExtra("side1");
        String side2=intent.getStringExtra("side2");



        date_tv.setText(date);
        side1_tv.setText(side1);
        side2_tv.setText(side2);

        url_b.setOnClickListener( k -> {

            Intent i = new Intent(this, HighlightsActivity.class);
            i.putExtra("VideosJsonArray",intent.getStringExtra("VideosJsonArray"));
            startActivity(i);
        });

    }
}