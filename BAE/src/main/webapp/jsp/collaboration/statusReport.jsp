<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.collaboration.*, com.boardwalk.whiteboard.*, com.boardwalk.table.*,java.io.*" %>

<%
Vector collabList = (Vector)request.getAttribute("collabList");
Hashtable collabTables = (Hashtable)request.getAttribute("collabTables");
Hashtable collabActivitySummaries = (Hashtable)request.getAttribute("collabActivitySummaries");
String nhIdName = (String)request.getAttribute("nhIdName");
//if ( collabList != null )
//{

%>
<script language="javascript">
<%@ include file="/jscript/dateutil.js" %>
</script>
<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<tr>
  <td>

        <!--start main page table-->
        <table  align="center" border="0" cellspacing="0" cellpadding="0"  class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->
		<table  border="0" cellspacing="1" cellpadding="2">
		 <tr align="left"  >
		   <td colspan="7" >
		   <%
		   if ( nhIdName != null &&! nhIdName.trim().equals("") )
		   {
		   %>
			<span class="page-heading"> Status report for Collaborations in Neighborhood <u><i><%=nhIdName%> </i></u></span>
			<br>
			<span class="small-text"> as of </span>
			<span class="small-text" id="reportDate"></span>
<script language="javascript">
var d = new Date()
reportDate.innerText = formatDateTime(d,"NNN dd, yyyy hh:mm:ssa")
</script>
		   <%
		   }
		   %>
		  </td>
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
		  <td class="body"> <b>Owner</b> </td>
		  <td class="body"> <b>Created On</b> </td>
		  <td class="body"> <b>Last Update By</b> </td>
		  <td class="body"> <b>Last Update On</b> </td>
		  <td class="body"> <b>Comments</b> </td>
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
				    TableUpdateSummary tus = (TableUpdateSummary)activitySummary.get(new Integer(ttn.getId()));
				    System.out.println("tus="+ tus);
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
				<td class="body"><%=tus!=null?tus.getTableCreatedBy():"N/A"%> </td>
				<td class="body" id="cre<%=ttn.getId()%>"> </td>
				<%
					if (tus != null)
					{
				%>
<script language="javascript">
var d = new Date(<%=tus.getTableCreatedOn()%>)
cre<%=ttn.getId()%>.innerText = formatDateTime(d,"NNN dd, yyyy hh:mm:ssa")
</script>
				<%
					}
					else
					{
				%>
				N/A
				<%
					}
				%>
				<td class="body"><%=tus!=null?tus.getLastUpdatedBy():"N/A"%> </td>
				<td class="body" id="upd<%=ttn.getId()%>"> </td>
				<%
					if (tus != null)
					{
				%>
<script language="javascript">
var d = new Date(<%=tus.getLastUpdatedOn()%>)
upd<%=ttn.getId()%>.innerText = formatDateTime(d,"NNN dd, yyyy hh:mm:ssa")
</script>
				<%
					}
					else
					{
				%>
				N/A
				<%
					}
				%>
				<td class="body"><%=tus!=null?tus.getLastUpdateComment():"N/A"%> </td>
			    </tr>
		<%
				} // tables

			 } // whiteboards
		 %>


		 <%

		     } // while there are more collaborations
		  %>

		</table>
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
