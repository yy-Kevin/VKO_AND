package cn.vko.ring.home.model;

public class BtnInfo {
	
	private String text;
	private int textColor;
	private int textBg;
	
	public BtnInfo(String text,int textColor,int textBg){
		this.text = text;
		this.textColor = textColor;
		this.textBg = textBg;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getTextColor() {
		return textColor;
	}
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	public int getTextBg() {
		return textBg;
	}
	public void setTextBg(int textBg) {
		this.textBg = textBg;
	}
	
	
}
