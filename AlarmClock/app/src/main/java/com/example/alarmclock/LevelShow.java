package com.example.alarmclock;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

public class LevelShow extends Fragment {


    private TextView viewTitle;
    private TextView viewContents;
    private TextView level;
    private TextView percentText;
    private TabLayout tabLayout;
    private ImageButton imagebutton;
    private Button button1;
    private Button button2;
    private TextView contents   = null;
    private TextView aimContent = null;
    private TextView foot_step_number=null;

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
    private int val;


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

        foot_step_number=view.findViewById(R.id.needstep);


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

//                BottomSheetDialog sheetDialog = new BottomSheetDialog(LevelShow.this, R.style.BottomSheetStyle);
//                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomdialog,
//                (LinearLayout) findViewById(R.id.dialog_container),false);
//                sheetView.findViewById(R.id.file_menu_cancel).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sheetDialog.dismiss();
//                    }
//                });
//
//                sheetView.findViewById(R.id.file_menu_cancel).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sheetDialog.dismiss();
//                    }
//                });
//
//                sheetDialog.setContentView(sheetView);
//                sheetDialog.show();

//                sheetView.findViewById(R.id.file_menu_share).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sheetDialog.dismiss();
//                    }
//                });
//
//                sheetView.findViewById(R.id.file_menu_rename).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sheetDialog.dismiss();
//                    }
//                });
//
//                sheetView.findViewById(R.id.file_menu_delete).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sheetDialog.dismiss();
//                    }
//                });
//
//                sheetView.findViewById(R.id.file_menu_properties).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sheetDialog.dismiss();
//                    }
//                });
////
//
            }
        });





        level = view.findViewById(R.id.level);
        percentText = view.findViewById(R.id.percent);


//        tabLayout = view.findViewById(R.id.tab_layout);
//        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_baseline_access_alarm_24);
//        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_baseline_info_24);
//        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_baseline_insert_drive_file_24);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });




        foot_step_number.setText("不明");
        DatabaseHelper helper = new DatabaseHelper(getContext());
        try(SQLiteDatabase db = helper.getReadableDatabase()) {
//            初めに現在の経験値を取得
            String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.COLUMN_NAME_FOOT_COUNT, DBDef.DBEntry.COLUMN_SOUND_LEVEL_FORMER, DBDef.DBEntry.COLUMN_SOUND_LEVEL_LATTER};
            Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME2, cols, null,
                    null, null, null, null, null);
            if (cursor.moveToFirst())
            {
                sound_level_former = Integer.parseInt(cursor.getString(2));
                sound_level_latter= Integer.parseInt(cursor.getString(3));
                foot_step_number.setText(cursor.getString(1));
            }

        }catch (Exception e)
        {
            Log.e( "aaaaaa",e.toString());
        }


        sound_level_former=30;
        sound_level_latter=100;
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

//        switch (sound_level){
//
//            case 1:
//
//                foot_step_number.setText("100歩");
//
//                break;
//
//            case 2:
//
//                foot_step_number.setText("200歩");
//
//                break;
//
//            case 3:
//
//                foot_step_number.setText("300歩");
//
//                break;
//
//            case 4:
//
//                foot_step_number.setText("400歩");
//
//                break;
//
//            case 5:
//
//                foot_step_number.setText("500歩");
//        }







//
//        bar = (ProgressBar)findViewById(R.id.progressBar1);
//        bar.setMax(100);
//        bar.setMin(0);




//        bar.setProgress(percent,false);
//
//
//

//






//        if(sound_level_former/100==sound_level_latter/100)
//        {
//            onProgressAnimation(sound_level_latter%100);
//            objectAnimator.setDuration(20 * diff);
//            Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
//            diff=0;
//        }
//        else
//        {
//            onProgressAnimation(100);
//            int sa=((sound_level_former/100)+1)*100-sound_level_former;
//            objectAnimator.setDuration(20 * sa);
//            diff=diff-sa;
//            sound_level++;
//        }




//        objectAnimator.addListener(new Animator.AnimatorListener() {
//            // アニメーション開始で呼ばれる
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            Log.e("debug","onAnimationStart()");
//                        }
//
//                        // アニメーションがキャンセルされると呼ばれる
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//                            Log.e("debug","onAnimationCancel()");
//                        }
//
//                        // アニメーション終了時
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
////                            objectAnimator.start();
//                            Log.e("Process",String.valueOf(bar.getProgress()));
//                            if(diff!=0) {
//                                Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
//                                level.setText("LEVEL" + (sound_level + 1));
//                                if (diff>=100){
//                                    diff=diff-100;
//                                    sound_level++;
//                                    Log.e("calc",String.valueOf(20 * 100));
////                                    objectAnimator.setDuration(10000);
//                                    Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
//                                    objectAnimator.setIntValues(0,100);
//                                    objectAnimator.setDuration(100 * 20);
//                                    Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
//                                }else {
//                                    objectAnimator.setIntValues(0,diff);
//                                    objectAnimator.setDuration(diff * 20);
//                                    Log.e("ProcessLevel", String.valueOf(bar.getProgress()));
//                                    diff = 0;
//                                }
//                                Log.e("duration",String.valueOf(objectAnimator.getDuration()));
//                                objectAnimator.start();
//                            }else
//                            {
//                                Log.e("aaaaaaaa","終了");
//                            }
//                        }
//
//                        // 繰り返しでコールバックされる
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {}
//        });
//
//


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

    public float convertToAngle(int percent)
    {
        return  (percent % 100) * 360 / 100;
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

//        finish();
    }

    private void openDialog(){


//        SampDatabaseHelper helper = new SampDatabaseHelper(this);
//        // 入力欄に入力されたタイトルとコンテンツを取得
//        String cognomen = null;
//
//        // 書き込みモードでデータベースをオープン
//        try (SQLiteDatabase db = helper.getWritableDatabase()) {
//            // 入力されたタイトルとコンテンツをContentValuesに設定
//            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
//            ContentValues cv = new ContentValues();
//            cv.put(DBContract.DBEntry.COGNOMEN, cognomen);
//
//
//            //新規登録mode
//            if(id == 0) {
//                // データ新規登録
//                db.insert(DBContract.DBEntry.TABLE_NAME3, null, cv);
////                登録したデータのidを取得
//                String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COGNOMEN};
//                try {
//                    Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME3, cols, DBContract.DBEntry.COGNOMEN + " = ?", new String[]{cognomen}
//                            , null, null, null, null);
//                    if (cursor.moveToFirst()) {
//                        id = cursor.getInt(0);
//                    }
//                }catch (Exception e){
//                    Log.e("title",e.toString());
//                }
//
//            } else {
//                // データ更新
//                db.update(DBContract.DBEntry.TABLE_NAME3, cv, DBContract.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
//            }
//
//
//
//
//
//        }



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


//repeatではなくstartを使う






