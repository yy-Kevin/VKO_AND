/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-9-11 
 * 
 *******************************************************************************/ 
package cn.vko.ring.mine.adapter;

import java.util.List;

import butterknife.Bind;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.mine.model.SearchSchoolInfo;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 
 * 编写日期:	2015-9-11
 * 作者:	宋宁宁
 * 
 * 历史记录
 *    修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class MyBindSchoolAdapter extends BaseListAdapter<SearchSchoolInfo.Data.School>{

	private List<SearchSchoolInfo.Data.School> list;
	private Resources res;
	/**
	 * @param ctx
	 */
	public MyBindSchoolAdapter(Context ctx) {
		super(ctx);
		res = ctx.getResources();
	}
	class MyViewHolder extends ViewHolder {
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			// TODO Auto-generated constructor stub
			super(view);
		}

		@Bind(R.id.bind_search_school_all)
		public TextView name;
	}

	
	@Override
	protected ViewHolder createViewHolder(
			View root) {
		// TODO Auto-generated method stub
		MyViewHolder holder = new MyViewHolder(root);
		return holder;
	}

//	@Override
//	protected void fillView(View root, SearchSchoolInfo item,
//			cn.vko.ring.common.base.BaseListAdapter.ViewHolder holder,
//			int position) {
//		MyViewHolder h = (MyViewHolder) holder;
//		if (item == null || item.school.get(position)==null)
//			return;
//			h.name.setText(item.school.get(position).sName);
//	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_bind_school_list;
	}

@Override
protected void fillView(View root,
		SearchSchoolInfo.Data.School item,
		ViewHolder holder, int position) {
	MyViewHolder h = (MyViewHolder) holder;
	if (item == null)
		return;
		h.name.setText(item.sName);
	
}

}
