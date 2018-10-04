package tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Copy {

	public static void copyF(String source ) throws IOException{
		System.out.println("file coped.");
		Files.copy(Paths.get(source), Paths.get("/voca.xml"), StandardCopyOption.REPLACE_EXISTING);

	}
//	public static void main(String[] args) throws IOException {
//		 copy("十月生词.html.xml");
//	}
}
