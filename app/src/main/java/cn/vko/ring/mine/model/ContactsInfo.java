/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-11 
 * 
 *******************************************************************************/ 
package cn.vko.ring.mine.model;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 
 * 编写日期:	2015-8-11
 * 作者:	宋宁宁
 * 
 * 历史记录
 *    修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ContactsInfo {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	/**
	 * @return ssssssss
	 */
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ContactsInfo [name=" + name + ", phone=" + phone + ", id=" + id
				+ "]";
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return ssssssss
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 
	 */
	public ContactsInfo() {
		super();
	}
	private String name;
	private String phone;
	private int id;
	/**
	 * @param name
	 * @param phone
	 * @param id
	 */
	public ContactsInfo(String name, String phone, int id) {
		super();
		this.name = name;
		this.phone = phone;
		this.id = id;
	}
	/**
	 * @return ssssssss
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
}
