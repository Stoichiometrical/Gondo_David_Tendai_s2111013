//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.utils;

import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.KEY_DEFAULT_CITY;
import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.getCachedValue;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


import com.example.gondo_david_tendai_s2111013.R;
import com.example.gondo_david_tendai_s2111013.model.LatestWeather;

public class UpdateUtils {

    public static final String NOTIFICATION_CHANNEL_ID = "weather_updates";
    private static boolean isChannelCreated = false; // Flag to track channel creation

    public static void sendWeatherNotification(Context context, LatestWeather latestWeather) {
        String cityId = getCachedValue(KEY_DEFAULT_CITY);
        String city= HelperMethods.getCityName(cityId);
        // 1. Build the notification content
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logos)  // Customize with a relevant weather icon
                .setContentTitle("Weather Update for " + city)
                .setContentText(String.format("It's currently %s °C in %s", latestWeather.getTemp(), city))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 2. Send the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = 1; // Or any unique ID for your weather notification
        // Permission Check
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission, so we need to request it:
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return; // Stop here if permission is not granted
        }

        // Permission already granted, send the notification
        notificationManager.notify(notificationId, builder.build());
    }

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isChannelCreated) {
            String channelName = "Weather Updates";
            String channelDescription = "Notifications for weather changes";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            isChannelCreated = true;
        }
    }



}

