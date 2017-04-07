package com.coofee.webpdemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.coofee.webpdemo.R;
import com.coofee.webpdemo.utils.BackgroundResources;

public class MultiItemRowSpan extends AppCompatActivity {
    private static final int COLUMN_COUNT = 4;
    private static final int SPACE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_item_row_span);


        addGridItem((ViewGroup) findViewById(R.id.test_grid_align), COLUMN_COUNT - 1);

        addGridItem((ViewGroup) findViewById(R.id.test_grid_align), COLUMN_COUNT);

        addGridItem((ViewGroup) findViewById(R.id.test_grid_align), COLUMN_COUNT + 1);
    }

    private static void addGridItem(ViewGroup container, int columnCount) {
        WindowManager wm = (WindowManager) container.getContext().getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        final int screenWidth = dm.widthPixels;

        int spaceWidth = SPACE;
        int normalItemWidth = (int) ((screenWidth - SPACE * 1.0f * (columnCount + 1)) / columnCount);
        int fourItemWidth = normalItemWidth * 2 + SPACE;
        Log.e("RecyclerViewRowSpan", "space=" + SPACE + ", normalItemWidth=" + normalItemWidth + ", fourItemWidth=" + fourItemWidth);

        container.addView(createSpaceView(container.getContext(), spaceWidth, LinearLayout.VERTICAL));

        LinearLayout row1 = new LinearLayout(container.getContext());
        row1.setOrientation(LinearLayout.HORIZONTAL);
        container.addView(row1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        row1.addView(createSpaceView(container.getContext(), spaceWidth, row1.getOrientation()));

        row1.addView(createNormalItemView(container.getContext(), fourItemWidth));

        row1.addView(createSpaceView(container.getContext(), spaceWidth, row1.getOrientation()));

        LinearLayout row1Two = new LinearLayout(container.getContext());
        row1Two.setOrientation(LinearLayout.VERTICAL);
        row1.addView(row1Two, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        row1Two.addView(createNormalItemView(container.getContext(), normalItemWidth));
        row1Two.addView(createSpaceView(container.getContext(), spaceWidth, row1Two.getOrientation()));
        row1Two.addView(createNormalItemView(container.getContext(), normalItemWidth));

        row1.addView(createSpaceView(container.getContext(), spaceWidth, row1.getOrientation()));

        LinearLayout row1Three = new LinearLayout(container.getContext());
        row1Three.setOrientation(LinearLayout.VERTICAL);
        row1.addView(row1Three, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        row1Three.addView(createNormalItemView(container.getContext(), normalItemWidth));
        row1Three.addView(createSpaceView(container.getContext(), spaceWidth, row1Two.getOrientation()));
        row1Three.addView(createNormalItemView(container.getContext(), normalItemWidth));

        container.addView(createSpaceView(container.getContext(), spaceWidth, LinearLayout.VERTICAL));

        LinearLayout row2 = new LinearLayout(container.getContext());
        row2.setOrientation(LinearLayout.HORIZONTAL);
        container.addView(row2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        row2.addView(createSpaceView(container.getContext(), spaceWidth, row2.getOrientation()));
        row2.addView(createNormalItemView(container.getContext(), normalItemWidth));

        row2.addView(createSpaceView(container.getContext(), spaceWidth, row2.getOrientation()));
        row2.addView(createNormalItemView(container.getContext(), normalItemWidth));

        row2.addView(createSpaceView(container.getContext(), spaceWidth, row2.getOrientation()));
        row2.addView(createNormalItemView(container.getContext(), normalItemWidth));

        row2.addView(createSpaceView(container.getContext(), spaceWidth, row2.getOrientation()));
        row2.addView(createNormalItemView(container.getContext(), normalItemWidth));

        row2.addView(createSpaceView(container.getContext(), spaceWidth, row2.getOrientation()));

        container.addView(createSpaceView(container.getContext(), spaceWidth, LinearLayout.VERTICAL));
    }

    private static View createNormalItemView(Context context, int width) {
        View spaceView = new View(context);
        spaceView.setBackgroundResource(BackgroundResources.nextBackgroundResource());
        ViewGroup.LayoutParams lpSpace = new ViewGroup.LayoutParams(width, width);
        spaceView.setLayoutParams(lpSpace);
        return spaceView;
    }

    private static View createSpaceView(Context context, int width, int orientation) {
        View spaceView = new View(context);
        ViewGroup.LayoutParams lpSpace = null;

        if (orientation == LinearLayout.VERTICAL) {
            lpSpace = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);
        } else {
            lpSpace = new ViewGroup.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        spaceView.setLayoutParams(lpSpace);
        return spaceView;
    }

}
