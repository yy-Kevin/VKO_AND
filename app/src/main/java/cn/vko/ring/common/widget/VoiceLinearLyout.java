package cn.vko.ring.common.widget;

import cn.vko.ring.R;
import cn.vko.ring.circle.model.AudioInfo;
import cn.vko.ring.common.speex.SpeexPlayer;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VoiceLinearLyout extends LinearLayout {
	private Context context;
	private AudioInfo audio;
	
	public AudioInfo getAudio() {
		return audio;
	}

	public void setAudio(AudioInfo audio) {
		this.audio = audio;
	}

	public VoiceLinearLyout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void initView(final AudioInfo audio) {
		if (audio == null || TextUtils.isEmpty(audio.getUrl()))
			return;
		this.audio = audio;
		removeAllViews();
		View child = View.inflate(context, R.layout.layout_sound_view, null);
		final ImageView ivVoice = (ImageView) child.findViewById(R.id.iv_voice);
		TextView tvLen = (TextView) child.findViewById(R.id.tv_voice_length);
		tvLen.setText(audio.getLen()/1000 + "''");
		child.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mPlay != null && mPlay.isStart()){
					mPlay.stopPlay();
				}else{
					play(audio, ivVoice);
				}
			}
		});
		
		addView(child);
	}

	private SpeexPlayer mPlay;

	private void play(AudioInfo audio, ImageView voiceIv) {
		if (mPlay == null) {
			mPlay = SpeexPlayer.getInstance(context);
		}
		mPlay.createPlayer(audio.getUrl(), null);
		mPlay.startPlay();
		mPlay.initAnimation(voiceIv);

	}

}
