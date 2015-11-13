package com.group4.land_of_oz.domain;

import com.group4.land_of_oz.persistence.Persistent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericm on 10/17/2015.
 */
public class GraphNode  extends Persistent {

    private Location location;
    List<GraphNode> neighbors;

    public GraphNode(){
        neighbors = new ArrayList<>();
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

    public GraphNode getNodeByLocation(Location origin) {
        if(origin.getLatitude() == this.location.getLatitude() && origin.getLongitude() == this.getLocation().getLongitude())
            return this;
        for (GraphNode neighbor: neighbors) {
            neighbor.getNodeByLocation(origin);
        }
        return null;
    }
}
