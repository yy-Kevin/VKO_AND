package cn.vko.ring.im.session.extension;

import java.io.Serializable;

/**
 * desc:
 * Created by jiarh on 16/7/7 11:44.
 */
public class TestMsg implements Serializable{
    public static String TITLE="title";
    public static String URL="url";
    public static String TEACHERNAME="teacherName";
    public static String OBJID="objId";
    public static String TYPE="type";
    public static String TASKID="taskId";

    /**
     * title : 2016年7月7日高中数学组卷
     * url : http://m.vko.cn/tiku/zuoti.html?paperId=39188&groupId=4081575&teacherId=14580935258731860&taskId=379
     * teacherName : 薛晓康
     * objId : 39188
     * type : 4
     * taskId : 379
     */

    private String title;
    private String url;
    private String teacherName;
    private String objId;
    private String type;
    private String taskId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
