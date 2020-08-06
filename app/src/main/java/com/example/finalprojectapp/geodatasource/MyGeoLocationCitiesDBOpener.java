package com.example.finalprojectapp.geodatasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Opener class to manage various database related operations,
 * this databse is having one table - which is used to store the list of favorite cities chosen by the user
 *
 * @author Meet Vora
 */
public class MyGeoLocationCitiesDBOpener extends SQLiteOpenHelper {


    /**
     * Name of the database
     */
    private final static String DB_NAME = "GeoLocationCitiesDB";
    /**
     * version of the database
     */
    private final static int VERSION_NUM = 1;

    /**
     * Table-name of the field ID
     */
    private final static String TABLE_CITY = "CITY";

    /**
     * Column-name of the field ID
     */
    private final static String COL_ID = "_id";
    /**
     * Column-name of the field city name
     */
    private final static String COL_CITY_NAME = "city_name";
    /**
     * Column-name of the field country
     */
    private final static String COL_CITY_COUNTRY = "city_country";
    /**
     * Column-name of the field region
     */
    private final static String COL_CITY_REGION = "city_region";
    /**
     * Column-name of the field currency
     */
    private final static String COL_CITY_CURRENCY = "city_currency";
    /**
     * Column-name of the field latitude
     */
    private final static String COL_CITY_LATITUDE = "city_latitude";
    /**
     * Column-name of the field longitude
     */
    private final static String COL_CITY_LONGITUDE = "city_longitude";

    public MyGeoLocationCitiesDBOpener(Context ctx) {
        super(ctx, DB_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "
                        + TABLE_CITY + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COL_CITY_NAME + " text,"
                        + COL_CITY_COUNTRY + " text,"
                        + COL_CITY_REGION + " text,"
                        + COL_CITY_CURRENCY + " text,"
                        + COL_CITY_LATITUDE + " text,"
                        + COL_CITY_LONGITUDE + " text);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        onCreate(db);
    }

    /**
     * This method will get all the favorite cities from the database and then will return them as a list
     */
    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                COL_ID,
                COL_CITY_NAME,
                COL_CITY_COUNTRY,
                COL_CITY_REGION,
                COL_CITY_CURRENCY,
                COL_CITY_LATITUDE,
                COL_CITY_LONGITUDE
        };

        Cursor results = db.query(
                TABLE_CITY,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        int indexId = results.getColumnIndex(COL_ID);
        int indexName = results.getColumnIndex(COL_CITY_NAME);
        int indexCountry = results.getColumnIndex(COL_CITY_COUNTRY);
        int indexRegion = results.getColumnIndex(COL_CITY_REGION);
        int indexCurrency = results.getColumnIndex(COL_CITY_CURRENCY);
        int indexLatitude = results.getColumnIndex(COL_CITY_LATITUDE);
        int indexLongitude = results.getColumnIndex(COL_CITY_LONGITUDE);

        while (results.moveToNext()) {
            cities.add(new City(
                    results.getLong(indexId),
                    results.getString(indexName),
                    results.getString(indexCountry),
                    results.getString(indexRegion),
                    results.getString(indexCurrency),
                    results.getString(indexLatitude),
                    results.getString(indexLongitude)
            ));
        }
        results.close();

        return cities;
    }

    /**
     * This method will add a city row in the table
     */
    public long addCity(City city) {
        ContentValues newRowValues = new ContentValues();

        newRowValues.put(COL_CITY_NAME, city.getCityName());
        newRowValues.put(COL_CITY_COUNTRY, city.getCountry());
        newRowValues.put(COL_CITY_REGION, city.getRegion());
        newRowValues.put(COL_CITY_CURRENCY, city.getCurrency());
        newRowValues.put(COL_CITY_LATITUDE, city.getLatitude());
        newRowValues.put(COL_CITY_LONGITUDE, city.getLongitude());

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_CITY, null, newRowValues);
    }

    /**
     * Method to remove a city from the database
     */
    public void removeCity(long cityID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CITY, COL_ID + "=?", new String[]{Long.toString(cityID)});
    }
}
