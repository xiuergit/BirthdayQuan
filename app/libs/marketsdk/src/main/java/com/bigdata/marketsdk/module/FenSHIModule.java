package com.bigdata.marketsdk.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * jsonBean
 */
public class FenSHIModule {


    private String PrevClose;
    private String PrevClsPrcFd;
    private String FundFlowContent;
    private String NewsRatingDate;
    private String NewsRatingLevel;
    private String NewsRatingName;

    public String getNewsRatingDate() {
        return NewsRatingDate;
    }

    public void setNewsRatingDate(String newsRatingDate) {
        NewsRatingDate = newsRatingDate;
    }

    public String getNewsRatingLevel() {
        return NewsRatingLevel;
    }

    public void setNewsRatingLevel(String newsRatingLevel) {
        NewsRatingLevel = newsRatingLevel;
    }

    public String getNewsRatingName() {
        return NewsRatingName;
    }

    public void setNewsRatingName(String newsRatingName) {
        NewsRatingName = newsRatingName;
    }

    public String getFundFlowContent() {
        return FundFlowContent;
    }

    public void setFundFlowContent(String fundFlowContent) {
        FundFlowContent = fundFlowContent;
    }

    public String getVolumeSpread() {
        return VolumeSpread;
    }

    public void setVolumeSpread(String volumeSpread) {
        VolumeSpread = volumeSpread;
    }

    public String getPE() {
        return PE;
    }

    public void setPE(String PE) {
        this.PE = PE;
    }

    public String getTtlShr() {
        return TtlShr;
    }

    public void setTtlShr(String ttlShr) {
        TtlShr = ttlShr;
    }

    private String VolumeSpread;
    private String PE;
    private String TtlShr;


    public String getPrevClsPrcFd() {
        return PrevClsPrcFd;
    }

    public void setPrevClsPrcFd(String prevClsPrcFd) {
        PrevClsPrcFd = prevClsPrcFd;
    }

    private String Open;
    private String code;
    private String Volume;
    private String Name;
    private String Change;
    @SerializedName("indicate-name")
    private String indicate_name;

    public String getIndicate_name() {
        return indicate_name;
    }

    public void setIndicate_name(String indicate_name) {
        this.indicate_name = indicate_name;
    }

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    public String getChangeRange() {
        return ChangeRange;
    }

    public void setChangeRange(String changeRange) {
        ChangeRange = changeRange;
    }

    private String ChangeRange;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Amount;

    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getHigh() {
        return High;
    }

    public void setHigh(String high) {
        High = high;
    }

    public String getLow() {
        return Low;
    }

    public void setLow(String low) {
        Low = low;
    }

    private String High;
    private String Low;


    private SerialsEntity serials;
    private String Now;

    public void setPrevClose(String PrevClose) {
        this.PrevClose = PrevClose;
    }

    public void setOpen(String Open) {
        this.Open = Open;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSerials(SerialsEntity serials) {
        this.serials = serials;
    }

    public void setNow(String Now) {
        this.Now = Now;
    }

    public String getPrevClose() {
        return PrevClose;
    }

    public String getOpen() {
        return Open;
    }

    public String getCode() {
        return code;
    }

    public SerialsEntity getSerials() {
        return serials;
    }

    public String getNow() {
        return Now;
    }

    public static class SerialsEntity {
        /**
         * date : 20160111
         * time : 91543000
         * now : 16.950000762939453
         * amount : 0
         * volume : 0
         */

        private List<DataEntity> data;

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public List<DataEntity> getData() {
            return data;
        }

        public static class DataEntity {
            private String date;
            private String time;
            private String open;
            private String high;
            private String low;


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

            private String now;
            private String amount;
            private String volume;

            public DataEntity(String now, String open, String low, String high, String amount, String volume) {
                this.now = now;
                this.high = high;
                this.low = low;
                this.open = open;
                this.amount = amount;
                this.volume = volume;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public void setNow(String now) {
                this.now = now;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public void setVolume(String volume) {
                this.volume = volume;
            }

            public String getDate() {
                return date;
            }

            public String getTime() {
                return time;
            }

            public String getNow() {
                return now;
            }

            public String getAmount() {
                return amount;
            }

            public String getVolume() {
                return volume;
            }


        }
    }
}
