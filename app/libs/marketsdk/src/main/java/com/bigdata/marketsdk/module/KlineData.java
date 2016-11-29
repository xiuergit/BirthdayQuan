package com.bigdata.marketsdk.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by windows on 2016/1/4.
 */
public class KlineData {
    private String code;
    /**
     * 后台给的数据不规范，只能使用这种解析
     */
    @SerializedName("indicate-name")private String indicate_name;
    @SerializedName("begin-date")private String begin_date;
    @SerializedName("begin-time")private String begin_time;
    @SerializedName("number-type")private String number_type;
    @SerializedName("number-from-begin")private String number_from_begin;
    private Serials serials;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIndicate_name() {
        return indicate_name;
    }

    public void setIndicate_name(String indicate_name) {
        this.indicate_name = indicate_name;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setNumber_type(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getNumber_type() {
        return number_type;
    }

    public void setBegin_type(String begin_type) {
        this.number_type = begin_type;
    }

    public String getNumber_from_begin() {
        return number_from_begin;
    }

    public void setNumber_from_begin(String number_from_begin) {
        this.number_from_begin = number_from_begin;
    }

    public Serials getSerials() {
        return serials;
    }

    public void setSerials(Serials serials) {
        this.serials = serials;
    }

    @Override
    public String toString() {
        return "Kline{" +
                "code='" + code + '\'' +
                ", indicate_name='" + indicate_name + '\'' +
                ", begin_date='" + begin_date + '\'' +
                ", begin_time='" + begin_time + '\'' +
                ", begin_type='" + number_type + '\'' +
                ", number_from_begin='" + number_from_begin + '\'' +
                ", serials=" + serials +
                '}';
    }
}
