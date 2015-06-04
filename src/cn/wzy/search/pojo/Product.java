package cn.wzy.search.pojo;

import java.util.Date;

public class Product {
	private Integer productCode;
	private Date creationDate;
	private Date lastModified;
	private String productName;
	private String classCode;
	private String introduction;
	private String seller;
	private Double purchasePrice;
	private Double marketPrice;
	private Double ourPrice;
	private Integer views;
	private String productImgUrl;
	private String productTag;
	private String jianPin;
	private String pinYin;
	public Integer getProductCode() {
		return productCode;
	}
	public void setProductCode(Integer productCode) {
		this.productCode = productCode;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public Double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public Double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(Double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public Double getOurPrice() {
		return ourPrice;
	}
	public void setOurPrice(Double ourPrice) {
		this.ourPrice = ourPrice;
	}
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	public String getProductImgUrl() {
		return productImgUrl;
	}
	public void setProductImgUrl(String productImgUrl) {
		this.productImgUrl = productImgUrl;
	}
	public String getProductTag() {
		return productTag;
	}
	public void setProductTag(String productTag) {
		this.productTag = productTag;
	}
	public String getJianPin() {
		return jianPin;
	}
	public void setJianPin(String jianPin) {
		this.jianPin = jianPin;
	}
	public String getPinYin() {
		return pinYin;
	}
	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}
	@Override
	public String toString() {
		return "Product [productCode=" + productCode + ", creationDate="
				+ creationDate + ", lastModified=" + lastModified
				+ ", productName=" + productName + ", classCode=" + classCode
				+ ", introduction=" + introduction + ", seller=" + seller
				+ ", purchasePrice=" + purchasePrice + ", marketPrice="
				+ marketPrice + ", ourPrice=" + ourPrice + ", views=" + views
				+ ", productImgUrl=" + productImgUrl + ", productTag="
				+ productTag + ", jianPin=" + jianPin + ", pinYin=" + pinYin
				+ "]";
	}
}
