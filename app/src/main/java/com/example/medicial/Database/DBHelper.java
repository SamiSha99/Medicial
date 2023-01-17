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
    private static final int DB_VERSION = 6;
    public static int activeUserID = -1;
    //private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table User (id INTEGER PRIMARY KEY AUTOINCREMENT,userName TEXT, firstName TEXT, lastName TEXT, password TEXT, email TEXT)");
        sqLiteDatabase.execSQL("create table Medicine (id INTEGER PRIMARY KEY AUTOINCREMENT, medName TEXT, amount INTEGER, image String)");
        sqLiteDatabase.execSQL("create table Alert (id INTEGER PRIMARY KEY AUTOINCREMENT, time TIME, date DATE, medID INTEGER, FOREIGN KEY(medID) REFERENCES Medicine(id))");
        //sqLiteDatabase.execSQL("create table Admin (id INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER, FOREIGN KEY(userID) REFERENCES User(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Medicine");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Alert");
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
        activeUserID = -1;
        if (cursor != null && cursor.moveToNext()) {
            activeUserID = cursor.getInt(0);
            Log.d("ACTIVE_USER_ID", "" + activeUserID);
            cursor.close();
        }
        return activeUserID != -1; // activeUserID == -1 -> Failed Login
    }

    public ArrayList<User> GetUserData() {
        int id = 0;
        String userName = "", firstName = "", lastName = "", email = "", password = "";
        ArrayList<User> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] args = new String[]{String.valueOf(activeUserID)};
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE id = ?", args);

        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
            userName = cursor.getString(1);
            firstName = cursor.getString(2);
            lastName = cursor.getString(3);
            email = cursor.getString(4);
            password = cursor.getString(5);
        }
        cursor.close();
        User user = new User(id, userName, firstName, lastName, password, email);
        arrayList.add(user);
        return arrayList;
    }

    //   {Adding new Medicine Details}
    // Return: ID number of the medicine if inserted, -1 if failed input
    public int insertMedicineData(String medName, int amount, String image) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medName", medName);
        values.put("amount", amount);
        values.put("image", image);
        // returns the row, which ironically also the Medicine ID
        long idPK = sqLiteDatabase.insert("Medicine", null, values);

        return idPK == -1 ? -1 : (int) idPK;
    }

    //   {Adding new Alert Details}
    // Return: ID number of the alert if inserted, -1 if failed input
    public int insertDateTime(int medicineID, String time, String date) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time", time);
        values.put("date", date);
        values.put("medID", medicineID);

        long idPK = sqLiteDatabase.insert("Alert", null, values);

        return idPK == -1 ? -1 : (int) idPK;
    }

    public Cursor getMedicineData() {
        return getWritableDatabase().rawQuery("SELECT * FROM Medicine", null);
    }

    public Cursor getAlertData() {
        return getWritableDatabase().rawQuery("SELECT * FROM Alert", null);
    }

    public void removeMedicineData(int medicineID) {
        // Clean up alerts first
        removeAlertData(medicineID);

        SQLiteDatabase sql = getWritableDatabase();
        sql.delete("Medicine", "id = ?", new String[]{String.valueOf(medicineID)});
        Cursor c = sql.rawQuery("SELECT * FROM Medicine WHERE id = ?", new String[]{String.valueOf(medicineID)});
        if (c.getCount() > 0)
            Log.wtf("removeMedicineData", "ID: " + medicineID + " | FAILED TO DELETE MEDICINE!");
        c.close();
        sql.close();
    }

    public void removeAlertData(int medicineID) {
        SQLiteDatabase sql = getWritableDatabase();
        sql.delete("Alert", "id = ?", new String[]{String.valueOf(medicineID)});
        Cursor c = getWritableDatabase().rawQuery("SELECT * FROM Alert WHERE id = ?", new String[]{String.valueOf(medicineID)});
        if (c.getCount() > 0)
            Log.wtf("removeAlertData", "FAILED TO DELETE ALERTS FOR MEDICINE ID: " + medicineID);
        c.close();
        sql.close();
    }

    public void updateMedicineData(int id, String medName, int amount, String image) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medName", medName);
        values.put("amount", amount);
        if(!image.isEmpty()) values.put("image", image); // don't replace image if its empty!
        sql.update("Medicine", values, "id = ?", new String[]{String.valueOf(id)});
        sql.close();
    }
}
