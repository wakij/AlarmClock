package com.example.alarmclock;

import android.provider.BaseColumns;

public final class DBContract {

    // 誤ってインスタンス化しないようにコンストラクタをプライベート宣言
    private DBContract() {}

    // テーブルの内容を定義
    public static class DBEntry implements BaseColumns {
        // BaseColumns インターフェースを実装することで、内部クラスは_IDを継承できる
        public static final String TABLE_NAME           = "samp_tbl";
        public static final String COLUMN_NAME_TIME    = "time";
        public static final String SWITCH_CONDITION = "switch_conditions";
        public static final String COLUMN_NAME_UPDATE   = "up_date";


        public static final String TABLE_NAME2           = "samp_tbl2";
        public static final String COLUMN_NAME_FOOT_COUNT    = "foot_count";
        public static final String COLUMN_SOUND_LEVEL_FORMER= "sound_level_former";
        public static final String COLUMU_SOUND_LEVEL_LATTER= "sound_level_latter";




    }
}