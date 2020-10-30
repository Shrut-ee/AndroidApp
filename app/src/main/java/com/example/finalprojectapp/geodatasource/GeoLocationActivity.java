package com.example.finalprojectapp.geodatasource;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectapp.BuildConfig;
import com.example.finalprojectapp.MainActivity;
import com.example.finalprojectapp.R;
import com.example.finalprojectapp.vrajsoccer.SoccerMainActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * This is the main drawer screen, user can navigate to different screens from here.
 *
 * @author Meet Vora
 */
public class GeoLocationActivity extends AppCompatActivity {

    /**
     * DrawerLayout object, later will be used to manually close the navigation drawer
     */
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_geo_location);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //For NavigationDrawer:
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.geo_navigation_drawer_open, R.string.geo_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.nav_city_search:
                    loadSearchCityFragment();
                    break;
                case R.id.nav_fav_cities:
                    startActivity(new Intent(this, CitiesListActivity.class));
                    break;
                case R.id.appInstructions:
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.geo_location_instructions)
                            .setMessage(R.string.geo_location_instructions_message)
                            .setNeutralButton(android.R.string.ok, null)
                            .show();
                    break;
                case R.id.aboutAPI:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.geodatasource.com/web-service")));
                    break;
                case R.id.donate:
                    EditText editTextDonateAmount = new EditText(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    editTextDonateAmount.setHint(R.string.geo_dollars);
                    editTextDonateAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editTextDonateAmount.setLayoutParams(layoutParams);
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.geo_donate_title)
                            .setMessage(R.string.geo_donate_message)
                            .setView(editTextDonateAmount)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(R.string.geo_thank_you, (dialog, which) -> Toast.makeText(GeoLocationActivity.this, R.string.geo_thank_you_for_donation, Toast.LENGTH_SHORT).show())
                            .show();
                    break;
            }
            return true;
        });

        // Setting the app version
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tvAppVersion)).setText("v" + BuildConfig.VERSION_NAME);

        // By default load City Search screen
        loadSearchCityFragment();
    }

    /**
     * This method will load City search fragment into the empty frame-layout
     */
    private void loadSearchCityFragment() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.geo_city_search);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new CitySearchFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.geo_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_soccer_match_highlights:
                startActivity(new Intent(this, SoccerMainActivity.class));
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
                Toast.makeText(this, getString(R.string.geo_about_the_project, getClass().getSimpleName()), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}