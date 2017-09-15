package cn.vko.ring.circle.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import butterknife.Bind;
import butterknife.OnClick;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoApplication;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.event.IEventListener;
import cn.vko.ring.circle.model.BaseGroupInfo;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.circle.model.MemberInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyQueueController;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.ImageLinearLayout;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.ImageUtils;


public class GroupDetailActivity extends BaseActivity implements
		IEventListener<GroupInfo> {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.imagebtn)
	public ImageView ivSetting;

	@Bind(R.id.iv_group_avatar)
	public RoundAngleImageView ivGroupAvatar;
	@Bind(R.id.tv_group_name)
	public TextView tvGroupName;
	@Bind(R.id.tv_group_info)
	public TextView tvGroupInfo;
	@Bind(R.id.tv_group_no)
	public TextView tvGroupNO;
	@Bind(R.id.tv_group_intro)
	public TextView tvGroupIntro;
	@Bind(R.id.layout_members)
	public ImageLinearLayout imageMember;
	@Bind(R.id.tv_group_btn)
	public TextView tvBtn;	
	
	@Bind(R.id.group_detail_view)
	public ScrollView groupDetailView;
	@Bind(R.id.group_empty)
	public View emptyView;
	
	
	private GroupInfo group;
	private String groupId;

	private VolleyUtils<BaseGroupInfo> volley;
	
	private int flog;
	private VolleyUtils<BaseResultInfo> http;

	private ImageLoader.ImageListener listener;
	private ImageLoader imageLoader;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_group_detail;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventManager.register(this);
		EventBus.getDefault().register(this);
		tvTitle.setText(R.string.circle_group_detail);
		listener = ImageLoader.getImageListener(
				ivGroupAvatar, R.drawable.icon_group_default_avatar,
				R.drawable.icon_group_default_avatar);
		imageLoader = VolleyQueueController.getInstance().getImageLoader();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		groupId = getIntent().getExtras().getString("GROUPID");
		getGroupDetail();
	}

	private void getGroupDetail() {
		// TODO Auto-generated method stub
		if (volley == null) {
			volley = new VolleyUtils<BaseGroupInfo>(this,BaseGroupInfo.class);
		}
		Uri.Builder builder = volley
				.getBuilder(ConstantUrl.VK_CIRCLE_GROUP_DETAIL);
		builder.appendQueryParameter("groupId", groupId);
//		builder.appendQueryParameter("userId", VkoContext.getInstance(this).getUserId());
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getToken());
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(new UIDataListener<BaseGroupInfo>() {
			@Override
			public void onDataChanged(BaseGroupInfo response) {
				if (response != null && response.getCode() == 0) {
					group = response.getData();
					if(group != null){
						initDetailView();
					}
				}
				switchView(group == null);
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				switchView(true);
			}
		});
	}

	protected void switchView(boolean isEmpty) {
		// TODO Auto-generated method stub
		if(isEmpty){
			groupDetailView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}else{
			groupDetailView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		}
	}

	protected void initDetailView() {
		// TODO Auto-generated method stub
		initGroupInfo(group);
		String btn = "我要加入群";
		if (group.getCrId().equals(VkoContext.getInstance(this).getUserId())) {// 群主
			ivSetting.setVisibility(View.VISIBLE);
			ivSetting.setImageResource(R.drawable.icon_circle_setting);
			btn = "我要解散群";
			flog = 2;
			tvBtn.setBackgroundResource(R.drawable.selector_red_button);
		} else if (group.getFlag() == 1) {
			if(group.getParentType().equals("4")){
				btn = "取消关注";
			}else{
				btn = "我要退出群";
			}
			flog = 1;
			tvBtn.setBackgroundResource(R.drawable.selector_red_button);
		} else if(group.getFlag() == 2){
			tvBtn.setBackgroundResource(R.drawable.shape_gray_btn);
			btn = "已申请加群";
		}else{
			if(group.getParentType().equals("4")){
				btn = "关注";
			}else{
				btn = "我要加入群";
			}
		}
		tvBtn.setText(btn);
	}

	private void initGroupInfo(GroupInfo group) {
		// TODO Auto-generated method stub
//		ImageUtils.loadPerviewImage(group.getHeadImg(), 80, 80, ivGroupAvatar);


		imageLoader.get(group.getHeadImg(), listener, 100, 100);
		tvGroupName.setText(group.getName());
		String type = "";
		if (group.getParentType().equals("2")) {
			type = "班级";
		} else if (group.getParentType().equals("3")) {
			type = group.getTypeName();
		} else if (group.getParentType().equals("4")) {
			type = "学习";
		}
		tvGroupInfo.setText("成员:" + group.getUserCnt() + "    分类:" + type);
		tvGroupNO.setText(group.getId());
		tvGroupIntro.setText(group.getIntroduction());
		initMemberView(group.getGroupUsers());
	}

	protected void initMemberView(List<MemberInfo> members) {
		// TODO Auto-generated method stub
		imageMember.cleanView();
		int total = members.size() > 4 ? 4 : members.size();
		for (int i = 0; i < total; i++) {
			imageMember.addImageView(members.get(i).getHeadImg(),false,R.drawable.icon_member_default_avatar);
		}
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}
	
	@OnClick(R.id.group_empty)
	void reStartLoad() {
		getGroupDetail();
	}
	
	@OnClick(R.id.imagebtn)
	void saySetGroupInfo() {
		if(group == null) return;
		Bundle bundle = new Bundle();
		bundle.putSerializable("GROUP", group);
		StartActivityUtil.startActivity(this, SettingGroupInfoActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}

	@OnClick(R.id.layout_group_code)
	void sayGroupCode() {
		if(group == null) return;
		Bundle bundle = new Bundle();
		if(group.getParentType().equals("3")){//兴趣圈
			String code = "http://m.vko.cn/group/crowdHome.html?groupId="+groupId;
			bundle.putString("CODE", code);
			StartActivityUtil.startActivity(this, Group2VCodeActivity.class, bundle,
					Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}else if(group.getParentType().equals("4")){//学习圈
			String code = "http://m.vko.cn/group/sCircleTopics.html?groupId="+groupId;
			bundle.putString("CODE", code);
			StartActivityUtil.startActivity(this, Group2VCodeActivity.class, bundle,
					Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}
	}

	@OnClick(R.id.layout_group_intro)
	void sayGroupIntro() {
		if(group == null) return;
		Bundle bundle = new Bundle();
		bundle.putSerializable("GROUP", group);
		bundle.putInt("TYPE", 0);
		StartActivityUtil.startActivity(this, GroupIntroActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}

	@OnClick(R.id.layout_group_member)
	void sayGroupMember() {
		if(group != null){
			Bundle bundle = new Bundle();
			bundle.putSerializable("GROUP", group);
			StartActivityUtil.startActivity(this, GroupMembersActivity.class, bundle,
					Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}

	}

	@OnClick(R.id.tv_group_btn)
	void sayGroupBtn() {
		if(group == null) return;
		if (flog == 2) {// 解散群
			deleteGroup();
		} else if (flog == 1) {// 退群
			quitGroup();
		} else if(group.getFlag() == 0){// 加群
			if(!VkoContext.getInstance(this).doLoginCheckToSkip(this)){
				if(group.getParentType().equals("3")){//兴趣圈
					Bundle bundle = new Bundle();
					bundle.putSerializable("GROUP", group);
					StartActivityUtil.startForResult(this, ApplyJoinActivity.class, bundle, 100);
				}else if(group.getParentType().equals("4")){//学习圈
					joinGroup();
				}
			}
		}

	}
	private void joinGroup() {
		// TODO Auto-generated method stub
		if(group == null)return;
		if (http == null) {
			http = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		}
		Uri.Builder builder = http.getBuilder(ConstantUrl.VK_CIRCLE_JOIN_GROUP);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getToken());
		builder.appendQueryParameter("groupIds", group.getId());
		builder.appendQueryParameter("userName", VkoContext.getInstance(this)
				.getUser().getName());
		builder.appendQueryParameter("pId", group.getParentType() + "");
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getToken());
		http.sendGETRequest(true,builder);
		http.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if (response != null && response.getCode() == 0) {
					if (response.isData()) {
						if(group.getParentType().equals("4")){
							group.setFlag(1);
						}else{
							group.setFlag(2);
						}
						EventManager.fire(new Event<GroupInfo>(group,Event.JOIN_EVENT));
						finish();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}
	
	private void quitGroup() {
		// TODO Auto-generated method stub
		if (http == null) {
			http = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		}
		Uri.Builder builder = http
				.getBuilder(ConstantUrl.VK_CIRCLE_QUIT_GROUP);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getToken());
		builder.appendQueryParameter("groupId", group.getId());
		http.sendGETRequest(true,builder);
		http.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if (response != null && response.getCode() == 0) {
					if (response.isData()) {
						EventManager.fire(new Event<GroupInfo>(group,
								Event.QUIT_EVENT));
						finish();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	private void deleteGroup() {
		// TODO Auto-generated method stub
		if (http == null) {
			http = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		}

		Map<String,String> params = new HashMap<String,String>();
//		builder.appendQueryParameter("groupId", );
		params.put("token", VkoContext.getInstance(this)
				.getToken());
		http.sendPostRequest(true,ConstantUrl.VK_CIRCLE_DELETE_GROUP+"/"+group.getId(),params);
		http.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if (response != null && response.getCode() == 0) {
					if (response.isData()) {
						EventManager.fire(new Event<GroupInfo>(group,
								Event.DETEIL_EVENT));
						finish();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Override
	public void event(Event<GroupInfo> event) {
		// TODO Auto-generated method stub
		GroupInfo info = event.getEvent();
		if (info != null) {
			group = info;
			initGroupInfo(info);
		}
	}

	@Override
	public int getFilter() {
		// TODO Auto-generated method stub
		return Event.UPDATE_EVENT;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventManager.unRegister(this);
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 100) {
			
			if(group.getParentType().equals("4")){
				group.setFlag(1);
				tvBtn.setBackgroundResource(R.drawable.selector_red_button);
				tvBtn.setText("取消关注");
			}else{
				group.setFlag(2);
				tvBtn.setText("已申请加群");
				tvBtn.setBackgroundResource(R.drawable.shape_gray_btn);
			}
		}
	}
	@Subscribe
	public void onEventMainThread(String msg) {

		if (!TextUtils.isEmpty(msg)) {
			// 用户登录时刷新数据
			if (msg.equals(Constants.LOGIN_REFRESH)) {
				getGroupDetail();
			}
		}
	}

}
