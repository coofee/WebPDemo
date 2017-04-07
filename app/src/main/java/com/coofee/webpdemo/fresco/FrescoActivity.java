package com.coofee.webpdemo.fresco;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.coofee.webpdemo.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.lang.ref.WeakReference;

public class FrescoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        DemoApplication.initFresco(this);

        setContentView(R.layout.activity_fresco);


        findViewAndLoadUri(R.id.baseline_jpeg, "https://www.gstatic.com/webp/gallery/1.sm.jpg");

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(
                Uri.parse("http://pooyak.com/p/progjpeg/jpegload.cgi?o=1"))
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setRetainImageOnFailure(true)
                .build();
        findViewAndSetController(R.id.progressive_jpeg, controller);

        findViewAndLoadUri(R.id.static_webp, "https://www.gstatic.com/webp/gallery/2.sm.webp");


//        findViewAndLoadUri(R.id.alpha_webp, "http://frescolib.org/static/translucent.webp")
//                .getHierarchy()
//                .setBackgroundImage(new ColorDrawable(0xFF302013));
        Uri webpAlpha = Uri.parse("asset:///html/sample_webp_lossy_with_alpha.webp");
        findViewAndLoadUri(R.id.alpha_webp, webpAlpha)
                .getHierarchy();

        findViewAndLoadAnimatedImageUri(
                R.id.animated_gif,
                "https://s3.amazonaws.com/giphygifs/media/4aBQ9oNjgEQ2k/giphy.gif");

        findViewAndLoadAnimatedImageUri(
                R.id.animated_webp,
                "https://www.gstatic.com/webp/animated/1.webp");

        findViewAndLoadAnimatedImageUri(
                R.id.one_loop_animated_webp,
                "https://dl.dropboxusercontent.com/u/6610969/webp_180_example.webp");

        String dataUri = "data:image/webp;base64," +
                "UklGRjgCAABXRUJQVlA4WAoAAAAQAAAAFwAAFwAAQUxQSC0AAAABJ6AgbQNm1+EkTnRExDkWahpJgRIk" +
                "oOCVgAKy/mVdSxvR/wyEHWJ49xCbCAcAVlA4IOQBAACQCgCdASoYABgAPlEkjkWjoiEUBAA4BQS2AE6Z" +
                "Qjgbyv8ZuWF3B3ANsBdoHoAeWj+s3wleTHcua7PMAYrNOLPHFqAbpKGWe8x3KqHen7YXTMnmq/c9GqBt" +
                "ZtuQ0AAA/r031iZbkliICmd/QSg0OjEWbX/nv8v+g4UDPpobcehywI6oypX8hbuzcQndgaVt0zW5DiZP" +
                "6Ueo/21IPqsuRm1WyZHL3bJIFStwH8BOWif7xVniUiHwD5HwW8AXIZiq2maDmyIvxn4a0fetR+flTrt/" +
                "5/Vq3BVTeorYBHMN7L09DE9xDW/2+dj45/mCe9vjNUGRpT5EJhV8jDz/ZxPixLvN9Tl5iPD/neh1RCl6" +
                "AOcx3JudnAseXqvm8dEtF+rA40Bg881EW88XwU1oXf/5RY/4ToF9NwcXPLC/AodLaAFPpiXt+C6cFDIj" +
                "+uqi12PWFO+p7jn1P+sjCpbP/OBdHIoez8Rp6nslBEiFG19LKqv6dkGzLKtvt9dRIpz2sef2JFUVB+v+" +
                "hvMMmQ4o6d8aMTGuv/4wZxogl/n/k3g83NO3bBnf7/TL8Baf97pQw43+FVR0hXfpvD0k51yE35e2jNF/" +
                "98Uv3fAfPXw0T8irZQR4r1/ktG5xrypg/aKDooBtoI5aQAAA";
        findViewAndLoadUri(R.id.data_webp, dataUri);


        testShowImage();
    }

    private RandomTestShowImage mRandomTestShowImage = null;

    private void testShowImage() {
        SimpleDraweeView testShowImage = (SimpleDraweeView) findViewById(R.id.test_show_image);

        mRandomTestShowImage = new RandomTestShowImage(testShowImage);
        mRandomTestShowImage.show();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mRandomTestShowImage != null) {
            mRandomTestShowImage.show();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRandomTestShowImage != null) {
            mRandomTestShowImage.recycle();
        }
    }

    public static class RandomTestShowImage {

        /**
         * uri type             schema
         * <p>
         * network              http://, https:// HttpURLConnection or network layer
         * file                 file:// FileInputStream
         * content provider     content://	ContentResolver
         * asset in app         asset://	AssetManager
         * resource in app      res:// as in res:///12345	Resources.openRawResource
         * Data in URI	        data:mime/type;base64,	Following data URI spec (UTF-8 only)
         */
        public static final String[] IMAGE_RES = {
                "http://10.252.162.241:9999/sample_png.png",
                "http://10.252.162.241:9999/sample_png.webp",
                "http://10.252.162.241:9999/sample_webp_lossless.webp",
                "http://10.252.162.241:9999/sample_webp_lossy_with_alpha.webp",
                "http://10.252.162.241:9999/sample_animated_webp.webp",

                "asset:///html/sample_png.png",
                "asset:///html/sample_webp_lossless.webp",
                "asset:///html/sample_webp_lossy_with_alpha.webp",
                "asset:///html/sample_animated_webp.webp",

                "res:///" + R.drawable.sample_png,
                "res:///" + R.drawable.sample_webp_lossless,
                "res:///" + R.drawable.sample_webp_lossy_with_alpha,
                "res:///" + R.drawable.sample_animated_webp,
        };

        private Runnable changeImageTask = new Runnable() {
            private int index = -1;

            @Override
            public void run() {
                SimpleDraweeView simpleDraweeView = draweeRef.get();
                if (simpleDraweeView != null) {
                    index++;
                    String url = IMAGE_RES[index % IMAGE_RES.length];
                    simpleDraweeView.setImageURI(Uri.parse(url));
                }
            }
        };

        WeakReference<SimpleDraweeView> draweeRef = null;
        Handler handler = new Handler();

        public RandomTestShowImage(SimpleDraweeView draweeView) {
            this.draweeRef = new WeakReference<SimpleDraweeView>(draweeView);
        }

        private void show() {
            if (draweeRef == null) {
                return;
            }

            SimpleDraweeView simpleDraweeView = draweeRef.get();
            if (simpleDraweeView != null) {
                handler.post(changeImageTask);
                simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.post(changeImageTask);
                    }
                });
            }
        }

        private void recycle() {
            if (draweeRef == null) {
                return;
            }

            SimpleDraweeView simpleDraweeView = draweeRef.get();
            if (simpleDraweeView == null) {
                return;
            }

            handler.removeCallbacks(changeImageTask);
        }
    }


    private SimpleDraweeView findAndPrepare(@IdRes int viewId) {
        SimpleDraweeView view = (SimpleDraweeView) findViewById(viewId);
        view.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        return view;
    }

    private SimpleDraweeView findViewAndLoadUri(@IdRes int viewId, String uri) {
        SimpleDraweeView view = findAndPrepare(viewId);
        view.setImageURI(Uri.parse(uri));
        return view;
    }

    private SimpleDraweeView findViewAndLoadUri(@IdRes int viewId, Uri uri) {
        SimpleDraweeView view = findAndPrepare(viewId);
        view.setImageURI(uri);
        return view;
    }

    private SimpleDraweeView findViewAndSetController(
            @IdRes int viewId,
            DraweeController controller) {
        SimpleDraweeView view = findAndPrepare(viewId);
        view.setController(controller);
        return view;
    }

    private SimpleDraweeView findViewAndLoadAnimatedImageUri(@IdRes int viewId, String uri) {
        DraweeController animatedController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(Uri.parse(uri))
                .build();
        return findViewAndSetController(viewId, animatedController);
    }
}
