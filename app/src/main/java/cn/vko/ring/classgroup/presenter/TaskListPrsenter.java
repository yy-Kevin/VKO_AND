package cn.vko.ring.classgroup.presenter;

/**
 * desc:
 * Created by jiarh on 16/5/12 11:48.
 */
public interface TaskListPrsenter {
    void getTaskList(String groupId,String teacherId, int pageIndex, int pageSize);
}
