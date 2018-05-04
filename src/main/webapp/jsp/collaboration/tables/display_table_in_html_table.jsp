<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.query.*,com.boardwalk.table.*,com.boardwalk.collaboration.*, com.boardwalk.table.Baseline,java.io.*" %>

<%

int tableId = ((Integer)request.getAttribute("TableId")).intValue();
String tableName = (String)request.getAttribute("TableName");
TableContents tbc = (TableContents)request.getAttribute("TableContents");
TableInfo tbi = (TableInfo)request.getAttribute("TableInfo");
String ViewPreference = (String)request.getParameter("ViewPreference");

Vector columnNames =tbc.getColumnNames();
Vector rowIds = (Vector)tbc.getRowIds();
Hashtable cellsByRowId  = tbc.getCellsByRowId();
System.out.println("no of rows " + rowIds.size());

%>
<html>
<body>

 <LINK REL=STYLESHEET TYPE="text/css" 
      HREF="css/stylesheet.css" TITLE="stylesheet">
<%@include file="/jsp/collaboration/tables/read_only_table.jsp"%>
</body>
</html>