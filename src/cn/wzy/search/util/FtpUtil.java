package cn.wzy.search.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.wzy.search.pojo.FtpNode;

public class FtpUtil {
	
	private static final String FileName = "ftp_image.xml";
	private static final String FTP_BASE_PATH = "/usr/local/nginx-image/html/images";
	
	public static void main(String[] args){
		List<FtpNode> nodes = readXML(FileName);
		File file = new File("C:\\Users\\wutiangui\\Desktop\\gif\\当了爸爸之后.gif");
		if (ftpUpload(nodes,file))
			System.out.println("上传文件成功！！！");
	}

	private static boolean ftpUpload(List<FtpNode> nodes, File file) {
		for (FtpNode node : nodes) {
			FTPClient ftpClient = new FTPClient();
			try {
				ftpClient.connect(node.getIp(), node.getPort());
				ftpClient.login(node.getUsername(), node.getPassword());
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				int replyCode = ftpClient.getReplyCode();
				if (!FTPReply.isPositiveCompletion(replyCode)) {
					ftpClient.disconnect();
					return false;
				}
				ftpClient.enterLocalPassiveMode();
				ftpClient.setControlEncoding("UTF-8");
				
				ftpClient.changeWorkingDirectory(FTP_BASE_PATH);
				FileInputStream fis = new FileInputStream(file);
				ftpClient.storeFile(new String(file.getName().getBytes("UTF-8"), "iso-8859-1"), fis);
				fis.close();
				ftpClient.logout();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	private static List<FtpNode> readXML(String filename) {
		List<FtpNode> nodes = new ArrayList<FtpNode>();
		try {
			InputStream in = FtpUtil.class.getResourceAsStream("/" + filename);
			Document doc = new SAXReader().read(in);
			Iterator it = doc.getRootElement().elements("node").iterator();
			while (it.hasNext()) {
				FtpNode node = new FtpNode();
				Element ele = (Element) it.next();
				node.setIp(ele.element("ip").getText());
				node.setPort(Integer.parseInt(ele.element("port").getText()));
				node.setUsername(ele.element("username").getText());
				node.setPassword(ele.element("password").getText());
				nodes.add(node);
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return nodes;
	}
}
