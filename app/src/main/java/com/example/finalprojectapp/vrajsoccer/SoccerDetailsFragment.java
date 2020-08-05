package com.example.finalprojectapp.vrajsoccer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.finalprojectapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * Fragment to show details of Soccer match seletced by user
 * @author Vraj Shah
 */

public class SoccerDetailsFragment extends Fragment {
    Bundle dataFromActivity;
    TextView date_tv,side1_tv,side2_tv;
    CheckBox favMatch;
    Button url_b;
    SoccerDBOpener dbOpener;
    private Soccer s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();

        View result =  inflater.inflate(R.layout.fragment_soccer_details, container, false);


        dbOpener = new SoccerDBOpener(getActivity());

        date_tv = result.findViewById(R.id.date_tv);
        side1_tv = result.findViewById(R.id.team1_tv);
        side2_tv = result.findViewById(R.id.team2_tv);
        url_b= result.findViewById(R.id.url_b);
        favMatch= result.findViewById(R.id.favMatch);

        String title=dataFromActivity.getString("title");
        String date=dataFromActivity.getString("date");
        Log.e("temp", "side1: " +date);
        String side1=dataFromActivity.getString("side1");
        String side2=dataFromActivity.getString("side2");
        String videosJsonArray=dataFromActivity.getString("VideosJsonArray");

        s = new Soccer(title, date, side1, side2, videosJsonArray);

        List<Soccer> favMatches = dbOpener.getAllMatches();
        for (Soccer match : favMatches) {
            if((match.getTitle()+match.getDate()).equals((s.getTitle()+s.getDate()))){
                favMatch.setChecked(true);
                s.setId(match.getId());
                break;
            }
        }

        date_tv.setText(date);
        side1_tv.setText(side1);
        side2_tv.setText(side2);

        url_b.setOnClickListener( k -> {

            Intent i = new Intent(getActivity(), HighlightsActivity.class);
            i.putExtra("VideosJsonArray",videosJsonArray);
            startActivity(i);
        });

        favMatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) { // Add to DB

                    long id = dbOpener.addMatch(s);
                    s.setId(id);
                    Toast.makeText(getActivity(), R.string.toast, Toast.LENGTH_SHORT).show();

                } else { // remove from DB
                    dbOpener.removeMatch(s.getId());
                    //Toast.makeText(getActivity(), "Removed from Favorites!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.snack, Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        return result;
    }
}