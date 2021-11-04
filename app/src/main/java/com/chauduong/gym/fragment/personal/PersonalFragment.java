package com.chauduong.gym.fragment.personal;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.DialogAddinfomationbodyBinding;
import com.chauduong.gym.databinding.DialogSelectChartBinding;
import com.chauduong.gym.databinding.FragmentPersonalBinding;
import com.chauduong.gym.fragment.bodyinformation.BodyInformationActivity;
import com.chauduong.gym.model.BodyInformation;
import com.chauduong.gym.utils.Util;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.material.appbar.AppBarLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PersonalFragment extends Fragment implements View.OnClickListener, OnChartValueSelectedListener, AppBarLayout.OnOffsetChangedListener {
    private static final int TYPE_CHART_WEIGHT = 1;
    private FragmentPersonalBinding mFragmentPersonalBinding;
    private PersonalViewModel mPersonalViewModel;
    private AlertDialog addDialog;
    private CombinedChart mChartWeight;
    private LineChart mChartFat;
    private LineChart mChartMuscle;
    private LineChart mChartComplex;
    private long millisFromDate = 0;
    private long millisToDate = 0;
    private AlertDialog mSaveChartDialog;


    public PersonalFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentPersonalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal, container, false);
        return mFragmentPersonalBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewModel();
        initDataChart();
    }

    private void initDataChart() {
        mPersonalViewModel.getAllBodyInformation();
    }

    private void initViewModel() {
        mPersonalViewModel = ViewModelProviders.of(this).get(PersonalViewModel.class);
        mPersonalViewModel.getIsAddSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (addDialog != null && addDialog.isShowing()) {
                    addDialog.dismiss();
                    if (aBoolean) {
                        Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getContext().getString(R.string.add_bodyinfor_success), Util.TYPE_SNACK_BAR_SUCCESS);
                    } else {
                        Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getContext().getString(R.string.add_bodyinfor_faild), Util.TYPE_SNACK_BAR_WRONG);
                    }
                }
            }
        });
        mPersonalViewModel.getListBodyInformation().observe(getViewLifecycleOwner(), new Observer<List<BodyInformation>>() {
            @Override
            public void onChanged(List<BodyInformation> bodyInformations) {
                updateHeightBMIChart(bodyInformations);
                updateFatChart(bodyInformations);
                updateMuscleChart(bodyInformations);
                updateComplexChart(bodyInformations);
            }
        });
        mPersonalViewModel.getIsSaveSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (mSaveChartDialog != null && mSaveChartDialog.isShowing()) {
                    if (aBoolean) {
                        Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getString(R.string.save_to_gallery_success), Util.TYPE_SNACK_BAR_SUCCESS);
                    } else {
                        Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getString(R.string.save_to_gallery_error), Util.TYPE_SNACK_BAR_WRONG);
                    }
                    mSaveChartDialog.dismiss();
                }
            }
        });
    }

    private void updateComplexChart(List<BodyInformation> bodyInformations) {
        if (bodyInformations != null && bodyInformations.size() != 0) {
            mChartComplex.getDescription().setEnabled(false);
            mChartComplex.setDrawGridBackground(false);
            mChartComplex.setOnChartValueSelectedListener(PersonalFragment.this);
            mChartComplex.setTouchEnabled(true);
            mChartComplex.setDragEnabled(true);
            mChartComplex.setScaleEnabled(true);
            mChartComplex.setDrawGridBackground(false);
            mChartComplex.setPinchZoom(true);
            mChartComplex.setBackgroundColor(Color.WHITE);
            mChartComplex.getAxisRight().setEnabled(true);
            YAxis leftAxis = mChartComplex.getAxisLeft();
            leftAxis.setDrawGridLines(false);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setAxisMaximum(100f);
            leftAxis.setTextSize(12f);
            leftAxis.setAxisLineWidth(1f);
            leftAxis.setTextColor(Color.BLACK);

            YAxis rightAxis = mChartComplex.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setAxisMinimum(0f);
            rightAxis.setAxisMaximum(30f);
            rightAxis.setTextSize(12f);
            rightAxis.setAxisLineWidth(1f);
            rightAxis.setTextColor(Color.BLACK);

            XAxis xAxis = mChartComplex.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setEnabled(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisLineColor(Color.BLACK);
            xAxis.setAxisLineWidth(1f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setGranularity(1);
            xAxis.setTextSize(12f);
            xAxis.setLabelRotationAngle(-45);

            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    long unixDate = (long) (value * 100000);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                    Date resulted = new Date(unixDate);
                    return sdf.format(resulted);
                }
            });


            ArrayList<Entry> valueWater = new ArrayList<>();
            ArrayList<Entry> valueProtein = new ArrayList<>();
            ArrayList<Entry> valueMinerals = new ArrayList<>();
            for (BodyInformation b : bodyInformations) {
                valueWater.add(new Entry(b.getDate(), Float.parseFloat(b.getWater())));
                valueProtein.add(new Entry(b.getDate(), Float.parseFloat(b.getProtein())));
                valueMinerals.add(new Entry(b.getDate(), Float.parseFloat(b.getMineral())));
            }

            LineDataSet waterSet, proteinSet, mineralSet;
            waterSet = new LineDataSet(valueWater, getString(R.string.water));
            waterSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            waterSet.setColor(getContext().getColor(R.color.chart_muscle));
            waterSet.setCircleColor(Color.GRAY);
            waterSet.setValueTextSize(10f);
            waterSet.setLineWidth(2f);
            waterSet.setCircleRadius(3f);
            waterSet.setHighLightColor(Color.rgb(244, 117, 117));
            waterSet.setValueFormatter(new IValueFormatter() {
                @SuppressLint("DefaultLocale")
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.format("%.1f", value);
                }
            });

            proteinSet = new LineDataSet(valueProtein, getString(R.string.protetin));
            proteinSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            proteinSet.setColor(Color.RED);
            proteinSet.setCircleColor(Color.GRAY);
            proteinSet.setValueTextSize(10f);
            proteinSet.setLineWidth(2f);
            proteinSet.setCircleRadius(3f);
            proteinSet.setHighLightColor(Color.rgb(244, 117, 117));
            proteinSet.setValueFormatter(new IValueFormatter() {
                @SuppressLint("DefaultLocale")
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.format("%.1f", value);
                }
            });

            mineralSet = new LineDataSet(valueMinerals, getString(R.string.mineral));
            mineralSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            mineralSet.setColor(getContext().getColor(R.color.colorPrimary));
            mineralSet.setCircleColor(Color.GRAY);
            mineralSet.setValueTextSize(10f);
            mineralSet.setLineWidth(2f);
            mineralSet.setCircleRadius(3f);
            mineralSet.setHighLightColor(Color.rgb(244, 117, 117));
            mineralSet.setValueFormatter(new IValueFormatter() {
                @SuppressLint("DefaultLocale")
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.format("%.1f", value);
                }
            });

            LineData lineData = new LineData();
            lineData.addDataSet(waterSet);
            lineData.addDataSet(proteinSet);
            lineData.addDataSet(mineralSet);
            mChartComplex.setData(lineData);
            mChartComplex.invalidate();
            mChartComplex.animateX(2000);
        }
    }

    private void updateMuscleChart(List<BodyInformation> bodyInformations) {
        if (bodyInformations != null && bodyInformations.size() != 0) {
            mChartMuscle.setBackgroundColor(Color.WHITE);
            // no description text
            mChartMuscle.getDescription().setEnabled(false);
            // enable touch gestures
            mChartMuscle.setTouchEnabled(true);
            // enable scaling and dragging
            mChartMuscle.setDragEnabled(true);
            mChartMuscle.setScaleEnabled(true);
            // if disabled, scaling can be done on x- and y-axis separately
            mChartMuscle.setPinchZoom(false);
            mChartMuscle.setDrawGridBackground(false);
            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
            mv.setChartView(mChartMuscle);
            mChartMuscle.setMarker(mv);

            ArrayList<Entry> values = new ArrayList<>();
            for (BodyInformation b : bodyInformations) {
                float val = Float.parseFloat(b.getMuscle());
                values.add(new Entry(b.getDate(), val));
            }

            LineDataSet set1;
            set1 = new LineDataSet(values, getString(R.string.muscle));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(true);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setCircleColor(getContext().getColor(R.color.chart_muscle));
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(getContext().getColor(R.color.chart_muscle));
            set1.setFillColor(getContext().getColor(R.color.chart_muscle_fill));
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return mChartMuscle.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(set1);
            data.setValueTextSize(12f);
            data.setDrawValues(true);

            // set data
            mChartMuscle.setData(data);

            YAxis y = mChartMuscle.getAxisLeft();
            y.setTextColor(Color.BLACK);
            y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            y.setDrawGridLines(false);
            y.setAxisMaximum(0f);
            y.setAxisMaximum(100f);
            y.setTextSize(12f);
            y.setAxisLineWidth(1f);
            y.setTextColor(Color.BLACK);

            XAxis x = mChartMuscle.getXAxis();
            x.setDrawGridLines(false);
            x.setPosition(XAxis.XAxisPosition.BOTTOM);
            x.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    long unixDate = (long) (value * 100000);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                    Date resulted = new Date(unixDate);
                    return sdf.format(resulted);
                }
            });
            x.setAxisLineColor(Color.BLACK);
            x.setAxisLineWidth(1f);
            x.setTextColor(Color.BLACK);
            mChartMuscle.getAxisRight().setEnabled(false);
            Legend l = mChartMuscle.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

            mChartMuscle.animateX(2000);

            // don't forget to refresh the drawing
            mChartMuscle.invalidate();
        }
    }

    private void updateFatChart(List<BodyInformation> bodyInformations) {
        if (bodyInformations != null && bodyInformations.size() != 0) {
            mChartFat.setBackgroundColor(Color.WHITE);
            mChartFat.getDescription().setEnabled(false);
            mChartFat.setTouchEnabled(true);
            mChartFat.setOnChartValueSelectedListener(this);
            mChartFat.setDrawGridBackground(false);
            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
            mv.setChartView(mChartFat);
            mChartFat.setMarker(mv);

            mChartFat.setDragEnabled(true);
            mChartFat.setScaleEnabled(true);
            mChartFat.setPinchZoom(true);
            XAxis xAxis;
            xAxis = mChartFat.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    long unixDate = (long) (value * 100000);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                    Date resulted = new Date(unixDate);
                    return sdf.format(resulted);
                }
            });
            xAxis.setAxisLineColor(Color.BLACK);
            xAxis.setAxisLineWidth(1f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setGranularity(1);
            xAxis.setTextSize(12f);
            xAxis.setLabelRotationAngle(-45);

            YAxis yAxis;
            yAxis = mChartFat.getAxisLeft();
            yAxis.setDrawGridLines(false);
            mChartFat.getAxisRight().setEnabled(false);
            yAxis.setAxisMaximum(40f);
            yAxis.setAxisMinimum(0);
            yAxis.setDrawGridLines(false);
            yAxis.setTextSize(12f);
            yAxis.setAxisLineWidth(1f);
            yAxis.setTextColor(Color.BLACK);


            LimitLine ll1 = new LimitLine(35f, getString(R.string.upper_limit_fat));
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(8f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);

            LimitLine ll2 = new LimitLine(8f, getString(R.string.lower_limit_fat));
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(8f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);

            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            Legend l = mChartFat.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            ArrayList<Entry> values = new ArrayList<>();
            for (BodyInformation b : bodyInformations) {
                float val = Float.parseFloat(b.getFat());
                values.add(new Entry(b.getDate(), val));
            }

            LineDataSet set = new LineDataSet(values, getString(R.string.fat));
            set.setDrawIcons(false);
            set.enableDashedLine(10f, 5f, 0f);
            set.setColor(Color.BLACK);
            set.setCircleColor(Color.BLACK);
            set.setLineWidth(2f);
            set.setCircleRadius(4f);
            set.setFormLineWidth(1f);
            set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set.setFormSize(15.f);
            set.setValueTextSize(12f);
            set.enableDashedHighlightLine(10f, 5f, 0f);
            set.setDrawFilled(true);
            set.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return mChartFat.getAxisLeft().getAxisMinimum();
                }
            });

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                set.setFillDrawable(drawable);
            } else {
                set.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            LineData data = new LineData(dataSets);
            mChartFat.setData(data);
            mChartFat.animateX(2000);
        }
    }


    private void updateHeightBMIChart(List<BodyInformation> bodyInformationList) {
        if (bodyInformationList != null && bodyInformationList.size() != 0) {
            mChartWeight.getDescription().setEnabled(false);
            mChartWeight.setDrawGridBackground(false);
            mChartWeight.setDrawBarShadow(false);
            mChartWeight.setHighlightFullBarEnabled(false);
            mChartWeight.setOnChartValueSelectedListener(PersonalFragment.this);
            mChartWeight.setTouchEnabled(true);
            mChartWeight.setDragEnabled(true);
            mChartWeight.setScaleEnabled(true);
            mChartWeight.setDrawGridBackground(false);
            mChartWeight.setPinchZoom(true);
            mChartWeight.setBackgroundColor(Color.WHITE);
            mChartWeight.getAxisRight().setEnabled(true);
            mChartWeight.setDrawBarShadow(false);

            YAxis leftAxis = mChartWeight.getAxisLeft();
            leftAxis.setDrawGridLines(false);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setTextSize(12f);
            leftAxis.setAxisLineWidth(1f);
            leftAxis.setTextColor(Color.BLACK);

            YAxis rightAxis = mChartWeight.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setAxisMinimum(0f);
            rightAxis.setAxisMaximum(40);
            rightAxis.setTextSize(12f);
            rightAxis.setAxisLineWidth(1f);
            rightAxis.setTextColor(Color.BLACK);

            XAxis xAxis = mChartWeight.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setEnabled(true);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisLineColor(Color.BLACK);
            xAxis.setAxisLineWidth(1f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setGranularity(1);
            xAxis.setTextSize(12f);
            xAxis.setLabelRotationAngle(-45);

            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    long unixDate = (long) (value * 100000);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                    Date resulted = new Date(unixDate);
                    return sdf.format(resulted);
                }
            });

            CombinedData data = new CombinedData();
            LineData lineDatas = new LineData();
            lineDatas.addDataSet((ILineDataSet) dataWeightChart(bodyInformationList));


            data.setData(lineDatas);
            data.setData(dataBMIChart(bodyInformationList));
            xAxis.setAxisMinimum(data.getXMin() - 864);
            xAxis.setAxisMaximum(data.getXMax() + 864);
            mChartWeight.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            mChartWeight.setData(data);
            mChartWeight.invalidate();
            mChartWeight.animateX(2000);
            mChartWeight.animateY(2000);

        }
    }


    private DataSet dataWeightChart(List<BodyInformation> bodyInformationList) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < bodyInformationList.size(); i++) {
            float value = 0;
            value = Float.parseFloat(bodyInformationList.get(i).getWeight());
            entries.add(new Entry(bodyInformationList.get(i).getDate(), value));
        }
        LineData d = new LineData();
        LineDataSet set = new LineDataSet(entries, getString(R.string.weight));
        set.setColor(getContext().getColor(R.color.colorSettingAccount));
        set.setLineWidth(2f);
        set.setCircleColor((getContext().getColor(R.color.colorSettingAccount)));
        set.setCircleRadius(4f);
        set.setFillColor(Color.YELLOW);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(12f);
        set.setValueTextColor(Color.BLACK);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);
        return set;
    }

    private BarData dataBMIChart(List<BodyInformation> bodyInformationList) {
        List<BarEntry> barEntries = new ArrayList<>();
        for (BodyInformation b : bodyInformationList) {
            float value;
            float weight = Float.parseFloat(b.getWeight());
            float height = Float.parseFloat(b.getHeight());
            value = (weight / (height * height));
            barEntries.add(new BarEntry(b.getDate(), value));
        }
        BarDataSet set = new BarDataSet(barEntries, "BMI");
        set.setColor(getContext().getColor(R.color.colorPrimary));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(12f);
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setBarBorderWidth(10f);
        set.setBarBorderColor(getContext().getColor(R.color.colorPrimary));
        return new BarData(set);
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        mFragmentPersonalBinding.btnMore.setOnClickListener(this);
        mFragmentPersonalBinding.appbar.addOnOffsetChangedListener(this);
        mFragmentPersonalBinding.btnAdd.setOnClickListener(this);
        mFragmentPersonalBinding.btnClear.setOnClickListener(this);
        mFragmentPersonalBinding.toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mChartWeight = mFragmentPersonalBinding.chartWeight;
        mChartFat = mFragmentPersonalBinding.chartFat;
        mChartMuscle = mFragmentPersonalBinding.chartMuscle;
        mChartComplex = mFragmentPersonalBinding.chartCombine;
        mFragmentPersonalBinding.btnSearh.setOnClickListener(this);
        mFragmentPersonalBinding.edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(myCalendar, (EditText) v);
                    }

                };
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mFragmentPersonalBinding.edtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(myCalendar, (EditText) v);
                    }

                };
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                showDialogAdd();
                break;
            case R.id.btnSearh:
                search();
                break;
            case R.id.btnClear:
                clearAndSearchAll();
                break;
            case R.id.btnMore:
                showMoreMenu();
                break;
            default:
                break;
        }
    }

    private void showMoreMenu() {
        PopupMenu menu = new PopupMenu(getContext(), mFragmentPersonalBinding.btnMore);
        menu.getMenuInflater().inflate(R.menu.main_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btnClearMenu:
                        clearAndSearchAll();
                        break;
                    case R.id.btnSaveMenu:
                        showSelectChart();
                        break;
                    case R.id.btnManagerMenu:
                        showBodyInformationActivity();
                        break;
                }
                return false;
            }
        });
        menu.show();
    }

    private void showBodyInformationActivity() {
        Intent intent = new Intent(getContext(), BodyInformationActivity.class);
        startActivity(intent);
    }

    private void showSelectChart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogSelectChartBinding dialogSelectChartBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_select_chart, null, false);
        builder.setView(dialogSelectChartBinding.getRoot());
        dialogSelectChartBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSaveChartDialog != null && mSaveChartDialog.isShowing()) {
                    mSaveChartDialog.dismiss();
                }
            }
        });
        dialogSelectChartBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    List<Chart> chartArrayList = new ArrayList<>();
                    if (dialogSelectChartBinding.cbHeight.isChecked()) {
                        chartArrayList.add(mChartWeight);
                    }
                    if (dialogSelectChartBinding.cbFat.isChecked()) {
                        chartArrayList.add(mChartFat);
                    }
                    if (dialogSelectChartBinding.cbMuscle.isChecked()) {
                        chartArrayList.add(mChartMuscle);
                    }
                    if (dialogSelectChartBinding.cbComplex.isChecked()) {
                        chartArrayList.add(mChartComplex);
                    }
                    mPersonalViewModel.saveChartGallery(chartArrayList);

                } else {
                    Util.requestStoragePermission(mFragmentPersonalBinding.getRoot(), getActivity());
                }
            }
        });
        mSaveChartDialog = builder.create();
        mSaveChartDialog.getWindow().setBackgroundDrawable((new ColorDrawable(android.graphics.Color.TRANSPARENT)));
        mSaveChartDialog.setCanceledOnTouchOutside(false);
        mSaveChartDialog.setCancelable(false);
        mSaveChartDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        if (mSaveChartDialog != null && !mSaveChartDialog.isShowing())
            mSaveChartDialog.show();
    }


    private void clearAndSearchAll() {
        mFragmentPersonalBinding.edtFromDate.setText("");
        mFragmentPersonalBinding.edtToDate.setText("");
        millisToDate = 0;
        millisFromDate = 0;
        mPersonalViewModel.getAllBodyInformation();
    }

    private void search() {
        boolean isValid = checkValid();
        if (isValid) {
            mPersonalViewModel.searchBodyInformation(millisFromDate, millisToDate);
        }
    }

    private boolean checkValid() {
        if (mFragmentPersonalBinding.edtFromDate.getText().toString().length() == 0) {
            Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getString(R.string.please_add_information), Util.TYPE_SNACK_BAR_WARNING);
            mFragmentPersonalBinding.edtFromDate.requestFocus();
            return false;
        }
        if (mFragmentPersonalBinding.edtToDate.getText().toString().length() == 0) {
            Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getString(R.string.please_add_information), Util.TYPE_SNACK_BAR_WARNING);
            mFragmentPersonalBinding.edtToDate.requestFocus();
            return false;
        }
        String fromDate = mFragmentPersonalBinding.edtFromDate.getText().toString();
        String toDate = mFragmentPersonalBinding.edtToDate.getText().toString();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = sdf.parse(fromDate);
            millisFromDate = Objects.requireNonNull(date).getTime() / 100000;
            date = sdf.parse(toDate);
            millisToDate = Objects.requireNonNull(date).getTime() / 100000;
            if (millisToDate - millisFromDate <= 0) {
                Util.showSnackbar(mFragmentPersonalBinding.getRoot(), getString(R.string.please_check_information), Util.TYPE_SNACK_BAR_WARNING);
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogAddinfomationbodyBinding dialogAddinfomationbodyBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_addinfomationbody, null, false);
        builder.setCustomTitle(dialogAddinfomationbodyBinding.getRoot());
        builder.setIcon(R.drawable.ic_baseline_addchart_24);
        addDialog = builder.create();
        addDialog.getWindow().setBackgroundDrawable((new ColorDrawable(android.graphics.Color.TRANSPARENT)));
        addDialog.setCancelable(false);
        addDialog.setCanceledOnTouchOutside(false);
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, dialogAddinfomationbodyBinding.edtDate);
            }

        };
        dialogAddinfomationbodyBinding.edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dialogAddinfomationbodyBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addDialog != null && addDialog.isShowing())
                    addDialog.dismiss();
            }
        });
        dialogAddinfomationbodyBinding.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = dialogAddinfomationbodyBinding.getRoot();
                boolean isFullValid = Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtHeight)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtWeight)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtMuscle)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtFat)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtProtetin)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtMineral)
                        && Util.checkValidEditText(parent, dialogAddinfomationbodyBinding.edtWater);
                if (isFullValid) {
                    String height = dialogAddinfomationbodyBinding.edtHeight.getText().toString();
                    String weight = dialogAddinfomationbodyBinding.edtWeight.getText().toString();
                    String muscle = dialogAddinfomationbodyBinding.edtMuscle.getText().toString();
                    String fat = dialogAddinfomationbodyBinding.edtFat.getText().toString();
                    String protein = dialogAddinfomationbodyBinding.edtProtetin.getText().toString();
                    String mineral = dialogAddinfomationbodyBinding.edtMineral.getText().toString();
                    String water = dialogAddinfomationbodyBinding.edtWater.getText().toString();
                    String dateText = dialogAddinfomationbodyBinding.edtDate.getText().toString();
                    String myDate = dateText;
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date date;
                    long millis = 0;
                    try {
                        date = sdf.parse(myDate);
                        millis = Objects.requireNonNull(date).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    BodyInformation bodyInformation = new BodyInformation(height, weight, muscle, fat, protein, mineral, water, (float) (millis / 100000));
                    mPersonalViewModel.addBodyInformation(bodyInformation);
                }
            }
        });
        addDialog.show();
        addDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void updateLabel(Calendar myCalendar, EditText editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
            mFragmentPersonalBinding.btnMore.setVisibility(View.VISIBLE);
        } else {
            mFragmentPersonalBinding.btnMore.setVisibility(View.GONE);
        }
    }

}