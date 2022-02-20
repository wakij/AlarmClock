package com.example.alarmclock;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.text.MessageFormat;

public class SoundService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{

    private static final String ACTION_PLAY = "MusicPlay";
    public static MediaPlayer mediaPlayer;
    private int limited_repeat_time = 10; //繰り返される回数
    private int count = 0; //再生する回数
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

        count = 0; //カウントのリセット
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        volume = intent.getIntExtra("volume",5);
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);　//volumeの設定
//        mediaPlayerの準備を非同期で行って準備が完了したらplay()する
        if (intent.getAction().equals(ACTION_PLAY))
        {
            AudioAttributes.Builder builder = new AudioAttributes.Builder();
            builder.setUsage(AudioAttributes.USAGE_ALARM);
            AudioAttributes audioAttributes = builder.build();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(audioAttributes);
            SharedPreferences sharedPreferences = getSharedPreferences("Info",Context.MODE_PRIVATE);
            String audioUri_ = sharedPreferences.getString("audioUri",null);
//            再生する音楽が決められている場合
            if (audioUri_ != null)
            {
                try {
                    mediaPlayer.setDataSource(getApplicationContext(),Uri.parse(audioUri_));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            {
                String fileName = "android.resource://"
                        + this.getPackageName() + "/" + R.raw.app_src_main_res_raw_wakeup;
                try
                {
                    mediaPlayer.setDataSource(this, Uri.parse(fileName));
                }catch (IOException e)
                {
                    throw new IllegalStateException(
                            MessageFormat.format(
                                    "setDataSource error: msg={0}, value={1}",
                                    e.getMessage(), e.getStackTrace()));
                }
            }

            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(1.0f,1.0f);
            mediaPlayer.prepareAsync();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mp.start();
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
        if (limited_repeat_time > count)
        {
//            volume += 2;
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
            play();
            count ++;
        }
        else
        {
            stop(); //再生を停止
            Intent footstepserviceStop = new Intent(this,FootStepService.class);
            getApplication().stopService(footstepserviceStop);
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

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        return false;
    }
}
