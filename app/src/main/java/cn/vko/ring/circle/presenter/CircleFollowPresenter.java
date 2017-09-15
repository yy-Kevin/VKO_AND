package cn.vko.ring.circle.presenter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.adapter.FollowListAdapter;
import cn.vko.ring.circle.listener.ICompleteOperationListener;
import cn.vko.ring.circle.model.BaseGroupList;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.NoTouchCheckBox;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.StringUtils;

/**
 * Created by shikh on 2016/6/20.
 */
public class CircleFollowPresenter  implements AdapterView.OnItemClickListener{
    private  String FOLLOW_KEY;
    private XListView mListView;
    private TextView tvSelected;
    private FollowListAdapter mAdapter;

    private VolleyUtils<BaseGroupList> http;
    private VolleyUtils<BaseResultInfo> volley;
    private ICompleteOperationListener listener;
    private Context context;
    private View layoutFollow;
    public CircleFollowPresenter(Context ctx,View layoutFollow,ICompleteOperationListener listener){
        this.context = ctx;
        this.listener = listener;
        this.layoutFollow = layoutFollow;
        FOLLOW_KEY = VkoContext.getInstance(ctx).getUserId()+"FOLLOW";
        initView(layoutFollow);
    }

    private void initView(View contentView) {
        mListView = (XListView) contentView.findViewById(R.id.xlistview);
        tvSelected = (TextView) contentView.findViewById(R.id.tv_secected);

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);

        mAdapter = new FollowListAdapter(context);
//        mAdapter.setOnClickItemListener(this);
        setUpListener();
        // 造一些数据
        makeData();
        mListView.setAdapter(mAdapter);
    }
    private void makeData() {
        // TODO Auto-generated method stub
        http = new VolleyUtils<BaseGroupList>(context,BaseGroupList.class);
        Uri.Builder builder = http
                .getBuilder(ConstantUrl.VK_CIRCLE_INTEREST_LIST);
        builder.appendQueryParameter("token", VkoContext.getInstance(context).getToken());
        http.sendGETRequest(true,builder);
        http.setUiDataListener(new UIDataListener<BaseGroupList>() {
            @Override
            public void onDataChanged(BaseGroupList response) {
                if (response != null && response.getCode() == 0) {
                    mAdapter.add(response.getData());
                }
                mAdapter.judgeHasEmpty();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
                mAdapter.judgeHasEmpty();
                mAdapter.notifyDataSetChanged();
            }
        });

    }
    public void setUpListener() {
        // TODO Auto-generated method stub
        mListView.setOnItemClickListener(this);
        tvSelected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mAdapter.getGroupIds().size() > 0) {
                    String myStr = StringUtils.Join(",",mAdapter.getGroupIds());
                    joinGroups(myStr);
                }
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(mAdapter.judgeHasEmpty()){
            makeData();
        }else{
            GroupInfo info = mAdapter.getListItem(position-1);
            if(info.isChecked()){
                mAdapter.getGroupIds().remove(info.getId());
            }else{
                mAdapter.getGroupIds().add(info.getId());
            }
            info.setChecked(!info.isChecked());
            NoTouchCheckBox mCB = (NoTouchCheckBox) view.findViewById(R.id.cb_follow);
            mCB.setChecked(info.isChecked());
//            mAdapter.notifyDataSetChanged();
//            cb.setChecked(!cb.isChecked());
        }
    }

    protected void joinGroups(String groups) {
        // TODO Auto-generated method stub
        if (volley == null) {
            volley = new VolleyUtils<BaseResultInfo>(context,BaseResultInfo.class);
        }
        Uri.Builder builder = volley
                .getBuilder(ConstantUrl.VK_CIRCLE_JOIN_GROUP);
        builder.appendQueryParameter("groupIds", groups);
        builder.appendQueryParameter("userName", VkoContext.getInstance(context).getUser().getName());
        builder.appendQueryParameter("pId", "4");// 学习群
        builder.appendQueryParameter("token", VkoContext.getInstance(context).getToken());
        volley.sendGETRequest(true,builder);
        volley.setUiDataListener(new UIDataListener<BaseResultInfo>() {
            @Override
            public void onDataChanged(BaseResultInfo response) {
                if (response != null && response.getCode() == 0) {
                    if (response.isData()) {
                        VkoConfigure.getConfigure(context).put(FOLLOW_KEY, true);
                        if(listener != null){
                            listener.onComplte(true);
                        }
                    }
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {

            }
        });
    }

}
