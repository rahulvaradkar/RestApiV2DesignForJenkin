package test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
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

import servlets.forgotPassword;
import servlets.xlLoginService;

public class forgotPasswordServletTest
{
	
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
	ServletConfig sg;
	public final static String Seperator = new Character((char)1).toString();
    @BeforeClass
    protected void setUp() throws Exception 
    {
    	MockitoAnnotations.initMocks(this);
    	//servletOut = mock(ServletOutputStream.class); 
    }

    @Test
    public void test() throws Exception {
    	
 
    	DataSet ds=new DataSet();	
				
		String buffer="jaydeepbobade@gmail.com"+Seperator;//to check user is active or not	 
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
		when(request.getReader()).thenReturn(reqbuffer);
		when(sg.getInitParameter("smptserver")).thenReturn("secure.emailsrvr.com");
		when(sg.getInitParameter("smtpport")).thenReturn("80");
		when(sg.getInitParameter("username")).thenReturn("password@boardwalktech.com");
		when(sg.getInitParameter("password")).thenReturn("boardwalk");
		when(response.getOutputStream()).thenReturn(servletOut);
		forgotPassword servlet=new forgotPassword();
		servlet.init(sg);
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
		AssertJUnit.assertEquals(2, res.length);
		AssertJUnit.assertEquals("Success",res[0]);
 	}
}
