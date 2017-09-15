package cn.vko.ring.study.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.common.widget.ContainsEmojiEditText;

public class MyVoiceSearchPersoner implements RecognitionListener {
	private Activity context;
	public RelativeLayout layout_video_luxyin;
	public ContainsEmojiEditText et_seacher;
	private SpeechRecognizer speechRecognizer;
	// View speechTips;
	View speechWave;
	private static final int EVENT_ERROR = 11;
	private long speechEndTime = -1;
	private PopupWindow pop;
	private View view;
	private TextView tv_seacher_text;
	private AlertDialog aDialogt;
	private View viewpopwindow;
	private RelativeLayout tv_Voice_search;
	private TextView tv_state;
	private Handler handler;
	private ArrayList<String> nbest;
	private InputMethodManager imm;

	private int x, y;

	public MyVoiceSearchPersoner(Activity context,
			SpeechRecognizer speechRecognizer, RelativeLayout tv_Voice_search,
			ContainsEmojiEditText et_seacher) {
		this.context = context;
		this.speechRecognizer = speechRecognizer;
		this.tv_Voice_search = tv_Voice_search;
		this.et_seacher = et_seacher;
		handler = new Handler();
		tv_state = (TextView) tv_Voice_search.findViewById(R.id.tv_state);

		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		initAlertDialog();
		initlistener();
//		initShowEtSeacher();
	}

	

	private void initShowEtSeacher() {
		et_seacher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_Voice_search.setVisibility(View.VISIBLE);
			}
		});

	}

	private void initAlertDialog() {
		Log.e("===initAlertDialog","initAlertDialog");
		aDialogt = new AlertDialog.Builder(context).create();
		/*
		 * Window window = aDialogt.getWindow();
		 * window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		 */
		aDialogt.show();

		aDialogt.getWindow().setLayout(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		aDialogt.getWindow().setContentView(R.layout.pop_bd_asr_speech);
		layout_video_luxyin = (RelativeLayout) aDialogt.getWindow()
				.findViewById(R.id.layout_video_luxyin);
		aDialogt.dismiss();
	}

//	public AlertDialog getAlertDialog() {
//		if (null != aDialogt) {
//			aDialogt.dismiss();
//			return aDialogt;
//		} else {
//			initAlertDialog();
//		}
//		return aDialogt;
//	}

	private void initView(int x, int y) {
		Log.e("===initView","initView");
		view = LayoutInflater.from(context).inflate(
				R.layout.pop_start_speech, null);
		TextView textview = (TextView) view.findViewById(R.id.tv_speech_tips);
		tv_seacher_text = (TextView) view.findViewById(R.id.tv_seacher_text);
		pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pop.setFocusable(false);
		pop.setOutsideTouchable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.showAtLocation(layout_video_luxyin, Gravity.CENTER, x, y);
		/*
		 * int[] location = new int[2]; v.getLocationOnScreen(location);
		 */

		/*
		 * popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0],
		 * location[1]-popupWindow.getHeight()); pop.showAtLocation(view,
		 * Gravity.CENTER, 0, 0);
		 */
	}

	private void initlistener() {
		Log.e("===initlistener","initlistener");
		speechRecognizer.setRecognitionListener(this);
		speechWave = layout_video_luxyin.findViewById(R.id.wave);
		tv_Voice_search.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.e("===ACTION_DOWN","ACTION_DOWN");
					if (pop != null) {
						pop.dismiss();
					}
					showAlertDiaLog();
					speechRecognizer.cancel();
					Intent intent = new Intent();
					bindParams(intent);
					intent.putExtra("vad", "touch");
					et_seacher.setText("");
					// tv_state.setText("");
					speechRecognizer.startListening(intent);
					return true;
				case MotionEvent.ACTION_UP:
					Log.e("===ACTION_UP","ACTION_UP");
					speechRecognizer.stopListening();
					aDialogt.dismiss();
					initView(x, y);
					break;
				}
				return false;
			}

		});
	}

	private void showAlertDiaLog() {
		Log.e("===showAlertDiaLog","===============showAlertDiaLog");
		int[] position = new int[2];
		tv_Voice_search.getLocationOnScreen(position);

		// 判断隐藏软键盘是否弹出
		if (position[1] > 700) {
			Window window = aDialogt.getWindow();
			window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置

			Window w = aDialogt.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.x = 0;
			lp.y = 0;

			x = lp.x;
			y = lp.y;
			aDialogt.show();

		} else {
			Window w = aDialogt.getWindow();
			WindowManager.LayoutParams lp = w.getAttributes();
			lp.x = 0;
			lp.y = -300;

			x = lp.x;
			y = lp.y;
			Toast.makeText(context, "打开", Toast.LENGTH_SHORT).show();
			aDialogt.show();
		}
	}

	public void bindParams(Intent intent) {
		Log.e("===bindParams","===============bindParams");
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (sp.getBoolean("tips_sound", true)) {

			// intent.putExtra(Constant.EXTRA_SOUND_START,
			// R.raw.bdspeech_recognition_start);
			// intent.putExtra(Constant.EXTRA_SOUND_END,
			// R.raw.bdspeech_speech_end);
			// intent.putExtra(Constant.EXTRA_SOUND_SUCCESS,
			// R.raw.bdspeech_recognition_success);
			// intent.putExtra(Constant.EXTRA_SOUND_ERROR,
			// R.raw.bdspeech_recognition_error);
			// intent.putExtra(Constant.EXTRA_SOUND_CANCEL,
			// R.raw.bdspeech_recognition_cancel);

		}
		if (sp.contains(Constants.EXTRA_INFILE)) {
			String tmp = sp.getString(Constants.EXTRA_INFILE, "")
					.replaceAll(",.*", "").trim();
			intent.putExtra(Constants.EXTRA_INFILE, tmp);
		}
		if (sp.getBoolean(Constants.EXTRA_OUTFILE, false)) {
			intent.putExtra(Constants.EXTRA_OUTFILE, "sdcard/outfile.pcm");
		}
		if (sp.contains(Constants.EXTRA_SAMPLE)) {
			String tmp = sp.getString(Constants.EXTRA_SAMPLE, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constants.EXTRA_SAMPLE, Integer.parseInt(tmp));
			}
		}
		if (sp.contains(Constants.EXTRA_LANGUAGE)) {
			String tmp = sp.getString(Constants.EXTRA_LANGUAGE, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constants.EXTRA_LANGUAGE, tmp);
			}
		}
		if (sp.contains(Constants.EXTRA_NLU)) {
			String tmp = sp.getString(Constants.EXTRA_NLU, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constants.EXTRA_NLU, tmp);
			}
		}

		if (sp.contains(Constants.EXTRA_VAD)) {
			String tmp = sp.getString(Constants.EXTRA_VAD, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constants.EXTRA_VAD, tmp);
			}
		}
		String prop = null;
		if (sp.contains(Constants.EXTRA_PROP)) {
			String tmp = sp.getString(Constants.EXTRA_PROP, "")
					.replaceAll(",.*", "").trim();
			if (null != tmp && !"".equals(tmp)) {
				intent.putExtra(Constants.EXTRA_PROP, Integer.parseInt(tmp));
				prop = tmp;
			}
		}
		// offline asr
		{
			intent.putExtra(Constants.EXTRA_OFFLINE_ASR_BASE_FILE_PATH,
					"/sdcard/easr/s_1");
			intent.putExtra(Constants.EXTRA_LICENSE_FILE_PATH,
					"/sdcard/easr/license-tmp-20150530.txt");
			if (null != prop) {
				int propInt = Integer.parseInt(prop);
				if (propInt == 10060) {
					intent.putExtra(Constants.EXTRA_OFFLINE_LM_RES_FILE_PATH,
							"/sdcard/easr/s_2_Navi");
				} else if (propInt == 20000) {
					intent.putExtra(Constants.EXTRA_OFFLINE_LM_RES_FILE_PATH,
							"/sdcard/easr/s_2_InputMethod");
				}
			}
			intent.putExtra(Constants.EXTRA_OFFLINE_SLOT_DATA,
					buildTestSlotData());
		}
	}

	private String buildTestSlotData() {
		Log.e("===buildTestSlotData","===============buildTestSlotData");
		JSONObject slotData = new JSONObject();
		JSONArray name = new JSONArray().put("李涌泉").put("郭下纶");
		JSONArray song = new JSONArray().put("七里香").put("发如雪");
		JSONArray artist = new JSONArray().put("周杰伦").put("李世龙");
		JSONArray app = new JSONArray().put("手机百度").put("百度地图");
		JSONArray usercommand = new JSONArray().put("关灯").put("开门");
		return slotData.toString();
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		Log.e("onReadyForSpeech", "----------------onBeginningOfSpeech");
	}

	@Override
	public void onBeginningOfSpeech() {
		Log.e("onBeginningOfSpeech", "---------------onBeginningOfSpeech");
	}

	@Override
	public void onRmsChanged(float rmsdB) {

		final int VTAG = 0xFF00AA01;
		Integer rawHeight = (Integer) speechWave.getTag(VTAG);
		if (rawHeight == null) {
			rawHeight = speechWave.getLayoutParams().height;
			speechWave.setTag(VTAG, rawHeight);
		}

		LayoutParams params = (LayoutParams) speechWave
				.getLayoutParams();
		params.height = (int) (rawHeight * rmsdB * 0.01);
		params.height = Math.max(params.height, speechWave.getMeasuredWidth());
		speechWave.setLayoutParams(params);
		Log.e("onRmsChanged", "---------------onRmsChanged");
	}

	@Override
	public void onBufferReceived(byte[] buffer) {
		// TODO Auto-generated method stub
		Log.e("onBufferReceived", "---------------onBufferReceived");
	}

	@Override
	public void onEndOfSpeech() {
		Log.e("onEndOfSpeech", "---------------onEndOfSpeech");

	}

	@Override
	public void onError(int error) {
		Log.e("===onError","===============onError");
		StringBuilder sb = new StringBuilder();
		switch (error) {
		case SpeechRecognizer.ERROR_AUDIO:
			sb.append("音频问题");
			break;
		case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
			sb.append("没有语音输入");
			break;
		case SpeechRecognizer.ERROR_CLIENT:
			sb.append("其它客户端错误");
			break;
		case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
			sb.append("权限不足");
			break;
		case SpeechRecognizer.ERROR_NETWORK:
			sb.append("网络问题");
			break;
		case SpeechRecognizer.ERROR_NO_MATCH:
			sb.append("没有匹配的识别结果");
			break;
		case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
			sb.append("引擎忙");
			break;
		case SpeechRecognizer.ERROR_SERVER:
			sb.append("服务端错误");
			break;
		case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
			sb.append("连接超时");
			break;
		}
//		print("识别失败：" + sb.toString());
		if(pop != null){
			pop.dismiss();
		}
		if(error != 5){
			Toast.makeText(context, "识别失败:"+sb.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onResults(Bundle results) {
		Log.e("===onResults","===============onResults");
		long end2finish = System.currentTimeMillis() - speechEndTime;
		nbest = results
				.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		Log.e("MyVoiceSearchPersoner",
				"识别成功："
						+ Arrays.toString(nbest.toArray(new String[nbest.size()])));

		String json_res = results.getString("origin_result");

		String strEnd2Finish = "";
		if (end2finish < 60 * 1000) {
			strEnd2Finish = "(waited " + end2finish + "ms)";
		}

		tv_seacher_text.setVisibility(View.VISIBLE);
		tv_seacher_text.setText(nbest.get(0) + strEnd2Finish);

		et_seacher.setText(nbest.get(0) + strEnd2Finish);
		pop.dismiss();
		if (lSeacherListener != null) {
			tv_Voice_search.setVisibility(View.GONE);
			lSeacherListener.seacherText();
		}
		Log.e("onResults", "---------------onResults");
	}

	@Override
	public void onPartialResults(Bundle partialResults) {

	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		switch (eventType) {
		case EVENT_ERROR:
			String reason = params.get("reason") + "";
			print("EVENT_ERROR, " + reason);
			break;
		}

	}

	public interface ISeacherDataListener {
		void seacherText();
	}

	public ISeacherDataListener lSeacherListener;

	public void setlSeacherListener(ISeacherDataListener lSeacherListener) {
		this.lSeacherListener = lSeacherListener;
	}

	private void print(String msg) {
		// txtLog.append(msg + "\n");
		// ScrollView sv = (ScrollView) txtLog.getParent();
		// sv.smoothScrollTo(0, 1000000);
		Log.d("MyVoiceSearchPersoner", "----" + msg);
		Log.e("MyVoiceSearchPersoner", "----" + msg);
	}
}
