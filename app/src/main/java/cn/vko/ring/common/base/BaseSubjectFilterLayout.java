package cn.vko.ring.common.base;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.GridView;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.event.SwitchLearnEvent;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.listener.ISwitchLearnListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.home.model.BaseSubjectInfo;
import cn.vko.ring.home.model.SubjectInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.model.TextBookInfo;
import cn.vko.ring.study.model.BookInfo;
import cn.vko.ring.utils.ACache;

/**
 * @author shikh 选择学科
 */
public abstract class BaseSubjectFilterLayout extends GridView implements
		ISwitchLearnListener {
	public IOnClickItemListener<SubjectInfo> listener;
	/** 存放所有科目的集合 */
	public List<SubjectInfo> subjectList;
	public SubjectInfo info;
	public Context context;
	public VolleyUtils<BaseSubjectInfo> volley;
	public UserInfo user;
	public String grade;
	public int type;

	/**0
	 * @param context
	 * @param attrs
	 */
	public BaseSubjectFilterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		user = VkoContext.getInstance(context).getUser();
		SwitchLearnEvent.register(this);
		volley = new VolleyUtils<BaseSubjectInfo>(context,BaseSubjectInfo.class);
		setListener();
	}

	public void setInitData(SubjectInfo info,
			IOnClickItemListener<SubjectInfo> listener) {
		this.listener = listener;
		this.info = info;
	}

	/**
	 * 获取学科
	 */
	public void initData(String grade, int type) {
		// TODO Auto-generated method stub
		this.grade = grade;
		this.type = type;
		if (user == null) {
			startLogin();
			return;
		}
		BaseSubjectInfo base = (BaseSubjectInfo) ACache.get(context)
				.getAsObject(user.getUdid() + type + grade);
		if (base != null && base.getData().size() > 0) {
			subjectList = base.getData();
			fillAdapter(subjectList);
		} else {
			getNetSubjectList(grade, type);
		}
	}



	/**
	 * @param list
	 */
	public abstract void fillAdapter(List<SubjectInfo> list);

	/**
	 * @param learnId
	 *            学段
	 */
	private void getNetSubjectList(final String learnId, final int type) {
		// TODO Auto-generated method stub
		String url = ConstantUrl.VKOIP.concat("/user/echoSubject");
		Uri.Builder builder = volley.getBuilder(url);
		builder.appendQueryParameter("learnId", learnId);
		builder.appendQueryParameter("token", user.getToken());
		builder.appendQueryParameter("type", "1");
		if (type != 0) {
			builder.appendQueryParameter("courseType", type + "");
		}
		volley.setUiDataListener(new UIDataListener<BaseSubjectInfo>() {
			@Override
			public void onDataChanged(BaseSubjectInfo data) {
				if (data != null) {
					if (data.getData() != null
							&& data.getData().size() > 0) {
						// SubDB.getInstance().saveListSub(base.getData(),
						// learnId);
						for (SubjectInfo sub : data.getData()) {
							if (sub != null) {
								sub.setLearnId(learnId);
							}
						}
						subjectList = data.getData();
						ACache.get(context).put(
								user.getUdid() + type + learnId, data,
								ACache.TIME_DAY);
						fillAdapter(data.getData());
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
		volley.sendGETRequest(true,builder);
	}

	public abstract void setListener();

	@Override
	public void onUpdateLearn(String grade) {
		// TODO Auto-generated method stub
		initData(grade, type);
	}

	@Override
	public void onChangeTextBook(BookInfo book) {
		// TODO Auto-generated method stub
		// ACache.get(context).remove(user.getUdid()+1+grade);
		// ACache.get(context).remove(user.getUdid()+3+grade);
		initData(book.getLearn(), type);
	}

	public void saveTextBookSuccess(BookInfo info) {
		BaseSubjectInfo response = (BaseSubjectInfo) ACache.get(context)
				.getAsObject(grade);
		if (response != null) {
			List<SubjectInfo> subjectList = response.getData();
			for (SubjectInfo sub : subjectList) {
				if (sub.getSubjectId().equals(info.getSubjectId())) {
					sub.setVersionId(info.getVersionId());
					sub.setBookid(info.getBookid());
					sub.setState(0);
					for (TextBookInfo t : sub.getBookVersion()) {
						if (t.getBvId().equals(info.getVersionId())) {
							t.setSubId(sub.getSubjectId());
							t.setLearn(grade);
							for (BookInfo book : t.getBook()) {
								book.setState(book.getBookid().equals(
										info.getBookid()) ? 0 : 1);
							}
						}
						continue;
					}
				}
				continue;
			}
			ACache.get(context).put(grade, response);
		}
	}

	protected void startLogin() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(context, LoginActivity.class);
		intent.putExtra("TOKEN", true);
		context.startActivity(intent);
	}

	public void unRegister() {
		SwitchLearnEvent.unRegister(this);
	}

}
