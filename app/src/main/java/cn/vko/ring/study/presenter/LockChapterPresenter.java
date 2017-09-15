package cn.vko.ring.study.presenter;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.dialog.UserVbDialog;
import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.home.model.BtnInfo;
import cn.vko.ring.mine.activity.MembersCenterActivity;
import cn.vko.ring.mine.activity.PlaceOrderActivity;
import cn.vko.ring.study.listener.ILockSuccessListener;
import cn.vko.ring.study.model.BaseSectionScoreInfo;
import cn.vko.ring.study.model.BaseSectionScoreInfo.SectionScoreInfo;
import cn.vko.ring.study.model.ComprehensiveCourses;
import cn.vko.ring.study.model.KnowledgeSection;
import cn.vko.ring.study.model.VideoAttributes;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;

public class LockChapterPresenter {
	private VolleyUtils<BaseSectionScoreInfo> volley;
	private VolleyUtils<BaseResultInfo> http;
//	private CommonDialog dialog;
	private UserVbDialog dialog;
	
//	private KnowledgeSection section;
//	private ComprehensiveCourses chapter;
	private int courseType;
	private String subjectId,bookId,chapterId;
	private Activity ctx;
	private ILockSuccessListener<Boolean> listener;
	private Map<String,String> paramsMap = new HashMap<String, String>();
	public void LockComprehensive(Activity ctx,ComprehensiveCourses chapter,ILockSuccessListener<Boolean> listener){
//		this.chapter = chapter;
		this.listener = listener;
		this.ctx = ctx;
		courseType = 43;
		subjectId = chapter.getSubjectInfo().getId();
		chapterId = chapter.getId();
		paramsMap.put("extendType",String.valueOf(1));
		paramsMap.put("courseType", "43");
		paramsMap.put("learnId",VkoContext.getInstance(ctx).getUser().getGradeId());
		paramsMap.put("subjectId",subjectId);
		paramsMap.put("k1",chapterId);
		getSectionScore(43,chapterId);
		
	}
	
	public void LockKnowledge(Activity ctx,KnowledgeSection chapter, ILockSuccessListener<Boolean> listener){
		this.ctx = ctx;
//		this.section = chapter;
		this.listener = listener;
		courseType = 41;
		chapterId = chapter.getChapterId();
		subjectId = chapter.getSubjectInfo().getId();
		bookId = chapter.getSubjectInfo().getBookId();
		paramsMap.put("extendType",String.valueOf(1));
		paramsMap.put("courseType", "41");
		paramsMap.put("learnId",VkoContext.getInstance(ctx).getUser().getGradeId());
		paramsMap.put("subjectId",subjectId);
		paramsMap.put("sectionId",chapterId);
		paramsMap.put("bookId",bookId);		
		getSectionScore(41,chapterId);
		
	}	
	
	public void LockChapter(Activity ctx,VideoAttributes vab,ILockSuccessListener<Boolean> listener){
		this.listener = listener;
		this.ctx = ctx;
		courseType = Integer.parseInt(vab.getCourseType());
		subjectId = vab.getSubjectId();
		chapterId = vab.getSectionId();
		if(courseType == 43){
			paramsMap.put("extendType",String.valueOf(1));
			paramsMap.put("courseType", "43");
			paramsMap.put("learnId",VkoContext.getInstance(ctx).getUser().getGradeId());
			paramsMap.put("subjectId",subjectId);
			paramsMap.put("k1",chapterId);
		}else{
			bookId = vab.getBookId();
			paramsMap.put("extendType",String.valueOf(1));
			paramsMap.put("courseType", "41");
			paramsMap.put("learnId",VkoContext.getInstance(ctx).getUser().getGradeId());
			paramsMap.put("subjectId",subjectId);
			paramsMap.put("sectionId",chapterId);
			paramsMap.put("bookId",bookId);
		}
		getSectionScore(courseType,chapterId);
		
	}
	
	public void getSectionScore(int courseType,String objId) {
		// TODO Auto-generated method stub
		if(dialog != null && dialog.isShowing()){
			return;
		}
		if(volley == null){
			volley = new VolleyUtils<BaseSectionScoreInfo>(ctx,BaseSectionScoreInfo.class);
		}
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VKOIP +"/tb/score");
		builder.appendQueryParameter("token", VkoContext.getInstance(ctx).getToken());
		builder.appendQueryParameter("courseType",courseType+"");
		if(courseType == 43){//综合
			builder.appendQueryParameter("k1", objId);
		}else{
			builder.appendQueryParameter("sectionId", objId);
		}
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(new UIDataListener<BaseSectionScoreInfo>() {
			@Override
			public void onDataChanged(BaseSectionScoreInfo response) {
				if(response != null && response.getData() != null){
					showHintLockDialog(response.getData());
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}
	int surplus,needScore;
	protected void showHintLockDialog( SectionScoreInfo data) {
		// TODO Auto-generated method stub
		final int scrose = data.getScore();
		needScore = data.getNeedScore();
		paramsMap.put("vb",String.valueOf(needScore));
		surplus = data.getScore() - needScore;
		if (dialog == null) {
			dialog = new UserVbDialog(ctx);
			dialog.setHeadImage(R.drawable.head_recharge);
			dialog.setOnClickItemListener(new IOnClickItemListener<String>() {

				@Override
				public void onItemClick(int position, String t, View v) {
					// TODO Auto-generated method stub
					if(position == 2){
						dialog.dismiss();
					}else if(position == 0){
						if(surplus >=0){
							buyCourseTask();
						}else{
							Bundle bundle = new Bundle();
							bundle.putInt("HAVESCORE",scrose);
							bundle.putDouble("NEEDSCORE", needScore);
							bundle.putString("SUBJECT", "解锁章节");
							bundle.putString("EXTENDINFO", JSON.toJSONString(paramsMap,true));
							StartActivityUtil.startActivity(ctx,PlaceOrderActivity.class, bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
							dialog.dismiss();
						}
					}else if(position == 1){
						StartActivityUtil.startActivity(ctx, MembersCenterActivity.class);
						dialog.dismiss();
					}
				}
			});
		}
		dialog.setContentText("你需要花费" + data.getNeedScore() + "V币"+"\r\n现有"+data.getScore()+"V币");
		dialog.setBtnInfo(new BtnInfo(surplus >=0 ?"解锁本章":"充值", R.color.white,
				R.drawable.selector_light_blue_button), new BtnInfo("开通会员", R.color.white,
				R.drawable.selector_red_button),new BtnInfo("取消", R.color.white, R.drawable.shape_gray_btn));
		if(!dialog.isShowing()){
			dialog.show();
		}
	}

	private void buyCourseTask() {
		// TODO Auto-generated method stub
		if(http == null){
			http = new VolleyUtils<BaseResultInfo>(ctx,BaseResultInfo.class);
		}
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VKOIP +"/tb/unlock");
		builder.appendQueryParameter("token", VkoContext.getInstance(ctx).getToken());
		builder.appendQueryParameter("courseType",courseType+"");
		builder.appendQueryParameter("learnId",VkoContext.getInstance(ctx).getUser().getGradeId());
		builder.appendQueryParameter("subjectId",subjectId);
		builder.appendQueryParameter("vb",String.valueOf(needScore));
		if(courseType == 43){//综合
			builder.appendQueryParameter("k1",chapterId);
		}else{
			builder.appendQueryParameter("sectionId",chapterId);
			builder.appendQueryParameter("bookId",bookId);
		}
		http.sendGETRequest(true,builder);
		http.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if(response != null && response.getCode() == 0){
					if(listener != null){
						listener.onLock(true);
					}
					dialog.dismiss();
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}
	
	public void refreshLock(){
		if(listener != null){
			listener.onLock(true);
		}
	}
 
}
