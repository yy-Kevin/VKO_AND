package cn.vko.ring.circle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.circle.model.GroupClassInfo;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.widget.NoTouchCheckBox;

public class GroupClassListAdapter extends BaseListAdapter<GroupClassInfo> {

	private GroupClassInfo info;


	public GroupClassInfo getInfo() {
		return info;
	}

	public void setInfo(GroupClassInfo info) {
		this.info = info;
	}

	public GroupClassListAdapter(Context ctx) {
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
	protected void fillView(View root, GroupClassInfo item, ViewHolder holder,
			final int position) {
		// TODO Auto-generated method stub
		if (item == null)return;
		final MyViewHolder h = (MyViewHolder) holder;
		h.tvName.setText(item.getName());
		if(info != null && item.equals(info)){
			h.mCB.setChecked(true);
		}else{
			h.mCB.setChecked(false);
		}
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_group_class;
	}

	class MyViewHolder extends ViewHolder {
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.tv_name)
		public TextView tvName;

		@Bind(R.id.cb)
		public NoTouchCheckBox mCB;
	}

}
