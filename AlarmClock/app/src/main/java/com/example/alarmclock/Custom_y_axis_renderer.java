package com.example.alarmclock;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class Custom_y_axis_renderer extends YAxisRenderer {
    private float[] values;
    public Custom_y_axis_renderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
    }
    @Override
    public void computeAxisValues(float min, float max)
    {
        super.computeAxisValues(min,max);
        mYAxis.mEntries = new float[values.length];
        mYAxis.mEntryCount = values.length;
        for (int i=0;i < values.length;i++)
        {
            mYAxis.mEntries[i] = values[i];
        }
    }
    public void setValue(float[] _values)
    {
        values = _values;
    }
}
