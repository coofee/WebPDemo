package com.coofee.webpdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class Demo extends BaseObservable {

    public String title;
    public Class<?> mActivityClass;

    public Demo() {
    }

    public Demo(String title, Class<?> mActivityClass) {
        this.title = title;
        this.mActivityClass = mActivityClass;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

}