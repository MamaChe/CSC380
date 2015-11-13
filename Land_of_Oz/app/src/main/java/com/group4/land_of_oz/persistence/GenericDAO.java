package com.group4.land_of_oz.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by ericm on 10/21/2015.
 */
public abstract class GenericDAO extends SQLiteOpenHelper implements BaseColumns {

    protected static final String TEXT_TYPE = " TEXT";
    protected static final String INTEGER_TYPE = " INTEGER";
    protected static final String FLOAT_TYPE = " REAL";
    protected static final String COMMA_SEP = ",";
    protected String SQL_CREATE_ENTRIES;
    protected String SQL_DELETE_ENTRIES;
    private String TABLE_NAME;
    protected SQLiteDatabase db;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LandOfOz.db";

    public GenericDAO(Context context, String TABLE_NAME, String SQL_CREATE_ENTRIES) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.TABLE_NAME = TABLE_NAME;
        SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
        this.SQL_CREATE_ENTRIES = SQL_CREATE_ENTRIES;
        db = this.getWritableDatabase();
        try {
            db.execSQL(SQL_CREATE_ENTRIES);
        } catch(SQLException e) {
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // for data used as a cache for online data its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    protected long insert(ContentValues values) {
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    protected boolean update(long id, ContentValues values) {
        // Which row to update, based on the ID
        String selection = _ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        int count = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count > 0;
    }

    public boolean remove(long id) {
        // Which row to remove, based on the ID
        String selection = _ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        return db.delete(TABLE_NAME, selection, selectionArgs) > 0;
    }

}
