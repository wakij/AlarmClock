package com.example.alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SampDatabaseHelper extends SQLiteOpenHelper {

    // データベースのバージョン
    // テーブルの内容などを変更したら、この数字を変更する
    static final private int VERSION = 2;

    // データベース名
    static final private String DBNAME = "samp.db";

    // コンストラクタは必ず必要
    public SampDatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    // データベース作成時にテーブルを作成
    public void onCreate(SQLiteDatabase db) {

        // テーブルを作成
        db.execSQL(
                "CREATE TABLE "+ DBContract.DBEntry.TABLE_NAME + " (" +
                        DBContract.DBEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.DBEntry.COLUMN_NAME_TIME + " TEXT default '', " +
                        DBContract.DBEntry.SWITCH_CONDITION+ " TEXT default '',"+
                        DBContract.DBEntry.MEMO+ " TEXT default '',"+
                        DBContract.DBEntry.COLUMN_NAME_UPDATE + " INTEGER DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))) ");




        // トリガーを作成
        db.execSQL(
                "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBContract.DBEntry.TABLE_NAME +
                        " BEGIN "+
                        " UPDATE " + DBContract.DBEntry.TABLE_NAME + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
                        " END;");


//テーブル作成２
        db.execSQL(
                "CREATE TABLE "+ DBContract.DBEntry.TABLE_NAME2 + " (" +
                        DBContract.DBEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT + " TEXT default '500', " +
                        DBContract.DBEntry.COLUMN_SOUND_LEVEL_FORMER+ " TEXT default '1', " +
                        DBContract.DBEntry.COLUMU_SOUND_LEVEL_LATTER+ " TEXT default '0') ");




//        db.execSQL(
//                "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBContract.DBEntry.TABLE_NAME2 +
//                        " BEGIN "+
//                        " UPDATE " + DBContract.DBEntry.TABLE_NAME2 + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
//                        " END;");




        //テーブル作成３


        db.execSQL(
                "CREATE TABLE "+ DBContract.DBEntry.TABLE_NAME3 + " (" +
                        DBContract.DBEntry._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.DBEntry.COGNOMEN + " TEXT default '') ");
    }







    // データベースをバージョンアップした時、テーブルを削除してから再作成
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DBEntry.TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DBEntry.TABLE_NAME2);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DBEntry.TABLE_NAME3);
        onCreate(db);




    }




}
