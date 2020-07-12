package com.example.finalprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectapp.geodatasource.GrabLocationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Click listener for Geo Location part
        findViewById(R.id.btnGeoLocation).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GrabLocationActivity.class));
        });
    }
}