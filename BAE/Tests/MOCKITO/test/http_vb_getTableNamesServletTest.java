package test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import servlets.http_vb_getTableInfo;
import servlets.httpt_vb_Login;
import servlets.httpt_vb_getTableNames;

public class http_vb_getTableNamesServletTest {
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
	@Mock
	ServletContext sc;
	@Mock
	FilterConfig filterConfig;
	public final static String Seperator = new Character((char)1).toString();
	public final static String ContentDelimeter = new Character((char)2).toString();
    
	@BeforeClass
    public void setUp() throws Exception 
    {
    	MockitoAnnotations.initMocks(this);
    	sc=mock(ServletContext.class);
    }

    @Test
    public void test() throws Exception {
	 	DataSet ds=new DataSet();
		
	 	String buffer=ds.getUserId()+Seperator+ds.getUsername();
	 	ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		ZipOutputStream out = new ZipOutputStream(bos);
		out.putNextEntry(new ZipEntry("request.txt"));
		out.write(buffer.getBytes("UTF-8"));
		out.closeEntry();
		out.close();
		bos.close();

//		encode to Base64 string
		String b64String = Base64.encodeBase64String(bos.toByteArray());
		Reader inputString = new StringReader(b64String);
		BufferedReader reqbuffer = new BufferedReader(inputString);
		httpt_vb_getTableNames servlet=new httpt_vb_getTableNames();
		
		when(request.getReader()).thenReturn(reqbuffer);
		when(request.getServletContext()).thenReturn(sc);
		when(response.getOutputStream()).thenReturn(servletOut);
		when(sc.getRequestDispatcher("/jsp/admin/chooseNeighborhood.jsp")).thenReturn(rd);
		when(sc.getRequestDispatcher("/jsp/admin/joinNeighborhood.jsp")).thenReturn(rd);
		servlet.service(request, response);
		
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
		
		String[] res=sb.toString().split(Seperator);		
		AssertJUnit.assertEquals("Success",res[0]);
    }
}
