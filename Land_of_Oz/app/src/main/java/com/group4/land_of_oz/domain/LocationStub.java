package com.group4.land_of_oz.domain;

/**
 * Created by ace on 11/15/15.
 */
public class LocationStub extends Location {
    public LocationStub(float lat, float lon, int level){
        super();
        setLatitude(lat);
        setLongitude(lon);
        Floor floor = new Floor();
        floor.setLevel(level);
        setFloor(floor);
    }
}
