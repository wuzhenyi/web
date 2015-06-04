package cn.wzy.search.util;

public class StringUtil {
	public static boolean isNullOrBlank(final Object obj) {
		return obj == null || "".equals(obj.toString().trim());
	}
}
