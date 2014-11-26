package com.mercadolibre.jvillarnovo.trainingpractico1.storage;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by jvillarnovo on 21/11/14.
 */
public class DataBaseContract {

    public interface Tables {
        public static final String TABLE_TRACKER = "table_tracker";
    }

    public interface TrackerColumns {
        public static final String ID = "_id";
        public static final String PRICE = "price";
    }

    public static final String AUTHORITY = "com.mercadolibre.jvillarnovo.trainingpractico1.tracker.TrackerProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + DataBaseContract.Tables.TABLE_TRACKER);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + Tables.TABLE_TRACKER;

}
