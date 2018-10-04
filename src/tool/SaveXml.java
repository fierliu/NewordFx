package tool;

import java.io.FileWriter;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class SaveXml {
	public static void saveXML(Document doc, String name) {
        try {
           XMLOutputter xmlopt = new XMLOutputter();
           FileWriter writer = new FileWriter(name);
           // 指定文档格式
           Format fm = Format.getPrettyFormat();
           // fm.setEncoding("GB2312");
           xmlopt.setFormat(fm);
           // 将doc写入到指定的文件中
           xmlopt.output(doc, writer);
//           writer.flush();
           writer.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
