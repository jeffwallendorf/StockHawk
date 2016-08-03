package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.StockTaskService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class LineGraphActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        Intent intent = getIntent();
        ArrayList<Double> historicalValues = (ArrayList<Double>) intent.getSerializableExtra("EXTRA_HISTORICAL_VALUES");
        float[] resultValues = new float[historicalValues.size()];

        Double Max = historicalValues.get(0), Min = historicalValues.get(0);

        String[] labels = new String[historicalValues.size()];
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        labels[historicalValues.size() - 1] = formattedDate;

        for (int i = historicalValues.size() - 1; i >= 0; i--) {
            if (historicalValues.get(i) > Max) {
                Max = historicalValues.get(i);
            }
            if (historicalValues.get(i) < Min) {
                Min = historicalValues.get(i);
            }
            c.add(Calendar.DAY_OF_YEAR, -1);

            if (i % 15 == 0) {
                formattedDate = df.format(c.getTime());
                labels[i] = formattedDate;
            } else {
                labels[i] = "";
            }
            resultValues[i] = historicalValues.get(historicalValues.size() - 1 - i).floatValue();
        }

        LineChartView mChart = (LineChartView) findViewById(R.id.linechart);
        LineSet dataset = new LineSet(labels, resultValues);
        dataset.setColor(Color.parseColor("#758cbb"))
                .setDotsColor(Color.parseColor("#758cbb"))
                .setThickness(2);

        mChart.addData(dataset);
        int step = (int) ((Max - Min) / 10);
        if (step == 0) {
            step = 1;
        }
        mChart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues((int) (Min - 0.05 * Min), (int) (Max + 0.05 * Min))
                .setLabelsColor(Color.parseColor("#6a84c3"))
                .setStep(step).setXAxis(true)
                .setYAxis(true);


        Animation anim = new Animation();

        mChart.show(anim);

    }
}
