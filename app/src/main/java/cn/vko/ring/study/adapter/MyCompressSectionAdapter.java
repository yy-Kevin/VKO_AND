package cn.vko.ring.study.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.CompressVideo;
import cn.vko.ring.study.widget.RectProgressView;


public class MyCompressSectionAdapter extends BaseListAdapter<CompressVideo> {
	private Context context;
//	private DisplayImageOptions options;
	private String goodId;
	private String subjectId;
	private String bookId;
	private String sectionId;
	private String courseId;

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public MyCompressSectionAdapter(Context ctx) {
		super(ctx);
		this.context = ctx;
	}

	@Override
	protected ViewHolder createViewHolder(
			View root) {
		MyViewHolder h = new MyViewHolder(root);
		h.tv_zhengjie_name = (TextView) root
				.findViewById(R.id.tv_zhengjie_name);
		h.iv_toplay = (ImageView) root.findViewById(R.id.iv_toplay);
		h.rect_progress = (RectProgressView) root
				.findViewById(R.id.rect_progress);
		h.tv_progress_length = (TextView) root
				.findViewById(R.id.tv_progress_length);
		return h;
	}

	@Override
	protected void fillView(View root, CompressVideo item,
			ViewHolder holder,
			int position) {
		if (item == null) {
			return;
		}
		final MyViewHolder h = (MyViewHolder) holder;
		Log.e("sectionname", item.getVideoName()+"");
		h.tv_zhengjie_name.setText(item.getVideoName());
		h.tv_progress_length.setText(item.getTrackRate() + "%");
		ChildShowProgress(h, item.getTrackRate());
	}
		
	private void ChildShowProgress(MyViewHolder viewHolder, int postion) {
		if (postion > 0 && postion < 5) {
			viewHolder.rect_progress.setmProgress(5);
		} else {
			viewHolder.rect_progress.setmProgress(postion);
		}

	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_zhangjie;
	}

	class MyViewHolder extends ViewHolder {
		TextView tv_zhengjie_name, tv_progress_length;
		RectProgressView rect_progress;
		ImageView iv_toplay;

		public MyViewHolder(View view) {
			super(view);

		}

	}

}
