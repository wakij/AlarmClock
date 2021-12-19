package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AlarmListScene extends Fragment implements LifecycleObserver {
    private DatabaseHelper helper;
    private RecyclerView recyclerView;
    private Resources res;
    private Drawable deleteIcon;
    private AlarmManager am;
    private AlarmListAdapter listAdapter;
    private ArrayList<AlarmInfo> alarmLists = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        Log.e("info","List再開");
        return (ViewGroup) inflater.inflate(R.layout.alarm_list_scene, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);
        //RecycleViewを枠線をいれる
//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(itemDecoration);
        am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        onShow();
        onswiped();
        ImageButton fab_btn = view.findViewById(R.id.fab_reg);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AlarmSetScene.class);
                getActivity().startActivity(intent);
            }
        });

        ConstraintLayout alarmlistScene = view.findViewById(R.id.alarmlistbg);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Info",Context.MODE_PRIVATE);
        String uriPath = sharedPreferences.getString("uriPath",null);
        if (uriPath != null)
        {
            ContentResolver cr = getActivity().getContentResolver();
            try {
                InputStream is = cr.openInputStream(Uri.parse(uriPath));
                Drawable alarmlistScenebgimg = Drawable.createFromStream(is, uriPath);
                alarmlistScene.setBackground(alarmlistScenebgimg);
                if (alarmlistScenebgimg == null)
                {
                    Log.e("nulllllllll","null");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onResume()
    {
        am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        onShow();
        onswiped();
        super.onResume();
    }


    protected void onShow() {
        //アラームリストを初期化
        alarmLists.clear();
        // データベースヘルパーを準備
        helper = new DatabaseHelper(getContext());

        // データベースを検索する項目を定義
        String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.COLUMN_NAME_TIME, DBDef.DBEntry.SWITCH_CONDITION, DBDef.DBEntry.MEMO};

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // データベースを検索
            Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            //データベースに格納されている全データを格納するリスト
//            ArrayList<String> timeList = new ArrayList<>();
            if (cursor.moveToFirst())
            {
                int id = cursor.getInt(0);
                String time = cursor.getString(1);
                String isSwitchOn = cursor.getString(2);
                String memo =cursor.getString(3);
                AlarmInfo alarmData = new AlarmInfo(id, time, isSwitchOn,memo);
                alarmLists.add(alarmData);
            }
            while(cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String time = cursor.getString(1);
                String isSwitchOn = cursor.getString(2);
                String memo = cursor.getString(3);
                AlarmInfo alarmData = new AlarmInfo(id, time, isSwitchOn,memo);
                alarmLists.add(alarmData);
            }
            cursor.close();
            listAdapter = new AlarmListAdapter(alarmLists);
            recyclerView.setAdapter(listAdapter);
        }
    }

    private void onswiped()
    {
        ItemSwipeController swipeController = new ItemSwipeController(0,ItemTouchHelper.LEFT){
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                listAdapter.removeAt(position);
                TextView textView = viewHolder.itemView.findViewById(R.id.title);
                String text = textView.getText().toString();
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.COLUMN_NAME_TIME, DBDef.DBEntry.SWITCH_CONDITION};
                    Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME, cols, DBDef.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{text}
                            , null, null, null, null);
                    if (cursor.moveToFirst())
                    {
                        int id = cursor.getInt(0);

                        if (am != null)
                        {
                            Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
                            Log.e("intent1",intent.toString());
                            Log.e("apple","id: " + String.valueOf(id));
                            PendingIntent pending = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            am.cancel(pending);
                        }
                    }
                    cursor.close();
                    db.delete(DBDef.DBEntry.TABLE_NAME, DBDef.DBEntry.COLUMN_NAME_TIME+" = ?", new String[] {text});
                } catch (Exception e)
                {
                    Log.e("test","don't open the db");
                }
            }
        };

//        res = getContext().getResources();
//        deleteIcon = ResourcesCompat.getDrawable(res, R.drawable.dustbox, null);
//        Bitmap orgBitmap = ((BitmapDrawable) deleteIcon).getBitmap();
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(orgBitmap, 30, 30, false);
//        deleteIcon =  new BitmapDrawable(res, resizedBitmap);
//        swipeController.setDeleteIcon(deleteIcon);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void switch_off(int id)
    {
        int pos = listAdapter.getNowPos(id);
        RecyclerView.ViewHolder viewHolder =  recyclerView.findViewHolderForAdapterPosition(pos);
        if (viewHolder != null)
        {
            Switch on_ff = (Switch)viewHolder.itemView.findViewById(R.id.on_off);
            on_ff.setChecked(false);
        }
    }
}
