package cn.vko.ring.study.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.KnowledgeSection;
import cn.vko.ring.study.widget.ProgressBarView;

/**
 * 同步
 * @author Administrator
 *
 */
public class SyncCourseListAdapter extends
		BaseListAdapter<KnowledgeSection> {

	private Context ctx;
	public SyncCourseListAdapter(Context ctx) {
		super(ctx);
		this.ctx = ctx;
	}

	@Override
	protected ViewHolder createViewHolder(
			View root) {
		MyViewHolder h = new MyViewHolder(root);
		h.tv_section_name = (TextView) root.findViewById(R.id.tv_section_name);
		h.rect_progress = (ProgressBarView) root
				.findViewById(R.id.rect_progress);
		h.tv_study_progress = (TextView) root
				.findViewById(R.id.tv_study_progress);
		h.tv_xunzhang_count = (TextView) root
				.findViewById(R.id.tv_xunzhang_count);
		h.iv_unlock = (ImageView) root.findViewById(R.id.iv_unlock);
		h.layout_unlock = (LinearLayout) root
				.findViewById(R.id.layout_unlock);
		h.tv_chapter_count = (TextView) root.findViewById(R.id.tv_chapter_cnt);
		return h;
	}

	@Override
	protected void fillView(View root, final KnowledgeSection item,
			ViewHolder holder,
			int position) {
		if (item == null) {
			return;
		}
		final MyViewHolder h = (MyViewHolder) holder;
		h.tv_section_name.setText(item.getChapterName());
		h.tv_study_progress.setText("学习进度："+item.getTrackRate() + "%");
		h.tv_xunzhang_count.setText(item.getStar() + "");
		h.tv_chapter_count.setText(item.getVideoCnt()+"节");
		if(item.getTrackRate()>100){
			h.rect_progress.setCurrentPosition(100);	
		}else{
			h.rect_progress.setCurrentPosition(item.getTrackRate());
		}
			
//		if(item.getLockState() == 1){
			h.layout_unlock.setVisibility(View.GONE);
//		}else{
//			h.layout_unlock.setVisibility(View.VISIBLE);
//		}
		
		
	}
	
	
	@Override
	protected int getItemViewId() {
		return R.layout.item_course_list;
	}

	class MyViewHolder extends ViewHolder {
		TextView tv_section_name;
		ProgressBarView rect_progress;
		TextView tv_study_progress;
		TextView tv_xunzhang_count;
		TextView tv_chapter_count;
		ImageView iv_unlock;
		LinearLayout layout_unlock;

		public MyViewHolder(View view) {
			super(view);

		}

	}

}
