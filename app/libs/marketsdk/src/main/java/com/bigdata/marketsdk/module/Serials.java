package com.bigdata.marketsdk.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by windows on 2016/1/4.
 */
public class Serials {

    @SerializedName("before-count")private String before_count;
    @SerializedName("after-count")private String after_count;
    private List<FenShiDatas> data;

    public String getBefore_count() {
        return before_count;
    }

    public void setBefore_count(String before_count) {
        this.before_count = before_count;
    }

    public String getAfter_count() {
        return after_count;
    }

    public void setAfter_count(String after_count) {
        this.after_count = after_count;
    }

    public List<FenShiDatas> getData() {
        return data;
    }

    public void setData(List<FenShiDatas> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Serials{" +
                "before_count='" + before_count + '\'' +
                ", after_count='" + after_count + '\'' +
                ", data=" + data +
                '}';
    }
}
