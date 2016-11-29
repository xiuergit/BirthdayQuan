package com.bigdata.marketsdk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bigdata.marketsdk.Adapter.AbsAdapter;
import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.activity.StockActivity;
import com.bigdata.marketsdk.module.A_Codes;
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
import java.util.List;

/**
 * A股
 */

public class SubFragmentAGu extends Fragment {


    private static final String TAG = "SubFragmentAGu";

    private AbsAdapter<FenSHIModule> adapter;

    private ListView listView;

    private Gson gson;


    private SwipeRefreshLayout refreshLayout;

    private String num;

    private List<String> codes;

    private LineChart lineChart;

    private boolean isbooen = false;

    private CandleStickChart stickChart;

    private List<FenSHIModule> fenSHIModules;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isbooen = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        LoadDialog.show(getActivity());
        num = getArguments().getString("sector_id");
        fenSHIModules = new ArrayList<>();
        codes = new ArrayList<>();


        adapter = new AbsAdapter<FenSHIModule>(getActivity().getApplicationContext(), fenSHIModules, R.layout.a_item_recylerview) {
            @Override
            public void showData(ViewHoder holder, FenSHIModule item, int position) {
                LinearLayout view = holder.getView(R.id.color_layout);


                lineChart = holder.getView(R.id.linechart);
                lineChart.setViewPortOffsets(10, 10, 10, 10);

                stickChart = holder.getView(R.id.candleChart);
                stickChart.setViewPortOffsets(10, 10, 10, 10);

                showCandleChart(stickChart, generateCandleData(item.getSerials().getData()), Color.rgb(0, 0, 0));
                showChart(lineChart, getLineData(item.getSerials().getData()), Color.rgb(0, 0, 0));
                if (item.getNow() != null && item.getPrevClose() != null) {
                    if (Double.parseDouble(item.getNow()) > Double.parseDouble(item.getPrevClose())) {
                        view.setBackgroundColor(Color.RED);
                    } else {
                        view.setBackgroundColor(Color.GREEN);
                    }
                }
                String Level = item.getNewsRatingLevel();
                int levelnum = 0;
                if (Level != null) {
                    levelnum = Integer.parseInt(Level);
                }
                View layout = holder.getView(R.id.fragment_view);
                switch (levelnum) {
                    case 2:
                        holder.setText(R.id.daxiao, "正+");
                        layout.setBackgroundColor(Color.rgb(204, 21, 21));
                        break;
                    case 1:
                        holder.setText(R.id.daxiao, "正");
                        layout.setBackgroundColor(Color.rgb(204, 21, 21));
                        break;
                    case 0:
                        holder.setText(R.id.daxiao, "中");
                        layout.setBackgroundColor(Color.rgb(43, 176, 241));
                        break;
                    case -1:
                        holder.setText(R.id.daxiao, "负");
                        layout.setBackgroundColor(Color.rgb(41, 152, 8));
                        break;
                    case -2:
                        holder.setText(R.id.daxiao, "负-");
                        layout.setBackgroundColor(Color.rgb(41, 152, 8));
                        break;
                }
                holder.setText(R.id.title, item.getName());
                holder.setText(R.id.code, item.getCode());

                if (item.getNewsRatingName() == null) {
                    holder.setText(R.id.zixun, "-");
                } else {
                    holder.setText(R.id.zixun, item.getNewsRatingName());
                }


                String newsRatingDate = item.getNewsRatingDate();

                if (newsRatingDate == null) {
                    holder.setText(R.id.date, "0-0-0");
                } else {
                    String year = newsRatingDate.substring(0, 4);
                    String mouth = newsRatingDate.substring(4, 6);
                    String day = newsRatingDate.substring(6, 8);
                    holder.setText(R.id.date, year + "-" + mouth + "-" + day);
                }
                double change = 0;
                double changeRange = 0;
                if (item.getChange() != null && item.getChangeRange() != null) {
                    change = Double.parseDouble(item.getChange());
                    changeRange = Double.parseDouble(item.getChangeRange());
                }


                if (item.getChange() != null && item.getChangeRange() != null) {
                    if (Double.parseDouble(item.getNow()) > Double.parseDouble(item.getPrevClose())) {
                        holder.setText(R.id.bizhi, String.format("%.2f", change));
                    } else {
                        holder.setText(R.id.bizhi, String.format("%.2f", change));
                    }
                    if (Double.parseDouble(item.getNow()) > Double.parseDouble(item.getPrevClose())) {
                        holder.setText(R.id.bai_fen_bi, String.format("%.2f", changeRange * 100) + "%");
                    } else {
                        holder.setText(R.id.bai_fen_bi, String.format("%.2f", changeRange * 100) + "%");
                    }
                }
                //现量
                holder.setText(R.id.xianliang_zhi, item.getVolumeSpread());
                //总市值
                if (item.getTtlShr() != null) {
                    double ttlshr = Double.parseDouble(item.getTtlShr());
                    int tt = (int) (ttlshr / 100000000);
                    holder.setText(R.id.shizhi_zhi, tt + "亿");
                }

                //PE
                if (item.getPE() != null) {
                    double PE = Double.parseDouble(item.getPE());
                    holder.setText(R.id.PE_zhi, String.format("%.2f", PE));
                }


            }
        };

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.a_fragment, container, false);

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refrshLayout);

        initRefrshLayout();

        init(view);
        loading();


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        isbooen = true;
                        if (view.getLastVisiblePosition() == view.getCount() - 1) {
                            RxVolley.get(Api.A + num, new HttpCallback() {
                                @Override
                                public void onSuccess(String t) {
                                    A_Codes A_codes = gson.fromJson(t, A_Codes.class);
                                    codes.addAll(A_codes.getCodes());
                                    for (int i = view.getLastVisiblePosition() + 1; i < view.getLastVisiblePosition() + 10; i++) {
                                        RxVolley.get(Api.FENSHIBASE + codes.get(i) + Api.FENSHI, new HttpCallback() {
                                            @Override
                                            public void onSuccess(String t) {
                                                FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                                                RxVolley.get(Api.FENSHIBASE + fenSHIModule.getCode() + Api.A_K, new HttpCallback() {
                                                    @Override
                                                    public void onSuccess(String t) {
                                                        FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                                                        fenSHIModules.add(fenSHIModule);
                                                        adapter.notifyDataSetChanged();

                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("bd_code", fenSHIModules.get(position).getCode());
                intent.setClass(getActivity().getApplication(), StockActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initRefrshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        isbooen = false;
                        codes.clear();
                        fenSHIModules.clear();
                        loading();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }


    private void init(View view) {
        listView = (ListView) view.findViewById(R.id.RecyclerView);
        listView.setAdapter(adapter);
    }

    private void loading() {
        if (!isbooen) {
            RxVolley.get(Api.A + num, new HttpCallback() {
                @Override
                public void onSuccess(String t) {

                    A_Codes A_codes = gson.fromJson(t, A_Codes.class);
                    codes.addAll(A_codes.getCodes());
                    for (int i = 0; i < 10; i++) {
                        RxVolley.get(Api.FENSHIBASE + codes.get(i) + Api.FENSHI, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);

                                RxVolley.get(Api.FENSHIBASE + fenSHIModule.getCode() + Api.A_K, new HttpCallback() {
                                    @Override
                                    public void onSuccess(String t) {
                                        FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                                        fenSHIModules.add(fenSHIModule);
                                        adapter.notifyDataSetChanged();
                                        LoadDialog.dismiss(getActivity());
                                    }
                                });
                            }
                        });
                    }
                }
            });


        } else {
            return;
        }
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

        lineChart.setDrawBorders(false);//设置图表内格子外的边框是否显示
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisLeft();
        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// 将X坐标轴放置在底部，默认是在顶部。
        xAxis.setLabelsToSkip(60);
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
            xValues.add(data.get(i).getDate());
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
            xValues.add(data.get(i).getDate());
        }

        for (int index = 0; index < data.size(); index++) {

            float now = Float.parseFloat(data.get(index).getNow());  //收盘
            float open = Float.parseFloat(data.get(index).getOpen()); //开盘

            float high = Float.parseFloat(data.get(index).getHigh()); //最高价
            float low = Float.parseFloat(data.get(index).getLow());   //最低价
            entries.add(new CandleEntry(index, high, low, open, now));

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
    public void onDestroy() {
        super.onDestroy();
        isbooen = true;
        lineChart = null;
        stickChart = null;
    }
}
