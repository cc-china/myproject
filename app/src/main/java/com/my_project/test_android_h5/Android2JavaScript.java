package com.my_project.test_android_h5;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.my_project.R;

/**
 * Created by Administrator on 2018\11\28 0028.
 */

public class Android2JavaScript extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setView());
        String TAG = this.getLocalClassName();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int heightPixels = dm.heightPixels;
        int widthPixels = dm.widthPixels;
        float density = dm.density;
        float heightDP = heightPixels / density;
        float widthDP = widthPixels / density;
        Log.e(TAG,"widthDP   "+widthDP+"\n"+
                "heightDP   "+heightDP+"\n"+
                "density   "+density+"\n"+
                "widthPixels   "+widthPixels+"\n"+
                "heightPixels   "+heightPixels);

        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        handler.sendMessage(new Message());

    }
Handler handler = new Handler();
    private View setView() {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        Button b1 = new Button(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        b1.setLayoutParams(params1);
        b1.setText("Android调用H5页面");
        b1.setId(R.id.btn_1);
        layout.addView(b1);
        webView = new WebView(this);
        webView.setLayoutParams(params);
        layout.addView(webView);

        initView(b1, webView);
        return layout;
    }

    @SuppressLint("JavascriptInterface")
    private void initView(Button b1, final WebView webView) {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:setRed()V=1");
                webView.loadUrl("javascript:setAlert()");
            }
        });
        webView.loadUrl("file:///android_asset/android2javascript.html");
        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
//        set.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView.addJavascriptInterface(new IAndroid(), "button");
    }

    public class IAndroid {
        @JavascriptInterface
        public void androidFunc() {
            Toast.makeText(Android2JavaScript.this,
                    "js调用Android方法", Toast.LENGTH_SHORT).show();
        }
    }
}
