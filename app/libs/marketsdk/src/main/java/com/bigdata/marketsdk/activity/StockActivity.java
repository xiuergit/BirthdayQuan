package com.bigdata.marketsdk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.base.BaseActivity;
import com.bigdata.marketsdk.fragment.F10_Frangemt;
import com.bigdata.marketsdk.fragment.FenShiFragment;
import com.bigdata.marketsdk.fragment.JXHFragment;
import com.bigdata.marketsdk.fragment.R_KLine;
import com.bigdata.marketsdk.fragment.WuRiFragment;
import com.bigdata.marketsdk.module.Stock;
import com.bigdata.marketsdk.utils.ListDataSave;
import com.bigdata.marketsdk.weight.LoadDialog;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.Calendar;
import java.util.HashMap;


/**
 * user:kun
 * Date:2016/10/25 or 下午1:49
 * email:hekun@gamil.com
 * Desc: 个股
 */
public class StockActivity extends BaseActivity {


    private Gson gson = new Gson();
    private Intent intent;

    private SwipeRefreshLayout refreshLayout;


    private String code;  //股票代码

    private String zhishu;  //指数标记

    private Stock stock = new Stock();

    private SPUtils spUtils;

    private ImageView image_back;



    private ListDataSave listDataSave;

    private Calendar c = Calendar.getInstance();

    private int hour = c.get(Calendar.HOUR_OF_DAY);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (hour < 15 && hour > 9) {
                        loding();
                    }
                    break;
            }
        }
    };


    private TextView stock_title,
            open, jin_kai, jin_kai_jia, gao, gao_zhi, bi_zhi_01, bizhi_02, zuo_shou,
            zuo_shou_zhi, di, di_zhi, xian_shou, xian_shou_zhi, cheng_jiao_ling, cjl_zhi,
            s_y_l, s_y_l_zhi, liang_bi, liang_bi_zhi, cheng_jiao_e, cheng_jiao_e_zhi,
            shou_yi, shou_yi_zhi, huang_shou, huan_shou_zhi, zong_shi, zong_shi_zhi,
            liu_tong, liu_tong_zhi;

    private RadioGroup group, group02;


    private RadioButton btn01, btn02, btn03, btn04, btn05, btn06, btn07, btn08, btn09, btn10;


    @Override
    protected void baseDestory() {
        finish();
    }

    @Override
    protected void baseCreate(Bundle savedInstanceState) {

        LoadDialog.show(StockActivity.this);


        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();


        listDataSave = new ListDataSave(StockActivity.this, ListDataSave.CODEFILE);


        if (spUtils == null) {
            spUtils = new SPUtils(this, "CODE");
        }

        boolean connected = NetworkUtils.isConnected(this);
        if (!connected) {
            ToastUtils.showShortToast(this, "网络不可用");
            return;
        }
        setContentView(R.layout.fragment_a);


        intent = getIntent();
        code = intent.getStringExtra("bd_code");
        zhishu = intent.getStringExtra("zhishu");
        loding();

        //初始化控件

        init();
        initData();

        initRefrshLayout();


        /**
         * 画图
         */
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        FenShiFragment fenShi = new FenShiFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        bundle.putString("typ", intent.getStringExtra("typ"));
        fenShi.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_main_ll, fenShi);
        fragmentTransaction.commit();

        /**
         * 资讯
         */
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        JXHFragment jxhFragment = new JXHFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("code", code);
        bundle1.putInt("id", 1577);
        jxhFragment.setArguments(bundle1);
        fragmentTransaction1.replace(R.id.fragment_main_ll_02, jxhFragment);
        fragmentTransaction1.commit();

        clickRadioGroup();
    }

    private void initRefrshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        stock = null;
                        loding();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }


    private void clickRadioGroup() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.fragment_fenshi) {
                    btn01.setTextColor(Color.RED);
                    btn02.setTextColor(Color.WHITE);
                    btn03.setTextColor(Color.WHITE);
                    btn04.setTextColor(Color.WHITE);
                    btn05.setTextColor(Color.WHITE);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    FenShiFragment fenShi = new FenShiFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("code", code);
                    bundle.putString("typ", intent.getStringExtra("typ"));
                    fenShi.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_main_ll, fenShi);
                    fragmentTransaction.commit();


                } else if (checkedId == R.id.fragment_wuri) {
                    btn02.setTextColor(Color.RED);
                    btn01.setTextColor(Color.WHITE);
                    btn03.setTextColor(Color.WHITE);
                    btn04.setTextColor(Color.WHITE);
                    btn05.setTextColor(Color.WHITE);
                    FragmentTransaction fragmentTransaction02 = getSupportFragmentManager().beginTransaction();
                    WuRiFragment wuri = new WuRiFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("code", code);
                    wuri.setArguments(bundle2);
                    fragmentTransaction02.replace(R.id.fragment_main_ll, wuri);
                    fragmentTransaction02.commit();

                } else if (checkedId == R.id.fragment_riK) {
                    btn03.setTextColor(Color.RED);
                    btn01.setTextColor(Color.WHITE);
                    btn02.setTextColor(Color.WHITE);
                    btn04.setTextColor(Color.WHITE);
                    btn05.setTextColor(Color.WHITE);
                    FragmentTransaction fragmentTransaction03 = getSupportFragmentManager().beginTransaction();
                    R_KLine r_kLine = new R_KLine();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("code", code);
                    bundle3.putInt("type", 16);
                    r_kLine.setArguments(bundle3);
                    fragmentTransaction03.replace(R.id.fragment_main_ll, r_kLine);
                    fragmentTransaction03.commit();

                } else if (checkedId == R.id.fragment_zhouK) {
                    btn04.setTextColor(Color.RED);
                    btn01.setTextColor(Color.WHITE);
                    btn03.setTextColor(Color.WHITE);
                    btn02.setTextColor(Color.WHITE);
                    btn05.setTextColor(Color.WHITE);
                    FragmentTransaction fragmentTransaction04 = getSupportFragmentManager().beginTransaction();
                    R_KLine zhou_kLine = new R_KLine();
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("code", code);
                    bundle4.putInt("type", 17);
                    zhou_kLine.setArguments(bundle4);
                    fragmentTransaction04.replace(R.id.fragment_main_ll, zhou_kLine);
                    fragmentTransaction04.commit();

                } else if (checkedId == R.id.fragment_yueK) {
                    btn05.setTextColor(Color.RED);
                    btn01.setTextColor(Color.WHITE);
                    btn03.setTextColor(Color.WHITE);
                    btn04.setTextColor(Color.WHITE);
                    btn02.setTextColor(Color.WHITE);
                    FragmentTransaction fragmentTransaction05 = getSupportFragmentManager().beginTransaction();
                    R_KLine yue_kLine = new R_KLine();
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("code", code);
                    bundle5.putInt("type", 18);
                    yue_kLine.setArguments(bundle5);
                    fragmentTransaction05.replace(R.id.fragment_main_ll, yue_kLine);
                    fragmentTransaction05.commit();

                }
            }
        });


        group02.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.fragment_jinxinhao) {
                    btn06.setTextColor(Color.RED);
                    btn07.setTextColor(Color.WHITE);
                    btn08.setTextColor(Color.WHITE);
                    btn09.setTextColor(Color.WHITE);
                    btn10.setTextColor(Color.WHITE);

                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    JXHFragment jxhFragment = new JXHFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("code", code);
                    bundle1.putInt("id", 1577);
                    jxhFragment.setArguments(bundle1);
                    fragmentTransaction1.replace(R.id.fragment_main_ll_02, jxhFragment);
                    fragmentTransaction1.commit();

                } else if (checkedId == R.id.fragment_yanbao) {
                    btn06.setTextColor(Color.WHITE);
                    btn07.setTextColor(Color.RED);
                    btn08.setTextColor(Color.WHITE);
                    btn09.setTextColor(Color.WHITE);
                    btn10.setTextColor(Color.WHITE);

                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    JXHFragment jxhFragment2 = new JXHFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("code", code);
                    bundle2.putInt("id", 1576);
                    jxhFragment2.setArguments(bundle2);
                    fragmentTransaction2.replace(R.id.fragment_main_ll_02, jxhFragment2);
                    fragmentTransaction2.commit();

                } else if (checkedId == R.id.fragment_gonggao) {
                    btn06.setTextColor(Color.WHITE);
                    btn07.setTextColor(Color.WHITE);
                    btn08.setTextColor(Color.RED);
                    btn09.setTextColor(Color.WHITE);
                    btn10.setTextColor(Color.WHITE);

                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    JXHFragment jxhFragment3 = new JXHFragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("code", code);
                    bundle3.putInt("id", 1575);
                    jxhFragment3.setArguments(bundle3);
                    fragmentTransaction3.replace(R.id.fragment_main_ll_02, jxhFragment3);
                    fragmentTransaction3.commit();

                } else if (checkedId == R.id.fragment_zhengduan) {
                    btn06.setTextColor(Color.WHITE);
                    btn07.setTextColor(Color.WHITE);
                    btn08.setTextColor(Color.WHITE);
                    btn09.setTextColor(Color.RED);
                    btn10.setTextColor(Color.WHITE);

                } else if (checkedId == R.id.fragment_F10) {
                    FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                    F10_Frangemt f10_frangemt = new F10_Frangemt();
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("code", code);
                    f10_frangemt.setArguments(bundle5);
                    fragmentTransaction5.replace(R.id.fragment_main_ll_02, f10_frangemt);
                    fragmentTransaction5.commit();
                    btn06.setTextColor(Color.WHITE);
                    btn07.setTextColor(Color.WHITE);
                    btn08.setTextColor(Color.WHITE);
                    btn09.setTextColor(Color.WHITE);
                    btn10.setTextColor(Color.RED);

                }
            }
        });
    }


    private void initData() {

        jin_kai.setText("今开");
        gao.setText("高");
        zuo_shou.setText("昨收");
        di.setText("低");
        xian_shou.setText("现手");
        cheng_jiao_ling.setText("成交量");
        s_y_l.setText("市赢率");
        liang_bi.setText("量比");
        cheng_jiao_e.setText("成交额");
        shou_yi.setText("收益");
        huang_shou.setText("换手");
        zong_shi.setText("总市值");
        liu_tong.setText("流通值");
    }

    private void init() {
        image_back = (ImageView) findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refrshLayout);
        open = (TextView) findViewById(R.id.open);
        jin_kai = (TextView) findViewById(R.id.jin_kai);
        jin_kai_jia = (TextView) findViewById(R.id.jin_kai_jia);
        gao = (TextView) findViewById(R.id.gao);
        gao_zhi = (TextView) findViewById(R.id.gao_zhi);
        bi_zhi_01 = (TextView) findViewById(R.id.bi_zhi_01);
        bizhi_02 = (TextView) findViewById(R.id.bizhi_02);
        zuo_shou = (TextView) findViewById(R.id.zuo_shou);
        zuo_shou_zhi = (TextView) findViewById(R.id.zuo_shou_zhi);
        di = (TextView) findViewById(R.id.di);
        di_zhi = (TextView) findViewById(R.id.di_zhi);
        xian_shou = (TextView) findViewById(R.id.xian_shou);
        xian_shou_zhi = (TextView) findViewById(R.id.xian_shou_zhi);
        cheng_jiao_ling = (TextView) findViewById(R.id.cheng_jiao_ling);
        cjl_zhi = (TextView) findViewById(R.id.cjl_zhi);
        s_y_l = (TextView) findViewById(R.id.s_y_l);
        s_y_l_zhi = (TextView) findViewById(R.id.s_y_l_zhi);
        liang_bi = (TextView) findViewById(R.id.liang_bi);
        liang_bi_zhi = (TextView) findViewById(R.id.liang_bi_zhi);
        cheng_jiao_e = (TextView) findViewById(R.id.cheng_jiao_e);
        cheng_jiao_e_zhi = (TextView) findViewById(R.id.cheng_jiao_e_zhi);
        shou_yi = (TextView) findViewById(R.id.shou_yi);
        shou_yi_zhi = (TextView) findViewById(R.id.shou_yi_zhi);
        huang_shou = (TextView) findViewById(R.id.huang_shou);
        huan_shou_zhi = (TextView) findViewById(R.id.huan_shou_zhi);
        zong_shi = (TextView) findViewById(R.id.zong_shi);
        zong_shi_zhi = (TextView) findViewById(R.id.zong_shi_zhi);
        liu_tong = (TextView) findViewById(R.id.liu_tong);
        liu_tong_zhi = (TextView) findViewById(R.id.liu_tong_zhi);

        stock_title = (TextView) findViewById(R.id.title);
        group = (RadioGroup) findViewById(R.id.fragment_rg);
        group02 = (RadioGroup) findViewById(R.id.fragment_rg_02);

        btn01 = (RadioButton) findViewById(R.id.fragment_fenshi);
        btn02 = (RadioButton) findViewById(R.id.fragment_wuri);
        btn03 = (RadioButton) findViewById(R.id.fragment_riK);
        btn04 = (RadioButton) findViewById(R.id.fragment_zhouK);
        btn05 = (RadioButton) findViewById(R.id.fragment_yueK);

        btn06 = (RadioButton) findViewById(R.id.fragment_jinxinhao);
        btn07 = (RadioButton) findViewById(R.id.fragment_yanbao);
        btn08 = (RadioButton) findViewById(R.id.fragment_gonggao);
        btn09 = (RadioButton) findViewById(R.id.fragment_zhengduan);
        btn10 = (RadioButton) findViewById(R.id.fragment_F10);


        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //加号点击事件
        final ImageView imageView = (ImageView) findViewById(R.id.image_jia);

        if (zhishu != null) {
            imageView.setVisibility(View.GONE);
        }

        if (!listDataSave.getCodeState(code)) {
            imageView.setBackgroundResource(R.drawable.favorite_delete);
        } else {
            imageView.setBackgroundResource(R.drawable.favorite_add);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                HashMap<String, String> dataMap = listDataSave.getDataMap(listDataSave.CODES_MAP);

                if (!listDataSave.getCodeState(code)) {
                    dataMap.remove(code);
                    imageView.setBackgroundResource(R.drawable.favorite_add);
                    listDataSave.setDataMap(listDataSave.CODES_MAP, dataMap);
                    listDataSave.setCodeState(code, !listDataSave.getCodeState(code));
                } else {
                    dataMap.put(code, code);
                    imageView.setBackgroundResource(R.drawable.favorite_delete);
                    listDataSave.setDataMap(ListDataSave.CODES_MAP, dataMap);
                    listDataSave.setCodeState(code, !listDataSave.getCodeState(code));

                }


            }
        });

        /**
         * 搜索跳转过来的判断
         */

        if (intent.getStringExtra("typ") != null) {
            if (intent.getStringExtra("typ").equals("13")) {
                group02.setVisibility(View.GONE);
            } else {
                group02.setVisibility(View.VISIBLE);
            }
            //指数页面跳转过来的
        } else {
            if (zhishu != null) {
                group02.setVisibility(View.GONE);
            } else {
                group02.setVisibility(View.VISIBLE);
            }
        }


    }


    private void loding() {

        RxVolley.get(Api.XQ + code + Api.CSHU, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                stock = gson.fromJson(t, Stock.class);
                //现价
                open.setText(String.format("%.2f", stock.getNow()));
                //今开
                jin_kai_jia.setText(String.format("%.2f", stock.getOpen()));
                //高
                gao_zhi.setText(String.format("%.2f", stock.getHigh()));
                //昨收
                zuo_shou_zhi.setText(String.format("%.2f", stock.getPrevClose()));
                //股票代码
                stock_title.setText(stock.getName() + "(" + code + ")");
                stock_title.setTextSize(20);
                //低
                di_zhi.setText(String.format("%.2f", stock.getLow()));
                //成交额
                if (stock.getAmount() > 100000000) {
                    double l = (double) stock.getAmount() / 100000000;
                    cheng_jiao_e_zhi.setText(String.format("%.2f", l) + "亿");
                }
                //成交量
                if (stock.getVolume() > 10000) {
                    double v = (double) stock.getVolume() / 1000000;
                    cjl_zhi.setText(String.format("%.2f", v) + "万手");
                }
                //量比
                liang_bi_zhi.setText(String.format("%.2f", stock.getVolRatio()));

                //现手
                int i = stock.getVolumeSpread() / 100;
                xian_shou_zhi.setText(i + "");
                //市盈率
                s_y_l_zhi.setText(String.format("%.2f", stock.getPettm()));
                //收益
                shou_yi_zhi.setText(String.format("%.2f", stock.getEps()));

                //换手
                huan_shou_zhi.setText(String.format("%.2f", stock.getChangeHandsRate() * 100) + "%");

                //总市值
                double zong = (stock.getNow() * stock.getTtlShr() / 100000000);
                zong_shi_zhi.setText(Math.round(zong) + "亿");
                //流通值
                double liu = (stock.getNow() * stock.getTtlShrNtlc() / 100000000);
                liu_tong_zhi.setText(Math.round(liu) + "亿");
                bizhi_02.setText(String.format("%.2f", stock.getChangeRange() * 100) + "%");
                bi_zhi_01.setText(String.format("%.2f", stock.getChange()));
                //颜色
                if (stock.getNow() > stock.getPrevClose()) {
                    open.setTextColor(Color.RED);
                    bi_zhi_01.setTextColor(Color.RED);
                    bizhi_02.setTextColor(Color.RED);
                } else {
                    open.setTextColor(Color.GREEN);
                    bi_zhi_01.setTextColor(Color.GREEN);
                    bizhi_02.setTextColor(Color.GREEN);
                }
                if (stock.getOpen() > stock.getPrevClose()) {
                    jin_kai_jia.setTextColor(Color.RED);
                } else {
                    jin_kai_jia.setTextColor(Color.GREEN);
                }

                if (stock.getOpen() > stock.getPrevClose()) {
                    bi_zhi_01.setText(String.format("%.2f", stock.getChange()));
                } else {
                    bi_zhi_01.setText(String.format("%.2f", stock.getChange()));
                }
                if (stock.getOpen() > stock.getPrevClose()) {
                    bizhi_02.setText(String.format("%.2f", stock.getChangeRange() * 100) + "%");
                } else {
                    bizhi_02.setText(String.format("%.2f", stock.getChangeRange() * 100) + "%");
                }

                LoadDialog.dismiss(StockActivity.this);
            }
        });


    }


}
