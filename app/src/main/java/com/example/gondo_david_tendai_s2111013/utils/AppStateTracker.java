//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.utils;

public class AppStateTracker {

    private static boolean isAppInForeground = false;

    // Activity lifecycle callbacks in your Activities
    public static void activityResumed() {
        isAppInForeground = true;
    }

    public static void activityPaused() {
        isAppInForeground = false;
    }

    public static boolean isAppInForeground() {
        return isAppInForeground;
    }
}
