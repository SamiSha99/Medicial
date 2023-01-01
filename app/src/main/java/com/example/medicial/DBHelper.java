package com.example.medicial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Medicial.db";
    private static final int DB_VERSION = 3;
    private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table User (id INTEGER PRIMARY KEY AUTOINCREMENT,userName Text, firstName Text, lastName TEXT, password TEXT, email TEXT)");
        sqLiteDatabase.execSQL("create table Medicine (id INTEGER PRIMARY KEY AUTOINCREMENT, medName TEXT, amount INTEGER)");
        sqLiteDatabase.execSQL("create table Alert (id INTEGER PRIMARY KEY AUTOINCREMENT, time TIME, date DATE)");
//        sqLiteDatabase.execSQL("create table List ( supplyAmount INTEGER, creationDate DATE,  FOREIGN KEY(userID) REFERENCES User(id), FOREIGN KEY(alertID) REFERENCES Alert(id),FOREIGN KEY(medicineID) REFERENCES Medicine(id))");
//        sqLiteDatabase.execSQL("create table Admin (id INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(userID) REFERENCES User(id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Medicine");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Alert");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS List");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Admin");

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    //   {Adding new User Details}
    boolean insertUserData(String userName, String firstName, String lastName, String password, String email) {
        // Get the Data Repository in write mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        // Key in table user
        values.put("userName", userName);
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("password", password);
        values.put("email", email);

        // Insert the new row, returning the primary key value of the new row
        long newRow = sqLiteDatabase.insert("User", null, values);
        if (newRow == -1) {
            return false;
        } else {
            sqLiteDatabase.close();
            return true;
        }
    }

    //   {Check Username}
    public boolean CheckUserName(String userName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where userName = ?", new String[]{userName});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //   {Check Username and Password}
    public boolean CheckUserPassword(String userName, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User where userName = ? and password = ?", new String[]{userName, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public ArrayList<User> GetUserData() {
        ArrayList<User> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from User", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String userName = cursor.getString(1);
            String firstName = cursor.getString(2);
            String lastName = cursor.getString(3);
            String email = cursor.getString(4);
            String password = cursor.getString(5);
            User user = new User(userName, firstName, lastName, password, email);
            arrayList.add(user);
//            cursor.moveToNext();
        }
        return arrayList;
    }

    //   {Adding new Medicine Details}
    boolean insertMedicineData(String medName, int amount) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medName", medName);
        values.put("amount", amount);

        long newRow = sqLiteDatabase.insert("Medicine", null, values);
        if (newRow == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getMedicineData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Medicine", null);
        return cursor;
    }

    //   {Adding new Alert Details}
    boolean insertDateTime(String time, String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);
        values.put("date", date);

        long newRow = sqLiteDatabase.insert("Alert", null, values);
        if (newRow == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAlertData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Alert", null);
        return cursor;
    }
}
