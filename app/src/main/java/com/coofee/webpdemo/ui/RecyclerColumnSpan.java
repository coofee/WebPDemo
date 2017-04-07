package com.coofee.webpdemo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.ui.widget.ItemDecorationGridManager;
import com.coofee.webpdemo.utils.BackgroundResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RecyclerColumnSpan extends AppCompatActivity {
    private static final String TAG = "RecyclerColumnSpan";

    private static final int SPACE = 20;
    private static final int COLUMN_COUNT = 3;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.scale_container)
    FrameLayout mScaleContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_column_span);

        ButterKnife.inject(this);

        final int rowCount = COLUMN_COUNT;
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, rowCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return COLUMN_COUNT;
                }
                return 1;
            }
        });

        mRecyclerView.setLayoutManager(gridLayoutManager);

        final MyAdapter adapter = new MyAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new ItemDecorationGridManager(SPACE, rowCount));


        adapter.attachToRecyclerView(mRecyclerView);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


    }

    public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_ITEM = 1;
        private static final int TYPE_COUNT = TYPE_ITEM + 1;

        private SparseIntArray mTypeLayout = new SparseIntArray(TYPE_COUNT);

        private SparseIntArray mBackground = new SparseIntArray();

        List<Integer> mDatas;

        private Context mContext;
        private RecyclerView mRecyclerView;
        private LayoutInflater mLayoutInflater;

        private ItemTouchHelper mItemTouchHelper;

        private int itemViewWidth = -1;
        private int itemViewHeight = -1;

        private int headerViewHeight = -1;

        public MyAdapter(Context context, RecyclerView recyclerView) {
            this.mContext = context;
            mLayoutInflater = LayoutInflater.from(context);

            mTypeLayout.append(TYPE_HEADER, R.layout.recycler_header);
            mTypeLayout.append(TYPE_ITEM, R.layout.image_with_title);

            mRecyclerView = recyclerView;

            mDatas = new ArrayList<>(100);
            for (int index = 0; index < 100; index++) {
                mDatas.add(index);
            }
        }

        void attachToRecyclerView(RecyclerView recyclerView) {
            if (mItemTouchHelper != null) {
                mItemTouchHelper.attachToRecyclerView(null);
            }

//            mItemTouchHelper = new ItemTouchHelper(mSwapDragCallback);
//            mItemTouchHelper = new ItemTouchHelper(mWindowScaleCallback);
            mItemTouchHelper = new ItemTouchHelper(mAddViewScaleCallback);
//            mItemTouchHelper = new ItemTouchHelper(mViewScaleCallback);

            mItemTouchHelper.attachToRecyclerView(recyclerView);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (itemViewWidth == -1) {
                itemViewWidth = (int) ((mRecyclerView.getWidth() - (float) (SPACE * (COLUMN_COUNT + 1))) / COLUMN_COUNT);
                itemViewHeight = (int) (itemViewWidth * 0.625f);

                headerViewHeight = (int) (mRecyclerView.getWidth() * 0.625);
            }

            final int layoutResId = mTypeLayout.get(viewType);
            View rootView = mLayoutInflater.inflate(layoutResId, parent, false);
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            if (viewType == TYPE_ITEM) {
                layoutParams.width = itemViewWidth;
                layoutParams.height = itemViewHeight;
            } else {
                layoutParams.width = mRecyclerView.getWidth();
                layoutParams.height = headerViewHeight;
            }
            rootView.setLayoutParams(layoutParams);

            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Integer data = mDatas.get(position);
            int backgrond = mBackground.get(data);
            if (backgrond == 0) {
                backgrond = BackgroundResources.nextBackgroundResource();
                mBackground.put(data, backgrond);
            }
            holder.itemView.setBackgroundResource(backgrond);
            if (holder.itemView instanceof TextView) {
                ((TextView) holder.itemView).setText("" + mDatas.get(position));
            } else {
                ((TextView) holder.itemView.findViewById(R.id.tv_cover)).setText("封面图: " + mDatas.get(position));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "点击了" + mDatas.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });

            if (holder.getItemViewType() == TYPE_ITEM) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mItemTouchHelper != null) {
                            // 长按滑动item view;
                            mItemTouchHelper.startDrag(holder);
                        }
                        return true;
                    }
                });
            } else {
                // header不允许滑动;
                holder.itemView.setOnLongClickListener(null);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mItemTouchHelper != null) {
                            // 长按滑动item view;
                            mItemTouchHelper.startDrag(holder);
                        }
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        private ItemTouchHelper.Callback mAddViewScaleCallback = new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                // 让item view自己控制是否可以拖动;
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 滑动方向;
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
//                return (target.getItemViewType() == MyAdapter.TYPE_HEADER);
                return true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(mDatas, i, i + 1);
                    }
                } else {
                    for (int i = from; i > to; i--) {
                        Collections.swap(mDatas, i, i - 1);
                    }
                }
                notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                    clearScaleView();
                }

                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    // 当item view开始滑动
                    ViewCompat.setAlpha(viewHolder.itemView, 0.6f);
                }

                super.onSelectedChanged(viewHolder, actionState);
            }

            ViewHolder mScaleViewHolder;

            void clearScaleView() {
                if (mScaleViewHolder != null) {
                    mScaleContainerView.removeView(mScaleViewHolder.itemView);
                }
                mScaleViewHolder = null;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                clearScaleView();

                super.clearView(recyclerView, viewHolder);


                // item view滑动结束;
                ViewCompat.setAlpha(viewHolder.itemView, 1f);
                ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.0f);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                final int viewType = viewHolder.getItemViewType();
                if (viewType == MyAdapter.TYPE_ITEM) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    float scale = 1.0f;
                    if (firstVisibleItemPosition == 0) {
                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
                        if (distanceFromHeaderView > 0) {
                            float max = (headerViewHeight * 1.0f / itemViewHeight) - 1;
                            scale = 1 + distanceFromHeaderView / headerViewHeight * max;
                        } else {
                            clearScaleView();
                            // no scale;
                            return;
                        }
                    }

                    if (mScaleViewHolder == null) {
                        mScaleViewHolder = createViewHolder(recyclerView, MyAdapter.TYPE_ITEM);
                    }

                    bindViewHolder(mScaleViewHolder, viewHolder.getAdapterPosition());

                    final float top = viewHolder.itemView.getTop() + dY - 5;
                    final float left = viewHolder.itemView.getLeft() + dX;

                    ViewGroup.LayoutParams originLp = viewHolder.itemView.getLayoutParams();
                    final FrameLayout.LayoutParams tempLp = new FrameLayout.LayoutParams(mScaleViewHolder.itemView.getLayoutParams());
                    tempLp.width = (int) (originLp.width * scale);
                    tempLp.height = (int) (originLp.height * scale);
                    tempLp.topMargin = (int) top;
                    tempLp.leftMargin = (int) (left - (tempLp.width - originLp.width) / 2.0f);
                    mScaleViewHolder.itemView.setLayoutParams(tempLp);
                    mScaleViewHolder.itemView.setLayoutParams(tempLp);
                    if (mScaleViewHolder.itemView.getParent() == null) {
                        mScaleContainerView.addView(mScaleViewHolder.itemView);
                    }

//                    ViewCompat.setScaleX(viewHolder.itemView, scale);
//                    ViewCompat.setScaleY(viewHolder.itemView, scale);
                } else if (viewType == MyAdapter.TYPE_HEADER) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    float scale = itemViewHeight * 1.0f / headerViewHeight;
                    if (firstVisibleItemPosition == 0) {
                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
                        if (distanceFromHeaderView > 0) {
                            scale = scale + (distanceFromHeaderView / headerViewHeight) * (1 - scale);
                        }
                    }

                    ViewCompat.setScaleX(viewHolder.itemView, scale);
                    ViewCompat.setScaleY(viewHolder.itemView, scale);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };


        private ItemTouchHelper.Callback mViewScaleCallback = new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                // 让item view自己控制是否可以拖动;
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 滑动方向;
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
//                return (target.getItemViewType() == MyAdapter.TYPE_HEADER);
                return true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(mDatas, i, i + 1);
                    }
                } else {
                    for (int i = from; i > to; i--) {
                        Collections.swap(mDatas, i, i - 1);
                    }
                }
                notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                    clearScaleView();
                }

                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    // 当item view开始滑动
                    ViewCompat.setAlpha(viewHolder.itemView, 0.6f);
                }

                super.onSelectedChanged(viewHolder, actionState);
            }

            ViewHolder mScaleViewHolder;
            View mScaleView;
            Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            void clearScaleView() {
                mScaleViewHolder = null;
                if (mScaleView != null) {
                    mScaleContainerView.removeView(mScaleView);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                clearScaleView();

                super.clearView(recyclerView, viewHolder);


                // item view滑动结束;
                ViewCompat.setAlpha(viewHolder.itemView, 1f);
                ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.0f);
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                final int viewType = viewHolder.getItemViewType();
                if (viewType == MyAdapter.TYPE_ITEM) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    float scale = 1.0f;
                    if (firstVisibleItemPosition == 0) {
                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
                        if (distanceFromHeaderView > 0) {
                            float max = (headerViewHeight * 1.0f / itemViewHeight) - 1;
                            scale = 1 + distanceFromHeaderView / headerViewHeight * max;
                        } else {
                            clearScaleView();
                            // no scale;
                            return;
                        }
                    }

                    if (mScaleViewHolder == null) {
                        mScaleViewHolder = createViewHolder(recyclerView, MyAdapter.TYPE_ITEM);
                        bindViewHolder(mScaleViewHolder, viewHolder.getAdapterPosition());
                        mScaleView = mScaleViewHolder.itemView;
                    }

                    ViewGroup.LayoutParams lp = viewHolder.itemView.getLayoutParams();
                    FrameLayout.LayoutParams tempLp = new FrameLayout.LayoutParams(mScaleView.getLayoutParams());
                    tempLp.width = (int) (lp.width * scale);
                    tempLp.height = (int) (lp.height * scale);
                    final float top = viewHolder.itemView.getTop() + dY;
                    final float left = viewHolder.itemView.getLeft() + dX;
//                    tempLp.topMargin = (int) top;
//                    tempLp.leftMargin = (int) left;
                    mScaleView.setLayoutParams(tempLp);
                    mScaleView.measure(0, 0);
                    mScaleView.layout(0, 0, tempLp.width, tempLp.height);

                    Bitmap bitmap = Bitmap.createBitmap(tempLp.width, tempLp.height, Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(bitmap);
                    mScaleView.draw(canvas);
                    c.drawBitmap(bitmap, left, top, mPaint);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                final int viewType = viewHolder.getItemViewType();
                if (viewType == MyAdapter.TYPE_ITEM) {
//                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//                    float scale = 1.0f;
//                    if (firstVisibleItemPosition == 0) {
//                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
//                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
//                        if (distanceFromHeaderView > 0) {
//                            float max = (headerViewHeight * 1.0f / itemViewHeight) - 1;
//                            scale = 1 + distanceFromHeaderView / headerViewHeight * max;
//                        } else {
//                            clearScaleView();
//                            // no scale;
//                            return;
//                        }
//                    }
//
//                    if (mScaleViewHolder == null) {
//                        mScaleViewHolder = createViewHolder(recyclerView, MyAdapter.TYPE_ITEM);
//                        bindViewHolder(mScaleViewHolder, viewHolder.getAdapterPosition());
//                        mScaleView = mScaleViewHolder.itemView;
//                    }
//
//                    ViewGroup.LayoutParams lp = viewHolder.itemView.getLayoutParams();
//                    FrameLayout.LayoutParams tempLp = new FrameLayout.LayoutParams(mScaleView.getLayoutParams());
//                    tempLp.width = (int) (lp.width * scale);
//                    tempLp.height = (int) (lp.height * scale);
//                    tempLp.topMargin = (int) (viewHolder.itemView.getTop() + dY) - 5;
//                    tempLp.leftMargin = (int) (viewHolder.itemView.getLeft() + dX) - 5;
//                    mScaleView.setLayoutParams(tempLp);
//                    if (mScaleView.getParent() == null) {
//                        mScaleContainerView.addView(mScaleView);
//                    }

//                    ViewCompat.setScaleX(viewHolder.itemView, scale);
//                    ViewCompat.setScaleY(viewHolder.itemView, scale);
                } else if (viewType == MyAdapter.TYPE_HEADER) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    float scale = itemViewHeight * 1.0f / headerViewHeight;
                    if (firstVisibleItemPosition == 0) {
                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
                        if (distanceFromHeaderView > 0) {
                            scale = scale + (distanceFromHeaderView / headerViewHeight) * (1 - scale);
                        }
                    }

                    ViewCompat.setScaleX(viewHolder.itemView, scale);
                    ViewCompat.setScaleY(viewHolder.itemView, scale);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        private ItemTouchHelper.Callback mWindowScaleCallback = new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                // 让item view自己控制是否可以拖动;
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 滑动方向;
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
//                return (target.getItemViewType() == MyAdapter.TYPE_HEADER);
                return true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(mDatas, i, i + 1);
                    }
                } else {
                    for (int i = from; i > to; i--) {
                        Collections.swap(mDatas, i, i - 1);
                    }
                }
                notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                    clearScaleView();
                }

                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    // 当item view开始滑动
                    ViewCompat.setAlpha(viewHolder.itemView, 0.6f);
                }

                super.onSelectedChanged(viewHolder, actionState);
            }

            void clearScaleView() {
                if (mWindowManager != null && mScaleView != null) {
                    mWindowManager.removeView(mScaleView);
                    mScaleView = null;
                    mScaleViewHolder = null;
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                clearScaleView();

                super.clearView(recyclerView, viewHolder);


                // item view滑动结束;
                ViewCompat.setAlpha(viewHolder.itemView, 1f);
                ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.0f);

            }

            WindowManager mWindowManager = null;
            ViewHolder mScaleViewHolder;
            View mScaleView = null;

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                final int viewType = viewHolder.getItemViewType();
                if (viewType == MyAdapter.TYPE_ITEM) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    float scale = 1.0f;
                    if (firstVisibleItemPosition == 0) {
                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
                        if (distanceFromHeaderView > 0) {
                            float max = (headerViewHeight * 1.0f / itemViewHeight) - 1;
                            scale = 1 + distanceFromHeaderView / headerViewHeight * max;
                        } else {
                            clearScaleView();
                            // no scale;
                            return;
                        }
                    }

                    if (mWindowManager == null) {
                        mWindowManager = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
                    }

                    if (mScaleViewHolder == null) {
                        mScaleViewHolder = createViewHolder(recyclerView, MyAdapter.TYPE_ITEM);
                        bindViewHolder(mScaleViewHolder, viewHolder.getAdapterPosition());
                        mScaleView = mScaleViewHolder.itemView;
                    }

                    ViewGroup.LayoutParams lp = viewHolder.itemView.getLayoutParams();
                    ViewGroup.LayoutParams tempLp = mScaleView.getLayoutParams();
                    tempLp.width = (int) (lp.width * scale);
                    tempLp.height = (int) (lp.height * scale);
                    mScaleView.setLayoutParams(tempLp);

                    int[] location = new int[2];
                    recyclerView.getLocationOnScreen(location);
                    int left = (int) (location[0] + viewHolder.itemView.getLeft() + dX);
                    int top = (int) (location[1] + viewHolder.itemView.getTop() + dY);

//                    int[] location = new int[2];
//                    viewHolder.itemView.getLocationOnScreen(location);
//                    int left = (int) (location[0] + dX);
//                    int top = (int) (location[1] + dY);

                    WindowManager.LayoutParams wlp = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
                    wlp.x = (int) (left - (tempLp.width - lp.width) / 2.0f);
                    wlp.y = (int) (top - (tempLp.height - lp.height) / 2.0f);
                    wlp.width = tempLp.width;
                    wlp.height = tempLp.height;

                    wlp.gravity = Gravity.TOP | Gravity.LEFT;

                    if (mScaleView.getParent() == null) {
                        mWindowManager.addView(mScaleView, wlp);
                    } else {
                        mWindowManager.updateViewLayout(mScaleView, wlp);
                    }
//                    ViewCompat.setScaleX(viewHolder.itemView, scale);
//                    ViewCompat.setScaleY(viewHolder.itemView, scale);

                } else if (viewType == MyAdapter.TYPE_HEADER) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    float scale = itemViewHeight * 1.0f / headerViewHeight;
                    if (firstVisibleItemPosition == 0) {
                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
                        if (distanceFromHeaderView > 0) {
                            scale = scale + (distanceFromHeaderView / headerViewHeight) * (1 - scale);
                        }
                    }

                    ViewCompat.setScaleX(viewHolder.itemView, scale);
                    ViewCompat.setScaleY(viewHolder.itemView, scale);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        private ItemTouchHelper.Callback mSwapDragCallback = new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                // 让item view自己控制是否可以拖动;
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 滑动方向;
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
//                return (target.getItemViewType() == MyAdapter.TYPE_HEADER);
                return true;
            }

            @Override
            public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
                return super.chooseDropTarget(selected, dropTargets, curX, curY);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                Collections.swap(mDatas, from, to);
                notifyDataSetChanged();

                Log.e(TAG, String.format("%s from=%d, to=%d", "onMove", from, to));
                return true;
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                Log.e(TAG, String.format("%s fromPosition=%d, actionState=%s", "onSelectedChanged", viewHolder == null ? -1 : viewHolder.getAdapterPosition(), (actionState == ItemTouchHelper.ACTION_STATE_IDLE ? "ItemTouchHelper.ACTION_STATE_IDLE" : (actionState == ItemTouchHelper.ACTION_STATE_DRAG ? "ItemTouchHelper.ACTION_STATE_DRAG" : "ItemTouchHelper.ACTION_STATE_SWIPE"))));

                if (viewHolder != null && actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    // 当item view开始滑动
                    ViewCompat.setAlpha(viewHolder.itemView, 0.6f);
                }

                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                Log.e(TAG, String.format("%s fromPosition=%d", "clearView", viewHolder.getAdapterPosition()));

                // item view滑动结束;
                ViewCompat.setAlpha(viewHolder.itemView, 1f);
                ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.0f);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                final int viewType = viewHolder.getItemViewType();
                if (viewType == MyAdapter.TYPE_ITEM) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    float scale = 1.0f;
                    if (firstVisibleItemPosition == 0) {
                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
                        if (distanceFromHeaderView > 0) {
                            float max = (headerViewHeight * 1.0f / itemViewHeight) - 1;
                            scale = 1 + distanceFromHeaderView / headerViewHeight * max;
                        }
                    }

                    ViewCompat.setScaleX(viewHolder.itemView, scale);
                    ViewCompat.setScaleY(viewHolder.itemView, scale);

                } else if (viewType == MyAdapter.TYPE_HEADER) {
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    float scale = itemViewHeight * 1.0f / headerViewHeight;
                    if (firstVisibleItemPosition == 0) {
                        View headerView = recyclerView.getChildAt(firstVisibleItemPosition);
                        float distanceFromHeaderView = headerView.getBottom() - viewHolder.itemView.getTop() - dY;
                        if (distanceFromHeaderView > 0) {
                            scale = scale + (distanceFromHeaderView / headerViewHeight) * (1 - scale);
                        }
                    }

                    ViewCompat.setScaleX(viewHolder.itemView, scale);
                    ViewCompat.setScaleY(viewHolder.itemView, scale);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
    }
}
