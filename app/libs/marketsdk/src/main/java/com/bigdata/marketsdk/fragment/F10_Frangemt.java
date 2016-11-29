package com.bigdata.marketsdk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bigdata.marketsdk.Api;
import com.bigdata.marketsdk.R;


/**
 * F10Fragment
 */
public class F10_Frangemt extends Fragment {

    private static final String TAG="F10_Frangemt";

    private WebView webView;

    private String code;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       code= getArguments().getString("code");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView= (WebView) view.findViewById(R.id.web_view);
        initWebView();

    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本


        webView.loadUrl(Api.F10+code);

        webView.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_f10,container,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView=null;
    }
}
