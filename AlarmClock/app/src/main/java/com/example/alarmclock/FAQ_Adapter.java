package com.example.alarmclock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FAQ_Adapter extends RecyclerView.Adapter<FAQ_Adapter.ViewHolder> {
    private ArrayList<RowLayout_FAQ> list;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView answer_text;
        private final TextView question_text;
        private final ImageView answer_back;
        private final ImageView minus_icon;
        private final ImageView answer_icon;
        private final ImageView plus_icon;

        public ViewHolder(View view){
            super(view);
            answer_text = view.findViewById(R.id.answer_text);
            question_text = view.findViewById(R.id.question_text);
            answer_back = view.findViewById(R.id.answer_back);
            answer_icon = view.findViewById(R.id.answer_icon);
            minus_icon = view.findViewById(R.id.minus_icon);
            plus_icon = view.findViewById(R.id.plus_icon);
        }
        public TextView getAnswerText()
        {
            return answer_text;
        }
        public TextView getQuestionText()
        {
            return question_text;
        }
        public ImageView getAnswer_back()
        {
            return answer_back;
        }
        public ImageView getMinus_icon()
        {
            return minus_icon;
        }

        public ImageView getAnswer_icon()
        {
            return answer_icon;
        }
        public ImageView getPlus_icon()
        {
            return plus_icon;
        }

        public void goneAnswer()
        {
            answer_back.setVisibility(View.GONE);
            answer_icon.setVisibility(View.GONE);
            answer_text.setVisibility(View.GONE);
            minus_icon.setVisibility(View.GONE);
        }

        public void visibleAnswer()
        {
            answer_back.setVisibility(View.VISIBLE);
            answer_icon.setVisibility(View.VISIBLE);
            answer_text.setVisibility(View.VISIBLE);
            minus_icon.setVisibility(View.VISIBLE);
        }
    }

    public FAQ_Adapter(ArrayList<RowLayout_FAQ> dataSet){
        list = dataSet;
    }

    @NonNull
    @Override
    public FAQ_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_faq, viewGroup, false);
        return new FAQ_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  FAQ_Adapter.ViewHolder holder, int position) {

        TextView answer_text = holder.getAnswerText();
        TextView question_text = holder.getQuestionText();
//        ImageView answer_back = holder.getAnswer_back();
        ImageView minus_icon = holder.getMinus_icon();
//        ImageView answer_icon = holder.getAnswer_icon();
        ImageView plus_icon = holder.getPlus_icon();

        answer_text.setText(list.get(position).getAnswer());
        question_text.setText(list.get(position).getQuestion());
        holder.goneAnswer(); //answer???????????????

        plus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.visibleAnswer();
            }
        });

        minus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.goneAnswer();
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


}
