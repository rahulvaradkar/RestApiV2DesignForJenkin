package servlets;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.sql.*;                  // JDBC package

import javax.sql.*;                 // extended JDBC package

import com.boardwalk.database.*;
import com.boardwalk.exception.BoardwalkException;
import com.boardwalk.user.UserManager;
import com.boardwalk.user.User;

import boardwalk.connection.ExcelAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.*;

public class BWExcelAuthenticationFilter
extends ExcelAuthenticationFilter
{
	private final static String Seperator = new Character((char)1).toString();
    private final static String ContentDelimeter = new Character((char)2).toString();
    
	public boolean authenticate(
			ServletRequest request, ServletResponse response, FilterChain chain)
	{
		boolean ok = false;
		int userId;
		String userName;
		String userPassword;
		String templateMode = null;
		String nhHierarchy = null;
		String manifestId = null;
		int memberId;
		int nhId;
		String nhName;
		Connection connection = null;
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;

		
		// pass through request if call to authentication service
		String rURI = req.getRequestURI();
		
		HttpSession hs = ((HttpServletRequest)request).getSession(false);
		if (hs != null && hs.getAttribute("userId") != null) 
		{
			System.out.println("Session is active. Forwarding request to service..."+hs.getId());
			try {
				chain.doFilter(request, response);//session is active
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (ServletException e) {
				
				e.printStackTrace();
			} 
		}
		else if(rURI.contains("/rest/") || rURI.contains("xlLogoutService") || rURI.contains("forgotPassword") || rURI.endsWith("jsp") || rURI.contains("logo-boardwalk.gif") || rURI.contains("scripts")   )// to bypass filter for jsp in weblogic deployment
		{
			try {
				chain.doFilter(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		else if (rURI.contains("httpt_vb_Login") || rURI.contains("LoginServlet"))
		{
			try {
				if (browserRequest)
				{
					userName = request.getParameter("username");
					userPassword = request.getParameter("password");	
				}
				else
				{
					String line = getRequestBuffer(request).toString();
					String wrkstr;
					StringTokenizer st = new StringTokenizer(line);
					wrkstr = st.nextToken(Seperator);
					userName = wrkstr;
					wrkstr = st.nextToken(Seperator);
					userPassword = wrkstr;
					//Added to get the Membership ID in case of Multiple Membership
					wrkstr = st.nextToken(Seperator);
					templateMode = wrkstr;

					if (!templateMode.equals("PasswordChange"))
					{
						if (st.hasMoreTokens())
						{
							wrkstr = st.nextToken(Seperator);
							nhHierarchy = wrkstr;
						}

						if (st.hasMoreTokens())
						{
							wrkstr = st.nextToken(Seperator);
							manifestId = wrkstr;
						}
					}
				}
				req.setAttribute("userName", userName);
				req.setAttribute("templateMode",templateMode);
				req.setAttribute("nhHierarchy", nhHierarchy);
				req.setAttribute("manifestId",manifestId);
				
				DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
				connection = databaseloader.getConnection();
				//Changes related to Login Enhancements for Password Complexity and User Authentication on 20170524 - START
				//userId = UserManager.authenticateUser(connection, userName, userPassword);
				if (browserRequest)
					userId = UserManager.authenticateUser(connection, userName, userPassword, false);
				else
				{
					if (templateMode.equals("PasswordChange"))
						userId = UserManager.authenticateUser(connection, userName, userPassword, true);
					else
						userId = UserManager.authenticateUser(connection, userName, userPassword, false);
				}
				//Changes related to Login Enhancements for Password Complexity and User Authentication on 20170524 - END
				if (userId > 0)
	            {
					System.out.println("BWExcelAuthenticationFilter::authenticate() : User is valid");
					chain.doFilter(request, response); // added by shirish 20150720
					ok = true;
				}
				else
				{
					System.out.println("BWExcelAuthenticationFilter::authenticate() : User is not valid");
					String Message = "";
					// block the request
					//Changes related to Login Enhancements for Password Complexity and User Authentication on 20170524 - START
					if (userId == -1)
					{
						if (browserRequest == false)
						{
							Message = " failure " + xlService.ContentDelimeter+ " Incorrect Password " ;
							commitResponseBuffer(Message, response);
						}
						else{
							request.setAttribute("com.boardwalk.exception.BoardwalkException", new BoardwalkException(11011));
							filterConfig.getServletContext().getRequestDispatcher("/jsp/admin/login.jsp").forward(request, response);
						}
					}
					else if(userId == -2)
					{
						
						if (browserRequest == false)
						{
							Message = " failure " + xlService.ContentDelimeter+ " User is Unavailable " ;
							commitResponseBuffer(Message, response);
						}
						else{
							request.setAttribute("com.boardwalk.exception.BoardwalkException", new BoardwalkException(11012));
							filterConfig.getServletContext().getRequestDispatcher("/jsp/admin/login.jsp").forward(request, response);
						}
					}
					
					else if(userId == -3)
					{
						if (browserRequest == false)
						{
							Message = " failure " + xlService.ContentDelimeter+ " User is Inactive " ;
							commitResponseBuffer(Message, response);
						}
						else{
							request.setAttribute("com.boardwalk.exception.BoardwalkException", new BoardwalkException(11013));
							filterConfig.getServletContext().getRequestDispatcher("/jsp/admin/login.jsp").forward(request, response);
						}	
					}
					
					else if(userId == -4)
					{
						if (browserRequest == false)
						{
							Message = " failure " + xlService.ContentDelimeter+ " Password Expired " ;
							commitResponseBuffer(Message, response);
						}
						else{
							request.setAttribute("com.boardwalk.exception.BoardwalkException", new BoardwalkException(11014));
							filterConfig.getServletContext().getRequestDispatcher("/jsp/admin/login.jsp").forward(request, response);
						}		
					}
					
					else if(userId == -5)
					{					
						if (browserRequest == false)
						{
							Message = " failure " + xlService.ContentDelimeter+ " Account Locked Permanently " ;
							commitResponseBuffer(Message, response);
						}
						else{
							request.setAttribute("com.boardwalk.exception.BoardwalkException", new BoardwalkException(11015));
							filterConfig.getServletContext().getRequestDispatcher("/jsp/admin/login.jsp").forward(request, response);
						}		
					}

					else if(userId == -6)
					{					
						if (browserRequest == false)
						{
							Message = " failure " + xlService.ContentDelimeter+ " Account Locked Temporarily " ;
							commitResponseBuffer(Message, response);
						}
						else{
							request.setAttribute("com.boardwalk.exception.BoardwalkException", new BoardwalkException(11016));
							filterConfig.getServletContext().getRequestDispatcher("/jsp/admin/login.jsp").forward(request, response);
						}		
					}	

					else if(userId == -7)
					{					
						if (browserRequest == false)
						{
							Message = " failure " + xlService.ContentDelimeter+ " Password Should be Changed " ;
							commitResponseBuffer(Message, response);
						}
						else{
							request.setAttribute("com.boardwalk.exception.BoardwalkException", new BoardwalkException(11017));
							filterConfig.getServletContext().getRequestDispatcher("/jsp/admin/login.jsp").forward(request, response);
						}		
					}	

					else
					{
						Message = " failure " + xlService.ContentDelimeter+ " Unknown Error occured. Please contact Administrator " ;
						commitResponseBuffer(Message, response);
					}
					//Changes related to Login Enhancements for Password Complexity and User Authentication on 20170524 - END
				}
				//chain.doFilter(request, response); commented by shirish 20150720
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				if (connection != null)
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		else if (rURI.contains("DisplayDocument") || rURI.contains("fileUploadService")|| rURI.contains("templates/"))
		{
			
			System.out.println("DisplayDocument fileUploadService");
			try {
				chain.doFilter(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// else redirect to appropriate authentication service
		else
		{
			System.out.println("BWExcelAuthenticationFilter::browserRequest : "+browserRequest); // TODO:REMOVE
			if (browserRequest)
			{
				System.out.println("BWExcelAuthenticationFilter: Sending to login page");
				try {
					//request.setAttribute("com.boardwalk.exception.BoardwalkException", new BoardwalkException(11004));
					filterConfig.getServletContext().getRequestDispatcher("/jsp/admin/login.jsp").forward(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return ok;
	}
}
