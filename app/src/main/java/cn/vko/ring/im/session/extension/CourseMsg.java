package cn.vko.ring.im.session.extension;

import java.io.Serializable;

/**
 * desc:
 * Created by jiarh on 16/5/3 11:50.
 */
public class CourseMsg implements Serializable {

    public static final String ID="objId";
    public static final String TEACHER_ID="teacherId";
    public static final String TITLE="title";
    public static final String URL="url";
    public static final String DESC="objInfo";
    public static final String IMGURL="objImg";
    public static final String TASK_ID="taskId";
    public static final String TYPE="type";
    public static final String COURSETYPE="CourseType";
    public static final String TEACHERNAME="teacherName";

    private String courseId;//objId
    private String courseType;//同步 综合 本地课
    private String courseDesc;//objInfo
    private String courseName;//title
    private String courseCover;//objImg
    private String teacherName;


    private String objId;
    private String title;
    private String url;
    private String objInfo;
    private String objImg;
    private String teacherId;

    private String startTime;
    private String endTime;
    /**
     * 任务id
     */
    private String taskId;

    private String videoType;

    /**
     * 课程类型 0 课程； 1 包 ；2 作业 ；3 自定义任务
     */
    private int type=0;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    private boolean  isChecked;
    private boolean isGroup;

    public String getCourseCover() {
        return courseCover;
    }

    public void setCourseCover(String courseCover) {
        this.courseCover = courseCover;
        this.objImg = courseCover;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
        this.objInfo = courseDesc;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
        this.objId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
        this.title = courseName;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getObjImg() {
        return objImg;
    }

    public void setObjImg(String objImg) {
        this.objImg = objImg;

    }

    public String getObjInfo() {
        return objInfo;
    }

    public void setObjInfo(String objInfo) {
        this.objInfo = objInfo;
        this.courseDesc = objInfo;

    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.courseName = title;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
