package com.joke.autobanner.bean;

import android.support.annotation.ColorInt;

/**
 * Created by SHF on 2016/12/15.
 */

public class BannerBean {
    private String title = "this is the title";
    private String info = "this is the info";
    private String pic ;
    private @ColorInt int colorInt = 0xff758CEE;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getColorInt() {
        return colorInt;
    }

    public void setColorInt(int colorInt) {
        this.colorInt = colorInt;
    }
}
