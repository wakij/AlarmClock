package com.example.alarmclock;

import android.content.Context;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class CognomenListAdapter extends RecyclerView.Adapter<CognomenListAdapter.ViewHolder>{


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

    public CognomenListAdapter(String[] list){
        loadDataSet = list;
    }

    @NonNull
    @Override
    public CognomenListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout_cognomen, viewGroup, false);

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


        return new CognomenListAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(CognomenListAdapter.ViewHolder viewHolder, final int position){

        TextView text = viewHolder.getTextView();
        text.setText(loadDataSet[position] );
        text.setMovementMethod(new ScrollingMovementMethod());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ここにクリック処理を書く
                Context context = v.getContext();










            }
        });


                }

                @Override
                public int getItemCount () {
                    return loadDataSet.length;
                }

            }






