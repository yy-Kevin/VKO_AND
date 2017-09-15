/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamContentDetail.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-19 下午4:22:15 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;

/** 
 * @ClassName: ExamContentDetail 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-19 下午4:22:15  
 */
public class ExamContentDetail implements Serializable {
	
	private String id;
	private String objective;
	private String context;
	private String typeId;
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
}