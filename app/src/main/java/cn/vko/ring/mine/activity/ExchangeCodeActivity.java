package cn.vko.ring.mine.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.model.BaseExeInfo;
import cn.vko.ring.mine.model.BaseExeInfo;
import cn.vko.ring.study.model.PeiyIsBuyInfo;

public class ExchangeCodeActivity extends BaseActivity implements
        UIDataListener<BaseExeInfo>{
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0x10;
    private TextView tvLogin;
    private Button butExchange;
    private EditText edCtat;
    private Context context;
    private VolleyUtils<BaseExeInfo> volleyUtils;

    @Override
    public int setContentViewResId() {
        return R.layout.activity_exchange_code;
    }

    @Override
    public void initView() {
        tvLogin = (TextView) findViewById(R.id.tv_title);
        butExchange = (Button) this.findViewById(R.id.but_exchange);
        edCtat = (EditText) this.findViewById(R.id.ed_exchange_ctat);
    }

    @OnClick(R.id.but_exchange)
    public void freeBackClick() {
        if (TextUtils.isEmpty(edCtat.getText().toString().trim())) {
            Toast.makeText(this, "输入内容", Toast.LENGTH_SHORT).show();
            return;
        }


        freeBackTask();
    }

    private void freeBackTask() {
        String url = ConstantUrl.VKOIP + "/exchangeCode/convertCode";
        VolleyUtils<BaseExeInfo> volleyUtils = new VolleyUtils<BaseExeInfo>(this,BaseExeInfo.class);
        UserInfo user = VkoContext.getInstance(this).getUser();
        if (user == null) {
            return;
        }
        Map<String,String> params = new HashMap<String,String>();
        params.put("convertCode", edCtat.getText().toString().trim());
        params.put("learnId", user.getLearn());
        Log.e("=======>learnId",user.getLearn()+"");
        params.put("token", user.getToken());
        volleyUtils.setUiDataListener(this);
        volleyUtils.sendPostRequest(true,url,params);

    }


    @Override
    public void initData() {
        tvLogin.setText("会员体验码");
    }

    @OnClick(R.id.iv_back)
    public void imgFinish() {
        this.finish();
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // EventCountAction.onActivityResumCount(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


    @Override
    public void onDataChanged(BaseExeInfo data) {
        if (data != null && data.getCode() == 0) {
            Log.e("======>兑换成功","走了这里");
            Toast.makeText(this, "兑换成功", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.putExtra("type", "0");
            setResult(1000,intent);
            finish();
        }
    }

    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {
//        Log.e("======>code","走了这里");
    }
}
