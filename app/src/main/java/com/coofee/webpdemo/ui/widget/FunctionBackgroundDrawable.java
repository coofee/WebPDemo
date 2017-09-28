//package com.coofee.webpdemo.ui.widget;
//
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.ColorFilter;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.ColorInt;
//import android.support.annotation.IntRange;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
///**
// * Created by zhaocongying on 17/8/9.
// */
//
//public class FunctionBackgroundDrawable extends Drawable {
//    /**
//     * 背景色
//     */
//    public int background = Color.TRANSPARENT;
//
//    /**
//     * function 竖直分割线颜色
//     */
//    public int verticalDividerColor = Color.TRANSPARENT;
//    /**
//     * 水平分割线颜色
//     */
//    public int horizontalDividerColor = Color.TRANSPARENT;
//
//
//    public FunctionBackgroundDrawable() {
//    }
//
//    @Override
//    public void draw(@NonNull Canvas canvas) {
//
//    }
//
//    @Override
//    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
//        alpha += alpha >> 7;   // make it 0..256
//        final int baseAlpha = mColorState.mBaseColor >>> 24;
//        final int useAlpha = baseAlpha * alpha >> 8;
//        final int useColor = (mColorState.mBaseColor << 8 >>> 8) | (useAlpha << 24);
//        if (mColorState.mUseColor != useColor) {
//            mColorState.mUseColor = useColor;
//            invalidateSelf();
//        }
//    }
//
//    @Override
//    public void setColorFilter(@Nullable ColorFilter colorFilter) {
//
//    }
//
//    @Override
//    public int getOpacity() {
//        return 0;
//    }
//
//    public void setBackgroundColor(@ColorInt int color) {
//        this.background = color;
//    }
//
//    public void setVerticalDividerColor(@ColorInt int color) {
//        this.verticalDividerColor = color;
//    }
//
//    public void setHorizontalDividerColor(@ColorInt int color) {
//        this.horizontalDividerColor = color;
//    }
//
//    final static class ColorState {
//        int mBaseColor; // base color, independent of setAlpha()
//        int mUseColor;  // basecolor modulated by setAlpha()
//
//        int m
//    }
//}
