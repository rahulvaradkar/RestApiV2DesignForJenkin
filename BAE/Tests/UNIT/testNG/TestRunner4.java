package testNG;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;

public class TestRunner4 {
  
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	public static void main(String[] args) {
	

				String currentPath = System.getProperty("user.dir");
		Paths.get(currentPath).getRoot();
		
		System.out.println("current Path  :"+currentPath);
		 
		
		
		
	}
}






