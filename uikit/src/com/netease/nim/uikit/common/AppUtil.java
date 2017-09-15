package com.netease.nim.uikit.common;

import android.os.Build;
import android.util.Log;

/**
 * desc:
 * Created by jiarh on 16/5/31 16:31.
 */
public class AppUtil {

    private static final String TAG = "AppUtil";

    public static boolean is64cpu() {
        // If already checked return cached result

        String CPU_ABI = android.os.Build.CPU_ABI;
        String CPU_ABI2 = "none";


        Log.d(TAG, "is64cpu() called with:CPU_ABI= " + CPU_ABI+"");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) { // CPU_ABI2
            // since
            // 2.2
            try {
           CPU_ABI2 = (String) android.os.Build.class.getDeclaredField(
                        "CPU_ABI2").get(null);
                Log.d(TAG, "is64cpu() called with:CPU_ABI2= " + CPU_ABI2+"");
            } catch (Exception e) {
                return false;
            }
        }

        if (CPU_ABI.equals("arm64-v8a") || CPU_ABI2.equals("arm64-v8a")||CPU_ABI.equals("x86_64") || CPU_ABI2.equals("x86_64")) {
            return true;
        }

  return false;

    }


}
