package com.example.alarmclock;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

public class Explaination extends FragmentActivity {

    private ViewPager2 pager;
    private ExplainationPageStateAdapter adapter;
    private LinearLayout layout;
    private ImageView[] imageViews;
    private int imageWidth;
    private int imageHeight;
    private LinearLayout.LayoutParams layoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.explaination);

        View decor = getWindow().getDecorView();
        // hide navigation bar, hide status bar
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        pager = findViewById(R.id.pager);
        adapter = new ExplainationPageStateAdapter(this);
        pager.setAdapter(adapter);
        layout = (LinearLayout)findViewById(R.id.layout);
        createPageControl(3);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state)
            {}
            @Override
            public void onPageScrolled(int position, float positionOffset, int PxpositionOffsetPixels)
            {}
            @Override
            public void onPageSelected(int position){
                for (int i = 0; i < 3; i++){
                    imageViews[i].setImageResource(R.drawable.shape_page_control_default);
                }
                imageViews[position].setImageResource(R.drawable.shape_page_control_selected);
            }

        });
    }

    public void createPageControl(int itemCount){
        imageViews = new ImageView[itemCount];
        imageWidth = 30;
        imageHeight = 30;

        for (int i = 0; i < imageViews.length; i++){
            imageViews[i] = new ImageView(this);
            imageViews[i].setImageResource(R.drawable.shape_page_control_default);
            layoutParams= new LinearLayout.LayoutParams(imageWidth, imageHeight);
            layoutParams.setMargins(220, 0, 220, 0);
            imageViews[i].setLayoutParams(layoutParams);
            layout.addView(imageViews[i]);
        }
        imageViews[0].setImageResource(R.drawable.shape_page_control_selected);
    }

}
