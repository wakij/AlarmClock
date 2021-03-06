package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AlarmListScene extends Fragment{
    private DatabaseHelper helper;
    private RecyclerView recyclerView;
    private AlarmManager am;
    private AlarmListAdapter listAdapter;
    private ArrayList<AlarmInfo> alarmLists = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        return inflater.inflate(R.layout.alarm_list_scene, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);
        am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        makeAlarmList();
        swipeSetting();


        ConstraintLayout alarmlistScene = view.findViewById(R.id.alarmlistbg);
        InputStream inputStream;
        try {
            inputStream = requireActivity().openFileInput("backimg.png");
            Bitmap bit_backimg = BitmapFactory.decodeStream(inputStream);
            Resources resources = requireActivity().getResources();
            BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bit_backimg);
            alarmlistScene.setBackground(bitmapDrawable);
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        ImageView setting_btn = view.findViewById(R.id.setting);
        setting_btn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.settingsContainer, new HelpeScene());
            fragmentTransaction.commit();
        });
    }

    @Override
    public void onResume()
    {
        am = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        makeAlarmList();
        swipeSetting();
        super.onResume();
    }


    protected void makeAlarmList() {
        //?????????????????????????????????
        alarmLists.clear();
        // ???????????????????????????????????????
        helper = new DatabaseHelper(getContext());

        // ????????????????????????????????????????????????
        String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.COLUMN_NAME_TIME, DBDef.DBEntry.SWITCH_CONDITION, DBDef.DBEntry.MEMO, DBDef.DBEntry.CARD_COLOR};

        // ?????????????????????????????????????????????????????????
        try (SQLiteDatabase db = helper.getReadableDatabase()){

            // ???????????????????????????
            Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            //??????????????????????????????????????????????????????????????????????????????
//            ArrayList<String> timeList = new ArrayList<>();
            if (cursor.moveToFirst())
            {
                int id = cursor.getInt(0);
                String time = cursor.getString(1);
                String isSwitchOn = cursor.getString(2);
                String memo = cursor.getString(3);
                String card_color = cursor.getString(4);
                AlarmInfo alarmData = new AlarmInfo(id, time, isSwitchOn,memo, card_color);
                alarmLists.add(alarmData);
            }
            while(cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String time = cursor.getString(1);
                String isSwitchOn = cursor.getString(2);
                String memo = cursor.getString(3);
                String card_color = cursor.getString(4);
                AlarmInfo alarmData = new AlarmInfo(id, time, isSwitchOn,memo, card_color);
                alarmLists.add(alarmData);
            }
            cursor.close();
            listAdapter = new AlarmListAdapter(alarmLists);
            recyclerView.setAdapter(listAdapter);
        }
    }

//    ?????????????????????????????????
    private void swipeSetting()
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


}
