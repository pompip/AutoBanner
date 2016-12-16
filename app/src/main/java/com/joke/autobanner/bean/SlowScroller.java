package com.joke.autobanner.bean;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by SHF on 2016/12/15.
 */

public class SlowScroller extends Scroller {
    private int mDuration = 1000;

    public SlowScroller(Context context) {
        super(context);
    }

    public SlowScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public SlowScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
