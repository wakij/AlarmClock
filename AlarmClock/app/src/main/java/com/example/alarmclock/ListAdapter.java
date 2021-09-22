package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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

    //    private String[] loadDataSet;
    private ArrayList<String> loadDataSet;
    private SampDatabaseHelper helper = null;

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

    public ListAdapter(ArrayList<String> dataSet){
        loadDataSet = dataSet;
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
        String text = loadDataSet.get(position);
        textView.setText(text);
        textView.setMovementMethod(new ScrollingMovementMethod());

        //dbの用意
        Context context = viewHolder.itemView.getContext();
        helper = new SampDatabaseHelper(context);
        String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME, DBContract.DBEntry.SWITCH_CONDITION};

        Switch on_off = viewHolder.itemView.findViewById(R.id.on_off);

        //スイッチの状態を反映
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, DBContract.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{text}
                    , null, null, null, null);
            if (cursor.moveToFirst()) {
                //同じ設定時間が複数あった場合にうまく機能しない可能性あり
                boolean isSwitchOn = Boolean.valueOf(cursor.getString(2));
                on_off.setChecked(isSwitchOn);
            }
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (SQLiteDatabase db = helper.getReadableDatabase()) {
                    Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, DBContract.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{text}
                            , null, null, null, null);
                    if (cursor.moveToFirst()) {
                        //同じ設定時間が複数あった場合にうまく機能しない可能性あり
                        int id = cursor.getInt(0);
                        boolean isSwitchOn = Boolean.valueOf(cursor.getString(2));
                        Log.e("test", String.valueOf(id));
                        Intent intent = new Intent(context, TextActivity.class);
                        intent.putExtra(DBContract.DBEntry._ID, id);
                        intent.putExtra(DBContract.DBEntry.COLUMN_NAME_TIME, text);
                        context.startActivity(intent);
                    }
                }
            }
        });




        on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                AlarmManager am = TextActivity.am;
                AlarmManager am = MainActivity.am;
                try (SQLiteDatabase db = helper.getWritableDatabase()) {
                    Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, DBContract.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{text}
                            , null, null, null, null);
                    if (cursor.moveToFirst()) {
                        //同じ設定時間が複数あった場合にうまく機能しない可能性あり
                        int id = cursor.getInt(0);
                        Log.e("test", String.valueOf(id));
                        //明示的なintet alarmを有効化する時も無効化する時も使う
                        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                        //再登録
                        if (isChecked)
                        {
                            ContentValues cv = new ContentValues();
                            cv.put(DBContract.DBEntry.SWITCH_CONDITION, "true");
                            db.update(DBContract.DBEntry.TABLE_NAME, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
                            String[] hour_minutes = text.split(":");
                            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
                            // Calendarを使って現在の時間をミリ秒で取得
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour_minutes[0]));
                            calendar.set(Calendar.MINUTE, Integer.valueOf(hour_minutes[1]));
                            calendar.getTimeInMillis();
                            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Log.e("time",String.valueOf(calendar.getTimeInMillis()/1000));

                            // 現在時刻を取得
                            Calendar nowCalendar = Calendar.getInstance();
                            nowCalendar.setTimeInMillis(System.currentTimeMillis());

                            // 比較(確証はないので実際に機能するかは分かりませんが（笑）
                            int diff = calendar.compareTo(nowCalendar);

                            // 日付を設定
                            if(diff <= 0){
                                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                            }


                            intent.putExtra(DBContract.DBEntry._ID, id);

                            PendingIntent pending = PendingIntent.getBroadcast(
                                    context, id, intent, 0);

                            Log.e("test",String.valueOf(id));

                            // アラームをセットする
                            am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                            if(am != null){
//            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
                                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        1000 * 60 * 1, pending);
                                Toast.makeText(context,
                                        "Set Alarm ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //alarmの無効化
                        else
                        {
                            ContentValues cv = new ContentValues();
                            cv.put(DBContract.DBEntry.SWITCH_CONDITION, "false");
                            db.update(DBContract.DBEntry.TABLE_NAME, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
                            if (am != null)
                            {
                                PendingIntent pending = PendingIntent.getBroadcast(context, id, intent, 0);
                                am.cancel(pending);
                                Log.e("test","Alarmをキャンセルしました");
                            }
                        }
                    }
                }
            }
        });


    }
    @Override
    public int getItemCount(){
        return loadDataSet.size();
    }

    public void removeAt(int position){
        loadDataSet.remove(position);
        //削除を反映
        notifyItemRemoved(position);
    }

    public void addItem(String item){
        loadDataSet.add(item);
    }

}
