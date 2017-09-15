package cn.vko.ring.circle.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.OnClick;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;

import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.circle.model.MemberInfo;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.DrawableCenterTextView;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.ImageUtils;

public class MemberListAdapter extends BaseListAdapter<MemberInfo> {

	private int index = -1;
	private LinearLayout tempLayout;
	private ImageView tempImage;
	private GroupInfo group;
	private String userId;
	private int roles = 4;// 本人角色
	private List<MemberInfo> managers = new ArrayList<MemberInfo>();

	public void setRole(int role) {
		this.roles = role;
	}

	public MemberListAdapter(Context ctx, GroupInfo group) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.group = group;
		userId = VkoContext.getInstance(ctx).getUserId();
	}

	@Override
	protected ViewHolder createViewHolder(View root) {
		// TODO Auto-generated method stub
		MyViewHolder holder = new MyViewHolder(root);
		return holder;
	}

	@Override
	protected void fillView(View root, MemberInfo item, ViewHolder holder,
			final int position) {
		// TODO Auto-generated method stub
		if (item == null)
			return;
		final MyViewHolder h = (MyViewHolder) holder;
		ImageUtils.loadPerviewImage(item.getHeadImg(), 200, 200,h.ivAvatar);
		h.tvUserName.setText(item.getName().trim());
		h.tvSchool.setText(item.getSchoolName().trim());
		h.setMemberInfo(item);
		h.cut.setVisibility(View.GONE);
		if (position > 0) {
			int lastr = getListItem(position - 1).getPrivilleges();
			int indexr = item.getPrivilleges();
			if ((lastr == 1 || lastr == 2) && (indexr == 3 || indexr == 4)) {
				h.cut.setVisibility(View.VISIBLE);
			} else {
				h.cut.setVisibility(View.GONE);
			}
		} else {
			h.cut.setVisibility(View.GONE);
		}

		if (roles == 1) {// 群主
			h.ivSwitch.setVisibility(View.VISIBLE);
			if (item.getPrivilleges() == 1) {
				h.ivSwitch.setVisibility(View.GONE);
			} else {
				h.ivSwitch.setVisibility(View.VISIBLE);
			}
		} else if (roles == 2) {// 管理员
			if (item.getPrivilleges() == 1 || item.getPrivilleges() == 2) {
				h.ivSwitch.setVisibility(View.GONE);
			} else {
				h.ivSwitch.setVisibility(View.VISIBLE);
			}
			h.tvManager.setVisibility(View.GONE);
		} else {
			h.ivSwitch.setVisibility(View.GONE);
		}

		String role = "", manager = "";
		if (item.getPrivilleges() == 1) {// 群主
			role = "群主";
			h.tvRole.setVisibility(View.VISIBLE);
			h.tvRole.setBackgroundResource(R.drawable.shape_yellow_bg);
		} else if (item.getPrivilleges() == 2) {// 管理员
			if (!managers.contains(item)) {
				managers.add(item);
			}
			role = "管理员";
			manager = "取消管理员";
			h.tvRole.setVisibility(View.VISIBLE);
			h.tvRole.setBackgroundResource(R.drawable.shape_green_btn);
		} else {
			h.tvRole.setVisibility(View.GONE);
			manager = "设为管理员";
		}
		h.tvRole.setText(role);
		h.tvManager.setText(manager);

		if (index == position) {
			h.layoutButtom.setVisibility(View.VISIBLE);
			h.ivSwitch.setImageResource(R.drawable.icon_arrow_unfold);
		} else {
			h.layoutButtom.setVisibility(View.GONE);
			h.ivSwitch.setImageResource(R.drawable.icon_arrow_close);
		}

		h.ivSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tempLayout != null) {
					if (tempLayout == h.layoutButtom) {
						tempLayout = null;
						index = -1;
						tempImage = null;
						h.layoutButtom.setVisibility(View.GONE);
						h.ivSwitch
								.setImageResource(R.drawable.icon_arrow_close);
					} else {
						tempLayout.setVisibility(View.GONE);
						tempImage.setImageResource(R.drawable.icon_arrow_close);
						h.layoutButtom.setVisibility(View.VISIBLE);
						tempLayout = h.layoutButtom;
						tempImage = h.ivSwitch;
						index = position;
						h.ivSwitch
								.setImageResource(R.drawable.icon_arrow_unfold);
					}
				} else {
					h.layoutButtom.setVisibility(View.VISIBLE);
					tempLayout = h.layoutButtom;
					tempImage = h.ivSwitch;
					index = position;
					h.ivSwitch.setImageResource(R.drawable.icon_arrow_unfold);
				}
			}
		});

	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_member_list;
	}

	class MyViewHolder extends ViewHolder {
		private MemberInfo member;
		private VolleyUtils<BaseResultInfo> volley;
		public void setMemberInfo(MemberInfo member){
			this.member = member;
		}
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.cut)
		public TextView cut;
		@Bind(R.id.tv_role)
		public TextView tvRole;
		@Bind(R.id.tv_user_name)
		public TextView tvUserName;
		@Bind(R.id.tv_school)
		public TextView tvSchool;
		@Bind(R.id.iv_member_avatar)
		public RoundAngleImageView ivAvatar;
		@Bind(R.id.iv_switch)
		public ImageView ivSwitch;

		@Bind(R.id.layout_buttom)
		public LinearLayout layoutButtom;
		@Bind(R.id.tv_chat)
		public DrawableCenterTextView tvChat;
		@Bind(R.id.tv_manager)
		public DrawableCenterTextView tvManager;
		@Bind(R.id.tv_del_member)
		public DrawableCenterTextView tvDel;

		@OnClick(R.id.tv_manager)
		public void sayManager() {
			if (roles == 1) {// 群主
				if (volley == null) {
					volley = new VolleyUtils<BaseResultInfo>(ctx,BaseResultInfo.class);
				}
				Map<String,String> params = new HashMap<String,String>();
				params.put("uId", member.getUserId());
				params.put("groupId", group.getId());
				params.put("token",VkoContext.getInstance(ctx).getToken());
				if (member.getPrivilleges() == 2) {// 取消管理员
					volley.sendPostRequest(true,ConstantUrl.VK_CIRCLE_CANCEL_MANAGER,params);
				} else {// 设为管理员
					if(managers.size() >=4){
						Toast.makeText(ctx, "最多设置5名管理员", Toast.LENGTH_LONG).show();
						return;
					}
				volley.sendPostRequest(true,ConstantUrl.VK_CIRCLE_SET_MANAGER,params);
				}
				volley.setUiDataListener(new UIDataListener<BaseResultInfo>() {
					@Override
					public void onDataChanged(BaseResultInfo response) {
						if (response != null && response.getCode() == 0) {
							if (response.isData()) {
								index = -1;
								if (member.getPrivilleges() == 2) {// 管理员最多5个人
									if (managers.contains(member)) {
										managers.remove(member);
									}
									member.setPrivilleges(3);
									switch2IndexItem(member,getCount() > 5 ? 5
											: getCount() - 1);
								} else {
									member.setPrivilleges(2);
									switch2IndexItem(member, 1);
									if (!managers.contains(member)) {
										managers.add(member);
									}
								}
								notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onErrorHappened(String errorCode, String errorMessage) {

					}
				});
			}else{
				Toast.makeText(ctx, "暂无此权限", Toast.LENGTH_LONG).show();
			}
		}

		@OnClick(R.id.tv_del_member)
		public void sayDelete() {
			if (roles == 3) {
				Toast.makeText(ctx, "权限不够", Toast.LENGTH_LONG).show();
				return;
			}
			if (roles == 2 && member.getPrivilleges() == 2) {
				Toast.makeText(ctx, "权限不够", Toast.LENGTH_LONG).show();
				return;
			}
			if (member.getUserId().equals(userId)) {
				Toast.makeText(ctx, "您暂时还不能删除自己", Toast.LENGTH_LONG).show();
				return;
			}

			if (volley == null) {
				volley = new VolleyUtils<BaseResultInfo>(ctx,BaseResultInfo.class);
			}
			Map<String,String> params = new HashMap<String,String>();
			params.put("groupId", group.getId());
			params.put("uId", member.getUserId());
			params.put("token", VkoContext.getInstance(ctx).getToken());
			volley.sendPostRequest(true,ConstantUrl.VK_CIRCLE_DELECT_MEMBER,params);
			volley.setUiDataListener(new UIDataListener<BaseResultInfo>() {
				@Override
				public void onDataChanged(BaseResultInfo response) {
					if (response != null && response.getCode() == 0) {
						if (response.isData()) {
							index = -1;
							remove(member);
							notifyDataSetChanged();
						}
					}
				}

				@Override
				public void onErrorHappened(String errorCode, String errorMessage) {

				}
			});
		}
	}

}
