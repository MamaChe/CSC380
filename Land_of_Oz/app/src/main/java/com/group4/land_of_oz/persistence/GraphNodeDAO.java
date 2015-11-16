package com.group4.land_of_oz.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.group4.land_of_oz.domain.GraphNode;
import com.group4.land_of_oz.domain.Location;
import com.group4.land_of_oz.domain.Neighbor;

import java.util.ArrayList;
import java.util.List;

//import java.util.ArrayList;
//import java.util.List;

public class GraphNodeDAO extends GenericDAO{

    public final static String TABLE_NAME = "graphNode";
    public final static String COLUMN_NAME_LOCATION = "location_id";

    Context context;

    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_LOCATION + INTEGER_TYPE +
            " )";

    public GraphNodeDAO(Context context) {
        super(context, TABLE_NAME, SQL_CREATE);
        this.context = context;
    }

    private ContentValues getContentValues(GraphNode graphNode){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_LOCATION, graphNode.getLocation().getId());
        values.put(_ID, graphNode.getId());
        return values;
    }

    public long insert(GraphNode graphNode){
	    NeighborDAO neighborDao = new NeighborDAO(context);
        List<Neighbor> neighbors = getNeighbors(graphNode);
        for (Neighbor n: neighbors) {
            neighborDao.insert(n);
        }
        return super.insert(getContentValues(graphNode));
    }

    public boolean remove(GraphNode graphNode){
        NeighborDAO neighborDao = new NeighborDAO(context);
        List<Neighbor> neighbors = getNeighbors(graphNode);
        for (Neighbor n: neighbors) {
            neighborDao.remove(n.getId());
        }
    	 return super.remove(graphNode.getId());
    }

    public List<Neighbor> getNeighbors(GraphNode graphNode){
        List<Neighbor> neighbors = new ArrayList<>();
        Neighbor neighbor = new Neighbor();
        for (GraphNode n: graphNode.getNeighbors()) {
            neighbor.setNode(graphNode);
            neighbor.setNeighbor(n);
            neighbors.add(neighbor);
        }
        return neighbors;
    }

    public boolean update(long id,GraphNode graphNode){
        NeighborDAO neighborDao = new NeighborDAO(context);
        List<Neighbor> neighbors = getNeighbors(graphNode);
        for (Neighbor n: neighbors) {
            neighborDao.update(n.getId(), n);
        }
		return super.update(graphNode.getId(), getContentValues(graphNode));
    }

    private List<GraphNode> getGraphNodes(Cursor cursor, Context context) {
        List<GraphNode> graphNodes = new ArrayList<GraphNode>();
        GraphNode graphNode;
        Location location;
        LocationDAO locationDAO = new LocationDAO(context);
        NeighborDAO neighborsDAO = new NeighborDAO(context);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                graphNode = new GraphNode();
                graphNode.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));

                long location_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_LOCATION));
                location = locationDAO.findById(location_id);
                graphNode.setLocation(location);

                List<GraphNode> neighbors = neighborsDAO.findNeighborsByNode(graphNode.getId());
                graphNode.setNeighbors((ArrayList<GraphNode>) neighbors);

                graphNodes.add(graphNode);
            } while (cursor.moveToNext());
        }
        return graphNodes;
    }


    public GraphNode findById(long id) {

		      String[] projection = {
                _ID,
                COLUMN_NAME_LOCATION,
        };
        String where = _ID+" = "+Long.toString(id);

        String[] whereValues = null;

            String sortOrder = null;

        Cursor cursor = null;
        List<GraphNode> graphNodes;
        try {
            cursor = db.query(
                    TABLE_NAME,
                    projection,
                    where,
                    whereValues,
                    null,
                    null,
                    sortOrder
            );
            graphNodes = getGraphNodes(cursor, context);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return graphNodes.size()!=0?graphNodes.get(0):null;
	}


    public GraphNode getGraph() {
        String[] projection = {
                _ID,
                COLUMN_NAME_LOCATION,
        };
        String where = "";

        String[] whereValues = null;

        String sortOrder = null;

        Cursor cursor = null;
        List<GraphNode> graphNodes;
        try {
            cursor = db.query(
                    TABLE_NAME,
                    projection,
                    where,
                    whereValues,
                    null,
                    null,
                    sortOrder
            );
            graphNodes = getGraphNodes(cursor, context);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        //return graphNodes.size()!=0?graphNodes.get(0):null;
        return graphNodes.size()!=0? getGraphAux(graphNodes.get(0)):null;
    }

    //When I wrote this, only God and I understood what I was doing
    //Now, God only knows
    public GraphNode getGraphAux(GraphNode g){
        g.visited = true;
        List<GraphNode> listToRemove = new ArrayList<>();
        for (GraphNode n: g.getNeighbors()) {
            if (!n.visited)
                listToRemove.add(n);
        }
        for (GraphNode n: listToRemove) {
            g.getNeighbors().remove(n);
            g.getNeighbors().add(findById(n.getId()));
        }

        for (GraphNode n: g.getNeighbors()) {
            GraphNode toRemove = null, neighbor =null;
            if(n!=null) {
                for (GraphNode n2 : n.getNeighbors()) {
                    if (n2.getId() == g.getId() && !n2.visited) {
                        toRemove = n2;
                        neighbor = n;
                        break;
                    }
                }
            }
            if(!(neighbor==null || toRemove==null)) {
                neighbor.getNeighbors().remove(toRemove);
                neighbor.getNeighbors().add(g);
            }
        }

        for (GraphNode n: g.getNeighbors()) {
            if (n!=null && !n.visited)
                getGraphAux(n);
        }
        return g;
    }

}