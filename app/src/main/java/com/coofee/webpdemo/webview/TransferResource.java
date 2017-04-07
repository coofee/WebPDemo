package com.coofee.webpdemo.webview;

import android.net.Uri;
import android.webkit.WebResourceResponse;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferOutputStream;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhaocongying on 17/2/23.
 */

public class TransferResource {

    public static WebResourceResponse replaceJpgAndPngWithWebp(String url) {

        // TODO check webview support webp; if support then replace jpg/png with webp;

        boolean isImage = false;
        // image;
        if (url.contains(".jpg")) {
            isImage = true;
            url = url.replace(".jpg", ".webp");
        } else if (url.contains(".jpeg")) {
            isImage = true;
            url = url.replace(".jpeg", ".webp");
        } else if (url.contains(".png")) {
            isImage = true;
            url = url.replace(".png", ".webp");
        } else if (url.contains(".webp")) {
            return null;
        }


        if (isImage) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                return new WebResourceResponse("image/webp", "utf-8", inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static void convertWebpToJpg(String url) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build();
        // get Bitmap;
//        DataSource<CloseableReference<CloseableImage>> closeableBitmap = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, null);
//        closeableBitmap.subscribe(new BaseBitmapDataSubscriber() {
//
//            @Override
//            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
//
//            }
//
//            @Override
//            protected void onNewResultImpl(Bitmap bitmap) {
//                // get bitmap;
//            }
//        }, UiThreadImmediateExecutorService.getInstance());
//        closeableBitmap.close();

        // get image origin bytes
        DataSource<CloseableReference<PooledByteBuffer>> closeableByte = Fresco.getImagePipeline().fetchEncodedImage(imageRequest, null);
        closeableByte.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                CloseableReference<PooledByteBuffer> result = dataSource.getResult();

                // TODO com.facebook.imagepipeline.producers.WebpTranscodeProducer.transcodeLastResult();
                // TODO remember this not support webp-animated;
//                PooledByteBufferOutputStream outputStream = mPooledByteBufferFactory.newOutputStream();
//                try {
//                    doTranscode(encodedImageCopy, outputStream);
//                    CloseableReference<PooledByteBuffer> ref =
//                            CloseableReference.of(outputStream.toByteBuffer());
//                    try {
//                        EncodedImage encodedImage = new EncodedImage(ref);
//                        encodedImage.copyMetaDataFrom(encodedImageCopy);
//                        return encodedImage;
//                    } finally {
//                        CloseableReference.closeSafely(ref);
//                    }
//                } finally {
//                    outputStream.close();
//                }

                // TODO com.facebook.imagepipeline.animated.factory.AnimatedImageFactoryImpl
                // TODO decode show gif or webp-animated; webview cannot support;
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {

            }
        }, UiThreadImmediateExecutorService.getInstance());

//        com.facebook.imagepipeline.nativecode.WebpTranscoderImpl
    }
}
