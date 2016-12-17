package com.joke.autobanner.widget;

import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SHF on 2016/12/15.
 */

public abstract class BaseAutoAdapter extends PagerAdapter implements Handler.Callback, ViewPager.OnPageChangeListener {
    private final static int AUTO_TURNING = 100;
    private int millisecond = 2000;
    private ViewPager viewPager;
    private Handler handler = new Handler(Looper.myLooper(), this);
    private boolean isTurning;

    private SparseArray<View> viewList = new SparseArray<>();//with viewpager to save the view has been removed
    private int maxCount;

    public BaseAutoAdapter(ViewPager viewPager) {
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(this);
    }

    protected abstract View instantiateItemView(ViewGroup parent, int position);

    protected abstract int getMaxCount();

    public void setGapMillisecond(int millisecond) {
        this.millisecond = millisecond;
    }

    public void startTurning() {
        handler.sendEmptyMessageDelayed(AUTO_TURNING, millisecond);
    }

    public void stopTurning() {
        handler.removeMessages(AUTO_TURNING);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        maxCount = getMaxCount();
        if (maxCount == 0) {
            maxCount = 1;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == AUTO_TURNING) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            return true;
        }
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int current = position % maxCount;
        View view = viewList.get(current);
        if (view == null) {
            view = instantiateItemView(container, current);
        }

        viewList.remove(position % maxCount);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        viewList.put(position % maxCount, (View) object);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) { //when drag viewpager autoScroll will stop
            case ViewPager.SCROLL_STATE_DRAGGING:
                handler.removeMessages(AUTO_TURNING);
                isTurning = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING://stop drag viewpager will start turning
                handler.sendEmptyMessageDelayed(AUTO_TURNING, millisecond);
                isTurning = true;
                break;
        }

    }
}
