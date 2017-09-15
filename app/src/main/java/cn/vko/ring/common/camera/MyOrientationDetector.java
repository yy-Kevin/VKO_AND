package cn.vko.ring.common.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.OrientationEventListener;

public class MyOrientationDetector extends OrientationEventListener {
	int Orientation;

	public MyOrientationDetector(Context context) {
		super(context);
		Orientation = context.getResources().getConfiguration().orientation;
	}

	@Override
	public void onOrientationChanged(int orientation) {
		Log.i("MyOrientationDetector ", "onOrientationChanged:" + orientation);
		this.Orientation = orientation;
		Log.d("MyOrientationDetector", "当前的传感器方向为" + orientation);
	}

	public int getOrientation() {
		return Orientation;
	}
}
