package com.example.zhangxiu.birthdayquan.Memory.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.birthdaymodule.Adapter.BaseAdapter;
import com.example.birthdaymodule.Util.AssetsManger;
import com.example.zhangxiu.birthdayquan.BaseFragment;
import com.example.zhangxiu.birthdayquan.Love.Fragment.LoveFragment;
import com.example.zhangxiu.birthdayquan.Love.LoveDetailsActivity;
import com.example.zhangxiu.birthdayquan.Love.Model.LoveModels;
import com.example.zhangxiu.birthdayquan.Memory.Adapter.MemoryAdapter;
import com.example.zhangxiu.birthdayquan.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MemoryFragment extends BaseFragment {

    private  static  String TAG=" MemoryFragment";

    @ViewInject(R.id.rl_memory)
    private RecyclerView mRlMemory;

    public MemoryFragment() {

    }

    public static MemoryFragment newInstance(String title) {
        MemoryFragment fragment = new MemoryFragment();
        Bundle args = new Bundle();
        args.putString(BaseFragment.BASE_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_memory, container, false);


        x.view().inject(this,view);


        

        //设置ui
        initUI();

        return  view;
    }

  public  void  initUI(){

      MemoryAdapter adapter=new MemoryAdapter(getActivity());

      final LoveModels cellModel= AssetsManger.readAssertToObject("love_values",LoveModels.class,getContext());

      adapter.setModels(cellModel.getValues());

      //
      adapter.setOnClickLister(new BaseAdapter.onClickLister() {
          @Override
          public void onItemClickLister(View itemView, int position) {
              Intent intent=new Intent(getActivity(), LoveDetailsActivity.class);
              intent.putExtra(LoveFragment.LoveDetails, cellModel.getValues().get(position));
              startActivity(intent);
              Log.i(TAG, "onItemClickLister: ");
          }

          @Override
          public void onItemLongClickLister(View itemView, int position) {

              Log.i(TAG, "onItemLongClickLister: ");
               }
      });


      mRlMemory.setAdapter(adapter);


      LinearLayoutManager manger=new LinearLayoutManager(getActivity());

      //manger.setMeasuredDimension(100,100);

      //manger.get
      mRlMemory.setLayoutManager(manger);
      mRlMemory.setLayoutFrozen(false);




  }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
      //  mListener = null;
    }

}
