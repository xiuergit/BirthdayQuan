package com.bigdata.marketsdk.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bigdata.marketsdk.Adapter.AbsAdapter;
import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.base.BaseFragment;
import com.bigdata.marketsdk.module.FenSHIModule;
import com.bigdata.marketsdk.weight.LoadDialog;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * user:kun
 * Date:2016/10/25 or 下午2:29
 * email:hekun@gamil.com
 * Desc: 行情首页
 */


public class HomeFragment extends BaseFragment {

    private GridView gridView;

    private static final int count = 243;

    private LineChart mLineChart;//线性线


    private List<FenSHIModule> itemsModels;

    private CandleStickChart stickChart;//k线

    private Map<Integer, String> codes;

    private SwipeRefreshLayout refreshLayout;

    private Gson gson;

    private FenSHIModule fenSHIModule;


    private Calendar c = Calendar.getInstance();
    private int hour = c.get(Calendar.HOUR_OF_DAY);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (hour < 15 && hour >= 9) {
                        itemsModels.clear();
                        loading();
                    }
                    break;
            }
        }
    };

    private AbsAdapter<FenSHIModule> adapter;

    @SuppressLint("UseSparseArrays")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        LoadDialog.show(getActivity());
        itemsModels = new ArrayList<>();
        codes = new HashMap<>();
        codes.put(0, "000001.SHI");
        codes.put(1, "399001.SZI");
        codes.put(2, "399006.SZI");
        codes.put(3, "399005.SZI");
        codes.put(4, "000016.SHI");
        codes.put(5, "000300.CSI");

        adapter = new AbsAdapter<FenSHIModule>(getContext(), itemsModels, R.layout.item_recylerview) {
            @Override
            public void showData(ViewHoder vHolder, FenSHIModule data, int position) {


                final LinearLayout view01 = vHolder.getView(R.id.layout_item_01);
                final LinearLayout view02 = vHolder.getView(R.id.layout_item_02);


                mLineChart = vHolder.getView(R.id.linechart_01_1);

                mLineChart.setViewPortOffsets(10, 10, 10, 10);
                /**
                 * 线性图
                 */

                Iterator<FenSHIModule.SerialsEntity.DataEntity> iterator = data.getSerials().getData().iterator();
                while (iterator.hasNext()) {
                    FenSHIModule.SerialsEntity.DataEntity next = iterator.next();
                    if (Integer.parseInt(next.getTime()) < 93057000) {
                        iterator.remove();
                    }
                }

                List<FenSHIModule.SerialsEntity.DataEntity> data1 = data.getSerials().getData();
                showChart(mLineChart, getLineData(data1), Color.alpha(1000));

                showCandleChart(stickChart, generateCandleData(data1), Color.alpha(1000));

                RelativeLayout layout = vHolder.getView(R.id.title_layout_01_1);

                if (data.getNow() != null && data.getPrevClose() != null) {
                    if (Double.parseDouble(data.getNow()) > Double.parseDouble(data.getPrevClose())) {
                        layout.setBackgroundColor(Color.RED);
                    } else {
                        layout.setBackgroundColor(Color.GREEN);
                    }
                }
                //股票名称
                if (data.getName() != null) {
                    vHolder.setText(R.id.title_01_1, data.getName());
                }

                //股票代码
                if (data.getCode() != null) {
                    vHolder.setText(R.id.code_01_1, data.getCode());
                }

                //开盘价
                if (data.getOpen() != null) {
                    vHolder.setText(R.id.open_01_1, String.format("%.2f", Double.parseDouble(data.getOpen())));
                }


                //成交量
                if (data.getVolume() != null) {
                    if (Double.parseDouble(data.getVolume()) > 10000) {
                        double v = Double.parseDouble(data.getVolume()) / 1000000;
                        vHolder.setText(R.id.cheng_jl_01_1, String.format("%.2f", v) + "万");
                    }
                }
                //比值
                double change = 0;
                double changeRange = 0;
                if (data.getChange() != null && data.getChangeRange() != null) {
                    change = Double.parseDouble(data.getChange());
                    changeRange = Double.parseDouble(data.getChangeRange());

                }
                if (data.getNow() != null && data.getPrevClose() != null) {
                    if (Double.parseDouble(data.getNow()) > Double.parseDouble(data.getPrevClose())) {
                        vHolder.setText(R.id.zengl_01_1, String.format("%.2f", change));
                    } else {
                        vHolder.setText(R.id.zengl_01_1, String.format("%.2f", change));
                    }
                    if (Double.parseDouble(data.getNow()) > Double.parseDouble(data.getPrevClose())) {
                        vHolder.setText(R.id.bizhi_01_1, String.format("%.2f", changeRange * 100) + "%");
                    } else {
                        vHolder.setText(R.id.bizhi_01_1, String.format("%.2f", changeRange * 100) + "%");
                    }
                    LinearLayout layout2 = vHolder.getView(R.id.title_layout_01_2);
                    if (Double.parseDouble(data.getNow()) > Double.parseDouble(data.getPrevClose())) {
                        layout2.setBackgroundColor(Color.RED);
                    } else {
                        layout2.setBackgroundColor(Color.GREEN);
                    }

                    vHolder.setText(R.id.now_01_1, String.format("%.2f", Double.parseDouble(data.getNow())));

                }


                /*
                    K线
                 */


                stickChart = vHolder.getView(R.id.candleChart_01);



                //股票名称
                vHolder.setText(R.id.title_01_2, data.getName());


                if (data.getChange() != null && data.getChangeRange() != null) {
                    double change_02 = Double.parseDouble(data.getChange());
                    double changeRange_02 = Double.parseDouble(data.getChangeRange());
                    vHolder.setText(R.id.now_01_2, String.format("%.2f", Double.parseDouble(data.getNow())));
                    if (Double.parseDouble(data.getNow()) > Double.parseDouble(data.getPrevClose())) {
                        vHolder.setText(R.id.zengl_01_2, String.format("%.2f", change_02));
                    } else {
                        vHolder.setText(R.id.zengl_01_2, String.format("%.2f", change_02));
                    }
                    if (Double.parseDouble(data.getNow()) > Double.parseDouble(data.getPrevClose())) {
                        vHolder.setText(R.id.bizhi_01_2, String.format("%.2f", changeRange_02 * 100) + "%");
                    } else {
                        vHolder.setText(R.id.bizhi_01_2, String.format("%.2f", changeRange_02 * 100) + "%");
                    }


                }


                view01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ObjectAnimator.ofFloat(view01, "rotationX", 0.0F, 360.0F)
                                .setDuration(1000)
                                .start();
                        view01.setVisibility(View.GONE);
                        view02.setVisibility(View.VISIBLE);

                    }
                });

                view02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ObjectAnimator.ofFloat(view02, "rotationX", 0.0F, 360.0F)
                                .setDuration(1000)
                                .start();
                        view01.setVisibility(View.VISIBLE);
                        view02.setVisibility(View.GONE);
                    }
                });

            }


        };
    }

    private void showChart(LineChart lineChart, LineData lineData, int color) {
        if (lineChart == null) {
            return;
        }

        lineChart.setBackgroundColor(color);  //背景色

        lineChart.setDrawGridBackground(false);//设置图表内格子背景是否显示，默认是false
        lineChart.setGridBackgroundColor(color);//设置格子背景色,参数是Color类型对象
        lineChart.setDescription("");
        lineChart.setDrawBorders(false);//设置图表内格子外的边框是否显示
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisLeft();
        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// 将X坐标轴放置在底部，默认是在顶部。
        xAxis.setDrawGridLines(false);


        yAxis.setDrawLabels(false);

        yAxis.setDrawGridLines(false);//是否显示Y坐标轴上的刻度竖线，默认是true
        yAxis.setTextColor(Color.WHITE);
        yAxis.setStartAtZero(false);//不从0开始
        yAxis.setEnabled(false);
        xAxis.setEnabled(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);  //参数是INSIDE_CHART(Y轴坐标在内部) 或 OUTSIDE_CHART(在外部（默认是这个）)

        YAxis rightAxis = lineChart.getAxisRight();

        rightAxis.setDrawLabels(false);// 不显示图表的右边y坐标轴线
        rightAxis.setStartAtZero(false);
        rightAxis.setDrawGridLines(false);

        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(false); // 设置是否可以触摸
        lineChart.setDragEnabled(false);// 是否可以拖拽

        lineChart.setScaleEnabled(false);// 是否可以缩放 x和y轴, 默认是true
        lineChart.setScaleXEnabled(false); //是否可以缩放 仅x轴
        lineChart.setScaleYEnabled(false); //是否可以缩放 仅y轴
        lineChart.setPinchZoom(false);  //设置x轴和y轴能否同时缩放。默认是否
        lineChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true

        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true

        lineChart.setData(lineData);
        lineChart.invalidate();
        lineChart.notifyDataSetChanged();

    }


    private LineData getLineData(List<FenSHIModule.SerialsEntity.DataEntity> data) {


        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            xValues.add(String.valueOf(i));
        }
        ArrayList<Entry> yValues1 = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            yValues1.add(new Entry(Float.parseFloat(data.get(i).getNow()), i));
        }

        LineDataSet lineDataSet1 = new LineDataSet(yValues1, "");
        lineDataSet1.setDrawCubic(true);
        lineDataSet1.setFillAlpha(110);
        //用y轴的集合来设置参数
        lineDataSet1.setLineWidth(0.8f); // 线宽
        lineDataSet1.setDrawCubic(true);
        lineDataSet1.setCubicIntensity(0.2f);
        lineDataSet1.setColor(Color.rgb(250, 140, 53));
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);     //以左边坐标轴为准 还是以右边坐标轴为基准
        lineDataSet1.setFillColor(Color.rgb(200, 155, 64));
        lineDataSet1.setFillAlpha(100);
        lineDataSet1.setDrawFilled(true);

        LineData lineData = new LineData(xValues, lineDataSet1);
        lineData.setDrawValues(false);

        return lineData;
    }

    private void showCandleChart(CandleStickChart stickChart, CandleData candleData, int color) {
        if (stickChart == null) {
            return;
        }

        stickChart.setBackgroundColor(color);  //背景色

        stickChart.setDescription(""); //图表默认右下方的描述，参数是String对象
        stickChart.setNoDataTextDescription("没有数据呢(⊙o⊙)");   //没有数据时显示在中央的字符串，参数是String对象

        stickChart.setDrawGridBackground(false);//设置图表内格子背景是否显示，默认是false
        stickChart.setGridBackgroundColor(color);//设置格子背景色,参数是Color类型对象

        stickChart.setDrawBorders(false);//设置图表内格子外的边框是否显示
        XAxis xAxis = stickChart.getXAxis();
        YAxis yAxis = stickChart.getAxisLeft();
        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// 将X坐标轴放置在底部，默认是在顶部。
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(60);

        yAxis.setDrawLabels(false);

        yAxis.setDrawGridLines(false);//是否显示Y坐标轴上的刻度竖线，默认是true
        yAxis.setTextColor(Color.WHITE);
        yAxis.setStartAtZero(false);//不从0开始
        yAxis.setEnabled(false);
        xAxis.setEnabled(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);  //参数是INSIDE_CHART(Y轴坐标在内部) 或 OUTSIDE_CHART(在外部（默认是这个）)

        YAxis rightAxis = stickChart.getAxisRight();

        rightAxis.setDrawLabels(false);// 不显示图表的右边y坐标轴线
        rightAxis.setStartAtZero(false);
        rightAxis.setDrawGridLines(false);

        stickChart.getLegend().setEnabled(false);
        stickChart.setTouchEnabled(false); // 设置是否可以触摸
        stickChart.setDragEnabled(false);// 是否可以拖拽

        stickChart.setScaleEnabled(false);// 是否可以缩放 x和y轴, 默认是true
        stickChart.setScaleXEnabled(false); //是否可以缩放 仅x轴
        stickChart.setScaleYEnabled(false); //是否可以缩放 仅y轴
        stickChart.setPinchZoom(false);  //设置x轴和y轴能否同时缩放。默认是否
        stickChart.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true

        stickChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true

        stickChart.setData(candleData);
        stickChart.invalidate();
        stickChart.notifyDataSetChanged();
    }


    protected CandleData generateCandleData(List<FenSHIModule.SerialsEntity.DataEntity> data) {


        ArrayList<CandleEntry> entries = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            xValues.add(String.valueOf(i));
        }

        for (int index = 0; index < data.size(); index++) {
            if (hour < 15 && hour >= 9) {
                float now = Float.parseFloat(data.get(index).getNow());  //收盘
                float open = Float.parseFloat(data.get(index).getOpen()); //开盘

                float high = Float.parseFloat(data.get(index).getHigh()); //最高价
                float low = Float.parseFloat(data.get(index).getLow());   //最低价
                entries.add(new CandleEntry(index, high, low, open, now));
            } else {
                float now = Float.parseFloat(data.get(index).getNow());  //收盘
                float open = Float.parseFloat(data.get(index).getOpen()); //开盘

                float high = Float.parseFloat(data.get(index).getHigh()); //最高价
                float low = Float.parseFloat(data.get(index).getLow());   //最低价
                entries.add(new CandleEntry(index, high, low, open, now));
            }
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
        CandleData d = new CandleData(xValues, set);
        d.setDrawValues(false);
        return d;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = (GridView) view.findViewById(R.id.GridViewFenLine);
        gridView.setAdapter(adapter);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refrshLayout);

        initRefrshLayout();

        loading();


    }

    private void initRefrshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        itemsModels.clear();
                        loading();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragmet_home, container, false);
    }

    private void loading() {
        for (int i = 0; i < 6; i++) {
            RxVolley.get(Api.FENSHIBASE + codes.get(i) + Api.FENSHI, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    fenSHIModule = gson.fromJson(t, FenSHIModule.class);

                    RxVolley.get(Api.FENSHIBASE + fenSHIModule.getCode() + Api.RIK + 16, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                            itemsModels.add(fenSHIModule);
                            adapter.notifyDataSetChanged();
                            LoadDialog.dismiss(getActivity());
                        }
                    });


                }
            });


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
                        Thread.sleep(10000);
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
    protected void BaseFgDestory() {
        mLineChart = null;
        stickChart = null;
    }
}
