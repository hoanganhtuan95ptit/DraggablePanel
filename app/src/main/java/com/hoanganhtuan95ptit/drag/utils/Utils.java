package com.hoanganhtuan95ptit.drag.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.hoanganhtuan95ptit.drag.App;

import androidx.annotation.DimenRes;

public class Utils {

    public static int getScreenHeight() {
        Context c = App.self();
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidth() {
        Context c = App.self();
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static float spFromRes(@DimenRes int res) {
        return App.self().getResources().getDimension(res);
    }

    public static float dpFromRes(@DimenRes int res) {
        return App.self().getResources().getDimension(res) / App.self().getResources().getDisplayMetrics().density;
    }

    public static float dp2px(float dp) {
        final float scale = App.self().getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(float sp) {
        final float scale = App.self().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getStatusBarHeight() {
        return getStatusBarHeight(App.self());
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarHeightId > 0) {
            return resources.getDimensionPixelSize(statusBarHeightId);
        } else {
            return 0;
        }
    }

}
