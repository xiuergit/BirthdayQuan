package com.bigdata.marketsdk.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by windows on 2016/1/4.
 */
public class FenShiDatas {
    private String date;
    private long time;
    private String open;
    private String high;
    private String low;
    private double now;
    private String amount;
    private String volume;
    private double change;
    @SerializedName("volume-spread")
    private int volume_spread;

    public int getTrade_direction() {
        return trade_direction;
    }

    public void setTrade_direction(int trade_direction) {
        this.trade_direction = trade_direction;
    }

    public int getPosition_spread() {
        return position_spread;
    }

    public void setPosition_spread(int position_spread) {
        this.position_spread = position_spread;
    }

    public int getVolume_spread() {
        return volume_spread;
    }

    public void setVolume_spread(int volume_spread) {
        this.volume_spread = volume_spread;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    @SerializedName("trade-direction")

    private int trade_direction;
    @SerializedName("position-spread")
    private int position_spread;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public double getNow() {
        return now;
    }

    public void setNow(double now) {
        this.now = now;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Datas{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", now='" + now + '\'' +
                ", amount='" + amount + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }
}
