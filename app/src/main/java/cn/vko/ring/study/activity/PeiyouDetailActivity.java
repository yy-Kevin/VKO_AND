package cn.vko.ring.study.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.BitmapUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.NoScrollListView;
import cn.vko.ring.local.presenter.PayLocalPresenter;
import cn.vko.ring.local.presenter.PayPeiyPresenter;
import cn.vko.ring.study.model.IntegratedCourse;
import cn.vko.ring.study.model.PeiyIsBuyInfo;
import cn.vko.ring.study.model.PeiyTopicDetailInfo;
import cn.vko.ring.study.model.TopicInfo;
import cn.vko.ring.study.model.VideoAttributes;
import cn.vko.ring.study.presenter.PeiyCourseListPresenter;
import cn.vko.ring.study.widget.TeacherHeadView;
import cn.vko.ring.utils.ImageUtils;

public class PeiyouDetailActivity extends BaseActivity {

    @Bind(R.id.tv_py_pay)
    public TextView tvPay;
    @Bind(R.id.tv_title)
    public TextView tvTitle;
    @Bind(R.id.lv_py_course)
    public NoScrollListView mListView;
    @Bind(R.id.tv_py_chapter)
    public TextView tvChapter;
    @Bind(R.id.tv_py_topice_name)
    public TextView tvTopiceName;
//    @Bind(R.id.hsv_py_teacher)
//    public HorizontalScrollView hsvTeacher;
    @Bind(R.id.tv_py_price)
    public TextView tvPrice;
//    @Bind(R.id.tv_py_buy)
//    public TextView ivBuy;
    @Bind(R.id.iv_py_topic)
    public ImageView ivPic;
    @Bind(R.id.tv_py_intro)
    public TextView tvIntro;
    @Bind(R.id.tv_py_intro_title)
    public TextView tvIntroTitle;
    private TopicInfo info;
    private Drawable dewUp, dewDown;
    private PeiyCourseListPresenter mPresenter;

    private VolleyUtils<PeiyTopicDetailInfo> volley;
    private VolleyUtils<PeiyIsBuyInfo> buyVolley;

    private String goodsId;
    private String objId;
    private double price;
    public String isBuy;

    private PayPeiyPresenter payPresenter;//购买培优课

    private VideoAttributes vab;

    @Override
    public int setContentViewResId() {
        return R.layout.activity_peiyou_detail;


    }

    @Override
    public void initView() {
        tvTitle.setText("培优直播");

        goodsId=getIntent().getExtras().getString("goodsId");
        objId=getIntent().getExtras().getString("objId");

        info=new TopicInfo();

        dewDown = getResources().getDrawable(R.drawable.class_special_conunfold1);
        dewUp = getResources().getDrawable(R.drawable.class_special_conclose1);


        getPeiyDetail();
        getIsBuy();


    }


    private void getIsBuy() {
        buyVolley = new VolleyUtils<PeiyIsBuyInfo>(this,PeiyIsBuyInfo.class);

        getIsBuyInfo();
    }

    private void getIsBuyInfo() {
        String url = ConstantUrl.VKOIP.concat("/zbpy/checkBuy");
        Uri.Builder builder = volley.getBuilder(url);
        builder.appendQueryParameter("id", goodsId);
        builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken() + "");
        buyVolley.sendGETRequest(true,builder);

        buyVolley.setUiDataListener(new UIDataListener<PeiyIsBuyInfo>() {
            @Override
            public void onDataChanged(PeiyIsBuyInfo peiyInfo) {
                if (peiyInfo != null) {
                    if (peiyInfo.getData() != null) {
                        isBuy=peiyInfo.getData();
                        if(isBuy.equals("true")){
                            tvPay.setText("已购买");
                        }else if (isBuy.equals("false")){
                            tvPay.setText("购买");
                        }
                    }
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
//                mAdapter.postNotifyDataSetChanged();
            }
        });
    }

    private void getPeiyDetail() {
        volley = new VolleyUtils<PeiyTopicDetailInfo>(this,PeiyTopicDetailInfo.class);

        getPeiyInfo();
    }

    private void getPeiyInfo() {
//        String url = "http://192.168.1.246:8109/zbpy/videoList/2318/?objId=19410";
//        String url = "http://192.168.1.246:8109/zbpy/videoList/".concat(goodsId).concat("/");
        String url = ConstantUrl.VKOIP.concat("/zbpy/videoList/").concat(goodsId).concat("/");

        Uri.Builder builder = volley.getBuilder(url);
        builder.appendQueryParameter("objId", objId);
        builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken() + "");
        volley.sendGETRequest(true,builder);

        volley.setUiDataListener(new UIDataListener<PeiyTopicDetailInfo>() {
            @Override
            public void onDataChanged(PeiyTopicDetailInfo peiyInfo) {
                if (peiyInfo != null) {
                    if (peiyInfo.getData().getObj() != null) {
                        info=peiyInfo.getData().getObj();
                        String chapter = getResources().getString(R.string.chapter_text, info.getClassTotal());
//                        price = getResources().getString(R.string.price_text,info.getSellPrice());
                        price=info.getSellPrice();
                        tvChapter.setText(chapter);
                        tvPrice.setText("￥"+price);
                        Log.e("====peiy===info",info.getName()+"");
                        tvTopiceName.setText(info.getName());
//                        ImageUtils.loadPerviewImage(info.getPic(), 100,300,ivPic);
//                        ImageUtils.loadPerviewImage(info.getPic(),100,100,ivPic);
                        ImageUtils.loadNoWHImage(info.getPic(),ivPic);
//                        if(info.isIsbuy()){
////                            ivBuy.setVisibility(View.GONE);
//                        }
//            TeacherHeadView v = new TeacherHeadView(this);
//            v.addData(info.getTeacher());
//            hsvTeacher.addView(v);
                    }
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
//                mAdapter.postNotifyDataSetChanged();
            }
        });
    }


    @Override
    public void initData() {

        mPresenter = new PeiyCourseListPresenter(this, mListView, info,tvIntro);
        mPresenter.getData(goodsId,objId);
    }

    //去购买
    @OnClick(R.id.tv_py_pay)
    void toPay(){
        String isBuy=tvPay.getText().toString();
        if (isBuy.equals("已购买")){
            Toast.makeText(this, "您已购买本直播", Toast.LENGTH_LONG).show();
        }else {
            lockLocalVideoTask();
        }
    }
    private void lockLocalVideoTask() {
        // TODO Auto-generated method stub
        if(payPresenter == null){
            payPresenter = new PayPeiyPresenter(this);
        }
        Log.e("======>goosId开始",info.getId());
        payPresenter.getData(goodsId,price);
    }

    @OnClick(R.id.tv_py_intro_title)
    void sayDisplay(){
        if(tvIntro.isShown()){
            tvIntro.setVisibility(View.GONE);
            switchDrawable(dewDown);
        }else{
            if(info != null && info.getIntro() != null){
                tvIntro.setText(info.getIntro());
            }else{
                info = mPresenter.getInfo();
                if(info != null && info.getIntro() != null){
                    tvIntro.setText(info.getIntro());
                }
            }
            tvIntro.setVisibility(View.VISIBLE);
            switchDrawable(dewUp);
        }
    }
    @OnClick(R.id.iv_back)
    void sayBack(){
        finish();
    }

    public void switchDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight()); // 设置边界
        tvIntroTitle.setCompoundDrawables(null, null, drawable, null);// 画在右边
        tvIntroTitle.setCompoundDrawablePadding(10);
    }


}
