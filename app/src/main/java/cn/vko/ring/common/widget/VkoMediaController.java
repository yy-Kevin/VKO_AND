package cn.vko.ring.common.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.easefun.polyvsdk.BitRateEnum;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.ijk.IjkBaseMediaController;

import java.util.List;
import java.util.Locale;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import tv.danmaku.ijk.media.widget.OutlineTextView;

/**
 * @author shikh
 * 
 */
public class VkoMediaController extends IjkBaseMediaController {
	private MediaPlayerControl mPlayer;
	private Context mContext;
	private PopupWindow mWindow;
	private int mAnimStyle;
	private View mAnchor;
	private View mRoot;
	private ImageButton fullScreen;
	private RelativeLayout topBar,footBar;
	public static ProgressBar mProgress;
	private TextView mEndTime;
	private TextView mCurrentTime;
	private TextView mFileName;
	private OutlineTextView mInfoView;
	private String mTitle;
	private long mDuration;
	private boolean mShowing;
	private boolean mDragging;
	private boolean mInstantSeeking = true;
	private static final int sDefaultTimeout = 3000;
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;
	private boolean mFromXml = false;
	private ImageButton mPauseButton;
	private ImageButton mFfwdButton;
	private ImageButton mRewButton;
	private ImageButton mNextButton;
	private ImageButton mPreButton;

	private LinearLayout bitrateLinearLayout = null;
	private SparseArray<Button> bitRateBtnArray = null;

	private AudioManager mAM;
	private boolean isUsePreNext = false;
	private boolean isVisibility = true;
	private boolean isVisib = true;
	private boolean isVisiTime = true;
	private boolean isFullScreen = true;
	public boolean isSwitchBoard = true;

	private OnBoardChangeListener onBoardChangeListener;
	private OnVideoChangeListener onVideoChangeListener;
	private OnBackListener onBackListener;
	private OnShownListener mShownListener;
	private OnHiddenListener mHiddenListener;
	private OnSwitchLevelListener onSwitchLevellistener;
	private OnShareListener onSharelistener;

	private ImageButton btnBack;
	private ImageButton btnBoardChange;
	private ImageButton videoChange;
	private TextView tvRate;// 码率   标清 高请之类的
	private TextView tvShare;
	private OnPreNextListener onPreNextListener;

	private String level = "流畅";
	private float lastX = 0, lastY = 0;
	private AudioManager mAudio = null; // 音频管理器
	private int currentVolume = 0;// 当前音亮
	private int maxVolume = 0; // 最大音亮
	/** 当前亮度 */
	private float mBrightness = -1f;
//	private VideoLevelPop mPop;
	private String vid;
	private boolean isLevel;

	private OnClickListener mPreListener = new OnClickListener()
	{

		public void onClick(View arg0)
		{
			if (VkoMediaController.this.onPreNextListener != null)
				VkoMediaController.this.onPreNextListener.onPreviou();
		}
	};

	private OnClickListener mNextListener = new OnClickListener()
	{

		public void onClick(View arg0)
		{

			if (VkoMediaController.this.onPreNextListener != null)
				VkoMediaController.this.onPreNextListener.onNext();
		}
	};

	private OnClickListener mVideoListener = new OnClickListener()
	{

		public void onClick(View v)
		{

			if (v.getTag().equals("0")) {
				v.setTag("1");
				if (VkoMediaController.this.onVideoChangeListener != null)
					VkoMediaController.this.onVideoChangeListener.onVideoChange(0);
			}
			else if (v.getTag().equals("1")) {
				v.setTag("2");
				if (VkoMediaController.this.onVideoChangeListener != null)
					VkoMediaController.this.onVideoChangeListener.onVideoChange(1);
			}
			else if (v.getTag().equals("2")) {
				v.setTag("3");
				if (VkoMediaController.this.onVideoChangeListener != null)
					VkoMediaController.this.onVideoChangeListener.onVideoChange(2);
			}
			else if (v.getTag().equals("3")) {
				v.setTag("0");
				if (VkoMediaController.this.onVideoChangeListener != null)
					VkoMediaController.this.onVideoChangeListener.onVideoChange(3);
			}
		}
	};

	private OnClickListener mBackListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			if(VkoMediaController.this.onBackListener != null){
				
				VkoMediaController.this.onBackListener.onBack();
			}
		}
	};
	
	private OnClickListener mSwitchLevel = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			VkoMediaController.this.mHandler.removeMessages(1);
			VkoMediaController.this.mHandler.sendMessageDelayed(VkoMediaController.this.mHandler.obtainMessage(FADE_OUT),sDefaultTimeout);
//			if (mPop == null) {
//				try{
//					mPop = new VideoLevelPop(mContext);
//					mPop.update(0, 0, 300, LayoutParams.WRAP_CONTENT);
//				}catch(Exception e){
//
//				}
//			}
//			mPop.setListData(levelList);
//			if(VkoMediaController.this.onSwitchLevellistener != null){
//				VkoMediaController.this.onSwitchLevellistener.onLevel(mPop,footBar,vid);
//			}
			if (bitrateLinearLayout.getVisibility() == View.INVISIBLE) {
				bitrateLinearLayout.setVisibility(View.VISIBLE);
			} else {
				bitrateLinearLayout.setVisibility(View.INVISIBLE);
			}
		}
	};
	
	private OnClickListener mShareListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		
			// TODO Auto-generated method stub
			if(VkoMediaController.this.onSharelistener != null){
				VkoMediaController.this.onSharelistener.onShare();
			}
		}
	};
	
	private OnClickListener mBoardListener = new OnClickListener()
	{

		public void onClick(View v)
		{

			if (VkoMediaController.this.isScreenPortrait()) {
				if (VkoMediaController.this.onBoardChangeListener != null)
					VkoMediaController.this.onBoardChangeListener.onPortrait();
			}
			else if (VkoMediaController.this.onBoardChangeListener != null)
				VkoMediaController.this.onBoardChangeListener.onLandscape();
		}
	};

	private OnClickListener mFfwdListener = new OnClickListener()
	{

		public void onClick(View arg0)
		{

			int pos = VkoMediaController.this.mPlayer.getCurrentPosition();
			pos += 5000;
			VkoMediaController.this.mPlayer.seekTo(pos);
			VkoMediaController.this.setProgress();

			VkoMediaController.this.show(sDefaultTimeout);
		}
	};

	private OnClickListener mRewListener = new OnClickListener()
	{

		public void onClick(View arg0)
		{

			// Log.v("MediaController", "into the mRew button");
			int pos = VkoMediaController.this.mPlayer.getCurrentPosition();
			pos -= 5000;
			VkoMediaController.this.mPlayer.seekTo(pos);
			VkoMediaController.this.setProgress();
			VkoMediaController.this.show(sDefaultTimeout);
		}
	};

	@SuppressLint({ "HandlerLeak" })
	private Handler mHandler = new Handler()
	{

		public void handleMessage(Message msg)
		{

			switch (msg.what) {
			case FADE_OUT:
				VkoMediaController.this.hide();
				break;
			case SHOW_PROGRESS:
				long pos = VkoMediaController.this.setProgress();
				if ((!VkoMediaController.this.mDragging) && (VkoMediaController.this.mShowing)) {
					msg = obtainMessage(SHOW_PROGRESS);
					sendMessageDelayed(msg, 1000L - pos % 1000L);
					VkoMediaController.this.updatePausePlay();
				}
				break;
			}
		}
	};

	private OnClickListener mPauseListener = new OnClickListener() {

		public void onClick(View v) {

			VkoMediaController.this.doPauseResume();
			VkoMediaController.this.show(sDefaultTimeout);
		}
	};

	public ImageButton getStartButton() {
		return mPauseButton;
	}
	
	private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {

		public void onStartTrackingTouch(SeekBar bar) {

			VkoMediaController.this.mDragging = true;
			VkoMediaController.this.show(3600000);
			VkoMediaController.this.mHandler.removeMessages(SHOW_PROGRESS);
			if (VkoMediaController.this.mInstantSeeking)
				VkoMediaController.this.mAM.setStreamMute(3, true);
			if (VkoMediaController.this.mInfoView != null) {
				VkoMediaController.this.mInfoView.setText("");
				VkoMediaController.this.mInfoView.setVisibility(View.VISIBLE);
			}
		}

		public void onProgressChanged(SeekBar bar, int progress, boolean fromuser)
		{

			if (!fromuser) {
				return;
			}
			long newposition = VkoMediaController.this.mDuration * progress / 1000L;
			String time = VkoMediaController.generateTime(newposition);
			if (VkoMediaController.this.mInstantSeeking && VkoMediaController.this.mPlayer != null)
				VkoMediaController.this.mPlayer.seekTo(newposition);
			if (VkoMediaController.this.mInfoView != null)
				VkoMediaController.this.mInfoView.setText(time);
			if (VkoMediaController.this.mCurrentTime != null)
				VkoMediaController.this.mCurrentTime.setText(time);
		}

		public void onStopTrackingTouch(SeekBar bar) {

			if (!VkoMediaController.this.mInstantSeeking)
				VkoMediaController.this.mPlayer.seekTo(VkoMediaController.this.mDuration * bar.getProgress() / 1000L);
			if (VkoMediaController.this.mInfoView != null) {
				VkoMediaController.this.mInfoView.setText("");
				VkoMediaController.this.mInfoView.setVisibility(View.GONE);
			}
			VkoMediaController.this.show(sDefaultTimeout);
			VkoMediaController.this.mHandler.removeMessages(SHOW_PROGRESS);
			VkoMediaController.this.mAM.setStreamMute(3, false);
			VkoMediaController.this.mDragging = false;
			VkoMediaController.this.mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000L);
		}
	};

	public void setOnShareListener(OnShareListener l){
		this.onSharelistener = l;
	}
	public void setOnSwitchLevelListener(OnSwitchLevelListener l) {

		this.onSwitchLevellistener = l;
	}

	public void setOnBoardChangeListener(OnBoardChangeListener l)
	{

		this.onBoardChangeListener = l;
	}

	public void setOnVideoChangeListener(OnVideoChangeListener l) {

		this.onVideoChangeListener = l;
	}

	public void setOnPreNextListener(OnPreNextListener l) {

		this.onPreNextListener = l;
	}

	public boolean isScreenPortrait()
	{

		return this.mContext.getResources().getConfiguration().orientation == 1;
	}

	public void setInstantSeeking(boolean seekWhenDragging)
	{

		this.mInstantSeeking = seekWhenDragging;
	}

	/**
	 * 
	 * @param context
	 * @param isUsePreNext
	 *            当你不需要实现上一集下一集按钮时，设置isUsePreNext 为false，需要时设为true
	 *            并实现setPreNextListener()方法
	 */
	public VkoMediaController(Context context, boolean isUsePreNext) {
		super(context);

		if ((!this.mFromXml) && (initController(context)))
			initFloatingWindow();
		this.isUsePreNext = isUsePreNext;
		initVolume(context);
	}

	private void initVolume(Context context) {
		// TODO Auto-generated method stub
		// 得到音频管理器
		mAudio = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
		// 获得最大音亮
		maxVolume = mAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// 获得当前音亮
		currentVolume = mAudio.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	private boolean initController(Context context) {
		this.mContext = context;
		this.mAM = ((AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE));
		return true;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (this.mRoot != null) initControllerView(this.mRoot);
	}

	private void initFloatingWindow() {

		this.mWindow = new PopupWindow(this.mContext);
		this.mWindow.setFocusable(false);
		this.mWindow.setTouchable(true);
		this.mWindow.setBackgroundDrawable(null);
		this.mWindow.setOutsideTouchable(true);
		this.mAnimStyle = android.R.style.Animation;
	}

	public View getPopupWindow()
	{

		return this.mWindow.getContentView();
	}

	public void setOnBackListener(OnBackListener l) {

		this.onBackListener = l;
	}


	@Override
	public void hide() {

		// TODO Auto-generated method stub
		if (this.mAnchor == null) {
			return;
		}
		
		if (this.mShowing)
		{
			try
			{
//				if(mPop != null){
//					mPop.dismiss();
//				}
				this.mHandler.removeMessages(SHOW_PROGRESS);
				if (this.mFromXml)
					setVisibility(View.GONE);
				else
					this.mWindow.dismiss();
			} catch (IllegalArgumentException ex) {
				// Log.d("IjkMediaController",
				// "MediaController already removed");
			}
			this.mShowing = false;
			if (this.mHiddenListener != null)
				this.mHiddenListener.onHidden();
		}
	}

	@SuppressLint("WrongViewCast")
	@Override
	protected void initControllerView(View v) {
		// TODO Auto-generated method stub
		this.mPauseButton = ((ImageButton) v.findViewById(R.id.mediacontroller_play_pause));
		if (this.mPauseButton != null) {
			this.mPauseButton.requestFocus();
			this.mPauseButton.setOnClickListener(this.mPauseListener);
		}

		this.mFfwdButton = ((ImageButton) v.findViewById(R.id.ffwd));
		this.mFfwdButton.setOnClickListener(this.mFfwdListener);

		this.mRewButton = ((ImageButton) v.findViewById(R.id.rew));
		this.mRewButton.setOnClickListener(this.mRewListener);

		this.btnBoardChange = ((ImageButton) v.findViewById( R.id.landscape));
		this.btnBoardChange.setOnClickListener(this.mBoardListener);
		this.btnBoardChange.setVisibility(isSwitchBoard ?View.VISIBLE:View.GONE);

		this.tvRate = (TextView) v.findViewById(R.id.swtichlevel);
		this.tvRate.setOnClickListener(mSwitchLevel);
		/*if(!TextUtils.isEmpty(level)){
			this.tvRate.setText(level);
		}*/
		this.tvShare = (TextView) v.findViewById(R.id.share_tv);
		this.tvShare.setOnClickListener(mShareListener);
		
		this.videoChange = (ImageButton) v.findViewById(R.id.videochange);
		this.videoChange.setTag("0");
		this.videoChange.setOnClickListener(this.mVideoListener);

		this.mPreButton = ((ImageButton) v.findViewById(R.id.prev));
		this.mNextButton = ((ImageButton) v.findViewById(R.id.next));
		if (this.isUsePreNext) {
			this.mPreButton.setVisibility(View.VISIBLE);
			this.mNextButton.setVisibility(View.VISIBLE);
		}
		this.mPreButton.setOnClickListener(this.mPreListener);
		this.mNextButton.setOnClickListener(this.mNextListener);

		this.mProgress = ((ProgressBar) v.findViewById(R.id.mediacontroller_seekbar));
		setProgressVisibility(isVisib);
		if (this.mProgress != null) {
			if ((this.mProgress instanceof SeekBar)) {
				SeekBar seeker = (SeekBar) this.mProgress;
				seeker.setOnSeekBarChangeListener(this.mSeekListener);
				seeker.setClickable(false);
				seeker.setThumbOffset(1);
			}
			this.mProgress.setMax(1000);
		}
		this.btnBack = (ImageButton) v.findViewById(R.id.btn_back);
		this.btnBack.setOnClickListener(mBackListener);
		this.mEndTime = ((TextView) v.findViewById(R.id.mediacontroller_time_total));
		setEndTimeVisibility(isVisiTime);
		this.fullScreen= (ImageButton) v.findViewById(R.id.landscape);
		setFullScreenVisivility(isFullScreen);
		this.mCurrentTime = ((TextView) v.findViewById(R.id.mediacontroller_time_current));
		this.mFileName = (TextView) v.findViewById(R.id.tv_video_title);
		this.mFileName.setText(mTitle);
		this.topBar = (RelativeLayout) v.findViewById(R.id.topbar);
//		this.topBar.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
		setTopBarVisibility(isVisibility);
		this.footBar = (RelativeLayout) v.findViewById(R.id.bot);

		bitrateLinearLayout = (LinearLayout) mRoot.findViewById(R.id.bitrate_linear_layout);

		bitRateBtnArray = new SparseArray<Button>();
		Button liuchangBtn = (Button) mRoot.findViewById(R.id.liuchang);
		liuchangBtn.setText(BitRateEnum.liuChang.getName());
		bitRateBtnArray.append(BitRateEnum.liuChang.getNum(), liuchangBtn);

		Button gaoqingBtn = (Button) mRoot.findViewById(R.id.gaoqing);
		gaoqingBtn.setText(BitRateEnum.gaoQing.getName());
		bitRateBtnArray.append(BitRateEnum.gaoQing.getNum(), gaoqingBtn);

		Button chaoqingBtn = (Button) mRoot.findViewById(R.id.chaoqing);
		chaoqingBtn.setText(BitRateEnum.chaoQing.getName());
		bitRateBtnArray.append(BitRateEnum.chaoQing.getNum(), chaoqingBtn);
	}

	@Override
	public boolean isShowing() {
		// TODO Auto-generated method stub
		return this.mShowing;
	}

	@Override
	protected View makeControllerView() {
		// TODO Auto-generated method stub
		LayoutInflater mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(mInflater != null) this.mRoot = mInflater.inflate(R.layout.ijkmedia_controller,null);
		return this.mRoot;
	}

	@Override
	public void setAnchorView(View view) {
		// TODO Auto-generated method stub
		if(!isLevel){
			this.mAnchor = view;
			if (!this.mFromXml) {
				removeAllViews();
				this.mRoot = makeControllerView();
				this.mWindow.setContentView(this.mRoot);
				this.mWindow.setWidth(this.mAnchor.getWidth());
				this.mWindow.setHeight(this.mAnchor.getHeight());
			}
			initControllerView(this.mRoot);
		}else{
			isLevel = false;
		}

	}

	@Override
	public void setMediaPlayer(MediaPlayerControl player) {
		// TODO Auto-generated method stub
		this.mPlayer = player;
		updatePausePlay();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		show(sDefaultTimeout);
	}

	@Override
	public void show(int timeout) {
		// TODO Auto-generated method stub
		if ((!this.mShowing) && (this.mAnchor != null) && (this.mAnchor.getWindowToken() != null))
		{
			if (this.mPauseButton != null)
				this.mPauseButton.requestFocus();
			disableUnsupportedButtons();
			if (this.mFromXml) {
				setVisibility(View.VISIBLE);
			} else {
				int[] location = new int[2];

				this.mAnchor.getLocationInWindow(location);
				Rect anchorRect = new Rect(location[0], location[1],
						location[0] + this.mAnchor.getWidth(), location[1] +
								this.mAnchor.getHeight());
				this.mWindow.setWidth(this.mAnchor.getWidth());
				this.mWindow.setHeight(this.mAnchor.getHeight());
				this.mWindow.setAnimationStyle(this.mAnimStyle);
				this.mWindow.showAtLocation(this.mAnchor, 0, 0, anchorRect.top);
			}
			this.mShowing = true;
			if (this.mShownListener != null)
				this.mShownListener.onShown();
		}
		updatePausePlay();
		this.mHandler.sendEmptyMessage(SHOW_PROGRESS);

		if (timeout != 0) {
			this.mHandler.removeMessages(1);
			this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(FADE_OUT),
					timeout);
		}
	}

	/**
	 * 是否可切换横竖屏
	 * 
	 * @param isSwitchBoard
	 */
	public void setHasSwitchBoard(boolean isSwitchBoard) {
		this.isSwitchBoard = isSwitchBoard;
		if (!isSwitchBoard) {
			this.btnBoardChange.setVisibility(View.GONE);
		} else {
			this.btnBoardChange.setVisibility(View.VISIBLE);
		}
	}

	public void switchBoardIcon(int orientation){
		if (orientation == Configuration.ORIENTATION_PORTRAIT){// 坚屏
			btnBoardChange.setImageResource(R.drawable.class_video_s_landscape);
		}else if (orientation == Configuration.ORIENTATION_LANDSCAPE){// 横屏
			btnBoardChange.setImageResource(R.drawable.class_video_s_portrait);
		}
		
	}
	public void setFileName(String name)
	{
		this.mTitle = name;
		if (this.mFileName != null){
			this.mFileName.setText(this.mTitle);
		}
	}
	
//	public void setLevelName(String name){
//		this.level = name;
//	}

	public void seekTo(long playTime){
		if(this.mPlayer != null){
			mPlayer.seekTo(playTime);
		}
	}
	
	public void setTopBarVisibility(boolean isVisibility){
		this.isVisibility = isVisibility;
		if(this.topBar != null){
//			this.topBar.setVisibility(isVisibility? View.VISIBLE : View.GONE);
			if(!isVisibility){
				this.topBar.setBackgroundDrawable((mContext.getResources().getDrawable(R.drawable.transparent)));
				this.mFileName.setVisibility(View.GONE);
			}else{
				this.topBar.setBackgroundDrawable((mContext.getResources().getDrawable(R.drawable.bg_controll)));
				this.mFileName.setVisibility(View.VISIBLE);
			}
		}
	}
	//是否显示快进快退
	public void setProgressVisibility(boolean isVisib){
		this.isVisib = isVisib;
			if(!isVisib){
				this.mProgress.setVisibility(View.INVISIBLE);
				this.mPauseButton.setVisibility(View.INVISIBLE);
//				this.mEndTime.setVisibility(View.INVISIBLE);
			}else{
				this.mProgress.setVisibility(View.VISIBLE);
				this.mPauseButton.setVisibility(View.VISIBLE);
//				this.mEndTime.setVisibility(View.VISIBLE);
			}
	}

	//是否显示快进快退
	public void setEndTimeVisibility(boolean isVisiTime){
		this.isVisiTime = isVisiTime;
		if(!isVisiTime){
				this.mEndTime.setVisibility(View.INVISIBLE);
		}else{
				this.mEndTime.setVisibility(View.VISIBLE);
		}
	}
	//是否显示全屏
	public void setFullScreenVisivility(boolean isFullScreen){
		this.isFullScreen = isFullScreen;
		if(!isFullScreen){
				this.fullScreen.setVisibility(View.INVISIBLE);
		}else{
				this.fullScreen.setVisibility(View.VISIBLE);
		}
	}


	private void disableUnsupportedButtons() {

		try {
			if (mPauseButton != null && mPlayer != null&& !mPlayer.canPause()){
				mPauseButton.setEnabled(false);
			}
		} catch (Exception ex) {
		}
	}

	private long setProgress()
	{

		if ((this.mPlayer == null) || (this.mDragging)) {
			return 0L;
		}
		int position = this.mPlayer.getCurrentPosition();
		int duration = this.mPlayer.getDuration();
		if (this.mProgress != null) {
			if (duration > 0) {
				long pos = 1000L * position / duration;
				this.mProgress.setProgress((int) pos);
			}
			int percent = this.mPlayer.getBufferPercentage();
			this.mProgress.setSecondaryProgress(percent * 10);
		}

		this.mDuration = duration;

		if (this.mEndTime != null)
			this.mEndTime.setText(generateTime(this.mDuration));
		if (this.mCurrentTime != null) {
			this.mCurrentTime.setText(generateTime(position));
		}
		return position;
	}

	private static String generateTime(long position) {

		int totalSeconds = (int) (position / 1000L);

		int seconds = totalSeconds % 60;
		int minutes = totalSeconds / 60 % 60;
		int hours = totalSeconds / 3600;

		if (hours > 0) {
			return String.format(Locale.US, "%02d:%02d:%02d",
					new Object[] { Integer.valueOf(hours), Integer.valueOf(minutes),
							Integer.valueOf(seconds) }).toString();
		}
		return String.format(Locale.US, "%02d:%02d", new Object[] { Integer.valueOf(minutes), Integer.valueOf(seconds) })
				.toString();
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		onVoiceBrightTouchEvent(event);
//		show(sDefaultTimeout);
		return true;
	}

	public boolean onTrackballEvent(MotionEvent ev)
	{
//		show(sDefaultTimeout);
		onVoiceBrightTouchEvent(ev);
		return false;
	}

	public boolean dispatchKeyEvent(KeyEvent event)
	{

		int keyCode = event.getKeyCode();
		if ((event.getRepeatCount() == 0) && (
				(keyCode == 79) ||
						(keyCode == 85) || (keyCode == 62))) {
			doPauseResume();
			show(sDefaultTimeout);
			if (this.mPauseButton != null)
				this.mPauseButton.requestFocus();
			return true;
		}
		if (keyCode == 86) {
			if (this.mPlayer.isPlaying()) {
				this.mPlayer.pause();
				updatePausePlay();
			}
			return true;
		}
		if ((keyCode == 4) ||
				(keyCode == 82)) {
			hide();
			return true;
		}
		show(sDefaultTimeout);

		return super.dispatchKeyEvent(event);
	}

	private void updatePausePlay()
	{

		if ((this.mRoot == null) || (this.mPauseButton == null)) {
			return;
		}
		if (this.mPlayer != null && this.mPlayer.isPlaying()) {
			// Log.i("IjkMediaController", "change to pause");
			this.mPauseButton
					.setImageResource(R.drawable.class_video_s_pause);
		}
		else {
			this.mPauseButton
					.setImageResource(R.drawable.class_video_s_play);
		}
	}

	private void doPauseResume() {
		if(this.mPlayer != null){
			if (this.mPlayer.isPlaying())
				this.mPlayer.pause();
			else
				this.mPlayer.start();
			updatePausePlay();
		}
	}

	public void setEnabled(boolean enabled)
	{
		if (this.mPauseButton != null)
			this.mPauseButton.setEnabled(enabled);
		if (this.mProgress != null)
			this.mProgress.setEnabled(enabled);
		disableUnsupportedButtons();
		super.setEnabled(enabled);
	}

	public int getResourseIdByName(String className, String name) {

		Class r = null;
		int id = 0;
		try {
			r = Class.forName(this.mContext.getPackageName() + ".R");

			Class[] classes = r.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; i++) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}

			if (desireClass != null)
				id = desireClass.getField(name).getInt(desireClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return id;
	}

	private long thisEventTime, lastDownTime;
	int x = 0; // 触碰的坐标
	int y = 0;

	public void onVoiceBrightTouchEvent(MotionEvent event) {
//		if(mPop != null){
//			mPop.dismiss();
//		}
		float[] temp = new float[] { 0, 0 };
		float[] upxy = new float[] { 0, 0 };

		final double FLING_MIN_DISTANCE = 20.0;// 最小距离
		final double FLING_MIN_VELOCITY = 10.0;// 最小
		int eventAction = event.getAction();

		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:
			lastX = x = (int) event.getX(); // 触碰的坐标
			lastY = y = (int) event.getY();
			lastDownTime = event.getDownTime();
			break;
		case MotionEvent.ACTION_MOVE:
			temp[0] = (int) event.getX();
			temp[1] = (int) event.getY();

			upxy[0] = Math.abs(lastX - temp[0]);
			upxy[1] = Math.abs(lastY - temp[1]);
			if (lastX > ViewUtils.getScreenWidth(mContext) / 2)// 右边滑动
				setVoice(temp, upxy, FLING_MIN_DISTANCE);
			else if (lastX < ViewUtils.getScreenWidth(mContext) / 2)// 左边滑动
				/*setBright((lastY - temp[1])
						/ ViewUtils.getScreenHeight(mContext));*/
			break;
		case MotionEvent.ACTION_UP:
			temp[0] = (int) event.getX();
			temp[1] = (int) event.getY();
			thisEventTime = event.getEventTime();
			if (!isLongPressed(lastDownTime, thisEventTime, 500)
					&& Math.abs(temp[0] - x) <= 10
					&& Math.abs(temp[1] - y) <= 10) {// 单击
				if (isShowing()) {
					hide();
				} else {
					show(sDefaultTimeout);
				}
			}
			break;

		}
	}
	/**
	 * 
	 * @param lastDownTime
	 *            按下时间
	 * @param thisEventTime
	 *            移动时间
	 * @param longPressTime
	 *            判断长按时间的阀值
	 * @return
	 */
	public static boolean isLongPressed(long lastDownTime, long thisEventTime,
			long longPressTime) {

		long intervalTime = thisEventTime - lastDownTime;
		if (intervalTime >= longPressTime) {
			return true;
		}
		return false;
	}

	private void setVoice(float[] temp, float[] upxy,
			final double FLING_MIN_DISTANCE) {

		if (upxy[0] < upxy[1] && upxy[1] > FLING_MIN_DISTANCE) {
			if (lastY - temp[1] > 0) {
				currentVolume += 1;
				updateVolume(currentVolume);
				// 增加音量，调出系统音量控制
				mAudio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE,
						AudioManager.FX_FOCUS_NAVIGATION_UP);
				lastX = temp[0];
				lastY = temp[1];
			} else {
				currentVolume -= 1;
				updateVolume(currentVolume);
				// 降低音量，调出系统音量控制
				mAudio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_LOWER,
						AudioManager.FX_FOCUS_NAVIGATION_UP);
				lastX = temp[0];
				lastY = temp[1];
			}

		}
	}

	// 更新音量
	protected void updateVolume(int index) {

		// TODO Auto-generated method stub
		if (mAudio != null) {
			if (index > maxVolume) {
				index = maxVolume;
			}
			if (index < 0)
				index = 0;
			mAudio.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
		}

		currentVolume = index;
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	// private SharePop mSharePop;
	public void setBright(float percent) {
		if (mBrightness < 0) {
			mBrightness = ((Activity) mContext).getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;
		}
		WindowManager.LayoutParams lpa = ((Activity) mContext).getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		
        ((Activity) mContext).getWindow().setAttributes(lpa);  
          
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int)(mBrightness + percent)); 
	}

	/*********************************************************************************************/

	public static abstract interface OnBoardChangeListener
	{

		public abstract void onLandscape();

		public abstract void onPortrait();
	}

	public static abstract interface OnHiddenListener
	{

		public abstract void onHidden();
	}

	public static abstract interface OnPreNextListener
	{

		public abstract void onPreviou();

		public abstract void onNext();
	}

	public static abstract interface OnShownListener
	{

		public abstract void onShown();
	}

	public static abstract interface OnVideoChangeListener
	{

		public abstract void onVideoChange(int paramInt);
	}

	public static abstract interface OnBackListener
	{

		public abstract void onBack();
	}
	
	public static abstract interface OnSwitchLevelListener
	{
//		public abstract void onLevel(VideoLevelPop pop, View parent, String vid);
		public abstract void onLevel(String vid, int targetBitRate);
	}
	
	public static abstract interface OnShareListener
	{
		public abstract void onShare();
	}

	@Override
	public void setViewBitRate(String vid, int bitRate) {
		// TODO Auto-generated method stub
		this.vid = vid;
		new GetDFNumWork().execute(vid, String.valueOf(bitRate));
	}

	/**
	 * 取得dfNum 任务
	 * @author TanQu 2015-10-8
	 */
	private class GetDFNumWork extends AsyncTask<String, String, Integer> {
		private String vid = "";
		private int currBitRate = 0;

		@Override
		protected Integer doInBackground(String... params) {
			vid = params[0];
			currBitRate = Integer.parseInt(params[1]);
			if (currBitRate < 0) currBitRate = 0;
			int dfNum = PolyvSDKClient.getInstance().getVideoDBService().getDFNum(vid);
			return dfNum;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 0) return;
			tvRate.setText(BitRateEnum.getBitRateName(currBitRate));
			List<BitRateEnum> levelList = BitRateEnum.getBitRateList(result);
			Button bitRateBtn = null;
			for (BitRateEnum bitRateEnum : levelList) {
				if (bitRateEnum == BitRateEnum.ziDong) continue;
				bitRateBtn = bitRateBtnArray.get(bitRateEnum.getNum());
				bitRateBtn.setVisibility(View.VISIBLE);
				if(currBitRate == bitRateEnum.getNum()){
					bitRateBtn.setTextColor(mContext.getResources().getColor(R.color.green));
				}else{
					bitRateBtn.setTextColor(mContext.getResources().getColor(R.color.white));
				}
				bitRateBtn.setOnClickListener(new bitRateClientListener(vid, currBitRate, bitRateEnum.getNum()));
			}
		}
	}

	private class bitRateClientListener implements OnClickListener {
		private final String vid;
		private final int currBitRate;
		private final int targetBitRate;

		public bitRateClientListener(String vid, int currBitRate, int targetBitRate) {
			this.vid = vid;
			this.currBitRate = currBitRate;
			this.targetBitRate = targetBitRate;
		}

		@Override
		public void onClick(View v) {
			bitrateLinearLayout.setVisibility(View.INVISIBLE);
			if (currBitRate == targetBitRate) {
				return;
			}
			if(VkoMediaController.this.onSwitchLevellistener != null){
				isLevel = true;
				VkoMediaController.this.onSwitchLevellistener.onLevel(vid,targetBitRate);
			}
		}
	}

}
