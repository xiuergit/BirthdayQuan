package com.bigdata.marketsdk.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 * user:kun
 * Date:2016/10/25 or 上午11:55
 * email:hekun@gamil.com
 * Desc: fragment基类
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseFgDestory();
    }

    protected abstract void BaseFgDestory();
}
