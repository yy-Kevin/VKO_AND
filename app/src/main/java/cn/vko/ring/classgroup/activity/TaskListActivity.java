package cn.vko.ring.classgroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.msg.MsgService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.adapter.TaskListAdapter;
import cn.vko.ring.classgroup.model.TaskItemData;
import cn.vko.ring.classgroup.presenter.TaskListPresenterImp;
import cn.vko.ring.classgroup.view.TaskListView;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.model.Pager;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.im.session.extension.CustomAttachParser;

/**
 * 任务列表
 */
public class TaskListActivity extends BaseActivity implements TaskListView, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    @Bind(R.id.xlistview)
    XListView xlistview;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.imagebtn)
    ImageView imagebtn;
    private TaskListAdapter adapter;
    private TaskListPresenterImp taskListPresenterImp;
    private List<TaskItemData.TaskItem> taskItems;
    private int pageIndex;
    private int pageSize = 10;
    private String sessionId;



    @Override
    public int setContentViewResId() {
        return R.layout.activity_task_list;
    }

    @Override
    public void initView() {
        getIntentData();
        tvTitle.setText("任务");
        if(VkoContext.getInstance(this).getUser().getIsTeacher().equals("true")){
            imagebtn.setVisibility(View.VISIBLE);
            imagebtn.setImageResource(R.drawable.add_task);
        }

    }

    private void getIntentData() {
        Intent in = getIntent();
        sessionId = in.getStringExtra("SessionId");
        Log.e("=-=", "getIntentData: "+sessionId );
    }

    @OnClick(R.id.iv_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.imagebtn)
    public void onAddTask() {
        Bundle bundle = new Bundle();
        bundle.putString("GROUPID", sessionId);
        StartActivityUtil.startActivity(this, CreateTaskActivty.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    @Override
    public void initData() {
        taskItems = new ArrayList<>();
        taskListPresenterImp = new TaskListPresenterImp(this, this);
        adapter = new TaskListAdapter(this);
        adapter.setList(taskItems);
        xlistview.setAdapter(adapter);
        xlistview.setPullRefreshEnable(true);
        xlistview.setXListViewListener(this);
        xlistview.setOnItemClickListener(this);
        onRefresh();

    }

    @Override
    public void getTaskDatas(Pager pager, List<TaskItemData.TaskItem> items) {
        if (pager == null) return;
        if (pager.getPageNo()==1)
            taskItems.clear();
        if (items != null) {
            taskItems.addAll(items);
        }
        adapter.postNotifyDataSetChanged();
        if (pager.getPageNo() >= pager.getPageTotal()) {
            xlistview.setPullLoadEnable(false);
        } else {
            xlistview.setPullLoadEnable(true);
        }
        stop();
    }

    @Override
    protected void onStart() {

        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onRefreshTask(TaskItemData.TaskItem item) {
        Log.e("=-=", "onRefreshTask: ");
        if (item != null) {
            taskItems.add(0, item);
            adapter.postNotifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void stop() {
        xlistview.stopRefresh();
        xlistview.stopLoadMore();
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        getData();
    }

    private void getData() {

        UserInfo user = VkoContext.getInstance(this).getUser();
        if (user != null) {

            if (!TextUtils.isEmpty(sessionId))
                taskListPresenterImp.getTaskList(sessionId, user.getIsTeacher().equals("true") ? user.getUserId() : "", pageIndex, pageSize);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (taskItems.size()>0){
            TaskItemData.TaskItem item = taskItems.get(position - 1);
            Log.e(":---------", "onItemClick: ");
            switch (item.getType()) {
                case 3:
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", item.getUrl());
                    bundle.putString("TITLE", item.getTitle());
                    StartActivityUtil.startActivity(TaskListActivity.this, TaskDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    break;
                case 0:
                    Bundle b = new Bundle();
                    b.putString("ID", item.getObjId());
                    b.putInt("TYPE", item.getType());
                    b.putString("TASKID", item.getId());
                    b.putString("GROUPID", sessionId);
                    b.putString("TEACHERID", item.getTeacherId());
                    StartActivityUtil.startActivity(TaskListActivity.this, GroupVideoViewActivity.class, b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    break;
            }
        }else {
            Toast.makeText(this, "暂无数据",Toast.LENGTH_SHORT).show();
        }

    }


}
