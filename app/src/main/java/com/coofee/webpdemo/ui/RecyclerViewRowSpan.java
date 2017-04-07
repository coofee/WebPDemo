package com.coofee.webpdemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.ui.widget.ItemDecorationGridOffset;
import com.coofee.webpdemo.utils.BackgroundResources;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecyclerViewRowSpan extends AppCompatActivity {

    private static final int COLUMN_COUNT = 4;
    private static final int SPACE = 20;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_spanned_demo);
        ButterKnife.inject(this);

        final int rowCount = COLUMN_COUNT;
//        final SpannedGridLayoutManager gridLayoutManager = new SpannedGridLayoutManager(new SpannedGridLayoutManager.GridSpanLookup() {
//            private SpannedGridLayoutManager.SpanInfo ITEM_BIG = new SpannedGridLayoutManager.SpanInfo(2, 2);
//            private SpannedGridLayoutManager.SpanInfo ITEM_NORMAL = new SpannedGridLayoutManager.SpanInfo(1, 1);
//
//            @Override
//            public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
//                if (position == 0) {
//                    return ITEM_BIG;
//                }
//                return ITEM_NORMAL;
//            }
//        }, rowCount, 1.0f);

//        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, rowCount);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if (position == 0) {
//                    return COLUMN_COUNT / 2;
//                }
//                return 1;
//            }
//        });

        final StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(rowCount, StaggeredGridLayoutManager.VERTICAL);

        final int padding = (int) (SPACE / 2.0f);
        mRecyclerView.setPadding(padding, padding, padding, padding);
        mRecyclerView.addItemDecoration(new ItemDecorationGridOffset(SPACE));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        mRecyclerView.addItemDecoration(new ItemDecorationOver(SPACE, rowCount));

        MyAdapter adapter = new MyAdapter(this);
        mRecyclerView.setAdapter(adapter);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<RecyclerColumnSpan.ViewHolder> {
        List<Integer> mDatas;

        private LayoutInflater mLayoutInflater;

        private static final int TYPE_FOUR = 0;
        private static final int TYPE_ITEM = 1;

        private final int mNormalItemViewWidth;

        public MyAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mDatas = new ArrayList<>(100);
            for (int index = 0; index < 100; index++) {
                mDatas.add(index);
            }

            WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            final int screenWidth = dm.widthPixels;
            mNormalItemViewWidth = (int) ((screenWidth - SPACE * 1.0f * (COLUMN_COUNT + 1)) / COLUMN_COUNT);
//            int mNormalItemViewWidth = (int) ((screenWidth) / COLUMN_COUNT);
        }

        @Override
        public RecyclerColumnSpan.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View rootView = mLayoutInflater.inflate(R.layout.item_spaned, parent, false);
            rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    rootView.getViewTreeObserver().removeOnPreDrawListener(this);

                    final int type = viewType;
                    ViewGroup.LayoutParams lp = rootView.getLayoutParams();
                    if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                        switch (type) {
                            case TYPE_FOUR:
                                lp.height = mNormalItemViewWidth * 2;
                                break;
                            case TYPE_ITEM:
                                lp.height = mNormalItemViewWidth;
                                break;
                        }
                        rootView.setLayoutParams(lp);

                        final StaggeredGridLayoutManager lm =
                                (StaggeredGridLayoutManager) ((RecyclerView) parent).getLayoutManager();
                        lm.invalidateSpanAssignments();
                    }
                    return true;
                }
            });

            return new RecyclerColumnSpan.ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final RecyclerColumnSpan.ViewHolder holder, int position) {
            holder.itemView.setBackgroundResource(BackgroundResources.nextBackgroundResource());
            if (holder.itemView instanceof TextView) {
                ((TextView) holder.itemView).setText("" + mDatas.get(position));
            } else {
                ((TextView) holder.itemView.findViewById(R.id.tv_cover)).setText("封面图: " + mDatas.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? TYPE_FOUR : TYPE_ITEM;
        }
    }
}
