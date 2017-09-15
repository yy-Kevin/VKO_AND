package cn.vko.ring.mine.adapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import butterknife.Bind;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.base.BaseResponseInfo;

import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.CardTagView;
import cn.vko.ring.mine.model.MineLocalCourseModel.DataEntity.GoodsListEntity;

public class MineLocalAdaper extends BaseListAdapter<GoodsListEntity> {

	VolleyUtils<BaseResponseInfo> mVolleyUtils;
	private int color = Color.parseColor("#2196F3");

	public MineLocalAdaper(Context ctx) {
		super(ctx);
		mVolleyUtils = new VolleyUtils<BaseResponseInfo>(ctx,BaseResponseInfo.class);
	}

	@Override
	protected ViewHolder createViewHolder(
			View root) {
		// TODO Auto-generated method stub
		return new MyHodler(root);
	}

	@Override
	protected void fillView(View root, GoodsListEntity item,
			ViewHolder holder,
			int position) {
		// TODO Auto-generated method stub
		if (item == null) {
			return;
		}

		MyHodler vh = (MyHodler) holder;
		vh.tcName.setText(item.getTcName());
		vh.tcInfo.setText(item.getTcSchool() + item.getTcSubject());
		if (!TextUtils.isEmpty(item.getTagdesc())) {
			if (!TextUtils.isEmpty(item.getColor())) {
				color = Color.parseColor(item.getColor());
			}
			vh.tagView.setTextAndColor(item.getTagdesc(), color);
			// vh.tagView.setBG_HEIGHT(50);

		}

		vh.courseTitle.setText(item.getGoodsName());
		vh.courseDesc.setText(item.getGoodsDesc());
		vh.tvCourseNum.setText(item.getTotalCourse() + "节");
		vh.tvCourseBuyCount.setVisibility(View.GONE);
		vh.tvPrice.setVisibility(View.GONE);
//		vh.tvCourseBuyCount.setText(item.getBuyCount() + "人购买");
//		if (item.isBuy()) {
//			vh.tvPrice.setText("已购买");
//		} else {
//			vh.tvPrice.setText("￥" + item.getSellPrice() + "");
//		}
		if (!TextUtils.isEmpty(item.getTcFace())&&URLUtil.isNetworkUrl(item.getTcFace())) {
			ImageListener imgListener = ImageLoader.getImageListener(vh.ivFace,
					R.drawable.head, R.drawable.head);
			mVolleyUtils.getImageLoader().get(item.getTcFace(), imgListener);
		}
		if (!TextUtils.isEmpty(item.getCover())&&URLUtil.isNetworkUrl(item.getCover())) {
			ImageListener imgListenerlay = ImageLoader.getImageListener(
					vh.midLayView, R.drawable.courese_default_bg,
					R.drawable.courese_default_bg);
			
			mVolleyUtils.getImageLoader().get(item.getCover(), imgListenerlay);
			
		}

	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_card_view;
	}

	class MyHodler extends ViewHolder {

		public MyHodler(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.tv_teacher_name)
		TextView tcName;
		@Bind(R.id.tv_tc_info)
		TextView tcInfo;
	
		@Bind(R.id.tv_course_title)
		TextView courseTitle;
		@Bind(R.id.tv_course_desc)
		TextView courseDesc;
		@Bind(R.id.tv_course_num)
		TextView tvCourseNum;
		@Bind(R.id.tv_buy_count)
		TextView tvCourseBuyCount;
		@Bind(R.id.tv_price)
		TextView tvPrice;
		@Bind(R.id.iv_tchead)
		ImageView ivFace;
		@Bind(R.id.iv_go)
		ImageView ivGo;
		@Bind(R.id.tag_view)
		CardTagView tagView;
		@Bind(R.id.iv_mid_lay)
		ImageView midLayView;

	}
}
