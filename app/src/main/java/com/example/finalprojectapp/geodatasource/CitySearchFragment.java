package com.example.finalprojectapp.geodatasource;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectapp.R;

import androidx.fragment.app.Fragment;

/**
 * This fragment gets latitude and longitude from the user, validate it and then pass it to the city list screen
 *
 * @author Meet Vora
 */
public class CitySearchFragment extends Fragment {

    /**
     * Two EditText objects to get the user input for Latitude and Longitude
     */
    private EditText etLat, etLong;

    /**
     * SharedPreferences to store the latitude and longitude, so that the next time user opens this screen,
     * it will be pre-filled
     */
    private SharedPreferences geoDataSourcePrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_city_search, container, false);

        etLat = view.findViewById(R.id.etLat);
        etLong = view.findViewById(R.id.etLong);

        // Getting values from shared-prefs
        geoDataSourcePrefs = getActivity().getSharedPreferences("GeoDataSourcePrefs", Context.MODE_PRIVATE);
        etLat.setText(geoDataSourcePrefs.getString("lat", ""));
        etLong.setText(geoDataSourcePrefs.getString("log", ""));

        view.findViewById(R.id.btnFindCities).setOnClickListener(v -> {
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
            Intent intent = new Intent(getActivity(), CitiesListActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("log", log);
            startActivity(intent);
        });

        return view;
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
            Toast.makeText(getActivity(), R.string.geo_please_enter_lat, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (longitude.isEmpty()) {
            Toast.makeText(getActivity(), R.string.geo_please_enter_log, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}