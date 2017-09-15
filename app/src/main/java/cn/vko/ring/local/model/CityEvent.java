package cn.vko.ring.local.model;

import java.io.Serializable;

import cn.vko.ring.local.model.CityAndSubject.CSdatas;

public class CityEvent implements Serializable{

	CSdatas citySubject;

	public CityEvent(CSdatas citySubject) {
		super();
		this.citySubject = citySubject;
	}

	public CSdatas getCitySubject() {
		return citySubject;
	}

	public void setCitySubject(CSdatas citySubject) {
		this.citySubject = citySubject;
	}
	
	


}
