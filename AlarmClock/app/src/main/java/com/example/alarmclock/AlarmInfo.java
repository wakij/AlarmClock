package com.example.alarmclock;

public class AlarmInfo {
    private int id;
    private String time;
    private int hour;
    private int minutes;
    private String isSwitchOn;

    AlarmInfo(int _id, String _time, String _isSwitchOn)
    {
        id = _id;
        time = _time;
        String[] hour_minutes = time.split(":");
        hour = Integer.parseInt(hour_minutes[0]);
        minutes = Integer.parseInt(hour_minutes[1]);
        isSwitchOn = _isSwitchOn;
    }

    AlarmInfo(int _id, int _hour, int _minutes, String _isSwitchOn)
    {
        id = _id;
        hour = _hour;
        minutes = _minutes;
        time = hour + ":" + minutes;
        isSwitchOn = _isSwitchOn;
    }

    public int getId(){return id;}
    public int getHour(){return hour;}
    public int getMinutes(){return minutes;}
    public String getTime(){return time;}
    public String getIsSwitchOn(){return isSwitchOn;}

    //    public void setId(int _id){id = _id;}
    public void setHour(int _hour){hour = _hour;}
    public void setMinutes(int _minutes){minutes = _minutes;}
    public void setTime(String _time){time = _time;}
    public void setIsSwitchOn(String _isSwitchOn){isSwitchOn = _isSwitchOn;}

}
