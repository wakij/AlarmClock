package com.example.alarmclock;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
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
    private AlarmManager am;


    // アクティビティの初期化処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rLayoutManager);
        onShow();
        // データベースを検索する項目を定義
        onswiped();
        //RecycleViewを枠線をいれる
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
//        getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.navigationBars());
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }




    // アクティビティの再開処理
    @Override
    protected void onResume() {
        super.onResume();
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // データを一覧表示
        onShow();
        onswiped();
    }

    // データを一覧表示
    protected void onShow() {

        // データベースヘルパーを準備
        helper = new SampDatabaseHelper(this);

        // データベースを検索する項目を定義
        String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION};

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            //データベースに格納されている全データを格納するリスト
//            ArrayList<String> timeList = new ArrayList<>();
            ArrayList<AlarmInfo> alarmLists = new ArrayList<>();
            if (cursor.moveToFirst())
            {
                int id = cursor.getInt(0);
                String time = cursor.getString(1);
                String isSwitchOn = cursor.getString(2);
                AlarmInfo alarmData = new AlarmInfo(id, time, isSwitchOn);
                alarmLists.add(alarmData);

            }
            while(cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String time = cursor.getString(1);
                String isSwitchOn = cursor.getString(2);
                AlarmInfo alarmData = new AlarmInfo(id, time, isSwitchOn);
                alarmLists.add(alarmData);
            }
            cursor.close();

            adapter = new ListAdapter(alarmLists);
            recyclerView.setAdapter(adapter);

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
                    String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION};
                    Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, DBContract.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{text}
                            , null, null, null, null);
                    if (cursor.moveToFirst())
                    {
                        int id = cursor.getInt(0);

                        if (am != null)
                        {
                            Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
                            Log.e("intent1",intent.toString());
                            Log.e("apple","id: " + String.valueOf(id));
                            PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.cancel(pending);
                        }
                    }

                    db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry.COLUMN_NAME_TIME+" = ?", new String[] {text});
                } catch (Exception e)
                {
                    Log.e("test","don't open the db");
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

//    public void toUsage(View view)
//    {
//        Intent intent = new Intent(MainActivity.this, WebShow.class);

//        startActivity(intent);
//    }


    public void tofootcountShow(View view){
        Intent intent = new Intent(MainActivity.this, FootCountShow.class);
        startActivity(intent);
    }

    public void toLevelShow(View view)
    {
        Intent intent = new Intent(MainActivity.this, LevelShow.class);
        startActivity(intent);
    }

    public void toWebShow(View view)
    {
        Intent intent = new Intent(MainActivity.this, Help.class);
        startActivity(intent);
    }
}