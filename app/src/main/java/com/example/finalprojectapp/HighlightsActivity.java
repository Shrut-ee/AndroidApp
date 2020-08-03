package com.example.finalprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HighlightsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);

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

            listViewHighlights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("temp", "onItemClick() returned: " + videoLinks[position]);
                    String url = videoLinks[position];
                    Intent links = new Intent(Intent.ACTION_VIEW);
                    links.setData(Uri.parse(url));
                    startActivity(links);


                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}