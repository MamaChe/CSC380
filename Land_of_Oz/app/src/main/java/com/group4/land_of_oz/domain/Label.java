package com.group4.land_of_oz.domain;

import com.group4.land_of_oz.persistence.Persistent;

/**
 * Created by ericm on 10/17/2015.
 */
public class Label extends Persistent {

    private String name;
    private Location location;

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
