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
public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    protected Context context;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LandOfOz.db";
    private static final String DB_PATH = "/data/data/com.group4.land_of_oz/databases/";
    public static final String DATABASE_COMPLETE_PATH = DB_PATH+DATABASE_NAME;

    private static DatabaseHelper instance = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        prepareDatabase();
    }

    public static DatabaseHelper getInstance(Context context){
        if(instance==null){
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        if(db==null){
            prepareDatabase();
        }
        if(!db.isOpen()){
            openDataBase();
        }
        return db;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();


            dbExist = checkDataBase(); //debug

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {
/*
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_COMPLETE_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false; */

        File dbFile = context.getDatabasePath(DATABASE_COMPLETE_PATH);
        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput;
        myInput = context.getAssets().open(DATABASE_NAME);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(DATABASE_COMPLETE_PATH);

        try {
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
        } finally {
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
    }

    public void openDataBase() throws SQLException {
        //Open the database
        db = SQLiteDatabase.openDatabase (DATABASE_COMPLETE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        if (db == null) throw new Error("db null");
    }

    public void prepareDatabase() {
        boolean ex = checkDataBase();
        if (!checkDataBase()) {
            try {
                createDataBase();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error creating database.");
            }
        }
        openDataBase();
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
