//Copyright 2020 Philipp Jahoda
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this software except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

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
