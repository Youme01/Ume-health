package com.example.mhealthapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// SQLite Database Code
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG="DatabaseHelper";
    private static final String TABLE_NAME="medicine_name";

    private static final String COL1="ID";
    private static final String COL2="name";
    private static final String COL3="dose";
    private static final String COL4="food";
    private static final String COL5="time";

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 10681);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name  TEXT, dose TEXT, food TEXT, time TEXT )";
        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE iF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(String item1,String item2,String item3,String item4){
        long res = 0 ;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2,item1);
            contentValues.put(COL3,item2);
            contentValues.put(COL4,item3);
            contentValues.put(COL5,item4);
            res = db.insert(TABLE_NAME , null ,contentValues);

        if(res==-1){
            return false;
        }else{
            return true;
        }

    }
    public boolean UpdateData(String item1,String item2,String item3,String item4,String item5) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, item1);
        contentValues.put(COL2, item2);
        contentValues.put(COL3, item3);
        contentValues.put(COL4, item4);
        contentValues.put(COL5, item5);
        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{item1});
        return true;

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;

    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[]{id});
    }

}
