package com.example.alarmclock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FAQ extends Fragment {
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.faq, container, false);
    }

    //    Viewが完成した後に呼ばれる
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView bar_back_btn = getActivity().findViewById(R.id.bar_back_button);
        bar_back_btn.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.faq_recycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rLayoutManager);
        ArrayList<FAQ_single_row> list = new ArrayList<FAQ_single_row>();
        list.add(new FAQ_single_row("毎朝起きることができません","このアプリを使おう"));
        list.add(new FAQ_single_row("レビューしたいです","google storeでお願いします"));
        list.add(new FAQ_single_row("落単しそうです。助けてください","このアプリを使ってください"));
        list.add(new FAQ_single_row("応援したいです","お金ください"));
        faq_adapter adapter = new faq_adapter(list);
        recyclerView.setAdapter(adapter);
    }

}
