package com.coofee.webpdemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.ui.widget.ItemDecorationGridOffset;
import com.coofee.webpdemo.ui.widget.ItemDecorationGridOver;
import com.coofee.webpdemo.utils.BackgroundResources;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecyclerViewMultiItem extends AppCompatActivity {

    private static final int COLUMN_COUNT = 4;
    private static final int SPACE = 20;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.btn_add_item_decoration_offset)
    Button mAddItemDecorationOffset;
    @InjectView(R.id.btn_add_item_decoration_over)
    Button mAddItemDecorationOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_multi_item);
        ButterKnife.inject(this);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, COLUMN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return COLUMN_COUNT;
                }
                return 1;
            }
        });
//        ItemDecorationOver itemDecorationGridManager = new ItemDecorationOver(SPACE, COLUMN_COUNT);
//        itemDecorationGridManager.setHeaderViewWithOffset(true);
//        mRecyclerView.addItemDecoration(itemDecorationGridManager);
        final int padding = (int) (SPACE / 2.0f);
//        mRecyclerView.setPadding(padding, padding, padding, padding);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        MyAdapter adapter = new MyAdapter(this);
        mRecyclerView.setAdapter(adapter);


        mAddItemDecorationOffset.setOnClickListener(new View.OnClickListener() {

            private RecyclerView.ItemDecoration mItemDecoration;

            @Override
            public void onClick(View v) {
                if (mItemDecoration == null) {
                    mItemDecoration = new ItemDecorationGridOffset(SPACE);
                    mRecyclerView.addItemDecoration(mItemDecoration);
                } else {
                    mRecyclerView.removeItemDecoration(mItemDecoration);
                    mItemDecoration = null;
                }
            }
        });

        mAddItemDecorationOver.setOnClickListener(new View.OnClickListener() {

            private RecyclerView.ItemDecoration mItemDecoration;

            @Override
            public void onClick(View v) {
                if (mItemDecoration == null) {
                    mItemDecoration = new ItemDecorationGridOver(SPACE);
                    mRecyclerView.addItemDecoration(mItemDecoration);
                } else {
                    mRecyclerView.removeItemDecoration(mItemDecoration);
                    mItemDecoration = null;
                }
            }
        });
    }

    private static class ItemHeaderView extends FrameLayout {

        int mFirstViewRatio;
        int mSpaceWidth;
        int mColumnCount;

        public ItemHeaderView(Context context) {
            super(context);
        }

    }

    public static class ItemHeaderViewHolder extends RecyclerView.ViewHolder {

        public ItemHeaderViewHolder(View itemView) {
            super(itemView);
        }

        void bind(List<Integer> data) {

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<RecyclerColumnSpan.ViewHolder> {
        List<Integer> mDatas;

        private LayoutInflater mLayoutInflater;

        private final int mNormalItemWidth;
        private final int mNormalItemViewWidth;

        private static final int TYPE_FOUR = 0;
        private static final int TYPE_ITEM = 1;

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
            mNormalItemWidth = (int) ((screenWidth - SPACE * 1.0f * (COLUMN_COUNT + 1)) / COLUMN_COUNT);
            mNormalItemViewWidth = (int) ((screenWidth) / COLUMN_COUNT);
            Log.e("RecyclerViewMultiItem", "normalItemWidth=" + mNormalItemWidth + ", fourItemWidth=" + (mNormalItemWidth * 2 + SPACE));
        }

        @Override
        public RecyclerColumnSpan.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View rootView = mLayoutInflater.inflate(R.layout.image_with_title, parent, false);
            ViewGroup.LayoutParams lp = rootView.getLayoutParams();
            if (viewType == TYPE_FOUR) {
                lp.width = mNormalItemWidth * 2 + SPACE;
                lp.height = mNormalItemWidth * 2 + SPACE;
            } else {
                lp.width = mNormalItemWidth;
                lp.height = mNormalItemWidth;
            }

            rootView.setLayoutParams(lp);
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
