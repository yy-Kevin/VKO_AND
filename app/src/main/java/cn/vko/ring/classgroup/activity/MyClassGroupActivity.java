package cn.vko.ring.classgroup.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.adapter.UserTeamAdapter;
import cn.vko.ring.classgroup.model.UserGroupTeamData;
import cn.vko.ring.classgroup.presenter.GetUserGroupTeamPresenterImp;
import cn.vko.ring.classgroup.view.UserTeamView;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.im.utils.DemoCache;
import cn.vko.ring.study.presenter.CusetemPresenter;

public class MyClassGroupActivity extends BaseActivity implements UserTeamView {

    @Bind(R.id.list_class_group)
    ListView myClassGroup;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tvTitle;


    private UserTeamAdapter userTeamAdapter;
    private List<UserGroupTeamData.DataEntity> teamData;
    private GetUserGroupTeamPresenterImp getUserGroupTeamPresenterImp;
    private Context mContext;

    @Override
    public int setContentViewResId() {
        return R.layout.activity_my_class_group;
    }

    @Override
    public void initView() {
        tvTitle.setText("我的班群");
        getUserTeem();
        teamData = new ArrayList<>();
        myClassGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (teamData.size() > position)
                    checkedImToken();
                SessionHelper.startTeamSession(MyClassGroupActivity.this, teamData.get(position).getTid() + "");
            }
        });

    }

    private void getUserTeem() {
        if (getUserGroupTeamPresenterImp == null) {
            getUserGroupTeamPresenterImp = new GetUserGroupTeamPresenterImp(MyClassGroupActivity.this, this);
        }else {
            getUserGroupTeamPresenterImp.getUserTeam();
        }
    }

    private void checkedImToken() {
        if(VkoContext.getInstance(this).getUser().getImToken() != null){
            if(DemoCache.getAccount() == null){
                if(getLoginInfo() == null){
                    new CusetemPresenter(this);
                }
            }
        }else{
            new CusetemPresenter(this);
        }
    }

    public LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());

            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    @Override
    public void initData() {
        getUserTeem();
        initIM();
    }

    @Override
    public void getUserTeam(UserGroupTeamData userGroupTeamData) {

        Log.e("TAG", "getUserTeam() called with: " + "userGroupTeamData = [" + userGroupTeamData + "]");
        if (userGroupTeamData != null && userGroupTeamData.getData() != null) {

            teamData.addAll(userGroupTeamData.getData());
            userTeamAdapter = new UserTeamAdapter(this);
            userTeamAdapter.setList(userGroupTeamData.getData());
            myClassGroup.setAdapter(userTeamAdapter);
            userTeamAdapter.notifyDataSetChanged();
        } else {
//            i--;
//            if(i>=0)
//            getUserTeem();
        }

    }

    private void initIM() {
        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {
                DialogMaker.dismissProgressDialog();
            }
        });

//		Log.i(TAG, "sync completed = " + syncCompleted);
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        }

    }

    @OnClick(R.id.iv_back)
    void sayBack() {
        finish();
    }

}
