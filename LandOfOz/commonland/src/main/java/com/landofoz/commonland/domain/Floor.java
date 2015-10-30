package com.landofoz.commonland.domain;

/**
 * Created by ericm on 10/17/2015.
 */
public class Floor {
    private long id;
    private String name;
    private int level;

    public long getId() {
       return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
