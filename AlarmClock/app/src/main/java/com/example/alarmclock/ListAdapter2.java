package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ListAdapter2 extends RecyclerView.Adapter<ListAdapter2.ViewHolder>{


    private String[] loadDataSet;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;

        public ViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.title);
        }

        public TextView getTextView() {
            return textView;
        }


    }

    public  ListAdapter2(String[] dataSet){
        loadDataSet = dataSet;
    }

    @NonNull
    @Override
    public ListAdapter2.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main2, viewGroup, false);

//        final ViewHolder viewHolder = new ViewHolder(view);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = viewHolder.itemView.getContext();
//                int position = viewHolder.getBindingAdapterPosition();
////                Log.i("test", Integer.valueOf(position).toString());
//                Intent intent = new Intent(context, TextActivity.class);
//                intent.putExtra(DBContract.DBEntry._ID, position);
//                intent.putExtra(DBContract.DBEntry.COLUMN_NAME_TIME, loadDataSet.get(position));
//                context.startActivity(intent);
//            }
//        });


        return new ListAdapter2.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ListAdapter2.ViewHolder viewHolder, final int position){

        TextView text = viewHolder.getTextView();
        text.setText(loadDataSet[position] );
        text.setMovementMethod(new ScrollingMovementMethod());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ここにクリック処理を書く
                Context context = v.getContext();


                if (position == 0) {

                    Intent intent = new Intent(context, usage.class);

                    context.startActivity(intent);

                }else if(position==1){


                    Intent intent1=new Intent(context,WebShow.class);

                    context.startActivity(intent1);

                }else {


                    Intent intent2 = new Intent(context, question.class);

                    context.startActivity(intent2);
                }









            }
        });


                }

                @Override
                public int getItemCount () {
                    return loadDataSet.length;
                }

            }






