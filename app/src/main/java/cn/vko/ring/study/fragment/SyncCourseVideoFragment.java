package cn.vko.ring.study.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.adapter.MyCourseSectionAdapter;
import cn.vko.ring.study.model.KnowledgeSection;
import cn.vko.ring.study.model.TonBuCourse;
import cn.vko.ring.study.model.TonBuCourseSection;
import cn.vko.ring.study.model.VideoAttributes;

/**
 * Created by shikh on 2016/5/11.
 */
public class SyncCourseVideoFragment extends BaseFragment implements AdapterView.OnItemClickListener,UIDataListener<TonBuCourse>{
    public KnowledgeSection knowsection;
    public MyCourseSectionAdapter myCourseSectionAdapter;

    @Bind(R.id.lv_courseVideo)
    public ListView lvCourseVideo;

//    private List<TonBuCourseSection> data;
    private int type;
    private VolleyUtils<TonBuCourse> volley;
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int setContentViewId() {
        return R.layout.fragment_course_video_list;
    }

    @Override
    public void initView(View root) {
        myCourseSectionAdapter=new MyCourseSectionAdapter(atx);
        lvCourseVideo.setAdapter(myCourseSectionAdapter);
        lvCourseVideo.setOnItemClickListener(this);
    }

    public void getKnowDataTask(KnowledgeSection knowsection) {
        if(knowsection == null) return;
        this.knowsection=knowsection;
        if(volley == null){
            volley=new VolleyUtils<TonBuCourse>(atx,TonBuCourse.class);
        }
        String synchurl = ConstantUrl.VKOIP.concat("/tb/videoList");
        Uri.Builder builder = volley.getBuilder(synchurl);
        builder.appendQueryParameter("token", VkoContext.getInstance(atx).getUser().getToken());
        builder.appendQueryParameter("bookId", knowsection.getBookId()+"");
        builder.appendQueryParameter("sectionId", knowsection.getChapterId()+"");
        builder.appendQueryParameter("pageIndex", 1+"");
        builder.appendQueryParameter("pageSize", 50+"");
        volley.setUiDataListener(this);
        volley.sendGETRequest(true,builder);
    }

    @Override
    public void initData(){
        super.initData();
        type = getArguments().getInt("TYPE");
        knowsection = (KnowledgeSection) getArguments()
                .getSerializable("KnowledgeSection");
        getKnowDataTask(knowsection);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(!VkoContext.getInstance(atx).isLogin()){
            VkoContext.getInstance(atx).doLoginCheckToSkip(atx);
            return;
        }
        if(myCourseSectionAdapter.judgeHasEmpty()){
            getKnowDataTask(knowsection);
            return;
        }
        TonBuCourseSection  tbcs=myCourseSectionAdapter.getListItem(position);
        VideoAttributes vab=new VideoAttributes();
        vab.setBookId(knowsection.getBookId());
        vab.setGoodsId(knowsection.getGoodsId());
        vab.setVideoId(tbcs.getVideoId());

        vab.setSectionId(knowsection.getChapterId());
        vab.setBookName(knowsection.getChapterName());
        vab.setSubjectId(knowsection.getSubjectInfo()==null?null:knowsection.getSubjectInfo().getId());
        //同步视频
        vab.setCourseType("41");

        Bundle bundle=new Bundle();
        bundle.putSerializable("video", vab);
        bundle.putInt("TYPE", type);
        StartActivityUtil.startActivity(atx, CourseVideoViewActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    @Override
    public void onDataChanged(TonBuCourse response) {
        if(response!=null && response.getData()!=null){
            myCourseSectionAdapter.clear();
            myCourseSectionAdapter.add( response.getData());
        }
        myCourseSectionAdapter.judgeHasEmpty();
        myCourseSectionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {
        myCourseSectionAdapter.judgeHasEmpty();
        myCourseSectionAdapter.notifyDataSetChanged();
    }
}
