package com.example.alarmclock;

import android.app.Service;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    //    private String[] loadDataSet;
    private ArrayList<String> loadDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;

        public ViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.text_view);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ListAdapter(ArrayList<String> dataSet){
        loadDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_text_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position){

        TextView text = viewHolder.getTextView();
        text.setText(loadDataSet.get(position));
        text.setMovementMethod(new ScrollingMovementMethod());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ここにクリック処理を書く

                Log.i("test", String.valueOf(position));
            }
        });

    }
    @Override
    public int getItemCount(){
        return loadDataSet.size();
    }

    public void removeAt(int position){
        loadDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(String item){
        loadDataSet.add(item);
    }

}
