package com.joke.autobanner;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Toast;

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
    private View iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        setRotate();
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


        final float dimension = getResources().getDimension(R.dimen.dp360);
        float sqrt = (float) Math.sqrt(3);
        PropertyValuesHolder xHolder = PropertyValuesHolder.ofFloat("y", (float) Math.PI/2, 0);
        xHolder.setEvaluator(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                float s = (float)startValue;
                float e = (float)endValue;
                float v = (float) (Math.sin(s + (e - s) * fraction) * dimension);
                return -v/2;
            }
        });

        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0.5f, 1);
        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX",-(float) Math.PI/4, (float) Math.PI/4);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY",-(float) Math.PI/4, (float) Math.PI/4);

        TypeEvaluator<Float> evaluator = new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {

                float s = (float) (startValue );
                float e = (float) (endValue );


                float v = (float) ( Math.sqrt(2)*Math.cos(s + (e - s) * fraction));

                Log.e(TAG, "evaluate: " +v+ "  :" + fraction +"  :"+Math.cos(s + (e - s) * fraction));
                return 1f/v;
            }
        };


        scaleXHolder.setEvaluator(evaluator);
        scaleYHolder.setEvaluator(evaluator);


        PropertyValuesHolder rotationX = PropertyValuesHolder.ofFloat("rotationX",90,0);


        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(new Object(),alphaHolder,xHolder, scaleXHolder,scaleYHolder,rotationX).setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());

        PropertyValuesHolder xHolder1 = PropertyValuesHolder.ofFloat("y", 0,(float) Math.PI/2);
        xHolder1.setEvaluator(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                float s = (float)startValue;
                float e = (float)endValue;
                float v = (float) (Math.sin(s + (e - s) * fraction) * dimension);
                return v/2;
            }
        });
        PropertyValuesHolder alphaHolder1 = PropertyValuesHolder.ofFloat("alpha", 1, 0.5f);
        PropertyValuesHolder scaleXHolder1 = PropertyValuesHolder.ofFloat("ScaleX", -(float) Math.PI/4, (float) Math.PI/4);
        PropertyValuesHolder scaleYHolder1 = PropertyValuesHolder.ofFloat("ScaleY", -(float) Math.PI/4, (float) Math.PI/4);
        scaleXHolder1.setEvaluator(evaluator);
        scaleYHolder1.setEvaluator(evaluator);


        PropertyValuesHolder rotationX1 = PropertyValuesHolder.ofFloat("rotationX",0,-90);

        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(new Object(),alphaHolder1,xHolder1, scaleXHolder1,scaleYHolder1 ,rotationX1).setDuration(1000);
        animator1.setInterpolator(new LinearInterpolator());



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

    private  void setRotate(){

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setRotationX(-45);
                Toast.makeText(MainActivity.this, ""+v.getX() +" :"+v.getY() , Toast.LENGTH_SHORT).show();
            }
        });

    }

}
