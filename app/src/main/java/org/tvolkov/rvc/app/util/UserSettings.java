package org.tvolkov.rvc.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UserSettings {

    private static final String SHARED_PREFS_FILENAME = "org.tvolkov.rvc.preferences";
    private static final String SHARED_PREFS_HOST = "host";
    private static final String SHARED_PREFS_PORT = "port";

    public static String getHost(final Context context) {
        final SharedPreferences sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(SHARED_PREFS_HOST, null);
    }

    public static void setHost(final Context c, final String hostName){
        SharedPreferences sp = c.getSharedPreferences(SHARED_PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SHARED_PREFS_HOST, hostName);
        editor.commit();
    }

    public static String getPort(final Context context){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        return sp.getString(SHARED_PREFS_PORT, null);
    }

    public static void setPassword(final Context context, final String portNumber){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SHARED_PREFS_PORT, portNumber);
        editor.commit();
    }

    public static boolean isConnectedToNetwork(final Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }
}
