package cn.vko.ring.home.model;

/**
 * 主菜单
 * @author Shikh
 *
 */
public class TabMenuInfo {

	public TabMenuInfo(){}
	public TabMenuInfo(int viewId,int focusedImageId,int defaultImageId ,int textId,int defaultTextColor,int focusedTextColor){
		this.viewId=viewId;
		this.defaultImageId=defaultImageId;
		this.focusedImageId=focusedImageId;
		this.textId=textId;
		this.focusedTextColor=focusedTextColor;
		this.defaultTextColor=defaultTextColor;
	}
	public int getFocusedImageId() {
		return focusedImageId;
	}
	public void setFocusedImageId(int focusedImageId) {
		this.focusedImageId = focusedImageId;
	}
	public int getDefaultImageId() {
		return defaultImageId;
	}
	public void setDefaultImageId(int defaultImageId) {
		this.defaultImageId = defaultImageId;
	}
	public int getFocusedTextColor() {
		return focusedTextColor;
	}
	public void setFocusedTextColor(int focusedTextColor) {
		this.focusedTextColor = focusedTextColor;
	}
	public int getDefaultTextColor() {
		return defaultTextColor;
	}
	public void setDefaultTextColor(int defaultTextColor) {
		this.defaultTextColor = defaultTextColor;
	}
	public int getViewId() {
		return viewId;
	}
	public void setViewId(int viewId) {
		this.viewId = viewId;
	}
	public boolean isFocused() {
		return isFocused;
	}
	public void setFocused(boolean isFocused) {
		this.isFocused = isFocused;
	}
	public int getCurrTextColor(){
		if(this.isFocused){
			return this.focusedTextColor;
		}
		return this.defaultTextColor;
	}
	public int getCurrImageId(){
		if(this.isFocused){
			return this.focusedImageId;
		}
		return this.defaultImageId;
	}
	public int getTextId() {
		return textId;
	}
	public void setTextId(int textId) {
		this.textId = textId;
	}
	
	/**
	 * 资源标签
	 */
	private int textId;
	/**
	 * 焦点图片资源ID
	 */
	private int focusedImageId;

	/**
	 * 默认图片资源ID
	 */
	private int defaultImageId;
	/**
	 * 焦点字体颜色
	 */
	private int focusedTextColor;
	/**
	 * 默认字体颜色
	 */
	private int defaultTextColor;
	/**
	 * 唯一标识
	 */
	private int viewId;
	
	/**
	 * 是否获取焦点
	 */
	private boolean isFocused=false;

	

}
