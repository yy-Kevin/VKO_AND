package cn.vko.ring.classgroup.view;

import java.util.List;

import cn.vko.ring.classgroup.model.TaskItemData;
import cn.vko.ring.home.model.Pager;

/**
 * desc:
 * Created by jiarh on 16/5/13 11:39.
 */
public interface TaskListView  {
    void getTaskDatas(Pager pager,List<TaskItemData.TaskItem> taskItems);
}
