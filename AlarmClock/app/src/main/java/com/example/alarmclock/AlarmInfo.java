package com.example.alarmclock;

public class AlarmInfo {
    private int id;
    private String time;
    private int hour;
    private int minutes;
    private String isSwitchOn;
    private String memo;
    private String card_color;

    AlarmInfo(int _id, String _time, String _isSwitchOn ,String Memo,String _card_color)
    {
        id = _id;
        time = _time;
        String[] hour_minutes = time.split(":");
        hour = Integer.parseInt(hour_minutes[0]);
        minutes = Integer.parseInt(hour_minutes[1]);
        isSwitchOn = _isSwitchOn;
        memo=Memo;
        card_color = _card_color;
    }


    public int getId(){return id;}
    public int getHour(){return hour;}
    public int getMinutes(){return minutes;}
    public String getTime(){return time;}
    public String getIsSwitchOn(){return isSwitchOn;}
    public String getMemo(){return memo;}
    public String getCard_color(){return card_color;}


    public void setHour(int _hour){hour = _hour;}
    public void setMinutes(int _minutes){minutes = _minutes;}
    public void setTime(String _time){time = _time;}
    public void setIsSwitchOn(String _isSwitchOn){isSwitchOn = _isSwitchOn;}
    public void setCard_color(String _card_color){card_color = _card_color;}

}
