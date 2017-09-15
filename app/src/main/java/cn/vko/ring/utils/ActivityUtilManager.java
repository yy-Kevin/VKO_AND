package cn.vko.ring.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.home.activity.MainActivity;

public class ActivityUtilManager {

	private List<Activity> activityMap = new ArrayList<Activity>();

	private static ActivityUtilManager instance;

	public synchronized static ActivityUtilManager getInstance() {
		if (null == instance) {
			instance = new ActivityUtilManager();
		}
		return instance;
	}

	public void addActivity(Activity act) {
		// if(!activityMap.containsKey(act.getClass().getName())){
		activityMap.add(act);
		// }
	}

	public void delActivity(Activity act) {
		if (activityMap.contains(act)) {
			activityMap.remove(act);
		}
	}

	public boolean isContains(Activity act) {
		return activityMap.contains(act);
	}

	public boolean isContains(String name) {
		for (Activity act : activityMap) {
			if (act.getClass().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Activity getTopActivity(Context context) {
		String activityName = getTopActivityName(context);
		for (Activity act : activityMap) {
			if (act.getClass().getName().equals(activityName)) {
				return act;
			}
		}
		return null;
	}

	private String getTopActivityName(Context context)

	{
		ActivityManager manager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
		if (runningTaskInfos != null) {
			return (runningTaskInfos.get(0).topActivity).getClassName();
		}
		return null;

	}

	public void startMActivity(Context context){
		String mainAcitity = context.getPackageName()+".home.activity.MainActivity";
		if (isContains(mainAcitity) ){
			return;
		}
		StartActivityUtil.startActivity(context, MainActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK);

	}
	/**
	 * 返回app运行状态
	 * 1:程序在前台运行
	 * 2:程序在后台运行
	 * 3:程序未启动
	 * 注意：需要配置权限<uses-permission android:name="android.permission.GET_TASKS" />
	 */
	public int getAppSatus(Context context, String pageName) {

	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> list = am.getRunningTasks(20);

	    //判断程序是否在栈顶
	    if (list.get(0).topActivity.getPackageName().equals(pageName)) {
	        return 1;
	    } else {
	        //判断程序是否在栈里
	        for (RunningTaskInfo info : list) {
	            if (info.topActivity.getPackageName().equals(pageName)) {
	                return 2;
	            }
	        }
	        return 3;//栈里找不到，返回3
	    }
	}
	
}
