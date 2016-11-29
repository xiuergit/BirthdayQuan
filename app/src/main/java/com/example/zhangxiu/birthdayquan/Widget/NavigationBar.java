package com.example.zhangxiu.birthdayquan.Widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.zhangxiu.birthdayquan.R;

/**
 * Created by xiuer on 16/11/9.
 */

public class NavigationBar extends RelativeLayout {

    private  RelativeLayout mLeftView;
    private  RelativeLayout mTitleView;
    private  RelativeLayout mRightView;


    public void initView(Context context){
        RelativeLayout relativeLayout= (RelativeLayout)View.inflate(context, R.layout.navigation,this);
        mLeftView=(RelativeLayout) relativeLayout.findViewById(R.id.rl_nav_left_viewgroup);
        mTitleView=(RelativeLayout)relativeLayout.findViewById(R.id.rl_nav_title_viewgroup);
        mRightView=(RelativeLayout)relativeLayout.findViewById(R.id.rl_nav_right_viewgroup);

        //初始化
        setLeftItem("",null);
        setTitleItem("");
        setRightItem("",null);

    }

    public NavigationBar(Context context) {
        super(context);
        initView(context);

    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public  void setLeftItem(View view){
        mLeftView.removeAllViews();
        mLeftView.setBackgroundColor(Color.BLUE);
        view.setBackgroundColor(Color.CYAN);
        LayoutParams params=new LayoutParams(100,40);
        params.setMargins(12,12,12,12);

        view.setLayoutParams(params);

        mLeftView.addView(view);
    }

    public  void setLeftItem(String title,OnClickListener onClickListener){
        MButton left=(MButton) mLeftView.findViewById(R.id.bt_nav_left);
        left.setText(title);
        left.setBackgroundColor(this.getDrawingCacheBackgroundColor());
        if(onClickListener!=null){
            left.setOnClickListener(onClickListener);
        }

    }


    public  void  setTitleItem(String tittle){
          MButton title=(MButton)mTitleView.findViewById(R.id.bt_nav_title);
          title.setBackgroundColor(this.getDrawingCacheBackgroundColor());
    }

    public  void  setTitleItem(View view){
        mTitleView.removeAllViews();
        mTitleView.addView(view);
    }


    public  void setRightItem(String title, OnClickListener onClickListener){


        MButton mButton=(MButton)mRightView.findViewById(R.id.bt_nav_right);
        mButton.setBackgroundColor(this.getDrawingCacheBackgroundColor());
        mButton.setText(title);
        if(title!=null&&onClickListener!=null){
            mButton.setOnClickListener(onClickListener);
        }
        else {
            mButton.setText("");
        }
    }

    public  void  setRightItem(int imageId,OnClickListener onClickListener){

        MButton mButton=(MButton)mRightView.findViewById(R.id.bt_nav_right);
        mButton.setBackgroundResource(imageId);
        mButton.setBackgroundColor(this.getDrawingCacheBackgroundColor());
        if(onClickListener!=null){
            mButton.setOnClickListener(onClickListener);
        }

    }

    public  void  setRightItem(View view){

        mRightView.removeAllViews();
        mRightView.addView(view);

    }




}
