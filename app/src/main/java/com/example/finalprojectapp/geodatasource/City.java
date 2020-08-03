package com.example.finalprojectapp.geodatasource;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is a POJO class to store single city, parsed from JSON
 *
 * @author Meet Vora
 */
class City implements Serializable {

    /**
     * ID of the city, will be used in database
     */
    private long cityID;

    /**
     * Name of the city
     */
    private String cityName;

    /**
     * COuntry of the city
     */
    private String country;

    /**
     * Region of the city
     */
    private String region;

    /**
     * Currency used the city
     */
    private String currency;

    /**
     * Latitude of the city
     */
    private String latitude;

    /**
     * Longitude of the city
     */
    private String longitude;

    public City(String cityName, String country, String region, String currency, String latitude, String longitude) {
        this(-1, cityName, country, region, currency, latitude, longitude);
    }

    public City(long cityID, String cityName, String country, String region, String currency, String latitude, String longitude) {
        this.cityID = cityID;
        this.cityName = cityName;
        this.country = country;
        this.region = region;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getCityID() {
        return cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return cityName.equals(city.cityName) &&
                country.equals(city.country) &&
                region.equals(city.region) &&
                currency.equals(city.currency) &&
                latitude.equals(city.latitude) &&
                longitude.equals(city.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country, region, currency, latitude, longitude);
    }

    @Override
    public String toString() {
        return cityName;
    }
}
