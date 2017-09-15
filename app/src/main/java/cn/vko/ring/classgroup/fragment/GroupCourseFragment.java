package cn.vko.ring.classgroup.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.vko.ring.R;
import cn.vko.ring.classgroup.adapter.CourseGroupListAdapter;
import cn.vko.ring.classgroup.listeners.SelectCourseCallBack;
import cn.vko.ring.classgroup.model.CourseTab;
import cn.vko.ring.classgroup.presenter.CourseListPresenterImp;
import cn.vko.ring.classgroup.view.CourseListView;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.model.Pager;
import cn.vko.ring.im.main.fragment.MainTabFragment;
import cn.vko.ring.im.session.extension.CourseMsg;

/**
 * desc:
 * Created by jiarh on 16/5/12 10:14.
 */
public class GroupCourseFragment extends MainTabFragment implements CourseListView, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    XListView courseLv;
    private CourseListPresenterImp presenter;
    private CourseGroupListAdapter adapter;
    private List<CourseMsg> courseDatas;
    private List<CourseMsg> selectCourses;
    private int pageIndex = 1;
    private int pageSize = 10;
    private static final String TAG = "PerCourseFragment";
    private SelectCourseCallBack callBack;

    public void setCallBack(SelectCourseCallBack callBack) {
        this.callBack = callBack;
    }
    @Override
    public void setContainerId(int containerId) {
        this.setContainerId(CourseTab.GROUP_COURSE.fragmentId);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }
    @Override
    protected void onInit() {
        findViews();
        initData();
    }

    private void initData() {
        courseDatas = new ArrayList<>();
        selectCourses = new ArrayList<>();
        presenter = new CourseListPresenterImp(this, getContext());
        adapter = new CourseGroupListAdapter(getContext());
        adapter.setList(courseDatas);
        courseLv.setAdapter(adapter);
        courseLv.setPullRefreshEnable(true);
        courseLv.setXListViewListener(this);
        courseLv.setOnItemClickListener(this);
        onRefresh();

    }

    private void findViews() {
        courseLv = findView(R.id.xlistview);
    }


    @Override
    public void getShowData(Pager pager,List<CourseMsg> datas) {

        if (pager==null||datas==null)return;
        if (pager.getPageNo()==1){
            courseDatas.clear();
        }
        if ( datas.size() > 0) {
            courseDatas.addAll(datas);

        }
        adapter.postNotifyDataSetChanged();
        stop();
        if (pager.getPageNo()>=pager.getPageTotal()){
            courseLv.setPullLoadEnable(false);
        }else {
            courseLv.setPullLoadEnable(true);
        }

    }

    private void stop() {
        courseLv.stopRefresh();
        courseLv.stopLoadMore();
    }

    @Override
    public void onClickItem(CourseMsg t, int position) {


    }

    @Override
    public void onRefresh() {

        pageIndex = 1;
        getData();
    }

    private void getData() {
        presenter.getCourseList("1", pageIndex, pageSize);
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        getData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        CourseMsg courseMsg = courseDatas.get(position-1);
        courseMsg.setChecked(!courseMsg.isChecked());
        courseMsg.setType(1);
        adapter.upDateItem(courseLv,position,courseMsg);
        EventBus.getDefault().post(courseMsg);

    }


}
