package com.example.zhangxiu.birthdayquan.Memory.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birthdaymodule.Adapter.BaseAdapter;
import com.example.zhangxiu.birthdayquan.Love.Model.LoveCellModel;
import com.example.zhangxiu.birthdayquan.R;
import com.example.zhangxiu.birthdayquan.Util.FontTypePath;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by xiuer on 16/11/14.
 */

public class MemoryAdapter extends BaseAdapter {
    private  String TAG="MemoryAdapter";

    private Context mContext;

    public MemoryAdapter(Context mContext) {
        this.mContext=mContext;

    }


    public  class  MemoryViewHolder extends  BaseAdapter.BaseViewHolder{

        private TextView mTitle;
        private ImageView mImage;
        private TextView mTime;

        //用来测试 memory
        private LoveCellModel model;

        public LoveCellModel getModel() {
            return model;
        }

        public void setModel(LoveCellModel model) {
            this.model = model;
            mTime.setText(model.getTime());
            mTitle.setText(model.getName());
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
            x.image().bind(mImage,model.getImageUrl(),options);


        }

        public MemoryViewHolder(View itemView) {
            super(itemView);
            initViewHolder(itemView);

        }

        public  void  initViewHolder(View itemView){
            mTitle=(TextView) itemView.findViewById(R.id.tv_memory_title_left);

            mTitle.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    FontTypePath.MHuaKangWaWaTi));

            mTitle.setTextSize(20);

            mTime=(TextView)itemView.findViewById(R.id.tv_memory_time_left);

            mTime.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    FontTypePath.MHuaKangWaWaTi));
            mTime.setTextSize(20);
            mImage=(ImageView)itemView.findViewById(R.id.iv_memory_left);





        }
    }



    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        super.onBindViewHolder(holder,position);
        MemoryViewHolder memoryViewHolder=(MemoryViewHolder)holder;
        memoryViewHolder.setModel((LoveCellModel) getModels().get(position));

          View view=memoryViewHolder.itemView;

       // Log.i(TAG, "onBindViewHolder: width:"+view.getWidth()+
         //       "position:"+position);


    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        //Log.i(TAG, "onCreateViewHolder: "+parent);


        LayoutInflater inflater= LayoutInflater.from(mContext);


      //  View view=inflater.inflate(R.layout.fragment_memory_details_ly,null);


        View view=inflater.inflate(R.layout.memory_details_item_left,null);

        MemoryViewHolder viewHolder=new MemoryViewHolder(view);


        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return  getModels().size();
    }


}
