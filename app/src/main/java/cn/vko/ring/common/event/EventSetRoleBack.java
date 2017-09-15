/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: EventSetRoleBack.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.event 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-8-26 下午2:14:54 
 * @version: V1.0   
 */
package cn.vko.ring.common.event;

/**
 * @ClassName: EventSetRoleBack
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-8-26 下午2:14:54
 */
public class EventSetRoleBack {
	boolean isSet;
	int doCheck;

	public EventSetRoleBack(boolean isSet,int docheck) {
		super();
		this.isSet = isSet;
		this.doCheck = docheck;
	}
	
	public int getDoCheck() {
		return doCheck;
	}

	public void setDoCheck(int doCheck) {
		this.doCheck = doCheck;
	}

	public boolean isSet() {
		return isSet;
	}
	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}
}
