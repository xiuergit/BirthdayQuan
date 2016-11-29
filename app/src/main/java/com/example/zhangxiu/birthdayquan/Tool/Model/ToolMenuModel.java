package com.example.zhangxiu.birthdayquan.Tool.Model;

/**
 * Created by zhangxiu on 2016/10/25.
 */
public class ToolMenuModel {

    private  String name;
    private  String imageUrl;
    private  int imageID;

    public ToolMenuModel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public ToolMenuModel(int imageID, String name) {
        this.imageID = imageID;
        this.name = name;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public ToolMenuModel(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "ToolMenuModel{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
