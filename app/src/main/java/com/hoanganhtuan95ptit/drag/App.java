package com.hoanganhtuan95ptit.drag;

import android.app.Application;

import com.hoanganhtuan95ptit.drag.data.network.ApiHelper;
import com.hoanganhtuan95ptit.drag.data.network.AppApiHelper;
import com.hoanganhtuan95ptit.drag.utils.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application implements Constants {
    private static App self;

    public static App self() {
        return self;
    }

    private ApiHelper apiHelper;

    private int marginThree;
    private int padding;
    private int margin;
    private int round;

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        apiHelper = new AppApiHelper();

        marginThree = (int) Utils.dp2px(4);
        padding = (int) Utils.dp2px(16);
        margin = (int) Utils.dp2px(8);
        round = margin;
    }

    public ApiHelper getApiHelper() {
        return apiHelper;
    }

    public int getMarginThree() {
        return marginThree;
    }

    public int getPadding() {
        return padding;
    }

    public int getMargin() {
        return margin;
    }

    public int getRound() {
        return round;
    }
}
