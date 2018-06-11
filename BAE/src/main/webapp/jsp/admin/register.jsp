<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.collaboration.*, com.boardwalk.user.*, java.io.*,com.boardwalk.database.*,java.sql.*" %>

<%
request.setAttribute("heading", "User Registration");
String username =(String) request.getAttribute("username");
String fname = (String)request.getAttribute("fname");
String lname =(String) request.getAttribute("lname");
int selNhid = -1;
boolean check_access = false;

Connection	connectionJsp = null;

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
if(check_access)
{
	selNhid = Integer.parseInt(request.getParameter("selNhid"));
}

if ( username == null )
{
	username = "";
}

if ( fname == null )
{
	fname = "";
}

if ( lname == null )
{
	lname = "";
}


%>
<script>

function CommitUser(action)
{
	var FormObj = document.registration_form;

	if(FormObj.username.value == '' || FormObj.password.value == '')
	{
		alert("Please add a valid UserName and Password");
		if(FormObj.username.value == '' && FormObj.password.value == '')
		{
			FormObj.username.focus();
			return;
		}
		else if(FormObj.username.value != '' && FormObj.password.value == '')
		{
			FormObj.password.focus();
			return;
		}
	}
	else
	{
		FormObj.action.value = action;
		FormObj.submit();
	}
}

function Cancel(action)
{
		document.registration_form.action.value = action;
	    document.registration_form.submit();
}

function onloadAction()
{
	document.registration_form.fname.focus();
}

</script>

<%@include file='/jsp/common/header.jsp' %>


<tr  class="body" >
<td  class="body" width="110"><img src="images/logo-boardwalk.gif" width="175" height="34"></td>
</tr>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<tr>
<td>
<table width="1000" class="body" >
  <tr class="body" >
    <td height="100%" class="body" >
      <div ><br>
        <br>
	<BODY onload="onloadAction()">
	<form method="post" name="registration_form" action="BW_Users">
		<input type="hidden" name="checkAccess" value="<%=check_access%>">
        <table width=500  cellpadding=1 cellspacing=1 class="body"  >
        <tr valign=TOP bgcolor="#fddeb9">
            <td align=LEFT colspan=2 height=20>
              <left><font face="verdana, arial, geneva" size="2" >
                <b>User Profile</b></font></left>
            </td>
          </tr>

        <tr bgcolor="#eeeeee" >
            <td align=LEFT width="120" valign=BOTTOM height="25">
            First Name</font></td>
            <td align=LEFT valign=BOTTOM>
              <input type="TEXT" size=16 name="fname"  value="<%=fname%>" >
            </td>
          </tr>
          <tr bgcolor="white" >
            <td align=LEFT valign=top height="25">
              <br>Last Name<br></td>
            <td align=LEFT valign=BOTTOM>
              <input type="TEXT" size=16 name="lname" value="<%=lname%>" >
              </td>
          </tr>
          <tr bgcolor="#fddeb9" valign=TOP >
            <td align=LEFT colspan=2 height=20>
              <left><font face="verdana, arial, geneva" size="2" >
                <b>User Identity</b></font></left>
            </td>
          </tr>
          <tr bgcolor="#eeeeee" >
            <td align=LEFT width="250" valign=BOTTOM height="25">
              Login Name (Email)</td>
            <td align=LEFT valign=BOTTOM>
              <input type="TEXT" size=30 name="username" value="<%=username%>" >
            </td>
          </tr>
          <tr bgcolor="white">
            <td align=LEFT valign=top height="25">
                <br>Password<br>
            </td>
            <td align=LEFT valign=BOTTOM> <font face="verdana, arial, geneva" size=2 >
              <input type="PASSWORD" size=16 name="password">
              </font>
            </td>
          </tr>
          <tr>
		  <%
			 if(check_access == true)
			{%>
				<td align=LEFT colspan=2 height=20><br>
              <input type="BUTTON" value="OK" onClick = "CommitUser('commitUser')"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          
              <input type="BUTTON" value="Cancel" onClick = "Cancel('cancel')" >
            </td>
			<%
			}
			  else
			  {
		  %>
            <td align=LEFT colspan=2 height=20>
              <input type="BUTTON" value="Add Me" onClick = "CommitUser('commitUser')">
              <input type="HIDDEN" name="action" value="commitUser">
            </td>
			<%}%>
          </tr>
        </table>
		<input type="hidden" name="action" value="">
		<input type="hidden" name="selNhid" value="<%=selNhid%>">
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
