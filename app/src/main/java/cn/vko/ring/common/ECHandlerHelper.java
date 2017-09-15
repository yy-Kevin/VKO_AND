package cn.vko.ring.common;

import android.os.Handler;
import android.os.Looper;

public class ECHandlerHelper {
	private static Handler a;
	private static Handler a() {
		if (a == null)
			a = new Handler(Looper.getMainLooper());
		return a;
	}

	public static void postRunnOnUI(Runnable paramRunnable) {
		if (paramRunnable == null)
			return;
		a().post(paramRunnable);
	}

}
