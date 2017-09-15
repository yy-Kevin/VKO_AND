package cn.vko.ring.common.umeng;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;

/**
 * 友盟统计
 * 
 * @author jrh
 * 
 */
public class EventCountAction {

	/*public static void onFragResumCount(Activity act, String fragMentName) {
		onFragRCount(fragMentName); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		onActivityResumCount(act); // 统计时长
	}*/

	public static void onFragRCount(Class c) {
		MobclickAgent.onPageStart(c.getSimpleName());
	}

	/**
	 * 在activity 的 onResume() 方法中调用
	 * 
	 * @param act
	 */
	public static void onActivityResumCount(Activity act) {
		MobclickAgent.onResume(act);
	}


	/*public static void onFragPauseCount(Activity act, String fragMentName) {
		onFragPCount(fragMentName);
		onActivityPauseCount(act);
	}
*/
	public static void onFragPCount(Class c) {
		MobclickAgent.onPageEnd(c.getSimpleName());
	}

	/**
	 * 在activity 的 onPause() 方法中调用
	 * 
	 * @param act
	 */
	public static void onActivityPauseCount(Activity act) {
		MobclickAgent.onPause(act);
	}
}
