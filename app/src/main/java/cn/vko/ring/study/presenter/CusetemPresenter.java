package cn.vko.ring.study.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.home.presenter.LoginImPresenter;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.study.model.StringRequestInfo;

/**
 * Created by shikh on 2016/5/27.
 */
public class CusetemPresenter {

    public CusetemPresenter(final Context context){
        VolleyUtils<StringRequestInfo> volleyUtils = new VolleyUtils<>(context,StringRequestInfo.class);
        Uri.Builder builder = volleyUtils.getBuilder(ConstantUrl.VK_CUSETEM_URL);
        builder.appendQueryParameter("token", VkoContext.getInstance(context).getToken());
        volleyUtils.sendGETRequest(true,builder);
        volleyUtils.setUiDataListener(new UIDataListener<StringRequestInfo>() {
            @Override
            public void onDataChanged(StringRequestInfo data) {
                if(data != null && data.getData() != null){
                    UserInfo info = VkoContext.getInstance(context).getUser();
                    info.setImToken(data.getData());
                    VkoContext.getInstance(context).setUser(info);
                    LoginImPresenter.getInstance(context).logInWYIm();
//                    SessionHelper.startP2PSession(context,"13886257916838578");
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {

            }
        });
    }
}
