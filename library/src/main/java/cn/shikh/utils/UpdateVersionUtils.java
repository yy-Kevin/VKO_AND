package cn.shikh.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.io.File;

import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.callback.FileCallBack;
import okhttp3.Call;

/**
 * @author：shikh on 2016/3/30
 * email：shikunhai@foxmail.com
 */
public class UpdateVersionUtils extends AsyncTask<String, Void, Boolean> {
    protected static final int DOWNLOAD_PROGRES = 2;
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;

    private int logo;
    private String appName;
    private Context context;
    private  RemoteViews v;
    // 通知栏
    private Notification updateNotification = null;
    private NotificationManager updateNotificationManager = null;
    private PendingIntent updatePendingIntent = null;
    private Notification.Builder builder;
    private int downProgress;
    public UpdateVersionUtils(Context context, String appName, int logo) {
        this.logo = logo;
        this.appName = appName;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String downloadUrl = strings[0];
        String deskUrl = strings[1];
        updateThread(logo,downloadUrl,deskUrl);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updateThread(int logo, final String downloadUrl, String deskUrl) {
        builder = new Notification.Builder(context).setTicker(appName + "开始下载").setSmallIcon(logo);
        v = new RemoteViews(context.getPackageName(), R.layout.offline_down_noti);
        v.setTextViewText(R.id.offline_down_title, "正在下载" + appName);
        v.setImageViewResource(R.id.offline_down_pic,logo);
        updateNotification = builder.setContent(v).setContentIntent(updatePendingIntent).build();
        updateNotification.flags = Notification.FLAG_ONGOING_EVENT;
        if (updateNotificationManager == null) {
            updateNotificationManager = (NotificationManager) context
                    .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        }
        updateNotificationManager.notify(0, updateNotification);

        if(FileUtils.hasSdcard()){

            OkHttpUtils.get().url(downloadUrl).build().execute(new FileCallBack(deskUrl,appName+".apk") {

                @Override
                public void inProgress(float progress, long total) {
                    if(downProgress == 0){
                        updateHandler.sendEmptyMessageDelayed(DOWNLOAD_PROGRES,1000);
                    }
                    downProgress = (int)(progress*100);
                }

                @Override
                public void onError(Call call, Exception e) {
                    updateHandler.sendEmptyMessage(DOWNLOAD_FAIL);
                }

                @Override
                public void onResponse(File response) {
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.addCategory("android.intent.category.DEFAULT");
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromFile(response);
                    installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                    updatePendingIntent = PendingIntent.getActivity(context, 0, installIntent, 0);
                    updateHandler.sendEmptyMessage(DOWNLOAD_COMPLETE);
                }
            });
        }
    }

    private Handler updateHandler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case DOWNLOAD_COMPLETE:
                    updateHandler.removeMessages(DOWNLOAD_PROGRES);
                    // 点击安装PendingIntent
                    updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
                    updateNotification.defaults = Notification.DEFAULT_SOUND;// 铃声提醒
                    v.setTextViewText(R.id.offline_down_title, "下载完成，点击安装");
//                    v.setViewVisibility(R.id.offline_down_pb, View.GONE);
                    updateNotification =  builder.setContentIntent(updatePendingIntent).setContentTitle(appName).setContentText("下载完成，点击安装").build();
//                    updateNotification.setLatestEventInfo(context, appName, "下载完成，点击安装", updatePendingIntent);
                    updateNotificationManager.notify(0, updateNotification);
                    break;
                case DOWNLOAD_FAIL:
                    updateHandler.removeMessages(DOWNLOAD_PROGRES);
                    updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                    v.setTextViewText(R.id.offline_down_title, "下载失败");
                    updateNotification = builder.setContentIntent(updatePendingIntent).setContentTitle(appName).setContentText("下载失败").build();
                    updateNotificationManager.notify(0, updateNotification);
                    break;
                case DOWNLOAD_PROGRES:
//                    int progress = msg.arg1;
                    v.setTextViewText(R.id.offline_down_percent, downProgress + "%");
                    v.setProgressBar(R.id.offline_down_pb, 100, downProgress, false);
                    updateNotificationManager.notify(0, updateNotification);
                    updateHandler.sendEmptyMessageDelayed(DOWNLOAD_PROGRES,1000);
                    break;
            }
        }
    };
}
