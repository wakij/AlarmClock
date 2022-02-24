package com.example.alarmclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Personal_Info extends Fragment {

    int soundlevel;
    int level;
    ArrayList<AlarmData> alarmDataList;
    private LineChart mChart;
    private int i;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.personal_info, container, false);
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("sound_level_latter", 0) == 0) {
            soundlevel = sharedPreferences.getInt("sound_level_former", 0);
        } else {
            soundlevel = sharedPreferences.getInt("sound_level_latter", 0);
        }
        level = soundlevel / 100 + 1;

        TextView level_text = view.findViewById(R.id.level_text);
        level_text.setText("Level" + level);

        TextView needstep = view.findViewById(R.id.needstep_text);
        needstep.setText(sharedPreferences.getInt("needfootstep", 0) + " 歩");

        ImageView level_icon = view.findViewById(R.id.level_icon);

        switch (level) {
            case 1:
                level_icon.setImageResource(R.drawable.alarmimg);
                break;
            case 2:
                level_icon.setImageResource(R.drawable.alarmimg);
                break;
            case 3:
                level_icon.setImageResource(R.drawable.dustbox);
                break;
            case 4:
                level_icon.setImageResource(R.drawable.ic_baseline_account_circle_24);
                break;
            case 5:
                level_icon.setImageResource(R.drawable.ic_baseline_unfold_more_24);
        }

//        グラフを描画
        mChart = view.findViewById(R.id.line_chart);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        alarmDataList = new ArrayList<>();

        //x軸
        ArrayList<String> xValues = new ArrayList<>();

        //y軸
        ArrayList<String> yValues = new ArrayList<>();

        //点の値
        ArrayList<Entry> Values = new ArrayList<>();

//        データを用意
        DatabaseHelper helper = new DatabaseHelper(getContext());

        // データベースを検索する項目を定義
        String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.DATE, DBDef.DBEntry.REAL_WAKE_UP_TIME, DBDef.DBEntry.ESTIMATED_WAKE_UP_TIME, DBDef.DBEntry.FOOTSTEP_COUNT};

        // 読み込みモードでデータベースをオープン
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            // データベースを検索
            Cursor cursor = db.query(DBDef.DBEntry.CHART_TABLE, cols, null,
                    null, null, null, null, null);

            //データベースに格納されている全データを格納するリスト
            if (cursor.moveToFirst()) {
                i=0;
                xValues.add(cursor.getString(1));
                int yValue = cursor.getInt(4);
                yValues.add(String.valueOf(yValue));
                Values.add(new Entry(i,yValue));
                i++;
            }
            while (cursor.moveToNext()){
                xValues.add(cursor.getString(1));
                int yValue = cursor.getInt(4);
                if (!yValues.contains(String.valueOf(yValue))) {
                    yValues.add(String.valueOf(yValue));
                }
                Values.add(new Entry(i,yValue));
                i++;
            }
            cursor.close();
        }
        createChart(xValues,yValues,Values);
    }

    //グラフを描画する
    public void createChart(ArrayList<String> xValues, ArrayList<String> yValues, ArrayList<Entry> Values)
    {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet valueDataSet = new LineDataSet(Values,"歩数");
        //pointの色を変更
        valueDataSet.setCircleColor(Color.parseColor("#e43a19"));
        //pointの半径
        valueDataSet.setCircleRadius(4);
        //線の色
        valueDataSet.setColor(Color.parseColor("#e43a19"));
        dataSets.add(valueDataSet);
        LineData lineData = new LineData(dataSets);
        //線下の色
        valueDataSet.setFillColor(Color.parseColor("#e43a19"));
        valueDataSet.setDrawFilled(true);
        //線の太さ
        valueDataSet.setLineWidth(2);

        //x軸を修正
        XAxis xAxis = mChart.getXAxis();
        IndexAxisValueFormatter indexAxisValueFormatter = new IndexAxisValueFormatter();
        indexAxisValueFormatter.setValues(xValues.toArray((new String[xValues.size()])));
        xAxis.setValueFormatter(indexAxisValueFormatter);
        xAxis.setLabelCount(xValues.size(),true);
        //縦のガイド線を消す
        xAxis.setDrawGridLines(false);
        //xラベルを下側に持っていく
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //descriptionを設定(右下のとこ
        Description description = new Description();
        description.setText("歩数の遷移");
        mChart.setDescription(description);
        //左側のyラベルを取得
        YAxis yAxis_l = mChart.getAxisLeft();
        //右側のyラベルを取得
        YAxis yAxis_r = mChart.getAxisRight();
        //右側のラベルを消す
        yAxis_r.setEnabled(false);
        //yラベルの値を設定する

        ValueFormatter y_valueformatter = new ValueFormatter() {
            @Override
            public  String getAxisLabel(float value, AxisBase axisBase)
            {
                return super.getAxisLabel(value,axisBase);
            }
            @Override
            public String getFormattedValue(float value) {
//                Log.e("value_p",String.valueOf(value));
                int i_value = (int)value;
                if (yValues.contains(String.valueOf(i_value)))
                {
                    return String.valueOf((int)value);
                }else
                {
                    return "";
                }
            }
        };
//        カスタムレンダーを設定
        ViewPortHandler viewPortHandler = mChart.getViewPortHandler();
        Transformer transformer = mChart.getTransformer(YAxis.AxisDependency.LEFT);
        Custom_y_axis_renderer custom_y_axis_renderer = new Custom_y_axis_renderer(viewPortHandler,yAxis_l,transformer);
        custom_y_axis_renderer.setValue(toList(yValues));
        mChart.setRendererLeftYAxis(custom_y_axis_renderer);
        yAxis_l.setLabelCount(yValues.size());

//        yラベル(float)を(int)に変える
        yAxis_l.setValueFormatter(y_valueformatter);

//        yラベルの文字サイズを設定
        yAxis_l.setTextSize(10);

        //entryを整数で表示できるようにする
        ValueFormatter int_value_formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int)value);
            }
        };
        valueDataSet.setValueFormatter(int_value_formatter);

        //データを設定
        mChart.setData(lineData);

        //反映
        mChart.invalidate();
    }

//    ArryaListを配列に変換する
    public float[] toList(ArrayList<String> values)
    {
        float[] list = new float[values.size()];
        for (int i =0;i < values.size();i++)
        {
            list[i] = Integer.parseInt(values.get(i));
        }
        return list;
    }

}



