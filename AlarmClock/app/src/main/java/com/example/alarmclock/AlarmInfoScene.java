package com.example.alarmclock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AlarmInfoScene extends Fragment {

    private TextView viewTitle;
    private TextView viewContents;
    private Arc arc;
    private int endAngle = 0;
    private int animationPeriod = 2000;
    private SampDatabaseHelper helper = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.levelshow, container, false);
        return rootView;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        // 88%に角度を合わせる
        endAngle = 100*360/100;

        arc = getActivity().findViewById(R.id.arc);

//        Button buttonStart = getActivity().findViewById(R.id.button_start);
//        buttonStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AnimationArc animation = new AnimationArc(arc, endAngle);
//                // アニメーションの起動期間を設定
//                animation.setDuration(animationPeriod);
//                animation.setRepeatCount(Animation.INFINITE);
//                arc.startAnimation(animation);
//
//            }
//        });

//        Button buttonReset = getActivity().findViewById(R.id.button_reset);
//        buttonReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AnimationArc animation = new AnimationArc(arc, 0);
//                animation.setDuration(0);
//                arc.startAnimation(animation);
//
//            }
//        });

        viewTitle = getActivity().findViewById(R.id.textView4);

        //ヘルパーを準備
        helper = new SampDatabaseHelper(getContext());

        //データを表示
//        onShow();
        viewTitle.setText("データがありません");

        Button btn2 = getActivity().findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData("100");
            }
        });

        Button btn3 = getActivity().findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData("300");
            }
        });

        Button btn4 = getActivity().findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData("500");
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    public void saveData(String foot_count)
    {
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

            //    入力されたタイトルとコンテンツをContentValuesに設定
            //  ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT, foot_count);

            //現在テーブルに登録されているデータの_IDを取得
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME2,  new String[] {DBContract.DBEntry._ID}, null, null,
                    null, null, null, null);

            //テーブルにデータが登録されていれば更新処理
            if (cursor.moveToFirst()){

                //  取得した_IDをparamsに設定
                String[] params = {cursor.getString(0)};

                //_IDのデータを更新
                db.update(DBContract.DBEntry.TABLE_NAME2, cv, DBContract.DBEntry._ID + " = ?", params);

            } else {
                //データがなければ新規登録
                db.insert(DBContract.DBEntry.TABLE_NAME2, null, cv);
            }

            viewTitle.setText(foot_count);

        }
    }

    protected void onShow() {

        //  データベースから取得する項目を設定
        String[] cols = {DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT, DBContract.DBEntry.COLUMN_SOUND_LEVEL_FORMER,DBContract.DBEntry.COLUMU_SOUND_LEVEL_LATTER};

//         読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()) {

            //  データを取得するSQLを実行
            //取得したデータがCursorオブジェクトに格納される
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME2, cols, null,
                    null, null, null, null, null);

            //moveToFirstで、カーソルを検索結果セットの先頭行に移動
            //検索結果が0件の場合、falseが返る
            if (cursor.moveToFirst()){

                //表示用のテキスト・コンテンツに検索結果を設定
                viewTitle.setText(cursor.getString(0));



            } else {
                //  検索結果が0件の場合のメッセージを設定
                viewTitle.setText("データがありません");


            }
        }

    }
}
