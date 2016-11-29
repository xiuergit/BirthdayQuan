package com.bigdata.marketsdk.weight;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigdata.marketsdk.R;


/**
 * kun on 2016/6/27.
 * 打造通用的TopBar,可以实现自定义显示控件
 * 文字，文字大小，文字颜色，等等
 */
public class TopBarCustom extends RelativeLayout {

    private TextView mTextView;
    private ImageView mImageLeft;
    private ImageView mImageRight;

    private TextView mLeftTextView;

    private TextView mRightTextView;

    private String mTitle;
    private float mTitleSize;
    private int mTitleColor;
    private boolean mTitleVisible;

    private String mLeftTitle;
    private float mLeftSize;
    private int mLeftColor;

    private String mRightTitle;
    private float mRightSize;
    private int mRightColor;

    private Drawable mImageLeftBackGround;
    private boolean mImageLeftVisible;

    private Drawable mImageRightBackGround;
    private boolean mImageRightVisible;

    private LayoutParams leftParams;
    private LayoutParams rightParams;
    private LayoutParams textViewParams;

    private topBarClickLister topBarClickLister;

    public interface topBarClickLister {
        void leftClick();

        void rightClick();
    }


    /**
     * 点击事件
     *
     * @param lister
     * @return
     */
    public void setTopBarClickLister(topBarClickLister lister) {
        this.topBarClickLister = lister;
    }

    /**
     * 设置标题栏
     * @param title
     */
    public void setmTitle(String title){
        mTextView.setText(title);
    }






    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public TopBarCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.topBar);
        mTitle = typedArray.getString(R.styleable.topBar_titleText);
        mTitleSize = typedArray.getDimension(R.styleable.topBar_titleSize, 16);
        mTitleColor = typedArray.getColor(R.styleable.topBar_titleColor, Color.BLACK);
        mTitleVisible = typedArray.getBoolean(R.styleable.topBar_titleVisible, true);

        mLeftTitle = typedArray.getString(R.styleable.topBar_leftText);
        mLeftColor = typedArray.getColor(R.styleable.topBar_leftTextColor, Color.BLACK);
        mLeftSize = typedArray.getDimension(R.styleable.topBar_leftTextSize, 16);

        mRightTitle = typedArray.getString(R.styleable.topBar_rightText);
        mRightColor = typedArray.getColor(R.styleable.topBar_rightTextColor, Color.BLACK);
        mRightSize = typedArray.getDimension(R.styleable.topBar_rightTextSize, 16);

        mImageLeftBackGround = typedArray.getDrawable(R.styleable.topBar_ImageLeftBackGround);
        mImageLeftVisible = typedArray.getBoolean(R.styleable.topBar_ImageLeftVisible, true);

        mImageRightBackGround = typedArray.getDrawable(R.styleable.topBar_ImageRightBackGround);
        mImageRightVisible = typedArray.getBoolean(R.styleable.topBar_ImageRightVisible, true);

        typedArray.recycle();

        mTextView = new TextView(context);
        mImageLeft = new ImageView(context);
        mImageRight = new ImageView(context);
        mLeftTextView = new TextView(context);
        mRightTextView = new TextView(context);

        mTextView.setText(mTitle);
        mTextView.setTextSize(mTitleSize);
        mTextView.setTextColor(mTitleColor);

        mLeftTextView.setText(mLeftTitle);
        mLeftTextView.setTextColor(mLeftColor);
        mLeftTextView.setTextSize(mLeftSize);

        mRightTextView.setText(mRightTitle);
        mRightTextView.setTextColor(mRightColor);
        mRightTextView.setTextSize(mRightSize);

        if (mTitleVisible) {
            mTextView.setVisibility(VISIBLE);
        } else {
            mTextView.setVisibility(INVISIBLE);
        }

        mImageLeft.setBackground(mImageLeftBackGround);
        if (mImageLeftVisible) {
            mImageLeft.setVisibility(VISIBLE);
        } else {
            mImageLeft.setVisibility(INVISIBLE);
        }
        mImageRight.setBackground(mImageRightBackGround);
        if (mImageRightVisible) {
            mImageRight.setVisibility(VISIBLE);
        } else {
            mImageRight.setVisibility(INVISIBLE);
        }

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(mImageLeft, leftParams);

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(mLeftTextView, leftParams);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(mImageRight, rightParams);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(mRightTextView, rightParams);

        textViewParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
        addView(mTextView, textViewParams);


        mLeftTextView.setClickable(true);
        mRightTextView.setClickable(true);

        mImageLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarClickLister.leftClick();
            }
        });

        mImageRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarClickLister.rightClick();
            }
        });

        mLeftTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarClickLister.leftClick();
            }
        });
        mRightTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarClickLister.rightClick();
            }
        });
    }
}
