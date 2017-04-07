package com.coofee.webpdemo.ui.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhaocongying on 17/3/28.
 */

public class ItemDecorationGridOver extends RecyclerView.ItemDecoration {

    private int spaceSize;
    private Paint paint;
    private float[] points = new float[10];

    public ItemDecorationGridOver(int spaceSize) {
        this.spaceSize = spaceSize;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(this.spaceSize);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View childAt = parent.getChildAt(i);
            points[0] = childAt.getLeft();
            points[1] = childAt.getTop();

            points[2] = childAt.getRight();
            points[3] = childAt.getTop();

            points[4] = childAt.getRight();
            points[5] = childAt.getBottom();

            points[6] = childAt.getLeft();
            points[7] = childAt.getBottom();

            points[8] = childAt.getLeft();
            points[9] = childAt.getTop();

            c.drawPoints(points, paint);
        }
    }
}
