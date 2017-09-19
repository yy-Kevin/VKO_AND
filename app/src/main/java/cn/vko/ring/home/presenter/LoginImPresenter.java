package cn.vko.ring.home.presenter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.greenrobot.eventbus.EventBus;

import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.config.preference.UserPreferences;
import cn.vko.ring.im.utils.DemoCache;

/**
 * desc:
 * Created by jiarh on 16/5/19 10:12.
 */
public class LoginImPresenter {
    private Context mContext;
    private AbortableFuture<LoginInfo> loginRequest;

    private static final String TAG = "LoginImPresenter";

    private static LoginImPresenter instance;
    private LoginImPresenter(){}
    public static LoginImPresenter getInstance(Context context){
        if (instance==null){
            synchronized (LoginImPresenter.class){
                if (instance==null){
                    instance = new LoginImPresenter();
                }
            }
        }
        instance.mContext= context;
        return instance;
    }

    public void logInWYIm() {
        UserInfo userInfo = VkoContext.getInstance(mContext).getUser();
        if(!TextUtils.isEmpty(userInfo.getImToken())){
            loginIm(userInfo.getUserId(),userInfo.getImToken());
        }

    }
    /**
     * 登录云信
     */
    private void loginIm(String accounts,String tokens) {
        Log.d(TAG, "loginIm() called with: " + "accounts = [" + accounts + "], tokens = [" + tokens + "]");
        Log.e(TAG, "loginIm() called with: " + "accounts = [" + accounts + "], tokens = [" + tokens + "]");

//		DialogMaker.showProgressDialog(this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				if (loginRequest != null) {
//					loginRequest.abort();
//					onLoginDone();
//				}
//			}
//		}).setCanceledOnTouchOutside(false);

        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。

        final	String account = accounts;
        final	String token = tokenFromPassword(tokens);
        // 登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success");
                onLoginDone();
                DemoCache.setAccount(account);
                saveLoginInfo(account, token);
                EventBus.getDefault().post("YXLOGINSUCCESS");
                // 初始化消息提醒
                NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                // 初始化免打扰
                if (UserPreferences.getStatusConfig() == null) {
                    UserPreferences.setStatusConfig(DemoCache.getNotificationConfig());
                }
                NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());

                // 构建缓存
                DataCacheManager.buildDataCacheAsync();

                // 进入主界面
//				cn.vko.ring.im.main.activity.MainActivity.start(LoginActivity.this, null);
//                finish();
            }

            @Override
            public void onFailed(int code) {
                LogUtil.e("Auth", "onFailed  user password error");
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(mContext, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                onLoginDone();
            }
        });
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(mContext);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return password;
    }
}
