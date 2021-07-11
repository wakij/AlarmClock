package com.example.alarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "TestDB.db";
    private final static String DB_TABLE1 = "wakeuptime";
    private final static String DB_TABLE2 = "countstep";


    DataOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_TABLE1 + "(id integer primary key, wakeUpTime integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_TABLE2 + "(id integer primary key, countStep interger)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE1);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE2);
        //新規テーブルの作成
        onCreate(db);

    }

    public void savaDada(SQLiteDatabase db, String TableName, int value)
    {
        ContentValues contentValues = new ContentValues();
        if (TableName.equals(DB_TABLE1)){
            contentValues.put("wakeUpTime", value);
        }
        else if (TableName.equals(DB_TABLE2)){
            contentValues.put("DB_TABLE2", value);
        }
        db.insert(DATABASE_NAME,null,contentValues);
    }

    //行数の取得
    public long getProfilesCount(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        long count= DatabaseUtils.queryNumEntries(db, tableName);
        db.close();
        return count;
    }

}
