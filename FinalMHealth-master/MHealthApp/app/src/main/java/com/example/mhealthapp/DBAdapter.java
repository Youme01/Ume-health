package com.example.mhealthapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter {

    // Database and table names
    private static final String databaseName           = "Food_Table";
    private static final String databaseTableNotes     = "Food";
    private static final int databaseVersion           = 15690;

    // Database variables
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static final String databaseCreateTableNotes =
            "CREATE TABLE IF NOT EXISTS " + databaseTableNotes + " " +
                    "( food_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " food_title VARCHAR, " +
                    " food_cal DOUBLE, "+
                    " food_imageA VARCHAR, "+
                    " food_text VARCHAR);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, databaseName, null, databaseVersion);
        }

        //All Tables are created
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(databaseCreateTableNotes);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            //db.execSQL("DROP TABLE IF EXISTS " + databaseTableNotes);
            onCreate(db);

            String TAG = "Tag";
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

        }
    }

    //Open Database
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //closes database
    public void close() {
        DBHelper.close();
    }


    // Insert record
     public void insertRecord(String table, String fields ,String data)
    {
        db.execSQL("INSERT INTO " + table + "(" + fields + ") VALUES (" + data +")" );

    }

    public int CountAllRecords(String table){
        Cursor mcount = db.rawQuery("SELECT COUNT(*) FROM " + table,null);
        mcount.moveToFirst();
        int count = mcount.getInt(0);
        mcount.close();
        return count;
    }

    // Retrieve
    public Cursor getAllRecordsFromNotes()
    {
        return db.query(databaseTableNotes, new String[]{
                "note_id",
                "note_title",
                "note_text"
        }, null, null, null, null, null, null);
    }
    public Cursor getAllRecordsFromNotesListView()
    {
        return db.query(databaseTableNotes, new String[]{
                "note_id AS _id",
                "note_title",
                "note_text"
        }, null, null, null, null, null, null);
    }
    // Retrieves a particular record
    public Cursor getRecordFromNotes(long rowId) throws SQLException {
        Cursor mCursor = db.query(databaseTableNotes, new String[] {
                        "note_id",
                        "note_title",
                        "note_text"
                },
                "note_id" + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    // Truncate
    public int truncateNotes()
    {
        return db.delete(databaseTableNotes, "1", null);
    }
    // Update
    public int updateNote(long rowId, String inpTitle, String inpText) {

        ContentValues values = new ContentValues();
        values.put("note_title", inpTitle);
        values.put("note_text", inpText);

        // updating row
        return db.update(databaseTableNotes, values, "note_id = " + rowId, null);
    }


}
