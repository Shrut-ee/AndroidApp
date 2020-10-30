package com.example.finalprojectapp.vrajsoccer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Database opener class which contains general methods to get view and delete database objects
 * @author Vraj Shah
 */

public class SoccerDBOpener extends SQLiteOpenHelper {

    private final static String DB_NAME = "SoccerDB";
    private final static int VERSION_NUM = 1;
    private final static String MATCH_TABLE = "MATCHES";
    private final static String ID_COL = "_id";
    private final static String TITLE_COL = "title";
    private final static String DATE_COL = "date";
    private final static String TEAM1_COL = "team_1";
    private final static String TEAM2_COL = "team_2";
    private final static String VIDEOS_COL = "Highlights";

    public SoccerDBOpener(Context ctx) {
        super(ctx, DB_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                        + MATCH_TABLE + " ("+ID_COL+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ TITLE_COL +" text," + DATE_COL + " text," + TEAM1_COL + " text,"
                        + TEAM2_COL + " text," + VIDEOS_COL+ " text);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MATCH_TABLE);
        onCreate(db);
    }

    public void removeMatch(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MATCH_TABLE, ID_COL + "=?", new String[]{Long.toString(id)});
    }

    public long addMatch(Soccer soccer) {
        ContentValues newRowValues = new ContentValues();

        newRowValues.put(TITLE_COL, soccer.getTitle());
        newRowValues.put(DATE_COL, soccer.getDate());
        newRowValues.put(TEAM1_COL, soccer.getSide1());
        newRowValues.put(TEAM2_COL, soccer.getSide2());
        newRowValues.put(VIDEOS_COL, soccer.getVideosJsonArray());

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(MATCH_TABLE, null, newRowValues);
    }

    public List<Soccer> getAllMatches() {
        List<Soccer> matches = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] cols = {ID_COL, TITLE_COL, DATE_COL, TEAM1_COL, TEAM2_COL, VIDEOS_COL};

        Cursor results = db.query(MATCH_TABLE, cols, null, null, null, null, null);

        int idPosition = results.getColumnIndex(ID_COL);
        int titlePosition = results.getColumnIndex(TITLE_COL);
        int datePosition = results.getColumnIndex(DATE_COL);
        int team1Position = results.getColumnIndex(TEAM1_COL);
        int team2Position = results.getColumnIndex(TEAM2_COL);
        int videosPosition = results.getColumnIndex(VIDEOS_COL);

        while (results.moveToNext()) {
            matches.add(new Soccer(
                    results.getLong(idPosition),
                    results.getString(titlePosition),
                    results.getString(datePosition),
                    results.getString(team1Position),
                    results.getString(team2Position),
                    results.getString(videosPosition)
            ));
        }
        results.close();

        return matches;
    }
}
