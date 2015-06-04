package cn.wzy.search.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import org.apache.commons.net.util.Base64;

public class CommonUtil {
	public static String getMD5Password(String originalPassword){
		try {
			MessageDigest digest=MessageDigest.getInstance("MD5");
			byte[] originalByte=digest.digest(originalPassword.getBytes());
			
			byte[] newByte=Base64.encodeBase64(originalByte);
			return new String(newByte);
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static long getCRCCode(String src) {
		CRC32 crc = new CRC32();
		crc.update(src.getBytes());
		return crc.getValue();
	}
	
	public static void main(String[] args) {
		String admin = "admin";
		System.out.println(getCRCCode(admin));
	}
}
