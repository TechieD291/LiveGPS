package com.gonext.livegps.routenavigation.sqllite;

/**
 * Created by sell-news on 11/17/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.gonext.livegps.routenavigation.models.CategoryModelClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_PATH = "/data/data/com.gonext.livegps.routenavigation/databases/";
    // Database Name
    private static final String DATABASE_NAME = "NearByMe";
    // Contacts table name
    private SQLiteDatabase db;
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_Placeid = "Placeid";
    private static final String KEY_Placename = "Placename";
    private static final String KEY_CategorysIcon = "CategoryIcon";
    private static final String KEY_Diastance = "distance";
    private static final String KEY_Visiteddate = "Visiteddate";
    private static final String KEY_Address = "Address";
    private static final String KEY_PhoneNumber = "PhoneNumber";

    private static final String KEY_CategoryId = "CategoryId";
    private static final String KEY_CategoryName = "CategoryName";
    private static final String KEY_CategoryIcon = "CategoryIcon";
    private static final String KEY_CategorySelected = "CategorySelected";

    private static final String TABLE_history = "History";
    private static final String TABLE_Favorite = "Favorite";
    private static final String TABLE_Categoty = "Categoty";


    Context ctx;

    public SqlLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    /**
     * getCategoryList is use for get list of category
     *
     * @return ArrayList oF CategoryModelClass
     */
    public ArrayList<CategoryModelClass> getCategoryList() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Categoty Where CategorySelected = 1";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<CategoryModelClass> list = new ArrayList<CategoryModelClass>();
        if (cursor != null && cursor.moveToFirst()) {


            while (!cursor.isAfterLast()) {
                CategoryModelClass categoryModelClass = new CategoryModelClass();
                categoryModelClass.setCategoryId(cursor.getString(0));
                categoryModelClass.setCategoryName(cursor.getString(1));
                categoryModelClass.setCategoryIcon(cursor.getString(2));
                categoryModelClass.setCategorySelected(cursor.getInt(3));
                list.add(categoryModelClass);
                cursor.moveToNext();
            }
            db.close();
        }
        return list;
    }

    /**
     * getCategoryList is use for get list of category
     *
     * @return ArrayList oF CategoryModelClass
     */
    public ArrayList<CategoryModelClass> getCategoryListforAll() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Categoty";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<CategoryModelClass> list = new ArrayList<CategoryModelClass>();
        if (cursor != null && cursor.moveToFirst()) {


            while (!cursor.isAfterLast()) {
                CategoryModelClass categoryModelClass = new CategoryModelClass();
                categoryModelClass.setCategoryId(cursor.getString(0));
                categoryModelClass.setCategoryName(cursor.getString(1));
                categoryModelClass.setCategoryIcon(cursor.getString(2));
                categoryModelClass.setCategorySelected(cursor.getInt(3));
                list.add(categoryModelClass);
                cursor.moveToNext();
            }
            db.close();
        }
        return list;
    }

    /**
     * inssertToCategory is use for insert in category database
     *
     * @param categoryName     categoty name
     * @param categoryIcon     category icon name
     * @param categoryselected category is selected or not it is mostly case is selected 1 and type is int
     */
    public void inssertToCategory(String categoryName, String categoryIcon, int categoryselected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CategoryName, categoryName);
        contentValues.put(KEY_CategoryIcon, categoryIcon);
        contentValues.put(KEY_CategorySelected, categoryselected);
        db.insert(TABLE_Categoty, null, contentValues);
    }


    /**
     * CopyDataBaseFromAsset is use for copy database from asset to database folder
     *
     * @throws IOException throws exception when file not found exception
     */
    public void CopyDataBaseFromAsset() throws IOException {
        InputStream in = ctx.getAssets().open("NearByMe.sqlite");
        Log.e("sample", "Starting copying");
        String outputFileName = DATABASE_PATH + DATABASE_NAME;
        File databaseFile = new File(DATABASE_PATH);
        // check if databases folder exists, if not create one and its subfolders
        if (!databaseFile.exists()) {
            databaseFile.mkdir();
        }

        OutputStream out = new FileOutputStream(outputFileName);

        byte[] buffer = new byte[1024];
        int length;


        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        Log.e("sample", "Completed");
        out.flush();
        out.close();
        in.close();

    }

    public void openDataBase() throws SQLException {
        String path = DATABASE_PATH + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    /**
     * updatename is use for update selected category
     *
     * @param categorySelected is selected category type is int
     * @param id               category id for update data
     * @return true or false
     */
    public boolean updatename(int categorySelected, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String ids[] = new String[]{id};
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CategorySelected, categorySelected);
        String wherecluses = KEY_CategoryId + "=?";
        db.update(TABLE_Categoty, contentValues, wherecluses, ids);
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public void deleterecord(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String ids[] = new String[]{id};
        String wherecluses = KEY_Placeid + "=?";
        db.delete(TABLE_Favorite, wherecluses, ids);
    }

}


