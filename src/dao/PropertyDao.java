package dao;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import model.Property;
import tool.SaveXml;

public class PropertyDao {
	//查
	public Property getProperty(String id) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
			doc = sb.build("Property.xml");//读取
			Element root = doc.getRootElement();
			Property prop = new Property();
			List<Element> list = root.getChildren("user");
			for (Element el : list) {
				if(el.getAttributeValue("id").equals(id)){
					prop.setQuota(el.getChildText("quota"));
					prop.setVocName(el.getChildText("vocName"));
					prop.setName(el.getChildText("name"));
					prop.setCode(el.getChildText("code"));
					prop.setTotal(el.getChildText("total"));
					prop.setDay(el.getChildText("day"));
				}
			}
		return prop;
	}
	//改
	public void setProperty(Property propt) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build("Property.xml");
		Element root = doc.getRootElement();

		String id = propt.getId();
//		String name = propt.getName();
//		String code = propt.getCode();
		String vocName = propt.getVocName();
		String quota = propt.getQuota();
		String total = propt.getTotal();
		String day = propt.getDay();

		List<Element> list = root.getChildren("user");
		for (Element el : list) {
			if (el.getAttributeValue("id").equals(id)) {
//				el.getChild("code").setText(code);
//				el.getChild("name").setText(name);
				el.getChild("vocName").setText(vocName);
				el.getChild("quota").setText(quota);
				el.getChild("total").setText(total);
				el.getChild("day").setText(day);
			}
		}
		SaveXml.saveXML(doc, "Property.xml");
	}
//	改day
	public void setDay(String day) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build("Property.xml");
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren("user");
		for (Element el : list) {
			if (el.getAttributeValue("id").equals("1")) {
				el.getChild("day").setText(day);
			}
		}
		SaveXml.saveXML(doc, "Property.xml");
	}
}
