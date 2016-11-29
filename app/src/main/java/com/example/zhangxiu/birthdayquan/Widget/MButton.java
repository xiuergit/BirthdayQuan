package com.example.zhangxiu.birthdayquan.Widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.zhangxiu.birthdayquan.Util.FontTypePath;

/**
 * Created by xiuer on 16/11/9.
 */

public class MButton extends Button {

   public void  init(Context context){
       this.setTypeface(Typeface.createFromAsset(context.getAssets(), FontTypePath.MWeiRuanYaHei));
       this.setTextColor(Color.WHITE);
       this.setTextSize(24);
       this.setBackgroundColor(Color.BLACK);
   }

    public MButton(Context context) {
        super(context);
        init(context);
    }

    public MButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

}
