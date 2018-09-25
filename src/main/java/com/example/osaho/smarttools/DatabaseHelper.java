package com.example.osaho.smarttools;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Program to create and insert data into an SQLite database
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "unit.db";
    public static final String TABLE_NAME = "units_table";
    public static final String COL_1= "INCH";
    public static final String COL_2= "FEET";
    public static final String COL_3= "YARD";
    public static final String COL_4= "KILOMETER";
    public static final String COL_5= "MILE";

    /**
     * Overloaded constructor
     * @param context is the Context variable
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

    }

    /**
     * Function to create SQLite database table on installation
     * @param sqLiteDatabase is the object of the SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Create table on execution
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (INCH VARCHAR(15), FEET VARCHAR(15), YARD VARCHAR(15), KILOMETER VARCHAR(15), MILE VARCHAR(15))");

    }


    /**
     * Function to drop the table on upgrade or uninstallation
     * @param sqLiteDatabase is the SQLite database object
     * @param i the first integer
     * @param i1 is the second integer
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME); //on removal of the app drop the table

        onCreate(sqLiteDatabase);
    }


    /**
     * Function to insert data into the SQLite Datase
     * @param inch is the first String
     * @param feet is the second String
     * @param yard is the third String
     * @param kilometer is the fourth String
     * @param mile is the fifth String
     * @return true if data has been inserted otherwise return false
     */
    public boolean insertData( String inch, String feet, String yard, String kilometer,String mile){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, inch);
        contentValues.put(COL_2, feet);
        contentValues.put(COL_3, yard);
        contentValues.put(COL_4, kilometer);
        contentValues.put(COL_5, mile);

        //Insering all the values on the contentValues into the database
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result== -1)
            return false;
        else
            return true;
    }


    /**
     * Functiont to read all the data from the SQLite Database
     * @return data read from the database
     */
    public Cursor getAllData(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor data = sqLiteDatabase.rawQuery("select * from " +TABLE_NAME,null);

        return data;
    }
}
