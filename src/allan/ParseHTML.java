package allan;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHTML {
	//解析欧路词典展出的HTML类型生词表
	public void Parse(String path) throws IOException, JDOMException{
		ParseXml ph = new ParseXml();
		LinkedList<String> al = new LinkedList<>();
		File input = new File(path);
		Document doc = Jsoup.parse(input, "UTF-8", "");
		Elements cl = doc.getElementsByClass("export-td");//获取所有Class为export-td的元素
		for(Element el: cl){
			al.add(el.text());//将所有元素文本添加到集合中
		}
		String fileName = path.substring(path.lastIndexOf("\\")+1);//截取词库文件名
		System.out.println(fileName);
		for(int i= 0; i<al.size(); i=i+3){//i=0,3,6...
			ph.creatXml(al.get(i), al.get(i+1), al.get(i+2), fileName);
//			System.out.println("num"+al.get(i)+"en"+al.get(i+1)+"ch"+al.get(i+2));
//			System.out.println("-----------");
		}
	}

}
