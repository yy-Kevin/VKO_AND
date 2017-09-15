package cn.vko.ring.classgroup.activity;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import org.greenrobot.eventbus.EventBus;

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
import cn.vko.ring.common.volley.VolleyQueueController;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.ContainsEmojiEditText;
import cn.vko.ring.common.widget.LinearLayoutForListView;
import cn.vko.ring.common.widget.PullToRefreshView;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.common.widget.VkoVideoView;
import cn.vko.ring.common.widget.VkoVideoView1;
import cn.vko.ring.common.widget.XGridView;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog.OnSweetClickListener;
import cn.vko.ring.mine.activity.SystemActivity;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.study.db.DBservice;
import cn.vko.ring.study.model.BaseUnit;
import cn.vko.ring.study.model.BaseUnitData;
import cn.vko.ring.study.model.DownloadInfo;
import cn.vko.ring.study.model.Praise;
import cn.vko.ring.study.model.UnitVideo;
import cn.vko.ring.study.model.VideoCommentInfo;
import cn.vko.ring.study.model.VideoSet;
import cn.vko.ring.study.model.Zan;
import cn.vko.ring.study.presenter.MyCollectVideoPersoner;
import cn.vko.ring.study.presenter.MyDownLoadCommons;
import cn.vko.ring.study.presenter.MyVideoListPersoner;
import cn.vko.ring.study.presenter.VideoCommentPrenenter;
import cn.vko.ring.study.presenter.VideoCommonListPresenter;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;


/**
 * @author shikh
 *
 */
public class GroupQinkeVideoActivity extends BaseActivity implements
        OnCompletionListener, VideoCommentPrenenter.IRefreshCommentListener, MyVideoListPersoner.IVideoListListener {
    @Bind(R.id.video_view)
    public VkoVideoView1 mVideoView;
    @Bind(R.id.scroll_view_video)
    public ScrollView scrollView;
    @Bind(R.id.pb_loadingprogress)
    public ProgressBar pb;
    @Bind(R.id.parent_layout)
    public RelativeLayout videoLayout;
    @Bind(R.id.start_imageView)
    public ImageView ivStart;
    @Bind(R.id.v_back)
    public ImageView videoBack;
    @Bind(R.id.teacher_video)
    public RelativeLayout teacher_video;

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

    private String groupId,videoId;
    private Animation animation;
    private SweetAlertDialog dialog;
    private VideoCommentPrenenter mReplyPrerenter;//评论
    private VideoCommonListPresenter commentListPresenter;//评论列表
    private DBservice service;
    private UnitVideo uVideo;
    public Zan zan;
    public BaseUnitData tUnitData;
    private String teacherVideoUrl;
    private boolean likekey = false;
    private boolean losekey = false;
    private boolean flag = false, isCache;
    private VolleyUtils<BaseUnit> volley;
    private VolleyUtils<Praise> volleyPraise;
    private Handler handler = new Handler();

    private long playTime;
    private MyDownLoadCommons downloadPresent;

    private int type;//1 课程包 4 视频
    private String videourl,videoname,teacherurl,teachername;
    private MyVideoListPersoner videosPersoner;
    private String taskId,teachedId,classId;
    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    @Override
    public int setContentViewResId() {
        // TODO Auto-generated method stub
        return R.layout.activity_course_video1;

    }
    @Override
    public void onCreateBefore(Bundle savedInstanceState) {
        super.onCreateBefore(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    @Override
    public void initView() {
        // TODO Auto-generated method stub
        scrollView.scrollTo(0,0);
//        scrollView.smoothScrollTo(0,0);
//        mGvChapter.setFocusable(false);
//        lvComment.setFocusable(false);
        teacher_video.setVisibility(View.GONE);
        tintManager.setStatusBarTintResource(R.color.black);
        animation = AnimationUtils.loadAnimation(this, R.anim.applaud_animation);
//        setVideoWithAndHeight(Configuration.ORIENTATION_PORTRAIT);
        mVideoView.setVideoLoadingPb(pb);
        mVideoView.setOnCompletionListener(this);
        mVideoView.getMediaController().setFullScreenVisivility(false);
        // mVideoView.getMediaController().setOnShareListener(this);
        ivForward.setVisibility(View.GONE);
        mVideoView.setLock(false);
        ivStart.setImageResource(R.drawable.video_start_play_kaishi);

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
        scrollView.scrollTo(0,0);
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
        taskId = getIntent().getExtras().getString("TASKID");
        teachedId = getIntent().getExtras().getString("TEACHERID");
        classId = getIntent().getExtras().getString("GROUPID");

        type = getIntent().getExtras().getInt("TYPE", 4);
        if(type == 1){
            groupId = (String) getIntent().getExtras().getSerializable("ID");
        }else{
            ivLike.setVisibility(View.INVISIBLE);
            tvLikeCount.setVisibility(View.INVISIBLE);
            videoId = (String) getIntent().getExtras().getSerializable("ID");
            chapterslayout.setVisibility(View.GONE);
        }
        mVideoView.setActivity(this, Configuration.ORIENTATION_PORTRAIT);
        initStartPlay();
    }

    private void initStartPlay() {
        // TODO Auto-generated method stub
        getVideoData();
        if(type == 4){
//			getCacheData(videoId);
        }else{
            // 视频播放 选集集合
            videosPersoner = new MyVideoListPersoner(this, groupId,"46",null,null, mGvChapter);
            videosPersoner.setiVideolistener(this);

            scrollView.scrollTo(0,0);
        }

    }

    private void getCacheData(String vid) {
        if(videoId != null) {
            DownloadInfo info = service.getLoadInfo(vid);
            if (info != null) {
                String encryptionid = info.getVid();
                if (encryptionid != null) {// 从下载列表进入
                    isCache = true;
                    ivStart.setVisibility(View.GONE);
                    videoBack.setVisibility(View.GONE);
                    mVideoView.getMediaController().setFileName(info.getTitle());
                    mVideoView.setVid(encryptionid, info.getBitrate());
                    mVideoView.start();
                }
            }
//		startPlay(tUnitData);
        }
    }

    // 获取视频详情
    private void getVideoData() {
        Log.e("=====getVideoData","getVideoData");
        // TODO Auto-generated method stub
        if (volley == null) {
            volley = new VolleyUtils<BaseUnit>(this,BaseUnit.class);
        }
        Uri.Builder builder = volley.getBuilder(ConstantUrl.VKOIP.concat("/play/schoolVideo"));
        builder.appendQueryParameter("type", type+"");
        if(type == 1){
            builder.appendQueryParameter("groupId", groupId);
        }else{
            builder.appendQueryParameter("videoId", videoId);
        }
        builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken());
        volley.sendGETRequest(false,builder);
        volley.setUiDataListener(new UIDataListener<BaseUnit>() {
            @Override
            public void onDataChanged(BaseUnit response) {
                videosPersoner.setSwitch(true);
                if (response != null && response.getData() != null) {
                    initVideoView(response.getData());
                    scrollView.scrollTo(0,0);
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
                videosPersoner.setSwitch(true);
            }
        });
    }

    public void initVideoView(BaseUnitData t) {
        Log.e("=====initVideoView","initVideoView");
        if(this.isFinishing() || t == null) return;
        this.tUnitData = t;
        uVideo = t.getVideo();
        zan = t.getZan();
        if (uVideo != null) {
            if (TextUtils.isEmpty(uVideo.getSchool())) {
                tvSchoolName.setText(uVideo.getSchool());
            }
            videoId = uVideo.getVideoId();
            videoname = uVideo.getVideoName();
            videourl = uVideo.getVideoUrl();
            teacherVideoUrl = uVideo.getTVideo();
            teacherurl = uVideo.getTsface();
            teachername = uVideo.getTName();
            if(commentListPresenter == null){
                // 获取评论列表
                commentListPresenter = new VideoCommonListPresenter(this,videoId , lvComment, mPullToRefreshView, svLoad);
                scrollView.scrollTo(0,0);
            }else{
                commentListPresenter.updateData(videoId);
                scrollView.scrollTo(0,0);
            }
            if(mReplyPrerenter == null){
                // 视频评论
                mReplyPrerenter = new VideoCommentPrenenter(etReply, ivSend, videoId,this);
                mReplyPrerenter.setOnRefreshCommentListener(this);
                scrollView.scrollTo(0,0);
            }else{

                mReplyPrerenter.updateVideoId(videoId);
                scrollView.scrollTo(0,0);
            }
        }
        if (videourl != null) {
            ImageListener imgListener = ImageLoader.getImageListener(videoBack, 0, 0);
            VolleyQueueController.getInstance().getImageLoader().get(videourl, imgListener);
        }
        if (teacherurl != null) {
            ImageListener imgListener = ImageLoader.getImageListener(ivTeacherAvatar, 0, 0);
            VolleyQueueController.getInstance().getImageLoader().get(teacherurl, imgListener);
        }
        tvTeacherName.setText(teachername);
        tvVideonName.setText(videoname);
        ZanAndStep(zan);
        // 收藏视频
        new MyCollectVideoPersoner(this, tUnitData, ivCollect);
        mVideoView.getMediaController().setFileName(videoname);
        if (flag && !isCache) {
            startVideo();
            scrollView.scrollTo(0,0);
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
        startVideo();
    }

    private void startVideo() {
        Log.e("=====startVideo","startVideo");
        scrollView.scrollTo(0,0);
        getCacheData(videoId);
        if(tUnitData == null){
            Toast.makeText(this, "正在获取视频信息，稍等...", Toast.LENGTH_SHORT).show();
            // 重新获取
            getVideoData();
        }else if(!isCache){
            startPlay(tUnitData);
        }
    }

    protected void startPlay(BaseUnitData t) {

        if (t!= null && t.isAuth() ) {//&& type != 4
            ivStart.setVisibility(View.GONE);
            videoBack.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
            palyAuthority(t.getVideo().getPlay());
        }
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
                scrollView.scrollTo(0,0);
            }
        } else {
            if (mVideoView != null) {
                try {
                    mVideoView.setVid(videoId);
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
                    Intent intent = new Intent(GroupQinkeVideoActivity.this,
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
        scrollView.scrollTo(0,0);
    }

    @Override
    public void getVideoList(VideoSet videoSet) {
        // TODO Auto-generated method stub
        SubmitCourseProgress(mVideoView.getPlayTimes(),false);
        videosPersoner.setSwitch(false);
        likekey = false;
        losekey = false;
        pb.setVisibility(View.VISIBLE);
        mVideoView.stopPlayback();
        mVideoView.seekToByProgreww(0);
        tUnitData = null;
        isCache = false;
        videoId = videoSet.getVideoId();
        getCacheData(videoId);
        getVideoData();
		/*// 视频评论
		mReplyPrerenter.updateVideoId(videoId);
		// 评论列表
		commentListPresenter.updateData(videoId);*/
        scrollView.scrollTo(0,0);
    }

    private void SubmitCourseProgress(long playTime,boolean isPause) {

        if(isPause && type == 1 && mVideoView.getDuration()*4/5>= playTime) {
            UpdatefinishTaskState();
        }
        if(isPause && type == 4 && playTime >1000){
            UpdatefinishTaskState();
        }
    }

    private void UpdatefinishTaskState() {
        VolleyUtils<BaseResultInfo> volley = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
        Uri.Builder builder = volley.getBuilder("http://api.vko.cn/groups/finishGroupTask");
        builder.appendQueryParameter("token",VkoContext.getInstance(this).getToken());
        builder.appendQueryParameter("taskId",taskId);
        builder.appendQueryParameter("groupId",classId);
        builder.appendQueryParameter("teacherId",teachedId);
        volley.sendGETRequest(false,builder);

        scrollView.scrollTo(0,0);
    }

    @OnClick(R.id.tv_wedge)
    public void reportWedge() {//报卡

    }

    @OnClick(R.id.iv_download)
    public void download() {
        if (tUnitData != null) {
            if(downloadPresent == null){
                downloadPresent = new MyDownLoadCommons(this);
            }
            downloadPresent.loadVideo(tUnitData);
        }
    }

    @OnClick(R.id.et_text)
    public void eTtextOnclick() {
        ivSend.setVisibility(View.VISIBLE);
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

        scrollView.scrollTo(0,0);
    }

    /**
     * @param opt
     */
    private void sendPraise(String opt) {
        String commenturl = ConstantUrl.VKOIP.concat("/course/packetZan/");
        if (volleyPraise == null) {
            volleyPraise = new VolleyUtils<Praise>(this,Praise.class);
        }
        final Uri.Builder builder = volleyPraise.getBuilder(commenturl);
        builder.appendQueryParameter("videoId", videoId);
        builder.appendQueryParameter("opt", opt);
        builder.appendQueryParameter("type", type ==4 ?"1":"7");
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
    @Override
    protected void onPause() {
        if (playTime == 0) {
            playTime = mVideoView.getPlayTimes();
        }
        mVideoView.pause();
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
            if (!mVideoView.getMediaController().isScreenPortrait()) {// 是否竖屏
                if (mVideoView.getMediaController().isSwitchBoard) {
                    mVideoView.onLandscape();
                    return false;
                }
            }
            playTime = mVideoView.getCurrentPosition();
            mVideoView.stopPlayback();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void onConfigurationChanged(Configuration arg0) {
//        // TODO Auto-generated method stub
////		mVideoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
//        super.onConfigurationChanged(arg0);
////		setVideoWithAndHeight(arg0.orientation);
//        /** 12.21添加，通知首页的推荐知识点刷新，解决布局异常问题，JiaRH */
//        EventBus.getDefault().post(Constants.RECOMMOND_REFRESH);
//        scrollView.scrollTo(0,0);
//    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mVideoView != null){
//			mVideoView.stopPlayback();
            mVideoView.unregisterReceiver();
        }
    }
}
