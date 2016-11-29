package com.example.zhangxiu.birthdayquan.Love.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.birthdaymodule.Adapter.BaseAdapter;
import com.example.zhangxiu.birthdayquan.Love.Model.LoveCellModel;
import com.example.zhangxiu.birthdayquan.R;
import com.example.zhangxiu.birthdayquan.Util.FontTypePath;
import com.example.zhangxiu.birthdayquan.Widget.MTextView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by zhangxiu on 2016/10/20.
 */
public class LoveAdapter extends BaseAdapter{

    private  static  String TAG="LoveAdapter";
    Context mContext;

    public LoveAdapter(Context context) {
        this.mContext=context;

    }



    public  class  LoveViewHolder extends BaseAdapter.BaseViewHolder{

        public LoveCellModel getLoveCellModel() {
            return loveCellModel;
        }

        public void setLoveCellModel(LoveCellModel loveCellModel) {
            this.loveCellModel = loveCellModel;

            timeView.setText(loveCellModel.getTime());
            timeView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),FontTypePath.MHuaKangWaWaTi));

            addressView.setText(loveCellModel.getAddress());
            addressView.setTypeface(Typeface.createFromAsset(mContext.getAssets(),FontTypePath.MHuaKangWaWaTi));

            titleView.setText(loveCellModel.getName());
            titleView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), FontTypePath.MHuaKangWaWaTi));


            ImageOptions options= new ImageOptions.Builder()
                    .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
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
            x.image().bind(imageView,loveCellModel.getImageUrl(),options);




        }

        private LoveCellModel loveCellModel;
        private  ImageView imageView;
        private  TextView  timeView,addressView;

        private MTextView titleView;


        private  void initViewHolder(View itemView){
            imageView=(ImageView) itemView.findViewById(R.id.iv_love_head);
            titleView=(MTextView)itemView.findViewById(R.id.tv_love_title);
            timeView=(TextView)itemView.findViewById(R.id.tv_love_time);
            addressView=(TextView)itemView.findViewById(R.id.tv_love_address);
        }


       public LoveViewHolder(View itemView) {
           super(itemView);
            initViewHolder(itemView);
       }
   }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view= LinearLayout.inflate(mContext, R.layout.love_fragment_item,null);
       LoveViewHolder baseViewHolder=new LoveViewHolder(view);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
          LoveViewHolder loveViewHolder=(LoveViewHolder)holder;
          loveViewHolder.setLoveCellModel((LoveCellModel) getModels().get(position));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
