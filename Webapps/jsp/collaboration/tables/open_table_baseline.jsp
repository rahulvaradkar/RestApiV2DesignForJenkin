<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*" %> 

<%
int tableId = ((Integer)request.getAttribute("TableId")).intValue();
int baselineId= ((Integer)request.getAttribute("baselineId")).intValue();

String tableName = (String)request.getAttribute("TableName");

TableContents tbc = (TableContents)request.getAttribute("TableContents");

TableInfo tbi = (TableInfo)request.getAttribute("TableInfo");

System.out.println("baselineid is " + baselineId);

Vector columnNames = tbc.getColumnNames();

Hashtable cellsByRowId = tbc.getCellsByRowId();

Vector   rowIds = tbc.getRowIds();
    Iterator cols1 = columnNames.iterator();
    while (cols1.hasNext()){
	String head = (String)cols1.next();
    }// while more columns

String ViewPreference = "LATEST";



System.out.println(" Number of column Names " + columnNames.size() ) ;


%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


</tr> 
    </td>


<tr> 
    <td>
     
     <!--start main page table-->
     <form method="post" action="MyTables" >
     
<%@include file="/jsp/collaboration/tables/read_only_table.jsp"%>

      <br>      
    </td>
  </tr>
 
 <br>
 
<%@include file="/jsp/common/footer.jsp"%>

  </td>
  </tr>
</table>
<br>

     		<input type="hidden" name="tableId" value="<%=tableId%>">
	       <input type="hidden" name="action" value="copyTable">	       
	       
	       <input type="hidden" name="ViewPreference" value="<%=tbi.getTableDefaultViewPreference()%>">	       
	       <input type="hidden" name="wbid" value="<%=tbi.getWhiteboardId()%>" >
	       <input type="hidden" name="tableName" value="<%=tbi.getTableName()%>" >
	       <input type="hidden" name="tableDescription" value="<%=tbi.getTablePurpose()%>" >	       
	       
	       <input type="hidden" name="baselineId" value="<%=baselineId%>" >
	       


    <%@include file='/jsp/common/commonparameters.jsp' %> </form>
</body>
</html>




