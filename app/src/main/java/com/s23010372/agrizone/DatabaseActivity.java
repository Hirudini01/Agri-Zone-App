package com.s23010372.agrizone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseActivity extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "agrizone.db";
    private static final int DATABASE_VERSION = 4; // Increased version for user profile updates

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_PHONE = "phone";
    public static final String COL_USER_TYPE = "user_type";

    // Expert support problems table
    public static final String TABLE_PROBLEMS = "expert_problems";
    public static final String COL_PROBLEM_ID = "problem_id";
    public static final String COL_CATEGORY = "category";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_LOCATION = "location";
    public static final String COL_CONTACT = "contact_number";
    public static final String COL_TIMESTAMP = "timestamp";
    public static final String COL_STATUS = "status";

    // Community posts table
    public static final String TABLE_COMMUNITY_POSTS = "community_posts";
    public static final String COL_POST_ID = "post_id";
    public static final String COL_POST_USER = "post_user";
    public static final String COL_POST_CONTENT = "post_content";
    public static final String COL_POST_IMAGE = "post_image"; // store image path or base64
    public static final String COL_POST_TIMESTAMP = "post_timestamp";

    // Community comments table
    public static final String TABLE_COMMUNITY_COMMENTS = "community_comments";
    public static final String COL_COMMENT_ID = "comment_id";
    public static final String COL_COMMENT_POST_ID = "comment_post_id";
    public static final String COL_COMMENT_USER = "comment_user";
    public static final String COL_COMMENT_CONTENT = "comment_content";
    public static final String COL_COMMENT_TIMESTAMP = "comment_timestamp";

    public DatabaseActivity(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table with phone field and user_type
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_USER_TYPE + " TEXT DEFAULT 'Farmer')";
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

        // Create community posts table
        String CREATE_COMMUNITY_POSTS_TABLE = "CREATE TABLE " + TABLE_COMMUNITY_POSTS + " (" +
                COL_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_POST_USER + " TEXT, " +
                COL_POST_CONTENT + " TEXT, " +
                COL_POST_IMAGE + " TEXT, " +
                COL_POST_TIMESTAMP + " TEXT)";
        db.execSQL(CREATE_COMMUNITY_POSTS_TABLE);

        // Create community comments table
        String CREATE_COMMUNITY_COMMENTS_TABLE = "CREATE TABLE " + TABLE_COMMUNITY_COMMENTS + " (" +
                COL_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_COMMENT_POST_ID + " INTEGER, " +
                COL_COMMENT_USER + " TEXT, " +
                COL_COMMENT_CONTENT + " TEXT, " +
                COL_COMMENT_TIMESTAMP + " TEXT)";
        db.execSQL(CREATE_COMMUNITY_COMMENTS_TABLE);
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
        if (oldVersion < 3) {
            // Add community posts and comments tables
            String CREATE_COMMUNITY_POSTS_TABLE = "CREATE TABLE " + TABLE_COMMUNITY_POSTS + " (" +
                    COL_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_POST_USER + " TEXT, " +
                    COL_POST_CONTENT + " TEXT, " +
                    COL_POST_IMAGE + " TEXT, " +
                    COL_POST_TIMESTAMP + " TEXT)";
            db.execSQL(CREATE_COMMUNITY_POSTS_TABLE);

            String CREATE_COMMUNITY_COMMENTS_TABLE = "CREATE TABLE " + TABLE_COMMUNITY_COMMENTS + " (" +
                    COL_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_COMMENT_POST_ID + " INTEGER, " +
                    COL_COMMENT_USER + " TEXT, " +
                    COL_COMMENT_CONTENT + " TEXT, " +
                    COL_COMMENT_TIMESTAMP + " TEXT)";
            db.execSQL(CREATE_COMMUNITY_COMMENTS_TABLE);
        }
        if (oldVersion < 4) {
            // Add phone column if it doesn't exist
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_PHONE + " TEXT");
            } catch (Exception e) {
                // Column might already exist
            }
            // Add user_type column if it doesn't exist
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COL_USER_TYPE + " TEXT DEFAULT 'Farmer'");
            } catch (Exception e) {
                // Column might already exist
            }
        }
    }

    // Add user with phone number and user type
    public boolean addUser(String username, String email, String password, String phone, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_PHONE, phone);
        values.put(COL_USER_TYPE, userType != null ? userType : "Farmer");
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Add user with phone number (default user type)
    public boolean addUser(String username, String email, String password, String phone) {
        return addUser(username, email, password, phone, "Farmer");
    }

    // Backward compatibility method - add user without phone number
    public boolean addUser(String username, String email, String password) {
        return addUser(username, email, password, "", "Farmer");
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

    // Get user by username
    public Cursor getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COL_USERNAME + "=?", new String[]{username}, null, null, null);
    }

    // Get complete user profile data for login verification
    public Cursor getUserProfile(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null,
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
    }

    // Add community post
    public long addCommunityPost(String user, String content, String imagePath, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_POST_USER, user);
        values.put(COL_POST_CONTENT, content);
        values.put(COL_POST_IMAGE, imagePath);
        values.put(COL_POST_TIMESTAMP, timestamp);
        long result = db.insert(TABLE_COMMUNITY_POSTS, null, values);
        db.close();
        return result;
    }

    // Get all community posts
    public Cursor getAllCommunityPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_COMMUNITY_POSTS, null, null, null, null, null, COL_POST_TIMESTAMP + " DESC");
    }

    // Add comment to post
    public long addCommunityComment(long postId, String user, String content, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_COMMENT_POST_ID, postId);
        values.put(COL_COMMENT_USER, user);
        values.put(COL_COMMENT_CONTENT, content);
        values.put(COL_COMMENT_TIMESTAMP, timestamp);
        long result = db.insert(TABLE_COMMUNITY_COMMENTS, null, values);
        db.close();
        return result;
    }

    // Get comments for a post
    public Cursor getCommentsForPost(long postId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_COMMUNITY_COMMENTS, null, COL_COMMENT_POST_ID + "=?", new String[]{String.valueOf(postId)}, null, null, COL_COMMENT_TIMESTAMP + " ASC");
    }

    // Update user profile
    public boolean updateUserProfile(String username, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, phone);
        int rows = db.update(TABLE_USERS, values, COL_USERNAME + "=?", new String[]{username});
        db.close();
        return rows > 0;
    }

    // Check if username exists
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Check if email exists
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // Get user by email
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COL_EMAIL + "=?", new String[]{email}, null, null, null);
    }

    // Get user statistics for community
    public int getUserPostCount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMMUNITY_POSTS, null, COL_POST_USER + "=?", new String[]{username}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getUserCommentCount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMMUNITY_COMMENTS, null, COL_COMMENT_USER + "=?", new String[]{username}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // Get user by ID
    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COL_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    // Update user type
    public boolean updateUserType(String username, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_TYPE, userType);
        int rows = db.update(TABLE_USERS, values, COL_USERNAME + "=?", new String[]{username});
        db.close();
        return rows > 0;
    }

    // Get all users (for admin purposes)
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, null, null, null, null, COL_USERNAME + " ASC");
    }

    // Delete user account
    public boolean deleteUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_USERS, COL_USERNAME + "=?", new String[]{username});
        db.close();
        return rows > 0;
    }

    // Update user password
    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, newPassword);
        int rows = db.update(TABLE_USERS, values, COL_USERNAME + "=?", new String[]{username});
        db.close();
        return rows > 0;
    }
}
