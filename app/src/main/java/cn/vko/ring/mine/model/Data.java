/*
 *Data.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.Data
 *宣义阳 Create at 2015-8-4 下午2:38:17
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;

/**
 * cn.vko.ring.mine.model.Data
 * @author 宣义阳 
 * create at 2015-8-4 下午2:38:17
 */
public class Data implements Serializable{
	private String expire;
	private String canWithdraw;
	private int level;
	private int scord;
	public String getExpire() {
		return expire;
	}
	public void setExpire(String expire) {
		this.expire = expire;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScord() {
		return scord;
	}
	public void setScord(int scord) {
		this.scord = scord;
	}
	public String getCanWithdraw() {
		return canWithdraw;
	}
	public void setCanWithdraw(String canWithdraw){
		this.canWithdraw=canWithdraw;
	}

	
}
