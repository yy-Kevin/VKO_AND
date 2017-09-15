package cn.vko.ring.study.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.crop.camera.gallery.Image;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.SystemBarTintManager;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.local.presenter.PayLocalPresenter;
import cn.vko.ring.study.model.ParamNewSyncAndComp;
import cn.vko.ring.study.model.VideoAttributes;
import cn.vko.ring.study.presenter.GetExamInfoPresenter;
import cn.vko.ring.utils.WebViewUtil;

public class CourseDetailActivity extends BaseActivity {

    @Bind(R.id.webview)
    public BridgeWebView mWebView;
    @Bind(R.id.iv_back)
    public ImageView iv_back;

    private int i;


    private GetExamInfoPresenter mGetExamInfoPresenter;
    private ParamNewSyncAndComp paramNewSyncAndComp;

    private PayLocalPresenter payPresenter;//购买

    @Override
    public int setContentViewResId() {
        // TODO Auto-generated method stub
        return R.layout.activity_course_detail;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        tintManager.setStatusBarTintResource(R.color.black);
        initWebView();
        getUrl();

    }

    private void getUrl() {
        String url=mWebView.getUrl();
    }


    private void initWebView() {
        new WebViewUtil(this, mWebView, "MyClassActivity");

        mWebView.registerHandler("toCoursePlay", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("=======去播放data值>", data+"");
                if (data != null) {
                    try {
                        JSONObject json = JSONObject.parseObject(data);
                        String bookId=json.getString("bookId");
                        String goodsId=json.getString("goodsId");
                        String videoId=json.getString("videoId");
                        String sectionId=json.getString("sectionId");
                        String subjectId=json.getString("subjectId");
                        String VideoName=json.getString("VideoName");
                        String courseType=json.getString("courseType");
                        String BookName=json.getString("BookName");

                        VideoAttributes vab=new VideoAttributes();
                        vab.setBookId(bookId);
                        vab.setGoodsId(goodsId);
                        vab.setVideoId(videoId);
                        vab.setSectionId(sectionId);
                        vab.setBookName(BookName);
                        vab.setSubjectId(subjectId==null?null:subjectId);
                        //综合视频
                        vab.setCourseType(courseType);
                        vab.setVideoName(VideoName);

                        Bundle bundle=new Bundle();
                        bundle.putSerializable("video", vab);
                        bundle.putInt("TYPE", 2);
                        StartActivityUtil.startActivity(CourseDetailActivity.this, CourseVideoViewActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    } catch (Exception e) {

                    }
                }
            }
        });

        mWebView.registerHandler("toCoursePay", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("=======去支付data值>", data+"");
                if (data != null) {
                    try {
                        JSONObject json = JSONObject.parseObject(data);
                        String bookId=json.getString("bookId");
                        String goodsId=json.getString("goodsId");
                        String videoId=json.getString("videoId");
                        String sectionId=json.getString("sectionId");
                        String subjectId=json.getString("subjectId");
                        String VideoName=json.getString("VideoName");
                        String courseType=json.getString("courseType");
                        String BookName=json.getString("BookName");
                        double sellPrice=json.getDoubleValue("sellPrice");

                        VideoAttributes vab=new VideoAttributes();
                        vab.setBookId(bookId);
                        vab.setGoodsId(goodsId);
                        vab.setVideoId(videoId);
                        vab.setSectionId(sectionId);
                        vab.setBookName(BookName);
                        vab.setSellPrice(sellPrice);
                        vab.setSubjectId(subjectId==null?null:subjectId);
                        //综合视频
                        vab.setCourseType(courseType);
                        vab.setVideoName(VideoName);

                        if(payPresenter == null){
                            payPresenter = new PayLocalPresenter(CourseDetailActivity.this);
                        }
                        payPresenter.initData(vab);


//                        Bundle bundle=new Bundle();
//                        bundle.putSerializable("video", vab);
//                        bundle.putInt("TYPE", 2);
//                        StartActivityUtil.startActivity(CourseDetailActivity.this, CourseVideoViewActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    } catch (Exception e) {

                    }
                }
            }
        });

        mWebView.registerHandler("toExamTest", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("=======去练习测评data值>", data+"");
                if (data != null) {
                    try {
                        JSONObject json = JSONObject.parseObject(data);
                        String courseId=json.getString("courseId");
                        String learnId=json.getString("learnId");
                        String subjectId=json.getString("subjectId");
                        String knowledgeId=json.getString("knowledgeId");
                        String courseType=json.getString("courseType");
                        String courseName=json.getString("courseName");
                        String bookId=json.getString("bookId");
                        String sectionId=json.getString("sectionId");
                        String k1=json.getString("k1");


                        if (courseType.equals("43")){
                            Log.e("=======去练习courseType综合>", courseType+"");
                            paramNewSyncAndComp=new ParamNewSyncAndComp(subjectId,"43",courseId,courseName,"3",k1,false);
                        }else {
                            Log.e("=======去练习courseType>", courseType+"");
                            paramNewSyncAndComp=new ParamNewSyncAndComp(courseId,bookId,subjectId,courseType,courseName,"3",sectionId,false);
                        }

                        mGetExamInfoPresenter = new GetExamInfoPresenter(CourseDetailActivity.this, paramNewSyncAndComp);
                        mGetExamInfoPresenter.getData();


                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    void goBack() {

//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
////                mWebView.setText(url);
//                Log.e("=====url",url);
//                if (url.contains("flag")){
//                    i=1;
//                }else {
//                    i=0;
//                }
//                Log.e("=====i",i+"");
//
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
////                view.loadUrl("file:///android_asset/error.html");
////                mWebView.setText("404 error");
//                mWebView.setVisibility(View.GONE);
//
//            }
//        });
//        Log.e("=====","走这里了goback");
//
//        Log.e("===back==i",i+"");
        if ( mWebView.canGoBack()&& i ==0) {
            mWebView.goBack();// 返回前一个页面
            mWebView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                }
            });
        }else {
            finish();
        }
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


}
