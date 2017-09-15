package cn.vko.ring.circle.event;

public class TopicEvent<T> {

	/**
	 * 发贴事件
	 */
	public static final int SUBMIT_TOPIC_EVENT = 1 << 10;
	
	/**
	 * 删除贴子事件
	 */
	public static final int DETEIL_TOPIC_EVENT = 1 << 12;
	
	
	
	
	/**
	 * 事件类型
	 */
	private int eventType;
	private T event;
	public TopicEvent() {

	}

	public TopicEvent(T event, int eventType) {
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
