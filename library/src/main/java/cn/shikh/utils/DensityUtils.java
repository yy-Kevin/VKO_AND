/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: DensityUtils.java 
 * @Prject: android-utils
 * @Package: cn.com.mink.utils 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-18 下午2:07:56 
 * @version: V1.0   
 */
package cn.shikh.utils;

import android.content.Context;
import android.util.TypedValue;

/** 
 * @ClassName: DensityUtils 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-18 下午2:07:56  
 */
public class DensityUtils {
	private DensityUtils()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");  
    }  
  
    /** 
     * dp转px 
     *  
     * @param context 
     * @param val 
     * @return 
     */  
    public static int dp2px(Context context, float dpVal)  
    {  
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  
                dpVal, context.getResources().getDisplayMetrics());  
    }  
  
    /** 
     * sp转px 
     *  
     * @param context 
     * @param val 
     * @return 
     */  
    public static int sp2px(Context context, float spVal)  
    {  
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,  
                spVal, context.getResources().getDisplayMetrics());  
    }  
  
    /** 
     * px转dp 
     *  
     * @param context 
     * @param pxVal 
     * @return 
     */  
    public static float px2dp(Context context, float pxVal)  
    {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (pxVal / scale);  
    }  
  
    /** 
     * px转sp 
     *  
     * @param fontScale 
     * @param pxVal 
     * @return 
     */  
    public static float px2sp(Context context, float pxVal)  
    {  
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);  
    }  
}
