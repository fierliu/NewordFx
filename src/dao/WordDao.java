package dao;

import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import model.Word;
import tool.SaveXml;
/*
*最初用于操作xml类型的词库
 *  计划后期用于生成原始词库时对词的操作
* */
public class WordDao {
//读和查取单词，条件是日期
	public Word getWordByDate(String vocName, String _date) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build(vocName);//读取,vocName需带.xml
		Element root = doc.getRootElement();
		Word word = new Word();
		List<Element> list = root.getChildren("word");
		for (Element el : list) {
//			System.out.println("ch:"+el.getChildText("date"));
//			System.out.println("date:"+_date);
			if(el.getChildText("date").equals(_date)){

				word.setCh(el.getChildText("ch"));
				word.setEn(el.getChildText("en"));
				word.setId(el.getAttributeValue("id"));
				word.setKnow(el.getChildText("know"));
			}
		}
		return word;
	}
//根据id查取词
	public Word getWordById(String vocName, String id) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build("Vocalist/"+vocName);//读取,vocName需带.xml
		Element root = doc.getRootElement();
		Word word = new Word();
		List<Element> list = root.getChildren("word");
		for (Element el : list) {
			if(el.getAttributeValue("id").equals(id)){
				word.setCh(el.getChildText("ch"));
				word.setEn(el.getChildText("en"));
				word.setId(el.getAttributeValue("id"));
				word.setKnow(el.getChildText("know"));
				word.setDate(el.getChildText("date"));
			}
		}
		return word;
	}
//	根据英文查word信息
	public Word getWordByEn(String vocName, String en) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build(vocName);//读取,vocName需带.xml
		Element root = doc.getRootElement();
		Word word = new Word();
		List<Element> list = root.getChildren("word");
		for (Element el : list) {
			if(el.getChildText("en").equals(en)){
				word.setCh(el.getChildText("ch"));
				word.setId(el.getAttributeValue("id"));
				word.setKnow(el.getChildText("know"));
				word.setDate(el.getChildText("date"));
			}
		}
		return word;
	}
//	查今日学习量
	public Integer getTodayAmount(String vocName, String _date) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build(vocName);//读取,vocName需带.xml
		Element root = doc.getRootElement();
		Integer count=0;
		List<Element> list = root.getChildren("word");
		for (Element el : list) {
			if(el.getChildText("date").equals(_date)){
				count++;
			}
		}
		System.out.println("今日学习量："+count);
		return count;
	}
//	根据单词id改TempVoc.xml单词时间
	public void updateWordDate(String _date, String id) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build("TempVoc.xml");
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren("word");
		for (Element el : list) {
			if(el.getAttributeValue("id").equals(id)){
				Element date = el.getChild("date");
				date.setText(_date);
			}
		}
		SaveXml.saveXML(doc, "TempVoc.xml");
	}
//根据id修改word时间和know
	public void updateWordDate_Know(String _date, String _know, String id) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build("TempVoc.xml");
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren("word");
		for (Element el : list) {
			if(el.getAttributeValue("id").equals(id)){
				Element date = el.getChild("date");
				date.setText(_date);
				Element know = el.getChild("know");
				know.setText(_know);
			}
		}
		SaveXml.saveXML(doc, "TempVoc.xml");
	}
//	将所有date>0的单词date-1
	public void updateDate() throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build("TempVoc.xml");
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren("word");
		for (Element el : list) {
			if(Integer.valueOf(el.getChildText("date")) > 0){
				Element date = el.getChild("date");
				Integer newDate = Integer.valueOf(el.getChildText("date"))-1;
//				System.out.println("newDate:"+newDate);
				if(newDate>=0){ date.setText(newDate.toString());
				}else{ date.setText("0");}
			}
		}
		SaveXml.saveXML(doc, "TempVoc.xml");
	}
//	根据id删除单词
	public void deleteWord(String vocName, String id) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		Document doc = null;
		doc = sb.build("TempVoc.xml");
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren("word");
		int size = list.size();
		for (int i = 0; i < size; i++) {
            Element el = (Element) list.get(i);    // 取得节点实例
            if (el.getAttributeValue("id").equals(id)) {     // 如果是chn标记。。则在if中写自己的操作
                // root.removeChild("chn");         // 移除节点 都可以。。。
//            	System.out.println("id:"+ id + ", remove:"+el.getAttribute(id));
                root.removeContent(el);             // 移除节点 都可以。。。
                i--;
                size--;

            }
//            el.removeAttribute("sid");             // 移除属性sid
        }
//		for (Element el : list) {
//			if(el.getAttributeValue("id").equals(id)){
//				root.removeContent(el);
//			}
//		}
		SaveXml.saveXML(doc, "TempVoc.xml");
	}

}
