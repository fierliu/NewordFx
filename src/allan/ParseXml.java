package allan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class ParseXml {
	String fileName;
	public void creatXml(String num, String en, String ch, String name) throws JDOMException, IOException{
		fileName = name+".xml";
		SAXBuilder sb = new SAXBuilder();
		Document myDoc;
		File file = new File(fileName);
//		第一次保存时检查文件是否已存在
		if(!file.exists()){
			myDoc = new Document();
			Element word = new Element("word");
			word.setAttribute("id", num);
			word.addContent(new Element("en").setText(en));
			word.addContent(new Element("ch").setText(ch));
			Element voca = new Element("voca");
			voca.addContent(word);
			myDoc.setRootElement(voca);

			saveXML(myDoc, name);
		}else{//第二次存储时
			myDoc = sb.build(fileName);
			Element voca = myDoc.getRootElement();
			Element word = new Element("word");
			word.setAttribute("id", num);
			word.addContent(new Element("en").setText(en));
			word.addContent(new Element("ch").setText(ch));
			voca.addContent(word);

			saveXML(myDoc, name);
		}
	}
	public static void saveXML(Document doc, String name) {
        try {
           XMLOutputter xmlopt = new XMLOutputter();
           FileWriter writer = new FileWriter(name+".xml");
           // 指定文档格式
           Format fm = Format.getPrettyFormat();
           // fm.setEncoding("GB2312");
           xmlopt.setFormat(fm);
           // 将doc写入到指定的文件中
           xmlopt.output(doc, writer);
           writer.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
