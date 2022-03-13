package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private ArrayList<AlarmInfo> alarmLInfoList;
    private DatabaseHelper helper = null;
    private ViewHolder viewHolder;
    private ArrayList<Switch>switchArrayList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;

        public ViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.title);
        }

        public TextView getTextView() {
            return textView;
        }
    }

//    public ListAdapter(ArrayList<String> dataSet){
//        loadDataSet = dataSet;
//    }
    public AlarmListAdapter(ArrayList<AlarmInfo> dataSet){
        alarmLInfoList = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_alam, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position){

        //テキストの設定
        TextView textView = viewHolder.getTextView();
//        String text = loadDataSet.get(position);
        String text = alarmLInfoList.get(position).getTime();
        textView.setText(text);
        textView.setMovementMethod(new ScrollingMovementMethod());

        String memo = alarmLInfoList.get(position).getMemo();

        ImageView monoBackground = viewHolder.itemView.findViewById(R.id.monoBackground);

        String card_color = alarmLInfoList.get(position).getCard_color();
//        switch (card_color)
//        {
//            case "red":
//                monoBackground.setColorFilter(Color.parseColor("#A11414"));
//                break;
//            case "blue":
//                monoBackground.setColorFilter(Color.parseColor("#15619E"));
//                break;
//            case "green":
//                monoBackground.setColorFilter(Color.parseColor("#0D7E73"));
//                break;
//            case "yellow":
//                monoBackground.setColorFilter(Color.parseColor("#91840F"));
//                break;
//            case "purple":
//                monoBackground.setColorFilter(Color.parseColor("#640B73"));
//                break;
//        }

        //dbの用意
        Context context = viewHolder.itemView.getContext();
        helper = new DatabaseHelper(context);
//        String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION};

        Switch on_off = viewHolder.itemView.findViewById(R.id.on_off);

        //スイッチの状態を反映
        boolean isSwitchOn = Boolean.valueOf(alarmLInfoList.get(position).getIsSwitchOn());
        on_off.setChecked(isSwitchOn);
        on_off.setSplitTrack(true);
        if (isSwitchOn)
        {
            onAlarm(viewHolder);
        }else
        {
            offAlarm(viewHolder);
        }



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlarmSetScene.class);
                intent.putExtra(DBDef.DBEntry._ID, alarmLInfoList.get(position).getId());
                intent.putExtra(DBDef.DBEntry.COLUMN_NAME_TIME, text);
                intent.putExtra(DBDef.DBEntry.MEMO,memo);
                context.startActivity(intent);
            }
        });

        on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                        //同じ設定時間が複数あった場合にうまく機能しない可能性あり
                        int id = alarmLInfoList.get(position).getId();
                        Log.e("test", String.valueOf(id));
                        //明示的なintet alarmを有効化する時も無効化する時も使う
                        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                        //再登録
                        if (isChecked)
                        {
//                            on_off.getThumbDrawable().setColorFilter(Color.parseColor("#1F1D36"), PorterDuff.Mode.MULTIPLY);
                            ContentValues cv = new ContentValues();
                            cv.put(DBDef.DBEntry.SWITCH_CONDITION, "true");
                            db.update(DBDef.DBEntry.TABLE_NAME, cv, DBDef.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
                            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            AlarmInfo alarmData = alarmLInfoList.get(position);
                            AlarmHelper.setAlarm(am, context, alarmData.getHour(), alarmData.getMinutes(), alarmData.getId(), alarmData.getMemo());
//                            TextView textView = viewHolder.getTextView();
//                            textView.setTextColor(Color.parseColor("#191919"));
//                            monoBackground.setColorFilter(Color.parseColor("#FFFFFF"));
                            onAlarm(viewHolder);
                        }
                        //alarmの無効化
                        else
                        {
                            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            ContentValues cv = new ContentValues();
                            cv.put(DBDef.DBEntry.SWITCH_CONDITION, "false");
                            db.update(DBDef.DBEntry.TABLE_NAME, cv, DBDef.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
//                            TextView textView = viewHolder.getTextView();
//                            textView.setTextColor(Color.parseColor("#191919"));
//                            monoBackground.setColorFilter(Color.parseColor("#FFFFFF"));
                            offAlarm(viewHolder);
                            if (am != null)
                            {
                                PendingIntent pending = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                am.cancel(pending);
                            }
                        }
                    }
                }
        });




    }
    @Override
    public int getItemCount()
    {
        return alarmLInfoList.size();
    }

    public void offAlarm(ViewHolder viewHolder)
    {
        viewHolder.itemView.findViewById(R.id.monoBackground).setAlpha(0.5f);
//        viewHolder.itemView.findViewById(R.id.commentparts).setAlpha(0.5f);
        viewHolder.itemView.findViewById(R.id.title).setAlpha(0.5f);
        viewHolder.itemView.findViewById(R.id.on_off).setAlpha(0.5f);
        viewHolder.itemView.findViewById(R.id.comments).setAlpha(0.5f);
    }

    public void onAlarm(ViewHolder viewHolder)
    {
        viewHolder.itemView.findViewById(R.id.monoBackground).setAlpha(1.0f);
//        viewHolder.itemView.findViewById(R.id.commentparts).setAlpha(1.0f);
        viewHolder.itemView.findViewById(R.id.title).setAlpha(1.0f);
        viewHolder.itemView.findViewById(R.id.on_off).setAlpha(1.0f);
//        viewHolder.itemView.findViewById(R.id.comments).setAlpha(1.0f);
    }

    public void removeAt(int position)
    {
        alarmLInfoList.remove(position);
        notifyItemRemoved(position);
    }

//    idからリサイクルビューの一番号を算出
    public int getNowPos(int id)
    {
        int pos = 0;
        for(int i = 0; i < alarmLInfoList.size(); i++)
        {
            if (alarmLInfoList.get(i).getId() == id)
            {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public void addItem(AlarmInfo alarmData)
    {
        alarmLInfoList.add(alarmData);
    }


}
