package com.example.alarmclock;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HelpeScene extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.help, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new RowsLayout_Help());
        fragmentTransaction.commit();

        ImageView bar_back_btn = view.findViewById(R.id.bar_back_button);
        bar_back_btn.setColorFilter(Color.parseColor("#FFFFFF"));
        bar_back_btn.setOnClickListener(v -> {
            TextView bar_title = view.findViewById(R.id.bar_title);
            bar_title.setText("ヘルプ");
            bar_back_btn.setVisibility(View.INVISIBLE);
            fragmentManager.popBackStack();
        });
    }
}
