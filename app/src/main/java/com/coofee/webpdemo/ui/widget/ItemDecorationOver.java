package com.coofee.webpdemo.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhaocongying on 17/3/25.
 */

public class ItemDecorationOver extends RecyclerView.ItemDecoration {

    private int mSpaceSize;
    private int mColumnCount;

    private int mFirstAndLastMargin = -1;
    private int mMiddleMargin = -1;


    public ItemDecorationOver(int spaceSize, int columnCount) {
        this.mSpaceSize = spaceSize;
        this.mColumnCount = columnCount;
    }

    private boolean mHeaderViewWithOffset = false;

    public void setHeaderViewWithOffset(boolean withOffset) {
        mHeaderViewWithOffset = withOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!(parent.getLayoutManager() instanceof SpannedGridLayoutManager)) {
            return;
        }

        int position = parent.getChildAdapterPosition(view);
        SpannedGridLayoutManager.GridCell lp = ((SpannedGridLayoutManager) parent.getLayoutManager()).getGridCell(position);

        if (mFirstAndLastMargin == -1) {
            final int itemViewDecorationWith = (int) ((float) parent.getWidth() / mColumnCount);
            final int itemViewWidth = (int) ((parent.getWidth() - (float) (mSpaceSize * (mColumnCount + 1))) / mColumnCount);

            mFirstAndLastMargin = itemViewDecorationWith - itemViewWidth - mSpaceSize;
            mMiddleMargin = (int) ((itemViewDecorationWith - itemViewWidth) / 2.0f);
        }

        outRect.top = mSpaceSize;
        if (lp.column == 0) {
            // left;
            outRect.left = mSpaceSize;

            if (lp.column + lp.columnSpan == mColumnCount) {
                // == 1;
                outRect.right = mSpaceSize;
            } else if (lp.column + lp.columnSpan == mColumnCount - 1) {
                // == 2;
                outRect.right = mMiddleMargin;
            } else {
                // >= 3;
                outRect.right = mFirstAndLastMargin;
            }
        } else if ((lp.column + lp.columnSpan) == mColumnCount) {
            // right;
            outRect.right = mSpaceSize;

            if (mColumnCount - lp.column == 1) {
                // == 2;
                outRect.left = mMiddleMargin;
            } else {
                // >= 3;
                outRect.left = mFirstAndLastMargin;
            }
        } else {
            // middle;
            outRect.left = mMiddleMargin;
            outRect.right = mMiddleMargin;
        }
    }

}
