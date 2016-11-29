package com.bigdata.marketsdk.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by windows on 2016/2/29.
 */
public class A_Codes {

    @SerializedName("sector-id") private String sector_id;
    @SerializedName("indicate-name") private String indicate_name;
    @SerializedName("sort-type") private String sort_type;

    public String getSector_id() {
        return sector_id;
    }

    public void setSector_id(String sector_id) {
        this.sector_id = sector_id;
    }

    public String getIndicate_name() {
        return indicate_name;
    }

    public void setIndicate_name(String indicate_name) {
        this.indicate_name = indicate_name;
    }

    public String getSort_type() {
        return sort_type;
    }

    public void setSort_type(String sort_type) {
        this.sort_type = sort_type;
    }

    private List<String> codes;

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }
}
