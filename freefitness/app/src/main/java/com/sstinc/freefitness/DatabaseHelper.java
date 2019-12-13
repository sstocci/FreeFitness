package com.sstinc.freefitness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    // This is currently setup to work with the in UserData Table
    private static final int DATABASE_VERSION = 1;
    private final static String tableName = "User_Data";
    private final static String tag = "DatabaseHelper Class";
    private final static String databaseName = "workoutDB.db";
    private final static String col1 = "ID";
    private final static String col2 = "date";
    private final static String col3 = "exercise";
    private final static String col4 = "setNumber";
    private final static String col5 = "weight";
    private final static String col6 = "reps";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        // Initialize our Database
        super(context, databaseName, factory, DATABASE_VERSION);    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //If this is the first time storing data we must first create the database which consists of 6 columns:
        // Column 1 - User ID - this is from our Firebase Authentication (would be helpful if we wanted to store data online)
        // Column 2 - Date YYYY//MM//DD HH:mm:ss
        // Column 3 - Exercise -  "Bench press", "Squat",... etc
        // Column 4 - SetNumber - set 1, set 2, set 3,...
        // Column 5 - Weight - Any number > 0
        // Column 6 - Reps- How many times they completed the exercise (0,1,2...,infinity)
        String createTable =  "CREATE TABLE " + tableName + "("+
                col1 +" TEXT,"+
                col2 + " TEXT,"+ col3 + " TEXT,"+col4 + " INTEGER,"+col5 + " DOUBLE,"+ col6 + " INTEGER," +
                "PRIMARY KEY (ID, date,exercise,setNumber)"+")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Used to upgrade our database from an older version to a newer one.
    }

    public ArrayList<List> loadData() {
        // An array List to store all the column data- this is what we will return at the end
        ArrayList<List> databaseData= new ArrayList<>(6);

        // SQLite query - we want to select all columns from the UserData Table
        String query = "Select * FROM " + tableName;

        // Stores each column as a list, here we just initialize them
        List<String> users = new ArrayList<String>();
        List<String> dates = new ArrayList<String>();
        List<String> exercise = new ArrayList<String>();
        List<Integer> setNumber = new ArrayList<Integer>();
        List<Double> weight = new ArrayList<Double>();
        List<Integer> reps = new ArrayList<Integer>();

        // Get a reference to our database - We can only read using this call
        SQLiteDatabase db = this.getReadableDatabase();

        // Our cursor we move over each column for each row and allow us to store the data
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String userData = cursor.getString(0);
            String dateData = cursor.getString(1);
            String exerciseData = cursor.getString(2);
            int setData = cursor.getInt(3);
            double weightData = cursor.getDouble(4);
            int repsData = cursor.getInt(5);
            users.add(userData);
            dates.add(dateData);
            exercise.add(exerciseData);
            setNumber.add(setData);
            weight.add(weightData);
            reps.add(repsData);
        }

        // Now we can close our database reference since we extracted all the data needed
        cursor.close();
        db.close();

        // Take our ArrayList<List> databaseData and add data at each index
        databaseData.add(0,users);
        databaseData.add(1,dates);
        databaseData.add(2,exercise);
        databaseData.add(3,setNumber);
        databaseData.add(4,weight);
        databaseData.add(5,reps);

        // We now return all the data we obtained from the database
        return databaseData;
    }
    public void addData(String id,String exceriseName,int setNumber, double weight,int reps){
        // Allows us to create a row in the database which we will call value
        ContentValues values = new ContentValues();

        // In value we have to add data for each column
        values.put(col1,id);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

//         used to generate fake date data
//        String todate = dateFormat.format(date);
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -2);
//        Date todate1 = cal.getTime();
//        String fromdate = dateFormat.format(todate1);

        values.put(col2, dateFormat.format(date));
        //values.put(col2, "2019/12/8");
        values.put(col3,exceriseName);
        values.put(col4,setNumber);
        values.put(col5,weight);
        values.put(col6,reps);

        // Get a reference to our database then we insert our new row or "value" to the database then close the database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }
    public ArrayList<List> getQueryData(String exerciseName) {
        // An array List to store all the column data- this is what we will return at the end
        ArrayList<List> databaseData= new ArrayList<>();

        // SQLite query - we want to select all columns from the UserData Table
        String query = "SELECT date,MAX(weight)\n" +
                "FROM User_Data\n" +
                "WHERE exercise = '" + exerciseName +"' \n"+
                "Group by date \n"+
                "ORDER BY date ASC";


        // Stores each column as a list, here we just initialize them
        List<String> dates = new ArrayList<String>();
        List<Double> weight = new ArrayList<Double>();

        // Get a reference to our database - We can only read using this call
        SQLiteDatabase db = this.getReadableDatabase();

        // Our cursor we move over each column for each row and allow us to store the data
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++) {
            String dateData = cursor.getString(0);
            double weightData = cursor.getDouble(1);
            dates.add(dateData);
            weight.add(weightData);
            cursor.moveToNext();

        }

        // Now we can close our database reference since we extracted all the data needed
        cursor.close();
        db.close();

        // Take our ArrayList<List> databaseData and add data at each index
        databaseData.add(0,dates);
        databaseData.add(1,weight);

        // We now return all the data we obtained from the database
        return databaseData;
    }
    public void deleteRow(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(tableName,"date=?", new String[]{ "Thu Dec 12 14:48:37 EST 2019"});
        Log.d("Deleted Hopefully","We balling");
        db.close();
    }
}
