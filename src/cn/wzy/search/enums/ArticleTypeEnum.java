package cn.wzy.search.enums;

public enum ArticleTypeEnum {
	Network(1, "互联网"),
	YangSheng(2, "养生保健"),
	Economy(3, "经济");
	int value;
	String name;
	private ArticleTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
	public static void main(String[] args) {
		System.out.println(ArticleTypeEnum.values());
	}
}
