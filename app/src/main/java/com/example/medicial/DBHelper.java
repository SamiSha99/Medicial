package com.example.medicial;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Medicial.db";
    private static final int DB_VERSION = 1;
/*
    private static final String TABLE_USER = "User";
    private static final String USER_ID = "id";
    private static final String USER_FNAME = "firstName";
    private static final String USER_LNAME = "lastName";
    private static final String USER_PASSWORD = "password";
    private static final String USER_EMAIL ="email";
    */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table User (id INTEGER PRIMARY KEY AUTOINCREMENT,userName Text, firstName Text, lastName TEXT, password TEXT, email TEXT)");
      /*
        sqLiteDatabase.execSQL("create table Medicine (id INEGER PRIMARY KEY AUTOINCREMENT, name TEXT, amount INTEGER)");
        sqLiteDatabase.execSQL("create table List ( supplayAmount INTEGER, creationDate DATE,  FOREIGN KEY(userID) REFERENCES User(id), FOREIGN KEY(alertID) REFERENCES Alert(id),FOREIGN KEY(medicineID) REFERENCES Medicine(id))");
        sqLiteDatabase.execSQL("create table Alert (id INTEGER PRIMARY KEY AUTOINCREMENT, time DATE, dates DATE )");
        sqLiteDatabase.execSQL("create table Admin (id INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(userID) REFERENCES User(id))");
    */
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        /*
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Medicine");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS List");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Alert");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Admin");
        */
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // Adding new User Details
    boolean insertUserData(String userName, String firstName, String lastName, String password, String email){
        //Get the Data Repository in write mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
                 // Key in table user
        values.put("userName", userName);
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("password", password);
        values.put("email", email);

        // Insert the new row, returning the primary key value of the new row
        long newRow = sqLiteDatabase.insert("User", null, values);
        if (newRow == -1){
            return false;
        }else {
            sqLiteDatabase.close();
            return true;
        }
    }

    public boolean CheckUserName(String userName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where userName = ?", new String[] {userName});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean CheckUserPassword(String userName, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where userName = ? and password = ?", new String[] {userName, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


//    public ArrayList getUserName(){
//        ArrayList arrayList = new ArrayList();
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery("select * from User",null);
//        cursor.moveToFirst();
//
//        while (cursor.isAfterLast() == false){
//            String getName = cursor.getString(1);
//            arrayList.add(getName);
//            cursor.moveToNext();
//        }
//        return arrayList;
//    }
}
