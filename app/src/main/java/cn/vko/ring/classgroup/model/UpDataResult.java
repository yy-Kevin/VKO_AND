package cn.vko.ring.classgroup.model;

import java.io.Serializable;
import java.util.List;

/**
 * desc:
 * Created by jiarh on 16/5/18 14:32.
 */
public class UpDataResult implements Serializable{


    /**
     * code : 0
     * msg :
     * stime : 1463553040341
     * data : [{"teacherId":"14454185225241561","title":"安卓测试数据4","description":"安卓测试数据","url":"nullhttp://www.vko.cnplay/course?objId=13976135449039819&type=4&groupId=4026299&teacherId=14454185225241561&taskId=109","type":0,"objId":"13976135449039819","startTime":"May 18, 2016 2:30:40 PM","endTime":"May 19, 2016 2:30:40 PM","id":"109"},{"teacherId":"14454185225241561","title":"安卓测试数据3","description":"安卓测试数据","url":"nullhttp://www.vko.cnplay/course?objId=13976135449039820&type=4&groupId=4026299&teacherId=14454185225241561&taskId=110","type":0,"objId":"13976135449039820","startTime":"May 18, 2016 2:30:40 PM","endTime":"May 19, 2016 2:30:40 PM","id":"110"},{"teacherId":"14454185225241561","title":"安卓测试数据2","description":"安卓测试数据","url":"nullhttp://www.vko.cnplay/course?objId=13976135449039821&type=4&groupId=4026299&teacherId=14454185225241561&taskId=111","type":0,"objId":"13976135449039821","startTime":"May 18, 2016 2:30:40 PM","endTime":"May 19, 2016 2:30:40 PM","id":"111"},{"teacherId":"14454185225241561","title":"安卓测试数据1","description":"安卓测试数据","url":"nullhttp://www.vko.cnplay/course?objId=13976135449039822&type=4&groupId=4026299&teacherId=14454185225241561&taskId=112","type":0,"objId":"13976135449039822","startTime":"May 18, 2016 2:30:40 PM","endTime":"May 19, 2016 2:30:40 PM","id":"112"}]
     */

    private int code;
    private String msg;
    private long stime;
    /**
     * teacherId : 14454185225241561
     * title : 安卓测试数据4
     * description : 安卓测试数据
     * url : nullhttp://www.vko.cnplay/course?objId=13976135449039819&type=4&groupId=4026299&teacherId=14454185225241561&taskId=109
     * type : 0
     * objId : 13976135449039819
     * startTime : May 18, 2016 2:30:40 PM
     * endTime : May 19, 2016 2:30:40 PM
     * id : 109
     */

    private List<TaskItemData.TaskItem> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getStime() {
        return stime;
    }

    public void setStime(long stime) {
        this.stime = stime;
    }

    public List<TaskItemData.TaskItem> getData() {
        return data;
    }

    public void setData(List<TaskItemData.TaskItem> data) {
        this.data = data;
    }

   /* public static class DataEntity {
        private String teacherId;
        private String title;
        private String description;
        private String url;
        private int type;
        private String objId;
        private String startTime;
        private String endTime;
        private String id;

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getObjId() {
            return objId;
        }

        public void setObjId(String objId) {
            this.objId = objId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }*/
}
