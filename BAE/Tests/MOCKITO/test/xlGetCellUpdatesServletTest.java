package test;

/**
 * 
 * @author Jaydeep bobade
 * */
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;

import com.boardwalk.database.DatabaseLoader;

import servlets.xlGetCellUpdates;
import servlets.xlLinkImportService;


public class xlGetCellUpdatesServletTest {
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
    
	@BeforeClass
    public void setUp() throws Exception 
    {
    	MockitoAnnotations.initMocks(this);
    	servletOut = mock(ServletOutputStream.class); 
    }

    @Test
    public void test() throws Exception {
	 	DataSet ds=new DataSet();		
	 	String buffer=ds.getTableID()+"Week1524614400000152521920000051291200-11525264331000";
	 	
	 	ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		ZipOutputStream out = new ZipOutputStream(bos);
		out.putNextEntry(new ZipEntry("response.txt"));
		out.write(buffer.getBytes("UTF-8"));
		out.closeEntry();
		out.close ();
		bos.close();

//		encode to Base64 string
		String b64String = Base64.encodeBase64String(bos.toByteArray());
		Reader inputString = new StringReader(b64String);
		BufferedReader reqbuffer = new BufferedReader(inputString);
		
		when(request.getReader()).thenReturn(reqbuffer);
		when(response.getOutputStream()).thenReturn(servletOut);
		new xlGetCellUpdates().service(request, response);
		
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
		AssertJUnit.assertEquals("Success",res[0]);
    }
}
