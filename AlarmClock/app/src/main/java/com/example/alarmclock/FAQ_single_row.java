package com.example.alarmclock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FAQ_single_row extends Fragment {
    private ImageView answer_back;
    private TextView answer_text;
    private TextView question_text;
    private ImageView minus_icon;
    private ImageView answer_icon;
    private String answer;
    private String question;

    public FAQ_single_row(String _question, String _answer)
    {
        answer = _answer;
        question = _question;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.faq_format, container, false);
    }

    //    Viewが完成した後に呼ばれる
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ImageView bar_back_btn = getActivity().findViewById(R.id.bar_back_button);
//        bar_back_btn.setVisibility(View.VISIBLE);

        question_text = view.findViewById(R.id.question_text);
        answer_back = view.findViewById(R.id.answer_back);
        answer_text = view.findViewById(R.id.answer_text);
        answer_icon = view.findViewById(R.id.answer_icon);
        minus_icon = view.findViewById(R.id.minus_icon);
        question_text.setText(question);
        answer_text.setText(answer);
        goneAnswer(); //answer部分を全て隠す

        ImageView plus_btn = view.findViewById(R.id.plus_icon);
        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleAnswer();
            }
        });

        ImageView minus_btn = view.findViewById(R.id.minus_icon);
        minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goneAnswer();
            }
        });
    }

    private void goneAnswer()
    {
        answer_back.setVisibility(View.GONE);
        answer_icon.setVisibility(View.GONE);
        answer_text.setVisibility(View.GONE);
        minus_icon.setVisibility(View.GONE);
    }

    private void visibleAnswer()
    {
        answer_back.setVisibility(View.VISIBLE);
        answer_icon.setVisibility(View.VISIBLE);
        answer_text.setVisibility(View.VISIBLE);
        minus_icon.setVisibility(View.VISIBLE);
    }

    public String getAnswer() {
        return answer;
    }
    public String getQuestion()
    {
        return question;
    }
}
