package com.example.alarmclock;

public class AlarmData {
    String date_;
    String real_wake_up_time_;
    String estimated_wake_up_time_;
    String foodstep_count_;

    AlarmData(String date, String real_wake_up_time, String estimated_wake_up_time, String foodstep_count)
    {
        date_ = date;
        real_wake_up_time_ = real_wake_up_time;
        estimated_wake_up_time_ = estimated_wake_up_time;
        foodstep_count_ = foodstep_count;
    }
}
