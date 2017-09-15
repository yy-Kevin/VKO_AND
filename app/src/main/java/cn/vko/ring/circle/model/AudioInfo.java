package cn.vko.ring.circle.model;

import java.io.Serializable;

import android.text.TextUtils;

public class AudioInfo implements Serializable {
	private String url;
	
	private int len;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public AudioInfo(){};
	public AudioInfo(String url,int len){
		this.url = url;
		this.len = len;
	}
//	public String getLocatUrl() {
//		return locatUrl;
//	}
//	public void setLocatUrl(String locatUrl) {
//		this.locatUrl = locatUrl;
//	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		AudioInfo a = (AudioInfo) o;
		return a.getUrl().equals(getUrl());
	}
}
