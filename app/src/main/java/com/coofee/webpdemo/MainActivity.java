package com.coofee.webpdemo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.coofee.webpdemo.databinding.ActivityMainBinding;
import com.coofee.webpdemo.fresco.FrescoActivity;
import com.coofee.webpdemo.home.DynamicTabActivity;
import com.coofee.webpdemo.imageview.FrescoImageActivity;
import com.coofee.webpdemo.imageview.ImageActivity;
import com.coofee.webpdemo.ui.MultiItemRowSpan;
import com.coofee.webpdemo.ui.RecyclerColumnSpan;
import com.coofee.webpdemo.ui.RecyclerViewMultiItem;
import com.coofee.webpdemo.ui.RecyclerViewRowSpan;
import com.coofee.webpdemo.ui.RecyclerViewSnapDemo;
import com.coofee.webpdemo.webview.WebViewActivity;
import com.github.markzhai.recyclerview.SingleTypeAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mActivityMainBinding.activityMainWebpDemoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mActivityMainBinding.activityMainChangeStatusBarColor.setOnClickListener(new View.OnClickListener() {
            int origin = -1;
            int newBg = getResources().getColor(R.color.status_bar_bg);

            @Override
            public void onClick(View v) {
                // see https://github.com/jgilfelt/SystemBarTint
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    if (origin == -1) {
                        origin = window.getStatusBarColor();
                    }

                    if (origin == window.getStatusBarColor()) {
                        window.setStatusBarColor(newBg);
                    } else {
                        window.setStatusBarColor(origin);
                    }
                }
            }
        });

        mActivityMainBinding.activityMainChangeStatusBarTextColor.setOnClickListener(new View.OnClickListener() {

            int flagStatusBar = -1;
            int origin = -1;

            @Override
            public void onClick(View v) {
                // see https://github.com/jgilfelt/SystemBarTint
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    if (origin == -1) {
                        origin = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    }

                    if (origin == (window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)) {
                        flagStatusBar = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    } else {
                        flagStatusBar = origin;
                    }
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | flagStatusBar);
                }
            }
        });

        mActivityMainBinding.activityMainCreateViewInAsyncThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createViewInAsyncThread();
            }
        });

        SingleTypeAdapter<Demo> demoSingleTypeAdapter = new SingleTypeAdapter<Demo>(this, R.layout.main_demo_list);
        demoSingleTypeAdapter.setPresenter(new DemoPresenter());

        demoSingleTypeAdapter.add(new Demo("ImageView", ImageActivity.class));
        demoSingleTypeAdapter.add(new Demo("FrescoImage", FrescoImageActivity.class));

        demoSingleTypeAdapter.add(new Demo("WebView", WebViewActivity.class));
        demoSingleTypeAdapter.add(new Demo("Fresco", FrescoActivity.class));

        demoSingleTypeAdapter.add(new Demo("Dynamic Tabs", DynamicTabActivity.class));
        demoSingleTypeAdapter.add(new Demo("RecyclerView Column Span", RecyclerColumnSpan.class));
        demoSingleTypeAdapter.add(new Demo("RecyclerView Row Span", RecyclerViewRowSpan.class));
        demoSingleTypeAdapter.add(new Demo("RecyclerView Multi Item", RecyclerViewMultiItem.class));
        demoSingleTypeAdapter.add(new Demo("MultiItem Row Span", MultiItemRowSpan.class));
        demoSingleTypeAdapter.add(new Demo("RecyclerView Snap", RecyclerViewSnapDemo.class));


        mActivityMainBinding.activityMainWebpDemoList.setAdapter(demoSingleTypeAdapter);
    }

    public class DemoPresenter implements SingleTypeAdapter.Presenter<Demo> {

        @Override
        public void onItemClick(Demo demo) {
            if (demo.mActivityClass != null) {
                startActivity(new Intent(MainActivity.this, demo.mActivityClass));
            }
        }
    }

    private void createViewInAsyncThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final View rowSpan = getLayoutInflater()
                        .inflate(R.layout.activity_multi_item_row_span, null);
                rowSpan.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                        .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowSpan.measure(0, 0);
                rowSpan.layout(0, 0, rowSpan.getMeasuredWidth(), rowSpan.getMeasuredHeight());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mActivityMainBinding.activityMainViewContainer.addView(rowSpan);
                    }
                });
            }
        }).start();
    }
}
