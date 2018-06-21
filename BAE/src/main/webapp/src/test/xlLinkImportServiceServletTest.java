package test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeClass;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.boardwalk.database.DatabaseLoader;

import servlets.xlLinkImportService;

public class xlLinkImportServiceServletTest {

	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	HttpSession session;
	@Mock
	RequestDispatcher rd;
	@Mock
	ServletOutputStream  servletOut;
	public final static String Seperator = new Character((char)1).toString();
	public final static String ContentDelimeter = new Character((char)2).toString();
	public static String propFileLocation="C:\\Tomcat8\\webapps\\BAE_TEST\\testdata.properties";

    @BeforeClass
    public void setUp() throws Exception
    {
    	MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() throws Exception {
    	
    	DataSet ds =new DataSet();
		String buffer=ds.getUserId()+Seperator+ds.getUsername()+Seperator+ds.getMemberID()+Seperator+"2"+Seperator+ds.getTableID()+"-10";
	 	ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		ZipOutputStream out = new ZipOutputStream(bos);
		out.putNextEntry(new ZipEntry("request.txt"));
		out.write(buffer.getBytes("UTF-8"));

//		out.flush();
		out.closeEntry();
		out.close ();
		bos.close();

//		encode to Base64 string
		String b64String = Base64.encodeBase64String(bos.toByteArray());
		Reader inputString = new StringReader(b64String);
		BufferedReader reqbuffer = new BufferedReader(inputString);
		
		when(request.getReader()).thenReturn(reqbuffer);
		when(response.getOutputStream()).thenReturn(servletOut);
		new xlLinkImportService().service(request, response);
		
		ArgumentCaptor<String> bufferCaptor = ArgumentCaptor.forClass(String.class);
		verify(servletOut).print(bufferCaptor.capture());
		String responseBody = bufferCaptor.getValue();		 
		
		ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(Base64.decodeBase64(responseBody.toString().getBytes())));
		zipIn.getNextEntry();
		StringBuffer sb = new StringBuffer();
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
		IOUtils.copy(zipIn, out1);
		sb = new StringBuffer();
		sb.append(out1.toString("UTF-8"));		
		String[] res=sb.toString().split(ContentDelimeter);
		String[] res1=res[0].split(Seperator);
		System.out.println("length "+res.length);
		System.out.println("Res 2 :"+res[1]);
		System.out.println("Res 3 :"+res[2]);
		System.out.println("Res 4 :"+res[3]);
	
		
		FileInputStream in = new FileInputStream(propFileLocation);
		Properties props = new Properties();
		props.load(in);
		in.close();

		FileOutputStream out2 = new FileOutputStream(propFileLocation);
		props.setProperty("ImportId", res1[10]);
		props.setProperty("ExportId", res1[11]);
		props.setProperty("columns", res1[8]);
		props.setProperty("Rows", res1[9]);
		props.store(out2, null);
		out2.close();
		
		AssertJUnit.assertEquals("Success",res1[0]);
    }
}