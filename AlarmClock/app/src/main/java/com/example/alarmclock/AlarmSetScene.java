package com.example.alarmclock;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import java.net.URI;

public class AlarmSetScene extends AppCompatActivity {

    private int id = 0;
    private Button setbtn = null;
    private Button backbtn = null;
    private spinner_timepicker timePicker = null;
    private EditText editContents;
    private ConstraintLayout mConstraintLayout;
    private InputMethodManager inputMethodManager;
    private String memo;
//    private TextView rest_time_text;
    private TextView comments;
    private String card_color;
    private Button pushed_btn_color;

    final int MUSIC_REQUEST = 1000;
    Uri audioUri;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set_scene);

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        comments = findViewById(R.id.comments);
        comments.setText("その調子!!!");



        timePicker = findViewById(R.id.spinner_time_picker);
        setbtn = findViewById(R.id.setbtn);
        backbtn = findViewById(R.id.backbtn);
        editContents=findViewById(R.id.editContents);
        mConstraintLayout = findViewById(R.id.constrain_layout);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);





        //MainActivity(登録されたアラーム一覧が乗っているactivity)からintentを取得
        Intent intent = getIntent();

       //intentのデータを取得(データがない場合、第２引数の 0 が返る)
        id = intent.getIntExtra(DBDef.DBEntry._ID, 0);

        //intentからidが取得できていればtiemPickerの表記を変更(編集mode)
        if(id>0){

            String time = intent.getStringExtra(DBDef.DBEntry.COLUMN_NAME_TIME);
            memo = intent.getStringExtra(DBDef.DBEntry.MEMO);
            String[] hour_minutes = time.split(":");
            timePicker.setHour(Integer.parseInt(String.valueOf(hour_minutes[0])));
            timePicker.setMinute(Integer.parseInt((String.valueOf((hour_minutes[1])))));
            editContents.setText(memo);
        }

        Button red_btn = findViewById(R.id.red);
        Button blue_btn = findViewById(R.id.blue);
        Button green_btn = findViewById(R.id.green);
        Button yellow_btn = findViewById(R.id.yellow);
        Button purple_btn = findViewById(R.id.purple);


        red_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_color =  "red";
                checkbuton(red_btn);
            }
        });

        blue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_color =  "blue";
                checkbuton(blue_btn);
            }
        });

        green_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_color =  "green";
                checkbuton(green_btn);
            }
        });

        yellow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_color =  "yellow";
                checkbuton(yellow_btn);
            }
        });

        purple_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_color =  "purple";
                checkbuton(purple_btn);
            }
        });

//        初期設定
        Drawable check_mark = ResourcesCompat.getDrawable(getResources(), R.drawable.check_mark, null);
        red_btn.setForeground(check_mark);
        card_color = "red";
        pushed_btn_color = red_btn;


        Button music_select = findViewById(R.id.music1);
        music_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickAudioIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickAudioIntent, MUSIC_REQUEST);
            }
        });




    }
    // 「設定」ボタン　タップ時に呼び出されるメソッド
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnReg_onClick(View view) {
        // ヘルパーを準備
        DatabaseHelper helper = new DatabaseHelper(this);
        // 入力欄に入力されたタイトルとコンテンツを取得
        String time = Integer.valueOf(timePicker.getHour()).toString()+":"+Integer.valueOf(timePicker.getMinute()).toString();
        String memo = editContents.getText().toString();
        // 書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            // 入力されたタイトルとコンテンツをContentValuesに設定
            // ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBDef.DBEntry.COLUMN_NAME_TIME, time);
            cv.put(DBDef.DBEntry.SWITCH_CONDITION, "true");
            cv.put(DBDef.DBEntry.MEMO,memo);
            cv.put(DBDef.DBEntry.CARD_COLOR, card_color);


            //新規登録mode
            if(id == 0) {
                // データ新規登録
                db.insert(DBDef.DBEntry.TABLE_NAME, null, cv);
//                登録したデータのidを取得
                String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.COLUMN_NAME_TIME, DBDef.DBEntry.SWITCH_CONDITION, DBDef.DBEntry.MEMO};
                try {
                    Cursor cursor = db.query(DBDef.DBEntry.TABLE_NAME, cols, DBDef.DBEntry.COLUMN_NAME_TIME + " = ?", new String[]{time}
                            , null, null, null, null);
                    if (cursor.moveToFirst()) {
                        id = cursor.getInt(0);
                    }
                }catch (Exception e){
                    Log.e("title",e.toString());
                }

            } else {
                // データ更新
                db.update(DBDef.DBEntry.TABLE_NAME, cv, DBDef.DBEntry._ID + " = ?", new String[] {String.valueOf(id)});
            }
        }
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmHelper.setAlarm(am, getApplicationContext(), timePicker.getHour(), timePicker.getMinute(), id, memo);
        finish();
    }

    // 「戻る」ボタン　タップ時に呼び出されるメソッド
    public void onBack(View view) {
        // TextActivityを終了
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(mConstraintLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        mConstraintLayout.requestFocus();
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkbuton(Button button)
    {
        if (pushed_btn_color != null)
        {
            pushed_btn_color.setForeground(null);
        }
        Drawable check_mark = ResourcesCompat.getDrawable(getResources(), R.drawable.check_mark, null);
        button.setForeground(check_mark);
        pushed_btn_color = button;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == MUSIC_REQUEST && requestCode == RESULT_OK)
        {
            audioUri = intent.getData();
            String audioUri_ = audioUri.toString();
            SharedPreferences sharedPreferences = getSharedPreferences("Info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("audioUri", audioUri_);
            editor.apply();
        }
    }
}



//メモ
//もう少し楽にデーターベースの検索を行いたい
//取得するのはidまたは時間なので簡単な関数を作れるはず
//あとコードが汚すぎる
//あとデータベースをシングルトン化して楽に呼び出せるようにしたい
