package com.example.zhangxiu.birthdayquan.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xiuer on 16/11/8.
 */

public class MTextView extends TextView {

    private  static  String TAG="MTextView";

    public MTextView(Context context) {
        super(context);
    }

    public MTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

}
