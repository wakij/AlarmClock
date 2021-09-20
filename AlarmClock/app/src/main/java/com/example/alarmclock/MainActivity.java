package com.example.alarmclock;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private SampDatabaseHelper helper = null;
    MainListAdapter sc_adapter;






    // アクティビティの初期化処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // アクティビティの再開処理
    @Override
    protected void onResume() {
        super.onResume();

        // データを一覧表示
        onShow();
    }

    // データを一覧表示
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new SampDatabaseHelper(this);

        // データベースを検索する項目を定義
        String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_HOUR, DBContract.DBEntry.COLUMN_NAME_MINUTES };

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            // 検索結果から取得する項目を定義
            String[] from = {DBContract.DBEntry.COLUMN_NAME_HOUR};

            // データを設定するレイアウトのフィールドを定義
            int[] to = {R.id.title};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
            sc_adapter = new MainListAdapter(
                    this, R.layout.row_main, cursor, from, to,0);

            // activity_main.xmlに定義したListViewオブジェクトを取得
            ListView list = findViewById(R.id.mainList);

            // ListViewにアダプターを設定
            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたときの処理
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView av, View view, int position, long id) {

                    //　クリックされた行のデータを取得
                    Cursor cursor = (Cursor)av.getItemAtPosition(position);

                    // テキスト登録画面 Activity へのインテントを作成
                    Intent intent  = new Intent(MainActivity.this, TextActivity.class);

                    intent.putExtra(DBContract.DBEntry._ID, cursor.getInt(0));
                    intent.putExtra(DBContract.DBEntry.COLUMN_NAME_HOUR, cursor.getString(1));
                    intent.putExtra(DBContract.DBEntry.COLUMN_NAME_MINUTES, cursor.getString(2));

                    // アクティビティを起動
                    startActivity(intent);
                }
            });
        }
    }

    // 削除ボタン　タップ時に呼び出されるメソッド
    public void btnDel_onClick(View view){

        // MainListAdapterで設定されたリスト内の位置を取得
        int pos = (Integer)view.getTag();

        // アダプターから、_idの値を取得
        int id = ((Cursor) sc_adapter.getItem(pos)).getInt(0);

        // データを削除
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry._ID+" = ?", new String[] {String.valueOf(id)});
        }

        // データを一覧表示
        onShow();
    }

    // 「+」フローティング操作ボタン　タップ時に呼び出されるメソッド
    public void fab_reg_onClick(View view) {

        // テキスト登録画面 Activity へのインテントを作成
        Intent intent  = new Intent(MainActivity.this, TextActivity.class);

        // アクティビティを起動
        startActivity(intent);
    }
}