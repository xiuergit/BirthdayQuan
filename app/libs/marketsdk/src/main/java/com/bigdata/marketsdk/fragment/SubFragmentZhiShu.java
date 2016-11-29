package com.bigdata.marketsdk.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
 * 指数
 */
public class SubFragmentZhiShu extends Fragment {

    private static final String TAG = "SubFragmentZhiShu";

    private ListView cy_listView, bcy_listview;

    private AbsAdapter<FenSHIModule> adapter1;
    private AbsAdapter<FenSHIModule> adapter2;

    private List<FenSHIModule> moduleList;
    private List<FenSHIModule> modules;

    private CandleStickChart stickChart;


    private Gson gson;

    private boolean isbooen = false;

    private List<String> codes_cy;
    private List<String> codes_bcy;

    private LineChart lineChart;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isbooen = false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadDialog.show(getActivity());
        codes_cy = new ArrayList<>();
        codes_bcy = new ArrayList<>();

        gson = new Gson();

        modules = new ArrayList<>();

        moduleList = new ArrayList<>();

        adapter1 = new AbsAdapter<FenSHIModule>(getActivity().getApplicationContext(), moduleList, R.layout.zhishu_item) {
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
                    if (Double.parseDouble(String.format("%.2f", Double.parseDouble(item.getNow()))) > Double.parseDouble(String.format("%.2f", Double.parseDouble(item.getPrevClose())))) {
                        view.setBackgroundColor(Color.RED);
                        TextView zhang = holder.getView(R.id.zhang_zhi);
                        zhang.setText("：涨");
                        zhang.setTextColor(Color.RED);
                    } else {
                        view.setBackgroundColor(Color.GREEN);
                        TextView die = holder.getView(R.id.die_zhi);
                        die.setText("：跌");
                        die.setTextColor(Color.GREEN);
                    }
                }
                holder.setText(R.id.title, item.getName());
                holder.setText(R.id.code, item.getCode());


                double changeRange = Double.parseDouble(item.getChangeRange());

                if (item.getChange() != null && item.getChangeRange() != null) {
                    if (Double.parseDouble(item.getNow()) > Double.parseDouble(item.getPrevClose())) {
                        holder.setText(R.id.bai_fen_bi, String.format("%.2f", changeRange * 100) + "%");
                    } else {
                        holder.setText(R.id.bai_fen_bi, String.format("%.2f", changeRange * 100) + "%");
                    }
                }
                //开盘价
                holder.setText(R.id.xianliang_zhi, String.format("%.2f", Double.parseDouble(item.getNow())));

                //交易额
                if (item.getFundFlowContent() == null) {
                    holder.setText(R.id.shizhi_zhi, "-");
                } else {
                    double ttlshr = Double.parseDouble(item.getFundFlowContent());
                    int tt = (int) (ttlshr / 100000000);
                    holder.setText(R.id.shizhi_zhi, tt + "亿");
                }
                //现量
                if (item.getVolumeSpread() == null) {
                    holder.setText(R.id.PE_zhi, "-");
                } else {
                    int xianliang = Integer.parseInt(item.getVolumeSpread());
                    holder.setText(R.id.PE_zhi, xianliang + "");
                }

            }
        };


        adapter2 = new AbsAdapter<FenSHIModule>(getActivity().getApplicationContext(), modules, R.layout.zhishu_item) {
            @Override
            public void showData(ViewHoder holder, FenSHIModule item, int position) {

                LinearLayout view = holder.getView(R.id.color_layout);
                lineChart = holder.getView(R.id.linechart);

                showChart(lineChart, getLineData(item.getSerials().getData()), Color.rgb(0, 0, 0));


                showCandleChart(stickChart, generateCandleData(item.getSerials().getData()), Color.rgb(0, 0, 0));

                if (item.getNow() != null && item.getPrevClose() != null) {
                    if (Double.parseDouble(String.format("%.2f", Double.parseDouble(item.getNow()))) > Double.parseDouble(String.format("%.2f", Double.parseDouble(item.getPrevClose())))) {
                        view.setBackgroundColor(Color.RED);
                        TextView zhang = holder.getView(R.id.zhang_zhi);
                        zhang.setText("：涨");
                        zhang.setTextColor(Color.RED);
                    } else {
                        view.setBackgroundColor(Color.GREEN);
                        TextView die = holder.getView(R.id.die_zhi);
                        die.setText("：跌");
                        die.setTextColor(Color.GREEN);
                    }
                }


                if (item.getCode() != null && item.getName() != null) {
                    holder.setText(R.id.title, item.getName());
                    holder.setText(R.id.code, item.getCode());
                }


                double changeRange = 0;
                if (item.getChangeRange() != null) {
                    changeRange = Double.parseDouble(item.getChangeRange());
                }

                if (item.getChange() != null && item.getChangeRange() != null) {
                    if (Double.parseDouble(item.getNow()) > Double.parseDouble(item.getPrevClose())) {
                        holder.setText(R.id.bai_fen_bi, String.format("%.2f", changeRange * 100) + "%");
                    } else {
                        holder.setText(R.id.bai_fen_bi, String.format("%.2f", changeRange * 100) + "%");
                    }
                }
                //开盘价
                if (item.getNow() != null) {
                    holder.setText(R.id.xianliang_zhi, String.format("%.2f", Double.parseDouble(item.getNow())));
                }

                //交易额
                if (item.getFundFlowContent() == null) {
                    holder.setText(R.id.shizhi_zhi, "-");
                } else {
                    double ttlshr = Double.parseDouble(item.getFundFlowContent());
                    int tt = (int) (ttlshr / 100000000);
                    holder.setText(R.id.shizhi_zhi, tt + "亿");
                }
                //现量
                if (item.getVolumeSpread() == null) {
                    holder.setText(R.id.PE_zhi, "-");
                } else {
                    double xianliang = Double.parseDouble(item.getVolumeSpread());
                    holder.setText(R.id.PE_zhi, String.format("%.2f", xianliang));
                }


            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zhishu, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        load();


        cy_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("bd_code", moduleList.get(position).getCode());
                intent.putExtra("zhishu", "exponent");
                intent.setClass(getActivity().getApplication(), StockActivity.class);
                startActivity(intent);
            }
        });
        bcy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("zhishu", "exponent");
                intent.putExtra("bd_code", modules.get(position).getCode());
                intent.setClass(getActivity().getApplication(), StockActivity.class);
                startActivity(intent);
            }
        });


    }

    private void load() {

        if (!isbooen) {
            RxVolley.get(Api.A + "100839", new HttpCallback() {
                @Override
                public void onSuccess(String t) {

                    A_Codes A_codes = gson.fromJson(t, A_Codes.class);
                    codes_cy.addAll(A_codes.getCodes());
                    for (int i = 0; i < codes_cy.size(); i++) {
                        RxVolley.get(Api.FENSHIBASE + codes_cy.get(i) + Api.FENSHI, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);

                                RxVolley.get(Api.FENSHIBASE + fenSHIModule.getCode() + Api.A_K, new HttpCallback() {
                                    @Override
                                    public void onSuccess(String t) {
                                        FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                                        moduleList.add(fenSHIModule);
                                        adapter1.notifyDataSetChanged();
                                    }
                                });

                            }
                        });


                    }
                }
            });


            RxVolley.get(Api.A + "101202", new HttpCallback() {
                @Override
                public void onSuccess(String t) {

                    A_Codes A_codes = gson.fromJson(t, A_Codes.class);
                    codes_bcy.addAll(A_codes.getCodes());
                    for (int i = 0; i < codes_bcy.size(); i++) {
                        RxVolley.get(Api.FENSHIBASE + codes_bcy.get(i) + Api.FENSHI, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                                RxVolley.get(Api.FENSHIBASE + fenSHIModule.getCode() + Api.A_K, new HttpCallback() {
                                    @Override
                                    public void onSuccess(String t) {
                                        FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                                        modules.add(fenSHIModule);
                                        adapter2.notifyDataSetChanged();
                                    }
                                });

                            }
                        });

                    }
                }
            });
        }


    }

    private void init(View view) {
        cy_listView = (ListView) view.findViewById(R.id.cy_listview);

        cy_listView.setAdapter(adapter1);

        bcy_listview = (ListView) view.findViewById(R.id.bcy_listview);

        bcy_listview.setAdapter(adapter2);

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
        LoadDialog.dismiss(getActivity());
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

        LoadDialog.dismiss(getActivity());
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        isbooen = true;
        lineChart = null;
    }
}
