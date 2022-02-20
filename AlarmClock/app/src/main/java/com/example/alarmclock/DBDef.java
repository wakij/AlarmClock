package com.example.alarmclock;

import android.provider.BaseColumns;

public final class DBDef {

    // 誤ってインスタンス化しないようにコンストラクタをプライベート宣言
    private DBDef() {}

    // テーブルの内容を定義
    public static class DBEntry implements BaseColumns {
        // BaseColumns インターフェースを実装することで、内部クラスは_IDを継承できる
        public static final String TABLE_NAME           = "samp_tbl";
        public static final String COLUMN_NAME_TIME    = "time";
        public static final String SWITCH_CONDITION = "switch_conditions";
        public static final String COLUMN_NAME_UPDATE   = "up_date";
        public static final String MEMO ="memo";
        public static final String CARD_COLOR = "card_color";

        public static final String CHART_TABLE = "chart_info";
        public static final String DATE = "date";
        public static final String REAL_WAKE_UP_TIME = "real_wake_up_time";
        public static final String ESTIMATED_WAKE_UP_TIME = "estimated_wake_up_time";
        public static final String FOOTSTEP_COUNT = "footstep_count";
    }
}