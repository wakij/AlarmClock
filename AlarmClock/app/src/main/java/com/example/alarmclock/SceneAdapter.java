package com.example.alarmclock;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SceneAdapter extends FragmentStateAdapter {
    private static final int PAGE_NUM = 3;

    public SceneAdapter(FragmentActivity fa)
    {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position)
    {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new AlarmListScene();
                break;
            case 1:
                fragment = new AlarmInfoScene();
                break;
            case 2:
                fragment = new HelperScene();
        }
        return fragment;

    }
    @Override
    public int getItemCount(){
        return PAGE_NUM;
    }


}
