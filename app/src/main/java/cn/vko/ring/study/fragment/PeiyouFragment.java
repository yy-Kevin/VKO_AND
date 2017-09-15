package cn.vko.ring.study.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;



import butterknife.Bind;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.widget.SubjectFilterLayout;
import cn.vko.ring.home.model.SubjectInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.study.activity.PeiyouDetailActivity;
import cn.vko.ring.study.model.BaseSubjectInfoCourse;
import cn.vko.ring.study.presenter.TopicListPresenter;
import cn.vko.ring.utils.WebViewUtil;


public class PeiyouFragment extends BaseFragment implements
        IOnClickItemListener<SubjectInfo> ,UIDataListener<BaseSubjectInfoCourse> {
    @Bind(R.id.webview)
    public BridgeWebView mWebView;
//    @Bind(R.id.peiyou_subject)
//    public SubjectFilterLayout mSubjectLayout;
//    @Bind(R.id.tv_peiyou_select)
//    public TextView tv_select;
//    @Bind(R.id.tv_peiyou_grade)
//    public TextView tv_grade;
//    @Bind(R.id.py_xlistview)
//    public XListView mListView;
    private TopicListPresenter presenter;

    public UserInfo user;
    String subjectId="-1";
    String gradeId;

//    private PopupWindow popupWindow;




    @Override
    public int setContentViewId() {
        return R.layout.fragment_peiyou;
    }

    @Override
    public void initView(View root) {

        initWebView();

        if (subjectId.equals("-1")){
            createJson();
        }

        //添加培优界面年级信息
        user = VkoContext.getInstance(atx).getUser();

//        if (user.getGrade() != null && !user.getGrade().isEmpty()) {
//            tv_grade.setText(user.getGrade());
//        }else {
//            tv_grade.setText("");
//        }
//
////        presenter =	new TopicListPresenter(getActivity(), mListView,1);
//
//        mSubjectLayout.setInitData(null, this);
//        mSubjectLayout.setEvent(tv_select, VkoContext.getInstance(getContext()).getGradeId(), 1);
//        mSubjectLayout.initData(VkoContext.getInstance(getContext()).getGradeId(), 0);
    }


    private void initWebView() {
        mWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        mWebView.requestFocusFromTouch();


        new WebViewUtil(getActivity(), mWebView, "PeiyouFragment");

        //进入培优详情
        mWebView.registerHandler("didSelectedLiveListAtIndex", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                Log.e("=======data值>",data);
                if (data != null) {
                    try {
                        JSONObject json = JSONObject.parseObject(data);
                        String goodsId = json.getString("goodsId");
                        String objId = json.getString("objId");
                        Bundle b = new Bundle();
                        b.putString("goodsId",goodsId);
                        b.putString("objId",objId);
                        StartActivityUtil.startActivity(getActivity(),PeiyouDetailActivity.class,b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        mWebView.loadUrl("http://static.vko.cn/m/activity/pyLive/home.html?ver=1111");
        super.initData();
    }

    @Override
    public void onItemClick(int position, SubjectInfo subjectInfo, View v) {

        UserInfo user=new UserInfo();
        subjectId=subjectInfo.getSubjectId();
        gradeId=user.getGradeId();

        createJson();

    }

    // 生成json
    public String createJson() {
        try {

            JSONObject data = new JSONObject();
            data.put("subject", subjectId);
            data.put("grade", VkoContext.getInstance(getContext()).getGradeId());
            onFilterData(data.toString());
            return data.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    private void  onFilterData(String data) {
        mWebView.callHandler("getLiveListBySubjectAndGrade", data,
                new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                    }
                });
    }



    @Override
    public void onDataChanged(BaseSubjectInfoCourse response) {
    }

    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {

    }


}