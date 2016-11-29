package com.bigdata.marketsdk.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.module.FenSHIModule;
import com.bigdata.marketsdk.weight.LoadDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 日k，周k，月k
 */
public class R_KLine extends Fragment {

    public static final String TAG = "R_KLine";

    private CombinedChart combinedChart;
    private BarChart barChart;

    private String code;
    private Gson gson;

    private int num;
    private String open, high, low, now;
    private String Volume, Amount;

    private Calendar c = Calendar.getInstance();
    private int hour = c.get(Calendar.HOUR_OF_DAY);

    private List<FenSHIModule.SerialsEntity.DataEntity> dataEntities;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (hour < 15 && hour >= 9) {
                        lodingData();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadDialog.show(getActivity());
        code = (String) getArguments().get("code");
        num = (int) getArguments().get("type");
        gson = new Gson();
        dataEntities = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rkline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        init(view);

        lodingData();

    }

    private void lodingData() {
        RxVolley.get(Api.FENSHIBASE + code + Api.RIK + num, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);

                open = fenSHIModule.getOpen();
                high = fenSHIModule.getHigh();
                low = fenSHIModule.getLow();
                now = fenSHIModule.getNow();
                Amount = fenSHIModule.getAmount();
                Volume = fenSHIModule.getVolume();
                dataEntities = fenSHIModule.getSerials().getData();

                if (dataEntities.size() < 80) {
                    LoadDialog.dismiss(getActivity());
                    return;
                }

                if (dataEntities != null) {
                    LoadDialog.dismiss(getActivity());
                }

                /**
                 * 给集合排序
                 */

                if (num == 16) {
                    Collections.sort(dataEntities, new Comparator<FenSHIModule.SerialsEntity.DataEntity>() {
                        @Override
                        public int compare(FenSHIModule.SerialsEntity.DataEntity lhs, FenSHIModule.SerialsEntity.DataEntity rhs) {
                            if (Integer.parseInt(lhs.getDate()) > Integer.parseInt(rhs.getDate())) {
                                return -1;
                            }
                            return 1;
                        }
                    });
                }

                if (hour < 15 && hour >= 9) {
                    dataEntities.add(new FenSHIModule.SerialsEntity.DataEntity(now, open, low, high, Volume, Amount));
                    showCombinedChart(combinedChart, dataEntities, Color.alpha(1000));
                    showBarChart(barChart, getBarData(dataEntities), Color.alpha(1000));
                } else {
                    showCombinedChart(combinedChart, dataEntities, Color.alpha(1000));
                    showBarChart(barChart, getBarData(dataEntities), Color.alpha(1000));
                }
            }
        });

    }

    private void showBarChart(BarChart barChart, BarData barData, int color) {

        if (barChart == null) {
            return;
        }
        barChart.setBackgroundColor(color);  //背景色

        barChart.setDescription(""); //图表默认右下方的描述，参数是String对象
        barChart.setNoDataTextDescription("没有数据呢(⊙o⊙)");   //没有数据时显示在中央的字符串，参数是String对象

        barChart.setDrawGridBackground(false);//设置图表内格子背景是否显示，默认是false
        barChart.setGridBackgroundColor(color);//设置格子背景色,参数是Color类型对象

        barChart.setDrawBorders(true);//设置图表内格子外的边框是否显示
        barChart.setBorderColor(Color.WHITE);
        XAxis xAxis = barChart.getXAxis();
        YAxis yAxis = barChart.getAxisLeft();
        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// 将X坐标轴放置在底部，默认是在顶部。
        yAxis.setDrawLabels(false);
        yAxis.setDrawGridLines(false);//是否显示Y坐标轴上的刻度竖线，默认是true
        yAxis.setDrawAxisLine(true);

        YAxis rightAxis = barChart.getAxisRight();

        rightAxis.setDrawLabels(false);// 不显示图表的右边y坐标轴线
        rightAxis.setDrawGridLines(false);
        barChart.setTouchEnabled(true); // 设置是否可以触摸
        barChart.setDragEnabled(true);// 是否可以拖拽

        barChart.getLegend().setEnabled(false);
        barChart.setScaleEnabled(true);// 是否可以缩放 x和y轴, 默认是true
        barChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        barChart.setScaleYEnabled(true); //是否可以缩放 仅y轴
        barChart.setPinchZoom(true);  //设置x轴和y轴能否同时缩放。默认是否
        barChart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true

        barChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true

        barChart.setData(barData);
        barChart.invalidate();
        barChart.notifyDataSetChanged();

    }

    private BarData getBarData(List<FenSHIModule.SerialsEntity.DataEntity> dataEntities) {

        ArrayList<BarEntry> yValues = new ArrayList<>();

        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            xValues.add(String.valueOf(dataEntities.get(i).getDate()));
        }

        switch (num) {
            case 16:
                for (int i = 0; i < 60; i++) {
                    float volume = Float.parseFloat(dataEntities.get(i + 20).getAmount()) - Float.parseFloat(dataEntities.get(i + 20).getVolume());
                    yValues.add(new BarEntry(volume, i));
                }
                break;
            case 17:
                for (int i = 0; i < 60; i++) {
                    float volume = Float.parseFloat(dataEntities.get(i + 24).getAmount()) - Float.parseFloat(dataEntities.get(i + 24).getVolume());
                    yValues.add(new BarEntry(volume, i));

                }
                break;
            case 18:
                for (int i = 0; i < 60; i++) {
                    float volume = Float.parseFloat(dataEntities.get(i + 19).getAmount()) - Float.parseFloat(dataEntities.get(i + 20).getVolume());
                    yValues.add(new BarEntry(volume, i));
                }
                break;
        }

        BarDataSet barDataSet = new BarDataSet(yValues, "");
        BarData barData = new BarData(xValues, barDataSet);
        ArrayList<Integer> colors = new ArrayList<>();
        switch (num) {
            case 16:
                for (int i = 0; i < 60; i++) {

                    if (Double.parseDouble(dataEntities.get(i + 20).getNow()) > Double.parseDouble(dataEntities.get(i + 20).getOpen())) {
                        colors.add(Color.RED);
                    }
                    if (Double.parseDouble(dataEntities.get(i + 20).getNow()) < Double.parseDouble(dataEntities.get(i + 20).getOpen())) {
                        colors.add(Color.GREEN);
                    }
                }
                break;
            case 17:
                for (int i = 0; i < 60; i++) {
                    if (Double.parseDouble(dataEntities.get(i + 24).getNow()) > Double.parseDouble(dataEntities.get(i + 24).getOpen())) {
                        colors.add(Color.RED);
                    }
                    if (Double.parseDouble(dataEntities.get(i + 24).getNow()) < Double.parseDouble(dataEntities.get(i + 24).getOpen())) {
                        colors.add(Color.GREEN);
                    }
                }
                break;
            case 18:
                for (int i = 0; i < 60; i++) {
                    if (Double.parseDouble(dataEntities.get(i + 19).getNow()) > Double.parseDouble(dataEntities.get(i + 20).getOpen())) {
                        colors.add(Color.RED);
                    }
                    if (Double.parseDouble(dataEntities.get(i + 19).getNow()) < Double.parseDouble(dataEntities.get(i + 20).getOpen())) {
                        colors.add(Color.GREEN);
                    }
                }
                break;
        }
        barDataSet.setColors(colors);
        barData.setDrawValues(false);
        return barData;

    }

    private void showCombinedChart(CombinedChart binedChart, List<FenSHIModule.SerialsEntity.DataEntity> dataEntities, int color) {

        if (binedChart == null) {
            return;
        }
        binedChart.setBackgroundColor(color);  //背景色

        binedChart.setDescription(""); //图表默认右下方的描述，参数是String对象
        binedChart.setNoDataTextDescription("数据错误...");   //没有数据时显示在中央的字符串，参数是String对象

        binedChart.setDrawGridBackground(true);//设置图表内格子背景是否显示，默认是false
        binedChart.setGridBackgroundColor(color);//设置格子背景色,参数是Color类型对象
        binedChart.setDrawBorders(true);//设置图表内格子外的边框是否显示
        binedChart.setBorderColor(Color.WHITE);
        XAxis xAxis = binedChart.getXAxis();
        YAxis yAxis = binedChart.getAxisLeft();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// 将X坐标轴放置在底部，默认是在顶部。
        yAxis.setShowOnlyMinMax(true);    //参数如果为true Y轴坐标只显示最大值和最小值
        yAxis.setTextColor(Color.WHITE);
        yAxis.setStartAtZero(false);//不从0开始
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);  //参数是INSIDE_CHART(Y轴坐标在内部) 或 OUTSIDE_CHART(在外部（默认是这个）)
        YAxis rightAxis = binedChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        binedChart.getLegend().setEnabled(false);
        binedChart.setTouchEnabled(true); // 设置是否可以触摸
        binedChart.setDragEnabled(true);// 是否可以拖拽

        binedChart.setScaleEnabled(true);// 是否可以缩放 x和y轴, 默认是true
        binedChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        binedChart.setScaleYEnabled(true); //是否可以缩放 仅y轴
        binedChart.setPinchZoom(true);  //设置x轴和y轴能否同时缩放。默认是否
        binedChart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true

        binedChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        ArrayList<String> xVals = new ArrayList<>();


        for (int i = 0; i < 60; i++) {
            xVals.add(String.valueOf(dataEntities.get(i + 20).getDate()));
        }

        CombinedData data = new CombinedData(xVals);


        data.setData(generateLineData(dataEntities));
        data.setData(generateCandleData(dataEntities));

        binedChart.setData(data);
        binedChart.invalidate();


    }

    private void init(View view) {
        barChart = (BarChart) view.findViewById(R.id.barChar);
        combinedChart = (CombinedChart) view.findViewById(R.id.combinedChart);
        combinedChart.setViewPortOffsets(10, 20, 10, 10);
        barChart.setViewPortOffsets(10, 10, 10, 10);
    }

    /**
     * k线图
     *
     * @param data
     * @return
     */

    protected CandleData generateCandleData(List<FenSHIModule.SerialsEntity.DataEntity> data) {

        CandleData d = new CandleData();
        ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();

        switch (num) {
            //日k线
            case 16:
                for (int index = 0; index < 60; index++) {
                    if (hour < 15 && hour >= 9) {
                        float now = Float.parseFloat(data.get(index + 21).getNow());  //收盘
                        float open = Float.parseFloat(data.get(index + 21).getOpen()); //开盘

                        float high = Float.parseFloat(data.get(index + 21).getHigh()); //最高价
                        float low = Float.parseFloat(data.get(index + 21).getLow());   //最低价
                        entries.add(new CandleEntry(index, high, low, open, now));
                    } else {
                        float now = Float.parseFloat(data.get(index + 20).getNow());  //收盘
                        float open = Float.parseFloat(data.get(index + 20).getOpen()); //开盘

                        float high = Float.parseFloat(data.get(index + 20).getHigh()); //最高价
                        float low = Float.parseFloat(data.get(index + 20).getLow());   //最低价
                        entries.add(new CandleEntry(index, high, low, open, now));
                    }
                }
                break;
            //周k线
            case 17:
                for (int index = 0; index < 60; index++) {
                    float now = Float.parseFloat(data.get(index + 24).getNow());  //收盘
                    float open = Float.parseFloat(data.get(index + 24).getOpen()); //开盘

                    float high = Float.parseFloat(data.get(index + 24).getHigh()); //最高价
                    float low = Float.parseFloat(data.get(index + 24).getLow());   //最低价
                    entries.add(new CandleEntry(index, high, low, open, now));

                }
                break;
            //月k线
            case 18:
                for (int index = 0; index < 60; index++) {
                    float now = Float.parseFloat(data.get(index + 20).getNow());  //收盘
                    float open = Float.parseFloat(data.get(index + 20).getOpen()); //开盘

                    float high = Float.parseFloat(data.get(index + 20).getHigh()); //最高价
                    float low = Float.parseFloat(data.get(index + 20).getLow());   //最低价
                    entries.add(new CandleEntry(index, high, low, open, now));
                }
                break;
        }
        CandleDataSet set = new CandleDataSet(entries, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowColor(Color.DKGRAY);
        set.setShadowColorSameAsCandle(true);
        set.setShadowWidth(0.7f);
        set.setDecreasingColor(Color.GREEN);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(Color.RED);
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        d.addDataSet(set);
        d.setDrawValues(false);
        return d;
    }

    /**
     * 线性图
     *
     * @return
     */
    private LineData generateLineData(List<FenSHIModule.SerialsEntity.DataEntity> dataEntities) {

        LineData d = new LineData();

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<Entry> entries3 = new ArrayList<>();

        switch (num) {
            //日k均线
            case 16:
                if (hour < 15 && hour >= 9) {
                    for (int i = 0; i < 60; i++) {
                        float now = Float.parseFloat(dataEntities.get(i + 17).getNow());  //收盘
                        float now1 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                        float now2 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                        float now3 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                        float now4 = Float.parseFloat(dataEntities.get(i + 21).getNow());
                        float v = (now + now1 + now2 + now3 + now4) / 5;
                        entries1.add(new Entry(v, i));
                    }

                    for (int i = 0; i < 60; i++) {
                        float now = Float.parseFloat(dataEntities.get(i + 12).getNow());  //收盘
                        float now1 = Float.parseFloat(dataEntities.get(i + 13).getNow());
                        float now2 = Float.parseFloat(dataEntities.get(i + 14).getNow());
                        float now3 = Float.parseFloat(dataEntities.get(i + 15).getNow());
                        float now4 = Float.parseFloat(dataEntities.get(i + 16).getNow());
                        float now5 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                        float now6 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                        float now7 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                        float now8 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                        float now9 = Float.parseFloat(dataEntities.get(i + 21).getNow());
                        float v1 = (now + now1 + now2 + now3 + now4 + now5 + now6 + now7 + now8 + now9) / 10;
                        entries2.add(new Entry(v1, i));
                    }

                    for (int i = 0; i < 60; i++) {
                        float now = Float.parseFloat(dataEntities.get(i + 2).getNow());  //收盘
                        float now1 = Float.parseFloat(dataEntities.get(i + 3).getNow());
                        float now2 = Float.parseFloat(dataEntities.get(i + 4).getNow());
                        float now3 = Float.parseFloat(dataEntities.get(i + 5).getNow());
                        float now4 = Float.parseFloat(dataEntities.get(i + 6).getNow());
                        float now5 = Float.parseFloat(dataEntities.get(i + 7).getNow());
                        float now6 = Float.parseFloat(dataEntities.get(i + 8).getNow());
                        float now7 = Float.parseFloat(dataEntities.get(i + 9).getNow());
                        float now8 = Float.parseFloat(dataEntities.get(i + 10).getNow());
                        float now9 = Float.parseFloat(dataEntities.get(i + 11).getNow());
                        float now10 = Float.parseFloat(dataEntities.get(i + 12).getNow());
                        float now11 = Float.parseFloat(dataEntities.get(i + 13).getNow());
                        float now12 = Float.parseFloat(dataEntities.get(i + 14).getNow());
                        float now13 = Float.parseFloat(dataEntities.get(i + 15).getNow());
                        float now14 = Float.parseFloat(dataEntities.get(i + 16).getNow());
                        float now15 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                        float now16 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                        float now17 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                        float now18 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                        float now19 = Float.parseFloat(dataEntities.get(i + 21).getNow());

                        float v2 = (now + now1 + now2 + now3 + now4 + now5 + now6 + now7 + now8 + now9 + now10 + now11 + now12 + now13 + now14 + now15 + now16 + now17 + now18 + now19) / 20;
                        entries3.add(new Entry(v2, i));
                    }
                } else {
                    for (int i = 0; i < 60; i++) {
                        float now = Float.parseFloat(dataEntities.get(i + 16).getNow());  //收盘
                        float now1 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                        float now2 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                        float now3 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                        float now4 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                        float v = (now + now1 + now2 + now3 + now4) / 5;
                        entries1.add(new Entry(v, i));
                    }

                    for (int i = 0; i < 60; i++) {
                        float now = Float.parseFloat(dataEntities.get(i + 11).getNow());  //收盘
                        float now1 = Float.parseFloat(dataEntities.get(i + 12).getNow());
                        float now2 = Float.parseFloat(dataEntities.get(i + 13).getNow());
                        float now3 = Float.parseFloat(dataEntities.get(i + 14).getNow());
                        float now4 = Float.parseFloat(dataEntities.get(i + 15).getNow());
                        float now5 = Float.parseFloat(dataEntities.get(i + 16).getNow());
                        float now6 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                        float now7 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                        float now8 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                        float now9 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                        float v1 = (now + now1 + now2 + now3 + now4 + now5 + now6 + now7 + now8 + now9) / 10;
                        entries2.add(new Entry(v1, i));
                    }

                    for (int i = 0; i < 60; i++) {
                        float now = Float.parseFloat(dataEntities.get(i + 1).getNow());  //收盘
                        float now1 = Float.parseFloat(dataEntities.get(i + 2).getNow());
                        float now2 = Float.parseFloat(dataEntities.get(i + 3).getNow());
                        float now3 = Float.parseFloat(dataEntities.get(i + 4).getNow());
                        float now4 = Float.parseFloat(dataEntities.get(i + 5).getNow());
                        float now5 = Float.parseFloat(dataEntities.get(i + 6).getNow());
                        float now6 = Float.parseFloat(dataEntities.get(i + 7).getNow());
                        float now7 = Float.parseFloat(dataEntities.get(i + 8).getNow());
                        float now8 = Float.parseFloat(dataEntities.get(i + 9).getNow());
                        float now9 = Float.parseFloat(dataEntities.get(i + 10).getNow());
                        float now10 = Float.parseFloat(dataEntities.get(i + 11).getNow());
                        float now11 = Float.parseFloat(dataEntities.get(i + 12).getNow());
                        float now12 = Float.parseFloat(dataEntities.get(i + 13).getNow());
                        float now13 = Float.parseFloat(dataEntities.get(i + 14).getNow());
                        float now14 = Float.parseFloat(dataEntities.get(i + 15).getNow());
                        float now15 = Float.parseFloat(dataEntities.get(i + 16).getNow());
                        float now16 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                        float now17 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                        float now18 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                        float now19 = Float.parseFloat(dataEntities.get(i + 20).getNow());

                        float v2 = (now + now1 + now2 + now3 + now4 + now5 + now6 + now7 + now8 + now9 + now10 + now11 + now12 + now13 + now14 + now15 + now16 + now17 + now18 + now19) / 20;
                        entries3.add(new Entry(v2, i));
                    }
                }

                break;
            //周k均线
            case 17:

                for (int i = 0; i < 60; i++) {
                    float now = Float.parseFloat(dataEntities.get(i + 20).getNow());  //收盘
                    float now1 = Float.parseFloat(dataEntities.get(i + 21).getNow());
                    float now2 = Float.parseFloat(dataEntities.get(i + 22).getNow());
                    float now3 = Float.parseFloat(dataEntities.get(i + 23).getNow());
                    float now4 = Float.parseFloat(dataEntities.get(i + 24).getNow());
                    float v = (now + now1 + now2 + now3 + now4) / 5;
                    entries1.add(new Entry(v, i));
                }

                for (int i = 0; i < 60; i++) {
                    float now = Float.parseFloat(dataEntities.get(i + 15).getNow());  //收盘
                    float now1 = Float.parseFloat(dataEntities.get(i + 16).getNow());
                    float now2 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                    float now3 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                    float now4 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                    float now5 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                    float now6 = Float.parseFloat(dataEntities.get(i + 21).getNow());
                    float now7 = Float.parseFloat(dataEntities.get(i + 22).getNow());
                    float now8 = Float.parseFloat(dataEntities.get(i + 23).getNow());
                    float now9 = Float.parseFloat(dataEntities.get(i + 24).getNow());
                    float v1 = (now + now1 + now2 + now3 + now4 + now5 + now6 + now7 + now8 + now9) / 10;
                    entries2.add(new Entry(v1, i));
                }

                for (int i = 0; i < 60; i++) {
                    float now = Float.parseFloat(dataEntities.get(i + 5).getNow());  //收盘
                    float now1 = Float.parseFloat(dataEntities.get(i + 6).getNow());
                    float now2 = Float.parseFloat(dataEntities.get(i + 7).getNow());
                    float now3 = Float.parseFloat(dataEntities.get(i + 8).getNow());
                    float now4 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                    float now5 = Float.parseFloat(dataEntities.get(i + 10).getNow());
                    float now6 = Float.parseFloat(dataEntities.get(i + 11).getNow());
                    float now7 = Float.parseFloat(dataEntities.get(i + 12).getNow());
                    float now8 = Float.parseFloat(dataEntities.get(i + 13).getNow());
                    float now9 = Float.parseFloat(dataEntities.get(i + 14).getNow());
                    float now10 = Float.parseFloat(dataEntities.get(i + 15).getNow());
                    float now11 = Float.parseFloat(dataEntities.get(i + 16).getNow());
                    float now12 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                    float now13 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                    float now14 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                    float now15 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                    float now16 = Float.parseFloat(dataEntities.get(i + 21).getNow());
                    float now17 = Float.parseFloat(dataEntities.get(i + 22).getNow());
                    float now18 = Float.parseFloat(dataEntities.get(i + 23).getNow());
                    float now19 = Float.parseFloat(dataEntities.get(i + 24).getNow());

                    float v2 = (now + now1 + now2 + now3 + now4 + now5 + now6 + now7 + now8 + now9 + now10 + now11 + now12 + now13 + now14 + now15 + now16 + now17 + now18 + now19) / 20;
                    entries3.add(new Entry(v2, i));
                }

                break;
            //月k均线
            case 18:

                for (int i = 0; i < 60; i++) {
                    float now = Float.parseFloat(dataEntities.get(i + 16).getNow());  //收盘
                    float now1 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                    float now2 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                    float now3 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                    float now4 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                    float v = (now + now1 + now2 + now3 + now4) / 5;
                    entries1.add(new Entry(v, i));
                }

                for (int i = 0; i < 60; i++) {
                    float now = Float.parseFloat(dataEntities.get(i + 11).getNow());  //收盘
                    float now1 = Float.parseFloat(dataEntities.get(i + 12).getNow());
                    float now2 = Float.parseFloat(dataEntities.get(i + 13).getNow());
                    float now3 = Float.parseFloat(dataEntities.get(i + 14).getNow());
                    float now4 = Float.parseFloat(dataEntities.get(i + 15).getNow());
                    float now5 = Float.parseFloat(dataEntities.get(i + 16).getNow());
                    float now6 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                    float now7 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                    float now8 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                    float now9 = Float.parseFloat(dataEntities.get(i + 20).getNow());
                    float v1 = (now + now1 + now2 + now3 + now4 + now5 + now6 + now7 + now8 + now9) / 10;
                    entries2.add(new Entry(v1, i));
                }

                for (int i = 0; i < 60; i++) {
                    float now = Float.parseFloat(dataEntities.get(i + 1).getNow());  //收盘
                    float now1 = Float.parseFloat(dataEntities.get(i + 2).getNow());
                    float now2 = Float.parseFloat(dataEntities.get(i + 3).getNow());
                    float now3 = Float.parseFloat(dataEntities.get(i + 4).getNow());
                    float now4 = Float.parseFloat(dataEntities.get(i + 5).getNow());
                    float now5 = Float.parseFloat(dataEntities.get(i + 6).getNow());
                    float now6 = Float.parseFloat(dataEntities.get(i + 7).getNow());
                    float now7 = Float.parseFloat(dataEntities.get(i + 8).getNow());
                    float now8 = Float.parseFloat(dataEntities.get(i + 9).getNow());
                    float now9 = Float.parseFloat(dataEntities.get(i + 10).getNow());
                    float now10 = Float.parseFloat(dataEntities.get(i + 11).getNow());
                    float now11 = Float.parseFloat(dataEntities.get(i + 12).getNow());
                    float now12 = Float.parseFloat(dataEntities.get(i + 13).getNow());
                    float now13 = Float.parseFloat(dataEntities.get(i + 14).getNow());
                    float now14 = Float.parseFloat(dataEntities.get(i + 15).getNow());
                    float now15 = Float.parseFloat(dataEntities.get(i + 16).getNow());
                    float now16 = Float.parseFloat(dataEntities.get(i + 17).getNow());
                    float now17 = Float.parseFloat(dataEntities.get(i + 18).getNow());
                    float now18 = Float.parseFloat(dataEntities.get(i + 19).getNow());
                    float now19 = Float.parseFloat(dataEntities.get(i + 20).getNow());

                    float v2 = (now + now1 + now2 + now3 + now4 + now5 + now6 + now7 + now8 + now9 + now10 + now11 + now12 + now13 + now14 + now15 + now16 + now17 + now18 + now19) / 20;
                    entries3.add(new Entry(v2, i));
                }
                break;
        }


        LineDataSet lineDataSet1 = new LineDataSet(entries1, "");
        LineDataSet lineDataSet2 = new LineDataSet(entries2, "");
        LineDataSet lineDataSet3 = new LineDataSet(entries3, "");


        /**
         * 5日
         */
        lineDataSet1.setDrawCubic(true);
        lineDataSet1.setFillAlpha(110);
        //用y轴的集合来设置参数
        lineDataSet1.setLineWidth(0.4f); // 线宽
        lineDataSet1.setDrawCubic(false);
        lineDataSet1.setCubicIntensity(0.2f);
        lineDataSet1.setColor(Color.WHITE);
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet1.setDrawCircles(false);

        /**
         * 10日
         */
        lineDataSet2.setDrawCubic(true);
        lineDataSet2.setFillAlpha(110);
        //用y轴的集合来设置参数
        lineDataSet2.setLineWidth(0.4f); // 线宽
        lineDataSet2.setDrawCubic(false);
        lineDataSet2.setCubicIntensity(0.2f);
        lineDataSet2.setColor(Color.YELLOW);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setDrawCircles(false);

        /**
         * 20日
         */
        lineDataSet3.setDrawCubic(true);
        lineDataSet3.setFillAlpha(110);
        //用y轴的集合来设置参数
        lineDataSet3.setLineWidth(0.4f); // 线宽
        lineDataSet3.setDrawCubic(false);
        lineDataSet3.setCubicIntensity(0.2f);
        lineDataSet3.setColor(Color.BLUE);
        lineDataSet3.setDrawCircleHole(false);
        lineDataSet3.setDrawCircles(false);

        d.addDataSet(lineDataSet1);
        d.addDataSet(lineDataSet2);
        d.addDataSet(lineDataSet3);
        d.setDrawValues(false);
        return d;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        barChart = null;
        combinedChart = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (handler != null) {
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            }
        }.start();
    }
}
