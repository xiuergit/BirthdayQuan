package com.example.zhangxiu.birthdayquan.Tool.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhangxiu.birthdayquan.R;
import com.example.zhangxiu.birthdayquan.Tool.Model.ToolMenuModel;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhangxiu on 2016/10/24.
 */
public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ToolViewHolder>{
    private static String TAG="ToolAdapter";


    private Context mContext;
    private ArrayList<ToolMenuModel> mModels;

    public ToolAdapter(ArrayList<ToolMenuModel> mModels, Context mContext) {
        this.mModels = mModels;
        this.mContext = mContext;
    }

    public  interface  OnItemClickListener{
        public  void  onItemClick(View view,int position);
        public  void  onItemLongClick(View view,int position);
    }
    private    OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public class ToolViewHolder extends RecyclerView.ViewHolder{

            private  TextView  mText;
            private  ImageView mImage;


        public ToolViewHolder(View itemView) {

            super(itemView);
            mText=(TextView) itemView.findViewById(R.id.tv_tool_title);
            mImage=(ImageView)itemView.findViewById(R.id.iv_tool_head);





        }
    }

    @Override
    public ToolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view= LayoutInflater.from(mContext).inflate(R.layout.tool_fragment_item,parent,false);
        return new ToolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ToolViewHolder holder, final int position) {
        ToolMenuModel menuModel=mModels.get(position%mModels.size());
        holder.mText.setText(menuModel.getName());
        holder.mImage.setImageResource(menuModel.getImageID());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick( holder.itemView,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return 100;
    }
}
