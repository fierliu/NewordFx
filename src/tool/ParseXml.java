package tool;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import model.Word;

//第一次从别的格式词库转换成XML格式词库
public class ParseXml {
	String fileName;
	public void creatXml(String id, String en, String ch, String name, String _date,
			String times) throws JDOMException, IOException{
		fileName = "VocaList/"+ name+".xml";//原始词库的保存位置
		SAXBuilder sb = new SAXBuilder();
		Document myDoc;
		File file = new File(fileName);

//		第一次保存时检查文件是否已存在
		if(!file.exists()){
			myDoc = new Document();
			Element word = new Element("word");
			word.setAttribute("id", id);
			word.addContent(new Element("en").setText(en));
			word.addContent(new Element("ch").setText(ch));
			word.addContent(new Element("date").setText(_date));
			word.addContent(new Element("know").setText(times));
			Element voca = new Element("voca");
			voca.addContent(word);
			myDoc.setRootElement(voca);

			SaveXml.saveXML(myDoc, fileName);
		}else{//第二次存储时
			myDoc = sb.build(fileName);
			Element voca = myDoc.getRootElement();
			Element word = new Element("word");
			word.setAttribute("id", id);
			word.addContent(new Element("en").setText(en));
			word.addContent(new Element("ch").setText(ch));
			word.addContent(new Element("date").setText(_date));
			word.addContent(new Element("know").setText(times));
			voca.addContent(word);

			SaveXml.saveXML(myDoc, fileName);
		}
	}

//	创建临时xml词库
	public void creatTempXml(Word _word, String id) throws JDOMException, IOException{
		fileName = "TempVoc.xml";//临时词库的保存位置
		SAXBuilder sb = new SAXBuilder();
		Document myDoc;
		File file = new File(fileName);

//		第一次保存时检查文件是否已存在
		if(!file.exists()){
			myDoc = new Document();
			Element word = new Element("word");
			word.setAttribute("id", id);
			word.addContent(new Element("en").setText(_word.getEn()));
			word.addContent(new Element("ch").setText(_word.getCh()));
			word.addContent(new Element("date").setText(_word.getDate()));
			word.addContent(new Element("know").setText(_word.getKnow()));
			Element voca = new Element("voca");
			voca.addContent(word);
			myDoc.setRootElement(voca);

			SaveXml.saveXML(myDoc, fileName);
		}else{//第二次存储时
			myDoc = sb.build(fileName);
			Element voca = myDoc.getRootElement();
			Element word = new Element("word");
			word.setAttribute("id", id);
			word.addContent(new Element("en").setText(_word.getEn()));
			word.addContent(new Element("ch").setText(_word.getCh()));
			word.addContent(new Element("date").setText(_word.getDate()));
			word.addContent(new Element("know").setText(_word.getKnow()));
			voca.addContent(word);

			SaveXml.saveXML(myDoc, fileName);
		}
	}

}
