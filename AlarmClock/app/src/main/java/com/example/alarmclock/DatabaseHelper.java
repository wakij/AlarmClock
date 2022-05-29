package com.example.alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // データベースのバージョン
    // テーブルの内容などを変更したら、この数字を変更する
    static final private int VERSION = 2;

    // データベース名
    static final private String DBNAME = "samp.db";
    static final private String CHART_DBNAME = "chart_db";

    // コンストラクタは必ず必要
    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    // データベース作成時にテーブルを作成
    public void onCreate(SQLiteDatabase db) {

        // テーブルを作成
        db.execSQL(
                "CREATE TABLE "+ DBDef.DBEntry.TABLE_NAME + " (" +
                        DBDef.DBEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBDef.DBEntry.COLUMN_NAME_TIME + " TEXT default '', " +
                        DBDef.DBEntry.SWITCH_CONDITION+ " TEXT default '',"+
                        DBDef.DBEntry.MEMO+ " TEXT default '',"+
                        DBDef.DBEntry.CARD_COLOR +  " TEXT default 'red', " +
                        DBDef.DBEntry.COLUMN_NAME_UPDATE + " INTEGER DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))) ");

        db.execSQL(
                "CREATE TABLE " + DBDef.DBEntry.CHART_TABLE + " (" +
                        DBDef.DBEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBDef.DBEntry.DATE + " TEXT default '', " +
                        DBDef.DBEntry.REAL_WAKE_UP_TIME+ " TEXT default '',"+
                        DBDef.DBEntry.ESTIMATED_WAKE_UP_TIME+ " TEXT default '',"+
                        DBDef.DBEntry.FOOTSTEP_COUNT +  " TEXT default '')"

        );




        // トリガーを作成
        db.execSQL(
                "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBDef.DBEntry.TABLE_NAME +
                        " BEGIN "+
                        " UPDATE " + DBDef.DBEntry.TABLE_NAME + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
                        " END;");

        db.execSQL(
                "CREATE TRIGGER trigger_chart_info_update AFTER UPDATE ON " + DBDef.DBEntry.CHART_TABLE +
                        " BEGIN "+
                        " UPDATE " + DBDef.DBEntry.CHART_TABLE + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
                        " END;");


    }







    // データベースをバージョンアップした時、テーブルを削除してから再作成
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + DBDef.DBEntry.TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + DBDef.DBEntry.CHART_TABLE);
        onCreate(db);

    }




}
