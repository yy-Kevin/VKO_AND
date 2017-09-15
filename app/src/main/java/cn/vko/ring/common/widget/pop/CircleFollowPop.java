package cn.vko.ring.common.widget.pop;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.adapter.FollowListAdapter;
import cn.vko.ring.circle.listener.ICompleteOperationListener;
import cn.vko.ring.circle.model.BaseGroupList;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BasePopuWindow;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.StringUtils;

public class CircleFollowPop extends BasePopuWindow implements IOnClickItemListener<GroupInfo> {
	private  String FOLLOW_KEY;
	private XListView mListView;
	private TextView tvSelected;
	private FollowListAdapter mAdapter;

	private VolleyUtils<BaseGroupList> http;
	private VolleyUtils<BaseResultInfo> volley;
	private ICompleteOperationListener listener;
	public CircleFollowPop(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		FOLLOW_KEY = VkoContext.getInstance(context).getUserId()+"FOLLOW";
	}
	
	public void setOnCompleteListener(ICompleteOperationListener listener) {
		this.listener = listener;
	}

	@Override
	public void setUpViews(View contentView) {
		// TODO Auto-generated method stub
		mListView = (XListView) contentView.findViewById(R.id.xlistview);
		tvSelected = (TextView) contentView.findViewById(R.id.tv_secected);

		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);

		mAdapter = new FollowListAdapter(context);
		mAdapter.setOnClickItemListener(this);
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

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
//		mListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
				// TODO Auto-generated method stub
//				if(mAdapter.judgeHasEmpty()){
//					makeData();
//				}else{
//					NoTouchCheckBox cb = (NoTouchCheckBox) view.findViewById(R.id.cb_follow);
//					GroupInfo info = mAdapter.getListItem(position-1);
//					if(cb.isChecked()){
//						mAdapter.groupIds.remove(info.getId());
//					}else{
//						mAdapter.groupIds.add(info.getId());
//					}
//					cb.setChecked(!cb.isChecked());
//				}
//			}
//		});

		tvSelected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mAdapter.groupIds.size() > 0) {
					String myStr = StringUtils.Join(",",mAdapter.groupIds);
					joinGroups(myStr);
				}
			}
		});
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
						dismiss();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Override
	public int getAnimationStyle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResView() {
		// TODO Auto-generated method stub
		return R.layout.pop_circle_follow;
	}

	@Override
	public boolean updateView(View contentView) {
		// TODO Auto-generated method stub
		setFocusable(true);
//		setOutsideTouchable(false);
		return true;
	}

	@Override
	public void onItemClick(int position, GroupInfo t, View v) {
		// TODO Auto-generated method stub
		makeData();
	}

}
