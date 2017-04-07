package com.coofee.webpdemo.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.utils.BackgroundResources;

import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImageActivity extends AppCompatActivity {
    @InjectView(R.id.root_layout)
    LinearLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                Log.e("ImageActivity", "setFactory; name=" + name);
                findWebpSrc(context, attrs);

                return getDelegate().createView(parent, name, context, attrs);
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ButterKnife.inject(this);

        final int childCount = mRootLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mRootLayout.getChildAt(i).setBackgroundResource(BackgroundResources.nextBackgroundResource());
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        getEntryImageExtension();
        return super.onCreateView(name, context, attrs);
    }

    private void getEntryImageExtension() {
        TypedValue typedValue = new TypedValue();
        getResources().getValue(R.drawable.sample_webp_lossy_with_alpha, typedValue, true);
        Log.e("ImageActivity", typedValue.string.toString());
    }

    private void findWebpSrc(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Webp);
        try {
            int srcResId = a.getResourceId(R.styleable.Webp_webp_src, -1);
            if (srcResId != -1) {
                Log.e("ImageActivity", "find Webp_src; " + srcResId + "=" + srcResId);
                TypedValue src = new TypedValue();
                getResources().getValue(srcResId, src, true);
                if (src.string.toString().endsWith(".webp")) {
                    Log.e("ImageActivity", "find Webp_src; " + srcResId + "=" + srcResId + ", file=" + src.string);
                    InputStream inputStream = getResources().openRawResource(srcResId);
//                    WebpTranscoderFactory.getWebpTranscoder().transcodeWebpToPng(inputStream, );
                }
            }
        } finally {
            a.recycle();
        }
    }
}
