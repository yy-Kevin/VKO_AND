package cn.vko.ring.common.speex;

import java.io.File;
import java.util.Date;

import cn.vko.ring.utils.FileUtil;

import com.gauss.speex.encode.SpeexEncoder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.text.format.Time;

public class SpeexRecorder implements Runnable {

	// private Logger log = LoggerFactory.getLogger(SpeexRecorder.class);
	private volatile boolean isRecording;
	private final Object mutex = new Object();
	private static final int frequency = 8000;
	private long startTime;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	public static int packagesize = 160;
	private String fileName = null;
	private AudioRecord recordInstance;

	private double amplitude = 0;
	private double DB;
	private double MaxRecode;
	private Callback callback;
	double dB;

	public SpeexRecorder() {

		super();
	}

	public String getFileName() {

		return this.fileName;
	}

	public void run() {
		this.fileName = getVoiceFilePath();
		try {
			SpeexEncoder encoder = new SpeexEncoder(this.fileName);
			Thread encodeThread = new Thread(encoder);
			encoder.setRecording(true);
			encodeThread.start();
			startTime = new Date().getTime();
			synchronized (mutex) {
				while (!this.isRecording) {
					try {
						mutex.wait();
					} catch (InterruptedException e) {
						throw new IllegalStateException("Wait() interrupted!", e);
					}
				}
			}
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

			int bufferRead = 0;
			int bufferSize = AudioRecord.getMinBufferSize(frequency, AudioFormat.CHANNEL_IN_MONO, audioEncoding);

			short[] tempBuffer = new short[packagesize];
			if (recordInstance == null) {
				recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, AudioFormat.CHANNEL_IN_MONO,
						audioEncoding, bufferSize);
			}

			recordInstance.startRecording();

			while (this.isRecording) {
				// log.debug("start to recording.........");
				bufferRead = recordInstance.read(tempBuffer, 0, packagesize);

				int v = 0;
				for (int i = 0; i < tempBuffer.length; i++) {
					v += tempBuffer[i] * tempBuffer[i];
				}
				// 获取振幅
				setAmplitude((v / (double) bufferRead) % 11);

//				 recordInstance.get
//				 if (MaxRecode < (v / (double) bufferRead)) {
//				 MaxRecode = (v / (double) bufferRead);
//				 }
				 // Log.d(TAG, "音量大小=" + (v / (double) bufferRead));
				 if (dB < (10 * Math.log10(v / (double) bufferRead))) {
					 dB = 10 * Math.log10(v / (double) bufferRead);
				 }

				// bufferRead = recordInstance.read(tempBuffer, 0, 320);
				if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
					throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
				} else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
					throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
				} else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
					throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
				}
				// log.debug("put data into encoder collector....");
				encoder.putData(tempBuffer, bufferRead);

			}
			recordInstance.stop();
			// tell encoder to stop.
			encoder.setRecording(false);
		} catch (Exception e) {
			if (callback != null) {
				callback.onError();
			}
		} finally {
			isRecording = false;
			if (callback != null) {
				callback.onEnd(new Date().getTime() - this.startTime);
			}
		}

	}

	public void setRecording(boolean isRecording) {

		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}

	public long stopRecording() {
		this.isRecording = false;
		long i = (int) (new Date().getTime() - this.startTime);
		return i;
	}

	public void discardRecording() {

		this.isRecording = false;
		if (!TextUtils.isEmpty(fileName)) {
			File file = new File(fileName);
			if (file != null && file.exists() && (!file.isDirectory())) {
				file.delete();
			}
		}
	}

	public String getVoiceFileName(String filename) {

		return filename + System.currentTimeMillis() + ".spx";
	}

	public String getVoiceFilePath() {

		return FileUtil.getAudioDir().getAbsolutePath() + "/"
				+ getVoiceFileName("vko_");
	}

	public double getAmplitude() {

		return amplitude;
	}

	public void setAmplitude(double amplitude) {

		this.amplitude = amplitude;
	}

	public Callback getCallback() {

		return callback;
	}

	public void setCallback(Callback callback) {

		this.callback = callback;
	}

	public static interface Callback {
		void onError();
		void onEnd(long time);
	}
}
