package test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import servlets.LoginServlet;

public class LoginSevletTest {
	
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
	ServletConfig config;
	@Mock
	LoginServlet ls;
	
	public final static String Seperator = new Character((char)1).toString();
	public final static String ContentDelimeter = new Character((char)2).toString();
    
	@BeforeClass
    public void setUp() throws Exception 
    {
    	MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() throws Exception {
	 	DataSet ds=new DataSet();
		String instance_name = ds.getInstanceName();
	 	String buffer="user10DESIGN";
	 	ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		ZipOutputStream out = new ZipOutputStream(bos);
		out.putNextEntry(new ZipEntry("request.txt"));
		out.write(buffer.getBytes("UTF-8"));
		out.closeEntry();
		out.close ();
		bos.close();

//		encode to Base64 string
		String b64String = Base64.encodeBase64String(bos.toByteArray());
		Reader inputString = new StringReader(b64String);
		BufferedReader reqbuffer = new BufferedReader(inputString);
		
		when(request.getParameter("username")).thenReturn(ds.getUsername());
		when(request.getParameter("password")).thenReturn(ds.getUserPassword());
		when(request.getSession(true)).thenReturn(session);
		when(session.getAttribute("Referer")).thenReturn("/"+instance_name+"/MyCollaborations");
		when(request.getRequestURI()).thenReturn("/"+instance_name+"/LoginServlet");
//		when(ls.getServletContext()).thenReturn(sc);
//		when(sc.getRequestDispatcher(anyString).thenReturn(rd);
		
		when(config.getInitParameter("databasename")).thenReturn(ds.getDatabaseName());
		when(config.getInitParameter("InstanceName")).thenReturn(ds.getInstanceName());
		when(config.getInitParameter("user")).thenReturn(ds.getDbUserName());
		when(config.getInitParameter("password")).thenReturn(ds.getDbUserPassword());
		when(config.getInitParameter("server")).thenReturn(ds.getDbServer());
		when(config.getInitParameter("port")).thenReturn(ds.getDbPort());
		when(config.getInitParameter("databasetype")).thenReturn(ds.getDbType());
		when(config.getInitParameter("sqlpath")).thenReturn(ds.getSqlPath());
		when(config.getInitParameter("templatedir")).thenReturn("Z:\\Tomcat8.5_new\\webapps\\BAE_4_2\\templates");
		when(config.getInitParameter("sourcexml")).thenReturn("Z:\\Tomcat8.5_new\\webapps\\BAE_4_2\\plugins\\bwinvitation.xml");
		when(config.getInitParameter("targetxml")).thenReturn("Z:\\Tomcat8.5_new\\webapps\\BAE_4_2\\plugins\\bwinvitationTarget.xml");
		
		when(response.getOutputStream()).thenReturn(servletOut);
		LoginServlet servlet = new LoginServlet();
		servlet.init(config);  // use the servlet life-cycle method to inject the mock
		servlet.doPost(request, response);
		
		ArgumentCaptor<String> bufferCaptor = ArgumentCaptor.forClass(String.class);
		verify(response).sendRedirect(bufferCaptor.capture());
		String responseBody = bufferCaptor.getValue();	
		System.out.println("Resonse redirecting to :"+responseBody);
		
		AssertJUnit.assertNotNull(responseBody);
		AssertJUnit.assertEquals("/"+instance_name+"/MyCollaborations",responseBody);
    }
}
