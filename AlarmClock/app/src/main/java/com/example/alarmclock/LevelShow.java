package com.example.alarmclock;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
    private int sound_level_former=0;
    private int sound_level_latter=1;





    private SampDatabaseHelper helper = null;
    private int val;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelshow);


        level=findViewById(R.id.level);



        SampDatabaseHelper helper = new SampDatabaseHelper(getApplicationContext());
        try(SQLiteDatabase db = helper.getReadableDatabase()) {
//            初めに現在の経験値を取得
            String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT, DBContract.DBEntry.COLUMN_SOUND_LEVEL_FORMER, DBContract.DBEntry.COLUMU_SOUND_LEVEL_LATTER};
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME2, cols, null,
                    null, null, null, null, null);
            if (cursor.moveToFirst())
            {
                sound_level_former = Integer.parseInt(cursor.getString(3));
                sound_level_latter= Integer.parseInt(cursor.getString(2));

            }

        }catch (Exception e)
        {
            Log.e( "aaaaaa",e.toString());
        }





        bar = (ProgressBar)findViewById(R.id.progressBar1);
        bar.setMax(100);
        bar.setMin(0);
        percent=sound_level_former%100;



        bar.setProgress(percent,true);

        onProgressAnimation(sound_level_latter%100);





        AlphaAnimation animation_alpha = new AlphaAnimation( 1, 0 );

        animation_alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // アニメーションの開始時に呼ばれます
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // アニメーションの繰り返し時に呼ばれます。
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // アニメーションの終了時に呼ばれます




            }



        });

    }


//    public void hogeButton(View v){
//        percent =50 ;
//        bar.setProgress(percent);

//
//    }


    private void onProgressAnimation(int percent){
        Animator animation = ObjectAnimator.ofInt(bar,"progress",percent);
        animation.setDuration(1000); // 0.5秒間でアニメーションする
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }




    public void backbtn(View view){

////


        finish();

     }

}







