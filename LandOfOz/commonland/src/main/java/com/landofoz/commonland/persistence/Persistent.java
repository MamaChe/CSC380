package com.landofoz.commonland.persistence;

/**
 * Created by ericm on 10/30/2015.
 */
public abstract class Persistent {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
