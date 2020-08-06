package com.example.finalprojectapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;


import java.text.BreakIterator;
import java.util.ArrayList;

public class LyricsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button save01;
    EditText Search;
    ImageButton SButton;
    ListView lview;
    boolean save02;
    LyricsDBhelp helper;
    SQLiteDatabase DB;
    ArrayList<Information> infoList;
    LyricsAdapter ladapter;

    final static String TABLE_NAME = "Lyrics_Search";
    final static String COL_TITLE = "Title";
    final static String COL_INFORMATION = "Information";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        Search = findViewById(R.id.SearchBox);
        SButton = findViewById(R.id.SearchButton);
        toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(Toolbar);

        lview = findViewById(R.id.ListView);
        infoList = new ArrayList<>();
        save01 = findViewById(R.id.SavedFile);

        boolean isTablet = findViewById(R.id.fragmentLayout) != null;
        helper = new LyricsDBhelp(this);
        db = helper.getWriteableDatabase();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Filename", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        save01.setOnClickListener(c -> {
            if (save02 == false) {
                save02 = true;
            } else {
                save02 = false;
            }
            editor.putBoolean("LyricsS", save02);
            editor.commit();
            save02();
        });

        myAdapter = new LyricsAdapter(getApplicationContext(), R.layout.activity_llistview, infoList);
        lview.setAdapter(myAdapter);

        save01.setOnClickListener(c -> {
            if (Search.getText() != null) {
                editor.putString("Search", String.valueOf(Search.getText()));
                editor.commit();
                infoList.clear();
                LyricsAsync query = new LyricsAsync();
                query.execute("https://api.lyrics.ovh/v1/artist/title" + Search.getText() + ": https://www.google.com/search?q=");
                myAdapter.notifyDataSetChanged();
            }
        });

        lview.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putLong("ID", infoList.get(position).getID());
            dataToPass.putString("Title", infoList.get(position).getTitle());
            dataToPass.putString("information", infoList.get(position).getInformation());

            if (isTablet) {

                LyricsFragment lyricsFragment = new LyricsFragment();
                lyricsFragment.setArguments(dataToPass);
                lyricsFragment.setTablet(true);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentLayout, lyricsFragment).addToBackStack("AnyName").commit();
            } else
            {

                Intent nextActivity = new Intent(LyricsActivity.this, LyricsFrameLayout.class);
                nextActivity.putExtras(dataToPass);

                startActivityForResult(nextActivity, 345);
            }
        });

        save02 = sharedPreferences.getBoolean("LyricsS", false);
        save02();

        //set the text for search
        Search.setText(sharedPreferences.getString("LyricsSearch", null));
        if (Search.getText() != null) {
            infoList.clear();
            LyricsAsync query = new LyricsAsync();
            query.execute("https://api.lyrics.ovh/v1/artist/title" + Search.getText() + ": https://www.google.com/search?q=");            myAdapter.notifyDataSetChanged();
            myAdapter.notifyDataSetChanged();
        }

        if (save02 == true) {
            Cursor cursor = databaseList().rawQuery("SELECT * from " + TABLE_NAME, null);
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String information = cursor.getString(cursor.getColumnIndex(COL_INFORMATION));
                addList(id, title, information);
                cursor.moveToNext();
            }
        }
    }
    public void save02() {
        if (save02 == true) {

            save01.setEnabled(false);
            infoList.clear();
            myAdapter.notifyDataSetChanged();

            Toast toast = Toast.makeText(getApplicationContext(), "geting lyrics from saved", Toast.LENGTH_SHORT);
            toast.show();

            Cursor cursor = db.rawQuery("SELECT * from " + TABLE_NAME, null);
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {

                long id = cursor.getLong(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String information = cursor.getString(cursor.getColumnIndex(COL_INFORMATION));
                addList(id, title, information);
                cursor.moveToNext();
            }

            myAdapter.notifyDataSetChanged();

        } else if (save02 == false) {

            save01.setEnabled(true);
            infoList.clear();
            myAdapter.notifyDataSetChanged();
            LyricsAsync query = new LyricsAsync();
            query.execute("https://api.lyrics.ovh/v1/artist/title" + Search.getText() + ": https://www.google.com/search?q=");
            myAdapter.notifyDataSetChanged();

        }
    }

    void addList(Long id, String title, String information) {

        Information information1 = new Information(id, title, information);
        infoList.add(information);
    }

    public void InformationSave(String title, String Information) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.COL_TITLE, title);
        contentValues.put(helper.COL_INFORMATION, infoList);

        long id = db.insert(TABLE_NAME, "ColumnName", contentValues);
        if (save02 == true) {

            addList(id, title, infoList);
            myAdapter.notifyDataSetChanged();
        }
    }

    public void deleteInfoId(int id) {

        db.delete(TABLE_NAME, "id=?", new String[]{Long.toString(id)});

        if (save02 == true) {

            infoList.clear();
            Cursor cursor = db.rawQuery("SELECT * from " + TABLE_NAME, null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                long idd = cursor.getLong(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String information = cursor.getString(cursor.getColumnIndex(COL_INFORMATION));
                addList(idd, title, information);
                cursor.moveToNext();
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lyrics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent nextPage;

        switch (item.getItemId()) {

            case R.id.lhelp:
                alertMessage();
                break;

        }
        return true;
    }

    public void alertMessage() {

        View middle = getLayoutInflater().inflate(R.layout.activity_lhelp, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.help_tittle);
        builder.setMessage(R.string.help_text);

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            long id = data.getLongExtra("ID", 0);

            boolean isDelete = data.getBooleanExtra("TYPE", false);

            if (isDelete == true) {

                deleteinformationId((int) id);

            } else {

                informationSave(data.getStringExtra("TITLE"), data.getStringExtra("information"));
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("close", v -> snackbar.dismiss());
                snackbar.show();
            }
        }
    }


}



    }
}
