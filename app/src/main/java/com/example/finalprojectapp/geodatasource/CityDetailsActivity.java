package com.example.finalprojectapp.geodatasource;

import android.os.Bundle;

import com.example.finalprojectapp.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity is just a loader for CityDetailsFragment
 *
 * @author Meet Vora
 */
public class CityDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        CityDetailsFragment cityDetailsFragment = new CityDetailsFragment();
        cityDetailsFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, cityDetailsFragment)
                .commit();

    }
}