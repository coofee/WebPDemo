package com.coofee.webpdemo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by zhaocongying on 17/7/19.
 */

public class Utils {

    public static Drawable createBackground(int background, int horizontalDividerColor, int verticalDividerColor, int dividerWidth) {
        GradientDrawable horizontalDivider = new GradientDrawable();
        horizontalDivider.setColor(horizontalDividerColor);
        horizontalDivider.setShape(GradientDrawable.RECTANGLE);

        GradientDrawable verticalDivider = new GradientDrawable();
        verticalDivider.setColor(verticalDividerColor);
        verticalDivider.setShape(GradientDrawable.RECTANGLE);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(background);
        bg.setShape(GradientDrawable.RECTANGLE);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                horizontalDivider, verticalDivider, bg
        });
        layerDrawable.setLayerInset(1, 0, dividerWidth, 0, 0);
        layerDrawable.setLayerInset(2, 0, dividerWidth, dividerWidth, 0);

        return layerDrawable;
    }

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
