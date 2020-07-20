package com.example.finalprojectapp.geodatasource;

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

        Bundle arguments = getArguments();
        TextView tvCityName = view.findViewById(R.id.tvCityName);
        tvCityName.setText(arguments.getString(CitiesActivity.ITEM_CITY_NAME));

        return view;
    }

}