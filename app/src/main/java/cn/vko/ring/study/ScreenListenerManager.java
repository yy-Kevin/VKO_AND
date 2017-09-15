package cn.vko.ring.study;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

public class ScreenListenerManager {
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private IScreenStateListener mScreenStateListener;
    private boolean isRegister;
    public ScreenListenerManager(Context context) {
        mContext = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
    }

    /**
     * screen状态广播接收者
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
                mScreenStateListener.onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
                mScreenStateListener.onScreenOff();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
                mScreenStateListener.onUserPresent();
            }
        }
    }

    /**
     * 开始监听screen状态
     * 
     * @param listener
     */
    public void begin(IScreenStateListener listener) {
        mScreenStateListener = listener;
        registerListener();
        getScreenState();
    }

    /**
     * 获取screen状态
     */
    private void getScreenState() {
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOn();
            }
        } else {
            if (mScreenStateListener != null) {
                mScreenStateListener.onScreenOff();
            }
        }
    }

    /**
     * 停止screen状态监听
     */
    public void unregisterListener() {
        if( mScreenReceiver != null && mContext!= null &&isRegister){
            try{
                mContext.unregisterReceiver(mScreenReceiver);
            }catch (Exception e){

            }
        }
    }

    /**
     * 启动screen状态广播接收器
     */
    private void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        isRegister = true;
        mContext.registerReceiver(mScreenReceiver, filter);
    }

    public interface IScreenStateListener {// 返回给调用者屏幕状态信息
        public void onScreenOn();

        public void onScreenOff();

        public void onUserPresent();
    }

	public void setmScreenStateListener(IScreenStateListener mScreenStateListener) {
		this.mScreenStateListener = mScreenStateListener;
	}
    
    
}
