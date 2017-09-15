
package cn.vko.ring.home.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.mine.model.TextBookInfo;
import cn.vko.ring.study.model.BookInfo;

public class SubjectInfo implements Serializable {
	public static final String SUBJECT_INFO="SubjectInfo";
	private String subjectId;
	
	private String versionId;
	private String bvName;
	private String LearnId;
	private String bookid;	
	
	private String imgurl;
	private String bookname;
	
	
	//新版2.0 参数
	
	private String id; //科目id
	private String bookId;//书的id
	private String version;//版本id
	private String vname;//版本名字
	private boolean subState; 
	private String name;// 科目名字
	private int state;
	private SubjectBook book;
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	
	
	public String getBvName() {
	
		return bvName;
	}
	
	public void setBvName(String bvName) {
	
		this.bvName = bvName;
	}
	
	public boolean isSubState() {
	
		return subState;
	}
	
	public void setSubState(boolean subState) {
	
		this.subState = subState;
	}
	public SubjectInfo(){
	}
	public SubjectInfo(String id){
		this.subjectId = id;
	}
	public List<TextBookInfo> bookVersion;
	
	public List<TextBookInfo> getBookVersion() {
		
		return bookVersion;
	}
	
	
	
	public void setBookVersion(List<TextBookInfo> bookVersion) {
	
		this.bookVersion = bookVersion;
	}


	

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	

	public String getVersionId() {
	
		return versionId;
	}
	
	public void setVersionId(String versionId) {
	
		this.versionId = versionId;
	}
	public String getLearnId() {
		return LearnId;
	}
	public void setLearnId(String learnId) {
		LearnId = learnId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	


	public String getBookid() {
		return bookid;
	}
	public void setBookid(String bookid) {
		this.bookid = bookid;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}



	public int getState() {
		return state;
	}



	public void setState(int state) {
		this.state = state;
	}



	public String getSubjectId() {
		return subjectId;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		SubjectInfo k = (SubjectInfo) o;
		if (k.getSubjectId() !=null && k.getSubjectId().equals(getSubjectId())) {
			return true;
		} else {
			return false;
		}
	}
	
	public BookInfo getBook(){
		BookInfo book=new BookInfo();
		book.setBookid(getBookid());
		book.setBookname(getBookname());
		book.setImgurl(getImgurl());
		book.setLearn(getLearnId());
		book.setVersionId(getVersionId());
		book.setSubjectId(getSubjectId());
		return book;
	}
	
	
}
