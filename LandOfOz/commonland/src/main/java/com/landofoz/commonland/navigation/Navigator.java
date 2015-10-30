package com.landofoz.commonland.navigation;

import com.landofoz.commonland.domain.GraphNode;
import com.landofoz.commonland.domain.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericm on 10/17/2015.
 */
public class Navigator {

    GraphNode graph;

    public Navigator(){
        //graph = new GraphNodeDAO().getGraph();
    }

    public List<Location> getBestPath(Location origin, Location destination, int type){
        GraphNode root = graph.getNodeByLocation(origin);
        return getBestPathAux(graph.getNodeByLocation(origin),graph.getNodeByLocation(destination), type);
    }

    private List<GraphNode> getBestPathAux(GraphNode origin, GraphNode destination, int type) {
        List<GraphNode> path = new ArrayList<GraphNode>();
        if(origin.equals(destination)){
            path.add(origin);
            return path;
        } else {
            for (GraphNode neighbor: origin.getNeighbors()) {

            }
        }
        return null;
    }

    ;

}
