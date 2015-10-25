package com.landofoz.commonland.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericm on 10/17/2015.
 */
public class GraphNode {
    private long id;
    private Location location;
    List<GraphNode> neighbors;

    GraphNode(Location l, List<GraphNode> neighbors) {
        location = l;
        neighbors = new ArrayList<GraphNode>();
    }



    public long getId() {
        return id;
    }

    public Location getLocation(){
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
