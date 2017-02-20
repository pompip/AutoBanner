package com.joke.autobanner;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;

import com.joke.autobanner.adapter.BannerPagerAdapter;
import com.joke.autobanner.anim.DepthPageTransformer;
import com.joke.autobanner.anim.MarginPageTransformer;
import com.joke.autobanner.anim.ZoomOutPageTransformer;
import com.joke.autobanner.bean.BannerBean;
import com.joke.autobanner.bean.SlowScroller;
import com.joke.autobanner.widget.IndicatorLayout;
import com.joke.autobanner.widget.IndicatorView;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp0;
    private BannerPagerAdapter adapter0;
    private IndicatorLayout il1;
    private IndicatorView itv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp0 = (ViewPager) findViewById(R.id.vp0);
        il1 = (IndicatorLayout) findViewById(R.id.il1);
        itv = (IndicatorView) findViewById(R.id.itv);
        setViewPagerScrollSpeed();
        adapter0 = new BannerPagerAdapter(vp0);
        adapter0.addPagerData(Arrays.asList(new BannerBean(), new BannerBean(), new BannerBean()));
        vp0.setAdapter(adapter0);
        vp0.setPageTransformer(true, new MarginPageTransformer());
        vp0.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                il1.toPositionWithOffset(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                itv.setCurrentPosition(position);
                itv.startAni(position);
            }
        });

        initAVF();
    }

    private void initAVF() {
        AdapterViewFlipper avf = (AdapterViewFlipper) findViewById(R.id.avf);
        BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewDataBinding inflate;
                if (convertView == null) {
                    inflate = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_pager_banner, parent, false);
                    convertView = inflate.getRoot();
                    convertView.setTag(inflate);
                } else {
                    inflate = (ViewDataBinding) convertView.getTag();
                }
                BannerBean value = new BannerBean();
                value.setInfo("" + position);
                inflate.setVariable(BR.banner, value);


                return convertView;
            }
        };


        PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("y", 0 - getResources().getDimension(R.dimen.dp360), 0);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0.5f, 1);
        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1);
        PropertyValuesHolder rotationX = PropertyValuesHolder.ofFloat("rotationX",45,0);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(new Object(),xHolder, alphaHolder, scaleXHolder, scaleYHolder,rotationX).setDuration(1000);

        PropertyValuesHolder xHolder1 = PropertyValuesHolder.ofFloat("y", 0, getResources().getDimension(R.dimen.dp360));
        PropertyValuesHolder alphaHolder1 = PropertyValuesHolder.ofFloat("alpha", 1, 0.5f);
        PropertyValuesHolder scaleXHolder1 = PropertyValuesHolder.ofFloat("ScaleX", 1, 0.5f);
        PropertyValuesHolder scaleYHolder1 = PropertyValuesHolder.ofFloat("ScaleY", 1, 0.5f);
        PropertyValuesHolder rotationX1 = PropertyValuesHolder.ofFloat("rotationX",0,-45);

        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(new Object(),xHolder1, alphaHolder1, scaleXHolder1, scaleYHolder1,rotationX1).setDuration(1000);

        avf.setInAnimation(animator);
        avf.setOutAnimation(animator1);
        avf.setAdapter(baseAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter0.startTurning();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("DepthPageTransformer");
        menu.add("ZoomOutPageTransformer");
        menu.add("MarginPageTransformer");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int dp40 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        if (item.getTitle().equals("DepthPageTransformer")) {
            vp0.setPageTransformer(true, new DepthPageTransformer());
            vp0.setClipToPadding(true);
            vp0.setPadding(0,0,0,0);
        } else if (item.getTitle().equals("MarginPageTransformer")) {
            vp0.setPageTransformer(true, new MarginPageTransformer());
            vp0.setClipToPadding(false);
            vp0.setPadding(dp40,0,dp40,0);
        }else if (item.getTitle().equals("ZoomOutPageTransformer")){
            vp0.setPageTransformer(true, new ZoomOutPageTransformer());
            vp0.setClipToPadding(true);
            vp0.setPadding(0,0,0,0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter0.stopTurning();
    }

    private static final String TAG = "MainActivity";

    /**
     * 更改viewpager 中Scroll对象,修改滚动速度
     */
    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            SlowScroller scroller = new SlowScroller(vp0.getContext());
            mScroller.set(vp0, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            Log.e(TAG, "setViewPagerScrollSpeed: ", e);
        }
    }

}
