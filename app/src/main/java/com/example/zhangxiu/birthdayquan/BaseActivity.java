package com.example.zhangxiu.birthdayquan;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;
@ContentView(R.layout.activity_base)
public class BaseActivity extends AppCompatActivity {
      private static String TAG="BaseActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_base);
        x.view().inject(this);
        x.Ext.init(getApplication());

        //设置状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
    }


    /**
     * 获取屏幕的宽与高
     * @return int［］ 0：width 1:height
     */
    public   int[] screenRect(){
        int screenValue[]=new int[2];
        //获取屏幕的宽与高
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        screenValue[0]=width;
        screenValue[1]=height;
        return screenValue;
    }
}



