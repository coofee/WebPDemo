package com.coofee.webpdemo.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by zhaocongying on 17/7/19.
 */

public class Utils {

    public static String getUserAgent(Context context) {
        String userAgent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
                Log.e("Utils", "getDefaultUserAgent, userAgent=" + userAgent);
                return userAgent;
            } catch (Throwable e) {
                Log.e("Utils", "getDefaultUserAgent.error", e);
            }
        }

        userAgent = new WebView(context).getSettings()
                .getUserAgentString();
        Log.e("Utils", "WebView.getUserAgentString, userAgent=" + userAgent);

//        userAgent = System.getProperty("http.agent");
//        Log.e("Utils", "System.getProperty, userAgent=" + userAgent);
//        if (userAgent != null) {
//            return userAgent;
//        }

        return userAgent;
    }
}
