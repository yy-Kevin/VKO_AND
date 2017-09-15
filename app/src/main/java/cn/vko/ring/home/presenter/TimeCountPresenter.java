/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: TimeCountPresenter.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.home.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-8-27 下午6:29:22 
 * @version: V1.0   
 */
package cn.vko.ring.home.presenter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.VolleyError;

import cn.shikh.utils.DateUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.common.volley.VolleyUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * @ClassName: TimeCountPresenter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-8-27 下午6:29:22
 */
public class TimeCountPresenter {
	private Context mContext;
	private static final String MY_PKG_NAME = "cn.vko.ring";
	private boolean isAppRunning;
	private ActivityManager am;
	public long starTime;
	private long endTime;
	private VolleyUtils<BaseResponseInfo> mVolleyUtils;
	private static  TimeCountPresenter Instance;

	public static TimeCountPresenter getInstance(Context context) {
		if (Instance == null) {
			Instance = new TimeCountPresenter(context);
		}
		return Instance;
	}
	private TimeCountPresenter(Context mContext) {
		super();
		this.mContext = mContext;
		init();
	}
	public void init() {
		am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		mVolleyUtils = new VolleyUtils<BaseResponseInfo>(mContext,BaseResponseInfo.class);
	}
	public void upLoadUseTime() {
//		Uri.Builder builder = mVolleyUtils.getBuilder(ConstantUrl.VK_APP_GATHER);
//		builder.appendQueryParameter("terminal", 1 + "");
//		builder.appendQueryParameter("phoneModel", android.os.Build.MODEL + "");
//		builder.appendQueryParameter("startTime", starTime + "");
//		builder.appendQueryParameter("endTime", endTime + "");
//		System.out.println("START_TIME=" + DateUtils.formatDate("yyyy-MM-dd hh:mm:ss", starTime));
//		System.out.println("END_TIME=" + DateUtils.formatDate("yyyy-MM-dd hh:mm:ss", endTime));
//		mVolleyUtils.requestHttpGetNoLoading(builder, new IVolleyResponseListener<BaseResponseInfo>() {
//			@Override
//			public void onResponse(BaseResponseInfo response) {
//				if (response != null && response.getCode() == 0) {
//					starTime = 0;
//					endTime = 0;
//				}
//			}
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				// TODO Auto-generated method stub
//			}
//		}, BaseResponseInfo.class);
	}
	public long endCountTime() {
		return System.currentTimeMillis();
	}
	/**
	 * 检查是否正在运行
	 * @Title: checkIsRunning
	 * @Description: TODO
	 * @return: void
	 */
	public void checkIsRunning() {
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		System.out.println("RunningTaskInfoSize=" + list.size());
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
					&& info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
				System.out.println("topActivityPackageName=" + info.topActivity.getPackageName());
				isAppRunning = true;
				break;
			}
		}
	}
	/**
	 * 检测某ActivityUpdate是否在当前Task的栈顶
	 * @Title: isTopActivy
	 * @Description: TODO
	 * @param cmdName
	 * @return
	 * @return: boolean
	 */
	public boolean isTopActivy(String cmdName) {
		List<RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);
		String cmpNameTemp = null;
		if (null != runningTaskInfos) {
			cmpNameTemp = (runningTaskInfos.get(0).topActivity.toString());
		}
		if (null == cmpNameTemp)
			return false;
		return cmpNameTemp.equals(cmdName);
	}
	private boolean isTopActivity() {
		List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (MY_PKG_NAME.equals(tasksInfo.get(0).topActivity.getPackageName())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断当前应用程序处于前台还是后台
	 */
	public boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
	public boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				/*
				 * BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000 PERCEPTIBLE=130 SERVICE=300
				 * ISIBLE=200
				 */
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				
					if (VkoContext.getInstance(context).getUser() != null) {
						endTime = endCountTime();
					}
					if (starTime < endTime && starTime > 0 && endTime > 0) {
						upLoadUseTime();
						System.out.println(context.getPackageName() + "处于后台" + appProcess.processName + "##startTime="
								+ starTime + "##endTime=" + endTime + "##用时：" + (endTime - starTime));
					}
					return true;
				} else {
					Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
					if (starTime == 0) {
						if (VkoContext.getInstance(context).getUser() != null) {
							starTime = endCountTime();
							System.out.println("startTime=" + starTime);
						}
					}
					return false;
				}
			}
		}
		return false;
	}
	public long getStarTime() {
		return starTime;
	}
	public void setStarTime(long starTime) {
		this.starTime = starTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public boolean isAppRunning() {
		return isAppRunning;
	}
	public void setAppRunning(boolean isAppRunning) {
		this.isAppRunning = isAppRunning;
	}
}
