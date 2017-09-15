package cn.vko.ring.study.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.common.media.picker.util.BitmapUtil;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.NoScrollListView;
import cn.vko.ring.study.model.TopicInfo;
import cn.vko.ring.study.presenter.TopicCourseListPresenter;
import cn.vko.ring.study.widget.TeacherHeadView;
import cn.vko.ring.utils.ImageUtils;


/**
 * @author shikh
 *	专题详情
 */
public class VideoTopicDetailActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.lv_course)
	public NoScrollListView mListView;
	@Bind(R.id.tv_chapter)
	public TextView tvChapter;
	@Bind(R.id.tv_topice_name)
	public TextView tvTopiceName;
	@Bind(R.id.hsv_teacher)
	public HorizontalScrollView hsvTeacher;
	@Bind(R.id.tv_price)
	public TextView tvPrice;
	@Bind(R.id.tv_buy)
	public TextView ivBuy;
	@Bind(R.id.iv_topic)
	public ImageView ivPic;
	@Bind(R.id.tv_intro)
	public TextView tvIntro;
	@Bind(R.id.tv_intro_title)
	public TextView tvIntroTitle;
	private TopicInfo info;
	private Drawable dewUp, dewDown;
	private TopicCourseListPresenter mPresenter;
	
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_video_topic_detail;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText(R.string.text_course_topic);
		info = (TopicInfo) getIntent().getExtras().get("OBJECT");
		if(info != null){
			String chapter = getResources().getString(R.string.chapter_text, info.getClassTotal());
			String price = getResources().getString(R.string.price_text,info.getSellPrice());
			tvChapter.setText(chapter);
			tvPrice.setText(price);
			tvTopiceName.setText(info.getName());
			ImageUtils.loadPerviewImage(info.getPic(), 100, 100,ivPic);
//			if(info.isIsbuy()){
//				ivBuy.setVisibility(View.GONE);
//			}
			TeacherHeadView v = new TeacherHeadView(this);
			v.addData(info.getTeacher());
			hsvTeacher.addView(v);
		}
		dewDown = getResources().getDrawable(R.drawable.class_special_conunfold);
		dewUp = getResources().getDrawable(R.drawable.class_special_conclose);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mPresenter = new TopicCourseListPresenter(this, mListView, info,tvIntro);
	}
	@OnClick(R.id.tv_intro_title)
	void sayDisplay(){
		if(tvIntro.isShown()){
			tvIntro.setVisibility(View.GONE);
			switchDrawable(dewDown);
		}else{
			if(info != null && info.getIntro() != null){
				tvIntro.setText(info.getIntro());
			}else{
				info = mPresenter.getInfo();
				if(info != null && info.getIntro() != null){
					tvIntro.setText(info.getIntro());
				}
			}
			tvIntro.setVisibility(View.VISIBLE);
			switchDrawable(dewUp);
		}
	}
	@OnClick(R.id.iv_back)
	void sayBack(){
		finish();
	}

	public void switchDrawable(Drawable drawable) {
		if (drawable == null) {
			return;
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		tvIntroTitle.setCompoundDrawables(null, null, drawable, null);// 画在右边
	}

}
