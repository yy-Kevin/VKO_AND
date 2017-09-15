package cn.vko.ring.circle.event;


public interface IEventListener<T> {
	public void event(Event<T> event);
	public int getFilter();
}
