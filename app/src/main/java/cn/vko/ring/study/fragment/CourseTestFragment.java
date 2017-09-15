package cn.vko.ring.study.fragment;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.adapter.TestSectionListAdapter;
import cn.vko.ring.study.model.ParamData;
import cn.vko.ring.study.model.ParamNewSyncAndComp;
import cn.vko.ring.study.model.TestSection;
import cn.vko.ring.study.model.TestSectionModel;
import cn.vko.ring.study.presenter.GetExamInfoPresenter;

/**
 * Created by shikh on 2016/5/11.
 */
public class CourseTestFragment extends BaseFragment implements UIDataListener<TestSectionModel>,
        AdapterView.OnItemClickListener {
    @Bind(R.id.test_list_view)
    ListView mListView;
    private ParamData mParamData;

    private TestSectionListAdapter mAdapter;
    private List<TestSection> mSectionList;
    private VolleyUtils<TestSectionModel> mVolleyUtils;

    private GetExamInfoPresenter mGetExamInfoPresenter;
    private ParamNewSyncAndComp paramNewSyncAndComp;
    public void setParamData(ParamData paramData) {
        this.mParamData = paramData;
    }
    @Override
    public int setContentViewId() {
        return R.layout.fragment_test_do_title_list;
    }

    @Override
    public void initView(View root) {

        if (mVolleyUtils == null) {
            mVolleyUtils = new VolleyUtils<TestSectionModel>(atx,TestSectionModel.class);
        }
        mSectionList = new ArrayList<TestSection>();
        mAdapter = new TestSectionListAdapter(atx);
        // mAdapter.setList(mSectionList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }
    @Override
    public void initData() {
        super.initData();
        refreshData();
    }

    public void refreshData() {
        Uri.Builder b;
        if (mParamData.isSync()) {
             b = mVolleyUtils.getBuilder(ConstantUrl.VK_TEST_SECTION_LIST);
            b.appendQueryParameter("bookId", mParamData.getBookId());
            b.appendQueryParameter("sectionId", mParamData.getSectionId());
        } else {
            b = mVolleyUtils.getBuilder(ConstantUrl.VK_TEST_COMP_SECTION_LIST);
            if(mParamData.getFromType() ==1){
                b.appendQueryParameter("objId", mParamData.getK1Id());
            }else{
                b.appendQueryParameter("k1", mParamData.getK1Id());
            }

        }
        b.appendQueryParameter("token", mParamData.getToken());
        mVolleyUtils.sendGETRequest(true,b);
        mVolleyUtils.setUiDataListener(this);
    }




    @Override
    public void onDataChanged(TestSectionModel response) {
        if (response != null && response.getData() != null) {
            mSectionList = response.getData();
            if (mSectionList.size() == 0) {
                return;
            }
            mAdapter.clear();
            mAdapter.add(mSectionList);
        }
            mAdapter.judgeHasEmpty();
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {
        if(mAdapter.judgeHasEmpty()){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (VkoContext.getInstance(atx).doLoginCheckToSkip(atx)) {
            return;
        }
        if(position==0&&mSectionList.size()==0){
            refreshData();
            return;
        }

        if (!mParamData.isSync()) {
            paramNewSyncAndComp  = new ParamNewSyncAndComp(mParamData.getSubjectId(), "43", mSectionList.get(position).getCourseId(), mSectionList.get(position).getCourseName(), "3",mParamData.getK1Id() , false);
//            Log.e("=====konwlageId",mSectionList.get(position).getCourseId());
//            Log.e("=====konwlageName",mSectionList.get(position).getCourseName());
//            Log.e("=====getK1Id",mParamData.getK1Id());
        }else{
            paramNewSyncAndComp = new ParamNewSyncAndComp(mSectionList.get(position).getCourseId(),
                    mParamData.getBookId(), mParamData.getSubjectId(), "41", mSectionList.get(position).getCourseName(), "3",
                    mParamData.getSectionId(), false);
//            Log.e("=====getCourseName",mSectionList.get(position).getCourseName());
//            Log.e("=====getSectionId",mParamData.getSectionId());
        }
        mGetExamInfoPresenter = new GetExamInfoPresenter(atx, paramNewSyncAndComp);
        mGetExamInfoPresenter.getData();
    }
}
