package com.joke.autobanner.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Scroller;

import com.joke.autobanner.R;

/**
 * Created by SHF on 2016/12/21.
 */

public class IndicatorView extends View {

    private int dotGap = 60;
    private int dotRadius = 15;
    private int mTotal = 4;
    private int currentPosition = 0;
    private int indicatorColor = 0xffff6666;
    private int normalColor = 0xff999999;
    private Paint normalPaint;
    private Paint indicatorPaint;
    private static final String TAG = "IndicatorView";

    Scroller scroller;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Indicator);
        indicatorColor = a.getColor(R.styleable.Indicator_indicatorColor, indicatorColor);
        normalColor = a.getColor(R.styleable.Indicator_normalColor, normalColor);
        mTotal = a.getInt(R.styleable.Indicator_total, mTotal);
        dotGap = a.getDimensionPixelSize(R.styleable.Indicator_dotGap, dp2px(20));
        dotRadius = a.getDimensionPixelSize(R.styleable.Indicator_dotRadius, dp2px(5));
        a.recycle();

        normalPaint = new Paint();
        indicatorPaint = new Paint();
        scroller = new Scroller(getContext());

        normalPaint.setColor(normalColor);
        indicatorPaint.setColor(indicatorColor);
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

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mTotal; i++) {
            drawCircle(canvas, normalPaint, dotGap / 2 + dotGap * i);
        }

        if (scroller != null && scroller.computeScrollOffset()) {
            drawCircle(canvas, indicatorPaint, dotGap * 0.5f + scroller.getCurrX());
            invalidate();
        } else {
            drawCircle(canvas, indicatorPaint, dotGap * (0.5f + currentPosition));
        }
    }

    private void drawCircle(Canvas canvas, Paint paint, float p) {
        canvas.drawCircle(getPaddingLeft() + p, getPaddingTop() + dotRadius, dotRadius, paint);
    }


    public void setCurrentPosition(int position) {
        this.currentPosition = position % mTotal;
        ViewCompat.postInvalidateOnAnimation(this);
    }


    public void startAni(int position) {
        position = position % mTotal;

        int direction = position - currentPosition;
        int originalPosition = dotGap * currentPosition;
        if (position == 0 && currentPosition == mTotal - 1) {
            direction = 1;
            originalPosition = -dotGap;
        } else if (position == mTotal - 1 && currentPosition == 0) {
            direction = -1;
            originalPosition = mTotal * dotGap;
        }
        scroller.startScroll(originalPosition, 0, direction * dotGap, 0, 1000);
        currentPosition = position;
        invalidate();
    }

    public void setCurrentPositionOffset(int position, float offset) {

    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


}
