package com.bigdata.marketsdk.Adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public abstract class AbsAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> datas;
    private int LayoutId;//item布局资源的id标识


    public AbsAdapter(Context context, List<T> datas, int layoutId) {
        super();
        this.context = context;
        this.datas = datas;
        this.LayoutId = layoutId;
    }

    public List<T> getCurData() {
        return datas;
    }

    public void setData(List<T> list) {
        datas.clear();
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<T> list) {
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(T ss,int position) {
        datas.add(position, ss);
        notifyDataSetChanged();
    }

    public void delItem(int position) {
        datas.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return datas.get(position);
    }

   

    @Override
    public long getItemId(int position) {

        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder vHolder=null;
        if (convertView==null) {
            convertView= LayoutInflater.from(context)
                    .inflate(LayoutId, parent, false);
            vHolder=new ViewHoder(convertView);
            convertView.setTag(vHolder);
        }else {
            vHolder=(ViewHoder) convertView.getTag();
        }
        showData(vHolder,getItem(position),position);
        return convertView;
    }
    public abstract void showData(ViewHoder vHolder,T data,int position);
    public static class ViewHoder{
        private SparseArray< View> mViews;
        private View itemView;
        public ViewHoder(View itemView) {
            this.mViews=new SparseArray<View>();
            this.itemView=itemView;
        }

        public  <T extends View>T getView(int id){
            View view=mViews.get(id);
            if (view==null) {
                view=itemView.findViewById(id);
                if (view!=null) {

                    mViews.put(id, view);
                }
            }
            return (T) view;
        }
        /**
         * @param viewId
         * @param text
         * @return
         */
        public ViewHoder setText(int viewId,String text) {
            TextView textView=getView(viewId);
            textView.setText(text);
            return this;
        }


        /**
         * 给view打标记
         * @param viewId
         * @param tag
         * @return
         */
        public ViewHoder setTag(int viewId, Object tag) {
            View view = getView(viewId);
            view.setTag(tag);
            return this;
        }

        /**
         * @param viewId
         * @param picId
         * @return
         */
        public ViewHoder setImageView(int viewId,int picId) {
            ImageView iView=getView(viewId);
            iView.setImageResource(picId);
            return this;
        }
    }
}
