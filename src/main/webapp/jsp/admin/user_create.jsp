<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, collaboration.*,java.io.*, java.util.*" %> 

<%

String username = (String)req.getAttribute("username");
String fname = (String)req.getAttribute("fname");
String lname = (String)req.getAttribute("lname");

if ( username == null )
username = "";

if ( fname == null )
fname = "";

if ( lname == null )
lname = "";

			

%>
<html>
<head>
<title>Administration > Users</title>
<script language="javascript">
    function setActionAndSubmit(action) {
	    document.forms[0].action.value = action;
	    document.forms[0].submit();
    }
    
    
   
</script>

<%@include file='/jsp/common/error_message.jsp' %>

</head>

<form method="post" action="BW_Users">
<body bgcolor="#FFFFFF">
<%@include file='/jsp/common/error_message.jsp' %>
<table width="773" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td rowspan="3" width="233"><img src="images/masthead/titlebar-top-left.gif" width="233" height="79"></td>
    <td height="59" background="images/masthead/titlebar-top-center-table.gif"> 
      <div align="center"><font face="Verdana,Arial, Helvetica, sans-serif" size="5" color="#663300">Administration</font></div>
    </td>
    <td rowspan="3" width="82"><img src="images/masthead/titlebar-top-right.gif" width="82" height="79"></td>
  </tr>
  <tr>
    <td bgcolor="ffffff" height="2" width="458"><img src="images/masthead/clear.gif" width="1" height="2"></td>
  </tr>
  <tr> 
   <td bgcolor="#702d07" height="18">
      <div align="center"><a href="index.html"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif" size="2"><b>Administration main</b></font></a><b><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif" size="2"> | </font><font color="#FFcc99" face="Verdana,Arial, Helvetica, sans-serif" size="2"><b>Users</b></font><b><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif" size="2"> | </font><a href="neighborhoods.html"><font color="#ffffff" face="Verdana,Arial, Helvetica, sans-serif" size="2"><b>Neighborhoods</b></font><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif" size="2"> | </font></a><a href="members.html"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif" size="2"><b>Members</b></font></a></b></b></div>
    </td>
  <!--
    <td bgcolor="#702d07" height="18">
      <div align="center"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif"><b><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif"><b><font size="2"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif"><b><font size="2"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif"><b><font color="#FFCC99">Users&nbsp;</font><font size="2">|&nbsp;</font></b></font></font><font size="2"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif"><b><font size="2"><a href="neighborhoods.html"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif"><b>Neighborhoods</b></font></a></font></b></font> 
        </font></b></font></font></b></font><font size="2"> | <font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif"><b><font size="2"> 
        <a href="members.html"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif"><b>Members</b></font></a></font></b></font> 
        </font></b></font></div>
    </td>-->
  </tr>
<tr>
<td colspan="3" width="773" height="2" bgcolr="#ffffff"><img src="images/masthead/clear.gif" width="1" height="2"></td>
</tr>

<tr>
<td colspan="3" width="773" height="2" bgcolor="#702do0"><img src="images/masthead/clear.gif" width="773" height="1"></td>
</tr>

</table>
<br>
<table width="773" border="0">
  <tr> 
    <td valign="top"> 
      <div align="center"><font size="3" face="Verdana,Arial, Helvetica, sans-serif" color="#660000"><b>Users</b></font><br>
        <br>
        <br>
        <table width="500" border="0" cellspacing="2" cellpadding="2" align="center">
          <tr valign="top"> 
            <td><font size="2" face="Verdana,Arial, Helvetica, sans-serif" color="#000000"><b> 
              Create User</b></font></td>
          </tr>
          <tr valign="top"> 
            <td> 
              <table width="500" border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#663300" valign="top"> 
                  <td bgcolor="#663300" colspan="2"><b><font face="Verdana,Arial, Helvetica, sans-serif" size="2" color="#FFFFFF">Create 
                    User</font></b></td>
                </tr>
                <tr bgcolor="#fddeb9" valign="top"> 
                  <td valign="top"><font size="2" color="#000000" face="Verdana,Arial, Helvetica, sans-serif">Email 
                    Address </font></td>
                  <td > <font size="2" color="#000000" face="Verdana,Arial, Helvetica, sans-serif"> 
                    <input type="text" name="userAddr">
                    <br>
                    </font></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr bgcolor="e7e7e7"> 
            <td> 
              <div align="center"><font face="Verdana,Arial, Helvetica, sans-serif" size="2" color="#000000"><b> 
                <a href="javascript:setActionAndSubmit('commitUser')">OK </a> | 
                <a href="javascript:setActionAndSubmit('')"> Cancel </a> 
		</b></font></div>
            </td>
          </tr>
        </table>
        <br>
        <br>
        <br>
      </div>
    </td>
  </tr>
  <tr> 
    <td height="20" bgcolor="#000000" valign="top"> 
      <div align="center"><font color="#FFFFFF" face="Verdana,Arial, Helvetica, sans-serif" size="2"><b>Powered 
        by Boardwalk</b></font></div>
    </td>
  </tr>
</table>
</body>
<input type="hidden" name="action" value="">
<%@include file='/jsp/common/commonparameters.jsp' %> </form>
</html>
