package cn.vko.ring.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.OnPreparedListener;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.study.ScreenListenerManager;
import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * @author shikh
 * 
 */
public class VkoVideoView extends IjkVideoView implements VkoMediaController.OnVideoChangeListener,
		VkoMediaController.OnBoardChangeListener,OnPreparedListener, VkoMediaController.OnSwitchLevelListener,IjkVideoView.OnVideoPlayErrorLisener,ScreenListenerManager.IScreenStateListener {
	private VkoMediaController mediaController;
	public Context ctx;
	private ProgressBar pb;
	private Activity act;
	private ScreenListenerManager screenListener;
	public boolean isLock;
	private long playTime;
	public boolean isLock() {
		return isLock;
	}
	private String level;
	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}
	private int mVideoWidth,mVideoHeight;
	/**
	 * @param context
	 * @param attrs
	 */
	public VkoVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.ctx = context;
//		initPolyvCilent();
		initMedia();
	}

	public long getPlayTimes() {
		playTime = getCurrentPosition(); // 文件已播入的时长（毫秒）
		return playTime;
	}
	public void seekToByProgreww(long progress){
		playTime = progress;
//		mediaController.seekTo(playTime);
	}
	/**
	 * 
	 * @param act
	 * @param orientation
	 *   坚屏 1 横屏 2
	 */
	public void setActivity(Activity act, int orientation) {
		mVideoWidth = ViewUtils.getScreenWidth(act);
		this.act = act;
		mediaController.switchBoardIcon(orientation);
		if (orientation == Configuration.ORIENTATION_PORTRAIT){// 坚屏
			mediaController.setTopBarVisibility(false);
//			Log.e("=====竖屏了","----------------");
//			setVideoLayout(IjkVideoView.VIDEO_LAYOUT_ZOOM);
			float ratio = (float) 15 / 9;
			mVideoHeight = (int) Math.ceil((float) mVideoWidth / ratio);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mVideoWidth, mVideoHeight);
			setLayoutParams(lp);
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (orientation == Configuration.ORIENTATION_LANDSCAPE){// 横屏
			mediaController.setTopBarVisibility(true);
			mediaController.setHasSwitchBoard(false);
//			setVideoLayout(IjkVideoView.VIDEO_LAYOUT_ZOOM);
			Log.e("=====横屏了","----------------");
			setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
	public void setActivity1(Activity act, int orientation) {
		mVideoWidth = ViewUtils.getScreenWidth(act);
		this.act = act;
		mediaController.switchBoardIcon(orientation);
		if (orientation == Configuration.ORIENTATION_PORTRAIT){// 坚屏
			mediaController.setTopBarVisibility(false);
//			Log.e("=====竖屏了","----------------");
//			setVideoLayout(IjkVideoView.VIDEO_LAYOUT_ZOOM);
//			float ratio = (float) 15 / 9;
//			mVideoHeight = (int) Math.ceil((float) mVideoWidth / ratio);
//			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mVideoWidth, mVideoHeight);
//			setLayoutParams(lp);
//			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (orientation == Configuration.ORIENTATION_LANDSCAPE){// 横屏
			mediaController.setTopBarVisibility(true);
			mediaController.setHasSwitchBoard(false);
//			setVideoLayout(IjkVideoView.VIDEO_LAYOUT_ZOOM);
			Log.e("=====横屏了","----------------");
			setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}



	private void initMedia() {
		mediaController = new VkoMediaController(ctx, false);
		mediaController.setOnBoardChangeListener(this);
		mediaController.setOnVideoChangeListener(this);
		mediaController.setOnSwitchLevelListener(this);
		screenListener = new ScreenListenerManager(ctx);
		screenListener.begin(this);
		screenListener.setmScreenStateListener(this);
		mediaController.setAnchorView(this);
		setMediaController(mediaController);
//		setOnCompletionListener(this);
//		setVideoLayout(IjkVideoView.VIDEO_LAYOUT_ORIGIN);
		setOnPreparedListener(this);
		setMediaBufferingIndicator(pb); // 在缓冲时出现的loading
		setOnVideoPlayErrorLisener(this);
		mediaController.setOnBackListener(new VkoMediaController.OnBackListener() {
			@Override
			public void onBack() {
				// TODO Auto-generated method stub
				if (act != null) {
					if(!mediaController.isScreenPortrait()){//是否竖屏
						if(mediaController.isSwitchBoard){
							onLandscape();
							return;
						}
					}
					playTime = getCurrentPosition();
					stopPlayback();
					act.finish();
				}
			}
		});
		setOnInfoListener(new IMediaPlayer.OnInfoListener() {
			@Override
			public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
				Log.i("player","i="+i+"  i1="+i1);
				return false;
			}
		});
	}

	@Override
	public void onPrepared(IMediaPlayer iMediaPlayer) {
		// TODO Auto-generated method stub
		if(playTime >0) {
			seekTo(playTime);
		}
		if (pb != null) {
			pb.setVisibility(View.GONE);
		}
		mediaController.show();

	}

	@Override
	public void onVideoChange(int layout) {
		// TODO Auto-generated method stub
		setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
	}

	@Override
	public void onLandscape() {
		// TODO Auto-generated method stub
		if (act != null) {
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	public void onPortrait() {
		// TODO Auto-generated method stub
		if (act != null) {
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	String[] items = null;

	@Override
	public void onLevel(final String vid,int targetBitRate) {
		// TODO Auto-generated method stub
//		setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
		playTime = getCurrentPosition();
		switchLevel(targetBitRate);
		if (pb != null) {
			pb.setVisibility(View.VISIBLE);
		}
		mediaController.hide();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		mediaController.onVoiceBrightTouchEvent(ev);
		return !isLock;
	}

	public ScreenListenerManager getScreenListener() {
		return screenListener;
	}

	public void setScreenListener(ScreenListenerManager screenListener) {
		this.screenListener = screenListener;
	}

	public VkoMediaController getMediaController() {

		return mediaController;
	}

	public void setVideoLoadingPb(ProgressBar pb) {

		this.pb = pb;
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
		mVideoWidth = ViewUtils.getScreenWidth(ctx);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//			Log.e("=====横屏了","-------下------");
			mediaController.setTopBarVisibility(true);
			setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
			mVideoHeight =  ViewUtils.getScreenHeight(ctx);
			setFullScreen(act);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mVideoWidth,mVideoHeight);
			setLayoutParams(lp);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//			Log.e("=====竖屏了","-------下------");
			mediaController.setTopBarVisibility(false);
			quitFullScreen(act);
			float ratio = (float) 15 / 9;
			mVideoHeight = (int) Math.ceil((float) mVideoWidth / ratio);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mVideoWidth, mVideoHeight);
			setLayoutParams(lp);
		}
		mediaController.hide();
		mediaController.switchBoardIcon(newConfig.orientation);
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 设置全屏
	 * 
	 * @param act
	 */
	public static void setFullScreen(Activity act) {
		act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 退出全屏
	 * 
	 * @param act
	 */
	public static void quitFullScreen(Activity act) {

		final WindowManager.LayoutParams attrs = act.getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		act.getWindow().setAttributes(attrs);
		act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	@Override
	public boolean onVideoPlayError(ErrorReason error) {
		// TODO Auto-generated method stub
		ErrorReason.ErrorType type = error.getType();
		switch (type) {
		case NOT_PERMISSION:
			Looper.prepare();
			Toast.makeText(ctx, "没有访问权限", Toast.LENGTH_SHORT).show();
			Looper.loop();
			break;
		case NETWORK_DENIED:
			Looper.prepare();
			Toast.makeText(ctx, "网络异常", Toast.LENGTH_SHORT).show();
			Looper.loop();
			break;
		case BITRATE_ERROR:
			Looper.prepare();
			Toast.makeText(ctx, " 码率错误", Toast.LENGTH_SHORT).show();
			Looper.loop();
			break;
		case LOCAL_VIEWO_ERROR:
			Looper.prepare();
			Toast.makeText(ctx, "本地视频文件错误", Toast.LENGTH_SHORT).show();
			Looper.loop();
			break;
//		case RUNTIME_EXCEPTION:
//			Looper.prepare();
//			Toast.makeText(ctx, "未知错误", Toast.LENGTH_SHORT).show();
//			Looper.loop();
//			break;
		case VIDEO_STATUS_ERROR:
			Looper.prepare();
			Toast.makeText(ctx, "视频状态错误", Toast.LENGTH_SHORT).show();
			Looper.loop();
			break;
		case VID_ERROR:
			Looper.prepare();
			Toast.makeText(ctx, "vid错误 ", Toast.LENGTH_SHORT).show();
			Looper.loop();
			break;
		}
		return false;
	}

	@Override
	public void onScreenOn() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScreenOff() {
		// TODO Auto-generated method stub
		mediaController.getStartButton().performClick();
		pause();
	}

	@Override
	public void onUserPresent() {
		// TODO Auto-generated method stub
		mediaController.getStartButton().performClick();
		start();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
		int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
		      if (mVideoWidth > 0 && mVideoHeight > 0) {
				  if (mVideoWidth * height > width * mVideoHeight) {
					  height = width * mVideoHeight / mVideoWidth;
				  } else if (mVideoWidth * height < width * mVideoHeight) {
					  //Log.i("@@@", "image too wide, correcting");
					  width = height * mVideoWidth / mVideoHeight;
				  }
			  }
		setMeasuredDimension(width,height);
	}

	public void unregisterReceiver(){
		if(screenListener != null){
			screenListener.unregisterListener();
		}
	}
}
