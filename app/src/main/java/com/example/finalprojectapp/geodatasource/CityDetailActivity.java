package com.example.finalprojectapp.geodatasource;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.finalprojectapp.R;

public class CityDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        TextView tvCityName = findViewById(R.id.tvCityName);

        Bundle extras = getIntent().getExtras();
        String cityName = extras.getString("cityName");

        tvCityName.setText(cityName);

    }
}