package com.joke.autobanner;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.lang.reflect.Field;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp0;
    private ViewPager vp1;
    private ViewPager vp2;
    private BannerPagerAdapter adapter0;
    private BannerPagerAdapter adapter1;
    private BannerPagerAdapter adapter2;
    private IndicatorLayout il1;
    private IndicatorLayout il2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp0 = (ViewPager) findViewById(R.id.vp0);
        vp1 = (ViewPager) findViewById(R.id.vp1);
        vp2 = (ViewPager) findViewById(R.id.vp2);
        il1 = (IndicatorLayout) findViewById(R.id.il1);
        il2 = (IndicatorLayout) findViewById(R.id.il2);
        il1.setUpWithViewPager(vp1);

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
        vp2.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                il2.toPosition(position);
            }
        });

        initAVF();
    }

    private void initAVF(){
        AdapterViewFlipper  avf = (AdapterViewFlipper)findViewById(R.id.avf);
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
                ViewDataBinding inflate ;
                if (convertView==null){
                    inflate =   DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_pager_banner, parent, false);
                    convertView = inflate.getRoot();
                    convertView.setTag(inflate);
                }else {
                 inflate = (ViewDataBinding) convertView.getTag();
                }
                BannerBean value = new BannerBean();
                value.setInfo(""+position);
                inflate.setVariable(BR.banner, value);


                return convertView;
            }
        };


        PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("x",0- getResources().getDimension(R.dimen.dp360),0);
        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0, 1);
        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 0, 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(new Object(),xHolder, alphaHolder, scaleXHolder,scaleYHolder).setDuration(1000);

        PropertyValuesHolder xHolder1 = PropertyValuesHolder.ofFloat("x",0,getResources().getDimension(R.dimen.dp360));
        PropertyValuesHolder alphaHolder1 = PropertyValuesHolder.ofFloat("alpha", 1, 0);
        PropertyValuesHolder scaleXHolder1 = PropertyValuesHolder.ofFloat("ScaleX", 1, 0);
        PropertyValuesHolder scaleYHolder1 = PropertyValuesHolder.ofFloat("ScaleY", 1, 0);

        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(new Object(),xHolder1, alphaHolder1, scaleXHolder1,scaleYHolder1).setDuration(1000);

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
            mScroller.set(vp1, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            Log.e(TAG, "setViewPagerScrollSpeed: ", e);
        }
    }

}
