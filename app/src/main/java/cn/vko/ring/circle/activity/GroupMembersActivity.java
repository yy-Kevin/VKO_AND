package cn.vko.ring.circle.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.android.volley.VolleyError;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.DateUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.circle.adapter.MemberListAdapter;
import cn.vko.ring.circle.model.BaseMemberInfo;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.circle.model.MemberInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;

public class GroupMembersActivity extends BaseActivity implements
		OnItemClickListener, IXListViewListener,UIDataListener<BaseMemberInfo>{

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.xlistview)
	public XListView mListView;

	private MemberListAdapter mAdapter;
	private List<MemberInfo> members;
	private GroupInfo group;
	private VolleyUtils<BaseMemberInfo> volley;
	private int page = 1;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_group_members;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("群成员");
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		group = (GroupInfo) getIntent().getExtras().get("GROUP");
		if (group == null)
			return;
		mAdapter = new MemberListAdapter(this,group);
		mAdapter.setRole(group.getRole());
		mListView.setAdapter(mAdapter);
		getMemberListTask(group.getId(), 1, true);
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}

	private void getMemberListTask(String groupId, int p, boolean isLoading) {
		// TODO Auto-generated method stub
		if (volley == null) {
			volley = new VolleyUtils<BaseMemberInfo>(this,BaseMemberInfo.class);
		}
		Uri.Builder builder = volley
				.getBuilder(ConstantUrl.VK_CIRCLE_SEARCH_MEMBER);
		builder.appendQueryParameter("groupId", groupId);
		builder.appendQueryParameter("pageIndex", p + "");
		builder.appendQueryParameter("pageSize", "10");
		volley.sendGETRequest(isLoading,builder);
		volley.setUiDataListener(this);
	}

	protected void initListView() {
		// TODO Auto-generated method stub
		if (members != null && members.size() > 0) {
			Collections.sort(members, new Comparator<MemberInfo>() {

				@Override
				public int compare(MemberInfo lhs, MemberInfo rhs) {
					// TODO Auto-generated method stub
					if (rhs.getPrivilleges() > lhs.getPrivilleges()) {
						return 1;
					} else {
						return 0;
					}
				}
			});

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (mAdapter.judgeHasEmpty()) {
			getMemberListTask(group.getId(), 1, true);
		}
	}

	public void stopUp() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		mListView.setRefreshTime(DateUtils.getCurDateStr());
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getMemberListTask(group.getId(), 1, false);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getMemberListTask(group.getId(), page + 1, false);
	}


	@Override
	public void onDataChanged(BaseMemberInfo response) {
		if (response != null && response.getCode() == 0) {
			if (response.getData() != null) {
				page = response.getData().getPager().getPageNo();
				if (page == 1) {
					mAdapter.clear();
				}
				if (page >= response.getData().getPager().getPageTotal()) {
					mListView.setPullLoadEnable(false);
				} else {
					mListView.setPullLoadEnable(true);
				}
				members = response.getData().getData();
				mAdapter.add(members);
			}
			// initListView();
			mAdapter.postNotifyDataSetChanged();
			return;
		}
		mAdapter.postNotifyDataSetChanged();
		stopUp();

	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		mAdapter.postNotifyDataSetChanged();
		stopUp();
	}
}
