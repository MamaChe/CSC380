package com.landofoz.commonland.navigation;

import com.google.android.gms.maps.model.LatLng;
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
        List<Location> path = new ArrayList<Location>();
        GraphNode root = graph.getRoot(origin);
        

        return null;
    };

}
