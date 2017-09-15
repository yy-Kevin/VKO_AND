package cn.vko.ring.study.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.vko.ring.R;
import cn.vko.ring.study.model.TopicInfo.Teacher;
import cn.vko.ring.utils.ImageUtils;

/**
 * @author shikh 
 * 专题 -- 老师头像
 */
public class TeacherHeadView extends LinearLayout {

	private Context context;

	public TeacherHeadView(Context context) {

		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TeacherHeadView(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		
	}

	public void addData(List<Teacher> teachers) {
		if (teachers != null && teachers.size() > 0) {
			for (Teacher u : teachers) {
				if (u != null) {
					View v = View.inflate(context, R.layout.item_teacher_head, null);
					TextView tvName = (TextView) v.findViewById(R.id.teacher_name);
					ImageView ivHead = (ImageView) v.findViewById(R.id.teacher_head);
					tvName.setText(u.getTname());
					ImageUtils.loadPerviewHead(ivHead,u.getTsface(),200, 200);
					addView(v);
				}
			}
		}
	}

	
	

}
