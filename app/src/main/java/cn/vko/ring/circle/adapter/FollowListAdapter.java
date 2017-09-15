package cn.vko.ring.circle.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.widget.NoTouchCheckBox;
import cn.vko.ring.common.widget.RoundAngleImageView;

public class FollowListAdapter extends BaseListAdapter<GroupInfo> {

	private IOnClickItemListener<GroupInfo> listener;
	public void setOnClickItemListener(IOnClickItemListener<GroupInfo> listener){
		this.listener = listener;
	}
	public List<String> groupIds = new ArrayList<String>();

	public List<String> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	public FollowListAdapter(Context ctx) {
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
	protected void fillView(View root, final GroupInfo item,ViewHolder holder,final int position) {
		// TODO Auto-generated method stub
		if(item == null) return ;
		final MyViewHolder h = (MyViewHolder) holder;
		h.tvInfo.setText(item.getIntroduction());
		h.tvName.setText(item.getName());

		h.mCB.setChecked(item.isChecked());

		loadImageView(h.ivCover, item.getHeadImg(), R.drawable.icon_group_default_avatar, 100, 100);
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_circle_follow;
	}
	
	class MyViewHolder extends ViewHolder{
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub

		}
		@Bind(R.id.iv_group_cover)
		public RoundAngleImageView ivCover;
		@Bind(R.id.tv_name)
		public TextView tvName;
		@Bind(R.id.tv_info)
		public TextView tvInfo;
		@Bind(R.id.cb_follow)
		public NoTouchCheckBox mCB;
		@Bind(R.id.layout_item)
		public LinearLayout layoutItem;


	}
	

}
