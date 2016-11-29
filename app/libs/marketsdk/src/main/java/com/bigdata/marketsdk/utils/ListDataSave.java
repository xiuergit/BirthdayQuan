package com.bigdata.marketsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ListDataSave {

    public  static  String CODEFILE="CODE_FILE";
    //code  code_map
    public   static  String  CODES_MAP="codes_map";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;  
  
    public ListDataSave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);  
        editor = preferences.edit();  
    }  
  
    /** 
     * 保存List 
     * @param tag 
     * @param datalist 
     */  
    public <T> void setDataList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)  
            return;  
  
        Gson gson = new Gson();
        //转换成json数据，再保存  
        String strJson = gson.toJson(datalist);  
        editor.clear();  
        editor.putString(tag, strJson);  
        editor.commit();  
  
    }  
  
    /** 
     * 获取List 
     * @param tag 
     * @return 
     */  
    public <T> List<T> getDataList(String tag) {  
        List<T> datalist=new ArrayList<T>();
        String strJson = preferences.getString(tag, null);  
        if (null == strJson) {  
            return datalist;  
        }  
        Gson gson = new Gson();  
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());  
        return datalist;  
  
    }

    /**
     * 保存MAP 集合    保存用户所选择的股票
     * @param tag
     * @param dataMap
     */
    public <T> void setDataMap(String tag, HashMap<String,String> dataMap) {

        if (null == dataMap || dataMap.size() <= 0)
        {
            editor.putString(tag,null);
            editor.commit();
        }

        else {
            String jsonStr = "{";

            Set<?> keySet = dataMap.keySet();
            for (Object key : keySet) {
                jsonStr += "\""+key+"\":\""+dataMap.get(key)+"\",";
            }
            jsonStr = jsonStr.substring(1,jsonStr.length()-2);
            jsonStr += "}";

            editor.putString(tag, jsonStr);
            editor.commit();}

    }

    /**
     * 获取MAP  获取用户选择的股票
     * @param tag
     * @return
     */
    public <T> HashMap<String,String> getDataMap(String tag) {
        HashMap<String,String> dataMap=new HashMap<String,String>();
        String strJson = preferences.getString(tag, null);

        if (null == strJson) {
            return dataMap;
        }else {
            String sb = strJson.substring(1, strJson.length() - 1);
            String[] name = sb.split("\\\",\\\"");
            String[] nn = null;

            for (int i = 0; i < name.length; i++) {
                nn = name[i].split("\\\":\\\"");
                dataMap.put(nn[0], nn[1]);
            }
        }

        return dataMap;

    }


    //保存当前股票状态
    public void setCodeState(String tag,boolean isAdd){
        editor.putBoolean(tag,isAdd);
        editor.commit();
    }
    //获取当前股票状态
    public  boolean getCodeState(String tag){
        return preferences.getBoolean(tag,true);
    }

}  