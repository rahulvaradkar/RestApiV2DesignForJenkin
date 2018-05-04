<%@ page isThreadSafe="false" %>
<%@ page  import ="java.util.*,com.boardwalk.collaboration.*, com.boardwalk.whiteboard.*, com.boardwalk.table.*,java.io.*" %>

<%
Vector collabList = (Vector)request.getAttribute("collabList");
Hashtable collabTables = (Hashtable)request.getAttribute("collabTables");
Hashtable collabActivitySummaries = (Hashtable)request.getAttribute("collabActivitySummaries");
String nhIdName = (String)request.getAttribute("nhIdName");

long startDate = ((Long)request.getAttribute("startDate")).longValue();
long endDate = ((Long)request.getAttribute("endDate")).longValue();

%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<script language="javascript">
<%@ include file="/jscript/dateutil.js" %>
</script>
<tr>
  <td>

        <!--start main page table-->
        <table  align="center" border="0" cellspacing="0" cellpadding="0"  class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->
		<table  border="0" cellspacing="1" cellpadding="2">

		 <tr>
		   <td colspan="7" align="left">
		   <%
		   if ( nhIdName != null &&! nhIdName.trim().equals("") )
		   {
		   %>
			<span class="page-heading"> Activity report for Collaborations in Neighborhood <u><i><%=nhIdName%> </i></u> </span>
			<br>
			<span class="small-text"> from </b>
			<span class="small-text" id="startDate"></span>
			&nbsp<span class="small-text">to </b>
			<span class="small-text" id="endDate"></span>
		   <%
		   }
		   %>
		  </td>
		  <td colspan="3" align="right">
		  	 <span class="small-text" id="reportDate"></span>
		  </td>
<script language="javascript">
	var today = new Date()
	var d1 = new Date(<%=startDate%>)
	var d2 = new Date(<%=endDate%>)
	//alert(formatDateTime(new Date(<%=startDate%>,"MMM dd, yyyy hh:mm:ssa")
	startDate.innerText = formatDateTime(d1,"MMM dd, yyyy hh:mm:ssa")
	endDate.innerText = formatDateTime(d2,"MMM dd, yyyy hh:mm:ssa")
	reportDate.innerText = formatDateTime(today, "MMM dd, yyyy hh:mm:ssa")
</script>
		 </tr>

		 <tr>
		   <td colspan="7" height="15">
		   </td>
		 </tr>

		<!--start table for table-->
		<tr bgcolor="gray" style='color:white;' align="left" >
		  <td class="body"> <b>Collaboration</b> </td>
		  <td class="body"> <b>Whiteboard</b> </td>
		  <td class="body"> <b>Table</b> </td>
		  <td class="body"> <b>All Updates</b> </td>
		  <td class="body"> <b>Rows Added</b> </td>
		  <td class="body"> <b>Rows Deleted</b> </td>
		  <td class="body"> <b>Columns Added</b> </td>
		  <td class="body"> <b>Columns Deleted</b> </td>
		  <td class="body"> <b>Cells Updated</b> </td>
		  <td class="body"> <b>Formulae Changed</b> </td>
		</tr>


		<%

		    Iterator i = collabList.iterator();
		    boolean grey = false;

		    while (i.hasNext()) {
			    CollaborationTreeNode  c = (CollaborationTreeNode)i.next();
			    int id = c.getId();
			    String name = c.getName();
			    String desc = c.getPurpose();
			    String rowColorCode = "#eeeeee";
			    if ( grey == true )
			    {
			      rowColorCode = "silver";
			      grey = false;
			    }
			    else
			    {
			      grey = true;
			    }
		 %>

		<%

			    Vector wbTables = null;
			    wbTables = (Vector)collabTables.get(new Integer(id));
			    c = (CollaborationTreeNode)wbTables.elementAt(0);
			    Vector Whiteboards = (Vector) c.getWhiteboards();
			    Iterator wIter = Whiteboards.iterator();
			    WhiteboardTreeNode wbt = null;
			    TableTreeNode ttn = null;
			    Hashtable activitySummary = (Hashtable)collabActivitySummaries.get(new Integer(id));
			    System.out.println("activitySummary="+ activitySummary);
			    while (wIter.hasNext())
			    {

				wbt = (WhiteboardTreeNode)wIter.next();
				int wbid = wbt.getId();
				String wbname =wbt.getName();
		 %>


		 <%
				Vector tables = (Vector)wbt.getTables();
				Iterator tIter = tables.iterator();
				while (tIter.hasNext())
				{
				    ttn = (TableTreeNode)tIter.next();
				    String tableName = ttn.getName();
				    String tableDescr = ttn.getPurpose();
				    System.out.println("ttn.getId()=" + ttn.getId());
				    TableActivitySummary tas = (TableActivitySummary)activitySummary.get(new Integer(ttn.getId()));
		%>
			    <tr  bgcolor="<%=rowColorCode%>" >
				<td class="body">
				<a href="MyCollaborations?collabId=<%=id%>&action=editCollab"><%=c.getName()%></a>
				</td>
				<td class="body">
				<a href="Whiteboard?collabId=<%=id%>&action=edit&wbid=<%=wbid%>"><%=wbname%> </td></a>
				 <td class="body">
				<a href="MyTables?tableId=<%=ttn.getId()%>&wbid=<%=wbid%>&ViewPreference=<%=ttn.getDefaultViewPreference()%>&action=editTable"><%=tableName%></a>
				 </td>

			<%
					if ( tas == null )
					{
			%>
						<td class="body">0</td>
						<td class="body">0</td>
						<td class="body">0</td>
						<td class="body">0</td>
						<td class="body">0</td>
						<td class="body">0</td>
						<td class="body">0</td>
						</tr>

			<%
					}
					else
					{
			%>
						<td class="body">
						<a href="MyTables?tableId=<%=ttn.getId()%>&wbid=<%=wbid%>&ViewPreference=<%=ttn.getDefaultViewPreference()%>&action=getTransactions&tableName=<%=tableName%>&startDate=<%=startDate%>&endDate=<%=endDate%>"><%=tas.numUpdates()%>
						</td>
						<td class="body"><%=tas.numRowsAdded()%> </td>
						<td class="body"><%=tas.numRowsDeleted()%> </td>
						<td class="body"><%=tas.numColumnsAdded()%> </td>
						<td class="body"><%=tas.numColumnsDeleted()%> </td>
						<td class="body"><%=tas.numCellsUpdated()%> </td>
						<td class="body"><%=tas.numFormulaUpdated()%> </td>
						</tr>
				<%
					 }
				} // tables

			    } // whiteboards
		 %>


		 <%

		     } // while there are more collaborations
		  %>

		</table>
        </tr>
     </table>
	</td>
  </tr>
</table>


    <br>
    </td>

  </tr>

<br>
<%@include file='/jsp/common/footer.jsp' %>
