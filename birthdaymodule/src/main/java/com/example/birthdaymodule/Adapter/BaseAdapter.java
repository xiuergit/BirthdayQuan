package com.example.birthdaymodule.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by xiuer on 16/11/3.
 */

public  class BaseAdapter  extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {


    private  static  String  TAG="BaseAdapter";


    public  interface  onClickLister{

        /**
         * 单击item 触发
         * @param itemView   单击的view
         * @param position   当前所在RecyclerView的位置
         */
         void onItemClickLister(View itemView,int position);

        /**
         * 长按item 触发
         * @param itemView  长按的view
         * @param position   当前所在RecyclerView的位置
         */
         void onItemLongClickLister(View itemView,int position);

    }


    public  class  BaseViewHolder extends  RecyclerView.ViewHolder{

        public BaseViewHolder(View itemView) {
            super(itemView);
            itemView.setFocusable(true);
            itemView.setEnabled(true);


            //Log.i(TAG, "BaseViewHolder: "+itemView.getWidth());
        }
    }

    public ArrayList<?> getModels() {
        return models;
    }

    public void setModels(ArrayList<?> models) {
        this.models = models;
    }

    private ArrayList<?>models;

    private  onClickLister mOnClickLister;

    public void setOnClickLister(BaseAdapter.onClickLister onClickLister) {
        this.mOnClickLister = onClickLister;
    }

    @Override
    public    BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         BaseViewHolder baseViewHolder=new BaseViewHolder(parent);

        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {


             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                    mOnClickLister.onItemClickLister(holder.itemView,position);
                 }
             });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                     mOnClickLister.onItemLongClickLister(holder.itemView,position);

                    return true;
                }
            });

    }



    @Override
    public int getItemCount() {

        return (models.size()>0?models.size():10);
    }
}
