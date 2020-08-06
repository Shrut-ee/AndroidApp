package com.example.finalprojectapp.lyricssearchshruti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.finalprojectapp.MainActivity;
import com.example.finalprojectapp.R;
import com.example.finalprojectapp.geodatasource.CitiesListActivity;
import com.example.finalprojectapp.geodatasource.GeoLocationActivity;
import com.example.finalprojectapp.vrajsoccer.SoccerMainActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LyricsActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText Search, SearchBoxSongName;
    ImageButton SButton;

    ArrayList<Information> infoList;

    final static String TABLE_NAME = "Lyrics_Search";
    final static String COL_TITLE = "Title";
    final static String COL_INFORMATION = "Information";
    //    private LyricsAdapter myAdapter;
    private SharedPreferences sharedPreferences;
    private boolean isTablet;
    private ProgressBar progressBar;
    private String lyrics;
    private DrawerLayout drawlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        Search = findViewById(R.id.SearchBox);
        SearchBoxSongName = findViewById(R.id.SearchBoxSongName);
        progressBar = findViewById(R.id.progressBar);
        SButton = findViewById(R.id.SearchButton);
        toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getApplicationContext().getSharedPreferences("Filename", 0);
        Search.setText(sharedPreferences.getString("Search", ""));
        SearchBoxSongName.setText(sharedPreferences.getString("SearchBoxSongName", ""));

        infoList = new ArrayList<>();

        isTablet = findViewById(R.id.fragmentLayout) != null;

        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Search.getText().toString().trim().isEmpty() && !SearchBoxSongName.getText().toString().trim().isEmpty()) {
                    sharedPreferences
                            .edit()
                            .putString("Search", String.valueOf(Search.getText()))
                            .putString("SearchBoxSongName", String.valueOf(SearchBoxSongName.getText()))
                            .apply();
                    infoList.clear();
                    LyricsAsync query = new LyricsAsync();
                    query.execute("https://api.lyrics.ovh/v1/" + Search.getText().toString().trim() + "/" + SearchBoxSongName.getText().toString().trim());

                }
            }
        });

        findViewById(R.id.favList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favScreen = new Intent(LyricsActivity.this, FavoriteLyricsActivity.class);
                startActivity(favScreen);

            }
        });

        //navdraw
        drawlay = findViewById(R.id.drawlay);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawlay, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawlay.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.navfavsongs:
                    startActivity(new Intent(this, FavoriteLyricsActivity.class));
                    break;

                case R.id.Instructions:
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.lyricsinstrustions)
                            .setMessage(R.string.lyricsinstrutionsmessage)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                    break;

                case R.id.API:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://lyricsovh.docs.apiary.io/#")));
                    break;

                case R.id.donations:
                    EditText editTextDonateAmount = new EditText(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        editTextDonateAmount.setHint(R.string.dollars);
                        editTextDonateAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        editTextDonateAmount.setLayoutParams(layoutParams);

                    new AlertDialog.Builder(this)
                            .setTitle(R.string.donate_title)
                            .setMessage(R.string.donate_message)
                            .setView(editTextDonateAmount)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(R.string.thank_you, (dialog, which) -> Toast.makeText(LyricsActivity.this, R.string.thank_you, Toast.LENGTH_SHORT).show())
                            .show();
                    break;

            }
            drawlay.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lyrics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_geo_data_source:
                startActivity(new Intent(this, GeoLocationActivity.class));
                break;

            case R.id.nav_soccer_match_highlights:
                startActivity(new Intent(this, SoccerMainActivity.class));
                break;

            case R.id.nav_deezer_song_search:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.menuItemAboutProject:
                Toast.makeText(this, "This is the Lyrics Search activity, written by Shruti.", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    public class LyricsAsync extends AsyncTask<String, Integer, String[]> {

        @Override
        protected void onPreExecute() {
            lyrics = "";
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            try {

                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream response = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) sb.append(line + "\n");
                String result = sb.toString();

                JSONObject resultJsonObject = new JSONObject(result);
                if (resultJsonObject.has("error")) {
                    Toast.makeText(LyricsActivity.this, resultJsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    return null;
                }

                lyrics = resultJsonObject.getString("lyrics");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            progressBar.setVisibility(View.INVISIBLE);

            if (!lyrics.isEmpty()) {
                Intent i = new Intent(LyricsActivity.this, LyricsDetailActivity.class);
                i.putExtra("Title", SearchBoxSongName.getText().toString().trim());
                i.putExtra("Artist", Search.getText().toString().trim());
                i.putExtra("Information", lyrics);
                startActivity(i);
            } else {
                Toast.makeText(LyricsActivity.this, "No song found", Toast.LENGTH_SHORT).show();
            }


        }
    }




}
