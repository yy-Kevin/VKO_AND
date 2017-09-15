package cn.vko.ring.study.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.TopicInfo;
import cn.vko.ring.study.widget.TeacherHeadView;


/**
 * @author shikh
 * 专题列表适配器
 */
public class TopicListAdapter extends BaseListAdapter<TopicInfo> {

	/**
	 * @param ctx
	 */
	private Resources res;
	public TopicListAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
		res = ctx.getResources();
	}

	@Override
	protected ViewHolder createViewHolder(View root) {
		// TODO Auto-generated method stub
		MyViewHolder h = new MyViewHolder(root);
		return h;
	}

	@Override
	protected void fillView(View root, TopicInfo item, ViewHolder holder,int position) {
		// TODO Auto-generated method stub
		MyViewHolder h = (MyViewHolder) holder;
		if(item == null) return;
		h.tvTopiceName.setText(item.getName());
		String chapter = res.getString(R.string.chapter_text, item.getClassTotal());
//		String price = res.getString(R.string.price_text,Double.toString(item.getPrice()));
		String price ;
		if (TextUtils.isEmpty(item.getSellPrice()+"")) {
		price = res.getString(R.string.price_text,0.00);
			
		}else{
			
		 price = res.getString(R.string.price_text,item.getSellPrice());
		}
		h.tvChapter.setText(chapter);
//		h.tvPrice.setText(price);
//		h.ivPic.setLayoutParams(new RelativeLayout.LayoutParams(ViewUtils.getScreenWidth(ctx)-80, RelativeLayout.LayoutParams.WRAP_CONTENT));
		loadImageView(h.ivPic,item.getPic(),R.drawable.icon_special,100,100);
//		if(item.isIsbuy()){
//			h.ivBuy.setVisibility(View.GONE);
//		}else{
//			h.ivBuy.setVisibility(View.VISIBLE);
//		}
		TeacherHeadView heads = new TeacherHeadView(ctx);
		heads.addData(item.getTeacher());
		h.hsvTeacher.removeAllViews();
		h.hsvTeacher.addView(heads);
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_topic_list;
	}

	class MyViewHolder extends ViewHolder{
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			super(view);
			
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.tv_topice_name)
		public TextView tvTopiceName;
		@Bind(R.id.tv_chapter)
		public TextView tvChapter;
		@Bind(R.id.hsv_teacher)
		public HorizontalScrollView hsvTeacher;
		@Bind(R.id.tv_price)
		public TextView tvPrice;
		@Bind(R.id.tv_buy)
		public TextView ivBuy;
		@Bind(R.id.iv_topic)
		public ImageView ivPic;
		
		@OnClick(R.id.tv_buy)
		void sayBuy(){
			
		}
	}
}
