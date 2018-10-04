package tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.JDOMException;

import controller.MyController;

public class ParseTXT {
	public void parseTxt(String path) throws JDOMException, IOException{
		ParseXml ph = new ParseXml();
		String _fileName = path.substring(path.lastIndexOf("\\")+1);//截取词库文件名
		String fileName = _fileName.substring(0, (_fileName.length()-5));//去掉.HTML后缀
		System.out.println("ParseTXT上下格式文件名称："+fileName);
		ArrayList<String> al = readLinedFile(path);

		for(Integer i = 0; i< al.size(); i=i+2){
//			System.out.println("i="+ al.get(i));
//			System.out.println("i+1="+ al.get(i+1));
			Integer id = i/2 +1;
			ph.creatXml(id.toString(), al.get(i), al.get(i+1), fileName, "-1", "0");
		}

		MyController mc = new MyController();
		mc.showInfo("词库导入完成！");
	}
	public ArrayList<String> readLinedFile(String filename){
		ArrayList<String> filecon = new ArrayList<String>();
		  String m = "";
		  BufferedReader file = null;
		  try{
		   file = new BufferedReader(new FileReader(filename));
		   while ((m = file.readLine()) != null) {
		    if (!m.equals("")) // 不需要读取空行
		    {
		     filecon.add(m);
		    }
		   }
		   file.close();
		  }catch(IOException e){
		   e.printStackTrace();
		  }

		  return filecon;
		 }
}
