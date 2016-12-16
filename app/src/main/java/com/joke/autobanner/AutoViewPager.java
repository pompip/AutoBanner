package com.joke.autobanner;

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
    public AutoViewPager(Context context) {
        super(context);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        try {
            Field mPopulatePending = ViewPager.class.getDeclaredField("mPopulatePending");
            mPopulatePending.setAccessible(true);
            mPopulatePending.setBoolean(this, false);
            Method method = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, boolean.class, int.class);
            method.setAccessible(true);
            method.invoke(this, item, smoothScroll, false, 1000);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
