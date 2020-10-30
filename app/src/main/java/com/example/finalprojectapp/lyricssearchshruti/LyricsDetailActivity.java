package com.example.finalprojectapp.lyricssearchshruti;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectapp.R;

public class LyricsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lframelayout);

        Bundle dataToPass = getIntent().getExtras();
        LyricsFragment lFragment = new LyricsFragment();
        lFragment.setArguments(dataToPass);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lframeLayout, lFragment)
                .commit();
    }
}

