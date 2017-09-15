package cn.vko.ring.study.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import butterknife.Bind;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.study.activity.SearchTestDetailActivity;
import cn.vko.ring.utils.WebViewUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;


/**
 * 
 * 搜索试题列表
 * 
 * @author Administrator
 * 
 */
public class SearchTestFragment extends BaseFragment {

	@Bind(R.id.webview)
	public BridgeWebView mWebView;
	private String url = "http://m.vko.cn/search/search.html";
	private StringBuffer sb;
	private String seacherString;

	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.layout_webview;
	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
		new WebViewUtil(atx, mWebView, "SearchTestFragment");
		// 进入搜索试题详情
		mWebView.registerHandler("toExamDetail", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				if (data != null) {
					JSONObject json = JSONObject.parseObject(data);
					String url = json.getString("url");
					String examId = json.getString("examId");
					Bundle bundle = new Bundle();
					bundle.putString("URL", url);
					bundle.putString("EXAMID", examId);
					StartActivityUtil.startActivity(atx,
							SearchTestDetailActivity.class, bundle,
							Intent.FLAG_ACTIVITY_SINGLE_TOP);

				}
			}

		});

		sb = new StringBuffer(url);
		String gradeid = VkoContext.getInstance(atx).getGradeId();
		sb.append("?token=" + VkoContext.getInstance(atx).getToken());

		sb.append("&learnId=" +("-1".equals(gradeid) ? "" : gradeid));
	}

	public void setlistVideo(String seacherString) {
		if (TextUtils.isEmpty(seacherString)) {
			return;
		}
		if (seacherString.equals(this.seacherString)) {
			return;
		}
		this.seacherString = seacherString;
		// 搜索试题
		if(mWebView != null && sb != null){
			mWebView.loadUrl(sb.toString()+"&key=" + seacherString);
		}
	}

}
