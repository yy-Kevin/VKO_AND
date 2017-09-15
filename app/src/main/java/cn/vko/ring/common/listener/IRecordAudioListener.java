package cn.vko.ring.common.listener;

import java.io.File;

public interface IRecordAudioListener<T> {
	void onCancel();
	void onDone(T t);
}
