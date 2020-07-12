package com.example.finalprojectapp.geodatasource;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class GrabLocationActivity extends AppCompatActivity {

    /**
     * Two EditText objects to get the user input for Latitude and Longitude
     */
    private EditText etLat, etLong;

    /**
     * Button object used to pass the latitude and the longitude entered by user to the next activity
     */
    private Button btnFindCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_location);

        etLat = findViewById(R.id.etLat);
        etLong = findViewById(R.id.etLong);
        btnFindCities = findViewById(R.id.btnFindCities);

        btnFindCities.setOnClickListener(v -> {
            // Check for validation
            if (!isValidInput()) return;

            // Open Cities list Activity and pass the data
            Intent intent = new Intent(GrabLocationActivity.this, CitiesActivity.class);
            intent.putExtra("Lat", etLat.getText().toString().trim());
            intent.putExtra("Log", etLong.getText().toString().trim());
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