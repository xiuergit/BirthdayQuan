package com.bigdata.marketsdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigdata.marketsdk.Adapter.StickyGridAdapter;
import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.base.BaseFragment;
import com.bigdata.marketsdk.module.MoreData;
import com.bigdata.marketsdk.weight.LoadDialog;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * user:kun
 * Date:2016/11/4 or 下午3:49
 * email:hekun@gamil.com
 * Desc: 更多
 */

public class MoreFragment extends BaseFragment {

    private StickyGridHeadersGridView mGridView;

    private List<MoreData.DATABean> dataBeen;

    private StickyGridAdapter adatper;

    private Gson gson;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        dataBeen = new ArrayList<>();
        LoadDialog.show(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (StickyGridHeadersGridView) view.findViewById(R.id.asset_grid);
        initData();


        adatper = new StickyGridAdapter(getActivity(), dataBeen, mGridView);


        mGridView.setAdapter(adatper);
    }


    private void initData() {
        RxVolley.get(Api.MoreUrl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                String substring = t.substring(2, t.length());
                try {
                    JSONArray jsonArray = new JSONArray(substring);
                    JSONObject o = (JSONObject) jsonArray.get(0);
                    JSONArray data = o.getJSONArray("DATA");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObj = (JSONObject) data.opt(i);
                        MoreData.DATABean dataModule = new MoreData.DATABean();
                        dataModule.setINFO_SOUR(jsonObj.optString("INFO_SOUR"));
                        dataModule.setRN(jsonObj.optInt("RN"));
                        dataModule.setSECT_CODE(jsonObj.optString("SECT_CODE"));
                        dataModule.setSECT_ID(jsonObj.optInt("SECT_ID"));
                        dataModule.setSECT_NAME(jsonObj.optString("SECT_NAME"));
                        dataModule.setSTD_CODE(jsonObj.optInt("STD_CODE"));
                        dataModule.setSTD_NAME(jsonObj.optString("STD_NAME"));
                        dataModule.setTYP(jsonObj.optInt("TYP"));
                        dataModule.setTYP_NAME(jsonObj.optString("TYP_NAME"));
                        dataBeen.add(dataModule);
                    }
                    adatper.notifyDataSetChanged();
                    LoadDialog.dismiss(getActivity());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void BaseFgDestory() {

    }

}
