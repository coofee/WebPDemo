package com.coofee.webpdemo.webview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding mActivityWebViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityWebViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        mActivityWebViewBinding.setHandlers(this);

        mActivityWebViewBinding.activityWebViewWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    return TransferResource.replaceJpgAndPngWithWebp(url);
                }

                return super.shouldInterceptRequest(view, url);
            }
        });
        mActivityWebViewBinding.activityWebViewWebView.setWebChromeClient(new WebChromeClient() {

        });

        WebSettings settings = mActivityWebViewBinding.activityWebViewWebView.getSettings();
        settings.setJavaScriptEnabled(false);
//        settings.setLoadWithOverviewMode(true);
//        settings.setUseWideViewPort(true);

//        loadAssetsUrl();

        loadUrl();
    }


    View.OnClickListener mChangeUrlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadUrl();
        }
    };

    private final String[] sUrls = {
            "file:///android_asset/html/image_png_webp.html",
            "http://10.252.162.241:9999/image_png_webp.html"
    };

    private int index = 0;

    public void loadUrl() {
        String url = sUrls[index++ % sUrls.length];
        mActivityWebViewBinding.activityWebViewWebView.loadUrl(url);
    }

}
