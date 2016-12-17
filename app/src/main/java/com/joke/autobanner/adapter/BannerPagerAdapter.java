package com.joke.autobanner.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joke.autobanner.BR;
import com.joke.autobanner.widget.BaseAutoAdapter;
import com.joke.autobanner.R;
import com.joke.autobanner.bean.BannerBean;

import java.util.List;

/**
 * Created by SHF on 2016/11/29.
 */

public class BannerPagerAdapter extends BaseAutoAdapter {
    private final static int[] colors = {0xff758CEE, 0xff4FCB95, 0xffFF8237};

    private List<BannerBean> couponList;
    private final LayoutInflater inflater;

    public BannerPagerAdapter(ViewPager viewPager) {
        super(viewPager);
        inflater = LayoutInflater.from(viewPager.getContext());
    }


    public void addPagerData(List<BannerBean> coupons) {
        if (coupons == null || coupons.isEmpty()) {
            return;
        }
        couponList = coupons;
        notifyDataSetChanged();
    }

    @Override
    protected View instantiateItemView(ViewGroup parent, int current) {

        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_pager_banner, parent, false);
        View root = dataBinding.getRoot();
        root.setBackgroundColor(colors[current % 3]);
        root.setTag(current%3);

        try {
            BannerBean value = couponList.get(current);
            value.setInfo(""+current%3);
            dataBinding.setVariable(BR.banner, value);
        } catch (IndexOutOfBoundsException e) {
            Log.e("BaseAutoAdapter", "instantiateItemView: ", e);
        }


        return root;
    }

    @Override
    protected int getMaxCount() {
        if (couponList == null) {
            return 0;
        }
        return couponList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position+"";
    }
}
