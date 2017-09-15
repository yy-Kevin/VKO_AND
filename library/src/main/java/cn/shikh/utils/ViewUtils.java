package cn.shikh.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

public class ViewUtils {

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}


	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	public static int getTitleHight(Activity context){
		int contentTop = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();  
		//statusBarHeight是上面所求的状态栏的高度  
		int titleBarHeight = contentTop - getStatusBarHeight(context) ; 
		return titleBarHeight;
	}
	/**
	 * 获取分辨率
	 * 
	 * @param context
	 * @return
	 */
	public static float getScreenDensity(Context context) {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			return dm.density;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1.0f;
	}
	public static int getRealHeight(Activity context) {
		return getScreenHeight(context) - getStatusBarHeight(context) - getTitleHight(context);
	}

	public static int getVrtualBtnHeight(Context poCotext) {
		int location[] = getScreenWH(poCotext);
		int realHeiht = getDpi((Activity) poCotext);
		int virvalHeight = realHeiht - location[1];
		return virvalHeight;
	}
	public static int[] getScreenWH(Context poCotext) {
		WindowManager wm = (WindowManager) poCotext
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		return new int[] { width, height };
	}

	public static int getDpi(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		int height = 0;
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			height = dm.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return height;
	}
	/**
	 * 获取状态栏的高度
	 * 
	 * @auther 惠鹏涛
	 * @param context
	 * @return int 上午11:32:40
	 */
	public static int getStatusBarHeight(Activity context) {
		Rect frame = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	public static Rect getRect(Activity activity) {
		Rect outRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(outRect);
		return outRect;
	}

	/** 隐藏与现实软键盘 */
	public static void hideOrShowSoftInput(Context cx, View edit, boolean hide) {
		InputMethodManager imm = (InputMethodManager) cx
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (hide) {
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
		} else {
			edit.setFocusable(true);
			edit.setFocusableInTouchMode(true);
			edit.requestFocus();
			imm.showSoftInput(edit, 0);
		}
	}

	/**
	 * 判断是否存在sd�?
	 */
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param context
	 * @param editText
	 *            输入框
	 * @param errorStringId
	 *            错误提示资源ID
	 * @return
	 */
	public static boolean validateEmpty(Context context, EditText editText,
			int errorStringId) {
		String text = editText.getText().toString();
		if (TextUtils.isEmpty(text)) {
			editText.setError(Html.fromHtml("<font color='red'>"
					+ context.getString(errorStringId) + "</font>"));
			return false;
		}
		return true;
	}

	/**
	 * 获取当前应用程序的版本号
	 */
	public static String getVersion(Context context) {
		// TODO Auto-generated method stub
		try {
			// 得到包管理器(管理了所有的应用程序)
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * 设备型号
	 * 
	 * @return
	 */
	public static String getModel() {
		return Build.MODEL;
	}

	/**
	 * 设备的系统版本
	 * 
	 * @return
	 */
	public static String getVersionRelease() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 设备SDK版本
	 */
	public static String getVersionSDK() {
		return Build.VERSION.SDK;
	}

	public static boolean isAddShortCut(Context context, int appName) {

		boolean isInstallShortcut = false;
		final ContentResolver cr = context.getContentResolver();

		int versionLevel = Build.VERSION.SDK_INT;
		String AUTHORITY = "com.android.launcher2.settings";

		// 2.2以上的系统的文件文件名字是不一样的
		if (versionLevel >= 8) {
			AUTHORITY = "com.android.launcher2.settings";
		} else {
			AUTHORITY = "com.android.launcher.settings";
		}

		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { context.getString(appName) }, null);

		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}

	// 添加快捷方式：
	public static void addShortCut(Context context, int appName, int icon,
			Class cl) {

		if (isAddShortCut(context, appName))
			return;
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 设置属性
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		ShortcutIconResource iconRes = ShortcutIconResource.fromContext(
				context, icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, iconRes);

		// 是否允许重复创建
		shortcut.putExtra("duplicate", false);

		// 设置桌面快捷方式的图标
		Parcelable pIcon = ShortcutIconResource.fromContext(context,
				icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, pIcon);

		// 点击快捷方式的操作
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(context, cl);

		// 设置启动程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 广播通知桌面去创建
		context.sendBroadcast(shortcut);
	}

	/**
	 * 获取application中指定的meta-data
	 * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
	 */
	public static String getAppMetaData(Context ctx, String key) {
		if (ctx == null || TextUtils.isEmpty(key)) {
			return null;
		}
		String resultData = null;
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
				if (applicationInfo != null) {
					if (applicationInfo.metaData != null) {
						resultData = applicationInfo.metaData.getString(key);
					}
				}

			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return resultData;
	}

}
