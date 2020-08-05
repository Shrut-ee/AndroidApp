package com.example.finalprojectapp.vrajsoccer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finalprojectapp.MainActivity;
import com.example.finalprojectapp.R;
import com.example.finalprojectapp.geodatasource.GeoLocationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This activity is about showing video/highlights for the match in list view.
 * @author Vraj Shah
 */

public class HighlightsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);
        setSupportActionBar(findViewById(R.id.toolbar));

        ListView listViewHighlights = findViewById(R.id.listViewHighlights);

        String videosJsonArray=getIntent().getStringExtra("VideosJsonArray");
        Log.e("temp", "asdasd: " +videosJsonArray);

        try {
            JSONArray videos = new JSONArray(videosJsonArray);
            String[] videoTitles = new String[videos.length()];
            String[] videoLinks = new String[videos.length()];
            for(int i=0;i<videos.length();i++) {
                JSONObject video = videos.getJSONObject(i);
                String videoTitle =  video.getString("title");
                videoTitles[i] = videoTitle;
                String embed = video.getString("embed");
                String sub = embed.substring(embed.indexOf("https:"));
                String finalLink = sub.substring(0, sub.indexOf("'"));
                videoLinks[i] = finalLink;
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, videoTitles);
            listViewHighlights.setAdapter(arrayAdapter);

            //Highlights button on click to show list of videos
            listViewHighlights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("temp", "onItemClick() returned: " + videoLinks[position]);
                    String url = videoLinks[position];
                    Intent links = new Intent(Intent.ACTION_VIEW);
                    links.setData(Uri.parse(url));
                    startActivity(links);

                    SharedPreferences soccerPrefs = getSharedPreferences("SoccerPrefs", Context.MODE_PRIVATE);
                    soccerPrefs.edit().putString("url", videoLinks[position]).apply();

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.soccer_matches, menu);
        return true;
    }
    //toolbar method

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_geo_data_source:
                startActivity(new Intent(this, GeoLocationActivity.class));
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
                Toast.makeText(this, R.string.soccer_about_the_project, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}