package cn.vko.ring.home.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.UpdateVersionUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoApplication;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.mine.model.VersionUpdateInfo;
import cn.vko.ring.utils.FileUtil;

/**
 * Created by shikh on 2016/5/11.
 */
public class VersionUpdatePresenter implements UIDataListener<VersionUpdateInfo> {
    private Activity context;
    private boolean isAuthor;
    public VersionUpdatePresenter(Activity context,boolean isAuthor){
        this.context = context;
        this.isAuthor = isAuthor;
        getUpdateTask();
    }

    /**
     * 版本更新
     */
    private void getUpdateTask() {
        String url = ConstantUrl.VKOIP + "/app/v";
        VolleyUtils<VersionUpdateInfo> volleyUtils = new VolleyUtils<VersionUpdateInfo>(context,VersionUpdateInfo.class);
        Uri.Builder builder = volleyUtils.getBuilder(url);
        // builder.appendQueryParameter("sid","");
        builder.appendQueryParameter("channelName", VkoApplication
                .getInstance().getChannel());
        Log.e("========渠道名称",VkoApplication.getInstance().getChannel()+"");
        builder.appendQueryParameter("name", context.getResources().getString(R.string.app_name));
        volleyUtils.sendGETRequest(true,builder);
        volleyUtils.setUiDataListener(this);

    }

    /**
     * 获得本应用程序的版本号
     *
     * @return
     */
    public int getVersionCode() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void installAPK() {
        // 安装apk
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(FileUtil.getDownloadDir().getAbsolutePath() + "/apk/vko.apk")),
                "application/vnd.android.package-archive");
        context.startActivityForResult(intent, 1);
    }

    @Override
    public void onDataChanged(VersionUpdateInfo t) {
        if (t != null) {
            if (t.code == 0 && t.data != null) {
                if (getVersionCode() < t.data.ver) {
                    String message = t.data.info;
                    String dir = FileUtil.getDownloadDir().getAbsolutePath();
                    String fileName = VkoApplication.getInstance().getChannel()
                            + "_" + t.data.vers;
                    showUpdateDialog(dir,message,t.data.url,fileName);
                } else if(!isAuthor){
                    Toast.makeText(context, "您使用的是最新版本", Toast.LENGTH_SHORT).show();
                }
            }
        } else if(!isAuthor){
            Toast.makeText(context, "没内容哦！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {

    }

    public  void showUpdateDialog( final String dir,String message,final String downloadUrl,final String appName) {
        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("软件升级").setContentText(message)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        // TODO Auto-generated method stub
                        sweetAlertDialog.dismiss();
                    }
                }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                // TODO Auto-generated method stub
                UpdateVersionUtils task = new UpdateVersionUtils(context,appName, R.drawable.my_setting_logo);
                LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>();
                ExecutorService exec = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, blockingQueue);
                task.executeOnExecutor(exec,downloadUrl,dir);
                sweetAlertDialog.dismiss();

            }
        }).setCancelText("取消").setConfirmText("更新").show();

    }
}
