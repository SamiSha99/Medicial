package com.example.medicial.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.medicial.model.Data;
import com.example.medicial.model.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Medicial.db";
    private static final int DB_VERSION = 12;
    public static int activeUserID = -1;
    public static boolean isAdmin = false;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table User (id INTEGER PRIMARY KEY AUTOINCREMENT,userName TEXT, firstName TEXT, lastName TEXT, password TEXT, email TEXT)");
        sqLiteDatabase.execSQL("create table Medicine (id INTEGER PRIMARY KEY AUTOINCREMENT, medName TEXT, amount INTEGER, description TEXT, image String, userID INTEGER, FOREIGN KEY(userID) REFERENCES User(id))");
        sqLiteDatabase.execSQL("create table Alert (id INTEGER PRIMARY KEY AUTOINCREMENT, time TIME, date DATE, medID INTEGER, FOREIGN KEY(medID) REFERENCES Medicine(id))");
        sqLiteDatabase.execSQL("create table Admin (id INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER, FOREIGN KEY(userID) REFERENCES User(id))");
        createAdmins(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS User");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Medicine");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Alert");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Admin");

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void createAdmins(SQLiteDatabase db) {
        insertAdminData("admin", "secret", "user", "123456", "NotYourBusiness@Medicial.com", db);
    }

    public boolean insertAdminData(String userName, String firstName, String lastName, String password, String email, SQLiteDatabase db) {
        SQLiteDatabase sqLiteDatabase = db != null ? db : getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userName", userName);
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("password", password);
        values.put("email", email);

        long newRow = sqLiteDatabase.insert("User", null, values);
        if (newRow == -1) {
            sqLiteDatabase.close();
            return false;
        }

        values.clear();
        values.put("userID", newRow);
        long newAdminRow = sqLiteDatabase.insert("Admin", null, values);
        //sqLiteDatabase.close();
        return newAdminRow != -1;
    }

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

    public boolean checkUserName(String userName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE userName = ?", new String[]{userName});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    public boolean checkUserPassword(String userName, String password) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE userName = ? AND password = ?", new String[]{userName, password});
        activeUserID = -1;
        isAdmin = false;
        if (cursor != null && cursor.moveToNext()) {
            activeUserID = cursor.getInt(0);
            cursor.close();
            isAdmin = isUserAdmin(activeUserID);
            Log.d("ACTIVE_USER_ID", "" + activeUserID);
            Log.d("Admin? =>", "" + isAdmin);
        }
        return activeUserID != -1; // activeUserID == -1 -> Failed Login
    }

    public ArrayList<User> getUserData() {
        ArrayList<User> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] args = new String[]{String.valueOf(activeUserID)};

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE id = ?", args);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String userName = cursor.getString(1);
                String firstName = cursor.getString(2);
                String lastName = cursor.getString(3);
                String email = cursor.getString(4);
                String password = cursor.getString(5);

                User user = new User(id, userName, firstName, lastName, password, email);
                arrayList.add(user);
            }
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<User> getAllUserData() {
        ArrayList<User> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User ", null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String userName = cursor.getString(1);
                String firstName = cursor.getString(2);
                String lastName = cursor.getString(3);
                String email = cursor.getString(4);
                String password = cursor.getString(5);

                User user = new User(id, userName, firstName, lastName, password, email);
                arrayList.add(user);
            }
        }
        cursor.close();
        return arrayList;
    }

    public boolean isUserAdmin(int userID) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Admin WHERE userID = ?", new String[]{String.valueOf(userID)});
        boolean match = cursor.getCount() != 0;
        cursor.close();
        return match;
    }

    // Return: ID number of the medicine if inserted, -1 if failed input
    public int insertMedicineData(String medName, int amount, String description, String image) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medName", medName);
        values.put("amount", amount);
        values.put("description", description);
        values.put("image", image);
        values.put("userID", activeUserID);
        // returns the row, which ironically also the Medicine ID
        long idPK = sqLiteDatabase.insert("Medicine", null, values);

        return idPK == -1 ? -1 : (int) idPK;
    }

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

    public ArrayList<Data> getReminderData() {
        ArrayList<Data> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT m.id, m.medName, m.amount, m.description, m.image, a.time, a.date FROM Medicine m JOIN Alert a ON m.id = a.medId WHERE m.userID = " + activeUserID, null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int mId = cursor.getInt(0);
                String medName = cursor.getString(1);
                int amount = cursor.getInt(2);
                String description = cursor.getString(3);
                String image = cursor.getString(4);
                String time = cursor.getString(5);
                String date = cursor.getString(6);

                Data data = new Data(mId, medName, amount, description, image, time, date);
                arrayList.add(data);
            }
        }
        cursor.close();
        return arrayList;
    }

    // For show data in admin {ShowDataActivity}
    public ArrayList<Data> showData() {
        ArrayList<Data> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT m.id, m.medName, m.amount, m.description, m.image, a.time, a.date FROM Medicine m JOIN Alert a ON m.id = a.medId", null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int mId = cursor.getInt(0);
                String medName = cursor.getString(1);
                int amount = cursor.getInt(2);
                String description = cursor.getString(3);
                String image = cursor.getString(4);
                String time = cursor.getString(5);
                String date = cursor.getString(6);

                Data data = new Data(mId, medName, amount, description, image, time, date);
                arrayList.add(data);
            }
        }
        cursor.close();
        return arrayList;
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

    public void updateMedicineData(int id, String medName, int amount, String description) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medName", medName);
        values.put("amount", amount);
        values.put("description", description);

        sql.update("Medicine", values, "id = ?", new String[]{String.valueOf(id)});
        sql.close();
    }

    public boolean resetPassword(String userName, String password) {
        SQLiteDatabase sql = getWritableDatabase();
        boolean passwordResetSuccess = false;
        try {
            if (checkUserName(userName)) {
                ContentValues values = new ContentValues();
                values.put("password", password);
                sql.update("User", values, "userName =?", new String[]{userName});
                passwordResetSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close();
        }
        return passwordResetSuccess;
    }

    public boolean checkUserId(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM User WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase sql = getWritableDatabase();
        boolean chkID = false;
        try {
            if (checkUserId(id)) {
                sql.delete("User", "id = ?", new String[]{String.valueOf(id)});
                sql.close();
                chkID = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close();
        }
        return chkID;
    }


    public boolean updateUser(int id, String userName, String firstName, String lastName, String password, String email) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        boolean chkID = false;
        try {
            if (checkUserId(id)) {
                values.put("userName", userName);
                values.put("firstName", firstName);
                values.put("lastName", lastName);
                values.put("password", password);
                values.put("email", email);

                sql.update("User", values, "id = ?", new String[]{String.valueOf(id)});
                sql.close();
                chkID = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sql.close();
        }
        return chkID;
    }
}
