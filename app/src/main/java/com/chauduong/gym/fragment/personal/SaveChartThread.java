package com.chauduong.gym.fragment.personal;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.chauduong.gym.R;
import com.chauduong.gym.utils.Util;
import com.github.mikephil.charting.charts.Chart;

public class SaveChartThread implements Runnable {
    private Chart mChart;
    private SaveChartListener listener;

    public SaveChartThread(Chart mChart, SaveChartListener saveChartListener) {
        this.mChart = mChart;
        listener = saveChartListener;
    }

    @Override
    public void run() {
        if (mChart != null) {
            boolean result= mChart.saveToGallery("chart" + "_" + System.currentTimeMillis(), 100);
            listener.onComplete(result);
        }
    }
}
