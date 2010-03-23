package com.u17od.upm;

import java.io.File;
import java.util.Date;

import com.u17od.upm.database.PasswordDatabase;

import android.app.Activity;
import android.content.SharedPreferences;

public class Utilities {

    public static final String DEFAULT_DATABASE_FILE = "upm.db";
    public static final String PREFS_NAME = "UPMPrefs";
    public static final String PREFS_DB_FILE_NAME = "DB_FILE_NAME";

    public static File getDatabaseFile(Activity activity) {
        String dbFileName = getDatabaseFileName(activity);
        if (dbFileName == null || dbFileName.equals("")) {
            return new File(activity.getFilesDir(), DEFAULT_DATABASE_FILE);
        } else {
            return new File(activity.getFilesDir(), dbFileName);
        }
    }

    public static String getDatabaseFileName(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return settings.getString(PREFS_DB_FILE_NAME, DEFAULT_DATABASE_FILE);
    }

    public static void setDatabaseFileName(String dbFileName, Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFS_DB_FILE_NAME, dbFileName);
        editor.commit();
    }

    public static boolean isSyncRequired(Activity activity) {
        UPMApplication app = (UPMApplication) activity.getApplication();
        PasswordDatabase db = app.getPasswordDatabase();
        Date timeOfLastSync = app.getTimeOfLastSync();

        boolean syncRequired = false;

        if (db.getDbOptions().getRemoteLocation() != null && !db.getDbOptions().getRemoteLocation().equals("")) {
            if (timeOfLastSync == null || System.currentTimeMillis() - timeOfLastSync.getTime() > (5 * 60 * 1000)) {
                syncRequired = true;
            }
        }

        return syncRequired;
    }

}