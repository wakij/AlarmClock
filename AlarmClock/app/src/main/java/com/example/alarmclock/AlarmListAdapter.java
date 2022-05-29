package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private ArrayList<AlarmInfo> alarmLInfoList;
    private DatabaseHelper helper = null;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView time_display;

        public ViewHolder(View view){
            super(view);
            time_display = (TextView) view.findViewById(R.id.title);
        }

        public TextView getTextView() {
            return time_display;
        }
    }

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
        TextView time_display = viewHolder.getTextView();
        String time_text = alarmLInfoList.get(position).getTime();
        time_display.setText(time_text);
        String memo = alarmLInfoList.get(position).getMemo();


        //dbの用意
        Context context = viewHolder.itemView.getContext();
        helper = new DatabaseHelper(context);

        Switch on_off = viewHolder.itemView.findViewById(R.id.on_off);

        //スイッチの状態を反映
        boolean isSwitchOn = Boolean.parseBoolean(alarmLInfoList.get(position).getIsSwitchOn());
        on_off.setChecked(isSwitchOn);
        on_off.setSplitTrack(true);
        if (isSwitchOn)
        {
            onAlarm(viewHolder);
        }else
        {
            offAlarm(viewHolder);
        }


        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AlarmSetScene.class);
            intent.putExtra(DBDef.DBEntry._ID, alarmLInfoList.get(position).getId());
            intent.putExtra(DBDef.DBEntry.COLUMN_NAME_TIME, time_text);
            intent.putExtra(DBDef.DBEntry.MEMO,memo);
            context.startActivity(intent);
        });

        on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                        //同じ設定時間が複数あった場合にうまく機能しない可能性あり
                        int id = alarmLInfoList.get(position).getId();
                        //明示的なintet alarmを有効化する時も無効化する時も使う
                        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                        //再登録
                        if (isChecked)
                        {
                            ContentValues cv = new ContentValues();
                            cv.put(DBDef.DBEntry.SWITCH_CONDITION, "true");
                            db.update(DBDef.DBEntry.TABLE_NAME, cv, DBDef.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
                            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            AlarmInfo alarmData = alarmLInfoList.get(position);
                            AlarmHelper.setAlarm(am, context, alarmData.getHour(), alarmData.getMinutes(), alarmData.getId(), alarmData.getMemo());
                            onAlarm(viewHolder);
                        }
                        //alarmの無効化
                        else
                        {
                            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            ContentValues cv = new ContentValues();
                            cv.put(DBDef.DBEntry.SWITCH_CONDITION, "false");
                            db.update(DBDef.DBEntry.TABLE_NAME, cv, DBDef.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
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
        viewHolder.itemView.findViewById(R.id.title).setAlpha(0.5f);
        viewHolder.itemView.findViewById(R.id.on_off).setAlpha(0.5f);
    }

    public void onAlarm(ViewHolder viewHolder)
    {
        viewHolder.itemView.findViewById(R.id.monoBackground).setAlpha(1.0f);
        viewHolder.itemView.findViewById(R.id.title).setAlpha(1.0f);
        viewHolder.itemView.findViewById(R.id.on_off).setAlpha(1.0f);
    }

    public void removeAt(int position)
    {
        alarmLInfoList.remove(position);
        notifyItemRemoved(position);
    }

}
