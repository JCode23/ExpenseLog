package com.expenselog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siddhant on 11/30/16.
 */
public class DBWallet extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "edit_account_wallet.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "data_wallet";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";

    public DBWallet(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_BALANCE + " DOUBLE, " +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_DATE + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(double balance, String description, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BALANCE, balance);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_DATE, date);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public List<Float> getAllBalance()
    {
        List<Float> amount_list = new ArrayList<Float>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query(true, TABLE_NAME,
                new String[] { COLUMN_BALANCE }, null, null, null, null,
                null, null);

        if (res.moveToFirst()) {
            do {
                amount_list.add(res.getFloat(res
                        .getColumnIndex(COLUMN_BALANCE)));
            } while (res.moveToNext());
        } else {
            return null;
        }
        return amount_list;
    }

    public double getAmount()
    {
        double amount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.query(true, TABLE_NAME,
                new String[] { COLUMN_BALANCE }, null, null, null, null,
                null, null);

        if (res.moveToFirst()) {
            do {
                amount = res.getDouble(res
                        .getColumnIndex(COLUMN_BALANCE));
            } while (res.moveToNext());
        } else {
            System.out.println("Database Empty");
        }

        return amount;
    }

    public boolean updateAmount(double balance)
    {
        int id = 1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BALANCE, balance);
        db.update(TABLE_NAME,contentValues,"_id="+id, null);
        return true;
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
