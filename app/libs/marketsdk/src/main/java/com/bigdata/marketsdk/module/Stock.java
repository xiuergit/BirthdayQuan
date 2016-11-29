package com.bigdata.marketsdk.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by windows on 2016/1/5.
 */
public class Stock {


    /**
     * PrevClose : 17.229999542236328
     * Amount : 551646016
     * Volume : 31982700
     * Open : 17.200000762939453
     * VolRatio : 0.688244451986041
     * PEttm : 7.4490057087281105
     * ChangeRange : 0.0046430601179038005
     * High : 17.329999923706055
     * Change : 0.07999992370605824
     * Low : 17.059999465942383
     * TtlShr : 25219845601
     * Eps : 1.9231
     * VolumeSpread : 2100
     * TtlShrNtlc : 25219845601
     * code : 600036.SH
     * Now : 17.309999465942383
     * ChangeHandsRate : 0.0012681560587639697
     */

    private double PrevClose;
    private long Amount;
    private long Volume;
    private double Open;
    private double VolRatio;
    @SerializedName("PEttm")private double Pettm;
    private double ChangeRange;
    private double High;
    private double Change;
    private double Low;
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private long TtlShr;
    private double Eps;
    private int VolumeSpread;
    private long TtlShrNtlc;
    private String code;
    private double Now;
    private double ChangeHandsRate;

    public double getPrevClose() {
        return PrevClose;
    }

    public void setPrevClose(double prevClose) {
        PrevClose = prevClose;
    }

    public long getAmount() {
        return Amount;
    }

    public void setAmount(long amount) {
        Amount = amount;
    }

    public long getVolume() {
        return Volume;
    }

    public void setVolume(long volume) {
        Volume = volume;
    }

    public double getOpen() {
        return Open;
    }

    public void setOpen(double open) {
        Open = open;
    }

    public double getVolRatio() {
        return VolRatio;
    }

    public void setVolRatio(double volRatio) {
        VolRatio = volRatio;
    }

    public double getPettm() {
        return Pettm;
    }

    public void setPettm(double pettm) {
        Pettm = pettm;
    }

    public double getChangeRange() {
        return ChangeRange;
    }

    public void setChangeRange(double changeRange) {
        ChangeRange = changeRange;
    }

    public double getHigh() {
        return High;
    }

    public void setHigh(double high) {
        High = high;
    }

    public double getChange() {
        return Change;
    }

    public void setChange(double change) {
        Change = change;
    }

    public double getLow() {
        return Low;
    }

    public void setLow(double low) {
        Low = low;
    }

    public long getTtlShr() {
        return TtlShr;
    }

    public void setTtlShr(long ttlShr) {
        TtlShr = ttlShr;
    }

    public double getEps() {
        return Eps;
    }

    public void setEps(double eps) {
        Eps = eps;
    }

    public int getVolumeSpread() {
        return VolumeSpread;
    }

    public void setVolumeSpread(int volumeSpread) {
        VolumeSpread = volumeSpread;
    }

    public long getTtlShrNtlc() {
        return TtlShrNtlc;
    }

    public void setTtlShrNtlc(long ttlShrNtlc) {
        TtlShrNtlc = ttlShrNtlc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getNow() {
        return Now;
    }

    public void setNow(double now) {
        Now = now;
    }

    public double getChangeHandsRate() {
        return ChangeHandsRate;
    }

    public void setChangeHandsRate(double changeHandsRate) {
        ChangeHandsRate = changeHandsRate;
    }
}
