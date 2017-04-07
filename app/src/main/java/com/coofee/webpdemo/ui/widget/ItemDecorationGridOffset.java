package com.coofee.webpdemo.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhaocongying on 17/3/28.
 */

public class ItemDecorationGridOffset extends RecyclerView.ItemDecoration {

    private int columnCount;
    private int originSpaceSize;
    private int halfSpaceSize;

    public ItemDecorationGridOffset(int spaceSize) {
        this.originSpaceSize = spaceSize;
        this.halfSpaceSize = (int) (spaceSize / 2.0f);

    }

    public void setColumnCount(int columnCount) {

        this.columnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(halfSpaceSize, halfSpaceSize, halfSpaceSize, halfSpaceSize);


//        int childAdapterPosition = parent.getChildAdapterPosition(view);
//        if (childAdapterPosition % columnCount == 0) {
//            outRect.set(originSpaceSize, halfSpaceSize, halfSpaceSize, halfSpaceSize);
//        } else if ((childAdapterPosition + 1) % columnCount == 0) {
//            outRect.set(halfSpaceSize, halfSpaceSize, originSpaceSize, halfSpaceSize);
//        } else {
//            outRect.set(halfSpaceSize, halfSpaceSize, halfSpaceSize, halfSpaceSize);
//        }
    }
}
