package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.study.model.BookInfo;


/**
 * @author shikh
 *
 */
public class TextBookInfo implements Serializable {
	private String subId;
	private String Learn;
	private String bvId;
	private String bvName;
//	private boolean state;
	private List<BookInfo> book;
	public TextBookInfo(){
	}
	public TextBookInfo(String bvId){
		this.bvId = bvId;
	}
	
	public String getSubId() {
	
		return subId;
	}

	
	public void setSubId(String subId) {
	
		this.subId = subId;
	}

	
	public String getLearn() {
	
		return Learn;
	}

	
	public void setLearn(String learn) {
	
		Learn = learn;
	}

	public String getBvId() {
	
		return bvId;
	}
	
	public void setBvId(String bvId) {
	
		this.bvId = bvId;
	}
	
	public String getBvName() {
	
		return bvName;
	}
	
	public void setBvName(String bvName) {
	
		this.bvName = bvName;
	}
	
//	public boolean isState() {
//	
//		return state;
//	}
//	
//	public void setState(boolean state) {
//	
//		this.state = state;
//	}
	
	public List<BookInfo> getBook() {
	
		return book;
	}
	
	public void setBook(List<BookInfo> book) {
	
		this.book = book;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		TextBookInfo t = (TextBookInfo) o;
		return t.getBvId().equals(getBvId());
	}
	
	
}
