package com.mercadolibre.jvillarnovo.trainingpractico1.tracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.mercadolibre.jvillarnovo.trainingpractico1.storage.DataBaseContract;
import com.mercadolibre.jvillarnovo.trainingpractico1.storage.DataBaseHelper;

/**
 * Created by jvillarnovo on 21/11/14.
 */
public class TrackerProvider extends ContentProvider {

    private static final int TRACKER = 10;
    private DataBaseHelper dataBaseHelper;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(DataBaseContract.AUTHORITY, DataBaseContract.Tables.TABLE_TRACKER, TRACKER);
    }

    @Override
    public boolean onCreate() {
        dataBaseHelper = new DataBaseHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(DataBaseContract.Tables.TABLE_TRACKER);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TRACKER:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        return DataBaseContract.CONTENT_TYPE;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        long id;
        switch (uriType) {
            case TRACKER:
                id = sqlDB.insert(DataBaseContract.Tables.TABLE_TRACKER, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DataBaseContract.Tables.TABLE_TRACKER + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case TRACKER:
                rowsDeleted = sqlDB.delete(DataBaseContract.Tables.TABLE_TRACKER, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dataBaseHelper.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case TRACKER:
                rowsUpdated = sqlDB.update(DataBaseContract.Tables.TABLE_TRACKER,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
