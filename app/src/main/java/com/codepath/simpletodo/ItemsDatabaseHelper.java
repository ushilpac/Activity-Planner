package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.ExifInterface;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Shilpa's on 7/21/2017.
 */

public class ItemsDatabaseHelper extends SQLiteOpenHelper {

    private static ItemsDatabaseHelper mInstance;

    // Database Info
    private static final String DATABASE_NAME = "itemsDatabase";
    private static final int DATABASE_VERSION = 13;

    //table name
    private static final String TABLE_ITEM = "items";

    // Items Table Columns
    private static final String COLMN_ITEM_TITLE = "itemTitle";
    private static final String COLMN_ITEM_ID = "itemId";
    private static final String COLMN_DUE_DATE = "dueDate";
    private static final String COLMN_NOTES = "notes";
    private static final String COLMN_PRIORITY = "priority";
    private static final String COLMN_STATUS = "status";
    private static final String COLMN_CATEGORY = "category";



    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private ItemsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ItemsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new ItemsDatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("SimpleTODO", "onCreate SQLiteDatabase");

        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "( " + COLMN_ITEM_ID + " INTEGER PRIMARY KEY, " +
                COLMN_ITEM_TITLE + " TEXT, " + COLMN_DUE_DATE + " TEXT, "+ COLMN_NOTES + " TEXT, "
        + COLMN_PRIORITY + " TEXT, " + COLMN_STATUS + " TEXT ," + COLMN_CATEGORY + " TEXT " + " )";

        try {
            db.execSQL(CREATE_ITEM_TABLE);
        }catch (Exception e){
            Log.d("OnCreate::", "msg="+e.getMessage());
        }

    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        onCreate(db);
    }

    // Insert a item into the database
    public void addItems(Item item) {

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(COLMN_ITEM_TITLE,item.getTitle());
            values.put(COLMN_DUE_DATE,item.getDueDate());
            values.put(COLMN_NOTES,item.getNotes());
            values.put(COLMN_PRIORITY,item.getPriority());
            values.put(COLMN_STATUS,item.getStatus());
            values.put(COLMN_CATEGORY, item.getCategory());

            // no need to insert the value for id it will be autoincremtned bydefault

            long rowID = db.insertOrThrow(TABLE_ITEM, null, values);
            db.setTransactionSuccessful();
            if(rowID >= 0){
                item.setWorkItemId(rowID);
            }


        }catch(Exception e){
            Log.d("SIMPLE TODO","error while adding item to databse ****");
        }finally{
            db.endTransaction();
        }

    }

    public ArrayList<Item> getAllItems(){

        ArrayList<Item> items = new ArrayList<>();

        String SELECT_QUERY = "SELECT * FROM " + TABLE_ITEM;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        try{
            if(cursor.moveToFirst()){
                do{
                    Item newItem = new Item();
                    String title = cursor.getString(cursor.getColumnIndex(COLMN_ITEM_TITLE));
                    newItem.setTitle(title);
                    int id = cursor.getInt(cursor.getColumnIndex(COLMN_ITEM_ID));
                    newItem.setWorkItemId(id);
                    String dueDate = cursor.getString(cursor.getColumnIndex(COLMN_DUE_DATE));
                    newItem.setDueDate(dueDate);
                    String notes = cursor.getString(cursor.getColumnIndex(COLMN_NOTES));
                    newItem.setNotes(notes);
                    String priority = cursor.getString(cursor.getColumnIndex(COLMN_PRIORITY));
                    newItem.setPriority(priority);
                    String status  = cursor.getString(cursor.getColumnIndex(COLMN_STATUS));
                    newItem.setStatus(status);
                    String category = cursor.getString(cursor.getColumnIndex(COLMN_CATEGORY));
                    newItem.setCategory(category);

                    items.add(newItem);

                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            Log.d("SImple TODO", "error while reading the data from database");
        }finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return items;
    }

    // update work items
    public int updateWorkItem(Item item){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLMN_ITEM_TITLE,item.getTitle());
        values.put(COLMN_DUE_DATE,item.getDueDate());
        values.put(COLMN_NOTES,item.getNotes());
        values.put(COLMN_PRIORITY,item.getPriority());
        values.put(COLMN_STATUS,item.getStatus());
        values.put(COLMN_CATEGORY, item.getCategory());

        return db.update(TABLE_ITEM,values,COLMN_ITEM_ID + "=?",new String [] {String.valueOf(item.getItemId())});
    }

    public int deleteWorkItems(long id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEM, COLMN_ITEM_ID+ "=?", new String [] {String.valueOf(id)});

    }

}
