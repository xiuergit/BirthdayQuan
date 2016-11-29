package com.bigdata.marketsdk.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.activity.MoreStockActivity;
import com.bigdata.marketsdk.module.MoreData;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;

/**
 * user:kun
 * Date:2016/11/4 or 下午5:18
 * email:hekun@gamil.com
 * Desc:
 */

public class StickyGridAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private List<MoreData.DATABean> dataBeanList;

    private LayoutInflater inflater;

    private GridView gridView;

    private Context context;

    public StickyGridAdapter(Context context, List<MoreData.DATABean> dataBeanList,
                             GridView gridView) {
        this.dataBeanList = dataBeanList;
        this.gridView = gridView;
        this.context = context;
        if (context != null) {
            inflater = LayoutInflater.from(context);
        }

    }

    @Override
    public long getHeaderId(int position) {
        return dataBeanList.get(position).getTYP();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder headerViewHolder;
        if (convertView == null) {
            headerViewHolder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.head, parent, false);
            headerViewHolder.mTextView = (TextView) convertView.findViewById(R.id.head);
            convertView.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }
        headerViewHolder.mTextView.setText(dataBeanList.get(position).getTYP_NAME());
        return convertView;
    }

    @Override
    public int getCount() {
        return dataBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHoder;
        if (view == null) {
            viewHoder = new ViewHolder();
            view = inflater.inflate(R.layout.item, viewGroup, false);
            viewHoder.textView = (Button) view.findViewById(R.id.text);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE); // 画框
            drawable.setStroke(2, Color.WHITE); // 边框粗细及颜色
            drawable.setColor(Color.BLACK); // 边框内部颜色
            viewHoder.textView.setBackground(drawable);
            view.setTag(viewHoder);
        } else {
            viewHoder = (ViewHolder) view.getTag();
        }
        viewHoder.textView.setText(dataBeanList.get(i).getSECT_NAME());
        viewHoder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("SECT_ID", dataBeanList.get(i).getSECT_ID());
                intent.putExtra("SECT_NAME", dataBeanList.get(i).getSECT_NAME());
                intent.setClass(context, MoreStockActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }

    public static class ViewHolder {
        public Button textView;
    }

    public static class HeaderViewHolder {
        public TextView mTextView;
    }
}
