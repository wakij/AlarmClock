package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<AlarmInfo> alarmLInfoList;
    private SampDatabaseHelper helper = null;
    private ViewHolder viewHolder;

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
    public ListAdapter(ArrayList<AlarmInfo> dataSet){
        alarmLInfoList = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main, viewGroup, false);
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

        String memo=alarmLInfoList.get(position).getMemo();

        //dbの用意
        Context context = viewHolder.itemView.getContext();
        helper = new SampDatabaseHelper(context);
//        String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION};

        Switch on_off = viewHolder.itemView.findViewById(R.id.on_off);
        ImageView monoBackground = viewHolder.itemView.findViewById(R.id.monoBackground);

        //スイッチの状態を反映
        boolean isSwitchOn = Boolean.valueOf(alarmLInfoList.get(position).getIsSwitchOn());
        on_off.setChecked(isSwitchOn);
        on_off.setSplitTrack(true);



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TextActivity.class);
                intent.putExtra(DBContract.DBEntry._ID, alarmLInfoList.get(position).getId());
                intent.putExtra(DBContract.DBEntry.COLUMN_NAME_TIME, text);
                intent.putExtra(DBContract.DBEntry.MEMO,memo);
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
                            cv.put(DBContract.DBEntry.SWITCH_CONDITION, "true");
                            db.update(DBContract.DBEntry.TABLE_NAME, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
                            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            AlarmInfo alarmData = alarmLInfoList.get(position);
                            AlarmHelper.setAlarm(am, context, alarmData.getHour(), alarmData.getMinutes(), alarmData.getId(), alarmData.getMemo());


                            TextView textView = viewHolder.getTextView();
                            textView.setTextColor(0xffff0000);
                            monoBackground.setColorFilter(Color.parseColor("#FFFFFF"));

                        }
                        //alarmの無効化
                        else
                        {
                            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            ContentValues cv = new ContentValues();
                            cv.put(DBContract.DBEntry.SWITCH_CONDITION, "false");
                            db.update(DBContract.DBEntry.TABLE_NAME, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
                            TextView textView = viewHolder.getTextView();
                            textView.setTextColor(0xff23ce34);
                            monoBackground.setColorFilter(Color.parseColor("#D3D3CF"));

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

    public void removeAt(int position)
    {
        alarmLInfoList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(AlarmInfo alarmData)
    {
        alarmLInfoList.add(alarmData);
    }


}
