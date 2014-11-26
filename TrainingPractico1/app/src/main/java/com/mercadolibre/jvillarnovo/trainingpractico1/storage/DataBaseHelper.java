package com.mercadolibre.jvillarnovo.trainingpractico1.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jvillarnovo on 20/11/14.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tracker.db";
    private static int DB_VERSION = 2;

    String SQL_CREATE_TABLE_TRACKER = "CREATE TABLE " + DataBaseContract.Tables.TABLE_TRACKER + "(" +
            DataBaseContract.TrackerColumns.ID + " TEXT PRIMARY KEY, " +
            DataBaseContract.TrackerColumns.PRICE + " DOUBLE );";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TRACKER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.Tables.TABLE_TRACKER);
        onCreate(sqLiteDatabase);
    }
}
