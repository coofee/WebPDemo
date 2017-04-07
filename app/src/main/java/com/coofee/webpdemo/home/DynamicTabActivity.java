package com.coofee.webpdemo.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.databinding.ActivityDynamicTabBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DynamicTabActivity extends AppCompatActivity {
    private static final String DEFAULT_URI = "wbmain://jump/core/main?params=" +
            "{" + "\"tab\":\"messageCenter\"," +
            "\"tabs\":[\"home\",\"discovery\",\"messageCenter\",\"publish\",\"personCenter\"]\n" +
            "}";

    private static final String DEFAULT_URI_PREFIX = "wbmain://jump/core/main?params=";

    private static final ArrayList<String> DEFAULT_TABS = new ArrayList<String>();

    ActivityDynamicTabBinding dynamicTabBinding;

    private JumpInfo jumpInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dynamicTabBinding = DataBindingUtil.setContentView(this, R.layout.activity_dynamic_tab);
        dynamicTabBinding.setDefaultUri(DEFAULT_URI);
        dynamicTabBinding.setHandlers(this);

        DEFAULT_TABS.add("home");
        DEFAULT_TABS.add("discovery");
        DEFAULT_TABS.add("messageCenter");
        DEFAULT_TABS.add("publish");
        DEFAULT_TABS.add("personCenter");

        jumpInfo = new JumpInfo("messageCenter", new ArrayList<>(DEFAULT_TABS));
    }

    public void jump() {
        Editable text = dynamicTabBinding.activityDynamicTabUri.getText();
        if (text != null) {
            Uri uri = Uri.parse(text.toString());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    public void add() {
        jumpInfo.add();
        dynamicTabBinding.activityDynamicTabUri.setText(DEFAULT_URI_PREFIX + jumpInfo);
    }

    public void shuffle() {
        jumpInfo.shuffle();
        dynamicTabBinding.activityDynamicTabUri.setText(DEFAULT_URI_PREFIX + jumpInfo);

    }

    public void remove() {
        jumpInfo.remove();
        dynamicTabBinding.activityDynamicTabUri.setText(DEFAULT_URI_PREFIX + jumpInfo);
    }

    public static class JumpInfo {
        public String tab;

        public ArrayList<String> tabs;

        public JumpInfo(String tab, ArrayList<String> tabs) {
            this.tab = tab;
            this.tabs = tabs;
        }

        void shuffle() {
            if (tabs != null && tabs.size() >= 1) {
                Collections.shuffle(tabs);
                tab = tabs.get(0);
            }
        }

        void add() {
            if (tabs != null && tabs.size() >= 1) {
                Collections.shuffle(tabs);
                int randomIndex = new Random().nextInt(DEFAULT_TABS.size());
                tabs.add(DEFAULT_TABS.get(randomIndex));
                tab = tabs.get(0);
            }
        }

        void remove() {
            if (tabs != null && tabs.size() >= 1) {
                Collections.shuffle(tabs);
                tab = tabs.get(0);
                tabs.remove(0);
            }
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

}
