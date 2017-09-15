package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.pop.CircleAddPop;
import cn.vko.ring.common.widget.pop.TxFilterPop;
import cn.vko.ring.utils.WebViewUtil;

/**
 * 收入明细
 * @author shikh
 * @time 16/9/27 下午8:11
 */
public class TxBrgDetailActivity extends BaseActivity {

    @Bind(R.id.webview)
    public BridgeWebView mWebView;
    @Bind(R.id.tv_title)
    public TextView tvTitle;
    @Bind(R.id.tv_right)
    public TextView tvRight;

    private TxFilterPop pop;
    private Drawable dewUp,dewDown;
    @Override
    public int setContentViewResId() {
        return R.layout.activity_my_in_come;
    }

    @Override
    public void initView() {
        tvTitle.setText("明细");
        tvRight.setText("全部");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setCompoundDrawablePadding(10);
        dewDown = getResources().getDrawable(R.drawable.title_drawabledown);
        dewUp = getResources().getDrawable(R.drawable.title_drawableup);
        toggle(false);
        initWebView();
    }
    public void toggle(boolean isOpen) {
        isOpen = !isOpen;
        if (isOpen) {
            switchDrawable(tvRight, dewUp);
        } else {
            switchDrawable(tvRight, dewDown);
        }
    }
    public void switchDrawable(TextView tv, Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        tv.setCompoundDrawables(null, null, drawable, null);// 画在右边
    }

    private void initWebView() {
        new WebViewUtil(this, mWebView, "TxBrgDetailActivity");
        //提现
        mWebView.registerHandler("logDetail", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                if (data != null) {
                    try {
                        JSONObject json = JSONObject.parseObject(data);
                        String url = json.getString("url");
                        Bundle b = new Bundle();
                        b.putString("URL",url);
                        StartActivityUtil.startActivity(TxBrgDetailActivity.this,TxLogDetailActivity.class,b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    } catch (Exception e) {

                    }
                }
            }
        });

    }
    @OnClick(R.id.tv_right)
    void onFilterDteail(){
        toggle(true);
        if (pop == null) {
            pop = new TxFilterPop(this);
            pop.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String filter = "-1";
                    if(view.getId() == R.id.tv_sell){
                        filter = "1";
                        tvRight.setText("卖课");
                    }else if(view.getId() ==R.id.tv_tx){
                        filter = "2";
                        tvRight.setText("提现");
                    }else{
                        tvRight.setText("全部");
                    }
		            onFilterData(filter);
                    pop.dismiss();
                }
            });
            pop.update(0, 0, ViewUtils.getScreenWidth(this)/ 5,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggle(false);
                }
            });
        }
         pop.showAsDropDown(tvRight);
    }
    private void  onFilterData(String filter) {
        mWebView.callHandler("getCashLogs", filter,
                new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.e("========>data",data);
                    }
                });
    }


    @Override
    public void initData() {
        String url = getIntent().getExtras().getString("URL");
        mWebView.loadUrl(url);
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

    @OnClick(R.id.iv_back)
    void sayBack() {
        finish();
    }
}
