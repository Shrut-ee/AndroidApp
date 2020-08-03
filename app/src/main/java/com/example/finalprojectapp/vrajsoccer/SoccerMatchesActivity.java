package com.example.finalprojectapp.vrajsoccer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SoccerMatchesActivity extends AppCompatActivity {
    ListView list;
    TextView titletv;
    MyListAdapter myAdapter;
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

        SoccerAsync soccerAsync = new SoccerAsync();
        soccerAsync.execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                title = soccerGamess.get(position).getTitle();
                date = soccerGamess.get(position).getDate();

                side1 = soccerGamess.get(position).getSide1();
                side2 = soccerGamess.get(position).getSide2();

                Soccer s=new Soccer(title,date,side1,side2,null);

               Intent intent=new Intent(SoccerMatchesActivity.this,SoccerDetails.class);
               intent.putExtra("title",title);
               intent.putExtra("date",date);
               intent.putExtra("side1",side1);
               intent.putExtra("side2",side2);
               intent.putExtra("VideosJsonArray",soccerGamess.get(position).getVideosJsonArray());
               startActivity(intent);
            }
        });

    }

    private class SoccerAsync extends AsyncTask<String, Integer, String> {
        String  titleString, dateString, side1String, side2String;
        ArrayList e= new ArrayList();
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL soccerUrl = new URL("https://www.scorebat.com/video-api/v1/");
                HttpURLConnection con = (HttpURLConnection) soccerUrl.openConnection();
                InputStream input = con.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }

                String result = sb.toString();

                JSONArray titleArray = new JSONArray(result);
                for(int i=0;i<titleArray.length();i++)
                {
                    JSONObject titles = (JSONObject) titleArray.get(i);

                    titleString =  titles.getString("title");
                    dateString = titles.getString("date");

                    JSONObject side1 = (JSONObject)((JSONObject) titleArray.get(i)).get("side1");
                    side1String = side1.getString("name");

                    JSONObject side2 = (JSONObject)((JSONObject) titleArray.get(i)).get("side2");
                    side2String = side2.getString("name");

                    JSONArray videos = titles.getJSONArray("videos");


                    /*Log.e("soccernew", "side1: " +side1String);
                    Log.e("soccer", "Title: " +titleString);*/
                    Soccer s=new Soccer(titleString,dateString,side1String,side2String,videos.toString());
                    soccerGamess.add(s);

                }


            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            myAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.soccer_matches, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_geo_data_source:
                startActivity(new Intent(this, SoccerMainActivity.class));
                finish();
                break;
            case R.id.nav_song_lyrics_search:
                startActivity(new Intent(this, SoccerMainActivity.class));
                finish();
                break;
            case R.id.nav_deezer_song_search:
                startActivity(new Intent(this, SoccerMainActivity.class));
                finish();
                break;
            case R.id.menuItemAboutProject:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
