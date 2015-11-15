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
        return getBestPathAux(nodeOrigin, nodeDestination, typeOfPreference);
    }

    private List<Location> getBestPathAux(GraphNode origin, GraphNode destination, int typeOfPreferences) {
        List<Location> path = null;
        origin.visited = true;
        if (origin.getId()==destination.getId()) {
            path = new ArrayList<Location>();
            path.add(origin.getLocation());
        } else {
            //first the same floor
            for (GraphNode neighbor : origin.getNeighbors()) {
                if(neighbor.getLocation().getFloor().getId()==origin.getLocation().getFloor().getId()) {
                    if (((typeOfPreferences == Location.ELEVATOR && neighbor.getLocation().getType() != Location.STAIRWAY)
                            || (typeOfPreferences == Location.STAIRWAY && neighbor.getLocation().getType() != Location.ELEVATOR)
                            || (typeOfPreferences != Location.STAIRWAY && typeOfPreferences != Location.ELEVATOR)) && !neighbor.visited) {
                        path = getBestPathAux(neighbor, destination, typeOfPreferences);
                        if (path != null) {
                            path.add(neighbor.getLocation());
                            break;
                        }
                    }
                }
            }
            //and then the rest
            for (GraphNode neighbor : origin.getNeighbors()) {
                if(neighbor.getLocation().getFloor().getId()!=origin.getLocation().getFloor().getId()) {
                    if (((typeOfPreferences == Location.ELEVATOR && neighbor.getLocation().getType() != Location.STAIRWAY)
                            || (typeOfPreferences == Location.STAIRWAY && neighbor.getLocation().getType() != Location.ELEVATOR)
                            || (typeOfPreferences != Location.STAIRWAY && typeOfPreferences != Location.ELEVATOR))  && !neighbor.visited) {
                        path = getBestPathAux(neighbor, destination, typeOfPreferences);
                        if (path != null) {
                            path.add(neighbor.getLocation());
                            break;
                        }
                    }
                }
            }
        }
        if(path!=null) Collections.reverse(path);
        return path;
    }

    public List<Location> getBestPath(Location origin, int specialType, int typeOfPreference) {
        GraphNode nodeOrigin = graph.getNodeByLocation(origin);
        GraphNode.resetVisitedTag(graph);
        return getBestPathAux(nodeOrigin, specialType, typeOfPreference);
    }

    private List<Location> getBestPathAux(GraphNode origin, int specialType, int typeOfPreferences) {
        List<Location> path = null;
        origin.visited = true;
        if (origin.getLocation().getType()==specialType) {
            path = new ArrayList<Location>();
            path.add(origin.getLocation());
        } else {
            //first the same floor
            for (GraphNode neighbor : origin.getNeighbors()) {
                if(neighbor.getLocation().getFloor().getId()==origin.getLocation().getFloor().getId()) {
                    if (((typeOfPreferences == Location.ELEVATOR && neighbor.getLocation().getType() != Location.STAIRWAY)
                            || (typeOfPreferences == Location.STAIRWAY && neighbor.getLocation().getType() != Location.ELEVATOR)
                            || (typeOfPreferences != Location.STAIRWAY && typeOfPreferences != Location.ELEVATOR)) && !neighbor.visited) {
                        path = getBestPathAux(neighbor, specialType, typeOfPreferences);
                        if (path != null) {
                            path.add(neighbor.getLocation());
                            break;
                        }
                    }
                }
            }
            //and then the rest
            for (GraphNode neighbor : origin.getNeighbors()) {
                if(neighbor.getLocation().getFloor().getId()!=origin.getLocation().getFloor().getId()) {
                    if (((typeOfPreferences == Location.ELEVATOR && neighbor.getLocation().getType() != Location.STAIRWAY)
                            || (typeOfPreferences == Location.STAIRWAY && neighbor.getLocation().getType() != Location.ELEVATOR)
                            || (typeOfPreferences != Location.STAIRWAY && typeOfPreferences != Location.ELEVATOR)) && !neighbor.visited) {
                        path = getBestPathAux(neighbor, specialType, typeOfPreferences);
                        if (path != null) {
                            path.add(neighbor.getLocation());
                            break;
                        }
                    }
                }
            }
        }
        if(path!=null) Collections.reverse(path);
        return path;
    }

}
