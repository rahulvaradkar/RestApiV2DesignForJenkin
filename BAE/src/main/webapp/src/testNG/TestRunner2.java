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

public class TestRunner2 {
  
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	public static void main(String[] args) {
	
		String path = Paths.get(".").toAbsolutePath().normalize().toString();
		
		File file = new File(Paths.get(".").toAbsolutePath().normalize().toString());
		File file2 = file.getParentFile().getParentFile();
		
		
		System.out.println("**********File path Location   :"+file);
		System.out.println("**********File2 path Location   :"+file2);
		
		String cDateTime = "";
		Date date = new Date();	     
	    cDateTime = cDateTime + (sdf.format(date));   

		
	cDateTime = file2+"\\Reports\\Unit Test Reports"+"\\Test Report__"+cDateTime;
	
	System.out.println("**********Test Report Location   :"+cDateTime);
		

		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		//testng.setOutputDirectory("C:\\Tomcat8\\webapps\\BAE_4_3_CLEAR_TEXT_PASSWORD\\Test Reports\\"+"Test Report__"+cDateTime+"");
		//testng.setOutputDirectory(cDateTime);
		testng.setOutputDirectory(cDateTime);
		//testng.setTestClasses(new Class[] { xlLinkImportServiceTest.class });
		//testng.setTestClasses(new Class[] { C:\\Tomcat8\\webapps\\BAE_4_3_CLEAR_TEXT_PASSWORD\\WEB-INF\\classes\\test\\xlLinkExportServiceTest.class });
		
		List<String> suites = Lists.newArrayList();
	    // suites.add("C:\\Program Files\\Apache Software Foundation\\Tomcat 8.0\\webapps\\BAE_4_3_TAS\\testng.xml");
		
		System.out.println("**********TestNG.xml Location    :"+file2+"\\testng.xml");
		
		suites.add(file2+"\\testng.xml");
	    testng.setTestSuites(suites);
		
		testng.addListener(tla);
		testng.run();		
		
	
		
		
		
	}
}







