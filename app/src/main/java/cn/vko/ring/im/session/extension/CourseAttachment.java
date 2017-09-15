package cn.vko.ring.im.session.extension;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

/**
 * desc:课程
 * Created by jiarh on 16/5/3 11:37.
 */
public class CourseAttachment extends CustomAttachment {


   CourseMsg course;
    private static final String TAG = "CourseAttachment";
    public CourseMsg getCourse() {
        return course;
    }

    public void setCourse(CourseMsg course) {
        this.course = course;
    }


    public CourseAttachment(){
        super(CustomAttachmentType.COURSE);
    }

    public CourseAttachment(int type, CourseMsg course) {
        super(type);
        this.course = course;
    }
   public CourseAttachment(JSONObject data){
        super(CustomAttachmentType.COURSE);
//       parseData(data);
    }

    @Override
    protected void parseData(JSONObject data) {

        Log.d(TAG, "parseData() called with: " + "data = [" + data + "]");
        Log.e(TAG, "parseData() called with: " + "data = [" + data + "]");
        course = new CourseMsg();
        course.setTitle(data.getString(CourseMsg.TITLE));
        course.setUrl(data.getString(CourseMsg.URL));
        course.setObjInfo(data.getString(CourseMsg.DESC));
        course.setObjId(data.getString(CourseMsg.ID));
        course.setTeacherId(data.getString(CourseMsg.TEACHER_ID));
        course.setType(Integer.parseInt(data.getString(CourseMsg.TYPE)));
        course.setTaskId(data.getString(CourseMsg.TASK_ID));
        course.setCourseType(data.getString(CourseMsg.COURSETYPE));
        course.setTeacherName(data.getString(CourseMsg.TEACHERNAME));




    }

    @Override
    protected JSONObject packData() {

      JSONObject data = new JSONObject();
        data.put(CourseMsg.TITLE,course.getTitle());
        data.put(CourseMsg.DESC,course.getObjInfo());
        data.put(CourseMsg.ID,course.getObjId());
        data.put(CourseMsg.URL,course.getUrl());
        data.put(CourseMsg.TEACHER_ID,course.getTeacherId());
        data.put(CourseMsg.TASK_ID,course.getTaskId());
        data.put(CourseMsg.TYPE,course.getType());
        data.put(CourseMsg.COURSETYPE,course.getCourseType());
        data.put(CourseMsg.TEACHERNAME,course.getTeacherName());


        return data;
    }
}
