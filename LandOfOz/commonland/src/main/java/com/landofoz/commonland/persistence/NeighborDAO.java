package com.landofoz.commonland.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.provider.BaseColumns;

import com.landofoz.commonland.domain.GraphNode;
import com.landofoz.commonland.domain.Location;

//import java.util.ArrayList;
//import java.util.List;

public class NeighborDAO extends GenericDAO{

    public final static String TABLE_NAME = "neighbor";
    public final static String COLUMN_NAME_NODE_ID = "node_id";
    public final static String COLUMN_NAME_NEIGHBOR_ID = "neighbor_id";

    Context context;

    private static final String SQL_CREATE = "CREATE TABLE " + NEIGHBORS_TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_NODE_ID + INTEGER_TYPE + COMMA_SEP +
            COLUMN_NAME_NEIGHBOR_ID + INTEGER_TYPE + COMMA_SEP +
            " )";

    public GraphNodeDAO(Context context) {
        super(context, TABLE_NAME, SQL_CREATE);
        try {
            db.execSQL(SQL_CREATE_NEIGHBORS);
        } catch(SQLException e) {
            System.out.println(e.getStackTrace());
        }
        this.context = context;
    }

    public static ContentValues getContentValues(Neighbor neighbor){
        ContentValues values = new ContentValues();
         values.put(COLUMN_NAME_LOCATION, graphNode.getLocation());
         values.put(COLUMN_NAME_NEIGHBORS, graphNode.getNeighbors());
         values.put(_ID, graphNode.getId());
        return values;
    }

    public boolean insert(GraphNode graphNode){
    	 return super.insert(getContentValues(graphNode));
    }

    public boolean remove(GraphNode graphNode){
    	 return super.remove(getContentValues(graphNode));
    }

    public boolean update(long id,GraphNode graphNode){
    	return super.update(graphNode.getId(), getContentValues(graphNode));
    }
    private List<GraphNode> getGraphNOdes(Cursor cursor, Context context) {
        List<GraphNode> graphNodes = new ArrayList<GraphNode>();
        GraphNode graphNode;
        Location location;
        LocationDAO locationDAO = new LocationDAO(context);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                graphNode = new GraphNode();
                graphNode.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));
                long graphNode_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_GRAPHNODE_ID));
                graphNode = GraphNodeDAO.findById(graphNode_id);
                graphNode.setLocation(graphNode);
                graphNode.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_NAME)));
                graphNodes.add(graphNode);
            } while (cursor.moveToNext());
        }
        return graphNodes;
    }


    public void findbyid(long id, GraphNode graphNode) {

		      String[] projection = {
                _ID,
                COLUMN_NAME_LOCATION,
                COLUMN_NAME_NEIGHBORS,
                COLUMN_NAME_ID
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
        return graphNode.getId();
        List<GraphNode> graphNode = getGraphNOdes(cursor, context);
        return getGraphNOde.get(0):null;
	}
}
