package cn.vko.ring.study.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.IBookClickListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.model.Book;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.study.adapter.SyncCourseListAdapter;
import cn.vko.ring.study.model.KnowledgeSection;
import cn.vko.ring.study.model.ParamData;
import cn.vko.ring.study.model.Section;
import cn.vko.ring.study.model.SubjectInfoCourse;
import cn.vko.ring.study.presenter.GetBookVersionPresenter;

/**
 * 同步课程列表
 * Created by shikh on 2016/5/10.
 */
public class SyncCourseListActivity extends  BaseCourseListActivity{
    @Bind(R.id.lv_courseVideo)
    public XListView mCourseListView;
    @Bind(R.id.tv_select_book)
    public TextView tvTitle;
    @Bind(R.id.tv_tocomplex)
    public ImageView tv_tocomplex;
    @Bind(R.id.book_and_version_container)
    public LinearLayout mContainer;
    @Bind(R.id.book_select_bg)
    public View bookBg;

    private VolleyUtils<Section> mVolleyUtils;
    private List<KnowledgeSection> mKnowModelDatas;
    private KnowledgeSection knowsection;
//    private SubjectInfoCourse mSubjectBookModel;
    private GetBookVersionPresenter getBookVersionPresenter;
    private SyncCourseListAdapter mAdapter;
    @Override
    public XListView setXListView() {
            return mCourseListView;
    }

    @Override
    public void initView() {
        mAdapter = new SyncCourseListAdapter(this);
        super.initView();
        mCourseListView.setAdapter(mAdapter);
        subjectInfo = (SubjectInfoCourse) getIntent().getExtras().getSerializable("MyCourseSetionActivity");
        initBookLay();
        setBookAndVersion();
    }

    @Override
    public int setContentViewResId() {
        return R.layout.activity_course_setion;
    }

    private void initBookLay() {
        Drawable dewUp = getResources().getDrawable(R.drawable.title_drawabledown);
        dewUp.setBounds(0, 0, dewUp.getMinimumWidth(), dewUp.getMinimumHeight()); // 设置边界
        tvTitle.setCompoundDrawables(null, null, dewUp, null);
    }

    private void setBookAndVersion() {
        getBookVersionPresenter = new GetBookVersionPresenter(this, mContainer, new ParamData.Builder(this)
                .setSubjectId(subjectInfo.getId()).setBookId(subjectInfo.getBookId()).setBookVersionId(subjectInfo.getVersion())
                .build(), bookBg);
        getBookVersionPresenter.setBookClickListener(new IBookClickListener() {
            @Override
            public void onBookSelected(Book book) {
                String title = getTitle(book.getBookname());
                tvTitle.setText(title);
                subjectInfo.setBookId(book.getBookid());
                VkoConfigure.getConfigure(SyncCourseListActivity.this).put("bookId", book.getBookid()+"");
                subjectInfo.setVersion(book.getParamData().getBookVersionId());
                subjectInfo.setType("1");
                subjectInfo.setRefreshHomeSubject(true);
                getCourseListTask();
            }
        });
    }
    private String getTitle(String name) {
        int len=0;
        len= ViewUtils.getScreenDensity(this)>2?12:8;
        String title;
        if(name.trim().contains(" ")){
            name = name.substring(name.indexOf(" ")+1);
        }
        if(name.length() >len){
            title = name.substring(0,len);
        }else{
            title = name;
        }
        return title;
    }
    @Override
    public void getCourseListTask() {
        if(mVolleyUtils == null){
            mVolleyUtils = new VolleyUtils(this,Section.class);
        }
        getCourseListData(subjectInfo,true);
    }

    private void getCourseListData(SubjectInfoCourse s, Boolean loading) {
        String synchurl = ConstantUrl.VKOIP.concat("/tb/chapter");
        Uri.Builder builder = mVolleyUtils.getBuilder(synchurl);
        builder.appendQueryParameter("token", VkoContext.getInstance(this)
                .getUser().getToken());
        if (!TextUtils.isEmpty(s.getBookId())) {
            builder.appendQueryParameter("bookId", s.getBookId() + "");
        }
        builder.appendQueryParameter("subjectId", s.getId() + "");
        builder.appendQueryParameter("gradeId", VkoContext
                .getInstance(this).getUser().getGradeId()
                + "");
        if (s.getVersion() != null) {
            builder.appendQueryParameter("bookVersionId", s.getVersion() + "");
        }
        if (s.getType() != null) {
            builder.appendQueryParameter("type", s.getType() + "");
        }
        mVolleyUtils.sendGETRequest(loading,builder);
        mVolleyUtils.setUiDataListener(new UIDataListener<Section>() {
            @Override
            public void onDataChanged(Section response) {
                if (response == null || response.getData() == null) {
                    mAdapter.postNotifyDataSetChanged();
                    return;
                }
                String bookName = response.getData().getBookName();
                tvTitle.setText(bookName);
                mKnowModelDatas = response.getData().getChapterList();
                if ( mKnowModelDatas != null && mKnowModelDatas.size() > 0) {
                    mAdapter.clear();
                    mAdapter.add(mKnowModelDatas);
                    if (subjectInfo.isRefreshHomeSubject()) {
                        //通知首页科目 更新数据
                        EventBus.getDefault().post(Constants.SUBJECT_REFRESH);
                    }
                } else {
                    mAdapter.clear();
                }
                mAdapter.judgeHasEmpty();
                mAdapter.postNotifyDataSetChanged();
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
                mAdapter.judgeHasEmpty();
                mAdapter.postNotifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.layout_select_book)
    public void onSelectBookClick() {
        getBookVersionPresenter.toggleView(tvTitle);
    }

    @OnClick(R.id.tv_tocomplex)
    public void startComprehensiveupgrade(){
        Intent intent = new Intent(this, CompCourseListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyComprehensiveupgradeActivity", subjectInfo);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (mAdapter.judgeHasEmpty()) {
            getCourseListTask();
            return;
        }
        knowsection = mAdapter.getListItem(position - 1);


        if (knowsection == null) {
            return;
        }
        knowsection.setSubjectInfo(subjectInfo);

        VkoConfigure.getConfigure(this).put("bookId",knowsection.getBookId());
        VkoConfigure.getConfigure(this).put("sectionId",knowsection.getChapterId());
        Intent intent = new Intent(this, SyncCourseVideoAndTestActivity.class);
        Bundle bundle = new Bundle();
        if (knowsection.getLockState() != 1) {
            bundle.putInt("TYPE", 3);
        }else{
            bundle.putInt("TYPE", 1);
        }
        bundle.putSerializable("KnowledgeSection", knowsection);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
