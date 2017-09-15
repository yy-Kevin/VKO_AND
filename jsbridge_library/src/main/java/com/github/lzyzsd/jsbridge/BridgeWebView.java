package com.github.lzyzsd.jsbridge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends WebView implements WebViewJavascriptBridge {

	private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0x10;
	private final String TAG = "BridgeWebView";
	WvLoadingDialog loadingDialog;
	private Context context;
	String toLoadJs = "WebViewJavascriptBridge.js";
	Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();
	Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
	BridgeHandler defaultHandler = new DefaultHandler();

	List<Message> startupMessage = new ArrayList<Message>();
	long uniqueId = 0;

	public BridgeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public BridgeWebView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
	 *
	 * @param handler
	 *            default handler,handle messages send by js without assigned handler name,
	 *            if js message has handler name, it will be handled by named handlers registered by native
	 */
	public void setDefaultHandler(BridgeHandler handler) {
		this.defaultHandler = handler;
	}

	private void init() {
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);
		this.getSettings().setJavaScriptEnabled(true);

		// 支持下载
		setDownloadListener(new MyWebViewDownLoadListener());
		// 支持视频播放
		getSettings().setPluginState(WebSettings.PluginState.ON);

		if (Build.VERSION.SDK_INT >= 19) {
			WebView.setWebContentsDebuggingEnabled(true);
		}
		this.setWebViewClient(new BridgeWebViewClient());
		loadingDialog = new WvLoadingDialog(context);
	}

	private void handlerReturnData(String url) {
		String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
		CallBackFunction f = responseCallbacks.get(functionName);
		String data = BridgeUtil.getDataFromReturnUrl(url);
		if (f != null) {
			f.onCallBack(data);
			responseCallbacks.remove(functionName);
			return;
		}
	}

	class BridgeWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			try {
				url = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
				handlerReturnData(url);
				return true;
			} else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
				flushMessageQueue();
				return true;
			} else {
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
					selfPermissionGranted(intent);
					return true;
				} else {
					return super.shouldOverrideUrlLoading(view, url);
				}
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			showLoading();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			closeDialog();
			if (toLoadJs != null) {
				BridgeUtil.webViewLoadLocalJs(view, toLoadJs);
			}

			//
			if (startupMessage != null) {
				for (Message m : startupMessage) {
					dispatchMessage(m);
				}
				startupMessage = null;
			}
		}
		private void showLoading() {
			try {
				if (!((Activity) context).isFinishing()) {
					loadingDialog.show();
				}
			} catch (Exception e) {

			}
		}

		private void closeDialog() {
			try {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.cancel();
				}
			} catch (Exception e) {

			}
		}

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
			// 用javascript隐藏系统定义的404页面信息
			String errorHtml = "<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
					+ "<meta name=\"description\" content=\"\">"
					+ "<meta name=\"apple-touch-fullscreen\" content=\"yes\" />"
					+ "<meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" name=\"viewport\" />"
					+ "<meta content=\"no\" name=\"apple-mobile-web-app-capable\" />"
					+ "<meta content=\"black\" name=\"apple-mobile-web-app-status-bar-style\" />"
					+ "<meta content=\"telephone=no\" name=\"format-detection\" />"
					+ "<title>404 Not found</title>"
					+ "<style type=\"text/css\">"
					+ "* {-webkit-box-sizing: border-box;-moz-box-sizing: border-box;box-sizing: border-box;font-family: \"微软雅黑\";padding: 0;margin: 0;-webkit-tap-highlight-color: rgba(0,0,0,0);}"
					+ "html {-ms-touch-action: none;}"
					+ "body,html{width: 100%;height: 100%;font-size: 12px;font-family: \"微软雅黑\",ubuntu, helvetica, arial;background:#edf0f3;overflow: hidden;}"
					+ "a{ text-decoration:none;}body { -webkit-user-select: none;-webkit-touch-callout: none;}img{border:0px;}"
					+ "#image {width: 30%;}"
					+ "#wrap {width: 100%;height: 100%;overflow-x: hidden!important;overflow-y: auto!important;}#wrap{background: #fff;}"
					+ ".box{margin: 45% auto;width: 100%;text-align: center;font-size: 16px;font-weight: lighter;line-height: 30px;}#fresh{color: #00AAEE;}"
					+ "</style></head>"
					+ "<body><div id=\"wrap\"><div class=\"box\">"
					+ "<img id =\"image\"src=\"empty.png\" style=\"vertical-align:middle;\" />"
					+ "<p style=\"color:#C8C9CB;\">页面未找到</p><a id=\"fresh\" href=\""
					+ failingUrl + "\">点击刷新</a></div></div></body></html>";
			// view.loadData(errorHtml, "text/html; charset=UTF-8", null);
			view.loadDataWithBaseURL("file:///android_asset/", errorHtml,
					"text/html; charset=UTF-8", null, null);
        }
    }

	@Override
	public void send(String data) {
		send(data, null);
	}

	@Override
	public void send(String data, CallBackFunction responseCallback) {
		doSend(null, data, responseCallback);
	}

	private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
		Message m = new Message();
		if (!TextUtils.isEmpty(data)) {
			m.setData(data);
		}
		if (responseCallback != null) {
			String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
			responseCallbacks.put(callbackStr, responseCallback);
			m.setCallbackId(callbackStr);
		}
		if (!TextUtils.isEmpty(handlerName)) {
			m.setHandlerName(handlerName);
		}
		queueMessage(m);
	}

	private void queueMessage(Message m) {
		if (startupMessage != null) {
			startupMessage.add(m);
		} else {
			dispatchMessage(m);
		}
	}

	private void dispatchMessage(Message m) {
        String messageJson = m.toJson();
        //escape special characters for json string
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            this.loadUrl(javascriptCommand);
        }
    }

	public void flushMessageQueue() {
		if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
			loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new CallBackFunction() {

				@Override
				public void onCallBack(String data) {
					// deserializeMessage
					List<Message> list = null;
					try {
						list = Message.toArrayList(data);
					} catch (Exception e) {
                        e.printStackTrace();
						return;
					}
					if (list == null || list.size() == 0) {
						return;
					}
					for (int i = 0; i < list.size(); i++) {
						Message m = list.get(i);
						String responseId = m.getResponseId();
						// 是否是response
						if (!TextUtils.isEmpty(responseId)) {
							CallBackFunction function = responseCallbacks.get(responseId);
							String responseData = m.getResponseData();
							function.onCallBack(responseData);
							responseCallbacks.remove(responseId);
						} else {
							CallBackFunction responseFunction = null;
							// if had callbackId
							final String callbackId = m.getCallbackId();
							if (!TextUtils.isEmpty(callbackId)) {
								responseFunction = new CallBackFunction() {
									@Override
									public void onCallBack(String data) {
										Message responseMsg = new Message();
										responseMsg.setResponseId(callbackId);
										responseMsg.setResponseData(data);
										queueMessage(responseMsg);
									}
								};
							} else {
								responseFunction = new CallBackFunction() {
									@Override
									public void onCallBack(String data) {
										// do nothing
									}
								};
							}
							BridgeHandler handler;
							if (!TextUtils.isEmpty(m.getHandlerName())) {
								handler = messageHandlers.get(m.getHandlerName());
							} else {
								handler = defaultHandler;
							}
							if (handler!=null)
							handler.handler(m.getData(), responseFunction);
						}
					}
				}
			});
		}
	}

	public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
		this.loadUrl(jsUrl);
		responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
	}

	/**
	 * register handler,so that javascript can call it
	 * 
	 * @param handlerName
	 * @param handler
	 */
	public void registerHandler(String handlerName, BridgeHandler handler) {
		if (handler != null) {
			messageHandlers.put(handlerName, handler);
		}
	}

	/**
	 * call javascript registered handler
	 *
     * @param handlerName
	 * @param data
	 * @param callBack
	 */
	public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        doSend(handlerName, data, callBack);
	}

	public void selfPermissionGranted(Intent intent) {
		// For Android < Android M, self permissions are always granted.
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			if (ContextCompat.checkSelfPermission(context,
//					Manifest.permission.CALL_PHONE)
//					!= PackageManager.PERMISSION_GRANTED) {
//
//				// Should we show an explanation?
//				if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,Manifest.permission.READ_CONTACTS)) {
//					// Show an expanation to the user *asynchronously* -- don't block
//					// this thread waiting for the user's response! After the user
//					// sees the explanation, try again to request the permission.
//					Toast.makeText(context,"开启通话权限才能使用",Toast.LENGTH_LONG).show();
//				} else {
//					// No explanation needed, we can request the permission.
//					ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
//					// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//					// app-defined int constant. The callback method gets the
//					// result of the request.
//				}
//			} else {
////				执行获取权限后的操作
//				context.startActivity(intent);
//			}
//			}else{
			context.startActivity(intent);
//		}
	}

	public void clearCache(Context context) {
		context.deleteDatabase("webview.db");
		context.deleteDatabase("webviewCache.db");
		// 清除cookie即可彻底清除缓存
		CookieSyncManager.createInstance(context);
		CookieManager.getInstance().removeAllCookie();
	}

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
									String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(intent);
		}

	}

	public void releaseAllWebViewCallback() {

		if (android.os.Build.VERSION.SDK_INT < 16) {

			try {

				Field field = WebView.class.getDeclaredField("mWebViewCore");
				field = field.getType().getDeclaredField("mBrowserFrame");
				field = field.getType().getDeclaredField("sConfigCallback");
				field.setAccessible(true);
				field.set(null, null);
			} catch (NoSuchFieldException e) {
//				if (BuildConfig.DEBUG) {
//					e.printStackTrace();
//				}
			} catch (IllegalAccessException e) {
//				if (BuildConfig.DEBUG) {
//					e.printStackTrace();
//				}
			}
		} else {
			try {
				Field sConfigCallback = Class.forName(
						"android.webkit.BrowserFrame").getDeclaredField(
						"sConfigCallback");
				if (sConfigCallback != null) {
					sConfigCallback.setAccessible(true);
					sConfigCallback.set(null, null);
				}
			} catch (NoSuchFieldException e) {
//				if (BuildConfig.DEBUG) {
//					e.printStackTrace();
//				}
			} catch (ClassNotFoundException e) {
//				if (BuildConfig.DEBUG) {
//					e.printStackTrace();
//				}
			} catch (IllegalAccessException e) {
//				if (BuildConfig.DEBUG) {
//					e.printStackTrace();
//				}
			}
		}
	}



}
