package com.group4.land_of_oz.navigation;

import android.content.Context;

import com.group4.land_of_oz.domain.GraphNode;
import com.group4.land_of_oz.domain.Location;
import com.group4.land_of_oz.persistence.GraphNodeDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ericm on 10/17/2015.
 */
public class Navigator {

    GraphNode graph;

    public Navigator(Context context) {
        graph = new GraphNodeDAO(context).getGraph();
    }

    public Navigator(GraphNode graph) {
        this.graph = graph;
    }

    public List<Location> getBestPath(Location origin, Location destination, int typeOfPreference) {
        GraphNode nodeOrigin = graph.getNodeByLocation(origin);
        GraphNode nodeDestination = graph.getNodeByLocation(destination);
        GraphNode.resetVisitedTag(graph);
        List<Location> ret = getBestPathAux(nodeOrigin, nodeDestination, typeOfPreference);
        Collections.reverse(ret);
        return ret;
    }

    public List<Location> getBestPath(Location origin, int specialType, int typeOfPreference) {
        GraphNode nodeOrigin = graph.getNodeByLocation(origin);
        GraphNode.resetVisitedTag(graph);
        List<Location> ret = getBestPathAux(nodeOrigin, specialType, typeOfPreference);
        Collections.reverse(ret);
        return ret;
    }

    private List<Location> getBestPathAux(GraphNode origin, GraphNode destination, int typeOfPreference) {
        List<Location> ret;
        origin.visited = true;
        if(origin!=null) {
            if (origin.getId() == destination.getId()) {
                List<Location> found = new ArrayList<>();
                found.add(origin.getLocation());
                return found;
            }
            //first same floor
            for (GraphNode neighbor : origin.getNeighbors()) {
                if (neighbor != null && !neighbor.visited
                        && origin.getLocation().getFloor().getLevel() == neighbor.getLocation().getFloor().getLevel()) {
                    List<Location> check = getBestPathAux(neighbor, destination, typeOfPreference);
                    if (check != null) {
                        ret = check;
                        ret.add(origin.getLocation());
                        return ret;
                    }
                }
            }
            //other floors with preference of stairs/elevator
            for (GraphNode neighbor : origin.getNeighbors()) {
                if (neighbor != null && !neighbor.visited
                        && modeMach(typeOfPreference, neighbor)) {
                    List<Location> check = getBestPathAux(neighbor, destination, typeOfPreference);
                    if (check != null) {
                        ret = check;
                        ret.add(origin.getLocation());
                        return ret;
                    }
                }
            }
            //any possible way
            for (GraphNode neighbor : origin.getNeighbors()) {
                if (neighbor != null && !neighbor.visited) {
                    List<Location> check = getBestPathAux(neighbor, destination, typeOfPreference);
                    if (check != null) {
                        ret = check;
                        ret.add(origin.getLocation());
                        return ret;
                    }
                }
            }
        }
        return null;
    }

    private List<Location> getBestPathAux(GraphNode origin, int specialType, int typeOfPreference) {
        List<Location> ret;
        origin.visited = true;
        if (origin.getLocation().getType() == specialType) {
            List<Location> found = new ArrayList<>();
            found.add(origin.getLocation());
            return found;
        }
        //first same floor
        for (GraphNode neighbor : origin.getNeighbors()) {
            if (neighbor != null && !neighbor.visited
                    && origin.getLocation().getFloor().getLevel() == neighbor.getLocation().getFloor().getLevel()) {
                List<Location> check = getBestPathAux(neighbor, specialType, typeOfPreference);
                if (check != null) {
                    ret = check;
                    ret.add(origin.getLocation());
                    return ret;
                }
            }
        }
        //other floors with preference of stairs/elevator
        for (GraphNode neighbor : origin.getNeighbors()) {
            if (neighbor != null && !neighbor.visited
                    && modeMach(typeOfPreference, neighbor)) {
                List<Location> check = getBestPathAux(neighbor, specialType, typeOfPreference);
                if (check != null) {
                    ret = check;
                    ret.add(origin.getLocation());
                    return ret;
                }
            }
        }
        //any possible way
        for (GraphNode neighbor : origin.getNeighbors()) {
            if (neighbor != null && !neighbor.visited) {
                List<Location> check = getBestPathAux(neighbor, specialType, typeOfPreference);
                if (check != null) {
                    ret = check;
                    ret.add(origin.getLocation());
                    return ret;
                }
            }
        }
        return null;
    }

    private boolean modeMach(int typeOfPreferences, GraphNode neighbor) {
        return (typeOfPreferences == Location.ELEVATOR && neighbor.getLocation().getType() != Location.STAIRWAY)
                || (typeOfPreferences == Location.STAIRWAY && neighbor.getLocation().getType() != Location.ELEVATOR)
                || (typeOfPreferences != Location.STAIRWAY && typeOfPreferences != Location.ELEVATOR);
    }


}
