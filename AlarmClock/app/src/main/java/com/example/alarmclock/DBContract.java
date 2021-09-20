package com.example.alarmclock;

import android.provider.BaseColumns;

public final class DBContract {

    // 誤ってインスタンス化しないようにコンストラクタをプライベート宣言
    private DBContract() {}

    // テーブルの内容を定義
    public static class DBEntry implements BaseColumns {
        // BaseColumns インターフェースを実装することで、内部クラスは_IDを継承できる
        public static final String TABLE_NAME           = "samp_tbl";
        public static final String COLUMN_NAME_HOUR    = "hour";
        public static final String COLUMN_NAME_MINUTES = "minutes";
        public static final String COLUMN_NAME_UPDATE   = "up_date";
    }
}