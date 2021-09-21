package com.example.alarmclock;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private SampDatabaseHelper helper = null;
    private RecyclerView recyclerView;
    public static ListAdapter adapter;
    private Resources res;
    private Drawable deleteIcon;
    private MainListAdapter sc_adapter;

//    MainListAdapter sc_adapter;


    // アクティビティの初期化処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rLayoutManager);
//        onShow();
        helper = new SampDatabaseHelper(this);

        // データベースを検索する項目を定義
        String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME};
        onswiped();

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            //データベースに格納されている全データを格納するリスト
            ArrayList<String> timeList = new ArrayList<>();
            if (cursor.moveToFirst())
            {
                String time = cursor.getString(1);
                timeList.add(time);
            }
            while(cursor.moveToNext())
            {
                String time = cursor.getString(1);
                timeList.add(time);
            }
            cursor.close();

            adapter = new ListAdapter(timeList);
            recyclerView.setAdapter(adapter);

            ItemSwipeController swipeController = new ItemSwipeController(0,ItemTouchHelper.LEFT){
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    adapter.removeAt(viewHolder.getBindingAdapterPosition());
                }
            };

            res = getApplicationContext().getResources();
            deleteIcon = ResourcesCompat.getDrawable(res, R.drawable.dustbox, null);
            Bitmap orgBitmap = ((BitmapDrawable) deleteIcon).getBitmap();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(orgBitmap, 30, 30, false);
            deleteIcon =  new BitmapDrawable(res, resizedBitmap);
            swipeController.setDeleteIcon(deleteIcon);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
            itemTouchHelper.attachToRecyclerView(recyclerView);




            // 検索結果から取得する項目を定義
//            String[] from = {DBContract.DBEntry.COLUMN_NAME_TIME};

            // データを設定するレイアウトのフィールドを定義
//            int[] to = {R.id.title};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
//            sc_adapter = new MainListAdapter(
//                    this, R.layout.row_main, cursor, from, to,0);

            // activity_main.xmlに定義したListViewオブジェクトを取得
//            ListView list = findViewById(R.id.mainList);

            // ListViewにアダプターを設定
//            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたときの処理
//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView av, View view, int position, long id) {
//
//                    //　クリックされた行のデータを取得
//                    Cursor cursor = (Cursor)av.getItemAtPosition(position);
//
//                    // テキスト登録画面 Activity へのインテントを作成
//                    Intent intent  = new Intent(MainActivity.this, TextActivity.class);
//
//                    intent.putExtra(DBContract.DBEntry._ID, cursor.getInt(0));
//                    intent.putExtra(DBContract.DBEntry.COLUMN_NAME_TIME, cursor.getString(1));
//
//
//                    // アクティビティを起動
//                    startActivity(intent);
//                }
//            });
        }
    }



    // アクティビティの再開処理
    @Override
    protected void onResume() {
        super.onResume();

        // データを一覧表示
        onShow();
        onswiped();
    }

    // データを一覧表示
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new SampDatabaseHelper(this);

        // データベースを検索する項目を定義
        String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME};

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            //データベースに格納されている全データを格納するリスト
            ArrayList<String> timeList = new ArrayList<>();
            if (cursor.moveToFirst())
            {
                String time = cursor.getString(1);
                timeList.add(time);
            }
            while(cursor.moveToNext())
            {
                String time = cursor.getString(1);
                timeList.add(time);
            }
            cursor.close();



            adapter = new ListAdapter(timeList);
            recyclerView.setAdapter(adapter);

//            ItemSwipeController swipeController = new ItemSwipeController(0,ItemTouchHelper.LEFT){
//                @Override
//                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                    int position = viewHolder.getBindingAdapterPosition();
//                    adapter.removeAt(position);
//                    try (SQLiteDatabase db = helper.getWritableDatabase()) {
////                        db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry._ID+" = ?", new String[] {String.valueOf(position + 1)});
//                        db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry._ID+" = ?", new String[] {String.valueOf(position + 1)});
//                        Log.i("test", String.valueOf(position + 1));
//                    } catch (Exception e)
//                    {
//                        Log.i("test","don't open the db");
//                    }
//                }
//            };

//            res = getApplicationContext().getResources();
//            deleteIcon = ResourcesCompat.getDrawable(res, R.drawable.dustbox, null);
//            Bitmap orgBitmap = ((BitmapDrawable) deleteIcon).getBitmap();
//            Bitmap resizedBitmap = Bitmap.createScaledBitmap(orgBitmap, 30, 30, false);
//            deleteIcon =  new BitmapDrawable(res, resizedBitmap);
//            swipeController.setDeleteIcon(deleteIcon);
//            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
//            itemTouchHelper.attachToRecyclerView(recyclerView);




            // 検索結果から取得する項目を定義
//            String[] from = {DBContract.DBEntry.COLUMN_NAME_TIME};

            // データを設定するレイアウトのフィールドを定義
//            int[] to = {R.id.title};

            // ListViewの1行分のレイアウト(row_main.xml)と検索結果を関連付け
//            sc_adapter = new MainListAdapter(
//                    this, R.layout.row_main, cursor, from, to,0);

            // activity_main.xmlに定義したListViewオブジェクトを取得
//            ListView list = findViewById(R.id.mainList);

            // ListViewにアダプターを設定
//            list.setAdapter(sc_adapter);

            // リストの項目をクリックしたときの処理
//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView av, View view, int position, long id) {
//
//                    //　クリックされた行のデータを取得
//                    Cursor cursor = (Cursor)av.getItemAtPosition(position);
//
//                    // テキスト登録画面 Activity へのインテントを作成
//                    Intent intent  = new Intent(MainActivity.this, TextActivity.class);
//
//                    intent.putExtra(DBContract.DBEntry._ID, cursor.getInt(0));
//                    intent.putExtra(DBContract.DBEntry.COLUMN_NAME_TIME, cursor.getString(1));
//
//
//                    // アクティビティを起動
//                    startActivity(intent);
//                }
//            });
        }
    }

    private void onswiped()
    {
        ItemSwipeController swipeController = new ItemSwipeController(0,ItemTouchHelper.LEFT){
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                adapter.removeAt(position);
                TextView textView = viewHolder.itemView.findViewById(R.id.title);
                String text = textView.getText().toString();


                try (SQLiteDatabase db = helper.getWritableDatabase()) {
//                        db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry._ID+" = ?", new String[] {String.valueOf(position + 1)});
                    db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry.COLUMN_NAME_TIME+" = ?", new String[] {text});
                    Log.i("test", String.valueOf(4));
                } catch (Exception e)
                {
                    Log.i("test","don't open the db");
                }
            }
        };

        res = getApplicationContext().getResources();
        deleteIcon = ResourcesCompat.getDrawable(res, R.drawable.dustbox, null);
        Bitmap orgBitmap = ((BitmapDrawable) deleteIcon).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(orgBitmap, 30, 30, false);
        deleteIcon =  new BitmapDrawable(res, resizedBitmap);
        swipeController.setDeleteIcon(deleteIcon);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    // 「+」フローティング操作ボタン　タップ時に呼び出されるメソッド
    public void fab_reg_onClick(View view) {

        // テキスト登録画面 Activity へのインテントを作成
        Intent intent  = new Intent(MainActivity.this, TextActivity.class);

        // アクティビティを起動
        startActivity(intent);
    }
}