package com.example.finalprojectapp.lyricssearchshruti;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectapp.R;

import java.util.ArrayList;

public class FavoriteLyricsActivity extends AppCompatActivity {

    ArrayList<Information> infoList = new ArrayList<>();
    private boolean isTablet;
    private LyricsAdapter ladapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_lyrics);

        ListView favSongLyrics = findViewById(R.id.favSongLyrics);
        isTablet = findViewById(R.id.lframeLayout) != null;
        ladapter = new LyricsAdapter(this, infoList);
        favSongLyrics.setAdapter(ladapter);


        loadFromDB();

        favSongLyrics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle dataToPass = new Bundle();
                dataToPass.putLong("ID", infoList.get(position).getId());
                dataToPass.putString("Title", infoList.get(position).getTitle());
                dataToPass.putString("Artist", infoList.get(position).getArtist());
                dataToPass.putString("Information", infoList.get(position).getInformation());

                if (isTablet) {

                    LyricsFragment lyricsFragment = new LyricsFragment();
                    lyricsFragment.setArguments(dataToPass);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.lframeLayout, lyricsFragment)
                            .commit();

                } else {

                    Intent nextActivity = new Intent(FavoriteLyricsActivity.this, LyricsDetailActivity.class);
                    nextActivity.putExtras(dataToPass);
                    startActivity(nextActivity);
                }
            }
        });

    }

    private void loadFromDB() {
        LyricsDBhelp lyricsDBhelp = new LyricsDBhelp(this);
        SQLiteDatabase db = lyricsDBhelp.getReadableDatabase();

        String[] columns = {LyricsDBhelp.COL_ID, LyricsDBhelp.COL_TITLE, LyricsDBhelp.COL_ARTIST, LyricsDBhelp.COL_INFORMATION};

        Cursor results = db.query(LyricsDBhelp.TABLE_NAME, columns, null, null, null, null, null);

        int indexId = results.getColumnIndex(LyricsDBhelp.COL_ID);
        int indexTitle = results.getColumnIndex(LyricsDBhelp.COL_TITLE);
        int indexArtist = results.getColumnIndex(LyricsDBhelp.COL_ARTIST);
        int indexLyrics = results.getColumnIndex(LyricsDBhelp.COL_INFORMATION);

        infoList.clear();
        while (results.moveToNext()) {
            infoList.add(new Information(
                    results.getLong(indexId),
                    results.getString(indexTitle),
                    results.getString(indexArtist),
                    results.getString(indexLyrics)
            ));
        }
        results.close();

        if (infoList.isEmpty()) {
            Toast.makeText(this, "No fav songs", Toast.LENGTH_SHORT).show();
        }
        ladapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromDB();
    }
}