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
                        DBDef.DBEntry.COLUMN_NAME_UPDATE + " INTEGER DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))) ");




        // トリガーを作成
        db.execSQL(
                "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBDef.DBEntry.TABLE_NAME +
                        " BEGIN "+
                        " UPDATE " + DBDef.DBEntry.TABLE_NAME + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
                        " END;");


//テーブル作成２
//        db.execSQL(
//                "CREATE TABLE "+ DBDef.DBEntry.TABLE_NAME2 + " (" +
//                        DBDef.DBEntry._ID + " INTEGER PRIMARY KEY, " +
//                        DBDef.DBEntry.COLUMN_NAME_FOOT_COUNT + " TEXT default '', " +
//                        DBDef.DBEntry.COLUMN_SOUND_LEVEL_FORMER+ " TEXT default '1', " +
//                        DBDef.DBEntry.COLUMN_SOUND_LEVEL_LATTER + " TEXT default '0') ");




//        db.execSQL(
//                "CREATE TRIGGER trigger_samp_tbl_update AFTER UPDATE ON " + DBContract.DBEntry.TABLE_NAME2 +
//                        " BEGIN "+
//                        " UPDATE " + DBContract.DBEntry.TABLE_NAME2 + " SET up_date = DATETIME('now', 'localtime') WHERE rowid == NEW.rowid; "+
//                        " END;");




        //テーブル作成３


//        db.execSQL(
//                "CREATE TABLE "+ DBDef.DBEntry.TABLE_NAME3 + " (" +
//                        DBDef.DBEntry._ID + " INTEGER PRIMARY KEY, " +
//                        DBDef.DBEntry.COGNOMEN + " TEXT default '') ");
//
//
//        db.execSQL(
//                "CREATE TABLE "+ DBDef.DBEntry.TABLE_NAME4 + " (" +
//                        DBDef.DBEntry._ID + " INTEGER PRIMARY KEY, " +
//                        DBDef.DBEntry.DATA+ " TEXT default '') ");
    }







    // データベースをバージョンアップした時、テーブルを削除してから再作成
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + DBDef.DBEntry.TABLE_NAME);
        onCreate(db);

//        db.execSQL("DROP TABLE IF EXISTS " + DBDef.DBEntry.TABLE_NAME2);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + DBDef.DBEntry.TABLE_NAME3);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + DBDef.DBEntry.TABLE_NAME4);
//        onCreate(db);




    }




}
