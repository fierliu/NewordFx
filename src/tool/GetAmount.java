package tool;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class GetAmount {
	public static Integer getAmount(String file, String child) throws JDOMException, IOException{
		SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(new File(file));
			Element element = document.getRootElement();
			List<Element> children = element.getChildren(child);
			Integer total = children.size();
		return total;

	}
}
