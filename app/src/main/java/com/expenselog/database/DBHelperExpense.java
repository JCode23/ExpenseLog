package com.expenselog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siddhant on 11/2/16.
 */
public class DBHelperExpense extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ChartData.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "data";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM = "item";
    public static final String COLUMN_AMOUNT = "amount";

    public DBHelperExpense(Context context)
    {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
               COLUMN_ITEM + " TEXT, " +
                COLUMN_AMOUNT + " DOUBLE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String item, Float amount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM, item);
        contentValues.put(COLUMN_AMOUNT, amount);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Integer deleteData(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public List<String> getAllItem()
    {
        List<String> item_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query(true, TABLE_NAME,
                new String[] { COLUMN_ITEM }, null, null, null, null,
                null, null);

        if (res.moveToFirst()) {
            do {
                item_list.add(res.getString(res
                        .getColumnIndex(COLUMN_ITEM)));
            } while (res.moveToNext());
        } else {
            return null;
        }
        return item_list;
    }

    public List<Float> getAllAmount()
    {
        List<Float> amount_list = new ArrayList<Float>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query(true, TABLE_NAME,
                new String[] { COLUMN_AMOUNT }, null, null, null, null,
                null, null);

        if (res.moveToFirst()) {
            do {
                amount_list.add(res.getFloat(res
                        .getColumnIndex(COLUMN_AMOUNT)));
            } while (res.moveToNext());
        } else {
            return null;
        }
        return amount_list;
    }

    public boolean checkForTables(){
        boolean hasTables = false;
        SQLiteDatabase db = getReadableDatabase();
        //db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME, null);

        if(cursor != null && cursor.getCount() > 0){
            hasTables=true;
            cursor.close();
        }

        return hasTables;
    }
}
