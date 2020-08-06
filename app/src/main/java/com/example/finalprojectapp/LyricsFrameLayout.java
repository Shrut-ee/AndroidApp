package com.example.finalprojectapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LyricsFrameLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lframelayout);
        Bundle dataToPass = getIntent().getExtras();
        LyricsFragement lFragment = new LyricsFragement();
        lFragment.setArguments(dataToPass);
        lFragment.setTablet(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLayout, lFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}

