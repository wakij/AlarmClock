package com.example.alarmclock;

import androidx.recyclerview.widget.ItemTouchHelper;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemSwipeController extends ItemTouchHelper.SimpleCallback {
    private final Paint paint = new Paint(); //色に関する情報を格納
    private final ColorDrawable background = new ColorDrawable();
    //    private Context mContext;
//    private final Resources res = mContext.getResources();
//    private Drawable deleteIcon = ResourcesCompat.getDrawable(res,R.drawable.dustbox,null);
//    private int deleteIconWidth = deleteIcon.getIntrinsicWidth();
//    private int deleteIconHeight = deleteIcon.getIntrinsicHeight();
    private Drawable deleteIcon;
    private int deleteIconWidth;
    private int deleteIconHeight;

    public ItemSwipeController(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

//        boolean isCanseled = dX == 0f && !isCurrentlyActive;
//        View itemview = viewHolder.itemView;
//        if (isCanseled) {
//            clearCanvas(c, itemview.getRight() + dX, itemview.getTop(), itemview.getRight(), itemview.getBottom());
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            return;
//        }
//
//        boolean  isLeftDirection= dX < 0;
//        if (isLeftDirection){
////            background.setColor(Color.parseColor("#FF6B6B"));
////            background.setBounds(itemview.getRight() + (int)dX, itemview.getTop(), itemview.getRight(), itemview.getBottom());
//        }
////        background.draw(c); //描画
//
//        int itemHeight = itemview.getBottom() - itemview.getTop();
//        if (isLeftDirection){
//            int deleteIconTop = itemview.getTop() + (itemHeight - deleteIconHeight) / 2;
//            int deleteIconMargin = (itemHeight - deleteIconHeight) / 2;
//            int deleteIconLeft = itemview.getRight() - deleteIconMargin - deleteIconWidth;
//            int deleteIconRight = itemview.getRight() - deleteIconMargin;
//            int deleteIconBottom = deleteIconTop + deleteIconHeight;
//
//            deleteIcon.setBounds(deleteIconLeft,deleteIconTop, deleteIconRight, deleteIconBottom);
//            deleteIcon.draw(c);
//        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

    //盤面を初期化する
    private void clearCanvas(Canvas c,float left, float top, float right, float bottom){
        paint.setColor(Color.parseColor("#FF6B6B"));
        c.drawRect(left, top, right, bottom, paint);
    }

    public void setDeleteIcon(Drawable Icon){
        deleteIcon = Icon;
        deleteIconWidth = deleteIcon.getIntrinsicWidth();
        deleteIconHeight = deleteIcon.getIntrinsicHeight();
    }

    //Drawableの取得をcreatefromPathを使った方が良いかもしれない


}
