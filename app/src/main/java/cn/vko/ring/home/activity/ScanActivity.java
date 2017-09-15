package cn.vko.ring.home.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.SystemBarTintManager;
import cn.vko.ring.R;
import cn.vko.ring.circle.activity.GroupHomeActivity;
import cn.vko.ring.circle.activity.ScanDisplyAtcivity;
import cn.vko.ring.circle.activity.TopicDetailH5Activity;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog.OnSweetClickListener;
import cn.vko.ring.home.listener.IMenuItemClickListener;
import cn.vko.ring.home.model.TabMenuInfo;
import cn.vko.ring.home.widget.TabMenuView;

import com.google.zxing.Result;
import com.zxing.android.CaptureActivity;
import com.zxing.android.camera.CameraManager.Callback;

public class ScanActivity extends CaptureActivity {

	public TabMenuView tabView;
	public SystemBarTintManager tintManager;
	SweetAlertDialog dialog;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			showDialog();
		};
	};

	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		initStatus();
		return R.layout.activity_scan;
	}

	private void initStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
			// 创建状态栏的管理实例
			tintManager = new SystemBarTintManager(this);
			// 设置一个状态栏资源
			tintManager.setStatusBarTintResource(R.color.black);
			// 激活状态栏设置
			tintManager.setStatusBarTintEnabled(true);
			// 激活导航栏设置
			tintManager.setNavigationBarTintEnabled(false);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tabView = (TabMenuView) findViewById(R.id.scan_bottom_menu);
		TabMenuInfo group = new TabMenuInfo(1, R.drawable.scan_groupa,
				R.drawable.scan_groupb, R.string.scan_group_text,
				R.color.text_default_color, R.color.bg_main_blue_color);
		TabMenuInfo active = new TabMenuInfo(2, R.drawable.scan_activitea,
				R.drawable.scan_activiteb, R.string.scan_active_text,
				R.color.text_default_color, R.color.bg_main_blue_color);
		TabMenuInfo other = new TabMenuInfo(3, R.drawable.scan_othera,
				R.drawable.scan_otherb, R.string.scan_other_text,
				R.color.text_default_color, R.color.bg_main_blue_color);

		tabView.addMenuItem(group, 0);
		tabView.addMenuItem(active, 1);
		tabView.addMenuItem(other, 2);

		tabView.setOnMenuClickListener(new IMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position) {
				tabView.setSelectIndex(position);
			}
		});
		// 默认选中课程页
		tabView.setSelectIndex(0);

	}

	@Override
	public void returnResult(Result obj, Bitmap barcode) {
		// TODO Auto-generated method stub
		if (obj != null && !TextUtils.isEmpty(obj.getText())) {
			String result = obj.getText();
			Bundle bundle = new Bundle();
			if (result.contains("http://m.vko.cn/group/crowdHome.html")
					|| result
							.contains("http://m.vko.cn/group/sCircleTopics.html")) {//群主页
				if (result.contains("groupId=")) {
					String groupId = result.substring(result
							.indexOf("groupId=") + 8);
					bundle.putString("GROUPID", groupId);
					bundle.putString("URL", result);
					StartActivityUtil.startActivity(this, GroupHomeActivity.class,
							bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
				}
			} else if (result.startsWith("http://m.vko.cn/group/scTopicDetail.html")) {//话题详题
				String articleId = result.substring(result.indexOf("articleId=") + 10);
				bundle.putString("ARTICLEID", articleId);
				bundle.putString("URL", result);
				StartActivityUtil.startActivity(this, TopicDetailH5Activity.class,
						bundle, Intent.FLAG_ACTIVITY_NEW_TASK);
			} else if (result.startsWith("http")){
				bundle.putString("URL", result);
				StartActivityUtil.startActivity(this, ScanDisplyAtcivity.class, bundle,
						Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}
			finish();
		}
	}

	private void showDialog() {
		// TODO Auto-generated method stub
		dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
		dialog.setContentText("请检查是否拍照权限被禁");
		dialog.setTitle("拍照异常");
		dialog.setCancelable(false);
		dialog.setConfirmText("OK");
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				ScanActivity.this.finish();
			}
		});
		dialog.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cameraManager.setCallback(new Callback() {
			@Override
			public void onError() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}
		});
	}

}
