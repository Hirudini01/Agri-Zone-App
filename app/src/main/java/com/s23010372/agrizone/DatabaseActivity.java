package com.s23010372.agrizone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseActivity extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "agrizone.db";
    private static final int DATABASE_VERSION = 2; // Increased version for expert support table

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";

    // Expert support problems table
    public static final String TABLE_PROBLEMS = "expert_problems";
    public static final String COL_PROBLEM_ID = "problem_id";
    public static final String COL_CATEGORY = "category";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_LOCATION = "location";
    public static final String COL_CONTACT = "contact_number";
    public static final String COL_TIMESTAMP = "timestamp";
    public static final String COL_STATUS = "status";

    public DatabaseActivity(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create expert problems table
        String CREATE_PROBLEMS_TABLE = "CREATE TABLE " + TABLE_PROBLEMS + " (" +
                COL_PROBLEM_ID + " TEXT PRIMARY KEY, " +
                COL_CATEGORY + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_CONTACT + " TEXT, " +
                COL_TIMESTAMP + " TEXT, " +
                COL_STATUS + " TEXT DEFAULT 'submitted')";
        db.execSQL(CREATE_PROBLEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add expert problems table
            String CREATE_PROBLEMS_TABLE = "CREATE TABLE " + TABLE_PROBLEMS + " (" +
                    COL_PROBLEM_ID + " TEXT PRIMARY KEY, " +
                    COL_CATEGORY + " TEXT, " +
                    COL_DESCRIPTION + " TEXT, " +
                    COL_LOCATION + " TEXT, " +
                    COL_CONTACT + " TEXT, " +
                    COL_TIMESTAMP + " TEXT, " +
                    COL_STATUS + " TEXT DEFAULT 'submitted')";
            db.execSQL(CREATE_PROBLEMS_TABLE);
        }
    }

    // Add user
    public boolean addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Check login
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Reset password
    public boolean resetPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, newPassword);
        int rows = db.update(TABLE_USERS, values, COL_EMAIL + "=?", new String[]{email});
        db.close();
        return rows > 0;
    }

    // Add expert support problem
    public boolean addProblem(String problemId, String category, String description, String location, String contact, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROBLEM_ID, problemId);
        values.put(COL_CATEGORY, category);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_LOCATION, location);
        values.put(COL_CONTACT, contact);
        values.put(COL_TIMESTAMP, timestamp);
        values.put(COL_STATUS, "submitted");
        long result = db.insert(TABLE_PROBLEMS, null, values);
        db.close();
        return result != -1;
    }

    // Get all problems
    public Cursor getAllProblems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PROBLEMS, null, null, null, null, null, COL_TIMESTAMP + " DESC");
    }
}
