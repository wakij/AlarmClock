package com.example.alarmclock;


import android.app.AlarmManager;
import android.app.Dialog;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper = null;
    private String data;
    private RecyclerView recyclerView;
    private Resources res;
    private Drawable deleteIcon;
    private TabLayout tabLayout;
    private AlarmBroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private Dialog dialog;
    private int nowpos;
    private int count;
    private int positivecount=0;
    private Handler handler;



    // アクティビティの初期化処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("Info", MODE_PRIVATE);
        int needfootstep = sharedPreferences.getInt("needfootstep", 0);

//        SharedPreferences.Editor editor = sharedPreferences.edit().clear();
//        editor.commit();


        if (needfootstep == 0) {
            //Create the Dialog here
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog_layout);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false); //Optional
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
            dialog.show();

            Button Okay = dialog.findViewById(R.id.btn_okay);
            Button Cancel = dialog.findViewById(R.id.btn_cancel);

            Okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nowpos++;
                    changeDialog(nowpos);
                    positivecount++;
                }
            });

            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nowpos++;
                    changeDialog(nowpos);
                }
            });
        }
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                updateAlarmListScene();
            }
        };
        AlarmBroadcastReceiver.registerHandler(handler);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.settingsContainer, new AlarmListScene());
        fragmentTransaction.commit();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.getTabAt(0).setCustomView(R.layout.tablayout1);
        tabLayout.getTabAt(1).setCustomView(R.layout.tablayout2);
        tabLayout.getTabAt(2).setCustomView(R.layout.tablayout3);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                selectTabColor(tab);
                switch (tab.getPosition()) {
                    case 0:
                        fragmentTransaction.replace(R.id.settingsContainer, new AlarmListScene());
                        break;
                    case 1:
                        fragmentTransaction.replace(R.id.settingsContainer, new LevelShow());
                        break;
                    case 2:
                        fragmentTransaction.replace(R.id.settingsContainer, new HelpeScene());
                        break;
                }
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                clearTabColor(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                selectTabColor(tab);
            }
        });

//        tabLayout.selectTab(tabLayout.getTabAt(2),true);
        tabLayout.selectTab(tabLayout.getTabAt(0),true);



    }


    public void selectTabColor(TabLayout.Tab tab)
    {
        ImageView imageView = tab.getCustomView().findViewById(R.id.icon);
        imageView.setColorFilter(Color.parseColor("#396EB0"));
    }
    public void clearTabColor(TabLayout.Tab tab)
    {
        ImageView imageView = tab.getCustomView().findViewById(R.id.icon);
        imageView.clearColorFilter();;
    }

    public void updateAlarmListScene()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.settingsContainer, new AlarmListScene());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("MainActivity","破壊されました");
    }


    @Override
    public void onPause()
    {
        super.onPause();
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Log.e("MainActivity","再開されました");
    }


    private void changeDialog(int pos)
    {
        TextView question_text = dialog.findViewById(R.id.textView2);
        switch (pos)
        {
            case 1:
                question_text.setText("朝起きるのはお得意ですか？");
                break;
            case 2:
                question_text.setText("夜更かししがちですか？");
                break;
            case 3:
                question_text.setText("自分に厳しくしたいですか？");
                break;
            case 4:
                writeQResult();
                dialog.dismiss();
        }
    }

//    アンケートの結果をデータベースに書き込む
    private void writeQResult()
    {
        switch (positivecount){
            case 0:
                count=50;
                break;
            case 1:
                count=80;
                break;
            case 2:
                count=90;
                break;
            case 3:
                count=100;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Info",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("needfootstep",count);
        editor.apply();
    }

//    final Handler updataHandler = new Handler(getMainLooper())
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            Log.e("apple","Apple");
//        }
//    };










    // アクティビティの再開処理
//    @Override
//    protected void onResume() {
//        super.onResume();
//        am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        // データを一覧表示
//        onShow();
//        onswiped();
//    }
//
//    // データを一覧表示
//    protected void onShow() {
//
//        // データベースヘルパーを準備
//        helper = new SampDatabaseHelper(this);
//
//        // データベースを検索する項目を定義
//        String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION,DBContract.DBEntry.MEMO};
//
//        // 読み込みモードでデータベースをオープン
//        try (SQLiteDatabase db = helper.getReadableDatabase()){
//
//            // データベースを検索
//            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, null,
//                    null, null, null, null, null);
//
//            //データベースに格納されている全データを格納するリスト
////            ArrayList<String> timeList = new ArrayList<>();
//            ArrayList<AlarmInfo> alarmLists = new ArrayList<>();
//            if (cursor.moveToFirst())
//            {
//                int id = cursor.getInt(0);
//                String time = cursor.getString(1);
//                String isSwitchOn = cursor.getString(2);
//                String memo =cursor.getString(3);
//                AlarmInfo alarmData = new AlarmInfo(id, time, isSwitchOn,memo);
//                alarmLists.add(alarmData);
//
//            }
//            while(cursor.moveToNext())
//            {
//                int id = cursor.getInt(0);
//                String time = cursor.getString(1);
//                String isSwitchOn = cursor.getString(2);
//                String memo=cursor.getString(3);
//                AlarmInfo alarmData = new AlarmInfo(id, time, isSwitchOn,memo);
//                alarmLists.add(alarmData);
//            }
//            cursor.close();
//
//            adapter = new ListAdapter(alarmLists);
//            recyclerView.setAdapter(adapter);
//
//        }
//    }
//
//    private void onswiped()
//    {
//        ItemSwipeController swipeController = new ItemSwipeController(0,ItemTouchHelper.LEFT){
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getBindingAdapterPosition();
//                adapter.removeAt(position);
//                TextView textView = viewHolder.itemView.findViewById(R.id.title);
//                String text = textView.getText().toString();
//                try (SQLiteDatabase db = helper.getWritableDatabase()) {
//                    String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION};
//                    Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, DBContract.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{text}
//                            , null, null, null, null);
//                    if (cursor.moveToFirst())
//                    {
//                        int id = cursor.getInt(0);
//
//                        if (am != null)
//                        {
//                            Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
//                            Log.e("intent1",intent.toString());
//                            Log.e("apple","id: " + String.valueOf(id));
//                            PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                            am.cancel(pending);
//                        }
//                    }
//
//                    db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry.COLUMN_NAME_TIME+" = ?", new String[] {text});
//                } catch (Exception e)
//                {
//                    Log.e("test","don't open the db");
//                }
//            }
//        };
//
//        res = getApplicationContext().getResources();
//        deleteIcon = ResourcesCompat.getDrawable(res, R.drawable.dustbox, null);
//        Bitmap orgBitmap = ((BitmapDrawable) deleteIcon).getBitmap();
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(orgBitmap, 30, 30, false);
//        deleteIcon =  new BitmapDrawable(res, resizedBitmap);
//        swipeController.setDeleteIcon(deleteIcon);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//    }
//
//
//    // 「+」フローティング操作ボタン　タップ時に呼び出されるメソッド
//    public void fab_reg_onClick(View view) {
//
//        // テキスト登録画面 Activity へのインテントを作成
//        Intent intent  = new Intent(MainActivity.this, TextActivity.class);
//
//        // アクティビティを起動
//        startActivity(intent);
//    }
//
////    public void toUsage(View view)
////    {
////        Intent intent = new Intent(MainActivity.this, WebShow.class);
//
////        startActivity(intent);
////    }
//
//
//    public void tofootcountShow(View view){
//        Intent intent = new Intent(MainActivity.this, FootCountShow.class);
//        startActivity(intent);
//    }
//
//    public void toLevelShow(View view)
//    {
//        Intent intent = new Intent(MainActivity.this, LevelShow.class);
//        startActivity(intent);
//    }
//
//    public void toWebShow(View view)
//    {
//        Intent intent = new Intent(MainActivity.this, Help.class);
//        startActivity(intent);
//    }
}