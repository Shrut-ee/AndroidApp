package com.example.finalprojectapp.vrajsoccer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectapp.MainActivity;
import com.example.finalprojectapp.R;
import com.example.finalprojectapp.geodatasource.GeoLocationActivity;

import java.util.ArrayList;

/**
 * Activity is about getting favourites and show favourites in list
 * @author Vraj Shah
 */
public class FavoriteSoccerMatchesActivity extends AppCompatActivity {
    /**
     * A listview to set adpter and to show matches from json
     */
    ListView list;
    TextView titletv;
    MyListAdapter myAdapter;
    /**
     * A list to store soccer objects
     */
    ArrayList<Soccer> soccerGamess = new ArrayList<>();
    String title,date,side1,side2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_matches);

        setSupportActionBar(findViewById(R.id.toolbar));

        list = (ListView) findViewById(R.id.list);
        titletv = (TextView) findViewById(R.id.title);

        myAdapter = new MyListAdapter(getLayoutInflater(), soccerGamess);
        list.setAdapter(myAdapter);


        findViewById(R.id.progressBar).setVisibility(View.GONE);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                title = soccerGamess.get(position).getTitle();
                date = soccerGamess.get(position).getDate();

                side1 = soccerGamess.get(position).getSide1();
                side2 = soccerGamess.get(position).getSide2();

                Soccer s=new Soccer(title,date,side1,side2,null);

               Intent intent=new Intent(FavoriteSoccerMatchesActivity.this,SoccerDetails.class);
               intent.putExtra("title",title);
               intent.putExtra("date",date);
               intent.putExtra("side1",side1);
               intent.putExtra("side2",side2);
               intent.putExtra("VideosJsonArray",soccerGamess.get(position).getVideosJsonArray());
               startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        soccerGamess.clear();
        SoccerDBOpener dbOpener = new SoccerDBOpener(this);
        soccerGamess.addAll(dbOpener.getAllMatches());
        myAdapter.notifyDataSetChanged();
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
