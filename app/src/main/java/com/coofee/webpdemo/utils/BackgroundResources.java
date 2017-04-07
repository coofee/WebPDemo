package com.coofee.webpdemo.utils;

/**
 * Created by zhaocongying on 17/2/20.
 */

public class BackgroundResources {
    private static final int[] sBackgroundResources = new int[]{
            android.R.color.holo_green_dark,
            android.R.color.holo_red_dark,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
    };


    private static int sNext = -1;

    public static int nextBackgroundResource() {
        sNext++;

        return sBackgroundResources[sNext % sBackgroundResources.length];
    }
}
