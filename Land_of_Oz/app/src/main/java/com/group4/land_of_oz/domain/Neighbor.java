package com.group4.land_of_oz.domain;

import com.group4.land_of_oz.persistence.Persistent;

/**
 * Created by ericm on 10/17/2015.
 */
public class Neighbor  extends Persistent {

    GraphNode node;
    GraphNode neighbor;

	public GraphNode getNode() {
        return node;
    }

    public void setNode(GraphNode node) {
        this.node = node;
    }
	
    public GraphNode getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(GraphNode neighbor) {
        this.neighbor = neighbor;
    }
}
