package cn.wzy.search.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.CRC32;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cn.wzy.search.pojo.Product;

public class SearchUtil {
	
	public static String makeXml(final List<Product> result) {
		Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("Products");
        Element e = null;
        Product prod = null;
        for (int i = 0; i < result.size(); i++) {
        	prod = result.get(i);
			e = root.addElement("Product");
			
			e.addElement("ProductCode").addText(String.valueOf(prod.getProductCode()));
			e.addElement("ProductName").addText(prod.getProductName());
			e.addElement("Introduction").addText(prod.getIntroduction());
			e.addElement("PurchasePrice").addText(String.valueOf(prod.getPurchasePrice()));
			e.addElement("MarketPrice").addText(String.valueOf(prod.getMarketPrice()));
			e.addElement("OurPrice").addText(String.valueOf(prod.getOurPrice()));
			e.addElement("Views").addText(String.valueOf(prod.getViews()));
			e.addElement("ProductImgUrl").addText(String.valueOf(prod.getProductImgUrl()));
			e.addElement("JianPin").addText(String.valueOf(prod.getJianPin()));
			e.addElement("PinYin").addText(String.valueOf(prod.getPinYin()));
        }
        
        return doc.asXML();
        
     }
	
	public static long makeCacheKey(String key) {
		CRC32 crc = new CRC32();
		crc.update(key.getBytes());
		return crc.getValue();
	}
}
