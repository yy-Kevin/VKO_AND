/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: TestSectionListAdapter.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.adapter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-26 下午2:17:12 
 * @version: V1.0   
 */
package cn.vko.ring.study.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.widget.LineView;
import cn.vko.ring.common.widget.LineView.Type;
import cn.vko.ring.study.model.TestSection;

/**
 * @ClassName: TestSectionListAdapter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-26 下午2:17:12
 */
public class TestSectionListAdapter extends BaseListAdapter<TestSection> {
	public TestSectionListAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected ViewHolder createViewHolder(View root) {
		// TODO Auto-generated method stub
		return new MyHolder(root);
	}
	@Override
	protected void fillView(View root, TestSection item, ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (item == null) {
			return;
		}
		MyHolder h = (MyHolder) holder;
		h.sectionName.setText(item.getCourseName());
		h.starNum.setText(item.getStar());
		h.mBottomLine.setType(Type.FILL);
	}
	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_test_list;
	}

	class MyHolder extends ViewHolder {
		@Bind(R.id.section_name)
		TextView sectionName;
		@Bind(R.id.test_list_star_num)
		TextView starNum;
		@Bind(R.id.item_bottom_line)
		LineView mBottomLine;

		public MyHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}
	}
}
