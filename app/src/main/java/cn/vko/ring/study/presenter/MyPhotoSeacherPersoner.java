package cn.vko.ring.study.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.study.activity.CameraActivity;

public class MyPhotoSeacherPersoner {
	public Context context;

	public MyPhotoSeacherPersoner(Context context) {
		super();
		this.context = context;		
		saySearch();
	}

	protected void saySearch() {
		Bundle bundle = new Bundle();
		bundle.putString("FROM", "SEARCH");
		StartActivityUtil.startActivity(context, CameraActivity.class,bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}


}
