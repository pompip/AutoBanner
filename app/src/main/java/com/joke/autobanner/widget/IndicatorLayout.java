package com.joke.autobanner.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.joke.autobanner.R;


/**
 * Created by SHF on 2016/12/17.
 */

public class IndicatorLayout extends ViewGroup {
    private static final String TAG = "IndicatorLayout";
    private int dotGap = 60;
    private int dotRadius = 15;
    private int mTotal = 3;
    private int currentPosition;
    private DotView indicator;
    private LayoutParams indicatorParams;
    private int indicatorColor = 0xffff6666;
    private int normalColor = 0xff999999;

    private DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            initTotal();
        }
    };
    private PagerAdapter adapter;

    public IndicatorLayout(Context context) {
        this(context, null);

    }

    public IndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Indicator);
        indicatorColor = a.getColor(R.styleable.Indicator_indicatorColor, indicatorColor);
        normalColor = a.getColor(R.styleable.Indicator_normalColor, normalColor);
        mTotal = a.getInt(R.styleable.Indicator_total, mTotal);
        dotGap = a.getDimensionPixelSize(R.styleable.Indicator_dotGap, dotGap);
        dotRadius = a.getDimensionPixelSize(R.styleable.Indicator_dotRadius, dotRadius);
        a.recycle();
        setClipToPadding(false);
        indicator = new DotView(context);
        indicator.setColor(indicatorColor);
        indicatorParams = new LayoutParams(dotGap, dotRadius * 2);
        setTotal(mTotal);
    }

    /**
     * 总个数
     */
    public void setTotal(int total) {
        removeAllViews();
        mTotal = total;
        for (int i = 0; i < total; i++) {
            DotView dotView = new DotView(getContext());
            dotView.setColor(normalColor);
            addViewInLayout(dotView, i, new LayoutParams(dotGap, dotRadius * 2), true);
        }
    }

    /**
     * 动画方式移动指示点到某个位置
     */
    public void toPosition(int position) {
        int oldPosition = position % mTotal;
        if (oldPosition == 0 && currentPosition == mTotal - 1) {
            ObjectAnimator.ofFloat(indicator, "x", 0, getPaddingLeft()).start();
        } else if (oldPosition == mTotal - 1 && currentPosition == 0) {
            ObjectAnimator.ofFloat(indicator, "x", getWidth(), getPaddingRight() + dotGap).start();
        }
        currentPosition = oldPosition;
        indicator.animate().x(getPaddingLeft() + dotGap * currentPosition).start();
    }

    /**
     * 与viewPager关联移动指示点
     */
    public void setUpWithViewPager(ViewPager viewPager) {
        adapter = viewPager.getAdapter();
        if (adapter == null) {
            Log.e(TAG, "setUpWithViewPager: you show setPagerAdapter first!");
        } else {
            adapter.registerDataSetObserver(observer);
            initTotal();
        }
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toPositionWithOffset(position, positionOffset);
            }
        });
    }

    private void initTotal() {
        if (adapter != null) {
            if (adapter instanceof BaseAutoAdapter) {
                mTotal = ((BaseAutoAdapter) adapter).getMaxCount();
            } else {
                mTotal = adapter.getCount();
            }
            setTotal(mTotal);

        }
    }

    /**
     * 通过位置和偏移量移动指示点
     */
    public void toPositionWithOffset(int position, float positionOffset) {
        currentPosition = position % mTotal;
        if (currentPosition == mTotal - 1 && positionOffset > 0.5) { //终点一半折返,动画效果更好
            currentPosition = 0;
            positionOffset = positionOffset - 1;
        }
        indicator.setTranslationX((currentPosition + positionOffset) * dotGap);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for (int i = 0, n = getChildCount(); i < n; i++) {
            getChildAt(i).layout(getPaddingLeft() + dotGap * i, getPaddingTop()
                    , getPaddingLeft() + dotGap * (i + 1), dotRadius * 2 + getPaddingTop());
        }
        addViewInLayout(indicator, -1, indicatorParams);
        indicator.layout(getPaddingLeft() + dotGap * currentPosition, getPaddingTop()
                , getPaddingLeft() + dotGap * (currentPosition + 1), dotRadius * 2 + getPaddingTop());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            width = dotGap * mTotal;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            height = dotRadius * 2;
        }
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), height + getPaddingTop() + getPaddingBottom());
    }

    /**
     * 圆点
     */
    private class DotView extends View {

        private final Paint mPaint;
        private int mColor;

        public DotView(Context context) {
            super(context);
            mPaint = new Paint();
        }

        public void setColor(@ColorInt int color) {
            mColor = color;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int min = Math.min(getWidth(), getHeight());
            mPaint.setColor(mColor);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, min / 2, mPaint);
        }
    }
}
