package com.example.finalprojectapp.geodatasource;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalprojectapp.R;

import androidx.fragment.app.Fragment;

/**
 * Fragment to show more details about the selected city.
 *
 * @author Meet Vora
 */
public class CityDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_city_details, container, false);

        City city = (City) getArguments().getSerializable(CitiesListActivity.ITEM_CITY);
        ((TextView) view.findViewById(R.id.tvCityName)).setText(city.getCityName());
        ((TextView) view.findViewById(R.id.tvCountry)).setText(city.getCountry());
        ((TextView) view.findViewById(R.id.tvRegion)).setText(city.getRegion());
        ((TextView) view.findViewById(R.id.tvCurrency)).setText(city.getCurrency());
        ((TextView) view.findViewById(R.id.tvLatitude)).setText(city.getLatitude());
        ((TextView) view.findViewById(R.id.tvLongitude)).setText(city.getLongitude());

        view.findViewById(R.id.fabOpenLocation).setOnClickListener(v -> {

            // [Source: Google Developers - https://developers.google.com/maps/documentation/urls/android-intents]
            Uri gmmIntentUri = Uri.parse("geo:" + city.getLatitude() + "," + city.getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        });

        return view;
    }

}