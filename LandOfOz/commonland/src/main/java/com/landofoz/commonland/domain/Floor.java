package com.landofoz.commonland.domain;

import com.landofoz.commonland.persistence.Persistent;

/**
 * Created by ericm on 10/17/2015.
 */
public class Floor extends Persistent {

    private String name;
    private int level;

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
