//Author: David Tendai Gondo
//StudentID :s2111013

package com.example.gondo_david_tendai_s2111013.ui.settings;

import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.KEY_UPDATE_TIME1;
import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.KEY_UPDATE_TIME2;
import static com.example.gondo_david_tendai_s2111013.utils.PreferencesManager.applyTheme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.gondo_david_tendai_s2111013.R;
import com.example.gondo_david_tendai_s2111013.utils.HelperMethods;
import com.example.gondo_david_tendai_s2111013.model.UpdateWorker;
import com.example.gondo_david_tendai_s2111013.utils.PreferencesManager;


import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SettingsActivity extends AppCompatActivity {



    private TimePicker timePicker1, timePicker2;
    private Spinner locationSpinner;

    private ToggleButton themeToggle;

    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        timePicker1 = findViewById(R.id.t1);
        timePicker2 = findViewById(R.id.t2);
        locationSpinner = findViewById(R.id.campuses);
        themeToggle  = findViewById(R.id.toggleButton);

        preferencesManager = new PreferencesManager(this);
        applyTheme(this);


        HelperMethods.setupSpinnerAdapter(this,locationSpinner);



        // Get theme setting from SharedPreferences
        String themeSetting = preferencesManager.getCachedValue(PreferencesManager.KEY_THEME);

        // Set switch state based on theme setting
        themeToggle.setChecked(themeSetting.equals("dark"));


    }


    public void saveSettings(View view) {

        int hour1 = timePicker1.getHour();
        int minute1 = timePicker1.getMinute();
        String updateTime1 = String.format("%02d:%02d", hour1, minute1);

        int hour2 = timePicker2.getHour();
        int minute2 = timePicker2.getMinute();
        String updateTime2 = String.format("%02d:%02d", hour2, minute2);

        // Get and store selected location
        String selectedLocation = locationSpinner.getSelectedItem().toString();
        String locationId = HelperMethods.getCityId(selectedLocation);


        // Get theme status
        boolean isDarkTheme = themeToggle.isChecked();
        String themeSetting = isDarkTheme ? "dark" : "light";
        Log.d("X1X1",themeSetting);

        preferencesManager.updateSettings(locationId, themeSetting, updateTime1, updateTime2);

        scheduleUpdateWork(getApplicationContext());

        // Show toast message
        Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
        recreate();

    }


    public static void scheduleUpdateWork(Context context) {
        String updateTime1String = PreferencesManager.getCachedValue(KEY_UPDATE_TIME1);
        String updateTime2String = PreferencesManager.getCachedValue(KEY_UPDATE_TIME2);

        // Parse update times
        String[] updateTime1Parts = updateTime1String.split(":");
        String[] updateTime2Parts = updateTime2String.split(":");
        int updateHour1 = Integer.parseInt(updateTime1Parts[0]);
        int updateMinute1 = Integer.parseInt(updateTime1Parts[1]);
        int updateHour2 = Integer.parseInt(updateTime2Parts[0]);
        int updateMinute2 = Integer.parseInt(updateTime2Parts[1]);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Schedule work for update time 1
        PeriodicWorkRequest updateWorkRequest1 =
                new PeriodicWorkRequest.Builder(UpdateWorker.class, 1, TimeUnit.DAYS)
                        .setConstraints(constraints)
                        .setInitialDelay(calculateInitialDelay(updateHour1, updateMinute1), TimeUnit.MILLISECONDS)
                        .build();

        // Schedule work for update time 2
        PeriodicWorkRequest updateWorkRequest2 =
                new PeriodicWorkRequest.Builder(UpdateWorker.class, 1, TimeUnit.DAYS)
                        .setConstraints(constraints)
                        .setInitialDelay(calculateInitialDelay(updateHour2, updateMinute2), TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "UpdateWork1",
                ExistingPeriodicWorkPolicy.REPLACE,
                updateWorkRequest1);

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "UpdateWork2",
                ExistingPeriodicWorkPolicy.REPLACE,
                updateWorkRequest2);
    }


    private static long calculateInitialDelay(int hourOfDay, int minute) {
        Calendar now = Calendar.getInstance();
        Calendar scheduledTime = Calendar.getInstance();

        // Set scheduled times
        scheduledTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        scheduledTime.set(Calendar.MINUTE, minute);
        scheduledTime.set(Calendar.SECOND, 0);

        // If the scheduled time is in the past, schedule it for tomorrow
        if (now.after(scheduledTime)) {
            scheduledTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Calculate initial delay in milliseconds
        return scheduledTime.getTimeInMillis() - now.getTimeInMillis();
    }

}