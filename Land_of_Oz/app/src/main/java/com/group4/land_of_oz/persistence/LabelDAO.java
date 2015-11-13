package com.group4.land_of_oz.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.group4.land_of_oz.domain.Label;
import com.group4.land_of_oz.domain.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericm on 10/21/2015.
 */
public class LabelDAO extends GenericDAO {

    public final static String TABLE_NAME = "label";
    public final static String COLUMN_NAME_NAME = "name";
    public final static String COLUMN_NAME_LOCATION_ID = "location_id";

    Context context;

    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            COLUMN_NAME_LOCATION_ID + INTEGER_TYPE +
            " )";

    public LabelDAO(Context context) {
        super(context, TABLE_NAME, SQL_CREATE);
        this.context = context;
    }

    private ContentValues getContentValues(Label label){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, label.getName());
        values.put(COLUMN_NAME_LOCATION_ID, label.getLocation().getId());
        return values;
    }

    public long insert(Label label){
        return super.insert(getContentValues(label));
    }

    public boolean update(Label label){
        return super.update(label.getId(), getContentValues(label));
    }

    private List<Label> getLabels(Cursor cursor, Context context) {
        List<Label> labels = new ArrayList<Label>();
        Label label;
        Location location;
        LocationDAO locationDAO = new LocationDAO(context);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                label = new Label();
                label.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));
                long location_id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NAME_LOCATION_ID));
                location = locationDAO.findById(location_id);
                label.setLocation(location);
                label.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_NAME)));
                labels.add(label);
            } while (cursor.moveToNext());
        }
        return labels;
    }

    public Label findById(long id){
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                _ID,
                COLUMN_NAME_NAME,
                COLUMN_NAME_LOCATION_ID
        };

        String where = " "+_ID+" = ? ";

        String[] whereValues = {Long.toString(id)};

// How you want the results sorted in the resulting Cursor
        String sortOrder = null;
        // _ID + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                where,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Label> labels = getLabels(cursor, context);
        return labels.size()==1?labels.get(0):null;
    }

}
