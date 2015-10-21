package com.landofoz.commonland.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.landofoz.commonland.domain.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericm on 10/21/2015.
 */
public class LocationDAO extends GenericDAO {

    public final static String TABLE_NAME = "location";
    public final static String COLUMN_NAME_LATITUDE = "latitude";
    public final static String COLUMN_NAME_LONGITUDE = "longitude";
    public final static String COLUMN_NAME_TYPE = "type";
    public final static String COLUMN_NAME_FLOOR_ID = "floor_id";

    Context context;

    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_LATITUDE + FLOAT_TYPE + COMMA_SEP +
            COLUMN_NAME_LONGITUDE + FLOAT_TYPE + COMMA_SEP +
            COLUMN_NAME_TYPE + INTEGER_TYPE + COMMA_SEP +
            COLUMN_NAME_FLOOR_ID + INTEGER_TYPE +
            " )";

    public LocationDAO(Context context) {
        super(context, TABLE_NAME, SQL_CREATE);
        this.context = context;
    }

    private ContentValues getContentValues(Location location){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_LATITUDE, location.getLatitude());
        values.put(COLUMN_NAME_LONGITUDE, location.getLongitude());
        values.put(COLUMN_NAME_TYPE, location.getType());
        values.put(COLUMN_NAME_FLOOR_ID, location.getFloor().getId());
        return values;
    }

    public long insert(Location location){
        return super.insert(getContentValues(location));
    }

    public boolean update(Location location){
        return super.update(location.getId(), getContentValues(location));
    }

    private List<Location> getLocations(Cursor cursor, Context context) {
        List<Location> locations = new ArrayList<Location>();
        Location location;
        FloorDAO floorDAO = new FloorDAO(context);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            do {
                location = new Location();
                location.setId(cursor.getLong(cursor.getColumnIndexOrThrow(_ID)));
                location.setLatitude(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_LATITUDE)));
                location.setLongitude(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_LONGITUDE)));
                location.setType(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_TYPE)));
                location.setFloor(floorDAO.findFloorByID(cursor.getLong(cursor.getColumnIndexOrThrow(_ID))));
                locations.add(location);
            } while (cursor.moveToNext());
        }
        return locations;
    }

    public List<Location> findByType(int type){
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                _ID,
                COLUMN_NAME_LATITUDE,
                COLUMN_NAME_LONGITUDE,
                COLUMN_NAME_TYPE,
                COLUMN_NAME_FLOOR_ID
        };

        String whereColumn = " "+COLUMN_NAME_TYPE+"=? ";

        String[] whereValues = {Integer.toString(type)};

// How you want the results sorted in the resulting Cursor
        String sortOrder = null;
               // _ID + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                whereColumn,                                // The columns for the WHERE clause
                whereValues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return getLocations(cursor, context);
    }

    public Location findById(long id){
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                _ID,
                COLUMN_NAME_LATITUDE,
                COLUMN_NAME_LONGITUDE,
                COLUMN_NAME_TYPE,
                COLUMN_NAME_FLOOR_ID
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

        List<Location> locations = getLocations(cursor, context);
        return locations.size()==1?locations.get(0):null;
    }

}
