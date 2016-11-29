package com.bigdata.marketsdk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bigdata.marketsdk.Adapter.AbsAdapter;
import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.activity.NewsDetailActivity;
import com.bigdata.marketsdk.module.ZI_XUN_Module;
import com.bigdata.marketsdk.weight.LoadDialog;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by windows on 2016/1/11.
 */
public class JXHFragment extends Fragment {


    private static final String TAG = "JXHFragment";

    private Gson gson;

    private ListView listView;

    private List<ZI_XUN_Module.DATAEntity> dataEntities;

    private AbsAdapter<ZI_XUN_Module.DATAEntity> adapter;

    private String code;

    private int ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadDialog.show(getActivity());
        code = getArguments().getString("code");
        ID = getArguments().getInt("id");
        gson = new Gson();
        dataEntities = new ArrayList<>();
        adapter = new AbsAdapter<ZI_XUN_Module.DATAEntity>(getActivity().getApplicationContext(), dataEntities, R.layout.jxh_listview_item) {
            @Override
            public void showData(ViewHoder vHolder, ZI_XUN_Module.DATAEntity data, int position) {
                vHolder.setText(R.id.title, data.getTIT());
                vHolder.setText(R.id.time, data.getPUB_DT());

            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loading();
    }

    private void loading() {

        switch (ID) {
            case 1577:  //金信号
                RxVolley.get(Api.ZIXUN + code + Api.JXH + ID, new HttpCallback() {


                    @Override
                    public void onSuccess(String result) {
                        JSONArray arr = null;


                        String substring = result.substring(2, result.length());

                        JSONArray array = null;
                        try {

                            arr = new JSONArray(substring);
                            JSONObject obj = arr.getJSONObject(0);
                            array = obj.getJSONArray("DATA");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObj = (JSONObject) array.opt(i);
                                ZI_XUN_Module.DATAEntity data = new ZI_XUN_Module.DATAEntity();
                                data.setTIT(jsonObj.getString("TIT"));
                                data.setPUB_DT(jsonObj.getString("PUB_DT"));
                                data.setID(jsonObj.getInt("ID"));

                                dataEntities.add(data);
                                adapter.notifyDataSetChanged();
                                JXHFragment.getTotalHeightofListView(listView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case 1576:  //研报
                RxVolley.get(Api.ZIXUN + "\'"+code+"\'" + Api.YANBAO + ID, new HttpCallback() {
                    @Override
                    public void onSuccess(String result) {
                        JSONArray arr = null;
                        JSONArray array = null;

                        String substring = result.substring(2, result.length());

                        try {

                            arr = new JSONArray(substring);
                            JSONObject obj = arr.getJSONObject(0);
                            array = obj.getJSONArray("DATA");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObj = (JSONObject) array.opt(i);
                                ZI_XUN_Module.DATAEntity data = new ZI_XUN_Module.DATAEntity();
                                data.setTIT(jsonObj.getString("TIT"));
                                data.setPUB_DT(jsonObj.getString("PUB_DT"));
                                data.setID(jsonObj.getInt("ID"));
                                data.setCONT_ID(jsonObj.getInt("CONT_ID"));
                                dataEntities.add(data);
                                adapter.notifyDataSetChanged();
                                JXHFragment.getTotalHeightofListView(listView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                break;
            case 1575:  //公告

                RxVolley.get(Api.ZIXUN + "\'"+code+"\'" + Api.JXH + ID, new HttpCallback() {


                    @Override
                    public void onSuccess(String result) {
                        JSONArray arr = null;


                        String substring = result.substring(2, result.length());

                        JSONArray array = null;
                        try {

                            arr = new JSONArray(substring);
                            JSONObject obj = arr.getJSONObject(0);
                            array = obj.getJSONArray("DATA");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObj = (JSONObject) array.opt(i);
                                ZI_XUN_Module.DATAEntity data = new ZI_XUN_Module.DATAEntity();
                                data.setTIT(jsonObj.getString("TIT"));
                                data.setPUB_DT(jsonObj.getString("PUB_DT"));
                                data.setID(jsonObj.getInt("ID"));

                                dataEntities.add(data);
                                adapter.notifyDataSetChanged();
                                JXHFragment.getTotalHeightofListView(listView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                break;


        }


    }

    private void init(View view) {
        listView = (ListView) view.findViewById(R.id.jxh_lv);
        listView.setAdapter(adapter);

        LoadDialog.dismiss(getActivity());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("id", dataEntities.get(position).getID());
                intent.putExtra("title", dataEntities.get(position).getTIT());
                intent.putExtra("time", dataEntities.get(position).getPUB_DT());
                intent.putExtra("aut", dataEntities.get(position).getAUT());
                intent.putExtra("cont_id", dataEntities.get(position).getCONT_ID());
                intent.putExtra("type", ID);
                startActivity(intent);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jxh, container, false);
    }

    public static void getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
