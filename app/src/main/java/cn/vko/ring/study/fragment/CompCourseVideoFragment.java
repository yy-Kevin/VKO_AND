package cn.vko.ring.study.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.Bind;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.adapter.MyCompressSectionAdapter;
import cn.vko.ring.study.model.ComprehensiveCourses;
import cn.vko.ring.study.model.CompressSection;
import cn.vko.ring.study.model.CompressVideo;
import cn.vko.ring.study.model.VideoAttributes;

/**
 * 综合课 视频列表
 * Created by shikh on 2016/5/11.
 */
public class CompCourseVideoFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.lv_courseVideo)
    public ListView lvCourseVideo;
    private VolleyUtils<CompressSection> volley;
    private int type;
    public MyCompressSectionAdapter mAdapter;
    private ComprehensiveCourses compreCourse;

    public void setType(int type) {
        this.type = type;
    }
    @Override
    public int setContentViewId() {
        return R.layout.fragment_course_video_list;
    }

    @Override
    public void initView(View root) {
        mAdapter=new MyCompressSectionAdapter(atx);
        lvCourseVideo.setAdapter(mAdapter);
        lvCourseVideo.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        type = getArguments().getInt("TYPE");
        compreCourse = (ComprehensiveCourses) getArguments().getSerializable("MyComPressAndTestActivity");
        setCompreCourse(compreCourse);
    }

    public void setCompreCourse(ComprehensiveCourses compreCourse) {
        this.compreCourse = compreCourse;
        if(compreCourse!=null) {
            if (volley == null) {
                volley = new VolleyUtils<CompressSection>(atx, CompressSection.class);
            }
            String url = ConstantUrl.VKOIP.concat("/zonghe/videoList");
            Uri.Builder builder = volley.getBuilder(url);
            builder.appendQueryParameter("token", VkoContext.getInstance(atx).getToken() + "");
            if (compreCourse.getTypeFrom() == 1) {
                builder.appendQueryParameter("objId", compreCourse.getId() + "");
            } else {
                builder.appendQueryParameter("k1", compreCourse.getId() + "");
            }
            volley.setUiDataListener(new UIDataListener<CompressSection>() {
                @Override
                public void onDataChanged(CompressSection response) {
                    if (response != null) {
                        if (response.getData() != null) {
                            mAdapter.clear();
                            mAdapter.add(response.getData());
                        }
                    }
//                    mAdapter.judgeHasEmpty();
                    mAdapter.postNotifyDataSetChanged();
                }

                @Override
                public void onErrorHappened(String errorCode, String errorMessage) {
//                    mAdapter.judgeHasEmpty();
                    mAdapter.postNotifyDataSetChanged();
                }
            });
            volley.sendGETRequest(true, builder);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if(mAdapter.judgeHasEmpty()){
            setCompreCourse(compreCourse);
            return;
        }
        if(!VkoContext.getInstance(atx).isLogin()){
            VkoContext.getInstance(atx).doLoginCheckToSkip(atx);
            return;
        }

        CompressVideo compVideo=mAdapter.getListItem(position);
        if(compVideo==null){
            return;
        }
        VideoAttributes vab=new VideoAttributes();
        vab.setBookId(compVideo.getObjId());
        vab.setGoodsId(compVideo.getGoodsId());
        vab.setVideoId(compVideo.getVideoId());
        vab.setSectionId(compreCourse.getId());
        vab.setBookName(compreCourse.getName());
        vab.setSubjectId(compreCourse.getSubjectInfo()==null?null:compreCourse.getSubjectInfo().getId());
        //综合视频
        vab.setCourseType("43");

        Bundle bundle=new Bundle();
        bundle.putSerializable("video", vab);
        bundle.putInt("TYPE", type);
        StartActivityUtil.startActivity(atx,CourseVideoViewActivity .class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
}
