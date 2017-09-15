/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: TimeLayout.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.common.weight 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-7-23 下午3:47:39 
 * @version: V1.0   
 */
package cn.vko.ring.common.widget;

import java.util.Timer;
import java.util.TimerTask;

import cn.vko.ring.R;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 计时器
 * @ClassName: TimeLayout
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-7-23 下午3:47:39
 */
public class TimeLayout extends LinearLayout {
	private View view;
	TextView timeInfo;
	TextView timeNow;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private Timer mTimer = new Timer();
	private boolean run;
	private Handler mHandler = new Handler();
	private long mDay, mHour, mMin, mSecond;// 天，小时，分钟，秒
	/** 是否倒计时 */
	private boolean isDownCountTime = true;

	public interface OnTimeOverListener {
		boolean isTimeOver(boolean isOver);
	}

	private OnTimeOverListener onTimeOverListener;

	public void setOnTimeOverListener(OnTimeOverListener onTimeOverListener) {
		this.onTimeOverListener = onTimeOverListener;
	}
	/**
	 * @Description:TODO
	 * @param context
	 * @param attrs
	 */
	public TimeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		view = mLayoutInflater.inflate(R.layout.layout_time_lay, null);
		timeInfo = (TextView) view.findViewById(R.id.time_info);
		timeNow = (TextView) view.findViewById(R.id.time_now);
		addView(view);
	}
	public void setDuration(long time) {
		mDay = time / 1000 / 60 / 60 / 24;
		mHour = (time - mDay * 24 * 60 * 60 * 1000) / (1000 * 60 * 60);
		mMin = (time - mHour * 60 * 60 * 1000 - mDay * 24 * 60 * 60 * 1000) / (60 * 1000);
		mSecond = (time - mMin * 60 * 1000 - mHour * 60 * 60 * 1000 - mDay * 24 * 60 * 60 * 1000) / 1000;
		setTime();
	}
	public void start() {
		if (!isRun()) {
			setRun(true);
			if (mTimer == null) {
				mTimer = new Timer();
			}
			mTimer.schedule(task, 1000, 1000);
		}
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					run = true;
					computeTime();
					if (mDay < 0) {
						setRun(false);
						if (null != onTimeOverListener) {
							onTimeOverListener.isTimeOver(true);
							timeNow.setText("00:00");
							stop();
							return;
						}
					}
					setTime();
				}
			});
		}
	};

	private void setTime() {
		String hour = mHour < 10 ? ("0" + mHour) : mHour + "";
		String min = mMin < 10 ? ("0" + mMin) : mMin + "";
		String second = mSecond < 10 ? ("0" + mSecond) : mSecond + "";
		if (timeNow == null) {
			return;
		}
		timeNow.setText((mDay == 0 ? "" : (mDay + ":")) + (hour.equals("00") ? "" : (hour + ":")) + min + ":" + second);
	}
	private void computeTime() {
		if (isDownCountTime) {
			mSecond--;
			if (mSecond < 0) {
				mSecond = 59;
				mMin--;
				if (mMin < 0) {
					mMin = 59;
					mHour--;
					if (mHour < 0) {
						mHour = 24;
						mDay--;
					}
				}
			}
		} else {
			mSecond++;
			if (mSecond == 60) {
				mSecond = 0;
				mMin++;
				if (mMin == 60) {
					mMin = 0;
					mHour++;
					if (mHour == 24) {
						mHour = 0;
						mDay++;
					}
				}
			}
		}
	}
	public void stop() {
		run = false;
		if (null != mTimer) {
			mTimer.cancel();
			mTimer = null;
		}
	}
	/**
	 * 获取当前时间
	 * @Title: getNowTime
	 * @Description: TODO
	 * @return
	 * @return: String
	 */
	public String getNowTime() {
		return timeNow.getText().toString();
	}
	public long getNowTimeLong() {
		return mDay * 24 * 60 * 60 * 1000 + mHour * 60 * 60 * 1000 + mMin * 60 * 1000 + mSecond * 1000;
	}
	public TextView getTimeInfo() {
		return timeInfo;
	}
	public void setTimeInfo(TextView timeInfo) {
		this.timeInfo = timeInfo;
	}
	public TextView getTimeNow() {
		return timeNow;
	}
	public void setTimeNow(TextView timeNow) {
		this.timeNow = timeNow;
	}
	public boolean isRun() {
		return run;
	}
	public void setRun(boolean run) {
		this.run = run;
	}
	public boolean isDownCountTime() {
		return isDownCountTime;
	}
	public void setDownCountTime(boolean isDownCountTime) {
		this.isDownCountTime = isDownCountTime;
	}
}
