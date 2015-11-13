package com.group4.land_of_oz;

/**
 * Created by ericm on 10/17/2015.
 */
public class Location {

    private long id;

    private float longitude;

    private float latitude;

    private int type;

    private int floor;


    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Location(float lon, float lat, int floor){
        longitude = lon;
        latitude = lat;
        this.floor = floor;
    }
}
