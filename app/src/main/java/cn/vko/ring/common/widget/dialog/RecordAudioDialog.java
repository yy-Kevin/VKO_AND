package cn.vko.ring.common.widget.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import cn.vko.ring.circle.model.AudioInfo;
import cn.vko.ring.common.base.BaseDialog;
import cn.vko.ring.common.listener.IRecordAudioListener;
import cn.vko.ring.common.speex.SpeexRecorder;
import cn.vko.ring.common.speex.SpeexRecorder.Callback;

public class RecordAudioDialog extends BaseDialog implements Callback {

	private ImageView ivLeftAudio, ivRightAudio;
	private TextView tvLength;
	private ImageView ivRecord;
	private AnimationDrawable animationLeftDrawabl, animationRightDrawablee;
	private SpeexRecorder recorderInstance = null;
	private int time;
	private IRecordAudioListener<AudioInfo> listener;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				time++;
				tvLength.setText(time + "''");
				sendEmptyMessageDelayed(0, 1000);
				break;
			}
		}
	};

	public void setListener(IRecordAudioListener<AudioInfo> listener) {

		this.listener = listener;
	}

	public RecordAudioDialog(Context context) {
		super(context, R.style.MaskDialog);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setUpViews(View root) {
		// TODO Auto-generated method stub
		tvLength = (TextView) root.findViewById(R.id.tv_audio_lenth);
		ivRecord = (ImageView) root.findViewById(R.id.iv_record);
		ivLeftAudio = (ImageView) root.findViewById(R.id.iv_left_audio);
		ivRightAudio = (ImageView) root.findViewById(R.id.iv_right_audio);

		animationLeftDrawabl = (AnimationDrawable) ivLeftAudio.getBackground();
		animationRightDrawablee = (AnimationDrawable) ivRightAudio
				.getBackground();
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub

		ivRecord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (recorderInstance != null && recorderInstance.isRecording()) {
					int padding = (int) context.getResources().getDimension(R.dimen.dimen5);
					ivRecord.setPadding(padding, padding, padding, padding);
					ivRecord.setImageResource(R.drawable.shape_audio_normal);
					stopRecord();
				} else {
					int padding = (int) context.getResources().getDimension(R.dimen.dimen15);
					ivRecord.setPadding(padding, padding, padding, padding);
					ivRecord.setImageResource(R.drawable.shape_audio_record);
					recordAudio();
				}
			}
		});
	}

	protected void stopRecord() {
		// TODO Auto-generated method stub
		long lenth = recorderInstance.stopRecording();
		if (lenth > 1000) {
			String voicePath = recorderInstance.getFileName();
			AudioInfo audio = new AudioInfo(voicePath, (int) lenth);
			if (listener != null) {
				listener.onDone(audio);
			}
		}
		dismiss();
	}

	public void recoverView() {
		mHandler.removeMessages(0);
		if (recorderInstance != null) {
			if (recorderInstance.isRecording()) {
				recorderInstance.discardRecording();
			}
			time = 0;
			tvLength.setText("点击录音");

			animationLeftDrawabl.stop();
			animationLeftDrawabl.selectDrawable(0);
			animationRightDrawablee.stop();
			animationRightDrawablee.selectDrawable(0);
			
			ivLeftAudio.setVisibility(View.GONE);
			ivRightAudio.setVisibility(View.GONE);
			ivRecord.setPadding(20, 20, 20, 20);
			ivRecord.setImageResource(R.drawable.shape_audio_normal);
		}
	}

	protected void recordAudio() {
		// TODO Auto-generated method stub
		if (recorderInstance == null) {
			recorderInstance = new SpeexRecorder();
			recorderInstance.setCallback(this);
		}
		if (!recorderInstance.isRecording()) {
			recorderInstance.setRecording(true);
			Thread th = new Thread(recorderInstance);
			th.start();
		}

		animationLeftDrawabl.start();
		animationRightDrawablee.start();

		ivLeftAudio.setVisibility(View.VISIBLE);
		ivRightAudio.setVisibility(View.VISIBLE);
		mHandler.sendEmptyMessageDelayed(0, 1000);
	}

	@Override
	public boolean getCancelableOnclick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getGravity() {
		// TODO Auto-generated method stub
		return Gravity.BOTTOM;
	}

	@Override
	public int getRootId() {
		// TODO Auto-generated method stub
		return R.layout.layout_record_view;
	}

	@Override
	public int getAnimatStly() {
		// TODO Auto-generated method stub
		return R.style.dialog_anim;
	}

	@Override
	public LayoutParams getILayoutParams() {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(ViewUtils.getScreenWidth(context),
				LayoutParams.WRAP_CONTENT);
		return lp;
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		recorderInstance.stopRecording();
	}

	@Override
	public void onEnd(long time) {
		// TODO Auto-generated method stub

	}

}
