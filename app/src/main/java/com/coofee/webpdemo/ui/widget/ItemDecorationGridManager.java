package com.coofee.webpdemo.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecorationGridManager extends RecyclerView.ItemDecoration {

    private int mSpaceSize;
    private int mColumnCount;

    private int mFirstAndLastMargin = -1;
    private int mMiddleMargin = -1;

    private boolean mHeaderViewWithOffset = false;

    public ItemDecorationGridManager(int spaceSize, int columnCount) {
        mSpaceSize = spaceSize;
        mColumnCount = columnCount;
    }

    public void setHeaderViewWithOffset(boolean withOffset) {
        mHeaderViewWithOffset = withOffset;
    }

//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
//        if (itemPosition == 0) {
//            // header;
//            if (mHeaderViewWithOffset) {
//                outRect.top = mSpaceSize;
//                outRect.left = mSpaceSize;
//                outRect.right = mSpaceSize;
//            }
//            return;
//        }
//
//        if (mFirstAndLastMargin == -1) {
//            final int itemViewDecorationWith = (int) ((float) parent.getWidth() / mColumnCount);
//            final int itemViewWidth = (int) ((parent.getWidth() - (float) (mSpaceSize * (mColumnCount + 1))) / mColumnCount);
//
//            mFirstAndLastMargin = itemViewDecorationWith - itemViewWidth - mSpaceSize;
//            mMiddleMargin = (int) ((itemViewDecorationWith - itemViewWidth) / 2.0f);
//
//            Log.e("ItemDecoration", "itemViewDecorationWith=" + itemViewDecorationWith + ", itemViewWidth=" + itemViewWidth + ", mSpaceSize=" + mSpaceSize + ", mFirstAndLastMargin=" + mFirstAndLastMargin + ", mMiddleMargin=" + mMiddleMargin);
//        }
//
//        outRect.top = mSpaceSize;
//
//        if (mColumnCount == 1) {
//            outRect.left = mSpaceSize;
//            outRect.right = mSpaceSize;
//
//        } else {
//
//            if ((itemPosition - 1) % mColumnCount == 0) {
//                // left;
//                outRect.left = mSpaceSize;
//                outRect.right = mFirstAndLastMargin;
//            } else if (itemPosition % mColumnCount == 0) {
//                // right;
//                outRect.right = mSpaceSize;
//                outRect.left = mFirstAndLastMargin;
//            } else {
//                // middle;
//                outRect.left = mMiddleMargin;
//                outRect.right = outRect.left;
//            }
//        }
//
//        outRect.bottom = 0;
//        final int itemCount = state.getItemCount() - 1;
//        final int mod = itemCount % mColumnCount;
//        final int lastRowItemStart = itemCount - (mod == 0 ? mColumnCount : mod);
//        if (itemPosition > lastRowItemStart) {
//            // last row;
//            outRect.bottom = mSpaceSize;
//        }
//    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            return;
        }

        position = position - 1;
        int column = position % mColumnCount;

        outRect.left = mSpaceSize - column * mSpaceSize / mColumnCount;
        outRect.right = (column + 1) * mSpaceSize / mColumnCount;

        if (position < mColumnCount) {
            outRect.top = mSpaceSize;
        }
        outRect.bottom = mSpaceSize;
    }
}