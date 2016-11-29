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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigdata.marketsdk.Adapter.AbsAdapter;
import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.module.FenSHIModule;
import com.bigdata.marketsdk.module.FenShiDatas;
import com.bigdata.marketsdk.module.Five_Speed;
import com.bigdata.marketsdk.module.KlineData;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


/**
 * user:kun
 * Date:2016/10/25
 * email:hekun@gamil.com
 * Desc: 分时-fragment
 */
public class FenShiFragment extends Fragment {


    private static final String TAG = "FenShiFragment";

    private LineChart mlineChart;
    private BarChart mbarChart;
    private static final int count = 243;
    private Gson gson;
    private String code;
    private String prevClose, high, low, max, min;


    private TextView volume1, volume2, volume3, volume4, volume5, volume6, volume7, volume8, volume9, volume10, mai,
            price1, price2, price3, price4, price5, price6, price7, price8, price9, price10;

    private RadioGroup radioGroup;

    private RadioButton btn01, btn02;

    private LinearLayout linearLayout01, linearLayout02, layout_type;

    private ListView listView;

    private List<FenShiDatas> datas;

    private AbsAdapter<FenShiDatas> adapter;

    private String typ;

    private LimitLine ll1;

    private Calendar c = Calendar.getInstance();
    private int hour = c.get(Calendar.HOUR_OF_DAY);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (hour < 15 && hour >= 9) {
                        loadData();
                        loda();
                        lodaListData();
                    }
                    break;
            }
        }
    };
    private List<FenSHIModule.SerialsEntity.DataEntity> dataEntities;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadDialog.show(getContext());
        gson = new Gson();
        dataEntities = new ArrayList<>();
        code = (String) getArguments().get("code");
        typ = (String) getArguments().get("typ");
        datas = new ArrayList<>();
        adapter = new AbsAdapter<FenShiDatas>(getActivity().getApplicationContext(), datas, R.layout.item_mimgxi) {
            @Override
            public void showData(ViewHoder vHolder, FenShiDatas data, int position) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                vHolder.setText(R.id.item_time, formatter.format(data.getTime()));
                vHolder.setText(R.id.item_Price, String.format("%.2f", data.getNow()));
                vHolder.setText(R.id.item_Volume, data.getVolume_spread() + "");
            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fenshi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mbarChart = (BarChart) view.findViewById(R.id.barChar);
        mlineChart = (LineChart) view.findViewById(R.id.lineChar);

        mbarChart.setViewPortOffsets(10, 10, 10, 10);//左、上、右、下
        mlineChart.setViewPortOffsets(10, 20, 10, 10);
        loadData();
        init(view);
        loda();
        clickRG();
        lodaListData();

        LoadDialog.dismiss(getActivity());


    }

    private void lodaListData() {
        RxVolley.get(Api.FENSHIBASE + code + Api.MINGXI, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                KlineData kline = gson.fromJson(t, KlineData.class);
                datas.clear();
                datas.addAll(kline.getSerials().getData());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void clickRG() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb01) {
                    btn01.setTextColor(Color.RED);
                    btn02.setTextColor(Color.WHITE);
                    linearLayout02.setVisibility(View.GONE);
                    linearLayout01.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.rb02) {
                    btn01.setTextColor(Color.WHITE);
                    btn02.setTextColor(Color.RED);
                    linearLayout01.setVisibility(View.GONE);
                    linearLayout02.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void loda() {
        RxVolley.get(Api.XQ + code + Api.WUDANG, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Five_Speed five_speed = gson.fromJson(t, Five_Speed.class);
                if (five_speed.getNow() > five_speed.getPrevClose()) {
                    price1.setText(String.format("%.2f", five_speed.getAskPrice5()));
                    price1.setTextColor(Color.RED);
                    price2.setText(String.format("%.2f", five_speed.getAskPrice4()));
                    price2.setTextColor(Color.RED);
                    price3.setText(String.format("%.2f", five_speed.getAskPrice3()));
                    price3.setTextColor(Color.RED);
                    price4.setText(String.format("%.2f", five_speed.getAskPrice2()));
                    price4.setTextColor(Color.RED);
                    price5.setText(String.format("%.2f", five_speed.getAskPrice1()));
                    price5.setTextColor(Color.RED);
                    price6.setText(String.format("%.2f", five_speed.getBidPrice1()));
                    price6.setTextColor(Color.RED);
                    price7.setText(String.format("%.2f", five_speed.getBidPrice2()));
                    price7.setTextColor(Color.RED);
                    price8.setText(String.format("%.2f", five_speed.getBidPrice3()));
                    price8.setTextColor(Color.RED);
                    price9.setText(String.format("%.2f", five_speed.getBidPrice4()));
                    price9.setTextColor(Color.RED);
                    price10.setText(String.format("%.2f", five_speed.getBidPrice5()));
                    price10.setTextColor(Color.RED);
                } else {
                    price1.setText(String.format("%.2f", five_speed.getAskPrice5()));
                    price1.setTextColor(Color.GREEN);
                    price2.setText(String.format("%.2f", five_speed.getAskPrice4()));
                    price2.setTextColor(Color.GREEN);
                    price3.setText(String.format("%.2f", five_speed.getAskPrice3()));
                    price3.setTextColor(Color.GREEN);
                    price4.setText(String.format("%.2f", five_speed.getAskPrice2()));
                    price4.setTextColor(Color.GREEN);
                    price5.setText(String.format("%.2f", five_speed.getAskPrice1()));
                    price5.setTextColor(Color.GREEN);
                    price6.setText(String.format("%.2f", five_speed.getBidPrice1()));
                    price6.setTextColor(Color.GREEN);
                    price7.setText(String.format("%.2f", five_speed.getBidPrice2()));
                    price7.setTextColor(Color.GREEN);
                    price8.setText(String.format("%.2f", five_speed.getBidPrice3()));
                    price8.setTextColor(Color.GREEN);
                    price9.setText(String.format("%.2f", five_speed.getBidPrice4()));
                    price9.setTextColor(Color.GREEN);
                    price10.setText(String.format("%.2f", five_speed.getBidPrice5()));
                    price10.setTextColor(Color.GREEN);
                }


                volume1.setText(five_speed.getAskVolume5() / 100 + "");
                volume2.setText(five_speed.getAskVolume4() / 100 + "");
                volume3.setText(five_speed.getAskVolume3() / 100 + "");
                volume4.setText(five_speed.getAskVolume2() / 100 + "");
                volume5.setText(five_speed.getAskVolume1() / 100 + "");
                volume6.setText(five_speed.getBidVolume1() / 100 + "");
                volume7.setText(five_speed.getBidVolume2() / 100 + "");
                volume8.setText(five_speed.getBidVolume3() / 100 + "");
                volume9.setText(five_speed.getBidVolume4() / 100 + "");
                volume10.setText(five_speed.getBidVolume5() / 100 + "");


            }
        });

    }

    private void init(View view) {
        radioGroup = (RadioGroup) view.findViewById(R.id.fragment_fenshi_rg);
        linearLayout01 = (LinearLayout) view.findViewById(R.id.fragment_wudang);
        linearLayout02 = (LinearLayout) view.findViewById(R.id.fragment_mingxi);


        layout_type = (LinearLayout) view.findViewById(R.id.fenshi_type);

        if (typ != null) {
            if (typ.equals("13")) {
                layout_type.setVisibility(View.GONE);
            } else {
                layout_type.setVisibility(View.VISIBLE);
            }
        } else {
            layout_type.setVisibility(View.VISIBLE);
        }


        btn01 = (RadioButton) view.findViewById(R.id.rb01);
        btn02 = (RadioButton) view.findViewById(R.id.rb02);

        price1 = (TextView) view.findViewById(R.id.price1);
        price2 = (TextView) view.findViewById(R.id.price2);
        price3 = (TextView) view.findViewById(R.id.price3);
        price4 = (TextView) view.findViewById(R.id.price4);
        price5 = (TextView) view.findViewById(R.id.price5);
        price6 = (TextView) view.findViewById(R.id.price6);
        price7 = (TextView) view.findViewById(R.id.price7);
        price8 = (TextView) view.findViewById(R.id.price8);
        price9 = (TextView) view.findViewById(R.id.price9);
        price10 = (TextView) view.findViewById(R.id.price10);


        volume1 = (TextView) view.findViewById(R.id.volume1);
        volume2 = (TextView) view.findViewById(R.id.volume2);
        volume3 = (TextView) view.findViewById(R.id.volume3);
        volume4 = (TextView) view.findViewById(R.id.volume4);
        volume5 = (TextView) view.findViewById(R.id.volume5);
        volume6 = (TextView) view.findViewById(R.id.volume6);
        volume7 = (TextView) view.findViewById(R.id.volume7);
        volume8 = (TextView) view.findViewById(R.id.volume8);
        volume9 = (TextView) view.findViewById(R.id.volume9);
        volume10 = (TextView) view.findViewById(R.id.volume10);

        listView = (ListView) view.findViewById(R.id.fragment_fenshi_lv);
        listView.setAdapter(adapter);

        for (int i = 5; i > 0; i--) {
            mai = new TextView(getActivity().getApplicationContext());
            mai.setText("卖" + i);
            mai.setTextColor(Color.YELLOW);
            mai.setTextSize(10);
            LinearLayout viewById = (LinearLayout) (view.findViewById(R.id.fragment_fenshi_l1));
            viewById.addView(mai);
        }
        for (int i = 1; i <= 5; i++) {
            mai = new TextView(getActivity().getApplicationContext());
            mai.setText("买" + i);
            mai.setTextColor(Color.YELLOW);
            mai.setTextSize(10);
            LinearLayout viewById = (LinearLayout) (view.findViewById(R.id.fragment_fenshi_l1));
            viewById.addView(mai);
        }

    }


    private void loadData() {
        RxVolley.get(Api.FENSHIBASE + code + Api.FENSHI, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                FenSHIModule fenSHIModule = gson.fromJson(t, FenSHIModule.class);
                prevClose = fenSHIModule.getPrevClose();
                max = fenSHIModule.getHigh();
                min = fenSHIModule.getLow();
                dataEntities = fenSHIModule.getSerials().getData();
                if (dataEntities.size() <= 0) {
                    return;
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

                showChart(mlineChart, getLineData(dataEntities), Color.alpha(1000));
                showBarChart(mbarChart, getBarData(dataEntities), Color.alpha(1000));
            }
        });


    }


    /**
     * 线性图
     *
     * @param lineChart
     * @param lineData
     * @param color
     */
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
        if (prevClose != null) {
            ll1 = new LimitLine(Float.parseFloat(prevClose), "");
            ll1.setLineWidth(1f);
            ll1.enableDashedLine(1f, 1f, 0f);
            ll1.setLineColor(Color.WHITE);
            yAxis.addLimitLine(ll1);
        }


        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);// 将X坐标轴放置在底部，默认是在顶部。
        xAxis.setLabelsToSkip(60);
        yAxis.setDrawGridLines(true);//是否显示Y坐标轴上的刻度竖线，默认是true
        yAxis.setShowOnlyMinMax(true);    //参数如果为true Y轴坐标只显示最大值和最小值
        yAxis.setTextColor(Color.WHITE);
        yAxis.setStartAtZero(false);//不从0开始


        if (Float.parseFloat(prevClose) < Float.parseFloat(high)) {
            yAxis.setAxisMaxValue(Float.parseFloat(high) + 1);    //设置Y轴坐标最大为多少
            yAxis.setAxisMinValue(Float.parseFloat(prevClose) - (Float.parseFloat(high) + 1 - Float.parseFloat(prevClose)));    //设置Y轴坐标最小为多少
        } else {
            yAxis.setAxisMaxValue(((Float.parseFloat(prevClose) - Float.parseFloat(low)) + Float.parseFloat(prevClose)));    //设置Y轴坐标最大为多少
            yAxis.setAxisMinValue(Float.parseFloat(low));    //设置Y轴坐标最小为多少
        }
        yAxis.setLabelCount(5, false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);  //参数是INSIDE_CHART(Y轴坐标在内部) 或 OUTSIDE_CHART(在外部（默认是这个）)

        YAxis rightAxis = lineChart.getAxisRight();

        rightAxis.setDrawLabels(true);// 不显示图表的右边y坐标轴线
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setShowOnlyMinMax(true);
        rightAxis.setStartAtZero(false);
        double v = (Float.parseFloat(high) - Float.parseFloat(prevClose)) / Float.parseFloat(prevClose) * 100;
        double v1 = 0 - v;
        rightAxis.setAxisMaxValue((float) v);
        rightAxis.setAxisMinValue((float) v1);
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
        xAxis.setLabelsToSkip(60);
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

    /**
     * lineData
     *
     * @param data
     * @return
     */
    private LineData getLineData(List<FenSHIModule.SerialsEntity.DataEntity> data) {


        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
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
        lineDataSet1.setColor(Color.rgb(51, 161, 201));
        lineDataSet1.setDrawCircleHole(false);
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);     //以左边坐标轴为准 还是以右边坐标轴为基准
        lineDataSet1.setFillColor(Color.argb(0, 0, 0, 0));
        lineDataSet1.setFillAlpha(100);
        lineDataSet1.setDrawFilled(true);

        ArrayList<Entry> yValues2 = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            float amount = Float.parseFloat(data.get(i).getAmount());
            float volume = Float.parseFloat(data.get(i).getVolume());
            float i3 = amount / volume;
            yValues2.add(new Entry(i3, i));
        }
        LineDataSet lineDataSet2 = new LineDataSet(yValues2, "");


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

    /**
     * barData
     *
     * @param data
     * @return
     */
    private BarData getBarData(List<FenSHIModule.SerialsEntity.DataEntity> data) {
        ArrayList<String> xValues = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            xValues.add(i + "");
        }
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {

            float volume = Float.parseFloat(data.get(i).getVolume()) - Float.parseFloat(data.get(i - 1).getVolume());
            yValues.add(new BarEntry(volume, i));
        }
        // create a dataset and give it a type
        // y轴的数据集合

        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarData barData = new BarData(xValues, barDataSet);
        barData.setDrawValues(false);
        barDataSet.setColor(Color.YELLOW);
        return barData;
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
        mbarChart = null;
        mlineChart = null;
    }

}
