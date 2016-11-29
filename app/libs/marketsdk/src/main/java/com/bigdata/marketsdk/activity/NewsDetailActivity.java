package com.bigdata.marketsdk.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.base.BaseActivity;
import com.bigdata.marketsdk.weight.LoadDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * user:kun
 * Date:2016/10/25
 * email:hekun@gamil.com
 * Desc: 资讯详情
 */
public class NewsDetailActivity extends BaseActivity {

    private String strUrl;
    private TextView mNewsTitle;

    private TextView mTextTitle;
    private ImageView imag_back;
    private TextView mNewsTime;
    private TextView mAut;
    private TextView mCont;
    private String title;

    private String aut;
    private String cont;
    private String time;
    private int id;
    private int type;
    private int cont_id;


    public void initView(String cont) {
        mNewsTitle = (TextView) findViewById(R.id.news_title);
        mNewsTime = (TextView) findViewById(R.id.news_time);
        mAut = (TextView) findViewById(R.id.news_aut);
        mCont = (TextView) findViewById(R.id.news_cont);
        mNewsTitle.setText(title);
        mNewsTime.setText(time);
        mAut.setText(aut);
        mCont.setText(cont);
        mTextTitle.setText(title);
    }


    // 解析新闻列表
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String value1 = val.substring(3, val.length() - 1);
            try {
                JSONObject object = new JSONObject(value1);
                JSONArray obj = object.getJSONArray("DATA");

                for (int i = 0; i < obj.length(); i++) {
                    JSONObject jsonObj = (JSONObject) obj.opt(i);
                    cont = jsonObj.getString("CONT");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            initView(cont);
            LoadDialog.dismiss(NewsDetailActivity.this);
        }

    };

    /**
     * 网络操作相关的子线程
     */

    private void loding() {
        final Message msg = new Message();
        final Bundle data = new Bundle();

        switch (type) {
            case 1576:
                RxVolley.get(Api.YB_XQ + cont_id, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        data.putString("value", t);
                        msg.setData(data);
                        handler.sendMessage(msg);
                    }
                });
                break;
            case 1575:
                RxVolley.get(Api.GG_XQ + id, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        data.putString("value", t);
                        msg.setData(data);
                        handler.sendMessage(msg);
                    }
                });
                break;
            case 1577:
                RxVolley.get(strUrl, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        data.putString("value", t);
                        msg.setData(data);
                        handler.sendMessage(msg);
                    }
                });
                break;
            default:
                RxVolley.get(strUrl, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        data.putString("value", t);
                        msg.setData(data);
                        handler.sendMessage(msg);
                    }
                });
                break;
        }


    }

    @Override
    protected void baseDestory() {
    }

    @Override
    protected void baseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.newsdetail);
        LoadDialog.show(this);
        LinearLayout ll = (LinearLayout) findViewById(R.id.linear_news);
        mTextTitle = (TextView) findViewById(R.id.title);
        imag_back = (ImageView) findViewById(R.id.image_back);
        imag_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll.setLongClickable(true);

        id = getIntent().getExtras().getInt("id");

        type = getIntent().getExtras().getInt("type");
        cont_id = getIntent().getExtras().getInt("cont_id");

        title = getIntent().getStringExtra("title");
        aut = getIntent().getStringExtra("aut");
        time = getIntent().getStringExtra("time");

        strUrl = "http://t1.chinabigdata.com/PostService.aspx?Service=NewsEventDetailService.Gets&Function=GetsService&id=" + id + "&atype=json";
        loding();
    }
}
