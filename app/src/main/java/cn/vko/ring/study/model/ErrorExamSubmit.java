/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ErrorExamSubmit.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-21 下午5:45:58 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;

/**
 * @ClassName: ErrorExamSubmit
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-21 下午5:45:58
 */
public class ErrorExamSubmit implements Serializable {
	public static final String ERROR_SUBMIT="ErrorExamSubmit";
	private String id;
	private String content;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
