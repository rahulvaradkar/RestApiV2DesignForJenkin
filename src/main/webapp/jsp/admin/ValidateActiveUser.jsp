<%@ page import ="servlets.*"%>
<%@ page import ="boardwalk.common.*"%>
<%
	String emailAdd		= request.getParameter("emailAddress");
	//System.out.println("<<<<<<<email address>>>>>>"+emailAdd);
	int retValueIsActiveUser=0;
	boolean IsActiveUser = false ;
	try
	{
		if(!BoardwalkUtility.checkIfNullOrBlank(emailAdd))
		{
		IsActiveUser = BW_Users.isActiveUser(emailAdd);
		System.out.println("**************IsActiveUser " +IsActiveUser );
		}
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
		if(IsActiveUser)
		{
			retValueIsActiveUser =1;
		}
	
	%>
	<%=IsActiveUser +"^"+retValueIsActiveUser%>
		


