package com.example.medicial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_Name = "Medicial.db";

    public DBHelper(Context context) {
        super(context, DB_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table User (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT, email TEXT, password TEXT)");
        sqLiteDatabase.execSQL("create table Medicine (id INEGER PRIMARY KEY AUTOINCREMENT, name TEXT, amount INTEGER)");
        sqLiteDatabase.execSQL("create table List ( supplayAmount INTEGER, creationDate DATE,  FOREIGN KEY(userID) REFERENCES User(id), FOREIGN KEY(alertID) REFERENCES Alert(id),FOREIGN KEY(medicineID) REFERENCES Medicine(id))");
        sqLiteDatabase.execSQL("create table Alert (id INTEGER PRIMARY KEY AUTOINCREMENT, time DATE, dates DATE )");
        sqLiteDatabase.execSQL("create table Admin (id INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(userID) REFERENCES User(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Medicine");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS List");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Alert");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Admin");
    }

    public boolean insertUserData(String firstName, String lastName, String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("email", email);
        values.put("password", password);

        long result = sqLiteDatabase.insert("User", null, values);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean CheckUserName(String firstName, String lastName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where firstName = ? and lastName = ?", new String[] {firstName, lastName});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean CheckEmailPassword(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where email = ? and password = ?", new String[] {email, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
