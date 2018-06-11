<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.database.Transaction, com.boardwalk.collaboration.*, com.boardwalk.whiteboard.*, com.boardwalk.table.*,java.io.*,java.text.*" %>

<%
try
{

Hashtable tList		= (Hashtable)request.getAttribute("cellVersions");
String[] columnIds	= (String[])request.getAttribute("columnIds");

Vector lvColumnHeader	= (Vector)request.getAttribute("columnHeader");
ArrayList laReportTable	= (ArrayList)request.getAttribute("table");

String tableId		= request.getParameter("tableId");
String tableName	= request.getParameter("tableName");
String rowIds		= request.getParameter("rowIds");
String columnids	= request.getParameter("columnids");
String baseline		= request.getParameter("baseline");
String period		= request.getParameter("period");
long startDate		= ((Long)request.getAttribute("startDate")).longValue();
long endDate		= ((Long)request.getAttribute("endDate")).longValue();


//System.out.println("lvColumnHeader "+lvColumnHeader.size());

String execlDump = request.getParameter("execlDump");
System.out.println("requested format"+execlDump);
if ((execlDump != null) && (execlDump.equals("excel"))) {

// Content type based on parameter.
   response.setContentType("application/vnd.ms-excel");

	//response.setContentType("application/xlet");
	response.setHeader("Content-Disposition", "filename=Report.xls");
}


%>
<% if ((execlDump != null) && (!execlDump.equals("excel"))) 
	{
%>
<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<script language="javascript">
<%@ include file="/jscript/dateutil.js" %>

function openInExcel()
{
	var action = 'getRowVersions';
	var args = new Object();
	
		  var url = location.protocol + "//" +
			  location.hostname + ":"+
			  location.port +
			  location.pathname + "?" +
			  "action=" + action + "&" +
			  "period=" + document.all.period.value + "&" +
			  "startDate=" + document.all.startDate.value + "&" +
			  "endDate=" + document.all.endDate.value + "&" +
			  "tableId=" + document.all.tableId.value + "&" +
			  "rowIds=" + document.all.rowIds.value + "&" +
			  "baseline=" + document.all.baseline.value + "&" +
			  "execlDump=excel&" +
			  "columnids=" + document.all.columnids.value ;
	//var query = showModalDialog(url, args, "dialogHeight:25;dialogWidth:25");
	 window.open(url,'','height=300,width=450,left=0,top=0,menubar=yes,status=yes,toolbar=yes,scrollbars=yes,location=yes,directories=yes,resizable=yes');

}

</script>
<tr>
  <td>
		<form name="f1" method="get" action="MyTables"  >
        <!--start main page table-->
        <table   border="0" cellspacing="0" cellpadding="5"  class="body">

          <tr>
            <td valign="top">
<!-- ******************** Header details ******************** -->
		<table  border="0" cellspacing="1" cellpadding="2">
		 <tr>
		   <td colspan="4" align="left">
			<b class="page-heading"> List of Updates on selected row

			</b>
			<br>
			<span class="small-text"> from </b>
			<span class="small-text" id="startDate"></span>
			&nbsp<span class="small-text">to </b>
			<span class="small-text" id="endDate"></span>
		  </td>
		  <td colspan="1" align="right">
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
		   <td colspan="5" height="2">
		   </td>
		 </tr>
		<tr><td>
		<div class="box"><a href="javascript:openInExcel()"><img src="images/icon-excel.gif" width="16" height="16"></a></td></div>
		</tr>
		 </table>
<%
	}		
%>
<!-- ******************** start table for table ******************** -->
	<table class="body" width="100%" cellspacing = 1 cellpadding = 0>
		<tr bgcolor="gray" style='color:white;' align="left" >
		<%
		int liColumnCount	= columnIds.length;
		if(liColumnCount < 1)
			liColumnCount = lvColumnHeader.size(); 
		System.out.println("######## No. of columns in report "+liColumnCount);
		String rowColorCode = "#eeeeee";
		int DummyTxid		= 0;

		Vector RowCells = new Vector();
		Vector ModRowCells = new Vector();
		// Printing the table header
		int liColumnIdIndex = 0;
		for(int i = 0; i < lvColumnHeader.size(); i++)
		{
			Column bc = (Column)lvColumnHeader.get(i);
			if(liColumnIdIndex < columnIds.length  && Integer.parseInt(columnIds[liColumnIdIndex]) == bc.getId())
			{
		%>
				<td class="body"> <b><%=bc.getColumnName()%></b> </td>
		<%
				liColumnIdIndex++;
			}
			else
			{
				if(columnIds.length == 0)
				{
		%>
				<td class="body"> <b><%=bc.getColumnName()%></b> </td>
		<%
				}
			}
		}
		%>
		<td class="body"> <b>Modified By</b> </td>
		<td class="body"> <b>Modified Date</b> </td>
		<td class="body"> <b>Comments</b> </td>
		<%
			if(!baseline.equals("-1"))
			{
		%>
				<td class="body"> <b>Baseline</b> </td>
				<td class="body"> <b>Baseline Description</b> </td>
		<%
			}
		%>

		</tr>

		<%
			//System.out.println("######## No. of rows in report "+laReportTable.size());
			VersionedCell printCell = null;
			int TotalRows = laReportTable.size();
			Transaction trans1 = null;
			int cellIndex = 0;
			
			for(int i = 0; i < TotalRows; i++)
			{
				ArrayList laReportRowsCells = (ArrayList)laReportTable.get(i);
				int rows = laReportRowsCells.size()/liColumnCount;
				int index = laReportRowsCells.size();
				if(i%2 == 0)
					rowColorCode = "#cee7ff";
				else
					rowColorCode = "#ffdfef";
				//System.out.println("######## No. of cells in a row in report "+index);
				for(int j = 0; j < rows; j++)
				{			
					//System.out.println("row "+j+"\t");
					
					%>
						<tr bgcolor="<%=rowColorCode%>">
					<%
					String lsModifiedBy = "";
					String lsModifiedOn = "";
					String lsComments = "";
					String lsBaseline = "";
					String lsBaselineDesc = "";
					index = index - liColumnCount;
					for(int col = 0; col < liColumnCount; col++)
					{
						//System.out.print("index+col "+(index+col)+"\t");
						
						printCell = (VersionedCell) laReportRowsCells.get(index+col);
						boolean formulaModified = printCell.getformulaModified();	
						trans1			= printCell.getTransaction();	
						int liRowNum	= printCell.getRowId();
						String styleStart	= "";						
						String styleEnd	= "";	
						String color = "";
						String colorFormula = "";
						if(!printCell.getDescription().equals(""))
						{		
							color = "bgcolor=\"#e9cdcd\"";
							colorFormula = "bgcolor=\"#F9FF9B\"";

							//styleStart	= "<b>";						
							//styleEnd	= "</b>";						
							lsModifiedBy	= trans1.getCreatedByUserAddress();			
							SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ssa");
							lsModifiedOn	= df.format(new java.sql.Timestamp(trans1.getCreatedOnTime()));
							lsComments = trans1.getComment();
							if(lsComments == null)
								lsComments = "";
							lsBaseline = printCell.getTableName();
							lsBaselineDesc = printCell.getbaselineDesc();
						}
						%>
						<%if(!formulaModified) {%>

						<td height="15" class="body" <%=color%> id="cell_<%=cellIndex%>" ><%=styleStart%><%=printCell.getValueAsString() %><%=styleEnd%></td>

						<%
						}
						else 
							{
							
						%>
								
						<td height="15" class="body" <%=colorFormula%> id="cell_<%=cellIndex%>" ><%=styleStart%><%=printCell.getValueAsString() %><%=styleEnd%></td>

						<% }%>
<script language="javascript">
<%if(baseline.equals("-1")){%>
cell_<%=cellIndex%>.title ="<%=printCell.getDescription()%>" ;
<%}
else {
	%>
cell_<%=cellIndex%>.title ="<%=printCell.getDescription()%>" ;
<%
}
%>
</script>
			
						<%
						cellIndex++;
					}				
					//System.out.print("\n");
					%>
						<td height="15" class="body" ><%=lsModifiedBy%></td>
						<td height="15" class="body" ><%=lsModifiedOn%></td>
						<td height="15" class="body" ><%=lsComments%></td>
						<%
							if(!baseline.equals("-1"))
							{
						%>
								<td class="body"> <%=lsBaseline%> </td>
								<td class="body"> <%=lsBaselineDesc%> </td>
						<%
							}
						%>
						</tr>
					<%
				}
			}
		 %>

		</table>
<% if ((execlDump != null) && (!execlDump.equals("excel"))) 
	{
%>
       </tr>
     </table>
     </form>
              <!--end table for main page table 2 -->
            </td>
          </tr>

        </table>

        <!--start main page table -->

    <br>
    </td>

  </tr>

<br>

<input type="hidden" id="tableId" name="tableId" value="<%=tableId%>">
<input type="hidden" id="tableName" name="tableName" value="<%=tableName%>">
<input type="hidden" id="rowIds" name="rowIds" value="<%=rowIds%>">
<input type="hidden" id="columnids" name="columnids" value="<%=columnids%>">
<input type="hidden" id="baseline" name="baseline" value="<%=baseline%>">
<input type="hidden" id="period" name="period" value="<%=period%>">
<input type="hidden" id="endDate" name="endDate" value="<%=endDate%>">
<input type="hidden" id="startDate" name="startDate" value="<%=startDate%>">

<%@include file='/jsp/common/footer.jsp' %>

<%
	}	
}
catch(Exception e)
{
	e.printStackTrace();
}
%>
