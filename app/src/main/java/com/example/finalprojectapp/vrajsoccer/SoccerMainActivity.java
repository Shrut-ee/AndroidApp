package com.example.finalprojectapp.vrajsoccer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.finalprojectapp.MainActivity;
import com.example.finalprojectapp.R;
import com.google.android.material.navigation.NavigationView;

/**
 * Home/main activity from which user can select two options. and drawer layout options
 * @author Vraj Shah
 */

public class SoccerMainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soccer_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.allMatches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SoccerMainActivity.this, SoccerMatchesActivity.class));
            }
        });
        findViewById(R.id.favMatches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SoccerMainActivity.this, FavoriteSoccerMatchesActivity.class));
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.soccer_matches:
                    startActivity(new Intent(this, SoccerMatchesActivity.class));
                    break;
                case R.id.soccer_fav_matches:
                    startActivity(new Intent(this, FavoriteSoccerMatchesActivity.class));
                    break;
                case R.id.appInstructions:
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.soccer_instructions)
                            .setMessage(R.string.soccer_instructions_message)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                    break;
                case R.id.aboutAPI:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.scorebat.com/video-api/")));
                    break;
                case R.id.donate:
                    EditText editTextDonateAmount = new EditText(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    editTextDonateAmount.setHint("$$$");
                    editTextDonateAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editTextDonateAmount.setLayoutParams(layoutParams);
                   new AlertDialog.Builder(this)
                            .setTitle(R.string.soccer_donate_title)
                            .setMessage(R.string.soccer_donate_message)
                            .setView(editTextDonateAmount)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(R.string.soccer_thank_you, (dialog, which) -> Toast.makeText(SoccerMainActivity.this, R.string.soccer_thank_you_for_donation, Toast.LENGTH_SHORT).show())
                            .show();
                    break;
            }
            return true;
        });

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
                startActivity(new Intent(this, MainActivity.class));
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