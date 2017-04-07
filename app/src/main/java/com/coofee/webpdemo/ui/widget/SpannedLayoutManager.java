package com.coofee.webpdemo.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by zhaocongying on 17/3/27.
 */

public class SpannedLayoutManager extends RecyclerView.LayoutManager {

    private final int mColumnCount;

    private int mItemViewWidth = -1;
    private int mItemViewHeight = -1;

    private int mRatio = -1;
    private SpanLookup mSpanLookup;

    public SpannedLayoutManager(int columnCount, int ratio) {
        this.mColumnCount = columnCount;
        this.mRatio = ratio;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.spanInfo = new SpanInfo(1, 1);
        return layoutParams;
    }

    public void setSpanLookup(SpanLookup spanLookup) {
        mSpanLookup = spanLookup;
    }

    public static interface SpanLookup {
        SpanInfo getSpanInfo(int position);
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {
        public int column;
        public int row;

        public SpanInfo spanInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }
    }


    public static final class SpanInfo {
        public int columnSpan;

        public int rowSpan;

        public SpanInfo(int columnSpan, int rowSpan) {
            this.columnSpan = columnSpan;
            this.rowSpan = rowSpan;
        }

//        public static final SpanInfo SINGLE = new SpanInfo(1, 1);
    }
}
