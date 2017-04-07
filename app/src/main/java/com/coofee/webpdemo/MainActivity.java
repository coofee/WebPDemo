package com.coofee.webpdemo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

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
}
