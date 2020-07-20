package com.example.finalprojectapp.geodatasource;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.finalprojectapp.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity fetch the cities from web-service API call and then list them
 *
 * @author Meet Vora
 */
public class CitiesActivity extends AppCompatActivity {

    /**
     * Key to use for passing data in Bundle
     */
    public static final String ITEM_CITY_NAME = "CITY_NAME";

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

    /**
     * A boolean to store the value if the current device is phone or a tablet
     */
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        Bundle extras = getIntent().getExtras();
        latitude = extras.getString("lat");
        longitude = extras.getString("log");

        lvCities = findViewById(R.id.lvCities);
        progressBar = findViewById(R.id.progressBar);

        // Create adapter object and set it to the list view
        adapter = new MyListAdapter();
        lvCities.setAdapter(adapter);

        lvCities.setOnItemClickListener((parent, view, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_CITY_NAME, cities.get(position));

            if (isTablet) {
                CityDetailsFragment cityDetailsFragment = new CityDetailsFragment();
                cityDetailsFragment.setArguments(dataToPass);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, cityDetailsFragment)
                        .commit();
            } else {
                Intent nextActivity = new Intent(this, CityDetailsActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }

        });

        lvCities.setOnItemLongClickListener((parent, view, position, id) -> {

            new AlertDialog.Builder(this)
                    .setTitle("Clicked")
                    .setMessage("Position: " + position + "\nCity: " + cities.get(position))
                    .setPositiveButton("Ok", null)
                    .show();
            return true;

        });

        isTablet = findViewById(R.id.fragmentContainer) != null;

        new FetchCountryListTask()
                .execute("https://api.geodatasource.com/cities" +
                        "?key=QGGUAWT41WPDWUPP5LT8CUJ98M7QTSMC" +
                        "&lat=" + latitude +
                        "&lng=" + longitude +
                        "&format=JSON");

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

    /**
     * AsyncTask to fetch list of cities nearby the provided lat-long using the web-service call,
     * This will handle the JSON-parsing and after populating cities list, it will refresh the adapter
     * on the main thread.
     */
    private class FetchCountryListTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // Show progress bar before loading cities
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL(strings[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data
                InputStream response = urlConnection.getInputStream();

                //JSON reading
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) sb.append(line + "\n");
                String result = sb.toString();

                JSONArray uvReport = new JSONArray(result);
                for (int i = 0; i < uvReport.length(); i++) {
                    JSONObject jsonObject = uvReport.getJSONObject(i);
                    cities.add(jsonObject.getString("city"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();

            // Show snack bar
            Snackbar.make(findViewById(android.R.id.content), "Cities loaded!", Snackbar.LENGTH_SHORT).show();
        }
    }
}