package com.example.zhangxiu.birthdayquan.Love.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.birthdaymodule.Adapter.BaseAdapter;
import com.example.birthdaymodule.Util.AssetsManger;
import com.example.zhangxiu.birthdayquan.BaseFragment;
import com.example.zhangxiu.birthdayquan.Love.Adapter.LoveAdapter;
import com.example.zhangxiu.birthdayquan.Love.Adapter.LovePageAdapter;
import com.example.zhangxiu.birthdayquan.Love.LoveDetailsActivity;
import com.example.zhangxiu.birthdayquan.Love.Model.LoveModels;
import com.example.zhangxiu.birthdayquan.R;
import com.google.gson.Gson;
import com.google.gson.internal.Primitives;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoveFragment extends BaseFragment implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private static  final  String TAG="LoveFragment";

    public  static  final  String LoveDetails="LoveDetails";

    public  static  final  String BundleLove="BundleLove";
    //组件
    @ViewInject(R.id.vp_love_head)
    private ViewPager mViewPager;

    @ViewInject(R.id.ll_point_buttons)
    private LinearLayout mPointView;

    @ViewInject(R.id.love_content_list)
    private RecyclerView mRecyclerView;

    private   int mImageHeadCount=3;





    private OnFragmentInteractionListener mListener;

    public LoveFragment() {
        // Required empty public constructor
    }

    public static LoveFragment newInstance(String title) {
        LoveFragment fragment = new LoveFragment();

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
           View view=inflater.inflate(R.layout.fragment_love, container, false);
           x.view().inject(this,view);
        try {
            initUI();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;

    }

    public void setPointSelect(ImageView imageView){
        imageView.setBackgroundResource(R.drawable.umeng_socialize_follow_on);

    }
    public void setPointUnSelect(ImageView imageView){
        imageView.setBackgroundResource(R.drawable.umeng_socialize_follow_off);
    }

  //给圆点添加事件
    @Override
    public void onClick(View view) {
        setPointSelect((ImageView) view);
        ImageView imageView;
        for(int i=0;i<mPointView.getChildCount();i++){
             imageView=(ImageView) mPointView.getChildAt(i);
            if(!imageView.equals(view)){
                setPointUnSelect(imageView);
            }
            else {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    //添加小圆点
    public  void addPointIndex(){
        ImageView imageView;
        for (int i=0;i<mImageHeadCount;i++) {
            imageView = new ImageView(getActivity());
            if(i==0){
               setPointSelect(imageView);

            }else {
                setPointUnSelect(imageView);
            }
            LinearLayout.LayoutParams  params=new LinearLayout.LayoutParams(20,20);
            params.setMargins(5,0,5,0);
            imageView.setLayoutParams(params);
            imageView.setFocusable(true);
            imageView.setEnabled(true);
            imageView.setOnClickListener(this
            );
            mPointView.addView(imageView, i);
        }
    }

     public  void initUI() throws IOException {

        //HEAD
           mViewPager.setAdapter(new LovePageAdapter(getActivity()));
           addPointIndex();
           mViewPager.addOnPageChangeListener(this);

         //CONTENT

         final LoveModels lovemodels= AssetsManger.readAssertToObject("love_values",LoveModels.class,getContext());
        // readAssertToObject("jsont",LoveModels.class);

         LoveAdapter loveAdapter=new LoveAdapter(getActivity());

         loveAdapter.setModels(lovemodels.getValues());

         loveAdapter.setOnClickLister(new BaseAdapter.onClickLister() {

            //单击
            @Override
             public void onItemClickLister(View itemView, int position) {

                 Intent intent=new Intent(getActivity(), LoveDetailsActivity.class);
                 intent.putExtra(LoveDetails, lovemodels.getValues().get(position));
                  startActivity(intent);
             }

             //长按
             @Override
             public void onItemLongClickLister(View itemView, int position) {

                 Log.i(TAG, "onItemLongClickLister: "+position);
             }
         });

         mRecyclerView.setAdapter(loveAdapter);
         mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


         RecyclerView.ItemAnimator animator=new RecyclerView.ItemAnimator() {
             @Override
             public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
                 return false;
             }

             @Override
             public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder,
                                              @Nullable ItemHolderInfo preLayoutInfo,
                                              @NonNull ItemHolderInfo postLayoutInfo) {
                 return false;
             }

             @Override
             public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                 return false;
             }

             @Override
             public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                 return false;
             }

             @Override
             public void runPendingAnimations() {

             }

             @Override
             public void endAnimation(RecyclerView.ViewHolder item) {

             }

             @Override
             public void endAnimations() {

             }

             @Override
             public boolean isRunning() {
                 return false;
             }
         };
        mRecyclerView.setItemAnimator(animator);








     }


    public  <T> T readAssertToObject(String assertName,Class<T>mClass ) throws IOException {

        InputStream is;
        BufferedReader reader;
        is=getContext().getAssets().open(assertName);
        reader=new BufferedReader(new InputStreamReader(is));
        Gson gson=new Gson();
        Object object  = gson.fromJson(reader,mClass);

        return  Primitives.wrap(mClass).cast(object);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
          ImageView imageViewSelect= (ImageView) mPointView.getChildAt(position);
        ImageView imageView;
           for(int i=0;i<mPointView.getChildCount();i++){
               imageView=(ImageView) mPointView.getChildAt(i);
               if(!imageView.equals(imageViewSelect)){
                   setPointUnSelect(imageView);
               }
               else {
                   setPointSelect(imageView);
               }
           }

}

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
