package com.landofoz.commonland.navigation;

import android.content.Context;

import com.landofoz.commonland.domain.GraphNode;
import com.landofoz.commonland.domain.Location;
import com.landofoz.commonland.persistence.GraphNodeDAO;

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

    public List<Location> getBestPath(Location origin, Location destination, int type) {
        GraphNode nodeOrigin = graph.getNodeByLocation(origin);
        GraphNode nodeDestination = graph.getNodeByLocation(destination);
        return getBestPathAux(nodeOrigin, nodeDestination, type);
    }

    private List<Location> getBestPathAux(GraphNode origin, GraphNode destination, int type) {
        List<Location> path = null;
        if (origin.equals(destination)) {
            path = new ArrayList<Location>();
            path.add(origin.getLocation());
        } else {
            for (GraphNode neighbor : origin.getNeighbors()) {
                if ((type == Location.ELEVATOR && neighbor.getLocation().getType() != Location.STAIRWAY)
                        || (type == Location.STAIRWAY && neighbor.getLocation().getType() != Location.ELEVATOR)
                        || (type != Location.STAIRWAY && type != Location.ELEVATOR)) {
                    path = getBestPathAux(neighbor, destination, type);
                    if (path!=null) {
                        path.add(neighbor.getLocation());
                        break;
                    }
                }
            }
        }
        if(path!=null) Collections.reverse(path);
        return path;
    }

}
