/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-13 
 * 
 *******************************************************************************/
package cn.vko.ring.mine.fragment;

import java.util.List;

import android.content.Intent;
import android.net.Uri.Builder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import butterknife.Bind;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.EventCountAction;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.activity.ErrNotepadDetailsActivity;
import cn.vko.ring.mine.adapter.NotepadListAdapter;
import cn.vko.ring.mine.model.NotedInfo;
import cn.vko.ring.mine.model.NotedInfo.Data;

import com.android.volley.VolleyError;

/**
 * 错题本 综合课
 * 
 * @author Administrator
 *
 */
public class AllClassFragment extends BaseFragment implements
		OnItemClickListener {
	@Bind(R.id.refresh_lv)
	public XListView xListView;
	@Bind(R.id.empty)
	View emptyView;
	private NotepadListAdapter apapter;
	private VolleyUtils<NotedInfo> volleyUtils;
	private String subjectId;
	private String courseType = "43";

	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_xlistview_err_notepad;
	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		apapter = new NotepadListAdapter(getActivity(), 2);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		xListView.setOnItemClickListener(this);
		// apapter.judgeHasEmpty();
		xListView.setAdapter(apapter);
		volleyUtils = new VolleyUtils<NotedInfo>(getActivity(),NotedInfo.class);
		getChineseTask(subjectId, courseType);
	}

	// 选中科目
	private void getChineseTask(String subjectId, String courseType) {
		String url = ConstantUrl.VKOIP + "/user/newErrBook";
		Builder builder = volleyUtils.getBuilder(url);
		UserInfo user = VkoContext.getInstance(getActivity()).getUser();
		if (user == null) {
			return;
		}
		if (subjectId != null) {
			builder.appendQueryParameter("courseType", "43");
			builder.appendQueryParameter("subjectId", subjectId);
			builder.appendQueryParameter("learnId", user.getLearn());
			builder.appendQueryParameter("token", user.getToken());
			volleyUtils.sendGETRequest(false,builder);
			volleyUtils.setUiDataListener(new UIDataListener<NotedInfo>() {
				@Override
				public void onDataChanged(NotedInfo response) {
					if (response != null && response.code == 0) {

						if (response.data != null
								&& response.data.size() > 0) {
							fillAdapter(response.data);
							emptyView.setVisibility(View.GONE);
						} else {
							apapter.clear();
							apapter.notifyDataSetChanged();
							emptyView.setVisibility(View.VISIBLE);
						}
					}
				}

				@Override
				public void onErrorHappened(String errorCode, String errorMessage) {

				}
			});

		} else {
			Toast.makeText(getActivity(), "请选择学科", Toast.LENGTH_SHORT).show();
		}
	}

	private void fillAdapter(List<Data> noteList) {
		apapter.clear();
		apapter.add(noteList);
		apapter.judgeHasEmpty();
		apapter.notifyDataSetChanged();
	}

	public void updateSubject(String subjectId) {
		if (this.subjectId == null || !this.subjectId.equals(subjectId)) {
			if (volleyUtils != null) {

				getChineseTask(subjectId, courseType);
			}
			this.subjectId = subjectId;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (apapter.judgeHasEmpty()) {
			getChineseTask(subjectId, courseType);
			return;
		}
		Data t = apapter.getListItem(arg2 - 1);
		Intent intent = new Intent(getActivity(),
				ErrNotepadDetailsActivity.class);
		intent.putExtra("subjectData", t);
		intent.putExtra("courseType", courseType);
		startActivity(intent);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		EventCountAction.onFragPCount(AllClassFragment.class);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EventCountAction.onFragRCount(AllClassFragment.class);
	}

	// @Override.2
	// public Class getFragmentClass() {
	//
	// // TODO Auto-generated method stub
	//
	// return AllClassFragment.class;
	// }

}
