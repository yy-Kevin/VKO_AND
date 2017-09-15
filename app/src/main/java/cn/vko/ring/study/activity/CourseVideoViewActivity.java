package cn.vko.ring.study.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.NetUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.BaseUMShare;
import cn.vko.ring.common.volley.VolleyQueueController;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.ContainsEmojiEditText;
import cn.vko.ring.common.widget.LinearLayoutForListView;
import cn.vko.ring.common.widget.ListScrollView;
import cn.vko.ring.common.widget.NoScrollListView;
import cn.vko.ring.common.widget.PullToRefreshView;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.common.widget.VkoMediaController;
import cn.vko.ring.common.widget.VkoVideoView;
import cn.vko.ring.common.widget.XGridView;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog.OnSweetClickListener;
import cn.vko.ring.common.widget.dialog.ShapeDialog;
import cn.vko.ring.home.presenter.VbDealPrsenter;
import cn.vko.ring.local.presenter.PayLocalPresenter;
import cn.vko.ring.mine.activity.SystemActivity;
import cn.vko.ring.study.db.DBservice;
import cn.vko.ring.study.listener.ILockSuccessListener;
import cn.vko.ring.study.model.BaseUnit;
import cn.vko.ring.study.model.BaseUnitData;
import cn.vko.ring.study.model.DownloadInfo;
import cn.vko.ring.study.model.Praise;
import cn.vko.ring.study.model.UnitVideo;
import cn.vko.ring.study.model.VideoAttributes;
import cn.vko.ring.study.model.VideoCommentInfo;
import cn.vko.ring.study.model.VideoRecordInfo;
import cn.vko.ring.study.model.VideoSet;
import cn.vko.ring.study.model.Zan;
import cn.vko.ring.study.presenter.LockChapterPresenter;
import cn.vko.ring.study.presenter.MyCollectVideoPersoner;
import cn.vko.ring.study.presenter.MyDownLoadCommons;
import cn.vko.ring.study.presenter.MySubmitHostoryPersoner;
import cn.vko.ring.study.presenter.MyVideoListPersoner;
import cn.vko.ring.study.presenter.VideoCommentPrenenter;
import cn.vko.ring.study.presenter.VideoCommonListPresenter;
import cn.vko.ring.study.widget.OpenVipDialog;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;


/**
 * @author shikh
 * 
 */
public class CourseVideoViewActivity extends BaseActivity implements
		OnCompletionListener, VideoCommentPrenenter.IRefreshCommentListener, MyVideoListPersoner.IVideoListListener,UMShareListener {
	@Bind(R.id.video_view)
	public VkoVideoView mVideoView;
	@Bind(R.id.pb_loadingprogress)
	public ProgressBar pb;
	@Bind(R.id.parent_layout)
	public RelativeLayout videoLayout;
	@Bind(R.id.start_imageView)
	public ImageView ivStart;
	@Bind(R.id.v_back)
	public ImageView videoBack;

	@Bind(R.id.tv_content)
	public TextView tvChapterName;// 章节名
	@Bind(R.id.tv_like_animation)
	public TextView tvLikeAnimation;
	@Bind(R.id.tv_video_name)
	public TextView tvVideonName;
	@Bind(R.id.iv_like)
	public ImageView ivLike;// 点赞
	@Bind(R.id.tv_like_count)
	public TextView tvLikeCount;// 点赞数
	@Bind(R.id.iv_forward)
	public ImageView ivForward;// 分享
	@Bind(R.id.iv_download)
	public ImageView ivDownload;// 下载
	@Bind(R.id.iv_collect)
	public ImageView ivCollect;// 收藏

	@Bind(R.id.iv_teacher_photo)
	public RoundAngleImageView ivTeacherAvatar;
	@Bind(R.id.tv_teacher_name)
	public TextView tvTeacherName;
	@Bind(R.id.tv_school_name)
	public TextView tvSchoolName;
	@Bind(R.id.iv_teacher_play)
	public TextView ivTeacherPlay;
	@Bind(R.id.layout_close_list)
	public LinearLayout chapterslayout;// 其他章节
	@Bind(R.id.gv_number_sections)
	public XGridView mGvChapter;
	@Bind(R.id.lv_review_Comments_list)
	public LinearLayoutForListView lvComment;
	@Bind(R.id.sv_load)
	public ScrollView svLoad;
	@Bind(R.id.main_pull_refresh_view)
	public PullToRefreshView mPullToRefreshView;

	@Bind(R.id.reply_layout)
	public View replyLayout;
	// 视频评论部分
	@Bind(R.id.et_text)
	public ContainsEmojiEditText etReply;
	@Bind(R.id.iv_send)
	public ImageView ivSend;

	private VideoAttributes vab;
	private Animation animation;
	private SweetAlertDialog dialog;
	private VideoCommentPrenenter mReplyPrerenter;//评论
	private VideoCommonListPresenter commentListPresenter;//评论列表
	private LockChapterPresenter presenter;//解锁章节
	private PayLocalPresenter payPresenter;//购买本地课
	private ShapeDialog shapeDialog;
	private DBservice service;
	private SharedPreferences sharedPreferences;
	private int i;

	private UnitVideo uVideo;
	private String vid;
	public Zan zan;
	public BaseUnitData tUnitData;
	private String teacherurl, teachername, videoname, videourl, sectionId,bookId;
	private String teacherVideoUrl;
	private String courseType;//
	private String goodId;
	public String encryptionid;
	private boolean likekey = false;
	private boolean losekey = false;
	private boolean flag = false,isCache;

	private VolleyUtils<BaseUnit> volley;
	private VolleyUtils<Praise> volleyPraise;
	private Handler handler = new Handler();

	private boolean isUpdate;
	private long playTime;
	private MyDownLoadCommons downloadPresent;

	private int type;//1章节解锁视频  //3章节未解锁视频 4本地课程 2专题  -1 圈子视频  -2下载列表（横屏） 0 其他
	private int now;//是否在直播0不在直播  1在直播
	private long timeCha;//距离开始播放时间差
	private String specialId;
	private MyVideoListPersoner videosPersoner;

	private VkoMediaController mediaController;
	@Override
	protected boolean isEnableSwipe() {
		return false;
	}

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_course_video;
	}

	@Override
	public void onCreateBefore(Bundle savedInstanceState) {
		super.onCreateBefore(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		animation = AnimationUtils.loadAnimation(this, R.anim.applaud_animation);
		tintManager.setStatusBarTintResource(R.color.black);
		setVideoWithAndHeight(Configuration.ORIENTATION_PORTRAIT);
		mVideoView.setVideoLoadingPb(pb);
		mVideoView.setOnCompletionListener(this);
//		mVideoView.setLock(true);
		// mVideoView.getMediaController().setOnShareListener(this);
		etReply.setFocusable(true);
		etReply.setFocusableInTouchMode(true);
		etReply.requestFocus();
		replyLayout.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				svLoad.getChildAt(0).setPadding(0, 0, 0,
						replyLayout.getHeight());
			}
		});
	}

	private void setVideoWithAndHeight(int orintation) {
		WindowManager wm = this.getWindowManager();
		int w = wm.getDefaultDisplay().getWidth();
		int h = wm.getDefaultDisplay().getHeight();
		// 小窗口的比例
		float ratio = (float) 15 / 9;
		int adjusted_h = (int) Math.ceil((float) w / ratio);
		if (orintation == Configuration.ORIENTATION_PORTRAIT) {
			videoBack.setLayoutParams(new RelativeLayout.LayoutParams(w,
					adjusted_h));
		} else {
			videoBack.setLayoutParams(new RelativeLayout.LayoutParams(w, h));
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		service = new DBservice(this);
		type = getIntent().getExtras().getInt("TYPE", 0);
		now = getIntent().getExtras().getInt("NOW", 0);
		timeCha = getIntent().getExtras().getLong("TIME", 0);
		specialId=getIntent().getStringExtra("goodsId");

		vab = (VideoAttributes) getIntent().getExtras().getSerializable("video");
//		Log.e("=========>now",now+"");
//		Log.e("=========>timeCha时间差",timeCha+"");
		if (now==1){
			mVideoView.getMediaController().setProgressVisibility(false);
			mVideoView.getMediaController().setEndTimeVisibility(false);
		}else{
			mVideoView.getMediaController().setProgressVisibility(true);
			mVideoView.getMediaController().setEndTimeVisibility(true);
		}
		if (type == -2) {
			mVideoView.setActivity(this, Configuration.ORIENTATION_LANDSCAPE);
		} else {
			mVideoView.setActivity(this, Configuration.ORIENTATION_PORTRAIT);
		}
		if (vab != null) {
			initStartPlay();
		}
	}

	private void initStartPlay() {
		// TODO Auto-generated method stub
		if (vab.getBookName() != null) {
			tvChapterName.setText(vab.getBookName());
		}
		courseType = vab.getCourseType();
		if (courseType == null) {
			ivForward.setVisibility(View.GONE);
			chapterslayout.setVisibility(View.GONE);
		}
		if (vab.getGoodsId()==null){
			goodId=specialId;
		}else {
			goodId = vab.getGoodsId();
		}
		Log.e("=========>goosId播放页",goodId+"");
		bookId = vab.getBookId();
		vid = vab.getVideoId();
		encryptionid = vab.getEncryptionid();// 下载的视频 已解密的vid

//		Log.e("=========>encryptionid",encryptionid);
		sectionId = vab.getSectionId();
		if (encryptionid != null) {// 从下载列表进入

			getCacheData(vid);
//			ivStart.setVisibility(View.GONE);
//			videoBack.setVisibility(View.GONE);
//			replyLayout.setVisibility(View.GONE);
//			mVideoView.getMediaController().setFileName(vab.getVideoName());
//			mVideoView.setVid(encryptionid, 1);
//			Log.e("---下载==1----->播放了","180"+"");
//			mVideoView.start();
		} else {
			getVideoData();
			// 获取评论列表
			commentListPresenter = new VideoCommonListPresenter(this, vid,
					lvComment,  mPullToRefreshView, svLoad);
			// 视频评论
			mReplyPrerenter = new VideoCommentPrenenter(etReply, ivSend, vid,this);
			mReplyPrerenter.setOnRefreshCommentListener(this);
			// 视频播放 选集集合
			videosPersoner = new MyVideoListPersoner(this, vab.getBookId(),vab.getCourseType(),vab.getSectionId(),vab.getVideoId(), mGvChapter);
			videosPersoner.setiVideolistener(this);
//			getCacheData();
		}
	}

	private void getCacheData(String vid) {
		if(vid != null){
			DownloadInfo info = service.getLoadInfo(vid);
			if (info != null) {
				String encryptionid = info.getVid();
				Log.e("=========>getCacheData","encryptionid=========="+encryptionid);
				if (encryptionid != null) {// 从下载列表进入
					isCache = true;
					ivStart.setVisibility(View.GONE);
					videoBack.setVisibility(View.GONE);
					mVideoView.getMediaController().setFileName(info.getTitle());
					mVideoView.setVid(encryptionid, info.getBitrate());
					mVideoView.start();
				}
			}
		}
//		startPlay(tUnitData);
	}

	// 获取视频详情
	private void getVideoData() {
		// TODO Auto-generated method stub
		if (volley == null) {
			volley = new VolleyUtils<BaseUnit>(this,BaseUnit.class);
		}
		Uri.Builder builder;
		if(type != -1){
			builder = volley.getBuilder(ConstantUrl.VKOIP.concat("/play/video"));
			if(type == 1 || type == 2 || type == 3){
//				builder.appendQueryParameter("type", "1");
//			}else if(type == 3){//章节未解锁
//				builder.appendQueryParameter("type", "2");
			}
//			if (vab.getGoodsId() != null) {
//				builder.appendQueryParameter("goodsId", vab.getGoodsId());
//			}
			if (goodId != null) {
				builder.appendQueryParameter("goodsId", goodId);
			}
			if (sectionId!=null&&!sectionId.isEmpty()){
				builder.appendQueryParameter("sectionId", sectionId);
			}
			if (bookId!=null){
				builder.appendQueryParameter("bookId", bookId);
			}
		}else{
			 builder = volley.getBuilder(ConstantUrl.VKOIP.concat("/groups/video"));
			 if (goodId != null) {
					builder.appendQueryParameter("groupId",goodId);
				}
		}
		builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken());

		builder.appendQueryParameter("videoId", vab.getVideoId());
		volley.sendGETRequest(false,builder);
		volley.setUiDataListener(new UIDataListener<BaseUnit>() {
			@Override
			public void onDataChanged(BaseUnit response) {
				if (response != null && response.getData() != null) {
					initVideoView(response.getData());
				}
				videosPersoner.setSwitch(true);

			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				videosPersoner.setSwitch(true);
//				getCacheData(vid);
			}
		});
	}

	public void initVideoView(BaseUnitData t) {
		if(this.isFinishing() || t == null) return;
		this.tUnitData = t;
		uVideo = t.getVideo();
		zan = t.getZan();
		if (uVideo != null) {
			if (TextUtils.isEmpty(uVideo.getSchool())) {
				tvSchoolName.setText(uVideo.getSchool());
			}
			vid = uVideo.getVideoId();
			videoname = uVideo.getVideoName();
			videourl = uVideo.getVideoUrl();
			teacherVideoUrl = uVideo.getTVideo();
			teacherurl = uVideo.getTsface();
			teachername = uVideo.getTName();
			mReplyPrerenter.updateVideoId(vid);
			commentListPresenter.updateData(vid);
		}
		if(tUnitData != null && tUnitData.isAuth() && type != 3){
			mVideoView.setLock(false);
			ivStart.setImageResource(R.drawable.video_start_play_kaishi);
		}else {
			type=3;
		}

		if (videourl != null) {
			ImageListener imgListener = ImageLoader.getImageListener(videoBack, 0, 0);
			VolleyQueueController.getInstance().getImageLoader().get(videourl, imgListener);
		}

		if (teacherurl != null) {
			ImageListener imgListener = ImageLoader.getImageListener(
					ivTeacherAvatar, 0, 0);
			VolleyQueueController.getInstance().getImageLoader().get(teacherurl, imgListener);
		}
		tvTeacherName.setText(teachername);
		tvVideonName.setText(videoname);
		//now位1时自动播放视频
		if(now==1){
			videoStartPlay();
		}
		ZanAndStep(zan);
		// 收藏视频
		new MyCollectVideoPersoner(this, tUnitData, ivCollect);
		mVideoView.getMediaController().setFileName(videoname);
		if (flag && !isCache) {
			VideoPlayOnClick();
		}
	}

	protected void ZanAndStep(Zan zan) {
		if (zan != null && ivLike != null) {
			tvLikeCount.setText(zan.getPraiseCnt() + "");
			if (zan.getIsPraise()) {
				likekey = true;
				ivLike.setImageResource(R.drawable.zan_video_pressed);
			} else {
				likekey = false;
				ivLike.setImageResource(R.drawable.zan_video_nomal);
			}
		}
	}

	@Override
	public void onCompletion(IMediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.start_imageView)
	public void videoStartPlay() {
		flag = true;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		VideoPlayOnClick();
	}

	private void VideoPlayOnClick() {
		getCacheData(vid);
		if(tUnitData == null){
			Toast.makeText(this, "正在获取视频信息，稍等...", Toast.LENGTH_SHORT).show();
			// 重新获取
			getVideoData();
		}else if(!isCache){
			startPlay(tUnitData);
		}
	}

	protected void startPlay(BaseUnitData t) {
		Log.e("=====type",type+"");
		if (t!= null && t.isAuth() && type != 3 && type != 4) {//&& type != 4
			ivStart.setVisibility(View.GONE);
			videoBack.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
//			mVideoView.seekTo(199602);
			palyAuthority(t.getVideo().getPlay());
		} else if(type == 3&&vab.getCourseType().equals("41")){//未解锁-》去解锁视频
			lockChapterTask();
		}else if(type == 4){//解锁本地程程
			lockLocalVideoTask();
		}else{
			new OpenVipDialog(this);
		}
	}
	//解锁本地程程
	private void lockLocalVideoTask() {
		// TODO Auto-generated method stub
		if(payPresenter == null){
			payPresenter = new PayLocalPresenter(this);
		}
		payPresenter.initData(vab);
	}
	//解锁章节视频
	private void lockChapterTask(){
		if(presenter == null){
			presenter = new LockChapterPresenter();
		}
		presenter.LockChapter(this,vab,new ILockSuccessListener<Boolean>() {
					@Override
					public void onLock(Boolean t) {
						// TODO Auto-generated method stub
						if(type == 3){
							type = 1;
							EventBus.getDefault().post(Constants.LOCKCHAPTER);
							tUnitData.setAuth(true);
							mVideoView.setLock(false);
							getVideoData();
							VideoPlayOnClick();
						}
//						t.setLockState(1);
					}
				});
	}

	/**
	 * 播放权限
	 */
	private void palyAuthority(String videoId) {
		// tv_video_name.setText(uVideo.getVideoName());
		if (!NetUtils.isCheckNetAvailable(this)) {
			Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
			return;
		}
		if (NetUtils.getAPNType(this) != NetUtils.WIFI) {
			if (VkoConfigure.getConfigure(this).getBoolean(
					Constants.STATEWATCH, false)) {
				Toast.makeText(this, "当前用的是移动流量", Toast.LENGTH_SHORT).show();
				pb.setVisibility(View.GONE);
				mVideoView.setVid(videoId);
			} else {
				showAlerDialogNet();
			}
		} else {
			if (mVideoView != null) {
				try {
					mVideoView.setVid(videoId);
					mVideoView.seekTo(timeCha);
//					mVideoView.getMediaController().;
					Log.e("-------->开始从",timeCha+"播放了欧耶");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 *
	 */
	private void showAlerDialogNet() {
		// TODO Auto-generated method stub
		if (dialog == null) {
			dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
			dialog.setContentText("当前没有网络，请跳至设置进行设置");
			dialog.setTitleText("提示！");
			dialog.setCancelText("取消");
			dialog.setConfirmText("确定");
			dialog.setCancelClickListener(new OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sweetAlertDialog) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					pb.setVisibility(View.GONE);
					ivStart.setVisibility(View.VISIBLE);
				}
			});
			dialog.setConfirmClickListener(new OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sweetAlertDialog) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(CourseVideoViewActivity.this,
							SystemActivity.class);
					startActivity(intent);
					dialog.dismiss();
					pb.setVisibility(View.GONE);
					ivStart.setVisibility(View.VISIBLE);
				}
			});
		}
		dialog.show();
	}

	@OnClick(R.id.iv_teacher_play)
	void sayPlayTeach() {
		if (!TextUtils.isEmpty(teacherVideoUrl)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(teacherVideoUrl), "video/mp4");
			startActivity(intent);
		} else {
			Toast.makeText(this, "老师个人视频简介暂未上传，请稍后", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRefresh(VideoCommentInfo comm) {
		// TODO Auto-generated method stub
		commentListPresenter.addComment(comm);
	}

	@Override
	public void getVideoList(VideoSet videoSet) {
		// TODO Auto-generated method stub
		encryptionid = null;
		SubmitCourseProgress(mVideoView.getPlayTimes(),false);
		videosPersoner.setSwitch(false);
		likekey = false;
		losekey = false;
		pb.setVisibility(View.VISIBLE);
		mVideoView.stopPlayback();
		tUnitData = null;
		isCache = false;
		vid = videoSet.getVideoId();
		vab.setVideoId(vid);
		// new MyVideoScrollCommons(this, vid, lv_review_Comments_list,
		// sv_load,et_text);
		mVideoView.seekToByProgreww(0);
//		mVideoView.seekTo(180);
		getVideoData();
		getCacheData(vid);
	}

	private void SubmitCourseProgress(long playTime,boolean isPause) {
		Log.e("======playTime", playTime+"");
		if (playTime > 1000) {
			VideoRecordInfo submitRecord = new VideoRecordInfo();
			submitRecord.setBookId(bookId);
			submitRecord.setCourseType(courseType);
			submitRecord.setGoodsId(goodId);
			submitRecord.setSec(playTime);
			submitRecord.setSubjectId(vab.getSubjectId());
			submitRecord.setVideoId(vid);
			submitRecord.setKnowledgeId(sectionId);
			if(!isPause){
				isUpdate = true;
			}
			new MySubmitHostoryPersoner(submitRecord, this,isPause);
		}else if(isPause){
			if(isUpdate){
				EventBus.getDefault().post(Constants.REFRESH_SYNCTEST_SYNC_DATA_VIDEO);
			}
		}
	}

	@OnClick(R.id.tv_wedge)
	public void reportWedge() {//报卡

	}

	@OnClick(R.id.iv_download)
	public void download() {


		if (tUnitData != null && type != 3) {
			getSahrePreference();
//			if(downloadPresent == null){
//				downloadPresent = new MyDownLoadCommons(this);
//			}
//			downloadPresent.loadVideo(tUnitData);
		}else if(type == 3){
			Toast.makeText(getApplicationContext(), "你没有下载该视频的权限",Toast.LENGTH_SHORT).show();
		}
	}


	// 获得sharedpreferences的数据
	public void getSahrePreference() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String year1 = String.valueOf(year);
		String month1 = String.valueOf(month);
		String day1 = String.valueOf(day);
		String dDay=year1+month1+day1;
		Log.e("========dDay",dDay+"*");
		SharedPreferences preferences=getSharedPreferences("downtimes", Context.MODE_MULTI_PROCESS);
		String downLoadDay = preferences.getString("downLoadDay", "");
		int downLoadTime = preferences.getInt("downLoadTime", 0);
		Log.e("========downLoadDay",downLoadDay+"*");
		Log.e("========downLoadTime",downLoadTime+"*");
//		String str = String.valueOf(downLoadTime);

		if (downLoadDay.equals(dDay)){
			if (downLoadTime>9){
				showToast("今日下载超过次数");
			}else {
				if(downloadPresent == null){
					downloadPresent = new MyDownLoadCommons(this);
				}
				downloadPresent.loadVideo(tUnitData);
			}
		}else {
			if(downloadPresent == null){
				downloadPresent = new MyDownLoadCommons(this);
			}
			downloadPresent.loadVideo(tUnitData);
		}
	}

	// 存储sharedpreferences
//	public void setSharedPreference() {
//		Calendar c = Calendar.getInstance();
//		int year = c.get(Calendar.YEAR);
//		int month = c.get(Calendar.MONTH)+1;
//		int day = c.get(Calendar.DAY_OF_MONTH);
//		String year1 = String.valueOf(year);
//		String month1 = String.valueOf(month);
//		String day1 = String.valueOf(day);
//		String downLoadDay=year1+month1+day1;
//		sharedPreferences = getSharedPreferences("itcast", Context.MODE_APPEND);
//		SharedPreferences.Editor editor = sharedPreferences.edit();
//		editor.putInt("downLoadTime",i++);
//		editor.putString("downLoadDay",downLoadDay);
//		Log.e("========时间",downLoadDay);
//		Log.e("========downLoadTime",i+"");
//		editor.commit();// 提交修改
//
//
//	}

	@OnClick(R.id.et_text)
	public void eTtextOnclick() {
		ivSend.setVisibility(View.VISIBLE);

	}

	@OnClick(R.id.iv_forward)
	public void forword() {
//		commonShare = new CommonShare(this, vid, teachername, videoname,videourl, courseType);
		final String shareContent = "同学们都在微课圈看 "+teachername+"名师课程，点此获取价值千元课程。";
		final String shareUrl =getShareUrl(courseType,vid,this);
		if (shapeDialog == null) {
			shapeDialog = new ShapeDialog(this);
			shapeDialog.setCanceledOnTouchOutside(true);
//			commonShare.initSocialSDK(this,this.getResources().getString(R.string.share_str));
			shapeDialog.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					BaseUMShare umShare = null;
					if (position == 0) {
						umShare = new BaseUMShare(CourseVideoViewActivity.this,SHARE_MEDIA.WEIXIN);
					} else if (position == 1) {
						umShare = new BaseUMShare(CourseVideoViewActivity.this,SHARE_MEDIA.WEIXIN_CIRCLE);
					} else if (position == 2) {
						umShare = new BaseUMShare(CourseVideoViewActivity.this,SHARE_MEDIA.QQ);
					} else if (position == 3) {
						umShare = new BaseUMShare(CourseVideoViewActivity.this,SHARE_MEDIA.QZONE);
					}
					if(umShare != null){
						umShare.shareVideo(videoname,shareContent,shareUrl,videourl);
						umShare.setShareListener(CourseVideoViewActivity.this);
					}
					shapeDialog.dismiss();
				}
			});
		}
		shapeDialog.show();

	}
	private static String getShareUrl(String courseType,String vid,Context mAct) {
		int KEY = Integer.parseInt(courseType);
		StringBuffer sb = new StringBuffer();
		switch (KEY) {
			case 41:
				String bookId=VkoConfigure.getConfigure(mAct).getString("bookId");
				String sectionId=VkoConfigure.getConfigure(mAct).getString("sectionId");
				sb.append("videoId="+vid).append("&").append("sharefrom="+KEY).append("&").append("bookId="+bookId).append("&").append("sectionId="+sectionId);
				break;
			case 43:
				String k1=VkoConfigure.getConfigure(mAct).getString("k1");
				String subjectId=VkoConfigure.getConfigure(mAct).getString("subjectId");
				String learnId=VkoContext.getInstance(mAct).getUser().getLearn();
				sb.append("videoId="+vid).append("&").append("sharefrom="+KEY).append("&").append("k1="+k1).append("&").append("learnId="+learnId).append("&").append("subjectId="+subjectId);
				break;
			case 44:
				String goodId=VkoConfigure.getConfigure(mAct).getString("goodId");
				sb.append("videoId="+vid).append("&").append("sharefrom="+KEY).append("&").append("goodsId="+goodId);
				break;

			default:
				break;
		}

		Log.e("分享路径2", Constants.SHAREWINQQ+sb.toString());
		return Constants.SHAREWINQQ+sb.toString();

	}
	@OnClick(R.id.layout_like)
	public void onLike() {

		if (!likekey && !losekey) {
			// 发送点赞请求
			sendPraise("1");
		} else {
			if (likekey) {
				Toast.makeText(this, "你已经点过赞了", Toast.LENGTH_SHORT).show();
			}
			if (losekey) {
				Toast.makeText(this, "你已经点过踩了", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void DisplayAnimation(final TextView textView) {
		textView.setVisibility(View.VISIBLE);
		textView.startAnimation(animation);
		handler.postDelayed(new Runnable() {
			public void run() {
				textView.setVisibility(View.GONE);
			}
		}, 1000);
	}

	/**
	 * @param opt
	 */
	private void sendPraise(String opt) {
		String commenturl = ConstantUrl.VKOIP.concat("/course/videoZan/");
		// http://192.168.1.77:9999/course/videoZan?videoId=13390692443880183&opt=2&token=fb828ff805f44cd49509170f82c77e86
		if (volleyPraise == null) {
			volleyPraise = new VolleyUtils<Praise>(this,Praise.class);
		}
		final Uri.Builder builder = volleyPraise.getBuilder(commenturl);
		builder.appendQueryParameter("videoId", vid);
		builder.appendQueryParameter("opt", opt);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getUser().getToken());
		Log.e("地址", builder.toString());
		volleyPraise.sendGETRequest(false,builder);
		volleyPraise.setUiDataListener(new UIDataListener<Praise>() {
			@Override
			public void onDataChanged(Praise praise) {
				if (praise != null) {
					ivLike.setImageResource(R.drawable.zan_video_pressed);
					DisplayAnimation(tvLikeAnimation);
					likekey = true;
					if (zan != null) {
						tvLikeCount.setText(zan.getPraiseCnt() + 1 + "");
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Subscribe
	public void onEventMainThread(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			if (msg.equals(Constants.REFRESH_RESTORE)) {
				VideoPlayOnClick();
			}else if(msg.equals(Constants.OPEN_MEMBER)){
				flag = true;
				if(type == 3){
					type = 1;
					tUnitData.setAuth(true);
					mVideoView.setLock(false);
					VideoPlayOnClick();
				}else if(type == 4){
					getVideoData();
				}
				
			}else if(msg.equals(Constants.LOGIN_REFRESH)){
				getVideoData();
			}else if(msg.equals(Constants.PAYMENT_COMPLETE)){
				flag = true;
				if(type == 3){
					type = 1;
					tUnitData.setAuth(true);
					VideoPlayOnClick();
					mVideoView.setLock(false);
					EventBus.getDefault().post(Constants.LOCKCHAPTER);
				}else if(type == 4){
					getVideoData();
					EventBus.getDefault().post(Constants.PAYLOCALCOURSE);
				}
			}
		}
	}

	@Override
	protected void onPause() {
		if (playTime == 0) {
			playTime = mVideoView.getPlayTimes();
		}
		mVideoView.pause();
		Log.e("======暂停playTime", playTime+"");
//		isPlay = mVideoView.isPlaying();
		if (type == 1 || type == 0) {
			SubmitCourseProgress(playTime,true);
		}
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		if (mVideoView.getScreenListener() != null) {
			mVideoView.getScreenListener().unregisterListener();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (shapeDialog != null) {
				shapeDialog.dismiss();
			}
			if (!mVideoView.getMediaController().isScreenPortrait()) {// 是否竖屏
				if (mVideoView.getMediaController().isSwitchBoard) {
					mVideoView.onLandscape();
					return false;
				}
			}
			playTime = mVideoView.getCurrentPosition();
//			Log.e("======playTime3", playTime+"");
			mVideoView.stopPlayback();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onConfigurationChanged(Configuration arg0) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(arg0);
		setVideoWithAndHeight(arg0.orientation);
		/** 12.21添加，通知首页的推荐知识点刷新，解决布局异常问题，JiaRH */
		EventBus.getDefault().post(Constants.RECOMMOND_REFRESH);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(mVideoView != null){
//			mVideoView.stopPlayback();
			mVideoView.unregisterReceiver();
		}
	}

	@Override
	public void onResult(SHARE_MEDIA share_media) {
		new VbDealPrsenter(this).doRequest(VbDealPrsenter.SHARE_VIDEO);
	}

	@Override
	public void onError(SHARE_MEDIA share_media, Throwable throwable) {

	}

	@Override
	public void onCancel(SHARE_MEDIA share_media) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}
}
