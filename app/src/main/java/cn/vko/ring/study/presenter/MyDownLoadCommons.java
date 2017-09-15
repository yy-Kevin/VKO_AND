package cn.vko.ring.study.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.Video;

import java.util.ArrayList;
import java.util.Calendar;

import cn.shikh.utils.DateUtils;
import cn.shikh.utils.NetUtils;
import cn.vko.ring.Constants;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.common.model.ItemDialogModel;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog.OnSweetClickListener;
import cn.vko.ring.common.widget.dialog.CommonDialog;
import cn.vko.ring.mine.activity.SystemActivity;
import cn.vko.ring.study.db.DBservice;
import cn.vko.ring.study.model.BaseUnitData;
import cn.vko.ring.study.model.DownloadInfo;

public class MyDownLoadCommons {

	private Context ctx;
	// private DisplayImageOptions options;
	private DBservice service;
	private SweetAlertDialog dialoglogAlert;
	public BaseUnitData t;
	private CommonDialog mDialog;
	private SharedPreferences sharedPreferences;
//	private int i;

	public MyDownLoadCommons(Context ctx) {
		this.ctx = ctx;
		this.service = new DBservice(ctx);
	}

	public void loadVideo(BaseUnitData t) {
		this.t = t;
		if (!NetUtils.isCheckNetAvailable(ctx)) {
			Toast.makeText(ctx, "网络连接失败", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (NetUtils.getAPNType(ctx) == NetUtils.WIFI) {
				initData(t);
			} else {
				// 判断app的移动网络下载是否开启 true为开启
				if (VkoConfigure.getConfigure(ctx).getBoolean(
						Constants.STATEDOWN, false)) {
					initData(t);
				} else {
					// 移动网络没有打开 ，是否打开
					showAlerDialogNet();

				}

			}

		}
	}

	private void initData(BaseUnitData t2) {
		if (t2.isAuth()) {
			init();
		} else {
			Toast.makeText(ctx.getApplicationContext(), "你没有下载该视频的权限",Toast.LENGTH_SHORT).show();
		}

	}

	private void init() {
		// iv_download_video.setClickable(true);
		if(t.getVideo() == null) return;
		Video.loadVideo(t.getVideo().getPlay(), new Video.OnVideoLoaded() {
			public void onloaded(final Video v) {
				if (v == null) {
					return;
				}
				// 码率数
				int df_num = v.getDfNum();
				String[] items = null;
				if (df_num == 1) {
					items = new String[] { "流畅" };
				}
				if (df_num == 2) {
					items = new String[] { "流畅", "高清" };
				}
				if (df_num == 3) {
					items = new String[] { "流畅", "高清", "超清" };
				}
				showChoseRateDialog(items, v);
			}
		});
	}

	private void showChoseRateDialog(String[] rates, final Video v) {
		if (rates == null || rates.length <= 0)
			return;
		ArrayList<ItemDialogModel> items = new ArrayList<ItemDialogModel>();
		for (String str : rates) {
			items.add(new ItemDialogModel(str));
		}
		if (mDialog == null) {
			mDialog = new CommonDialog.Builder(ctx).setItems(items)
					.setTitleText("选择下载码率")
					.setOnLvItemClickListener(new CommonDialog.Builder.OnLvItemClickListener() {

						@Override
						public void onItemClick(View view, int position) {
							// TODO Auto-generated method stub

							int bitrate = position + 1;
							DownloadInfo downloadInfo = new DownloadInfo(t
									.getVideo().getPlay(), t.getVideo()
									.getVideoId(), t.getVideo().getGoodsId(), v
									.getDuration(), v.getFileSize(bitrate),
									bitrate);
							downloadInfo.setTitle(t.getVideo().getVideoName());
							if (service != null && !service.isAdd(downloadInfo)) {
								service.addDownloadFile(downloadInfo);
								PolyvDownloader polyvDownloader = PolyvDownloaderManager
										.getPolyvDownloader(t.getVideo().getPlay(), bitrate);
								polyvDownloader.start();
								if (VkoConfigure.getConfigure(ctx).getBoolean(
										Constants.STATEDOWN, false)
										&& NetUtils.getAPNType(ctx) != NetUtils.WIFI) {
									Toast.makeText(ctx, "当前在移动网络下下载",
											Toast.LENGTH_SHORT).show();
								}
								if (NetUtils.getAPNType(ctx) == NetUtils.WIFI) {
									Toast.makeText(ctx, "已开始下载，可在我的-我的下载中查看",Toast.LENGTH_SHORT).show();
								}
								VkoConfigure.getConfigure(ctx).put(
										DateUtils.currentData() + "1", true);

							} else {
								((Activity) ctx).runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(ctx, "下载任务已经增加到队列", Toast.LENGTH_LONG).show();
									}
								});

							}
							setSharedPreference();
							mDialog.dismiss();
						}
					}).build();
		}
		mDialog.show();
	}

	// 存储sharedpreferences
	public void setSharedPreference() {
//		int i = 0;
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String year1 = String.valueOf(year);
		String month1 = String.valueOf(month);
		String day1 = String.valueOf(day);
		String downLoadDay=year1+month1+day1;
		sharedPreferences = ctx.getSharedPreferences("downtimes", Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		int count = sharedPreferences.getInt("downLoadTime",0);
		editor.putInt("downLoadTime",++count);
		editor.putString("downLoadDay",downLoadDay);
//		Log.e("========时间",downLoadDay);
//		Log.e("========downLoadTime",i+"");
		editor.commit();// 提交修改
	}

	/**
	 * 移动网络设置没有打开
	 */
	private void showAlerDialogNet() {
		// TODO Auto-generated method stub
		if (dialoglogAlert == null) {
			dialoglogAlert = new SweetAlertDialog(ctx,
					SweetAlertDialog.WARNING_TYPE);
			dialoglogAlert.setContentText("当前没有网络，请跳至设置进行设置");
			dialoglogAlert.setTitleText("提示！");
			dialoglogAlert.setCancelText("取消");
			dialoglogAlert.setConfirmText("确定");
			dialoglogAlert.setCancelClickListener(new OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sweetAlertDialog) {
					// TODO Auto-generated method stub
					dialoglogAlert.dismiss();

				}
			});
			dialoglogAlert.setConfirmClickListener(new OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sweetAlertDialog) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ctx, SystemActivity.class);
					ctx.startActivity(intent);
					dialoglogAlert.dismiss();
				}
			});
		}
		dialoglogAlert.show();
	}

}
