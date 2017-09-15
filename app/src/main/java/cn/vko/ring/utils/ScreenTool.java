package cn.vko.ring.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by A on 2017/2/24.
 */
public class ScreenTool {

    private static final int HIDE_STATUSBAR = 1;
    private static final Handler HANDLER = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case 1:
                    ScreenTool.reSetStatusBar((Activity)msg.obj);
                default:
            }
        }
    };

    public ScreenTool() {
    }

    private static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static void setHideStatusBarListener(final Activity activity, final long millissecond) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & 4) == 0) {
                    Message message = ScreenTool.HANDLER.obtainMessage();
                    message.obj = activity;
                    message.what = 1;
                    ScreenTool.HANDLER.sendMessageDelayed(message, millissecond);
                }

            }
        });
    }

    public static int[] getNormalWH(Activity activity) {
        if(Build.VERSION.SDK_INT < 14) {
            DisplayMetrics point1 = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(point1);
            return new int[]{point1.widthPixels, point1.heightPixels};
        } else {
            Point point = new Point();
            WindowManager wm = activity.getWindowManager();
            wm.getDefaultDisplay().getSize(point);
            return new int[]{point.x, point.y};
        }
    }

    public static void reSetStatusBar(Activity activity) {
        if(isLandscape(activity)) {
            hideStatusBar(activity);
        } else {
            setDecorVisible(activity);
        }

    }

    private static void hideStatusBar(Activity activity) {
        if(Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(1024, 1024);
        } else {
            View decorView = activity.getWindow().getDecorView();
            activity.getWindow().setFlags(1024, 1024);
            short uiOptions = 7172;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

    private static void setDecorVisible(Activity activity) {
        if(Build.VERSION.SDK_INT < 16) {
            activity.getWindow().clearFlags(1024);
        } else {
            View decorView = activity.getWindow().getDecorView();
            activity.getWindow().clearFlags(1024);
            byte uiOptions = 0;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }
}
