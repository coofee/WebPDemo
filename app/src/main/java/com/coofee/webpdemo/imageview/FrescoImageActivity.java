package com.coofee.webpdemo.imageview;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.databinding.ActivityFrescoImageBinding;
import com.coofee.webpdemo.fresco.FrescoActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

public class FrescoImageActivity extends AppCompatActivity {

    ActivityFrescoImageBinding frescoImageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        frescoImageBinding = DataBindingUtil.setContentView(this, R.layout.activity_fresco_image);
        frescoImageBinding.setHandlers(this);

        changeUrl(null);
    }

    private int index = 0;

    public void changeUrl(View view) {
        String imageRe = FrescoActivity.RandomTestShowImage.IMAGE_RES[index++ % FrescoActivity.RandomTestShowImage.IMAGE_RES.length];
        Log.e("fresco", "change url: " + imageRe);

        Uri imageUri = Uri.parse(imageRe);
        DraweeController animatedController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(imageUri)
                .build();
        frescoImageBinding.testShowImage.setController(animatedController);
    }
}
