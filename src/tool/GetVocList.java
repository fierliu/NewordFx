package tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
//选择词库时获取本地词库列表
public class GetVocList {
	public static List<File> getVocList(){
        File dir = new File("VocaList");
        List<File> list = new ArrayList<File>();
        findFile(dir, list);
        return list;
	}
	private static void  findFile(File dir, List<File> list) {
        File[] files = dir.listFiles();
        if ((files != null) && (files.length > 0)) {
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                list.add(file);
            }
        }
    }
}
