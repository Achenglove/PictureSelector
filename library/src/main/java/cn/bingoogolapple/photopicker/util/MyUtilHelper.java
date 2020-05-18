package cn.bingoogolapple.photopicker.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.bingoogolapple.photopicker.R;

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

    /**
     * 切换动画
     */
    public static void showAnimation(int type, boolean isShow, View view, Context context) {
        Animation animation;
        if (type == 1) {//上下
            if (isShow) {
                if (view.getVisibility() == View.GONE) {
                    view.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
                    view.startAnimation(animation);
                }
            } else {
                if (view.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);
                    view.startAnimation(animation);
                }
            }
        } else {//左右
            if (isShow) {
                if (view.getVisibility() == View.GONE) {
                    view.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(context, R.anim.slide_left_in);
                    view.startAnimation(animation);
                }
            } else {
                if (view.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(context, R.anim.slide_right_out);
                    view.startAnimation(animation);
                }
            }
        }
    }

    public static void releaseInputMethodManagerFocus(Activity paramActivity) {
        if (paramActivity == null) return;
        int count = 0;
        while (true) {
            //给个5次机会 省得无限循环
            count++;
            if (count == 5) return;
            try {
                InputMethodManager localInputMethodManager = (InputMethodManager) paramActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (localInputMethodManager != null) {
                    Method localMethod = InputMethodManager.class.getMethod("windowDismissed", new Class[]{IBinder.class});
                    if (localMethod != null) {
                        localMethod.invoke(localInputMethodManager, new Object[]{paramActivity.getWindow().getDecorView().getWindowToken()});
                    }
                    Field mLastSrvView = InputMethodManager.class.getDeclaredField("mLastSrvView");
                    if (mLastSrvView != null) {
                        mLastSrvView.setAccessible(true);
                        mLastSrvView.set(localInputMethodManager, null);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
