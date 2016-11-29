package com.bigdata.marketsdk.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.module.FenSHIModule;
import com.bigdata.marketsdk.weight.LoadDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 五日
 * Created by windows on 2016/1/20.
 */
public class WuRiFragment extends Fragment {

    public static final String TAG = "WuRiFragment";


    private LineChart[] lineCharts = new LineChart[5];
    private BarChart[] barCharts = new BarChart[5];
    private TextView[] dates = new TextView[5];

    private String code;
    private Gson gson;

    private String high, low, prevClose, min, max;
    private List<FenSHIModule.SerialsEntity.DataEntity> dataEntities;

    int time;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (hour < 15 && hour >= 9) {
                        loding();
                    }
                    break;
            }
        }
    };
    private Calendar c = Calendar.getInstance();
    private int hour = c.get(Calendar.HOUR_OF_DAY);

    public static final int count = 243;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadDialog.show(getActivity());
        code = (String) getArguments().get("code");
        gson = new Gson();
        dataEntities = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wuri, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        init(view);
        loding();

    }

    private void loding() {
        RxVolley.get(Api.FENSHIBASE + code + Api.FENSHI_03, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                prevClose = fenSHIModule.getPrevClose();
                dataEntities = fenSHIModule.getSerials().getData();
                min = fenSHIModule.getLow();
                max = fenSHIModule.getHigh();

                if (dataEntities != null) {
                    LoadDialog.dismiss(getActivity());
                }

                high = dataEntities.get(0).getNow();
                low = dataEntities.get(0).getNow();
                for (int i = 0; i < dataEntities.size(); i++) {
                    if (Double.parseDouble(low) > Double.parseDouble(dataEntities.get(i).getNow())) {
                        low = dataEntities.get(i).getNow();
                    }

                    if (Double.parseDouble(high) < Double.parseDouble(dataEntities.get(i).getNow())) {
                        high = dataEntities.get(i).getNow();
                    }
                }

                Iterator<FenSHIModule.SerialsEntity.DataEntity> iterator = dataEntities.iterator();
                while (iterator.hasNext()) {
                    FenSHIModule.SerialsEntity.DataEntity next = iterator.next();
                    if (Integer.parseInt(next.getTime()) < 93057000) {
                        iterator.remove();
                    }
                }

                final Map<Integer, List<FenSHIModule.SerialsEntity.DataEntity>> map = Group.group(dataEntities, new Group.GroupBy<Integer>() {
                    @Override
                    public Integer groupby(Object obj) {
                        FenSHIModule.SerialsEntity.DataEntity dataEntity = (FenSHIModule.SerialsEntity.DataEntity) obj;
                        return Integer.parseInt(dataEntity.getDate());//分组依据date
                    }
                });
                final List<Integer> day = new ArrayList<>();
                Set<Integer> integers = map.keySet();
                Iterator<Integer> iterator1 = integers.iterator();
                while (iterator1.hasNext()) {
                    day.add(iterator1.next());
                }
                Collections.sort(day);
                if (day.size() > 0) {
                    switch (day.size()) {
                        case 1:
                            showChart(lineCharts[4], getLineData(map.get(day.get(0))), Color.alpha(1000));
                            showBarChart(barCharts[4], getBarData(map.get(day.get(0))), Color.alpha(1000));
                            dates[4].setText(String.valueOf(day.get(0)) + "");
                            break;
                        case 2:
                            showChart(lineCharts[3], getLineData(map.get(day.get(0))), Color.alpha(1000));
                            showChart(lineCharts[4], getLineData(map.get(day.get(1))), Color.alpha(1000));

                            showBarChart(barCharts[3], getBarData(map.get(day.get(0))), Color.alpha(1000));
                            showBarChart(barCharts[4], getBarData(map.get(day.get(1))), Color.alpha(1000));

                            dates[3].setText(String.valueOf(day.get(0)) + "");
                            dates[4].setText(String.valueOf(day.get(1)) + "");
                            break;
                        case 3:
                            showChart(lineCharts[2], getLineData(map.get(day.get(0))), Color.alpha(1000));
                            showChart(lineCharts[3], getLineData(map.get(day.get(1))), Color.alpha(1000));
                            showChart(lineCharts[4], getLineData(map.get(day.get(2))), Color.alpha(1000));

                            showBarChart(barCharts[2], getBarData(map.get(day.get(0))), Color.alpha(1000));
                            showBarChart(barCharts[3], getBarData(map.get(day.get(1))), Color.alpha(1000));
                            showBarChart(barCharts[4], getBarData(map.get(day.get(2))), Color.alpha(1000));

                            dates[2].setText(String.valueOf(day.get(0)) + "");
                            dates[3].setText(String.valueOf(day.get(1)) + "");
                            dates[4].setText(String.valueOf(day.get(2)) + "");
                            break;
                        case 4:
                            showChart(lineCharts[1], getLineData(map.get(day.get(0))), Color.alpha(1000));
                            showChart(lineCharts[2], getLineData(map.get(day.get(1))), Color.alpha(1000));
                            showChart(lineCharts[3], getLineData(map.get(day.get(2))), Color.alpha(1000));
                            showChart(lineCharts[4], getLineData(map.get(day.get(3))), Color.alpha(1000));

                            showBarChart(barCharts[1], getBarData(map.get(day.get(0))), Color.alpha(1000));
                            showBarChart(barCharts[2], getBarData(map.get(day.get(1))), Color.alpha(1000));
                            showBarChart(barCharts[3], getBarData(map.get(day.get(2))), Color.alpha(1000));
                            showBarChart(barCharts[4], getBarData(map.get(day.get(3))), Color.alpha(1000));

                            dates[1].setText(String.valueOf(day.get(0)) + "");
                            dates[2].setText(String.valueOf(day.get(1)) + "");
                            dates[3].setText(String.valueOf(day.get(2)) + "");
                            dates[4].setText(String.valueOf(day.get(3)) + "");
                            break;
                        case 5:
                            showChart(lineCharts[0], getLineData(map.get(day.get(0))), Color.alpha(1000));
                            showChart(lineCharts[1], getLineData(map.get(day.get(1))), Color.alpha(1000));
                            showChart(lineCharts[2], getLineData(map.get(day.get(2))), Color.alpha(1000));
                            showChart(lineCharts[3], getLineData(map.get(day.get(3))), Color.alpha(1000));
                            showChart(lineCharts[4], getLineData(map.get(day.get(4))), Color.alpha(1000));

                            showBarChart(barCharts[0], getBarData(map.get(day.get(0))), Color.alpha(1000));
                            showBarChart(barCharts[1], getBarData(map.get(day.get(1))), Color.alpha(1000));
                            showBarChart(barCharts[2], getBarData(map.get(day.get(2))), Color.alpha(1000));
                            showBarChart(barCharts[3], getBarData(map.get(day.get(3))), Color.alpha(1000));
                            showBarChart(barCharts[4], getBarData(map.get(day.get(4))), Color.alpha(1000));

                            dates[0].setText(String.valueOf(day.get(0)) + "");
                            dates[1].setText(String.valueOf(day.get(1)) + "");
                            dates[2].setText(String.valueOf(day.get(2)) + "");
                            dates[3].setText(String.valueOf(day.get(3)) + "");
                            dates[4].setText(String.valueOf(day.get(4)) + "");
                            break;
                    }
                } else {
                    return;
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
        xAxis.setLabelsToSkip(240);
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
        for (int i = 0; i < count; i++) {
            xValues.add(String.valueOf(i));
        }

        for (int i = 1; i < dataEntities.size(); i++) {

            float volume = Float.parseFloat(dataEntities.get(i).getVolume()) - Float.parseFloat(dataEntities.get(i - 1).getVolume());
            yValues.add(new BarEntry(volume, i));
        }
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        BarData barData = new BarData(xValues, barDataSet);
        barData.setDrawValues(false);
        barDataSet.setColor(Color.YELLOW);
        return barData;

    }

    private void showChart(LineChart lineChart, LineData lineData, int color) {

        if (lineChart == null) {
            return;
        }

        lineChart.setBackgroundColor(color);  //背景色

        lineChart.setDescription(""); //图表默认右下方的描述，参数是String对象
        lineChart.setNoDataTextDescription("没有数据呢(⊙o⊙)");   //没有数据时显示在中央的字符串，参数是String对象

        lineChart.setDrawGridBackground(false);//设置图表内格子背景是否显示，默认是false
        lineChart.setGridBackgroundColor(color);//设置格子背景色,参数是Color类型对象

        lineChart.setDrawBorders(true);//设置图表内格子外的边框是否显示
        lineChart.setBorderColor(Color.WHITE);
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisLeft();
        LimitLine ll1 = new LimitLine(Float.parseFloat(prevClose), "");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(1f, 1f, 0f);
        ll1.setLineColor(Color.WHITE);
        yAxis.addLimitLine(ll1);
        xAxis.setDrawLabels(true);
        xAxis.resetLabelsToSkip();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// 将X坐标轴放置在  底部，默认是在顶部。
        xAxis.setLabelsToSkip(240);
        yAxis.setDrawGridLines(true);//是否显示Y坐标轴上的刻度竖线，默认是true
        yAxis.setShowOnlyMinMax(true);    //参数如果为true Y轴坐标只显示最大值和最小值
        yAxis.setTextColor(Color.WHITE);
        yAxis.setStartAtZero(false);//不从0开始

//        if (Float.parseFloat(prevClose) < Float.parseFloat(low)) {
            yAxis.setAxisMaxValue(Float.parseFloat(high) + 1);    //设置Y轴坐标最大为多少
            yAxis.setAxisMinValue(Float.parseFloat(prevClose) - (Float.parseFloat(high) + 1 - Float.parseFloat(prevClose)));    //设置Y轴坐标最小为多少
//        } else if{
//            yAxis.setAxisMaxValue(((Float.parseFloat(prevClose) - Float.parseFloat(low)) + Float.parseFloat(prevClose)));    //设置Y轴坐标最大为多少
//            yAxis.setAxisMinValue(Float.parseFloat(low));    //设置Y轴坐标最小为多少
//        }
        yAxis.setLabelCount(5, false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);  //参数是INSIDE_CHART(Y轴坐标在内部) 或 OUTSIDE_CHART(在外部（默认是这个）)

        YAxis rightAxis = lineChart.getAxisRight();

        rightAxis.setDrawLabels(false);// 不显示图表的右边y坐标轴线
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setShowOnlyMinMax(true);
        rightAxis.setStartAtZero(false);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setDrawGridLines(false);


        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        lineChart.setDragEnabled(true);// 是否可以拖拽

        lineChart.setScaleEnabled(true);// 是否可以缩放 x和y轴, 默认是true
        lineChart.setScaleXEnabled(true); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(true); //是否可以缩放 仅y轴
        lineChart.setPinchZoom(true);  //设置x轴和y轴能否同时缩放。默认是否
        lineChart.setDoubleTapToZoomEnabled(true);//设置是否可以通过双击屏幕放大图表。默认是true

        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true

        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.notifyDataSetChanged();
    }

    private LineData getLineData(final List<FenSHIModule.SerialsEntity.DataEntity> dataEntities) {

        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            xValues.add(String.valueOf(i));
        }
        ArrayList<Entry> yValuesm = new ArrayList<>();
        ArrayList<Entry> yValues2 = new ArrayList<>();
        LineDataSet lineDataSet1 = null;
        LineDataSet lineDataSet2 = null;

        for (int i = 0; i < dataEntities.size(); i++) {
            yValuesm.add(new Entry(Float.parseFloat(dataEntities.get(i).getNow()), i));
            float amount = Float.parseFloat(dataEntities.get(i).getAmount());
            float volume = Float.parseFloat(dataEntities.get(i).getVolume());
            float i3 = amount / volume;
            yValues2.add(new Entry(i3, i));
        }


        lineDataSet1 = new LineDataSet(yValuesm, "");
        lineDataSet1.setDrawCubic(true);
        lineDataSet1.setFillAlpha(110);
        //用y轴的集合来设置参数
        lineDataSet1.setLineWidth(0.8f); // 线宽
        lineDataSet1.setDrawCubic(true);
        lineDataSet1.setCubicIntensity(0.2f);
        lineDataSet1.setColor(Color.rgb(51, 161, 201));
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);     //以左边坐标轴为准 还是以右边坐标轴为基准
        lineDataSet1.setFillColor(Color.argb(0, 0, 0, 0));
        lineDataSet1.setFillAlpha(100);
        lineDataSet1.setDrawFilled(true);


        lineDataSet2 = new LineDataSet(yValues2, "");


        lineDataSet2.setDrawCubic(true);
        lineDataSet2.setFillAlpha(110);
        //用y轴的集合来设置参数
        lineDataSet2.setLineWidth(0.4f); // 线宽
        lineDataSet2.setDrawCubic(false);
        lineDataSet2.setCubicIntensity(0.2f);
        lineDataSet2.setColor(Color.YELLOW);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setDrawCircles(false);

        ArrayList<ILineDataSet> listLine = new ArrayList<>();
        listLine.add(lineDataSet1);
        listLine.add(lineDataSet2);
        LineData lineData = new LineData(xValues, listLine);
        lineData.setDrawValues(false);

        return lineData;

    }

    private void init(View view) {
        lineCharts[0] = (LineChart) view.findViewById(R.id.lineChar01);
        lineCharts[1] = (LineChart) view.findViewById(R.id.lineChar02);
        lineCharts[2] = (LineChart) view.findViewById(R.id.lineChar03);
        lineCharts[3] = (LineChart) view.findViewById(R.id.lineChar04);
        lineCharts[4] = (LineChart) view.findViewById(R.id.lineChar05);
        lineCharts[0].setViewPortOffsets(10, 20, 0, 10);
        lineCharts[1].setViewPortOffsets(0, 20, 0, 10);
        lineCharts[2].setViewPortOffsets(0, 20, 0, 10);
        lineCharts[3].setViewPortOffsets(0, 20, 0, 10);
        lineCharts[4].setViewPortOffsets(0, 20, 10, 10);
        lineCharts[1].getAxisLeft().setDrawLabels(false);
        lineCharts[2].getAxisLeft().setDrawLabels(false);
        lineCharts[3].getAxisLeft().setDrawLabels(false);
        lineCharts[4].getAxisLeft().setDrawLabels(false);


        barCharts[0] = (BarChart) view.findViewById(R.id.barchart01);
        barCharts[1] = (BarChart) view.findViewById(R.id.barchart02);
        barCharts[2] = (BarChart) view.findViewById(R.id.barchart03);
        barCharts[3] = (BarChart) view.findViewById(R.id.barchart04);
        barCharts[4] = (BarChart) view.findViewById(R.id.barchart05);
        barCharts[0].setViewPortOffsets(10, 10, 0, 10);
        barCharts[1].setViewPortOffsets(0, 10, 0, 10);
        barCharts[2].setViewPortOffsets(0, 10, 0, 10);
        barCharts[3].setViewPortOffsets(0, 10, 0, 10);
        barCharts[4].setViewPortOffsets(0, 10, 10, 10);

        dates[0] = (TextView) view.findViewById(R.id.date01);
        dates[1] = (TextView) view.findViewById(R.id.date02);
        dates[2] = (TextView) view.findViewById(R.id.date03);
        dates[3] = (TextView) view.findViewById(R.id.date04);
        dates[4] = (TextView) view.findViewById(R.id.date05);


    }


    /**
     * 分组
     */

    static class Group {
        public interface GroupBy<T> {
            T groupby(Object obj);
        }

        /**
         * @param colls
         * @param gb
         * @return
         */
        public static final <T extends Comparable<T>, D> Map<T, List<D>> group(Collection<D> colls, GroupBy<T> gb) {
            if (colls == null || colls.isEmpty()) {
                System.out.println("分組集合不能為空!");
                return null;
            }
            if (gb == null) {
                System.out.println("分組依據接口不能為Null!");
                return null;
            }
            Iterator<D> iter = colls.iterator();
            Map<T, List<D>> map = new HashMap<T, List<D>>();
            while (iter.hasNext()) {
                D d = iter.next();
                T t = gb.groupby(d);
                if (map.containsKey(t)) {
                    map.get(t).add(d);
                } else {
                    List<D> list = new ArrayList<D>();
                    list.add(d);
                    map.put(t, list);
                }
            }
            return map;
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < 5; i++) {

            barCharts[i] = null;
            lineCharts[i] = null;
//            barCharts[i].clear();
//            lineCharts[i].clear();
//
//            lineCharts[i].destroyDrawingCache();
//            barCharts[i].destroyDrawingCache();
        }
    }
}
