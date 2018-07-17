<%@ page import ="com.boardwalk.table.*,java.util.*,java.sql.*,com.boardwalk.database.*"%>

<%
	String lsTableId	= request.getParameter("tableid");
	String startDate	= request.getParameter("startdate");
	String endDate		= request.getParameter("enddate");
	String lsDropDownStr = "";
	try
	{
		lsDropDownStr = TableManager.getBaseLineForTableId(Integer.parseInt(lsTableId),startDate,endDate);	
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
%><%=lsDropDownStr%>

