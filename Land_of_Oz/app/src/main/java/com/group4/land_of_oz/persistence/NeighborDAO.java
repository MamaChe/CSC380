package com.group4.land_of_oz.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.group4.land_of_oz.domain.GraphNode;
import com.group4.land_of_oz.domain.Neighbor;

import java.util.ArrayList;
import java.util.List;

//import java.util.ArrayList;
//import java.util.List;

public class NeighborDAO extends GenericDAO{

    public final static String TABLE_NAME = "neighbor";
    public final static String COLUMN_NAME_NODE_ID = "node_id";
    public final static String COLUMN_NAME_NEIGHBOR_ID = "neighbor_id";

    Context context;

    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_NODE_ID + INTEGER_TYPE + COMMA_SEP +
            COLUMN_NAME_NEIGHBOR_ID + INTEGER_TYPE + COMMA_SEP +
            " )";

    public NeighborDAO(Context context) {
        super(context, TABLE_NAME, SQL_CREATE);
        this.context = context;
    }

    public static ContentValues getContentValues(Neighbor neighbor){
        ContentValues values = new ContentValues();
         values.put(COLUMN_NAME_NODE_ID, neighbor.getId());
         values.put(COLUMN_NAME_NEIGHBOR_ID, neighbor.getNeighbor().getId());
         values.put(_ID, neighbor.getId());
        return values;
    }

    public long insert(Neighbor neighbor){
    	 return super.insert(getContentValues(neighbor));
    }

    public boolean remove(Long neighbor_id){
    	 return super.remove(neighbor_id);
    }

    public boolean update(long id,Neighbor neighbor){
    	return super.update(neighbor.getId(), getContentValues(neighbor));
    }
    
    private List<Neighbor> getNeighbors(Cursor cursor, Context context) {
        List<Neighbor> neighbors = new ArrayList<Neighbor>();
        GraphNode node;
        GraphNode neighborGraph;
        Neighbor neighbor;
        GraphNodeDAO graphNodeDao = new GraphNodeDAO(context);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                neighbor = new Neighbor();

                long node_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_NODE_ID));
                long neighbor_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_NEIGHBOR_ID));

                node = graphNodeDao.findById(node_id);
                neighborGraph = graphNodeDao.findById(neighbor_id);

                neighbor.setNode(node);
                neighbor.setNeighbor(neighborGraph);
                neighbor.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));

                neighbors.add(neighbor);
            } while (cursor.moveToNext());
        }
        return neighbors;
    }


    public Neighbor findbyid(long id, Neighbor neighbor) {

		      String[] projection = {
                _ID,
                COLUMN_NAME_NODE_ID,
                COLUMN_NAME_NEIGHBOR_ID,
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

        List<Neighbor> neighbors = getNeighbors(cursor, context);
        return neighbors.size()!=0?neighbors.get(0):null;
	}

    public List<Long> findNeighborsByNode(long node_id) {
        String[] projection = {
                _ID,
                COLUMN_NAME_NODE_ID,
                COLUMN_NAME_NEIGHBOR_ID,
        };
        String where = " "+COLUMN_NAME_NODE_ID+" = ? ";

        String[] whereValues = {Long.toString(node_id)};

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

        List<Neighbor> neighbors = getNeighbors(cursor, context);
        List<Long> ids = new ArrayList<>();
        for (Neighbor n: neighbors) {
            ids.add(n.getNeighbor().getId());
        }
        return ids;
    }
}
