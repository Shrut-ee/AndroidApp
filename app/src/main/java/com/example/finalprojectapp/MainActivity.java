package com.example.finalprojectapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalprojectapp.geodatasource.GeoLocationActivity;
import com.example.finalprojectapp.lyricssearchshruti.LyricsActivity;
import com.example.finalprojectapp.vrajsoccer.SoccerMainActivity;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The main merged activity, which will be used to navigate to each of the four parts of the final project
 *
 * @author Meet Vora
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.geoLocation).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GeoLocationActivity.class));
        });

        findViewById(R.id.soccerHighlights).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SoccerMainActivity.class));
        });

        findViewById(R.id.songLyricsSearch).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LyricsActivity.class));
        });
    }
}