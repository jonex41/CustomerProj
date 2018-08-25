package com.wisdomgabriel.www.customerproj.DatabaseForCart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import com.wisdomgabriel.www.customerproj.ShopModel;

import java.util.ArrayList;
import java.util.List;


public class CartDatabase extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "cart_table";


    public static final String COLUMN_CART_ID ="_id" ;
    public static final String FOOD_NAME = "name";
    public static final String FOOD_IMAGE = "image_url";
    public static final String FOOD_PRICE = "total_numbers";
    public static final String PRICE_PER_ONE = "price_per_one";
    public static final String PLATE_NUMBER = "plates_number";
    public static final String FOOD_DISCRIPTION = "total_description";

    public SQLiteDatabase db;

    // Database Version
    private static final int DATABASE_VERSION =15;

    // Database Name
    private static final String DATABASE_NAME = "Cart.db";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FOOD_NAME + " TEXT, " +
                FOOD_PRICE + " TEXT, " +
                FOOD_IMAGE + " TEXT, " +
                PRICE_PER_ONE + " TEXT, " +
                PLATE_NUMBER + " TEXT, " +
                FOOD_DISCRIPTION + " TEXT " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    //drop beneficiary table
    private String DROP_BENEFICIARY_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public CartDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_BENEFICIARY_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //Method to create beneficiary records


    public void addtoCart(ShopModel contact) {


        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues contentValues = new ContentValues();

        contentValues.put(FOOD_NAME, contact.getFoodName());
        contentValues.put(FOOD_IMAGE, contact.getImageUrl());
        contentValues.put(FOOD_DISCRIPTION, contact.getFoodDescription());
        contentValues.put(FOOD_PRICE, contact.getFoodPrice());
        contentValues.put(PLATE_NUMBER, contact.getPlatesNumber());
        contentValues.put(PRICE_PER_ONE, contact.getPrice_per_one());

        db.insert(TABLE_NAME, null, contentValues);


        db.close();
    }


    public String Sum(String classname) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = new String[] {"SUM("+FOOD_PRICE+")" };



        Cursor cursor = db.query(TABLE_NAME, columns,null, null,null,null,null);
        String result = "";

        int index_SUM = cursor.getColumnIndex("SUM("+FOOD_PRICE+")");


            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                result = result + cursor.getString(index_SUM) + "\n";
            }

        db.close();




        return result;
    }



    public List<ShopModel> getindivMessages(){
        List<ShopModel> todos = new ArrayList<ShopModel>();
        // array of columns to fetch
        String[] columns = {
                COLUMN_CART_ID,
                FOOD_NAME,
                FOOD_DISCRIPTION,
                FOOD_IMAGE,
                PRICE_PER_ONE,
                PLATE_NUMBER,
                FOOD_PRICE
        };
        SQLiteDatabase db = this.getReadableDatabase();



        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor c = db.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                null ,                  //columns for the WHERE clause
                null,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order

        if (c.moveToFirst()) {
            do {


                ShopModel td = new ShopModel();
                td.setFoodName(c.getString(c.getColumnIndex(FOOD_NAME)));
                td.setFoodDescription(c.getString(c.getColumnIndex(FOOD_DISCRIPTION)));
                td.setImageUrl(c.getString(c.getColumnIndex(FOOD_IMAGE)));
                td.setPrice_per_one(c.getString(c.getColumnIndex(PRICE_PER_ONE)));
                td.setPlatesNumber(c.getString(c.getColumnIndex(PLATE_NUMBER)));
                td.setFoodPrice(c.getString(c.getColumnIndex(FOOD_PRICE)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        c.close();

        db.close();

        return todos;
    }
    public boolean checkUser(String food_name) {

        // array of columns to fetch
        String[] columns = {
                FOOD_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection =FOOD_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {food_name};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_NAME, //Table to query
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
    public void deleteRecord(String contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, FOOD_NAME + " = ?", new String[]{contact});
        db.close();
    }


    public void deleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null, null);
        db.close();
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }
}

