package cn.vko.ring.common.event;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.ISwitchLearnListener;
import cn.vko.ring.home.model.BaseSubjectInfo;
import cn.vko.ring.home.model.SubjectInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.model.TextBookInfo;
import cn.vko.ring.study.model.BookInfo;
import cn.vko.ring.utils.ACache;

public class SwitchLearnEvent {

	private SwitchLearnEvent() {
	};

	private static byte[] lock = new byte[0];
	private static List<ISwitchLearnListener> listeners;

	/**
	 * 注册一个监听器
	 * 
	 * @param l
	 */
	public static void register(ISwitchLearnListener l) {
		synchronized (lock) {
			if (null == listeners) {
				listeners = new ArrayList<ISwitchLearnListener>(0);
			}
			listeners.add(l);
		}
	}

	/**
	 * 卸载掉一个监听器
	 * 
	 * @param l
	 */
	public static void unRegister(ISwitchLearnListener l) {
		synchronized (lock) {
			if (null != listeners && listeners.contains(l)) {
				listeners.remove(l);
			}
		}
	}

	/**
	 * 删除所有
	 */
	public static void release() {
		synchronized (lock) {
			if (null != listeners) {
				listeners.clear();
			}
		}
	}

	/**
	 * 通知事件（年级）
	 * 
	 * @param grade
	 */
	public static void fireGrade(String grade) {
		if (null == listeners || listeners.size() == 0) {
			return;
		}
		ISwitchLearnListener[] la = null;
		synchronized (lock) {
			la = new ISwitchLearnListener[listeners.size()];
			listeners.toArray(la);
		}
		for (ISwitchLearnListener l : la) {
			l.onUpdateLearn(grade);
		}
		la = null;
	}

	/**
	 * 通知事件(教材)
	 * 
	 * @param book
	 */
	public static void fireBook(BookInfo book, Context ctx) {
		onChangeTextBook(book, ctx);
		if (null == listeners || listeners.size() == 0) {
			return;
		}
		ISwitchLearnListener[] la = null;
		synchronized (lock) {
			la = new ISwitchLearnListener[listeners.size()];
			listeners.toArray(la);
		}
		for (ISwitchLearnListener l : la) {
			l.onChangeTextBook(book);
		}
		la = null;
	}

	private static void onChangeTextBook(BookInfo book, Context context) {
		// TODO Auto-generated method stub
		UserInfo user = VkoContext.getInstance(context).getUser();
		BaseSubjectInfo info = (BaseSubjectInfo) ACache.get(context)
				.getAsObject(user.getUdid() + 1 + book.getLearn());
		if (info != null && info.getData() != null) {
			for (SubjectInfo sub : info.getData()) {
				if (sub.getSubjectId() != null
						&& sub.getSubjectId().equals(book.getSubjectId())) {
					sub.setBookid(book.getBookid());
					sub.setBookname(book.getBookname());
					sub.setState(0);
					if (!TextUtils.isEmpty(book.getBvName())) {
						sub.setBvName(book.getBvName());
					}
					sub.setImgurl(book.getImgurl());
					sub.setVersionId(book.getVersionId());
					continue;
				}
			}
			ACache.get(context).put(user.getUdid() + 1 + book.getLearn(), info, ACache.TIME_DAY);
		}

		BaseSubjectInfo info3 = (BaseSubjectInfo) ACache.get(context)
				.getAsObject(user.getUdid() + 3 + book.getLearn());
		if (info3 != null && info3.getData() != null) {
			for (SubjectInfo sub : info3.getData()) {
				if (sub.getSubjectId() != null
						&& sub.getSubjectId().equals(book.getSubjectId())) {
					sub.setBookid(book.getBookid());
					sub.setBookname(book.getBookname());
					if (!TextUtils.isEmpty(book.getBvName())) {
						sub.setBvName(book.getBvName());
					}
					sub.setState(0);
					sub.setImgurl(book.getImgurl());
					sub.setVersionId(book.getVersionId());
					continue;
				}
			}
			ACache.get(context).put(user.getUdid() + 3 + book.getLearn(),
					info3, ACache.TIME_DAY);
		}
		saveTextBookSuccess(book, context, user);
	}

	private static void saveTextBookSuccess(BookInfo info, Context context,
			UserInfo user) {
		BaseSubjectInfo response = (BaseSubjectInfo) ACache.get(context)
				.getAsObject(user.getUdid() + info.getLearn());
		if (response != null) {
			List<SubjectInfo> subjectList = response.getData();
			for (SubjectInfo sub : subjectList) {
				if (sub.getSubjectId().equals(info.getSubjectId())) {
					sub.setVersionId(info.getVersionId());
					sub.setBookid(info.getBookid());
					sub.setBvName(info.getBvName());
					sub.setState(0);
					for (TextBookInfo t : sub.getBookVersion()) {
						if (t.getBvId().equals(info.getVersionId())) {
							t.setSubId(sub.getSubjectId());
							t.setLearn(info.getLearn());
							for (BookInfo book : t.getBook()) {
								book.setState(book.getBookid().equals(
										info.getBookid()) ? 0 : 1);
							}
							continue;
						}
					}
					continue;
				}
			}
			ACache.get(context).put(user.getUdid() + info.getLearn(), response);
		}
	}

}
