package cn.vko.ring.circle.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;

import cn.vko.ring.circle.model.StringRequestInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.IClickAlertListener;
import cn.vko.ring.common.listener.IOnClickItemListener;

import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.BaseUMShare;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.dialog.ChosePicDialog;
import cn.vko.ring.common.widget.dialog.UserVbDialog;
import cn.vko.ring.common.widget.dialog.ShapeDialog;
import cn.vko.ring.common.widget.dialog.VbDialog;
import cn.vko.ring.home.model.BtnInfo;
import cn.vko.ring.home.presenter.VbDealPrsenter;
import cn.vko.ring.mine.activity.PlaceOrderActivity;
import cn.vko.ring.utils.ActivityUtilManager;
import cn.vko.ring.utils.WebViewUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class TopicDetailH5Activity extends BaseActivity implements
		UMShareListener {

	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.imagebtn)
	public ImageView ivMore;
	@Bind(R.id.image_other)
	public ImageView ivShape;
	private ChosePicDialog mDialog;
	private String articleId;
	private ShapeDialog shapeDialog;
	private ShareAction shareAction;
	private VolleyUtils<StringRequestInfo> volley;
	// private CommonDialog dialog;
	private UserVbDialog dialog;
	private BaseUMShare unShare;
	private Map<String, String> paramsMap = new HashMap<String, String>();

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.layout_html;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		tvTitle.setText("话题详情");
		ivMore.setVisibility(View.VISIBLE);
		ivShape.setVisibility(View.VISIBLE);
		ivMore.setImageResource(R.drawable.icon_head_more);
		initWebView();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String url = getIntent().getExtras().getString("URL");
		if (!TextUtils.isEmpty(url) && url.contains("articleId=")) {
			articleId = url.substring(url.indexOf("articleId=") + 10);
		}
		mWebView.loadUrl(url);

	}

	private void initWebView() {
		// TODO Auto-generated method stub
		new WebViewUtil(this, mWebView, "TopicDetailH5Activity");
		// 分享群 数据
		mWebView.registerHandler("shareArticle", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					try {
						JSONObject json = JSONObject.parseObject(data);
						String title = json.getString("title");
						String summary = json.getString("summary");
						String url = json.getString("url");
						if (!TextUtils.isEmpty(summary)
								|| !TextUtils.isEmpty(title)) {
							shareAction = new ShareAction(TopicDetailH5Activity.this).withText(summary).withTitle(title).withTargetUrl(url) .withExtra(new UMImage(TopicDetailH5Activity.this, R.drawable.ic_logo)).withMedia(new UMImage(TopicDetailH5Activity.this, R.drawable.ic_logo));
						}
					} catch (Exception e) {

					}
					
				}
			}
		});

		mWebView.registerHandler("toReport", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					try {
						JSONObject json = JSONObject.parseObject(data);
						String objId = json.getString("objId");
						String articleId = json.getString("articleId");
						boolean isDel = json.getBooleanValue("isdel");
						handMore(3, objId, articleId, isDel);
					} catch (Exception e) {

					}
					
				}
			}
		});

		// 打赏
		mWebView.registerHandler("toAwardVb", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					try {
						JSONObject json = JSONObject.parseObject(data);
						String articleId = json.getString("articleId");
						String crUserId = json.getString("crUserId");
						int score = json.getIntValue("score");
						int needScore = json.getIntValue("needScore");
						paramsMap.put("articleId", articleId);
						paramsMap.put("crUserId", crUserId);
						paramsMap.put("extendType", "3");
						showHintLockDialog(score, needScore, articleId,crUserId);
					} catch (Exception e) {

					}

				}
			}
		});
		// 点赞送V币
		mWebView.registerHandler("toUpZan", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					try {

					} catch (Exception e) {

					}
					JSONObject json = JSONObject.parseObject(data);
					int vb = json.getIntValue("vb");
					new VbDialog(TopicDetailH5Activity.this).showDialog(vb);
				}
			}
		});
		// 评论送V币
		mWebView.registerHandler("toCommentVb", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					try {

					} catch (Exception e) {

					}
					JSONObject json = JSONObject.parseObject(data);
					int vb = json.getIntValue("vb");
					new VbDialog(TopicDetailH5Activity.this).showDialog(vb);
				}
			}
		});

	}

	protected void showHintLockDialog(final int score, final int needScore,
			final String articleId, final String crUserId) {
		// TODO Auto-generated method stub
		final int surplus = score - needScore;
		if (dialog == null) {
			dialog = new UserVbDialog(this);
			dialog.setContentText("你需要花费" + needScore + "V币打赏");
			dialog.setBtnInfo(new BtnInfo("取消", R.color.white,
					R.drawable.shape_gray_btn), new BtnInfo(surplus >= 0 ? "打赏"
					: "充值", R.color.white,
					R.drawable.selector_light_blue_button));
			dialog.setHeadImage(R.drawable.head_gratuity);
			dialog.setOnClickItemListener(new IOnClickItemListener<String>() {

				@Override
				public void onItemClick(int position, String t, View v) {
					// TODO Auto-generated method stub
					if (position == 0) {
						dialog.dismiss();
					} else {
						if (surplus >= 0) {
							buyCourseTask(articleId, crUserId);
						} else {
							Bundle bundle = new Bundle();
							bundle.putInt("HAVESCORE", score);
							bundle.putDouble("NEEDSCORE", needScore);
							bundle.putString("SUBJECT", "打赏话题");
							bundle.putString("EXTENDINFO",
									JSON.toJSONString(paramsMap, true));
							StartActivityUtil.startActivity(TopicDetailH5Activity.this,
									PlaceOrderActivity.class, bundle,
									Intent.FLAG_ACTIVITY_SINGLE_TOP);
							dialog.dismiss();
						}
					}
				}
			});
		}
		dialog.show();
	}

	protected void buyCourseTask(String articleId, String crUserId) {
		// TODO Auto-generated method stub
		if (volley == null) {
			volley = new VolleyUtils<StringRequestInfo>(this,StringRequestInfo.class);
		}
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VKOIP
				+ "/groups/award");
		builder.appendQueryParameter("articleId", articleId);
		builder.appendQueryParameter("userId", crUserId);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getToken());
		volley.sendGETRequest(true,builder);
		volley.setUiDataListener(new UIDataListener<StringRequestInfo>() {
			@Override
			public void onDataChanged(StringRequestInfo response) {
				if (response != null && response.getCode() == 0) {// 打赏成功
					// 局部刷新群详情
					mWebView.callHandler("refreshDetail",
							"data from java", new CallBackFunction() {
								@Override
								public void onCallBack(String data) {

								}
							});
				}
				if(response != null&&response.getData() != null){
						if(response.getData().equals("true")){
							Toast.makeText(TopicDetailH5Activity.this, "打赏成功", Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(TopicDetailH5Activity.this, response.getData(), Toast.LENGTH_LONG).show();
						}

				}
				dialog.dismiss();
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Override
	protected boolean isEnableSwipe() {
		// TODO Auto-generated method stub
		ActivityUtilManager.getInstance().delActivity(this);// 不能及时删除的问题
		return true;
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		// ActivityUtilManager.getInstance().finishActivity(this);
		finish();
	}

	@OnClick(R.id.imagebtn)
	void sayChosePic(View v) {
		handMore(4, null, articleId, false);
	}

	private void handMore(final int type, final String objId,
			final String articleId, boolean isDel) {
		if (mDialog == null) {
			mDialog = new ChosePicDialog(this);
			mDialog.setCanceledOnTouchOutside(true);
			mDialog.setOnClickAlertListener(new IClickAlertListener() {

				@Override
				public void onClick(int t) {
					// TODO Auto-generated method stub
					if (t == 2) {
						Bundle bundle = new Bundle();
						if (type == 3) {
							bundle.putString("OBJID", objId);
						} else {
							bundle.putString("OBJID", articleId);
						}
						bundle.putInt("TYPE", type);
						StartActivityUtil.startActivity(TopicDetailH5Activity.this,
								ReportActivity.class, bundle,
								Intent.FLAG_ACTIVITY_SINGLE_TOP);
					} else if (t == 1) {// 删除
						delReplyTask(objId, articleId);
					} else if (t == 3) {// 回复
						try {
							mWebView.callHandler("commentReplyReg",
									"data from java", new CallBackFunction() {
										@Override
										public void onCallBack(String data) {

										}
									});
						} catch (Exception e) {

						}

					}
				}
			});

		}
		if (type == 3) {
			mDialog.setTextViewWord("删除", isDel, "举报", true, "回复", true);
		} else {
			mDialog.setTextViewWord("删除", isDel, "举报", true);
		}

		mDialog.show();
	}

	@OnClick(R.id.image_other)
	void sayShape() {
		if (shapeDialog == null) {
			shapeDialog = new ShapeDialog(this);
			shapeDialog.setCanceledOnTouchOutside(true);
			shapeDialog.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					BaseUMShare umShare = null;
					if (shareAction != null) {
						// Toast.makeText(TopicDetailH5Activity.this,
						// shareContent.toString(), 0).show();
						if (position == 0) {
							umShare = new BaseUMShare(TopicDetailH5Activity.this,SHARE_MEDIA.WEIXIN);
						} else if (position == 1) {
							umShare = new BaseUMShare(TopicDetailH5Activity.this,SHARE_MEDIA.WEIXIN_CIRCLE);
						} else if (position == 2) {
							umShare = new BaseUMShare(TopicDetailH5Activity.this,SHARE_MEDIA.QQ);
						} else if (position == 3) {
							umShare = new BaseUMShare(TopicDetailH5Activity.this,SHARE_MEDIA.QZONE);
						}
						if(umShare != null){
							umShare.shareAction(shareAction);
							umShare.setShareListener(TopicDetailH5Activity.this);
						}
					} else {
						Toast.makeText(TopicDetailH5Activity.this, "没有分享数据", Toast.LENGTH_LONG)
								.show();
					}
					shapeDialog.dismiss();
				}
			});
		}
		shapeDialog.show();
	}

	protected void delReplyTask(String objId, String articleId) {
		// TODO Auto-generated method stub
		if (volley == null) {
			volley = new VolleyUtils<StringRequestInfo>(this,StringRequestInfo.class);
		}
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", objId);
		params.put("articleId", articleId);
		volley.sendPostRequest(true,ConstantUrl.VK_CIRCLE_DEL_REPLY,params);
		volley.setUiDataListener(new UIDataListener<StringRequestInfo>() {
			@Override
			public void onDataChanged(StringRequestInfo response) {
				if (response != null && response.getCode() == 0) {
					// 局部刷新群详情
					mWebView.callHandler("refreshDetail",
							"data from java", new CallBackFunction() {
								@Override
								public void onCallBack(String data) {

								}
							});
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			// mWebView.reload();
			mWebView.getClass().getMethod("onPause")
					.invoke(mWebView, (Object[]) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		mWebView.callHandler("stopVedio", "data from java",
				new CallBackFunction() {
					@Override
					public void onCallBack(String data) {

					}
				});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			mWebView.getClass().getMethod("onResume")
					.invoke(mWebView, (Object[]) null);

			if (VkoContext.getInstance(this).isLogin()) {
				WebViewUtil.synCookies(this, "http://vko.cn",
						"vkoweb=" + VkoContext.getInstance(this).getToken()
								+ ";domain=.vko.cn");
				WebViewUtil.synCookies(this, "http://vko.cn",
						"userId=" + VkoContext.getInstance(this).getUserId()
								+ ";domain=.vko.cn");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Subscribe
	public void onEventMainThread(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			if (msg.equals(Constants.PAYMENT_COMPLETE)) {
				// 局部刷新群详情
				mWebView.callHandler("refreshDetail", "data from java",
						new CallBackFunction() {
							@Override
							public void onCallBack(String data) {

							}
						});
				Toast.makeText(TopicDetailH5Activity.this, "打赏成功",  Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		mWebView.releaseAllWebViewCallback();
		super.onDestroy();
	}

	@Override
	public void onResult(SHARE_MEDIA share_media) {
		new VbDealPrsenter(this).doRequest(VbDealPrsenter.SHARE_ARTICLE);
	}

	@Override
	public void onError(SHARE_MEDIA share_media, Throwable throwable) {

	}

	@Override
	public void onCancel(SHARE_MEDIA share_media) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}
}
