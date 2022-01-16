package com.example.alarmclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Personal_Info extends Fragment {

    int soundlevel;
    int level;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.personal_info, container, false);
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("sound_level_latter",0) == 0)
        {
            soundlevel = sharedPreferences.getInt("sound_level_former",0);
        }else
        {
            soundlevel = sharedPreferences.getInt("sound_level_latter",0);
        }
        level = soundlevel / 100 + 1;

        TextView level_text = view.findViewById(R.id.level_text);
        level_text.setText("Level" + level);

        TextView needstep = view.findViewById(R.id.needstep_text);
        needstep.setText(sharedPreferences.getInt("needfootstep", 0) + " 歩");

        ImageView level_icon = view.findViewById(R.id.level_icon);

        switch (level)
        {
            case 1:
                level_icon.setImageResource(R.drawable.alarmimg);
                break;
            case 2:
                level_icon.setImageResource(R.drawable.alarmimg);
                break;
            case 3:
                level_icon.setImageResource(R.drawable.dustbox);
                break;
            case 4:
                level_icon.setImageResource(R.drawable.ic_baseline_account_circle_24);
                break;
            case  5:
                level_icon.setImageResource(R.drawable.ic_baseline_unfold_more_24);
        }
    }
}
