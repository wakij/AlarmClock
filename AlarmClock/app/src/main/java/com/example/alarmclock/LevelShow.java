package com.example.alarmclock;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelShow extends AppCompatActivity {


    private TextView viewTitle;
    private TextView viewContents;

    private SampDatabaseHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelshow);


        viewTitle    = findViewById(R.id.textView4);



         //ヘルパーを準備
        helper = new SampDatabaseHelper(this);

         //データを表示
        onShow();

    }


     //データを表示する
    protected void onShow() {

       //  データベースから取得する項目を設定
        String[] cols = {DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT, DBContract.DBEntry.COLUMN_SOUND_LEVEL};

         //読み込みモードでデータベースをオープン
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

     //保存処理
    public void onSave(View view) {

     //    入力欄に入力されたタイトルとコンテンツを取得
        String foot_count    = "100歩";

       //  書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

         //    入力されたタイトルとコンテンツをContentValuesに設定
           //  ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT,foot_count);

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
        }

         //データを表示
        onShow();

    }





    public void onSave2(View view) {

         //入力欄に入力されたタイトルとコンテンツを取得
        String foot_count    = "300歩";

         //書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

           //  入力されたタイトルとコンテンツをContentValuesに設定
             //ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT,foot_count);

          //   現在テーブルに登録されているデータの_IDを取得
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME2,  new String[] {DBContract.DBEntry._ID}, null, null,
                    null, null, null, null);

            // テーブルにデータが登録されていれば更新処理
            if (cursor.moveToFirst()){

//                 取得した_IDをparamsに設定
                String[] params = {cursor.getString(0)};

//                 _IDのデータを更新
                db.update(DBContract.DBEntry.TABLE_NAME2, cv, DBContract.DBEntry._ID + " = ?", params);

            } else {

//                 データがなければ新規登録
                db.insert(DBContract.DBEntry.TABLE_NAME2, null, cv);
            }
        }

//         データを表示
        onShow();

    }

    public void onSave3(View view) {

//         入力欄に入力されたタイトルとコンテンツを取得
        String foot_count    = "500歩";

//         書き込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

//             入力されたタイトルとコンテンツをContentValuesに設定
//             ContentValuesは、項目名と値をセットで保存できるオブジェクト
            ContentValues cv = new ContentValues();
            cv.put(DBContract.DBEntry.COLUMN_NAME_FOOT_COUNT,foot_count);

//             現在テーブルに登録されているデータの_IDを取得
            Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME2,  new String[] {DBContract.DBEntry._ID}, null, null,
                    null, null, null, null);

//             テーブルにデータが登録されていれば更新処理
            if (cursor.moveToFirst()){

//                 取得した_IDをparamsに設定
                String[] params = {cursor.getString(0)};

//                 _IDのデータを更新
                db.update(DBContract.DBEntry.TABLE_NAME2, cv, DBContract.DBEntry._ID + " = ?", params);

            } else {

//                 データがなければ新規登録
                db.insert(DBContract.DBEntry.TABLE_NAME2, null, cv);
            }
        }

//         データを表示
        onShow();

    }






//     削除処理
    public void onDelete(View view){

        try (SQLiteDatabase db = helper.getWritableDatabase()) {
            db.delete(DBContract.DBEntry.TABLE_NAME2, null, null);
        }

//         データを表示
        onShow();
   }

}







