/*
 *MyClassActivity.java [V 1.0.0]
 *classes : cn.vko.ring.mine.views.MyClassActivity
 *宣义阳 Create at 2015-8-3 下午6:00:45
 */
package cn.vko.ring.mine.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.model.VideoAttributes;
import cn.vko.ring.utils.WebViewUtil;

/**
 * @author 施坤海
 *         create at 2015-8-3 下午6:00:45
 */
public class MyClassActivity extends BaseActivity {

    @Bind(R.id.webview)
    public BridgeWebView mWebView;
    @Bind(R.id.tv_title)
    public TextView tvTitle;
    @Bind(R.id.tv_right)
    public TextView tvRight;

    //	private String[] titles = new String[] {"本地课","同步课程","综合课程"};
//	private String[] titles = new String[] {"同步课程","综合课程"};
//	@Bind(R.id.tv_title)
//	public TextView tvTitle;
//	@Bind(R.id.my_viewpager)
//	public NoScrollViewPager mViewPager;
//	@Bind(R.id.layout_indicator)
//	public SimpleViewPagerIndicator mIndicator;
//
//	private CourseFragmentPagerAdapter mAdapter;
//	private List<BaseFragment> fragments;
    @Override
    public int setContentViewResId() {
        // TODO Auto-generated method stub
        return R.layout.activity_myclass1;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
//		EventBus.getDefault().register(this);
//        tvTitle.setText("我的收入");
        tvTitle.setText("我的课程");
        tvRight.setVisibility(View.GONE);
        initWebView();

    }

    private void initWebView() {
        Log.e("=====initWebView","走了");
        new WebViewUtil(this, mWebView, "MyClassActivity");

        //
        mWebView.registerHandler("toPlayBrg", new BridgeHandler() {
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
                        StartActivityUtil.startActivity(MyClassActivity.this, CourseVideoViewActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    } catch (Exception e) {

                    }
                }
            }
        });

    }

    @OnClick(R.id.iv_back)
    void onBack() {
        if ( mWebView.canGoBack()) {
            mWebView.goBack();// 返回前一个页面
            mWebView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
//                    Log.e("=====走了","换title");
                    tvTitle.setText(title);
                }
            });
        }else {
            finish();
        }
    }

    @Override
    public void initData() {

        mWebView.loadUrl("http://static.vko.cn/m/course/courseList.html");
        //2017年4月6日10:12:01  我的课程改H5注释
//		fragments = new ArrayList<BaseFragment>();
//		fragments.add(new LocalCourseFragment());
//		fragments.add(new SynchrCourseFragment());
//		fragments.add(new ComprehensiveFragment());
//		mViewPager.setClipChildren(false);
//		mViewPager.setOffscreenPageLimit(2);
//		mAdapter = new CourseFragmentPagerAdapter(getSupportFragmentManager(), fragments);
//		mViewPager.setAdapter(mAdapter);
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
