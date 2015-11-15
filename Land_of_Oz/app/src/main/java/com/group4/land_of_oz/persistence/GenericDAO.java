package com.group4.land_of_oz.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ericm on 10/21/2015.
 */
public abstract class GenericDAO implements BaseColumns {

    protected static final String TEXT_TYPE = " TEXT";
    protected static final String INTEGER_TYPE = " INTEGER";
    protected static final String FLOAT_TYPE = " REAL";
    protected static final String COMMA_SEP = ",";
    protected String SQL_CREATE_ENTRIES;
    protected String SQL_DELETE_ENTRIES;
    private String TABLE_NAME;
    protected SQLiteDatabase db;
    protected Context context;
    protected static String _ID = "_id";
    protected DatabaseHelper dbHelper;


    public GenericDAO(Context context, String TABLE_NAME, String SQL_CREATE_ENTRIES) {
        db = DatabaseHelper.getInstance(context).getDatabase();
        constructorHelper(context, TABLE_NAME, SQL_CREATE_ENTRIES);
    }

    public void constructorHelper(Context context, String TABLE_NAME, String SQL_CREATE_ENTRIES) {
        this.TABLE_NAME = TABLE_NAME;
        SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
        this.SQL_CREATE_ENTRIES = SQL_CREATE_ENTRIES;
        this.context = context;
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
