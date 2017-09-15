package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

public class SynchronousCourses implements Serializable{
	private String bookName;
			
	private String bookId;
	
	private String versionId;
	
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	private List<KnowledgeSection> chapterList;
				
	public List<KnowledgeSection> getChapterList() {
		return chapterList;
	}

	public void setChapterList(List<KnowledgeSection> chapterList) {
		this.chapterList = chapterList;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	
	
}

