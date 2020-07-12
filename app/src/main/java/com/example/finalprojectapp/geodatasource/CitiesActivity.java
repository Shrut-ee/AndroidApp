package com.example.finalprojectapp.geodatasource;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.finalprojectapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CitiesActivity extends AppCompatActivity {

    /**
     * latitude and longitude String variables for hold the values we got from the previous activity
     */
    private String latitude, longitude;

    /**
     * This ListView object will display the list of the cities, which will be fetched later
     */
    private ListView lvCities;

    /**
     * A list to store all the cities and this list will be used in the adapter to render on above ListView
     */
    private List<String> cities = new ArrayList<>();

    /**
     * The custom adapter to display cities
     */
    private MyListAdapter adapter;

    /**
     * The progress bar to show the loading progress while fetching data in the background
     */
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        Bundle extras = getIntent().getExtras();
        latitude = extras.getString("Lat");
        longitude = extras.getString("Log");

        lvCities = findViewById(R.id.lvCities);
        progressBar = findViewById(R.id.progressBar);

        // Create adapter object and set it to the list view
        adapter = new MyListAdapter();
        lvCities.setAdapter(adapter);

        lvCities.setOnItemClickListener((parent, view, position, id) -> {

            // Open Cities detail Activity and pass the data
            Intent intent = new Intent(CitiesActivity.this, CityDetailActivity.class);
            intent.putExtra("cityName", cities.get(position));
            startActivity(intent);

        });

        lvCities.setOnItemLongClickListener((parent, view, position, id) -> {

            new AlertDialog.Builder(this)
                    .setTitle("Clicked")
                    .setMessage("Position: " + position + "\nCity: " + cities.get(position))
                    .setPositiveButton("Ok", null)
                    .show();
            return true;

        });

        // Show progress bar before loading cities
        progressBar.setVisibility(View.VISIBLE);

        // To see the progress bar, I have added some delay, will remove this after async implementation
        new Handler().postDelayed(() -> {

            // Adding dummy values to test pur listView output
            cities.add("Ottawa");
            cities.add("Toronto");
            cities.add("Montreal");

            // Hide the loading and notify the adapter about the updated list of cities
            progressBar.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();

            // Show snack bar
            Snackbar.make(findViewById(android.R.id.content), "Cities loaded!", Snackbar.LENGTH_SHORT).show();

        }, 3000);

    }

    /**
     * Custom adapter class for our ListView lvCities
     */
    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cities.size();
        }

        @Override
        public String getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
//            return cities.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View newView = convertView;
            LayoutInflater inflater = getLayoutInflater();

            if (newView == null) {
                newView = inflater.inflate(R.layout.row_layout_city, parent, false);
            }

            //set what the text should be for this row:
            TextView tvCity = newView.findViewById(R.id.tvCity);
            tvCity.setText(getItem(position));

            return newView;
        }
    }
}