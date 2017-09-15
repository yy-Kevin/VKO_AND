package cn.vko.ring.circle.activity;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.Bind;
import butterknife.OnClick;

import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.umeng.BaseUMShare;
import cn.vko.ring.common.widget.dialog.ShapeDialog;
import cn.vko.ring.utils.WebViewUtil;

/**
 * 试卷
 * 
 * @author Administrator
 * 
 */
public class TestPageActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	@Bind(R.id.imagebtn)
	public ImageView imgShare;
	
	private String shapeUrl, msgTitle;
	private ShapeDialog shapeDialog;
	private ShareAction shareAction;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_advert;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		imgShare.setImageResource(R.drawable.icon_shape);
		imgShare.setVisibility(View.VISIBLE);
		new WebViewUtil(this, mWebView,"TestPageActivity");
		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if(msgTitle == null){
					msgTitle = title;
				}
				tvTitle.setText(title);
			}

		};
		// 设置setWebChromeClient对象
		mWebView.setWebChromeClient(wvcc);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String url = getIntent().getExtras().getString("URL");
		String pagerId = getIntent().getExtras().getString("PAGERID");
		shareAction = new ShareAction(this).withTitle(msgTitle).withTargetUrl(shapeUrl) .withExtra(new UMImage(this, R.drawable.ic_logo));
		mWebView.loadUrl(url);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		}else{
			finish();
		}
	}
	
	@OnClick(R.id.imagebtn)
	public void onShareClick() {
		if (shapeDialog == null) {
			shapeDialog = new ShapeDialog(this);
			shapeDialog.setCanceledOnTouchOutside(true);
			shapeDialog.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if (shareAction != null) {
						if (position == 0) {
							new BaseUMShare(TestPageActivity.this, SHARE_MEDIA.WEIXIN).shareAction(shareAction);
						} else if (position == 1) {
							new BaseUMShare(TestPageActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE).shareAction(shareAction);
						} else if (position == 2) {
							new BaseUMShare(TestPageActivity.this, SHARE_MEDIA.QQ).shareAction(shareAction);
						} else if (position == 3) {
							new BaseUMShare(TestPageActivity.this, SHARE_MEDIA.QZONE).shareAction(shareAction);
						}
					} else {
						Toast.makeText(TestPageActivity.this, "没有分享数据", Toast.LENGTH_LONG).show();
					}
					shapeDialog.dismiss();
				}
			});
		}
		shapeDialog.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (VkoContext.getInstance(this).isLogin()) {
			WebViewUtil.synCookies(this, "http://vko.cn",
					"vkoweb=" + VkoContext.getInstance(this).getToken()
							+ ";domain=.vko.cn");
			WebViewUtil.synCookies(this, "http://vko.cn",
					"userId=" + VkoContext.getInstance(this).getUserId()
							+ ";domain=.vko.cn");
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mWebView.releaseAllWebViewCallback();
		super.onDestroy();
	}
}
