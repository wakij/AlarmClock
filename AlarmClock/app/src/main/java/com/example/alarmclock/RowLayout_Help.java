package com.example.alarmclock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class RowLayout_Help extends Fragment {

    String mName;
    TextView mTextView;
    ImageView mIcon;
    ImageView mBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 先ほどのレイアウトをここでViewとして作成します
        return inflater.inflate(R.layout.row_layout_help, container, false);
    }

//    Viewが完成した後に呼ばれる
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mName = getTag();
        // TextViewをひも付けます
        mTextView = (TextView) view.findViewById(R.id.title);
        mBack = (ImageView) view.findViewById(R.id.back);
        mIcon = (ImageView) view.findViewById(R.id.icon);
        mIcon.setColorFilter(Color.parseColor("#FFFFFF"));
        if (mName != null)
        {
            mTextView.setText(mName);
        }else
        {
            mIcon.setImageDrawable(null);
            mTextView.setText("");
            mBack.setBackgroundColor(Color.parseColor("#EDEDED"));
        }
        mIcon.setColorFilter(Color.parseColor("#C9CCD5"));
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView bar_title = getActivity().findViewById(R.id.bar_title);
                FragmentManager fragmentManager = getParentFragment().getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (mName)
                {
                    case "ヘルプ":
                    case "お問い合わせ":
                        //メールsetting
                        Intent mailerIntent = new Intent();

                        mailerIntent.setAction(Intent.ACTION_SEND);
                        mailerIntent.setType("message/rfc822");
                        mailerIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"XXXX@gmail.com"});
                        mailerIntent.putExtra(Intent.EXTRA_SUBJECT,"myApp：お問い合わせ");
                        mailerIntent.putExtra(Intent.EXTRA_TEXT,
                                "\n\n\n\n"
                                        + "Device : " + Build.BRAND+" "+ Build.MODEL
                                        + "\nOS version : " + Build.VERSION.RELEASE
                                        + "\nApp :myApp"
                                        + "\nVersion : " + BuildConfig.VERSION_NAME);


                        startActivity(mailerIntent);
                    case "公式Twitter":
                        Log.e("title",mName);
                        break;
                    case "FAQ":
                        fragmentTransaction.replace(R.id.container, new FAQ());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        bar_title.setText(mName);
                        Log.e("title",mName);
                        break;
                    case "利用規約/その他":
                        fragmentTransaction.replace(R.id.container, new RuleInfo());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        bar_title.setText(mName);
                        Log.e("title",mName);
                        break;
                    case "バージョン情報":
                        fragmentTransaction.replace(R.id.container, new VersionInfo());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        bar_title.setText(mName);
                        Log.e("title",mName);
                        break;
                    case "設定":
//                        言語変更
//                        背景変更 部分ごとの方がいいかな？
                        fragmentTransaction.replace(R.id.container, new Setting());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        bar_title.setText(mName);
                        break;
                }
            }
        });
    }
//    公式Twitter FAQ お問い合わせ　利用規約その他　バージョン情報



}
