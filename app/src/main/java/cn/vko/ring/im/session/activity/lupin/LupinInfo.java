package cn.vko.ring.im.session.activity.lupin;

import java.io.Serializable;
import java.util.List;

/**
 * Created by A on 2017/2/13.
 */
public class LupinInfo implements Serializable {

    private int code;
    private String msg;
    private long stime;
    private Data data;
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
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }


    public class Data implements Serializable {
        private String msg;
        private String vid;
        private String courseType;
        private String videoId;
        private String videoState;
        private String videoName;
        private String playUrl;
        private String taskId;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCourseType() {
            return courseType;
        }

        public void setCourseType(String courseType) {
            this.courseType = courseType;
        }

        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public String getVid() {
            return vid;
        }
        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getVideoState() {
            return videoState;
        }

        public void setVideoState(String videoState) {
            this.videoState = videoState;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
    }
}
