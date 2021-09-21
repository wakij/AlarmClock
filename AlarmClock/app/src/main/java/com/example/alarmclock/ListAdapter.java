package com.example.alarmclock;

import android.app.Service;
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
    private SampDatabaseHelper helper = null;

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

    public ListAdapter(ArrayList<String> dataSet){
        loadDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main, viewGroup, false);

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
                Context context = v.getContext();
                helper = new SampDatabaseHelper(context);
                int id = 0;
                String[] cols = {DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TIME};
                try (SQLiteDatabase db = helper.getReadableDatabase()){
                    Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME, cols, DBContract.DBEntry.COLUMN_NAME_TIME + " = ?", new String[] {loadDataSet.get(position)}
                            , null, null, null, null);
                    if (cursor.moveToFirst())
                    {
                        id = cursor.getInt(0);
                        Log.e("test",String.valueOf(id));
                    }
                }
                Intent intent = new Intent(context, TextActivity.class);
                intent.putExtra(DBContract.DBEntry._ID, id);
                intent.putExtra(DBContract.DBEntry.COLUMN_NAME_TIME, loadDataSet.get(position));
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount(){
        return loadDataSet.size();
    }

    public void removeAt(int position){
        loadDataSet.remove(position);
        //削除を反映
        notifyItemRemoved(position);
    }

    public void addItem(String item){
        loadDataSet.add(item);
    }

}
