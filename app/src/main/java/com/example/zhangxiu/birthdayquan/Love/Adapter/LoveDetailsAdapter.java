package com.example.zhangxiu.birthdayquan.Love.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.birthdaymodule.Adapter.BaseAdapter;
import com.example.zhangxiu.birthdayquan.R;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by xiuer on 16/11/8.
 */

public class LoveDetailsAdapter extends BaseAdapter {



    private  static  String TAG="LoveDetailsAdapter";

    private ArrayList<String>mImageUrls;
    private Context mContext;
    public  LoveDetailsAdapter(Context mContext,ArrayList<String>imageUrls){
        this.mContext=mContext;
        this.mImageUrls=imageUrls;
    }


   public  class  DetailsViewHolder extends  BaseViewHolder{
       public DetailsViewHolder(View itemView) {
           super(itemView);
       }
   }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        super.onBindViewHolder(holder,position);

        View view= LinearLayout.inflate(mContext, R.layout.love_fragment_item,null);
        ImageView imageView=
                (ImageView)holder.itemView.findViewById(R.id.iv_love_details_yi);


        ImageOptions options= new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(150), DensityUtil.dip2px(150))
                .setRadius(DensityUtil.dip2px(10))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true)
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //设置加载过程中的图片
                .setLoadingDrawableId(R.drawable.love)
                //设置加载失败后的图片
                .setFailureDrawableId(R.drawable.kunshan)
                //设置使用缓存
                .setUseMemCache(true)
                //设置支持gif
                .setIgnoreGif(false)
                //设置显示圆形图片
                .setCircular(false)
                .setSquare(true)
                .build();

        x.image().bind(imageView,mImageUrls.get(position),options);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LinearLayout.inflate(mContext, R.layout.imageview_details,null);
        DetailsViewHolder detailsViewHolder=new DetailsViewHolder(view);
        return  detailsViewHolder;

    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }
}
