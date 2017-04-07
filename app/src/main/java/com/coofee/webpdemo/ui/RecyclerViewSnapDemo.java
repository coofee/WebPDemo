package com.coofee.webpdemo.ui;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.ui.widget.ItemDecorationGridOffset;
import com.coofee.webpdemo.utils.BackgroundResources;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecyclerViewSnapDemo extends AppCompatActivity {

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private SnapHelper mCurrentSnapHelper;

    private RecyclerView.Adapter mAdapter;
    private boolean mPagerSnap = false;

    private static final float MAX_SCALE = 1.2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_snap_demo);

        ButterKnife.inject(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.addItemDecoration(new ItemDecorationGridOffset(40));

        mAdapter = new RecyclerView.Adapter() {
            private SparseIntArray mBackground = new SparseIntArray();
            private final int ITEM_WIDTH = 200;
            private final int ITEM_HEIGHT = 300;
            private final int ITEM_SCALE_HEIGHT = (int) (ITEM_HEIGHT * MAX_SCALE);

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                FrameLayout frameLayout = new FrameLayout(parent.getContext());

                TextView button = new TextView(parent.getContext());
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ITEM_WIDTH, ITEM_HEIGHT);
                lp.gravity = Gravity.CENTER;
                frameLayout.addView(button, lp);

                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ITEM_WIDTH, ITEM_SCALE_HEIGHT));
                return new VH(frameLayout);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                int backgrond = mBackground.get(position);
                if (backgrond == 0) {
                    backgrond = BackgroundResources.nextBackgroundResource();
                    mBackground.put(position, backgrond);
                }

                if (mPagerSnap) {
                    holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                } else {
                    holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ITEM_WIDTH, ITEM_SCALE_HEIGHT));
                }

                if (holder.itemView instanceof TextView) {
                    ((TextView) holder.itemView).setText("" + position);
                } else if (holder.itemView instanceof ViewGroup) {
                    final int childCount = ((ViewGroup) holder.itemView).getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childAt = ((ViewGroup) holder.itemView).getChildAt(i);
                        if (childAt instanceof TextView) {
                            ((TextView) childAt).setText("" + position);
                            childAt.setBackgroundResource(backgrond);
                        }
                    }
                }
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.activity_recycler_view_snap_start).setOnClickListener(new View.OnClickListener() {
            SnapHelper startSnapHelper = new GravitySnapHelper(Gravity.START);

            @Override
            public void onClick(View v) {
                changeSnapHelper(mRecyclerView, startSnapHelper);
            }
        });
        findViewById(R.id.activity_recycler_view_snap_center).setOnClickListener(new View.OnClickListener() {
            SnapHelper centerSnapHelper = new LinearSnapHelper();

            @Override
            public void onClick(View v) {
                changeSnapHelper(mRecyclerView, centerSnapHelper);
            }
        });
        findViewById(R.id.activity_recycler_view_snap_end).setOnClickListener(new View.OnClickListener() {

            SnapHelper endSnapHelper = new GravitySnapHelper(Gravity.END);

            @Override
            public void onClick(View v) {
                changeSnapHelper(mRecyclerView, endSnapHelper);
            }
        });
        findViewById(R.id.activity_recycler_view_snap_pager).setOnClickListener(new View.OnClickListener() {

            PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();

            @Override
            public void onClick(View v) {
                changeSnapHelper(mRecyclerView, pagerSnapHelper);
            }
        });
        findViewById(R.id.activity_recycler_view_clear).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentSnapHelper != null) {
                    mCurrentSnapHelper.attachToRecyclerView(null);
                }
                mRecyclerView.setOnScrollListener(null);
                mPagerSnap = false;
                mRecyclerView.setAdapter(null);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }

    public static class VH extends RecyclerView.ViewHolder {

        TextView textView;

        public VH(View itemView) {
            super(itemView);
        }
    }

    private void changeSnapHelper(RecyclerView recyclerView, SnapHelper newSnapHelper) {
        if (mCurrentSnapHelper != null) {
            mCurrentSnapHelper.attachToRecyclerView(null);
        }

        newSnapHelper.attachToRecyclerView(recyclerView);
        mCurrentSnapHelper = newSnapHelper;

        final boolean preFalg = mPagerSnap;
        if (newSnapHelper instanceof PagerSnapHelper) {
            mPagerSnap = true;

        } else {
            recyclerView.setOnScrollListener(null);
            mPagerSnap = false;
        }

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mScrollOffset = 0;
            private View preSnapView;


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View snapView = mCurrentSnapHelper.findSnapView(mRecyclerView.getLayoutManager());
                    if (snapView != null) {
                        ViewCompat.setScaleX(preSnapView, MAX_SCALE);
                        ViewCompat.setScaleY(preSnapView, MAX_SCALE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                View snapView = mCurrentSnapHelper.findSnapView(mRecyclerView.getLayoutManager());

                if (preSnapView != null && preSnapView != snapView) {
                    ViewCompat.setScaleX(preSnapView, 1.0f);
                    ViewCompat.setScaleY(preSnapView, 1.0f);
//                    ViewCompat.setTranslationY(preSnapView, 0);
                    mScrollOffset = 0;
                }

                mScrollOffset += dx;

                if (snapView == null) {
                    return;
                }
                preSnapView = snapView;

                float scale = 1 + Math.abs(mScrollOffset * 2.0f / snapView.getWidth());
                scale = Math.min(scale, MAX_SCALE);
                ViewCompat.setScaleX(snapView, scale);
                ViewCompat.setScaleY(snapView, scale);
//                float translationY = (scale - 1) * snapView.getHeight() / 2.0f;
//                ViewCompat.setTranslationY(snapView, -translationY);
            }
        });

        if (preFalg != mPagerSnap) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
