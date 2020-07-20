package com.example.finalprojectapp.geodatasource;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectapp.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity gets latitude and longitude from the user
 *
 * @author Meet Vora
 */
public class GrabLocationActivity extends AppCompatActivity {

    /**
     * Two EditText objects to get the user input for Latitude and Longitude
     */
    private EditText etLat, etLong;

    /**
     * Button object used to pass the latitude and the longitude entered by user to the next activity
     */
    private Button btnFindCities;

    /**
     * SharedPreferences to store the latitude and longitude, so that the next time user opens this screen,
     * it will be pre-filled
     */
    private SharedPreferences geoDataSourcePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_location);

        etLat = findViewById(R.id.etLat);
        etLong = findViewById(R.id.etLong);
        btnFindCities = findViewById(R.id.btnFindCities);

        // Getting values from shared-prefs
        geoDataSourcePrefs = getSharedPreferences("GeoDataSourcePrefs", Context.MODE_PRIVATE);
        etLat.setText(geoDataSourcePrefs.getString("lat", ""));
        etLong.setText(geoDataSourcePrefs.getString("log", ""));

        btnFindCities.setOnClickListener(v -> {
            // Check for validation
            if (!isValidInput()) return;

            String lat = etLat.getText().toString().trim();
            String log = etLong.getText().toString().trim();

            // Storing into shared-preferences
            geoDataSourcePrefs.edit()
                    .putString("lat", lat)
                    .putString("log", log)
                    .apply();

            // Open Cities list Activity and pass the data
            Intent intent = new Intent(GrabLocationActivity.this, CitiesActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("log", log);
            startActivity(intent);
        });
    }

    /**
     * This method will make sure that user has entered values in both latitude and longitude fields
     *
     * @return false if either or both fields of latitude and longitude empty, otherwise true
     */
    private boolean isValidInput() {
        String latitude = etLat.getText().toString().trim();
        String longitude = etLong.getText().toString().trim();

        if (latitude.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_lat, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longitude.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_log, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}