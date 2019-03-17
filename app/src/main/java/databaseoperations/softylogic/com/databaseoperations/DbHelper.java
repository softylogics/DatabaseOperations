package databaseoperations.softylogic.com.databaseoperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_CONTACT = "contact";
    private static final String COLUMN_USER_TYPE = "user_type";
    private static final String COLUMN_REG_DATE = "reg_date";
    private static final String COLUMN_MOD_DATE = "mod_date";


    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_CONTACT + " TEXT," + COLUMN_USER_TYPE + " INTEGER," + COLUMN_REG_DATE + " TEXT,"
            + COLUMN_MOD_DATE + " TEXT,"
            + " FOREIGN KEY (user_type) REFERENCES USER_TYPE_TABLE(user_type)" +

            ")";
    private String CREATE_USER_TYPE_TABLE = "CREATE TABLE USER_TYPE_TABLE (user_type integer , type_title text)";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_TABLE_USER_TYPE = "DROP TABLE IF EXISTS USER_TYPE_TABLE";

    /**
     * Constructor
     *
     * @param context
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_TYPE_TABLE);
        db.execSQL("insert into USER_TYPE_TABLE (user_type , type_title) values (1 , 'normal_user') , (2, 'advanced_user')");
        db.execSQL("insert into user (user_name , user_email , user_password , contact , user_type, reg_date , mod_date)" +
                " values ('admin' , 'admin@hotmail.com' , 'admin' , '12345' , 2 , '' ,'' )");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_TABLE_USER_TYPE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public boolean addUser(userDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.name);
        values.put(COLUMN_USER_EMAIL, user.emailid);
        values.put(COLUMN_USER_PASSWORD, user.password);
        values.put(COLUMN_CONTACT , user.contact);
        values.put(COLUMN_USER_TYPE , user.usertype);
        values.put(COLUMN_REG_DATE , user.registerationdate);
        values.put(COLUMN_MOD_DATE, user.modifydate);

        // Inserting Row

        long rowInserted = db.insert(TABLE_USER, null, values);
        db.close();
        if(rowInserted != -1)
            return true;
        else
            return false;


    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public ArrayList<userDetails> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD ,
                COLUMN_CONTACT,
                COLUMN_USER_TYPE,
                COLUMN_REG_DATE,
                COLUMN_MOD_DATE
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        ArrayList<userDetails> userList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                userDetails user = new userDetails();
                user.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
                user.emailid = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
                user.password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD));
                user.contact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT));
                user.usertype =Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));
                user.registerationdate = cursor.getString(cursor.getColumnIndex(COLUMN_REG_DATE));
                user.modifydate = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(userDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.name);
        values.put(COLUMN_USER_EMAIL, user.emailid);
        values.put(COLUMN_USER_PASSWORD, user.password);
        values.put(COLUMN_CONTACT , user.contact);
        values.put(COLUMN_USER_TYPE , user.usertype);
        values.put(COLUMN_REG_DATE , user.registerationdate);
        values.put(COLUMN_MOD_DATE, user.modifydate);

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.user_id)});
        db.close();
    }


    public void deleteUser(userDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.user_id)});
        db.close();
    }


    public boolean checkUserbyemail(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    public ArrayList<userDetails> searchUserbyname(String name) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD ,
                COLUMN_CONTACT,
                COLUMN_USER_TYPE,
                COLUMN_REG_DATE,
                COLUMN_MOD_DATE
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {name};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        ArrayList<userDetails> userList = new ArrayList<>();
        userDetails user = new userDetails();
        if (cursor.moveToFirst()) {
            do {

                user.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
                user.emailid = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
                user.password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD));
                user.contact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT));
                user.usertype =Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));
                user.registerationdate = cursor.getString(cursor.getColumnIndex(COLUMN_REG_DATE));
                user.modifydate = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;


    }
    public ArrayList<userDetails> searchUserbyid(String id) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD ,
                COLUMN_CONTACT,
                COLUMN_USER_TYPE,
                COLUMN_REG_DATE,
                COLUMN_MOD_DATE
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_ID + " = ?";

        // selection argument
        String[] selectionArgs = {id};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        ArrayList<userDetails> userList = new ArrayList<>();
        userDetails user = new userDetails();
        if (cursor.moveToFirst()) {
            do {

                user.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
                user.emailid = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
                user.password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD));
                user.contact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT));
                user.usertype =Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));
                user.registerationdate = cursor.getString(cursor.getColumnIndex(COLUMN_REG_DATE));
                user.modifydate = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }
    public ArrayList<userDetails> searchUserByEmail(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD ,
                COLUMN_CONTACT,
                COLUMN_USER_TYPE,
                COLUMN_REG_DATE,
                COLUMN_MOD_DATE
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

ArrayList<userDetails> userList = new ArrayList<>();
        userDetails user = new userDetails();
        if (cursor.moveToFirst()) {
            do {

                user.user_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
                user.emailid = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
                user.password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD));
                user.contact = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT));
                user.usertype =Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE)));
                user.registerationdate = cursor.getString(cursor.getColumnIndex(COLUMN_REG_DATE));
                user.modifydate = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;



    }

    public boolean checkUserForLogin(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }



}
