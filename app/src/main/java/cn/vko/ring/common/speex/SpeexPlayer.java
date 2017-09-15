/**
 * 
 */
package cn.vko.ring.common.speex;

import java.io.File;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import com.gauss.speex.encode.SpeexDecoder;

import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.callback.FileCallBack;
import cn.vko.ring.circle.activity.TestPageActivity;
import cn.vko.ring.utils.FileUtil;
import okhttp3.Call;


/**
 * @author Gauss
 * 
 */
public class SpeexPlayer {
	private String filePath = null;
	private SpeexDecoder speexdec = null;
	private AnimationDrawable animationDrawable;
	private SpeexDecoder.IAudioCompleteListener listener;
	private Thread th;
	private boolean isStart;
	private Context context;
	private static SpeexPlayer instance;
	private File localFile;
	public static SpeexPlayer getInstance(Context context) {
		if (instance == null) {
			instance = new SpeexPlayer(context);
		}
		return instance;
	}

	private SpeexPlayer(Context context) {
		this.context = context;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				initSpeexDecoder(localFile, listener);
				if (isStart && th != null) {
					th.start();
				}
				break;
			case 1:
				if(animationDrawable != null){
					animationDrawable.start();
				}
				break;
			}
			
		};
	};

	private void initSpeexDecoder(File localFile,
			SpeexDecoder.IAudioCompleteListener listener) {

		try {
			speexdec = new SpeexDecoder(localFile);
			RecordPlayThread rpt = new RecordPlayThread();
			th = new Thread(rpt);
			if (listener != null) {
				speexdec.setOnCompleteListener(listener);
			} else {
				speexdec.setOnCompleteListener(new SpeexDecoder.IAudioCompleteListener() {

					@Override
					public void onComplete() {
						// TODO Auto-generated method stub
						if (animationDrawable != null) {
							animationDrawable.stop();
							animationDrawable.selectDrawable(0);
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPlayer(String filePath,
			final SpeexDecoder.IAudioCompleteListener listener) {
		this.filePath = filePath;
		this.listener = listener;
		// System.out.println(this.fileName);
		stopPlay();
		if (URLUtil.isNetworkUrl(filePath)) {// 网络地址先下载
			localFile = new File(FileUtil.getAudioDir(),FileUtil.getFileName(filePath));
			if (!localFile.exists()) {
				OkHttpUtils.get().url(filePath).build().execute(new FileCallBack(FileUtil.getAudioDir().getAbsolutePath(),FileUtil.getFileName(filePath)) {
					@Override
					public void inProgress(float progress, long total) {

					}

					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(File response) {
						Message msg = new Message();
						msg.what = 0;
						mHandler.sendMessage(msg);
					}
				});
			} else {
				initSpeexDecoder(localFile, listener);
			}
		} else {
			localFile = new File(filePath);
			if (!localFile.exists()) {
				if (context!=null) {
					
					Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
				}
				return;
			}
			initSpeexDecoder(localFile, listener);
		}
	}

	public void reStart(){
		if (!localFile.exists()) {
			Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		stopPlay();
		initSpeexDecoder(localFile, listener);
	}
	public void startPlay() {
		if (!isStart && th != null) {
			th.start();
		}
		isStart = true;
		if (speexdec != null) {
			speexdec.start();
		}
	}

	public void pusePlay() {
		if (speexdec != null) {
			speexdec.paused();
		}
	}

	public boolean isStart() {
		if (speexdec != null) {
			return speexdec.isPlaying();
		}
		return false;
	}

	public void stopPlay() {
		if (animationDrawable != null) {
			animationDrawable.stop();
			animationDrawable.selectDrawable(0);
			animationDrawable = null;
		}
		if (speexdec != null) {
			speexdec.stop();
		}
		speexdec = null;
		isStart = false;
		th = null;
	}

	public void initAnimation(ImageView voiceIv) {
		animationDrawable = (AnimationDrawable) voiceIv.getBackground();
		mHandler.sendEmptyMessageDelayed(1, 500);
	}

	class RecordPlayThread extends Thread {

		public void run() {
			try {
				if (speexdec != null)
					speexdec.decode();
			} catch (Exception t) {
				t.printStackTrace();
			}
		}
	};
}
