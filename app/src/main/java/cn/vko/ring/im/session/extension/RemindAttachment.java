package cn.vko.ring.im.session.extension;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

/**
 * desc:
 * Created by jiarh on 16/5/19 15:45.
 */
public class RemindAttachment extends CustomAttachment {
    CourseMsg course;

    RemindAttachment() {
        super(CustomAttachmentType.REMIND);
    }

    public CourseMsg getCourse() {
        return course;
    }

    public void setCourse(CourseMsg course) {
        this.course = course;
    }

    @Override
    protected void parseData(JSONObject data) {
        Log.e("====", "parseData: "+data);
        course = new CourseMsg();
        course.setTitle(data.getString(CourseMsg.TITLE));
        course.setUrl(data.getString(CourseMsg.URL));
        course.setObjInfo(data.getString(CourseMsg.DESC));
        course.setObjId(data.getString(CourseMsg.ID));
        course.setTeacherId(data.getString(CourseMsg.TEACHER_ID));
        course.setType(Integer.parseInt(data.getString(CourseMsg.TYPE)));
        course.setTaskId(data.getString(CourseMsg.TASK_ID));
        course.setCourseType(data.getString(CourseMsg.COURSETYPE));

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


        return data;
    }
}
