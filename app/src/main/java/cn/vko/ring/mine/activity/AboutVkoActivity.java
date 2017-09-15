package cn.vko.ring.mine.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.AnalyticsConfig;

import java.util.List;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.umeng.EventCountAction;
import cn.vko.ring.home.presenter.VersionUpdatePresenter;

public class AboutVkoActivity extends BaseActivity implements OnClickListener {

    private ImageView ivLeft;
    private TextView tvTitle;
    private Button butUpdate;
    private TextView tvVersion;
    private int pro;
    private Context context;
    /**
     * 下载apk对应的url
     */
    private String apkUrl;

    @Override
    public int setContentViewResId() {
        return R.layout.activity_about_vko;
    }

    @Override
    public void initView() {
        ivLeft = (ImageView) this.findViewById(R.id.iv_back);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
        butUpdate = (Button) this.findViewById(R.id.but_update);
        tvVersion = (TextView) this.findViewById(R.id.tv_version);
        ivLeft.setOnClickListener(this);
        butUpdate.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tvTitle.setText("关于微课");
        tvVersion.setText(AnalyticsConfig.getChannel(this) + "_v" + this.getVersionName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.but_update: // 检查更新
//                String packageName="cn.vko.ring";
//                goToMarket(context,packageName);
			new VersionUpdatePresenter(this,false);
                break;

        }
    }

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            goToMarket.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    // 获得版本号
    public String getVersionName() {
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        EventCountAction.onActivityResumCount(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        EventCountAction.onActivityPauseCount(this);
    }


}
