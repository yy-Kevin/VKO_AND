package cn.vko.ring.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;

import cn.vko.ring.circle.activity.AdvertActivity;
import cn.vko.ring.circle.activity.ApplyJoinActivity;
import cn.vko.ring.circle.activity.GroupDetailActivity;
import cn.vko.ring.circle.activity.GroupHomeActivity;
import cn.vko.ring.circle.activity.GroupListActivity;
import cn.vko.ring.circle.activity.PhotoGalleryActivity;
import cn.vko.ring.circle.activity.RankListActivity;
import cn.vko.ring.circle.activity.TestPageActivity;
import cn.vko.ring.circle.activity.TopicDetailH5Activity;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;
import cn.vko.ring.circle.event.TopicEvent;

import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.circle.model.ImageInfo;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.home.model.CancleCollectInfo;
import cn.vko.ring.mine.activity.MembersCenterActivity;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.model.VideoAttributes;


public class WebViewUtil {
	private String articleId;
	private Activity atx;
	private BridgeWebView mWebView;
	private String from;
	private boolean isFirst;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			isFirst = false;
		};
	};

	public WebViewUtil(Activity atx, BridgeWebView mWebView, String from) {
		this.atx = atx;
		this.mWebView = mWebView;
		this.from = from;
		initWebView();
	}

	public void initWebView() {
		WebSettings set = mWebView.getSettings();
		set.setDefaultTextEncodingName("UTF-8");// 设置字符编码
		//
		// set.setUseWideViewPort(true);
		// set.setLoadWithOverviewMode(true);
		// 打开本地缓存提供js调用
		set.setDomStorageEnabled(true);
		set.setAppCacheMaxSize(1024 * 1024 * 200);
		set.setAppCachePath(FileUtil.getWebViewDir().getAbsolutePath());
		set.setAllowFileAccess(true);
		set.setAppCacheEnabled(true);

		// 文章详情
		mWebView.registerHandler("articleDetail", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String url = json.getString("url");
					String articleId = json.getString("articleId");
					Bundle bundle = new Bundle();
					bundle.putString("ARTICLEID", articleId);
					bundle.putString("FROM", from);
					if (!isFirst) {
						isFirst = true;
						if (TextUtils.isEmpty(url)) {
							// ViewUtils.startActivity(atx,TopicDetailActivity.class,bundle,Intent.FLAG_ACTIVITY_NEW_TASK);
						} else {
							bundle.putString("URL", url);
							StartActivityUtil.startActivity(atx,
									TopicDetailH5Activity.class, bundle,
									Intent.FLAG_ACTIVITY_NEW_TASK);
						}
						mHandler.sendEmptyMessageDelayed(0, 1000);
					}
				}
			}

		});
		// 点击学习圈、兴趣圈
		mWebView.registerHandler("toGroup", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String url = json.getString("url");
					String title = json.getString("title");
					Bundle bundle = new Bundle();
					bundle.putString("URL", url);
					bundle.putString("TITLE", title);
					StartActivityUtil.startActivity(atx, GroupListActivity.class,
							bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);

				}
			}

		});
		// ..
		mWebView.registerHandler("WebViewJavascriptBridgeReady", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {


				}
			}

		});

		// 我加入的群
		mWebView.registerHandler("myJoinGroup", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String url = json.getString("url");
					String groupId = json.getString("groupId");
					Bundle bundle = new Bundle();
					bundle.putString("GROUPID", groupId);
					bundle.putString("URL", url);
					if (from.equals("TopicDetailH5Activity")
							&& ActivityUtilManager
									.getInstance()
									.isContains(
											"cn.vko.ring.circle.activity.GroupHomeActivity")) {
						atx.finish();
					} else {
						StartActivityUtil.startActivity(atx, GroupHomeActivity.class,
								bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
					}
					// if(!from.equals("TopicDetailH5Activity")){
					// ViewUtils.startActivity(atx,
					// GroupHomeActivity.class,bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
					// }
				}
			}

		});

		// 轮播广告
		mWebView.registerHandler("adCarousel", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String url = json.getString("url");
					Bundle bundle = new Bundle();
					bundle.putString("URL", url);
					StartActivityUtil.startActivity(atx, AdvertActivity.class, bundle,
							Intent.FLAG_ACTIVITY_SINGLE_TOP);

				}
			}

		});

		// 置顶广告
		mWebView.registerHandler("adClick", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String url = json.getString("url");
					Bundle bundle = new Bundle();
					bundle.putString("URL", url);
					StartActivityUtil.startActivity(atx, AdvertActivity.class, bundle,
							Intent.FLAG_ACTIVITY_SINGLE_TOP);

				}
			}

		});
		// 开通会员
		mWebView.registerHandler("openMember", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				StartActivityUtil.startActivity(atx, MembersCenterActivity.class);
			}

		});

		// 图片浏览
		mWebView.registerHandler("viewImgsDetail", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String urls = json.getString("urls");
					int currIndex = json.getIntValue("currIndex");
					List<ImageInfo> imgs = JSONArray.parseArray(urls,
							ImageInfo.class);
					if (imgs != null && imgs.size() > 0) {
						PhotoGalleryActivity.startImageInfo(atx, imgs,
								currIndex);
					}
				}
			}

		});
		// 关注群
		mWebView.registerHandler("refreshPage", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				EventManager.fire(new Event<GroupInfo>(null, Event.JOIN_EVENT));
			}

		});
		// 登录
		mWebView.registerHandler("toLogin", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// 清除webView cookiet
				WebViewUtil.clearCache(atx);
				StartActivityUtil.startActivity(atx, LoginActivity.class);
				atx.overridePendingTransition(R.anim.bottom_in, 0);
			}

		});
		// 视频列表
		mWebView.registerHandler("videoDetail", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (!VkoContext.getInstance(atx).doLoginCheckToSkip(atx)) {
					if (data != null) {
						JSONObject json = JSONObject.parseObject(data);
						String videoId = json.getString("videoId");
						String groupId = json.getString("groupId");
						VideoAttributes video = new VideoAttributes();
						video.setGoodsId(groupId);
						video.setVideoId(videoId);
						Bundle bundle = new Bundle();
						bundle.putSerializable("video", video);
						bundle.putInt("TYPE", -1);
						StartActivityUtil.startActivity(atx,
								CourseVideoViewActivity.class, bundle,
								Intent.FLAG_ACTIVITY_SINGLE_TOP);
					}
				}

			}
		});
		// 群组详情
		mWebView.registerHandler("toGroupDetail", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String groupId = json.getString("groupId");
					Bundle bundle = new Bundle();
					bundle.putString("GROUPID", groupId);
					StartActivityUtil.startActivity(atx, GroupDetailActivity.class,
							bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
				}

			}
		});
		// 试卷
		mWebView.registerHandler("toPaper", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String groupId = json.getString("paperId");
					String url = json.getString("url");
					Bundle bundle = new Bundle();
					bundle.putString("PAGERID", groupId);
					bundle.putString("URL", url);
					StartActivityUtil.startActivity(atx, TestPageActivity.class,
							bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
				}

			}
		});

		// 加入群
		mWebView.registerHandler("toApply", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String groupId = json.getString("groupIds");
					String pid = json.getString("pId");
					GroupInfo group = new GroupInfo();
					group.setId(groupId);
					group.setParentType(pid);
					Bundle bundle = new Bundle();
					bundle.putSerializable("GROUP", group);
					StartActivityUtil.startActivity(atx, ApplyJoinActivity.class,
							bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);

				}
			}
		});

		// 排行榜
		mWebView.registerHandler("rankListClick", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String url = json.getString("url");
					String title = json.getString("title");
					Bundle bundle = new Bundle();
					bundle.putString("URL", url);
					bundle.putString("TITLE", title);
					StartActivityUtil.startActivity(atx, RankListActivity.class,
							bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);

				}
			}
		});

		// 取消收藏
		mWebView.registerHandler("toFavorite", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					int type = json.getIntValue("title");
					String articleId = json.getString("articleId");
					EventBus.getDefault().post(new CancleCollectInfo(articleId));
				}
			}
		});

		// 发表评论成功
		mWebView.registerHandler("toCommentRefresh", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				EventManager.fire(new TopicEvent<Integer>(1,
						TopicEvent.SUBMIT_TOPIC_EVENT));
			}
		});
		// 会员续费
		mWebView.registerHandler("orderInfo", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				// System.out.println("data" + data);
				if (data != null) {
					JSONObject jsonObject = JSONObject.parseObject(data);
					String orderId = jsonObject.getString("orderId");
					String totalFee = jsonObject.getString("amount");
					String subject = jsonObject.getString("goodsName");
					String discountInfo = jsonObject.getString("discountInfo");
					Bundle bundle = new Bundle();
					bundle.putString("SUBJECT", subject);
					bundle.putString("TOTALFEE", totalFee);
					bundle.putString("ORDERID", orderId);
					bundle.putString("DISCOUNT", discountInfo);
//					StartActivityUtil.startActivity(atx,
//							ToPayActivity.class, bundle,
//							Intent.FLAG_ACTIVITY_NEW_TASK);
				}
			}
		});

	}

	public static void clearCache(Context context) {
		// loadDataWithBaseURL(null, "","text/html", "utf-8",null);
		// File file = CacheManager.getCacheFileBaseDir();
		// if (file != null && file.exists() && file.isDirectory()) {
		// for (File item : file.listFiles()) {
		// item.delete();
		// }
		// file.delete();
		// }
		try {
			context.deleteDatabase("webview.db");
			context.deleteDatabase("webviewCache.db");
			// 清除cookie即可彻底清除缓存
			CookieSyncManager.createInstance(context);
			CookieManager.getInstance().removeAllCookie();
		} catch (Exception e) {

		}
	}

	public void setCookie() {
		try {
			mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
			if (VkoContext.getInstance(atx).isLogin()) {
				synCookies(atx, "http://vko.cn", "vkoweb=" + VkoContext.getInstance(atx).getToken()
								+ ";domain=.vko.cn");
				synCookies(atx, "http://vko.cn", "userId=" + VkoContext.getInstance(atx).getUserId()
								+ ";domain=.vko.cn");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 同步一下cookie
	 */
	public static void synCookies(Context context, String url, String cookies) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) { // 2.3及以下
			CookieSyncManager.createInstance(context.getApplicationContext());
		}
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.setCookie(url, cookies);
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			CookieSyncManager.getInstance().sync();
		}
	}


}
