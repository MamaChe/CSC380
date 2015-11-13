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
            COLUMN_NAME_LOCATION + INTEGER_TYPE + COMMA_SEP +
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
            neighborDao.update(n.getId(),n);
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

                List<Long> neighbors_id = neighborsDAO.findNeighborsByNode(graphNode.getId());
                List<GraphNode> neighbors = new ArrayList<>();
                for (Long id: neighbors_id) {
                    neighbors.add(findById(id));
                }
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
            String where = " "+_ID+" = ? ";

            String[] whereValues = {Long.toString(id)};

            String sortOrder = null;

            Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                where,
                whereValues,
                null,
                null,
                sortOrder
        );
        List<GraphNode> graphNodes = getGraphNodes(cursor, context);
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

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                where,
                whereValues,
                null,
                null,
                sortOrder,
                "1"
        );
        List<GraphNode> graphNodes = getGraphNodes(cursor, context);
        return graphNodes.size()!=0?graphNodes.get(0):null;
    }

}
