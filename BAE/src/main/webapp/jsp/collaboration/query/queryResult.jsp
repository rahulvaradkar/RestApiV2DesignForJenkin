<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.query.*,com.boardwalk.table.*,com.boardwalk.collaboration.*, com.boardwalk.table.Baseline,java.io.*" %>

<%

QueryResultSet queryResultSet = (QueryResultSet)request.getAttribute("queryResultSet");
System.out.println("coulmn Name " + (String)queryResultSet.getColumnNames().elementAt(0) );
Vector columnNames =queryResultSet.getColumnNames();
Vector rows = (Vector)queryResultSet.getRows();
request.setAttribute("heading", "Query Results");
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>




<tr>
    <td>
        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0"  class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->

		<table  border="0" cellspacing="0" cellpadding="0" valign="top">
                    <tr valign="top" bgcolor="#FFCC66">
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>

                    <tr valign="top">
                    <br>
                    <br>
                        <td class="body"> <%=queryResultSet.getDescription()%>
                            <br>

                            <br>
                        <table width="450" border="0" cellspacing="2" cellpadding="2" class="body">
                                <form name="BaselineListForm" method="get" action="MyCollaborations">

                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20>

       		<%
		Iterator j = columnNames.iterator();
		while (j.hasNext())
		{
		    String columnName = (String)j.next();
		    %>
		    <td body="class" ><%=columnName%></td>
		    <%
		}
		%>
		<td body="class">Formula</td>
		<td body="class" >UserName</td>
		<td body="class" >Date</td>
		<td body="class" >Comments</td>
		</tr>

		<%


		for ( int i = 0 ; i < rows.size(); i++ )
		{
		Vector cellsInARow = (Vector)rows.elementAt(i);

		%>
		<tr  >
		<%


		    for ( int c = 0 ;c < cellsInARow.size(); c++ )
		    {
			VersionedCell vcell = (VersionedCell)cellsInARow.elementAt(c);
			//System.out.println( "cell value " + vcell.getValueAsString() );
			String value = vcell.getValueAsString();
			String member  = vcell.getTransaction().getCreatedByUserAddress();
			String Time = vcell.getTransaction().getCreatedOn();
			String formula = vcell.getFormula();
			System.out.println("formula="+formula);
			if (formula == null)
			{
			  formula = "";
			}
			String comments = vcell.getTransaction().getComment();

		%>
		<td class="body" ><%=value%> </td>
		<td class="body" ><%=formula%> </td>
		<td class="body" ><%=member%> </td>
		<td class="body" ><%=Time%> </td>
		<td class="body" ><%=comments%> </td>
		 </tr>
		<%
			 } // while there are more cells
		}
		%>


             <%@include file='/jsp/common/commonparameters.jsp' %> </form>
        </table>
        <!--end table for table-->
                    <td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td>

        </tr>

     </table>
              <!--end table for main page table 2 -->


            </td>




                   </tr>
        </table>

        <!--start main page table -->

    <br>
    </td>
  </tr>

<br>
<%@include file='/jsp/common/footer.jsp' %>




