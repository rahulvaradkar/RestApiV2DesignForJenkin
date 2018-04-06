<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.collaboration.*,com.boardwalk.exception.*,com.boardwalk.user.*,com.boardwalk.database.*, java.io.*,java.sql.*" %>

<%
	request.setAttribute("heading", "BAE Server Login");
	boolean check_access = false;

	Connection connectionJsp = null;

	try
	{
		DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
		connectionJsp = databaseloader.getConnection();
		check_access = UserManager.isUserAccessPresent(connectionJsp);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
%>


<%@include file='/jsp/common/header.jsp' %>

<tr align=CENTER valign=CENTER class="body" >
<td align=CENTER valign=CENTER  class="body" width="110">
<img src="images/logo-boardwalk.gif" width="175" height="34"></td>
</tr>


<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


<tr>
<td>
<table width="600" class="body" >
  <tr class="body" >
    <td height="100%" class="body" >
      <div align="center"><br>
        <br>
	<form method="post" name="loginform" action="LoginServlet">
        <table  cellpadding=1 cellspacing=1 align="center" class="body"  >
        <tr bgcolor="#eeeeee" border="0" >
            <td align=LEFT valign=LEFT height="25" border="0"  ><B> Login: <B></td>
            <td align=LEFT  valign=LEFT border="0" >
              <input width ="300" type="TEXT" size=30 name="username">
            </td>
          </tr>

          <tr bgcolor="#cccccc" border="0" >
            <td align=LEFT valign=LEFT height="25" >
              <B>Password:</B>
              </td>
            <td align=LEFT valign=LEFT>
              <input type="PASSWORD" size=16 name="password">
              <input type="HIDDEN" name='action' value='login'>
              </td>
          </tr>

          <tr border="0" >
            <td align=LEFT valign=LEFT >
              <input type="SUBMIT" value="Login" name="go" onClick="makeLogin()">
            </td>
          </tr>
		<% if(check_access == false) {%>
          <tr border="0">
            <td colspan=2 valign=LEFT > 
            <a href="javascript:setActionAndSubmit('register')">
            New users click here to register
            </a>            
            </td>
          </tr>
         <%}%>   
        </table>
        <%@include file='/jsp/common/commonparameters.jsp' %> </form>
        <br>
        <br>
</tr>
</td>

<%  
	try
	{
		System.out.println(" in Try JSP block");
		connectionJsp.close();
		connectionJsp = null;
	}
	catch ( SQLException sql )
	{
		sql.printStackTrace();
	}
%>
<%@include file='/jsp/common/footer.jsp' %>

