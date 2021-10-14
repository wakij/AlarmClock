package com.example.alarmclock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LevelShow extends AppCompatActivity {


    private TextView viewTitle;
    private TextView viewContents;
    private TextView level;


    private ProgressBar bar;
    private  int percent;
    private int sound_level_former=70;
    private int sound_level_latter=80;
    private int sound_level;
    private  int diff;


    private  ObjectAnimator objectAnimator;




    private SampDatabaseHelper helper = null;
    private int val;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelshow);
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        level=findViewById(R.id.level);




        SampDatabaseHelper helper = new SampDatabaseHelper(getApplicationContext());
        try(SQLiteDatabase db = helper.getReadableDatabase()) {
//            初めに現在の経験値を取得
            String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT, DBContract.DBEntry.COLUMN_SOUND_LEVEL_FORMER, DBContract.DBEntry.COLUMU_SOUND_LEVEL_LATTER};
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME2, cols, null,
                    null, null, null, null, null);
            if (cursor.moveToFirst())
            {
                sound_level_former = Integer.parseInt(cursor.getString(2));
                sound_level_latter= Integer.parseInt(cursor.getString(3));

            }

        }catch (Exception e)
        {
            Log.e( "aaaaaa",e.toString());
        }



        sound_level_former=30;
        sound_level_latter=250;

        bar = (ProgressBar)findViewById(R.id.progressBar1);
        bar.setMax(100);
        bar.setMin(0);
        percent=sound_level_former%100;



        bar.setProgress(percent,false);



        diff=sound_level_latter-sound_level_former;

        sound_level = sound_level_former/100;





        if(sound_level_former/100==sound_level_latter/100)
        {
            onProgressAnimation(sound_level_latter%100);
            objectAnimator.setDuration(20 * diff);
            Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
            diff=0;
        }
        else
        {
            onProgressAnimation(100);
            int sa=((sound_level_former/100)+1)*100-sound_level_former;
            objectAnimator.setDuration(20 * sa);
            diff=diff-sa;
            sound_level++;
        }




        objectAnimator.addListener(new Animator.AnimatorListener() {
            // アニメーション開始で呼ばれる
                        @Override
                        public void onAnimationStart(Animator animation) {
                            Log.e("debug","onAnimationStart()");
                        }

                        // アニメーションがキャンセルされると呼ばれる
                        @Override
                        public void onAnimationCancel(Animator animation) {
                            Log.e("debug","onAnimationCancel()");
                        }

                        // アニメーション終了時
                        @Override
                        public void onAnimationEnd(Animator animation) {
//                            objectAnimator.start();
                            Log.e("Process",String.valueOf(bar.getProgress()));
                            if(diff!=0) {
                                Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
                                level.setText("LEVEL" + (sound_level + 1));
                                if (diff>=100){
                                    diff=diff-100;
                                    sound_level++;
                                    Log.e("calc",String.valueOf(20 * 100));
//                                    objectAnimator.setDuration(10000);
                                    Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
                                    objectAnimator.setIntValues(0,100);
                                    objectAnimator.setDuration(100 * 20);
                                    Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
                                }else {
                                    objectAnimator.setIntValues(0,diff);
                                    objectAnimator.setDuration(diff * 20);
                                    Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
                                    diff = 0;
                                }
                                Log.e("duration",String.valueOf(objectAnimator.getDuration()));
                                objectAnimator.start();
                            }else
                            {
                                Log.e("aaaaaaaa","終了");
                            }
                        }

                        // 繰り返しでコールバックされる
                        @Override
                        public void onAnimationRepeat(Animator animation) {}
        });




    }


//    public void hogeButton(View v){
////        percent =50 ;
////        bar.setProgress(percent);
//        if (objectAnimator != null)
//        {
//            Log.e("aaaaaaaaaa","いないよ");
//        }
//
//
//    }


    private void onProgressAnimation(int percent){
        objectAnimator = ObjectAnimator.ofInt(bar,"progress",percent);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void backbtn(View view){
//        objectAnimator.resume();
//        objectAnimator.setDuration(10000);
//        if (objectAnimator != null)
//        {
//            if (objectAnimator.getListeners() != null)
//            {
//                Log.e("aaaaaaaaaaa","Aa");
//            }
//
//        }

////
//        Log.e("isRunning",String.valueOf(objectAnimator.isRunning()));
//        Log.e("isStarted",String.valueOf(objectAnimator.isStarted()));
//        Log.e("isPaused",String.valueOf(objectAnimator.isPaused()));

        finish();

     }

}


//repeatではなくstartを使う






