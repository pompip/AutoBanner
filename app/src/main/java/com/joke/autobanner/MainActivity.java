package com.joke.autobanner;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.joke.autobanner.adapter.BannerPagerAdapter;
import com.joke.autobanner.anim.DepthPageTransformer;
import com.joke.autobanner.anim.MarginPageTransformer;
import com.joke.autobanner.anim.ZoomOutPageTransformer;
import com.joke.autobanner.bean.BannerBean;
import com.joke.autobanner.bean.SlowScroller;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp0;
    private ViewPager vp1;
    private ViewPager vp2;
    private BannerPagerAdapter adapter0;
    private BannerPagerAdapter adapter1;
    private BannerPagerAdapter adapter2;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp0 = (ViewPager) findViewById(R.id.vp0);
        vp1 = (ViewPager) findViewById(R.id.vp1);
        vp2 = (ViewPager) findViewById(R.id.vp2);

        setViewPagerScrollSpeed();
        adapter0 = new BannerPagerAdapter(vp0);
        adapter1 = new BannerPagerAdapter(vp1);
        adapter2 = new BannerPagerAdapter(vp2);

        adapter0.addPagerData(Arrays.asList(new BannerBean(), new BannerBean(), new BannerBean()));
        vp0.setAdapter(adapter0);

        vp0.setPageTransformer(true, new MarginPageTransformer());
        vp1.setAdapter(adapter1);
        vp1.setPageTransformer(true, new DepthPageTransformer());
        adapter1.addPagerData(Arrays.asList(new BannerBean(), new BannerBean(), new BannerBean()));
        vp2.setAdapter(adapter2);
        vp2.setPageTransformer(true, new ZoomOutPageTransformer());
        adapter2.addPagerData(Arrays.asList(new BannerBean(), new BannerBean(), new BannerBean()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter0.startTurning();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter0.stopTurning();
    }

    /**
     * 更改viewpager 中Scroll对象,修改滚动速度
     */
    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            SlowScroller scroller = new SlowScroller(vp0.getContext());
            mScroller.set(vp1, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            Log.e(TAG, "setViewPagerScrollSpeed: ", e);
        }
    }

}
