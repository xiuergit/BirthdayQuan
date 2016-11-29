package com.example.zhangxiu.birthdayquan.Love.Adapter;

import android.content.Context;
import android.support.v4.print.PrintHelper;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.zhangxiu.birthdayquan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiu on 2016/10/21.
 */
public class LovePageAdapter extends PagerAdapter {

    private  static String TAG="LovePageAdapter";
    private  int[] images={R.drawable.love1,R.drawable.home,R.drawable.love2};
    private List<ImageView>mImageViews=new ArrayList<ImageView>();
    private Context mContext;

    public LovePageAdapter(Context mContext) {
        this.mContext = mContext;
        ImageView imageView;
        for (int i=0;i<images.length;i++){
            imageView=new ImageView(mContext);
            imageView.setBackgroundResource(images[i]);
            mImageViews.add(imageView);
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
         container.addView(mImageViews.get(position));

        return  mImageViews.get(position);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImageViews.get(position));
    }

}
