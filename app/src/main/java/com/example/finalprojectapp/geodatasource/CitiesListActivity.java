package com.example.finalprojectapp.geodatasource;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectapp.MainActivity;
import com.example.finalprojectapp.R;
import com.example.finalprojectapp.vrajsoccer.SoccerMainActivity;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity fetch the cities from web-service API call and then list them
 *
 * @author Meet Vora
 */
public class CitiesListActivity extends AppCompatActivity {

    /**
     * Key to use for passing data in Bundle
     */
    public static final String ITEM_CITY = "CITY";

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
    private List<City> cities = new ArrayList<>();

    /**
     * A list to store the list of all favorite cities
     */
    private List<City> favoriteCities = new ArrayList<>();

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

    /**
     * Object of my custom database open helper class, to perform databse related operations
     */
    private MyGeoLocationCitiesDBOpener dbOpener;

    /**
     * This activity can show only favorites cities' list also, this boolean will handle that case
     */
    private boolean showFavListOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        dbOpener = new MyGeoLocationCitiesDBOpener(this);

        // Setting up the toolbar
        setSupportActionBar(findViewById(R.id.toolbar));

        // If no extra for lat or log, it means we have to display only favorite list only
        showFavListOnly = !getIntent().hasExtra("lat");

        if (!showFavListOnly) {
            Bundle extras = getIntent().getExtras();
            latitude = extras.getString("lat");
            longitude = extras.getString("log");
        }

        if (showFavListOnly)
            getSupportActionBar().setTitle(R.string.geo_favorite_cities);

        lvCities = findViewById(R.id.lvCities);
        progressBar = findViewById(R.id.progressBar);

        // Create adapter object and set it to the list view
        adapter = new MyListAdapter();
        lvCities.setAdapter(adapter);

        lvCities.setOnItemClickListener((parent, view, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putSerializable(ITEM_CITY, cities.get(position));

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
                    .setMessage(
                            "Position: " + position +
                            "\nCity: " + cities.get(position).getCityName() +
                            "\nID: " + id
                    )
                    .setPositiveButton("Ok", null)
                    .show();
            return true;

        });

        // Fetch favorite cities' list from database
        favoriteCities.addAll(dbOpener.getAllCities());

        isTablet = findViewById(R.id.fragmentContainer) != null;

        if (showFavListOnly) {
            // If we are showing fav list, then we can add favoriteCities as fetched from database above,
            // then we can add them into cities and then just refresh the adapter
            cities.addAll(favoriteCities);
            adapter.notifyDataSetChanged();
        } else {
            // Call API only, if we are NOT showing favorite list
            new FetchCountryListTask()
                    .execute("https://api.geodatasource.com/cities" +
                            "?key=QGGUAWT41WPDWUPP5LT8CUJ98M7QTSMC" +
                            "&lat=" + latitude +
                            "&lng=" + longitude +
                            "&format=JSON");
        }

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
        public City getItem(int position) {
            return cities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return cities.get(position).getCityID();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            City city = getItem(position);

            View newView = inflater.inflate(R.layout.row_layout_city, parent, false);

            CheckBox likeIcon = newView.findViewById(R.id.likeIcon);
            likeIcon.setChecked(favoriteCities.contains(city));
            likeIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    city.setCityID(dbOpener.addCity(city));
                    favoriteCities.add(city);
                    Snackbar.make(findViewById(android.R.id.content), R.string.geo_added_to_fav, Snackbar.LENGTH_SHORT).show();
                } else {
                    dbOpener.removeCity(favoriteCities.get(favoriteCities.indexOf(city)).getCityID());
                    favoriteCities.remove(city);
                    Snackbar.make(findViewById(android.R.id.content), R.string.geo_removed_from_fav, Snackbar.LENGTH_SHORT).show();
                }
            });

            //set what the text should be for this row:
            TextView tvCity = newView.findViewById(R.id.tvCity);
            tvCity.setText(city.getCityName());

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
                    City city = new City(
                            jsonObject.getString("city"),
                            jsonObject.getString("country"),
                            jsonObject.getString("region"),
                            jsonObject.getString("currency_name"),
                            jsonObject.getString("latitude"),
                            jsonObject.getString("longitude")
                    );
                    cities.add(city);
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
            Snackbar.make(findViewById(android.R.id.content), R.string.geo_cities_loaded, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.geo_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_soccer_match_highlights:
                startActivity(new Intent(this, SoccerMainActivity.class));
                finish();
                break;
            case R.id.nav_song_lyrics_search:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.nav_deezer_song_search:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.menuItemAboutProject:
                Toast.makeText(this, getString(R.string.geo_about_the_project, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}