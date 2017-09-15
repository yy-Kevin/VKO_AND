package cn.vko.ring.classgroup.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.home.model.Pager;

/**
 * desc:
 * Created by jiarh on 16/5/13 11:14.
 */
public class TaskItemData extends BaseResponseInfo{
    private TaskData data;

    public TaskData getData() {
        return data;
    }

    public void setData(TaskData data) {
        this.data = data;
    }

    public static class TaskData implements Serializable{
        private Pager pager;
        private List<TaskItem> gtList;

        public List<TaskItem> getGtList() {
            return gtList;
        }

        public void setGtList(List<TaskItem> gtList) {
            this.gtList = gtList;
        }

        public Pager getPager() {
            return pager;
        }

        public void setPager(Pager pager) {
            this.pager = pager;
        }
    }
    public static class TaskItem implements Serializable{

        private List<TaskMedia> groupTaskMediaList;
        private String teacherId;
        private String title;
        private String description;
        private String url;
        private int type;
        private String objId;
        private String startTime;
        private String endTime;
        private String state;
        private String id;
        private String userName;

        @Override
        public boolean equals(Object o) {

            return ((TaskItem)o).getId().equals(getId());
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public List<TaskMedia> getGroupTaskMediaList() {
            return groupTaskMediaList;
        }

        public void setGroupTaskMediaList(List<TaskMedia> groupTaskMediaList) {
            this.groupTaskMediaList = groupTaskMediaList;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class TaskMedia implements Serializable{
        private String url;
        private String taskId;
        private String duration;
        private String sn;
        private String type;
        private String id;


        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
