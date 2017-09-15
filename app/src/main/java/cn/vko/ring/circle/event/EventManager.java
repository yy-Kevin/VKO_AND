package cn.vko.ring.circle.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件管理
 * 
 * @author Administrator
 * 
 */
public class EventManager {

	private EventManager() {
	}

	private static List<IEventListener> listeners;

	private static List<ITopicEventListener> topListeners;
	private static byte[] lock = new byte[0];

	/**
	 * 注册一个监听器
	 * 
	 * @param l
	 */
	public static void register(IEventListener l) {
		synchronized (lock) {
			if (null == listeners) {
				listeners = new ArrayList<IEventListener>(0);
			}
			listeners.add(l);
		}
	}

	/**
	 * 注册一个监听器
	 * 
	 * @param l
	 */
	public static void register(ITopicEventListener l) {
		synchronized (lock) {
			if (null == topListeners) {
				topListeners = new ArrayList<ITopicEventListener>(0);
			}
			topListeners.add(l);
		}
	}

	/**
	 * 卸载掉一个监听器
	 * 
	 * @param l
	 */
	public static void unRegister(ITopicEventListener l) {
		synchronized (lock) {
			if (null != topListeners && topListeners.contains(l)) {
				topListeners.remove(l);
			}
		}
	}

	public static void unRegister(IEventListener l) {
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
			if (null != topListeners) {
				topListeners.clear();
			}
		}
	}

	public static void fire(TopicEvent e) {
		if (null == topListeners || topListeners.size() == 0) {
			return;
		}
		ITopicEventListener[] la = null;
		synchronized (lock) {
			la = new ITopicEventListener[topListeners.size()];
			topListeners.toArray(la);
		}

		for (ITopicEventListener l : la) {
			if ((l.getFilter() & TopicEvent.SUBMIT_TOPIC_EVENT) == e
					.getEventType()
					|| (l.getFilter() & TopicEvent.DETEIL_TOPIC_EVENT) == e
							.getEventType()) {
				l.event(e);
			}
		}
		la = null;
	}

	/**
	 * 通知事件
	 * 
	 * @param event
	 */
	public static void fire(Event event) {
		if (null == listeners || listeners.size() == 0) {
			return;
		}
		IEventListener[] la = null;
		synchronized (lock) {
			la = new IEventListener[listeners.size()];
			listeners.toArray(la);
		}

		for (IEventListener l : la) {
			if ((l.getFilter() & Event.CREATE_EVENT) == event.getEventType()
					|| (l.getFilter() & Event.DETEIL_EVENT) == event
							.getEventType()
					|| (l.getFilter() & Event.JOIN_EVENT) == event
							.getEventType()

					|| (l.getFilter() & Event.UPDATE_EVENT) == event
							.getEventType()
					|| (l.getFilter() & Event.QUIT_EVENT) == event
							.getEventType()) {
				l.event(event);
			}
		}
		la = null;
	}
}
