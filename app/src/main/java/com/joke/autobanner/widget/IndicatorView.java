package com.joke.autobanner.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by SHF on 2016/12/21.
 */

public class IndicatorView extends View {

    private int childWidth = 60;
    private int childHeight = 30;
    private int mTotal = 13;
    private int currentPosition = 0;
    private int indicatorColor = 0xffff6666;
    private int normalColor = 0xff999999;
    private Paint normalPaint;
    private Paint indicatorPaint;
    private static final String TAG = "IndicatorView";

    Scroller scroller;

    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        normalPaint = new Paint();
        indicatorPaint = new Paint();
        scroller = new Scroller(getContext());
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            width = childWidth * mTotal;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            height = childHeight;
        }
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int min = Math.min(childWidth, childHeight);
        for (int i = 0; i < mTotal; i++) {

            normalPaint.setColor(normalColor);
            canvas.drawCircle(getPaddingLeft()+childWidth / 2 + childWidth * i, childHeight / 2, min / 2, normalPaint);
        }
        indicatorPaint.setColor(indicatorColor);

        if (scroller != null && scroller.computeScrollOffset()) {
            Log.e(TAG, "onDraw: " + scroller.getCurrX());
            canvas.drawCircle(getPaddingLeft()+childWidth * 0.5f + scroller.getCurrX(), childHeight / 2, min / 2, indicatorPaint);
            invalidate();
        } else {
            canvas.drawCircle(getPaddingLeft()+childWidth * (0.5f + currentPosition), childHeight / 2, min / 2, indicatorPaint);
        }
    }


    public void setCurrentPosition(int position) {
        this.currentPosition = position % mTotal;
        ViewCompat.postInvalidateOnAnimation(this);
    }


    public void startAni(int position) {
        position = position % mTotal;
        scroller.startScroll(childWidth * currentPosition, 0, (position-currentPosition)*childWidth, 0, 1000);
        currentPosition = position;
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }


}
