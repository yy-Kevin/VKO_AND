package cn.vko.ring.circle.model;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.text.TextUtils;

import cn.shikh.utils.BitmapUtils;

public class ImageInfo implements Serializable {

	private String url;// 图片地址
	private String burl;//大图地址
	private String aud;// 图片配的音频地址
//	private String locatAud;// 本地地址
	private int len;// 音频长度，单位-秒

	public ImageInfo() {

	}

	public String getBurl() {
		return burl;
	}

	public void setBurl(String burl) {
		this.burl = burl;
	}


	public ImageInfo(String url) {
		this.url = url;
	}
	public ImageInfo(String url,int len) {
		this.aud = url;
		this.len = len;
	}
	public void setAudio(String url,int len) {
		this.aud = url;
		this.len = len;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAud() {
		return aud;
	}

	public void setAud(String aud) {
		this.aud = aud;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public Bitmap getThumbnail(int width) {
		if (TextUtils.isEmpty(url))
			return null;
		Bitmap bitmap = BitmapUtils.getBitmapPreview(url, width, width, 0);
		if(bitmap == null) return null;
		Bitmap bm = BitmapUtils.toRoundCorner(bitmap, 10);
		return bm;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		ImageInfo a = (ImageInfo) o;
		return a.getUrl() == null ? false : a.getUrl().equals(getUrl());
	}
}
