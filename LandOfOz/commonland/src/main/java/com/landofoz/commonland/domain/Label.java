package com.landofoz.commonland.domain;

/**
 * Created by ericm on 10/17/2015.
 */
public class Label {

    private long id;
    private String name;
    private Location location;

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
