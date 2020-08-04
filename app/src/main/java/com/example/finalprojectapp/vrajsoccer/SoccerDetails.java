package com.example.finalprojectapp.vrajsoccer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectapp.R;

import java.util.List;

/**
 * Activity for showing details of match selected and favourite checkbox action
 * @author Vraj Shah
 */

public class SoccerDetails extends AppCompatActivity {
    TextView date_tv,side1_tv,side2_tv;
    CheckBox favMatch;
    Button url_b;
    SoccerDBOpener dbOpener;
    private Soccer s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_details);

        dbOpener = new SoccerDBOpener(this);

        date_tv = findViewById(R.id.date_tv);
        side1_tv = findViewById(R.id.team1_tv);
        side2_tv = findViewById(R.id.team2_tv);
        url_b= findViewById(R.id.url_b);
        favMatch= findViewById(R.id.favMatch);

        Intent intent =getIntent();
        String title=intent.getStringExtra("title");
        String date=intent.getStringExtra("date");
        Log.e("temp", "side1: " +date);
        String side1=intent.getStringExtra("side1");
        String side2=intent.getStringExtra("side2");
        String videosJsonArray=intent.getStringExtra("VideosJsonArray");

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

            Intent i = new Intent(this, HighlightsActivity.class);
            i.putExtra("VideosJsonArray",videosJsonArray);
            startActivity(i);
        });

        favMatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) { // Add to DB

                    long id = dbOpener.addMatch(s);
                    s.setId(id);
                    Toast.makeText(SoccerDetails.this, "Added to Favorites!", Toast.LENGTH_SHORT).show();

                } else { // remove from DB
                    dbOpener.removeMatch(s.getId());
                    Toast.makeText(SoccerDetails.this, "Removed from Favorites!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}