package com.example.alarmclock;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class LifecycleObserver extends Application{
    public static boolean isMainActivityOn;
    public static boolean isAlarmSetSceneOn;

    @Override
    public void onCreate()
    {
        super.onCreate();
        registerActivityLifecycleCallbacks(new LifecycleObserverCallback());
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
    }



    static class LifecycleObserverCallback implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            switch (activity.getLocalClassName())
            {
                case "MainActivity":
                    Log.e("start","MainActivity");
                    isMainActivityOn = true;
                    break;
                case "AlarmSetScene":
                    Log.e("start","AlarmSetScene");
                    isAlarmSetSceneOn = true;
                    break;
            }

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            switch (activity.getLocalClassName())
            {
                case "MainActivity":
                    Log.e("Resume","MainActivity");
                    isMainActivityOn = true;
                    break;
                case "AlarmSetScene":
                    Log.e("Resume","AlarmSetScene");
                    isAlarmSetSceneOn = true;
                    break;
            }
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            switch (activity.getLocalClassName())
            {
                case "MainActivity":
                    Log.e("Pause","MainActivity");
                    isMainActivityOn = false;
                    break;
                case "AlarmSetScene":
                    Log.e("Pause","AlarmSetScene");
                    isAlarmSetSceneOn = false;
                    break;
            }
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    }
}




