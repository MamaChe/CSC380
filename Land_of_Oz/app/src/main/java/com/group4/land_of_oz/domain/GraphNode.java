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
    public boolean visited = false;

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
        resetVisitedTag(this);
        return getNodeByLocationAux(origin, this);
    }

    public GraphNode getNodeByLocationAux(Location origin, GraphNode g) {
        this.visited = true;
        if(origin.getLatitude() == this.location.getLatitude() && origin.getLongitude() == this.getLocation().getLongitude())
            return this;
        for (GraphNode neighbor: neighbors) {
            if(neighbor!=null && !neighbor.visited)
                return neighbor.getNodeByLocationAux(origin,neighbor);
        }
        return null;
    }


    public static void resetVisitedTag(GraphNode g){
        g.visited = false;
        if(g.getNeighbors()!=null && g.getNeighbors().size()>0){
            for (GraphNode n: g.getNeighbors()) {
                if(n!= null && n.visited){
                    resetVisitedTag(n);
                }
            }
        }
    }

}
