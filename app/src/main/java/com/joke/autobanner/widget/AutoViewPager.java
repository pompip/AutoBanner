package com.joke.autobanner.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by SHF on 2016/12/15.
 */

/**
 * 修改smoothScroll 速度
 */
public class AutoViewPager extends ViewPager {

    private Field mPopulatePending;
    private Method setCurrentItemInternal;

    public AutoViewPager(Context context) {
        this(context, null);

    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            mPopulatePending = ViewPager.class.getDeclaredField("mPopulatePending");
            mPopulatePending.setAccessible(true);
            setCurrentItemInternal = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, Boolean.TYPE, int.class);
            setCurrentItemInternal.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        try {
            mPopulatePending.setBoolean(this, false);
            setCurrentItemInternal.invoke(this, item, smoothScroll, false, 1000);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            super.setCurrentItem(item, smoothScroll);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            super.setCurrentItem(item, smoothScroll);
        }
    }
}
