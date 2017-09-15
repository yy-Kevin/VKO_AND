package cn.vko.ring.classgroup.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.model.UserGroupTeamData;
import cn.vko.ring.classgroup.view.UserTeamView;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;

/**
 * desc:
 * Created by jiarh on 16/5/18 18:05.
 */
public class GetUserGroupTeamPresenterImp implements GetUserGroupTeamPresenter{
    private UserTeamView userTeamView;
    private Context mContext;
    private VolleyUtils<UserGroupTeamData> volleyUtils;

    public GetUserGroupTeamPresenterImp(Context mContext, UserTeamView userTeamView) {
        this.mContext = mContext;
        this.userTeamView = userTeamView;
    }

    @Override
    public void getUserTeam() {
//        Log.e("====getUserTeam","走这里了");
        Map<String,String> params = new HashMap<String,String>();
        if(volleyUtils == null){
            volleyUtils = new VolleyUtils<UserGroupTeamData>(mContext,UserGroupTeamData.class);
        }
        Uri.Builder b = volleyUtils.getBuilder(ConstantUrl.VK_GETUSERTEAM);
        b.appendQueryParameter("token", VkoContext.getInstance(mContext).getToken());

        volleyUtils.setUiDataListener(new UIDataListener<UserGroupTeamData>() {
            @Override
            public void onDataChanged(UserGroupTeamData data) {

                if (data!=null){
                    userTeamView.getUserTeam(data);
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
                userTeamView.getUserTeam(null);

            }
        });
        volleyUtils.sendGETRequest(false,b);
    }
}
