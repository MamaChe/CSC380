package com.group4.land_of_oz.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.group4.land_of_oz.domain.GraphNode;
import com.group4.land_of_oz.domain.Location;
import com.group4.land_of_oz.domain.Neighbor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            COLUMN_NAME_NEIGHBOR_ID + INTEGER_TYPE +
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
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                neighbor = new Neighbor();

                long node_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_NODE_ID));
                long neighbor_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_NEIGHBOR_ID));

                node = new GraphNode();
                neighborGraph = new GraphNode();

                node.setId(node_id);
                neighborGraph.setId(neighbor_id);

                neighbor.setNode(node);
                neighbor.setNeighbor(neighborGraph);
                neighbor.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));

                neighbors.add(neighbor);
            } while (cursor.moveToNext());
        }
        return neighbors;
    }

    //the worst efficiency ever, but we need to avoid erros of database related with connections unidirecionais
    public List<GraphNode> findNeighborsByNode(long node_id) {
        List<GraphNode> nodesNeighbors = new ArrayList<>();
        nodesNeighbors.addAll(findNeighborsByNodeAux1(node_id));
        nodesNeighbors.addAll(findNeighborsByNodeAux2(node_id));
        Set<Long> hash = new HashSet<>();
        for (GraphNode g: nodesNeighbors) {
            hash.add(g.getId());
        }
        hash.clear();
        List<Long> list = new ArrayList<>();
        list.addAll(hash);
        nodesNeighbors = new ArrayList<>();
        for (Long id:list) {
            GraphNode g = new GraphNode();
            g.setId(id);
            nodesNeighbors.add(g);
        }
        return nodesNeighbors;
    }

    public List<GraphNode> findNeighborsByNodeAux1(long node_id) {
        String[] projection = {
                _ID,
                COLUMN_NAME_NODE_ID,
                COLUMN_NAME_NEIGHBOR_ID,
        };
        String where = " "+COLUMN_NAME_NODE_ID+" = "+node_id;

        String[] whereValues = null;

        String sortOrder = null;

        Cursor cursor = null;
        List<Neighbor> neighbors;
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
            neighbors = getNeighbors(cursor, context);
        } finally {
            if (cursor != null)
                cursor.close();
        }

        List<GraphNode> graphs = new ArrayList<>();
        for (Neighbor n: neighbors) {
            graphs.add(n.getNeighbor());
        }
        return graphs;
    }

    public List<GraphNode> findNeighborsByNodeAux2(long node_id) {
        String[] projection = {
                _ID,
                COLUMN_NAME_NODE_ID,
                COLUMN_NAME_NEIGHBOR_ID,
        };
        String where = " "+COLUMN_NAME_NEIGHBOR_ID+" = "+node_id;

        String[] whereValues = null;

        String sortOrder = null;

        Cursor cursor = null;
        List<Neighbor> neighbors;
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
            neighbors = getNeighbors(cursor, context);
        } finally {
            if (cursor != null)
                cursor.close();
        }

        List<GraphNode> graphs = new ArrayList<>();
        for (Neighbor n: neighbors) {
            graphs.add(n.getNeighbor());
        }
        return graphs;
    }

    public List<Neighbor> findAll() {
        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                _ID,
                COLUMN_NAME_NODE_ID,
                COLUMN_NAME_NEIGHBOR_ID
        };

        String where = null;

        String[] whereValues = null;

// How you want the results sorted in the resulting Cursor
        String sortOrder = null;
        // _ID + " DESC";

        Cursor cursor = null;
        List<Neighbor> neighbors;
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
            neighbors = getNeighbors(cursor, context);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return neighbors;
    }

}
