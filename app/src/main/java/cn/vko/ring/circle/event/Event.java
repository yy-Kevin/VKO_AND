package cn.vko.ring.circle.event;

public class Event<T> {

	/**
	 * 创建群组事件
	 */
	public static final int CREATE_EVENT = 1 << 1;
	
	/**
	 * 同意加入群组事件
	 */
	public static final int JOIN_EVENT = 1 << 2;
	
	/**
	 * 退出群组事件
	 */
	public static final int QUIT_EVENT = 1 << 3;
	
	/**
	 * 解散群组事件
	 */
	public static final int DETEIL_EVENT = 1 << 4;
	
	/**
	 * 修改群组信息事件
	 */
	public static final int UPDATE_EVENT = 1 << 5;
	
	
	/**
	 * 事件类型
	 */
	private int eventType;
	private T event;
	public Event() {

	}

	public Event(T event, int eventType) {
		this.event=event;
		this.eventType=eventType;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public T getEvent() {
		return event;
	}

	public void setEvent(T event) {
		this.event = event;
	}
	

}
