/*
 *MemberCenterInfo.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.MemberCenterInfo
 *宣义阳 Create at 2015-8-4 下午3:34:47
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

/**
 * cn.vko.ring.mine.model.MemberCenterInfo
 * @author 宣义阳 
 * create at 2015-8-4 下午3:34:47
 */
public class MemberCenterInfo extends BaseResponseInfo {

	private List<Data> data;
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public class Data implements Serializable{
		private String description;
		private int discount;
		private int id;
		private String imgPath;
		private int months;
		private String name;
		private double price;
		private double sellPrice;
		private int status;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public int getDiscount() {
			return discount;
		}
		public void setDiscount(int discount) {
			this.discount = discount;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getImgPath() {
			return imgPath;
		}
		public void setImgPath(String imgPath) {
			this.imgPath = imgPath;
		}
		public int getMonths() {
			return months;
		}
		public void setMonths(int months) {
			this.months = months;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public double getSellPrice() {
			return sellPrice;
		}
		public void setSellPrice(double sellPrice) {
			this.sellPrice = sellPrice;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		
	}
}
