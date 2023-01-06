package com.example.medicial.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.medicial.Model.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Medicial.db";
    private static final int DB_VERSION = 3;
    public static int activeUserID = -1;
    private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table User (id INTEGER PRIMARY KEY AUTOINCREMENT,userName TEXT, firstName TEXT, lastName TEXT, password TEXT, email TEXT)");
        sqLiteDatabase.execSQL("create table Medicine (id INTEGER PRIMARY KEY AUTOINCREMENT, medName TEXT, amount INTEGER)");
        sqLiteDatabase.execSQL("create table Alert (id INTEGER PRIMARY KEY AUTOINCREMENT, time TIME, date DATE)");
        // sqLiteDatabase.execSQL("create table List ( supplyAmount INTEGER, creationDate DATE,  FOREIGN KEY(userID) REFERENCES User(id), FOREIGN KEY(alertID) REFERENCES Alert(id),FOREIGN KEY(medicineID) REFERENCES Medicine(id))");
        // sqLiteDatabase.execSQL("create table Admin (id INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(userID) REFERENCES User(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Medicine");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Alert");
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS List");
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Admin");

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    //   {Adding new User Details}
    public boolean insertUserData(String userName, String firstName, String lastName, String password, String email) {
        // Get the Data Repository in write mode
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

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
        if (newRow != -1) sqLiteDatabase.close();
        return newRow != -1;
    }

    //   {Check Username}
    public boolean CheckUserName(String userName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE userName = ?", new String[]{userName});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    //   {Check Username and Password}
    public boolean CheckUserPassword(String userName, String password) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE userName = ? AND password = ?", new String[]{userName, password});
        if (cursor != null && cursor.moveToNext()) {
            activeUserID = cursor.getInt(0);
            cursor.close();
        }
        return activeUserID != -1; // activeUserID == -1 -> Failed Login
    }

    public ArrayList<User> GetUserData() {
        String userName = "", firstName = "", lastName = "", email = "", password = "";
        ArrayList<User> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] args = new String[]{String.valueOf(activeUserID)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE id = ?", args);

        while (cursor.moveToNext()) {

            userName = cursor.getString(1);
            firstName = cursor.getString(2);
            lastName = cursor.getString(3);
            email = cursor.getString(4);
            password = cursor.getString(5);
        }
        cursor.close();
        User user = new User(userName, firstName, lastName, password, email);
        arrayList.add(user);
        return arrayList;
    }

    //   {Adding new Medicine Details}
    public boolean insertMedicineData(String medName, int amount) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medName", medName);
        values.put("amount", amount);

        long newRow = sqLiteDatabase.insert("Medicine", null, values);
        return newRow != -1;
    }

    //   {Adding new Alert Details}
    public boolean insertDateTime(String time, String date) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);
        values.put("date", date);

        long newRow = sqLiteDatabase.insert("Alert", null, values);
        return newRow != -1;
    }

    public Cursor getMedicineData() {
        return getWritableDatabase().rawQuery("SELECT * FROM Medicine", null);
    }

    public Cursor getAlertData() {
        return getWritableDatabase().rawQuery("SELECT * FROM Alert", null);
    }
}
