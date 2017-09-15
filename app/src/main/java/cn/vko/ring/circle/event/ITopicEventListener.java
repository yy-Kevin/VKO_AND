package cn.vko.ring.circle.event;


public interface ITopicEventListener<T> {
	public void event(TopicEvent<T> event);
	public int getFilter();
}
