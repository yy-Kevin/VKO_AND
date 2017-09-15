package cn.vko.ring.study.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.NoScrollListView;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.activity.MainActivity;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.activity.PeiyouDetailActivity;
import cn.vko.ring.study.adapter.PeiyCourseListAdapter;
import cn.vko.ring.study.adapter.TopicCourseListAdapter;
import cn.vko.ring.study.model.PeiyIsBuyInfo;
import cn.vko.ring.study.model.PeiyTopicDetailInfo;
import cn.vko.ring.study.model.CourceInfo;
import cn.vko.ring.study.model.TopicInfo;
import cn.vko.ring.study.model.VideoAttributes;
import cn.vko.ring.utils.ImageUtils;

/**
 * Created by A on 2016/12/5.
 */
public class PeiyCourseListPresenter implements UIDataListener<PeiyTopicDetailInfo>, AdapterView.OnItemClickListener {
    private TopicInfo info;
    private VolleyUtils<PeiyTopicDetailInfo> volley;
    private VolleyUtils<PeiyIsBuyInfo> buyVolley;
    private Activity act;
    private NoScrollListView mListView;
    private PeiyCourseListAdapter mAdapter;
    private TextView topicIntro;

    private double sellPrice;

    private String goodsId;
    private String objId;
    private Context context;
    public void getData(String goodsId,String objId){
        this.goodsId=goodsId;
        this.objId=objId;
        getCourseListTask(goodsId,objId);
    }

    public PeiyCourseListPresenter(Activity act, NoScrollListView mListView, TopicInfo info, TextView tvIntro) {
        this.info = info;
        this.act = act;
        this.mListView = mListView;
        this.topicIntro = tvIntro;
        initListView();
        volley = new VolleyUtils<PeiyTopicDetailInfo>(act,PeiyTopicDetailInfo.class);
        buyVolley = new VolleyUtils<PeiyIsBuyInfo>(act,PeiyIsBuyInfo.class);
//        getCourseListTask();
    }
    /**
     *
     */
    private void initListView() {


        // TODO Auto-generated method stub
        // mListView.setPullLoadEnable(false);
        // mListView.setPullRefreshEnable(false);
        mListView.setOnItemClickListener(this);
        mAdapter = new PeiyCourseListAdapter(act);
        // mAdapter.judgeHasEmpty();
        mListView.setAdapter(mAdapter);


    }

    public void getCourseListTask(String goodsId,String objId) {
        // TODO Auto-generated method stub

        String url = ConstantUrl.VKOIP.concat("/zbpy/videoList/").concat(goodsId).concat("/");

        Uri.Builder builder = volley.getBuilder(url);
        builder.appendQueryParameter("objId",objId);
        builder.appendQueryParameter("token", VkoContext.getInstance(act).getUser().getToken());
//        Log.e("======>info.getObjId",objId);
        volley.setUiDataListener(this);
        volley.sendGETRequest(true,builder);
        volley.setUiDataListener(new UIDataListener<PeiyTopicDetailInfo>() {
            @Override
            public void onDataChanged(PeiyTopicDetailInfo peiyInfo) {
                if (peiyInfo != null) {
                    if (peiyInfo.getCode() == 0) {
                        PeiyTopicDetailInfo.PeiyDetailInfo base = peiyInfo.getData();
                        if (base != null && base.getObj() != null) {
                            topicIntro.setText(base.getObj().getIntro());
//                    tvTopiceName.setText(base.getObj().getName());
//                    tvChapter.setText(base.getObj().getClassTotal());
                            info.setSubjectId(base.getObj().getSubjectId());
                            info.setSellPrice(base.getObj().getSellPrice());
                            info.setObjId(base.getObj().getObjId());
                            info.setId(base.getObj().getId());
                            mAdapter.setInfo(info);
                        }
                        if (base != null && base.getVideos() != null && base.getVideos().size() > 0) {
                            mAdapter.add(base.getVideos());
                        }
                    }
                }
                mAdapter.judgeHasEmpty();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
            }
        });

        //检查是否购买
        String url1 = ConstantUrl.VKOIP.concat("/zbpy/checkBuy");
        Uri.Builder builder1 = volley.getBuilder(url1);
        builder1.appendQueryParameter("id", goodsId);
        builder1.appendQueryParameter("token", VkoContext.getInstance(act).getToken() + "");
        buyVolley.sendGETRequest(true,builder1);

        buyVolley.setUiDataListener(new UIDataListener<PeiyIsBuyInfo>() {
            @Override
            public void onDataChanged(PeiyIsBuyInfo isBuyInfo) {
                if (isBuyInfo != null) {
                    if (isBuyInfo.getData() != null) {
                        String isBuy=isBuyInfo.getData();
                        info.setIsbuy(isBuy);
                        Log.e("======>isBuy",isBuy);
                    }
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
//                mAdapter.postNotifyDataSetChanged();
            }
        });
    }

    public void stopUp(XListView xlistview) {
        xlistview.stopLoadMore();
        xlistview.stopRefresh();
        xlistview.setRefreshTime(DateUtils.getCurDateStr());
    }
    public TopicInfo getInfo() {
        return info;
    }
    //	List<IntegraSectionCourse> intelist;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        if (mAdapter.getList() == null || mAdapter.getList().size() == 0) {
            getCourseListTask(goodsId,objId);
        } else {
            CourceInfo info1 = mAdapter.getListItem(position);
            long startTime=info1.getStartTime();//开始时间
            long endTime=info1.getEndTime();//培优结束时间
//            Log.e("=======startTime",startTime+"");
//            Log.e("=======endTime",endTime+"");
            if (info1.getState().equals("1")){
                Toast.makeText(act, "直播还未开始", Toast.LENGTH_SHORT).show();
                Log.e("======>info.getIsbuy()",info.getIsbuy());
            }else if (info1.getState().equals("2")){
                //获取当前时间
                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String time = formatter.format(curDate);
                long nowTimeQ=curDate.getTime();

                long timeCha=nowTimeQ-startTime;
                Log.e("======>timeCha时间差毫秒",timeCha+"");

                if(info1 != null){
                    VideoAttributes video = new VideoAttributes();
                    video.setVideoId(info1.getId());
                    video.setVideoName(info1.getName());
                    video.setCourseType("46");
                    video.setGoodsId(info.getId());
                    video.setSellPrice(info.getSellPrice());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("video", video);

                    Log.e("======>info.getIsbuy()",info.getIsbuy());
                    if ((info.getIsbuy()).equals("true")){
                        bundle.putInt("TYPE", 1);
                        bundle.putInt("NOW", 1);
                        bundle.putLong("TIME",timeCha);
                    }else if ((info.getIsbuy()).equals("false")){
                        bundle.putInt("TYPE", 4);
                        bundle.putInt("NOW", 0);
                        bundle.putLong("TIME",timeCha);
                    }
                    StartActivityUtil.startActivity(act, CourseVideoViewActivity.class,bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
            }else if (info1.getState().equals("3")){

                if(info1 != null){
                    VideoAttributes video = new VideoAttributes();
                    video.setVideoId(info1.getId());
//                video.setBookId(info.getObjId());
                    video.setVideoName(info1.getName());
                    video.setCourseType("46");
                    video.setGoodsId(info.getId());
                    Log.e("======>goosId列表页",info.getId()+"");
                    Log.e("======>info1.getName()",info1.getName()+"");
                    Log.e("======>info.getObjId()",info.getObjId()+"");
                    Log.e("======>info1.getId()",info1.getId()+"");
                    Log.e("======>SellPrice()",info.getSellPrice()+"");
                    video.setSellPrice(info.getSellPrice());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("video", video);
                    if ((info.getIsbuy()).equals("true")){
                        bundle.putInt("TYPE", 1);
                    }else if ((info.getIsbuy()).equals("false")){
                        bundle.putInt("TYPE", 4);
                    }
                    StartActivityUtil.startActivity(act, CourseVideoViewActivity.class,bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
                }
            }
        }
    }

    @Override
    public void onDataChanged(PeiyTopicDetailInfo t) {
        if (t != null) {
            if (t.getCode() == 0) {
                PeiyTopicDetailInfo.PeiyDetailInfo base = t.getData();
                if (base != null && base.getObj() != null) {
                    topicIntro.setText(base.getObj().getIntro());
//                    tvTopiceName.setText(base.getObj().getName());
//                    tvChapter.setText(base.getObj().getClassTotal());
                    info.setSubjectId(base.getObj().getSubjectId());
                    mAdapter.setInfo(info);
                }
                if (base != null && base.getVideos() != null && base.getVideos().size() > 0) {
                    mAdapter.add(base.getVideos());
                }
            }
        }
        mAdapter.judgeHasEmpty();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {
        if (mAdapter.judgeHasEmpty()) {
            mAdapter.notifyDataSetChanged();
        }
    }


}
