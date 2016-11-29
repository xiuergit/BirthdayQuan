package com.bigdata.marketsdk.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;


/**
 * user:kun
 * Date:2016/10/25 or 上午11:31
 * email:hekun@gamil.com
 * Desc: Activity基类
 */

public abstract class BaseActivity extends FragmentActivity {

    private Context context;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        baseCreate(savedInstanceState);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
        baseDestory();
    }


    public void openOnActivity(Context context, Class cls) {
        this.context = context;
        startActivity(new Intent(context, cls));
    }

    protected abstract void baseDestory();

    protected abstract void baseCreate(Bundle savedInstanceState);


}
