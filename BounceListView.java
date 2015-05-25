package com.eugene.listbottomtesting;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;


public class BounceListView extends ListView {
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

    private Context mContext;
    private int mMaxYOverscrollDistance;
    private int returnOverScrollBy;

    public BounceListView(Context context) {
        super(context);
        mContext = context;
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initBounceListView();
    }

    private void initBounceListView() {
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;
        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (scrollY < 0) { // List being over scrolled from the top
            int scrollYMin = (scrollY * -1) - 1; // convert scrollY to a positive then subtract 1
            if (scrollYMin < 0) {// Due to subtracting 1 from scrollY, the base scrollY would be -1 rather than 0. So if scrollY is less than 0, we set scrollY to to its base value, 0.
                returnOverScrollBy = 0;
            } else {
                returnOverScrollBy = scrollYMin;
            }
        } else {  // List being over scrolled from the top
            int scrollYMin = scrollY - 1;
            if (scrollYMin < 0) {
                returnOverScrollBy = 0;
            } else {
                returnOverScrollBy = scrollYMin;
            }
        }

        if (isTouchEvent) {
            // Does exactly what it originally did on all previous versions.
            return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, true);
        } else {
            /**
             * What I did:
             * 1- Get the scrollY which is the current scroll value.
             * 2- Convert scrollY to positive if necessary.
             * 3- int returnOverScrollBy = scrollY - 1;
             * 4- returnOverScrollBy is always one less than scrollY current position.
             * finally- if isTouchEvent == false, we set the maxOverScrollY = returnOverScrollBy forcing maxOverScrollY to move programmatically, which prevents touch errors that were happening before.
             */
            return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, returnOverScrollBy, false);
        }
    }
}