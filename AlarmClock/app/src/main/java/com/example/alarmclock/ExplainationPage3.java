package com.example.alarmclock;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ExplainationPage3 extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.third, container, false);

        Button nextbtn= (Button) rootView.findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
        startActivity(intent);
    }

}
