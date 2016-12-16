package com.joke.autobanner.anim;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by SHF on 2016/12/15.
 */

public class MarginPageTransformer implements ViewPager.PageTransformer{
    @Override
    public void transformPage(View page, float position) {

        float abs = Math.abs(position);
        if (abs < 0.3f) {
            page.setScaleX(1);
            page.setScaleY(1);
        } else if (abs >= 0.3 && abs <= 0.5) {
            page.setScaleX(1 - abs + 0.3f);
            page.setScaleY(1 - abs + 0.3f);
        } else {
            page.setScaleX(0.8f);
            page.setScaleY(0.8f);
        }
    }
}
