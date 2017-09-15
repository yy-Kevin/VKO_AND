package cn.vko.ring.circle.adapter;

import com.android.volley.VolleyError;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.Bind;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.activity.ApplyJoinActivity;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.mine.model.BaseResultInfo;

public class SearchGroupListAdapter extends BaseListAdapter<GroupInfo> {

	public SearchGroupListAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ViewHolder createViewHolder(View root) {
		// TODO Auto-generated method stub
		MyViewHolder holder = new MyViewHolder(root);
		return holder;
	}

	@Override
	protected void fillView(View root, final GroupInfo item, ViewHolder holder,
			int position) {
		// TODO Auto-generated method stub
		if (item == null)
			return;
		final MyViewHolder h = (MyViewHolder) holder;
		h.tvName.setText(item.getName());
		h.tvNum.setText(item.getUserCnt() + "");
		h.tvIntro.setText(item.getIntroduction());
		if (item.getFlag() == 0) {// 是否加入该群
			if(item.getParentType().equals("4")){
				h.tvJoin.setText("关注");
			}else{
				h.tvJoin.setText("加入");
			}
			h.tvJoin.setBackgroundResource(R.drawable.login_button_selector);
			h.tvJoin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!VkoContext.getInstance(ctx).doLoginCheckToSkip(ctx)){
						if(item.getParentType().equals("4")){
							h.joinGroup(item);
						}else{
							Bundle bundle = new Bundle();
							bundle.putSerializable("GROUP", item);
							StartActivityUtil.startActivity(ctx, ApplyJoinActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
						}
					}
				}
			});
		} else if(item.getFlag() == 1){// 已加入
			if(item.getParentType().equals("4")){
				h.tvJoin.setText("已关注");
			}else{
				h.tvJoin.setText("已加入");
			}
			h.tvJoin.setBackgroundResource(R.drawable.shape_gray_btn);
		}else{
			h.tvJoin.setText("已申请");
			h.tvJoin.setBackgroundResource(R.drawable.shape_gray_btn);
		}
		loadImageView(h.ivCover, item.getHeadImg(),
				R.drawable.icon_group_default_avatar, 100, 100);
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_search_group_list;
	}

	class MyViewHolder extends ViewHolder {
		private VolleyUtils<BaseResultInfo> volley;

		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.iv_cover)
		public RoundAngleImageView ivCover;
		@Bind(R.id.tv_group_name)
		public TextView tvName;
		@Bind(R.id.tv_num)
		public TextView tvNum;
		@Bind(R.id.tv_intro)
		public TextView tvIntro;
		@Bind(R.id.tv_join)
		public TextView tvJoin;

		protected void joinGroup(final GroupInfo item) {
			// TODO Auto-generated method stub
			if (volley == null) {
				volley = new VolleyUtils<BaseResultInfo>(ctx,BaseResultInfo.class);
			}
			Uri.Builder builder = volley
					.getBuilder(ConstantUrl.VK_CIRCLE_JOIN_GROUP);
			builder.appendQueryParameter("token", VkoContext.getInstance(ctx)
					.getToken());
			builder.appendQueryParameter("groupIds", item.getId());
			builder.appendQueryParameter("userName", VkoContext
					.getInstance(ctx).getUser().getName());
			builder.appendQueryParameter("pId", item.getParentType());
			builder.appendQueryParameter("token", VkoContext.getInstance(ctx).getToken());
			volley.sendGETRequest(true,builder);
			volley.setUiDataListener(new UIDataListener<BaseResultInfo>() {
				@Override
				public void onDataChanged(BaseResultInfo response) {
					if (response != null && response.getCode() == 0) {
						if (response.isData()) {
							if(item.getParentType().equals("4")){//学习群
								tvJoin.setText("已关注");
								item.setFlag(1);
								EventManager.fire(new Event<GroupInfo>(item, Event.JOIN_EVENT));
							}else{
								tvJoin.setText("已申请");
								item.setFlag(2);
							}
							tvJoin.setBackgroundResource(R.drawable.shape_gray_btn);

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
