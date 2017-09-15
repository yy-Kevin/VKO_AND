package cn.vko.ring.circle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseListAdapter;

public class GroupListAdapter extends BaseListAdapter<GroupInfo> {

	public GroupListAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BaseListAdapter.ViewHolder createViewHolder(View root) {
		// TODO Auto-generated method stub
		MyViewHolder holder = new MyViewHolder(root);
		return holder;
	}

	@Override
	protected void fillView(View root, GroupInfo item, ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if(item == null) return;
		final MyViewHolder h = (MyViewHolder) holder;
		h.tvName.setText(item.getName());
		h.tvNum.setText(item.getUserCnt()+"");
		if(item.getParentType().equals("2")){
			h.ivType.setImageResource(R.drawable.icon_class_type);
		}else if(item.getParentType().equals("3")){
			h.ivType.setImageResource(R.drawable.icon_interest_type);
		}else if(item.getParentType().equals("4")){
			h.ivType.setImageResource(R.drawable.icon_study_type);
		}
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_group_list;
	}
	
	
	class MyViewHolder extends ViewHolder{
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.iv_cover)
		public ImageView ivCover;
		@Bind(R.id.tv_group_name)
		public TextView tvName;
		@Bind(R.id.tv_num)
		public TextView tvNum;
		@Bind(R.id.iv_type)
		public ImageView ivType;
	}

}
