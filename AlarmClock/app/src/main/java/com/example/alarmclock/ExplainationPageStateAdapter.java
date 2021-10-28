package com.example.alarmclock;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ExplainationPageStateAdapter extends FragmentStateAdapter {
    private static final int PAGE_NUM = 3;

    public ExplainationPageStateAdapter(FragmentActivity fa){
        super(fa);
    }

    @Override
    public Fragment createFragment(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new ExplainationPage1();
                break;
            case 1:
                fragment = new ExplainationPage2();
                break;
            case 2:
                fragment = new ExplainationPage3();
        }
        return fragment;
    }

    @Override
    public int getItemCount(){
        return PAGE_NUM;
    }

}
