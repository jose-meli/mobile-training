package com.mercadolibre.jvillarnovo.trainingpractico1.tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by jvillarnovo on 20/11/14.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String IS_ALARM_SET = "is_set_alarm";

    public interface PreferencesConstants {
        public static final String PREFF_NAME = "com.mdelbel.practicauno.preferences";
        public static final String LAST_SEARCH = "last_search";
        public static final String IS_ALARM_SET = "is_alarm_set";
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences
                (context.getPackageName(), Context.MODE_PRIVATE);
        if (!preferences.getBoolean(IS_ALARM_SET, false)) {
            setAlarmItems(context);
            preferences.edit().putBoolean(IS_ALARM_SET, true).commit();
        }
    }

    public static void setAlarmItems(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getService(context, 1,
                new Intent(context, TrackerService.class), PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pendingIntent);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
    }

}
