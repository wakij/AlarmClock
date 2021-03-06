package com.example.alarmclock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;

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
        needstep.setText(sharedPreferences.getInt("needfootstep", 0) + " ???");


//        ??????????????????
        mChart = view.findViewById(R.id.line_chart);

        alarmDataList = new ArrayList<>();

        //x???
        ArrayList<String> xValues = new ArrayList<>();

        //y???
        ArrayList<String> yValues = new ArrayList<>();

        //?????????
        ArrayList<Entry> Values = new ArrayList<>();

//        ??????????????????
        DatabaseHelper helper = new DatabaseHelper(getContext());

        // ????????????????????????????????????????????????
        String[] cols = {DBDef.DBEntry._ID, DBDef.DBEntry.DATE, DBDef.DBEntry.REAL_WAKE_UP_TIME, DBDef.DBEntry.ESTIMATED_WAKE_UP_TIME, DBDef.DBEntry.FOOTSTEP_COUNT};

        // ?????????????????????????????????????????????????????????
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            // ???????????????????????????
            Cursor cursor = db.query(DBDef.DBEntry.CHART_TABLE, cols, null,
                    null, null, null, null, null);

            //??????????????????????????????????????????????????????????????????????????????
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

    //????????????????????????
    public void createChart(ArrayList<String> xValues, ArrayList<String> yValues, ArrayList<Entry> Values)
    {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet valueDataSet = new LineDataSet(Values,"??????");
        //point???????????????
        valueDataSet.setCircleColor(Color.parseColor("#e43a19"));
        //point?????????
        valueDataSet.setCircleRadius(4);
        //?????????
        valueDataSet.setColor(Color.parseColor("#e43a19"));
        dataSets.add(valueDataSet);
        LineData lineData = new LineData(dataSets);
        //????????????
        valueDataSet.setFillColor(Color.parseColor("#e43a19"));
        valueDataSet.setDrawFilled(true);
        //????????????
        valueDataSet.setLineWidth(2);

        //x????????????
        XAxis xAxis = mChart.getXAxis();
        IndexAxisValueFormatter indexAxisValueFormatter = new IndexAxisValueFormatter();
        indexAxisValueFormatter.setValues(xValues.toArray((new String[xValues.size()])));
        xAxis.setValueFormatter(indexAxisValueFormatter);
        xAxis.setLabelCount(xValues.size(),true);
        //???????????????????????????
        xAxis.setDrawGridLines(false);
        //x????????????????????????????????????
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //description?????????(???????????????
        Description description = new Description();
        description.setText("???????????????");
        mChart.setDescription(description);
        //?????????y??????????????????
        YAxis yAxis_l = mChart.getAxisLeft();
        //?????????y??????????????????
        YAxis yAxis_r = mChart.getAxisRight();
        //???????????????????????????
        yAxis_r.setEnabled(false);
        //y??????????????????????????????

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
//        ?????????????????????????????????
        ViewPortHandler viewPortHandler = mChart.getViewPortHandler();
        Transformer transformer = mChart.getTransformer(YAxis.AxisDependency.LEFT);
        Custom_y_axis_renderer custom_y_axis_renderer = new Custom_y_axis_renderer(viewPortHandler,yAxis_l,transformer);
        custom_y_axis_renderer.setValue(toList(yValues));
        mChart.setRendererLeftYAxis(custom_y_axis_renderer);
        yAxis_l.setLabelCount(yValues.size());

//        y?????????(float)???(int)????????????
        yAxis_l.setValueFormatter(y_valueformatter);

//        y????????????????????????????????????
        yAxis_l.setTextSize(10);

        //entry??????????????????????????????????????????
        ValueFormatter int_value_formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int)value);
            }
        };
        valueDataSet.setValueFormatter(int_value_formatter);

        //??????????????????
        mChart.setData(lineData);

        //????????????????????????????????????
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.e("entryX",String.valueOf(entry.getX()));
                Log.e("entryY",String.valueOf(entry.getY()));
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //??????
        mChart.invalidate();
    }

//    ArryaList????????????????????????
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



