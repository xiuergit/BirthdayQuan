package com.example.zhangxiu.birthdayquan.Love.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.birthdaymodule.Bean.BaseModel;

import java.util.ArrayList;

/**
 * recycle view  的每一项 model
 * Created by zhangxiu on 2016/10/20.
 */
public class LoveCellModel extends BaseModel implements Parcelable{
    private  String imageUrl;
    private  String time;
    private  String address;
    private  String name;
    private  String describe;


    private ArrayList<String> imageUrls;



    protected LoveCellModel(Parcel in) {
        imageUrl = in.readString();
        time = in.readString();
        address = in.readString();
        name = in.readString();
        describe = in.readString();
        imageUrls = in.createStringArrayList();


    }

    public static final Creator<LoveCellModel> CREATOR = new Creator<LoveCellModel>() {
        @Override
        public LoveCellModel createFromParcel(Parcel in) {
            return new LoveCellModel(in);
        }

        @Override
        public LoveCellModel[] newArray(int size) {
            return new LoveCellModel[size];
        }
    };

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }





    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "LoveCellModel{" +
                "imageUrl='" + imageUrl + '\'' +
                ", time='" + time + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", imageUrls=" + imageUrls +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(time);
        parcel.writeString(address);
        parcel.writeString(name);
        parcel.writeString(describe);
        parcel.writeStringList(imageUrls);

    }
}
