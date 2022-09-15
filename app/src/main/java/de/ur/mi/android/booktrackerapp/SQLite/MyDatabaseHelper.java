package de.ur.mi.android.booktrackerapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BookTrackerApp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_bookTrackerApp";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHORS = "authors";
    private static final String COLUMN_COVER = "cover";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_PAGES = "pages";
    private static final String COLUMN_LANG = "language";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CURR_PAGE = "curr_page";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_TITLE + " TEXT, " +
                        COLUMN_AUTHORS + " TEXT, " +
                        COLUMN_COVER + " TEXT, " +
                        COLUMN_RATING + " DOUBLE, " +
                        COLUMN_PAGES + " INTEGER, " +
                        COLUMN_LANG + " TEXT, " +
                        COLUMN_STATUS + " TEXT, " +
                        COLUMN_CURR_PAGE + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addData(String cover, String title, String authors,
                        double rating, int pages, String language,
                        String status, int currPage){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_COVER, cover);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_AUTHORS, authors);
        contentValues.put(COLUMN_RATING, rating);
        contentValues.put(COLUMN_PAGES, pages);
        contentValues.put(COLUMN_LANG, language);
        contentValues.put(COLUMN_STATUS, status);
        contentValues.put(COLUMN_CURR_PAGE, currPage);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
