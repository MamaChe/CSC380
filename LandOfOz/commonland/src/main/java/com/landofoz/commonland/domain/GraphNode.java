package com.landofoz.commonland.domain;

import java.util.List;

/**
 * Created by ericm on 10/17/2015.
 */
public class GraphNode {
    private Location location;
    private List<GraphNode> neighbors;

    GraphNode(Location l) {
        location = l;
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

    public void setNeighbors(List<GraphNode> neighbors) {
        this.neighbors = neighbors;
    }
}
