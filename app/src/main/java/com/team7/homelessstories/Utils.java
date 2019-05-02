package com.team7.homelessstories;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    // Transition duration in ms
    public static final int TRANSITION_DURATION = 100;
    private static SharedPreferences prefs;

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static SharedPreferences getPrefs(Context context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("com.team7.homelessstories", Context.MODE_PRIVATE);
        }
        return prefs;
    }
}
