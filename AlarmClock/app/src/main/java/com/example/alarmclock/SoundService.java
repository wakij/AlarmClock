package com.example.alarmclock;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class SoundService extends Service implements MediaPlayer.OnCompletionListener{

    public static MediaPlayer mediaPlayer;
    private int repeat_time = 10; //繰り返される回数
    private int count = 0; //再生する回数
    public SoundService soundService;
    private NotificationManager notificationManager;
    private AudioManager audioManager;
    private int volume;
    private int defaultVolume = 5;
    private int SnoozeCount = 0;
    private int soundLevel = 1;
    private int step;

    public SoundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 参考 https://smartomaizu.com/ringtones/sozai/775.html
//        soundLevel = intent.getIntExtra("soundLevel", 0);
        count = 0; //カウントのリセット
        mediaPlayer = MediaPlayer.create(this, R.raw.app_src_main_res_raw_wakeup);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setLooping(false);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        volume = intent.getIntExtra("volume",5);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
        play();
        Log.e("music","play");
        return START_NOT_STICKY;
    }

    //stopselfで呼ばれる
    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        SharedPreferences sharedPreferences = getSharedPreferences("Info",MODE_PRIVATE);
        int needfootstep = sharedPreferences.getInt("needfootstep",0);
        int sound_level_latter = sharedPreferences.getInt("sound_level_latter",0);
        int sound_level_former = sound_level_latter; //値の更新

        if (count < 5)
        {
            sound_level_latter = (sound_level_latter - 20) > 0 ? sound_level_latter - 20 : 0;
        }
        else if (count < 9) {}
        else
        {
            sound_level_latter += (count - 8) * 5;
        }
        needfootstep += (int)((2 /  (1 + Math.exp(- sound_level_latter + sound_level_former)) - 1) * 100); //シグモイド曲線を少しいじったもので-1<x<1の間に圧縮
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("needfootstep",needfootstep);
        editor.putInt("sound_level_latter",sound_level_latter);
        editor.putInt("sound_level_former",sound_level_former);
        editor.apply();

//        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

//        try(SQLiteDatabase db = helper.getWritableDatabase()) {
//            String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.DATA};
//            Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME4, cols, null,
//                    null, null, null, null, null);
//            if (cursor.moveToFirst()) //データが存在していれば
//            {
//             step=Integer.parseInt(cursor.getString(1));
//            }
//        }

//        try(SQLiteDatabase db = helper.getWritableDatabase()) {
////            初めに現在の経験値を取得
//            String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.COLUMN_NAME_FOOT_COUNT, DBDef.DBEntry.COLUMN_SOUND_LEVEL_FORMER, DBDef.DBEntry.COLUMN_SOUND_LEVEL_LATTER};
//            Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME2, cols, null,
//                    null, null, null, null, null);
//            if (cursor.moveToFirst()) //データが存在していれば
//            {
//                int needfootstep = step;
//                int sound_level_latter = Integer.parseInt(cursor.getString(3));
//                int sound_level_former = sound_level_latter;
//                if (count < 5)
//                {
//                    sound_level_latter -= 20;
//                }
//                else if (count < 9) {}
//                else
//                {
//                    sound_level_latter += (count - 8) * 5;
//                }
//                sound_level_latter += count * 50; //ペナルティーを加える
//                needfootstep += (int)((2 /  (1 + Math.exp(- sound_level_latter + sound_level_former)) - 1) * 100); //シグモイド曲線を少しいじったもので-1<x<1の間に圧縮
//                ContentValues cv = new ContentValues();
//                cv.put(DBDef.DBEntry.COLUMN_SOUND_LEVEL_LATTER, String.valueOf(sound_level_latter));
//                cv.put(DBDef.DBEntry.COLUMN_SOUND_LEVEL_FORMER, String.valueOf(sound_level_former));
//                cv.put(DBDef.DBEntry.COLUMN_NAME_FOOT_COUNT, String.valueOf(needfootstep));
//                db.update(DBDef.DBEntry.TABLE_NAME2, cv, DBDef.DBEntry._ID + " = ?", new String[] {String.valueOf(1)});
//            }
//            else //データが存在していなければ
//            {
//                int sound_level_latter = count * 50;
//                ContentValues cv = new ContentValues();
//                cv.put(DBDef.DBEntry.COLUMN_SOUND_LEVEL_LATTER, String.valueOf(sound_level_latter));
//                db.insert(DBDef.DBEntry.TABLE_NAME2, null, cv);
//            }
//        }catch (Exception e)
//        {
//            Log.e( "aaaaaa",e.toString());
//        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 再生
    private void play() {
        mediaPlayer.start();
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 再生が終わる度に音量を上げてループ再生
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (repeat_time > count)
        {
            volume += 2;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
            play();
            count ++;
        }
        else
        {
            stop(); //再生を停止
            stopSelf();
        }
    }
    public void pauseMusic()
    {
        mediaPlayer.pause();
    }

    public static MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }

}
