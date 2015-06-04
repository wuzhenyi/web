package cn.wzy.search.pojo;

import java.util.Date;

public class SearchResult {
	
	private long _id;
	private String searchContent;
	private Date createDate;
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
	public String getSearchContent() {
		return searchContent;
	}
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
