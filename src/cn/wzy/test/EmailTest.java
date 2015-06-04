package cn.wzy.test;

import java.util.UUID;

import org.apache.commons.mail.HtmlEmail;

public class EmailTest {
	public static void main(String[] args) throws Exception {
		HtmlEmail he = new HtmlEmail();
		he.setStartTLSEnabled(true);
		he.setHostName("smtp.163.com");
		he.setSmtpPort(25);
		he.setAuthentication("a15018268203@163.com", "3555279");
		he.addTo("1509769972@qq.com", "吴侦医");
		he.setFrom("a15018268203@163.com", "吴填贵");
		he.setSubject("Email测试");
		he.setCharset("UTF-8");
		he.setHtmlMsg("恭喜，您已注册成功，请点击<a href=\"https://www.baidu.com\">这里</a>完成激活。");
		he.send();
//		String code = UUID.randomUUID().toString();
//		System.out.println(code);
	}
}
