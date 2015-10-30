package com.landofoz.commonland.domain;

import com.landofoz.commonland.persistence.Persistent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericm on 10/17/2015.
 */
public class GraphNode extends Persistent {

    private Location location;
    List<GraphNode> neighbors;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<GraphNode> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<GraphNode> neighbors) {
        this.neighbors = neighbors;
    }
}
