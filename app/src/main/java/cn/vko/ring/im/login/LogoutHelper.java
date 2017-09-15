package cn.vko.ring.im.login;


import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.im.utils.DemoCache;


/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();

        DemoCache.clear();
        Preferences.saveUserAccount(null);
        Preferences.saveUserToken(null);
        LoginSyncDataStatusObserver.getInstance().reset();
        SessionHelper.reset();
        NIMClient.getService(AuthService.class).logout();
    }
}
