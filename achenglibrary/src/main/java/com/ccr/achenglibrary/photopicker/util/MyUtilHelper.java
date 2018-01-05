package com.ccr.achenglibrary.photopicker.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;

/**
 * 在此写用途
 *
 * @Author: Acheng
 * @Email: 345887272@qq.com
 * @Date: 2017-09-04 10:15
 * @Version: V1.0 <描述当前版本功能>
 */

public class MyUtilHelper {
    /**
     * 显示底部栏
     */
    public static void showBottomUIMenu(Context activity) {
        View v = ((Activity) activity).getWindow().getDecorView();
        v.setSystemUiVisibility(View.VISIBLE);
    }

    public static void hideBottomUIMenu(Context activity) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = ((Activity) activity).getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = ((Activity) activity).getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
