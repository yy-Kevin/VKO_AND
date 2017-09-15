package cn.vko.ring.home.widget;

import android.content.Context;
import android.net.Uri.Builder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseDialog;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.UserInfo;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.netease.nim.uikit.common.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class ChooseGradeDialog extends BaseDialog implements
        View.OnClickListener {
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    private RadioButton radioButton7;
    private int temp;
    private VkoContext vkoContext;
    private Context context;
    private RadioButton radioButton8;
    private RadioButton radioButton9;
    private RadioButton radioButton10;
    private RadioButton radioButton11;
    private RadioButton radioButton12;

    public ChooseGradeDialog(Context context) {
        super(context, R.style.SelectGradeDialog);
        // TODO Auto-generated constructor stub
        this.context = context;
        vkoContext = VkoContext.getInstance(context);
    }

    @Override
    public void setUpViews(View root) {
        // TODO Auto-generated method stub
        radioButton1 = (RadioButton) root.findViewById(R.id.tv_juniorone);
        radioButton2 = (RadioButton) root.findViewById(R.id.tv_juniortwo);
        radioButton3 = (RadioButton) root.findViewById(R.id.tv_juniorthree);
        radioButton4 = (RadioButton) root.findViewById(R.id.tv_highone);
        radioButton5 = (RadioButton) root.findViewById(R.id.tv_hightwo);
        radioButton6 = (RadioButton) root.findViewById(R.id.tv_highthree);
        radioButton7 = (RadioButton) root.findViewById(R.id.tv_lowhone);
        radioButton8 = (RadioButton) root.findViewById(R.id.tv_lowhtwo);
        radioButton9 = (RadioButton) root.findViewById(R.id.tv_lowhthree);
        radioButton10 = (RadioButton) root.findViewById(R.id.tv_lowfour);
        radioButton11 = (RadioButton) root.findViewById(R.id.tv_lowfive);
        radioButton12 = (RadioButton) root.findViewById(R.id.tv_lowsix);


    }

    @Override
    public void setUpListener() {
        // TODO Auto-generated method stub
        // 为每个RadioButton设置监听器
        radioButton1.setOnClickListener(this);
        radioButton2.setOnClickListener(this);
        radioButton3.setOnClickListener(this);
        radioButton4.setOnClickListener(this);
        radioButton5.setOnClickListener(this);
        radioButton6.setOnClickListener(this);
        radioButton7.setOnClickListener(this);
        radioButton8.setOnClickListener(this);
        radioButton9.setOnClickListener(this);
        radioButton10.setOnClickListener(this);
        radioButton11.setOnClickListener(this);
        radioButton12.setOnClickListener(this);
    }

    @Override
    public boolean getCancelableOnclick() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getGravity() {
        // TODO Auto-generated method stub
        return Gravity.CENTER;
    }

    @Override
    public int getRootId() {
        // TODO Auto-generated method stub
        return R.layout.dialog_select_grade;
    }

    @Override
    public int getAnimatStly() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public LayoutParams getILayoutParams() {
        // TODO Auto-generated method stub
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        return lp;
    }

    @Override
    public void onClick(View v) {
        LogUtil.i("YUYAO", "aaaaaaaa----");
        // TODO Auto-generated method stub
        radioButton1.setChecked(false);
        radioButton2.setChecked(false);
        radioButton3.setChecked(false);
        radioButton4.setChecked(false);
        radioButton5.setChecked(false);
        radioButton6.setChecked(false);
        radioButton7.setChecked(false);
        radioButton8.setChecked(false);
        radioButton9.setChecked(false);
        radioButton10.setChecked(false);
        radioButton11.setChecked(false);
        radioButton12.setChecked(false);
        String grade = "";
        switch (v.getId()) {
            case R.id.tv_juniorone:
                radioButton1.setChecked(true);
                grade = radioButton1.getText().toString();
                temp = 0;
                break;
            case R.id.tv_juniortwo:
                radioButton2.setChecked(true);
                grade = radioButton2.getText().toString();
                temp = 1;
                break;
            case R.id.tv_juniorthree:
                radioButton3.setChecked(true);
                grade = radioButton3.getText().toString();
                temp = 2;
                break;
            case R.id.tv_highone:
                radioButton4.setChecked(true);
                grade = radioButton4.getText().toString();
                temp = 3;
                break;
            case R.id.tv_hightwo:
                radioButton5.setChecked(true);
                grade = radioButton5.getText().toString();
                temp = 4;
                break;
            case R.id.tv_highthree:
                radioButton6.setChecked(true);
                grade = radioButton6.getText().toString();
                temp = 5;
                break;
            case R.id.tv_lowhone:
                radioButton7.setChecked(true);
                grade = radioButton7.getText().toString();
                temp = 6;
                break;
            case R.id.tv_lowhtwo:
                radioButton8.setChecked(true);
                grade = radioButton8.getText().toString();
                temp = 7;
                break;
            case R.id.tv_lowhthree:
                radioButton9.setChecked(true);
                grade = radioButton9.getText().toString();
                temp = 8;
                break;
            case R.id.tv_lowfour:
                radioButton10.setChecked(true);
                grade = radioButton10.getText().toString();
                temp = 9;
                break;
            case R.id.tv_lowfive:
                radioButton11.setChecked(true);
                grade = radioButton11.getText().toString();
                temp = 10;
                break;
            case R.id.tv_lowsix:
                radioButton12.setChecked(true);
                grade = radioButton12.getText().toString();
                temp = 11;
                break;
            default:
                temp = -1;
                grade = "";
                break;
        }
        saveGrade(temp, grade);
    }

    private void saveGrade(final int gradeId, final String grade) {
        String url = ConstantUrl.VKOIP + "/index/saveGrade";
        VolleyUtils<String> volleyUtils = new VolleyUtils<String>(context, String.class);
        Map<String, String> params = new HashMap<String, String>();
        Builder builder = volleyUtils.getBuilder(url);
        params.put("gradeId", String.valueOf(gradeId));
        params.put("token", VkoContext.getInstance(context).getToken());
        volleyUtils.sendPostRequest(true, url, params);
        volleyUtils.setUiDataListener(new UIDataListener<String>() {
            @Override
            public void onDataChanged(String response) {
                if (response != null) {
                    JSONObject jsonObject = JSONObject.parseObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        UserInfo user = vkoContext.getUser();
                        user.setGradeId(String.valueOf(gradeId));
                        user.setGrade(grade);
                        VkoContext.getInstance(context).setUser(user);
//						notifySubjectUpdate();
                        dismiss();
                    }
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {

            }
        });

    }

    private void notifySubjectUpdate() {
        EventBus.getDefault().post(Constants.SUBJECT_REFRESH);
    }

}
