package com.example.alarmclock;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.io.FileDescriptor;

public class LevelShow extends Fragment {


    private TextView viewTitle;
    private TextView viewContents;
    private TextView level;
    private TextView percentText;
    private TabLayout tabLayout;
    private ImageButton imagebutton;
    private Button button1;
    private Button button2;
    private TextView contents;
    private TextView aimContent;
    private TextView foot_step_number;

    private int id=0;

    private Dialog dialog;
    public static CognomenListAdapter adapter;
    private RecyclerView recyclerView;

    private ProgressBar bar;
    private  int percent;
    private int sound_level_former=70;
    private int sound_level_latter=80;
    private int sound_level;
    private  int diff;
    private float endAngle = 0.0f; //何度回転させるか
    private PieChart arc;
    private int animationPeriod = 2000;
    private float initAngle; //どこから回転させるか
    private PieChartAnim animationArc;


    private  ObjectAnimator objectAnimator;

    private DatabaseHelper helper = null;
    private DatabaseHelper helper2=null;
    public static final int RESULT_PICK_IMAGEFILE = 1001;
    private ImageView cognomenimg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.levelshow, container, false);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View decor = getWindow().getDecorView();
//        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        foot_step_number=view.findViewById(R.id.needfootstep);
        aimContent = view.findViewById(R.id.goal);
//         ヘルパーを準備
        helper2 = new DatabaseHelper(getContext());

        // データを表示
        onShow2();


        imagebutton = view.findViewById(R.id.imageButton);
        dialog = new Dialog(getContext());
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        level = view.findViewById(R.id.level);
        percentText = view.findViewById(R.id.percent);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
        sound_level_former = sharedPreferences.getInt("sound_level_former",0);
        sound_level_latter = sharedPreferences.getInt("sound_level_latter",0);
        foot_step_number.setText(String.valueOf(sharedPreferences.getInt("needfootstep",0)) + "歩");

        percent=sound_level_former%100;
        diff=sound_level_latter-sound_level_former;
        sound_level = sound_level_former/100 + 1;


        if(sound_level_former/100==sound_level_latter/100)
        {
            initAngle = convertToAngle(sound_level_former);
            endAngle = convertToAngle(sound_level_latter) - initAngle;
            diff=0;
        }
        else
        {
            initAngle = convertToAngle(sound_level_former);
            endAngle = 360 - initAngle;
            int sa=((sound_level_former/100)+1)*100-sound_level_former;
            diff=diff-sa;
            sound_level++;
        }

        boolean endFlag = false; //アニメーションを終了するかどうか
        arc = view.findViewById(R.id.arc);
        arc.setconstEndAngle(initAngle);
        animationArc = new PieChartAnim(arc, endAngle, initAngle);
        animationArc.setDuration(animationPeriod);
        arc.startAnimation(animationArc);
        animationArc.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                percentText.setText("-%");
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                level.setText("LEVEL" + sound_level);
                if(diff!=0) {
                    arc.setconstEndAngle(0); //前回からの引き継ぎ分を見えないようにする
                    if (diff>=100){
                        diff=diff-100;
                        sound_level++;
                        animationArc.setInitAngle(0);
                        animationArc.setEndAngle(360.0f);
                    }else {
                        animationArc.setInitAngle(0);
                        animationArc.setEndAngle(diff * 360 / 100);
                        diff = 0;
                    }
                    arc.startAnimation(animationArc);
                }
                else if ((int)(arc.getAngle() * 180 / Math.PI) == 360 && diff == 0)
                {
                    arc.setconstEndAngle(0); //前回からの引き継ぎ分を見えないようにする
                    animationArc.setInitAngle(0);
                    animationArc.setEndAngle(0);
                    animationArc.setDuration(1); //一瞬でアニメーションが終わるようにする
                    arc.startAnimation(animationArc);
                }
                else
                {
                    percentText.setText(String.valueOf(sound_level_latter%100) + "%");
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void onProgressAnimation(int percent){
        objectAnimator = ObjectAnimator.ofInt(bar,"progress",percent);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    public float convertToAngle(int percent)
    {
        return  (percent % 100) * 360 / 100;
    }

//    ギャラリーから写真を選んで表示
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData)
    {
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK)
        {
            if (resultData.getData() != null)
            {
                ParcelFileDescriptor pfDescriptor = null;
                try {
                    Uri uri = resultData.getData();
                    pfDescriptor = getActivity().getContentResolver().openFileDescriptor(uri, "r");
                    if (pfDescriptor != null)
                    {
                        FileDescriptor fileDescriptor = pfDescriptor.getFileDescriptor();
                        Bitmap bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        pfDescriptor.close();
                        cognomenimg.setImageBitmap(bmp);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }finally {
                    try {
                        if (pfDescriptor != null)
                        {
                            pfDescriptor.close();
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openDialog(){
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Info",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
        dialog.setContentView(R.layout.layout_cognomen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        View decor = dialog.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        recyclerView=dialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


        String[] List={"greed","lazy"};
        adapter = new CognomenListAdapter(List);
        recyclerView.setAdapter(adapter);

        dialog.show();

        Button button1= dialog.findViewById(R.id.button7);
        Button button2= dialog.findViewById(R.id.button8);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        cognomenimg = dialog.findViewById(R.id.cognomenimg);
        cognomenimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });







    }

    protected void onShow2() {

        // データベースから取得する項目を設定
        String[] cols = {DBDef.DBEntry.MEMO};

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper2.getReadableDatabase()) {

            // データを取得するSQLを実行
            // 取得したデータがCursorオブジェクトに格納される
            Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME, cols, null,
                    null, null, null, null, null);

            // moveToFirstで、カーソルを検索結果セットの先頭行に移動
            // 検索結果が0件の場合、falseが返る
            if (cursor.moveToFirst()){

                // 表示用のテキスト・コンテンツに検索結果を設定

                aimContent.setText(cursor.getString(0));


            } else {
                // 検索結果が0件の場合のメッセージを設定

                aimContent.setText("目標が設定されていません！");


            }
        }

    }
}

//    public void backbtn(View view){
////        objectAnimator.resume();
////        objectAnimator.setDuration(10000);
////        if (objectAnimator != null)
////        {
////            if (objectAnimator.getListeners() != null)
////            {
////                Log.e("aaaaaaaaaaa","Aa");
////            }
////
////        }
//
//////
////        Log.e("isRunning",String.valueOf(objectAnimator.isRunning()));
////        Log.e("isStarted",String.valueOf(objectAnimator.isStarted()));
////        Log.e("isPaused",String.valueOf(objectAnimator.isPaused()));
//
////        finish();
//    }


//repeatではなくstartを使う






