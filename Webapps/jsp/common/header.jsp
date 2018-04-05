<%@ page import ="java.util.*,com.boardwalk.util.*" %>
<%
String ShowinExcel = (String) request.getAttribute("ShowinExcel");


if(ShowinExcel != null )
{
	if(ShowinExcel.equals("false"))
	{
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	}
}
else
{
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
}
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server



String title = new String();
String heading = new String();

title = (String)request.getAttribute("title");
if (title == null){
    title = "BOARDWALK";
}
heading = (String)request.getAttribute("heading");
if (heading == null){
    heading = "";
}


String userName = (String)session.getAttribute("userEmailAddress");
// Hashtable memberships =  (Hashtable)session.getAttribute("neighborhoods");

BoardwalkSession bws = (BoardwalkSession) session.getAttribute("bwSession");

System.out.println("bwSession  %%%%%%%%%%%%%%%%%%%" + bws  );
Hashtable memberships = null;

if ( bws != null )
{
	memberships =bws.memberIdToMember;
	System.out.println("memberships size %%%%%%%%%%%%%%%%%%%" + memberships.size()  );


}




%>





<html>
<head>
<title><%=title%></title>
<script language="javascript">
<%@ include file="/jscript/collab.js" %>


</script>

<META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=UTF-8">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="EXPIRES" CONTENT="-1">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">


</head>

    <LINK REL=STYLESHEET TYPE="text/css"
      HREF="css/stylesheet.css" TITLE="stylesheet">


<body bgcolor="#FFFFFF" marginheight="0" marginwidth="10" leftmargin="10" topmargin="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="body" align="top">
<tr>
	<td>