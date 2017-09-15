package cn.vko.ring.classgroup.presenter;

import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.model.TaskItemData;
import cn.vko.ring.classgroup.view.TaskListView;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;

/**
 * desc:
 * Created by jiarh on 16/5/12 11:49.
 */
public class TaskListPresenterImp implements TaskListPrsenter {
    private Context mContext;
    private TaskListView taskListView;
    private VolleyUtils<TaskItemData> volleyUtils;

    public TaskListPresenterImp(TaskListView taskListView, Context mContext) {
        this.taskListView = taskListView;
        this.mContext = mContext;
    }

    @Override
    public void getTaskList(String groupId, String teacherId, int pageIndex, int pageSize) {
        Map<String,String> params = new HashMap<String,String>();
        if(volleyUtils == null){
            volleyUtils = new VolleyUtils<TaskItemData>(mContext,TaskItemData.class);
        }
        Uri.Builder b = volleyUtils.getBuilder(ConstantUrl.VK_GROUP_TASK_LIST);
        b.appendQueryParameter("token", VkoContext.getInstance(mContext).getToken());
        b.appendQueryParameter("teacherId", teacherId);
        b.appendQueryParameter("groupId", groupId);
        b.appendQueryParameter("pageIndex", pageIndex+"");
        b.appendQueryParameter("pageSize", pageSize+"");
        volleyUtils.setUiDataListener(new UIDataListener<TaskItemData>() {
            @Override
            public void onDataChanged(TaskItemData data) {
                if (data!=null&&data.getData()!=null&&data.getData().getGtList()!=null)
                {
                    taskListView.getTaskDatas(data.getData().getPager(),data.getData().getGtList());
                }else {
                    taskListView.getTaskDatas(null,null);
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {

                taskListView.getTaskDatas(null,null);
            }
        });
        volleyUtils.sendGETRequest(true,b);
    }
}
