package testNG;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;

public class TestRunner {
  
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
	public static void main(String[] args) {
	
		
		String cDateTime = "";
		Date date = new Date();	     
	    cDateTime = cDateTime + (sdf.format(date));   
		System.out.println(cDateTime);
		
	cDateTime = "C:\\Tomcat8\\webapps\\BAE_4_3_CLEAR_TEXT_PASSWORD\\Test Reports\\"+"Test Report__"+cDateTime;
	     
		

		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		//testng.setOutputDirectory("C:\\Tomcat8\\webapps\\BAE_4_3_CLEAR_TEXT_PASSWORD\\Test Reports\\"+"Test Report__"+cDateTime+"");
		testng.setOutputDirectory(cDateTime);
		testng.setTestClasses(new Class[] { xlLinkImportServiceTest.class });
		//testng.setTestClasses(new Class[] { C:\\Tomcat8\\webapps\\BAE_4_3_CLEAR_TEXT_PASSWORD\\WEB-INF\\classes\\test\\xlLinkExportServiceTest.class });
		testng.addListener(tla);
		testng.run();		
		
		
		
		
		
	}
}







