package com.example.finalprojectapp.lyricssearchshruti;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.finalprojectapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LyricsFragment extends Fragment {

    Bundle dataFromActivity;
    long id;
    LyricsDBhelp helper;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("ID", -1);

        View result = inflater.inflate(R.layout.activity_lfragment, container, false);

        TextView title = result.findViewById(R.id.Fragment_title);
        title.setText(dataFromActivity.getString("Title"));

        TextView information = result.findViewById(R.id.fragment_lyrics);
        information.setText(dataFromActivity.getString("Information"));

        // get the delete button, and add a click listener:
        Button googleSearchButton = (Button) result.findViewById(R.id.googleSearchButton);
        googleSearchButton.setOnClickListener(clk -> {

            try {
                String artist = URLEncoder.encode(dataFromActivity.getString("Artist"), "utf-8");
                String song = URLEncoder.encode(dataFromActivity.getString("Title"), "utf-8");
                String url = "https://www.google.com/search?q=" + artist + "+" + song;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(getActivity(), "Error while encoding URL", Toast.LENGTH_SHORT).show();
            }


        });

        helper = new LyricsDBhelp(getActivity());
        db = helper.getWriteableDatabase();

        Button saveB = (Button) result.findViewById(R.id.fragment_save);
        if(id != -1){
            saveB.setText("Remove");
        }
        saveB.setOnClickListener(clk -> {

            if(id != -1){

                db.delete(LyricsDBhelp.TABLE_NAME, LyricsDBhelp.COL_ID + "=?", new String[]{Long.toString(id)});
                Snackbar.make(result.findViewById(R.id.fragmentLayout), "Deleted", Snackbar.LENGTH_LONG).show();
                id = -1;
                saveB.setText("Save");

            } else {

                ContentValues contentValues = new ContentValues();
                contentValues.put(LyricsDBhelp.COL_TITLE, dataFromActivity.getString("Title"));
                contentValues.put(LyricsDBhelp.COL_ARTIST, dataFromActivity.getString("Artist"));
                contentValues.put(LyricsDBhelp.COL_INFORMATION, dataFromActivity.getString("Information"));

                id = db.insert(LyricsDBhelp.TABLE_NAME, "ColumnName", contentValues);

                Snackbar sb = Snackbar.make(result.findViewById(R.id.fragmentLayout), "Saved", Snackbar.LENGTH_LONG);
                sb.setAction("Close", v -> sb.dismiss()).show();

                saveB.setText("Remove");
            }

        });

        return result;
    }
}
