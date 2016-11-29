package com.example.zhangxiu.birthdayquan.Love.Model;

import java.util.ArrayList;

/**
 * Created by xiuer on 16/11/3.
 */

public class LoveModels {

    private ArrayList<LoveCellModel>values;

    public ArrayList<LoveCellModel> getValues() {
        return values;
    }

    public void setValues(ArrayList<LoveCellModel> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "LoveModels{" +
                "values=" + values +
                '}';
    }
}
