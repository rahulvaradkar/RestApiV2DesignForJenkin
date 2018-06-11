<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.database.Transaction, com.boardwalk.collaboration.*, com.boardwalk.whiteboard.*, com.boardwalk.table.*,java.io.*" %>

<%
Hashtable tList = (Hashtable)request.getAttribute("transactionList");
String viewPref = request.getParameter("ViewPreference");
String tableId = request.getParameter("tableId");
String tableName = request.getParameter("tableName");
long startDate = ((Long)request.getAttribute("startDate")).longValue();
long endDate = ((Long)request.getAttribute("endDate")).longValue();
if (tableName == null)
{
	tableName = "";
}
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<script language="javascript">
<%@ include file="/jscript/dateutil.js" %>
</script>
<script language="javascript">
function checkAndSubmit(ii)
{
	var countChecked = 0
	var asOfTid = -1;
	var compTid = -1;

	if (document.f1.c1.value != undefined) // only one button
	{
		asOfTid = document.f1.c1.value
		countChecked = 1;
	}
	else
	{
		for(var i=0;i<document.f1.c1.length;i++)
		{
			var chk = document.f1.c1[i].checked
			if (chk && countChecked == 0)
			{
				asOfTid = document.f1.c1[i].value
			}
			else if (chk && countChecked == 1)
			{
				compTid = document.f1.c1[i].value
			}

			if (chk)
				countChecked = countChecked + 1
		}
	}
	if (ii <= 2)
	{
		if (countChecked != 1)
		{
			alert("Please select only one item in the list");
			return;
		}
	}
	else
	{
		if (countChecked != 2)
		{
			alert("Please select only two items in the list");
			return;
		}

	}

	if (ii == 2 || ii == 4)
		document.forms[0].showChangesOnly.value = "true";

	if (compTid == -1)
		compTid = asOfTid

	if (compTid > asOfTid)
	{
		var tmp = asOfTid
		asOfTid = compTid
		compTid = tmp
	}

	document.forms[0].asOfTid.value = asOfTid
	document.forms[0].compTid.value = compTid
	document.forms[0].submit()
}
</script>

<tr>
  <td>
		<form name="f1" method="get" action="MyTables"  >
        <!--start main page table-->
        <table  align="center" border="0" cellspacing="0" cellpadding="0"  class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->
		<table  border="0" cellspacing="1" cellpadding="2">
		 <tr>
		   <td colspan="8" align="left">
			<b class="page-heading"> List of Updates on table
			<u><i>
			<a href="MyTables?tableId=<%=tableId%>&ViewPreference=<%=viewPref%>&action=editTable"><%=tableName%></a>
			</i></u></b>
			<br>
			<span class="small-text"> from </b>
			<span class="small-text" id="startDate"></span>
			&nbsp<span class="small-text">to </b>
			<span class="small-text" id="endDate"></span>
		  </td>
		  <td colspan="2" align="right">
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
		  <td></td>
		  <td class="body"> <b>Updated By</b> </td>
		  <td class="body"> <b>Updated On</b> </td>
		  <td class="body"> <b>Comment</b> </td>
		  <td class="body"> <b>Rows Added</b> </td>
		  <td class="body"> <b>Rows Deleted</b> </td>
		  <td class="body"> <b>Columns Added</b> </td>
		  <td class="body"> <b>Cells Updated</b> </td>
		  <td class="body"> <b>Formulae Changed</b> </td>
		  <td class="body"> <b>Baseline Created</b> </td>
		</tr>


		<%
			Vector tvec = new Vector(tList.keySet());
			Collections.sort(tvec);
		    Iterator i = tvec.iterator();
		    boolean grey = false;

		    while (i.hasNext()) {
		    	Integer tid = (Integer)i.next();
			    Vector  vt = (Vector)tList.get(tid);
			    Transaction t = (Transaction)vt.elementAt(0);
			    boolean rowadd = false;
			    boolean rowdel = false;
			    boolean coladd = false;
			    boolean cellupd = false;
			    boolean frmupd = false;
			    boolean blnadd = false;
			    Iterator j = vt.iterator();
			    String checkImage = "";
			    while (j.hasNext())
			    {
			    	Transaction ts = (Transaction)j.next();
			    	String descr = ts.getDescription();
			    	System.out.println("descr=" + descr);
			    	if (descr.startsWith("rowadd"))
			    	{
			    		rowadd = true;
			    		//System.out.println("rowadd set to true");
			    	}
			    	else if (descr.startsWith("rowdel"))
			    	{
			    		rowdel = true;
			    		//System.out.println("rowdel set to true");
			    	}
			    	else if (descr.startsWith("coladd"))
			    	{
			    		coladd = true;
			    		//System.out.println("coladd set to true");
			    	}
			    	else if (descr.startsWith("cellupd"))
			    	{
			    		cellupd = true;
			    		//System.out.println("cellupd set to true");
			    	}
			    	else if (descr.startsWith("frmupd"))
			    	{
			    		frmupd = true;
			    		//System.out.println("frmupd set to true");
			    	}
			    	else if (descr.startsWith("blnadd"))
			    	{
			    		blnadd = true;
			    		//System.out.println("blnadd set to true");
			    	}
			    }
			    int id = t.getId();
			    String updatedBy = t.getCreatedByUserAddress();
			    //String updatedOn = t.getCreatedOn();
			    String comment = t.getComment();
			    String descr = t.getDescription();
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
		    <tr bgcolor="<%=rowColorCode%>">
		    <td>
		    <input name="c1" type="checkbox" width="1%" value="<%=id%>" >
		    </td>
			<td class="body"><%=updatedBy%> </td>
			<td class="body" id="updatedOn<%=id%>"></td>
<script language="javascript">
var d = new Date (<%=t.getCreatedOnTime()%>)
updatedOn<%=id%>.innerText = formatDateTime(d,"NNN dd, yyyy hh:mm:ssa");
</script>
			<td class="body"><%=comment%> </td>
			<td align="center" class="body">
			 <%
			   	if (rowadd == true)
			   	{
			 %>
			 	<img src="images/check.gif" />
			 <%
			 	}
			 %>
			</td>
			<td align="center" class="body">
			 <%
			   	if (rowdel == true)
			   	{
			 %>
			 	<img src="images/check.gif" />
			 <%
			 	}
			 %>
			</td>
			<td align="center" class="body">
			 <%
			   	if (coladd == true)
			   	{
			 %>
			 	<img src="images/check.gif" />
			 <%
			 	}
			 %>
			</td>
			<td align="center" class="body">
			 <%
			   	if (cellupd == true)
			   	{
			 %>
			 	<img src="images/check.gif" />
			 <%
			 	}
			 %>
			</td>
			<td align="center" class="body">
			 <%
			   	if (frmupd == true)
			   	{
			 %>
			 	<img src="images/check.gif" />
			 <%
			 	}
			 %>
			</td>
			<td align="center" class="body">
			 <%
			   	if (blnadd == true)
			   	{
			 %>
			 	<img src="images/check.gif" />
			 <%
			 	}
			 %>
			</td>
		    </tr>
		<%
		     } // tables
		 %>

		</table>
        </tr>
          <tr>
          	<td height="15"> </td>
          </tr>
          <tr >
          	<td border="1" bordercolor="grey">
				<a href="javascript:checkAndSubmit(1)" > Show Transaction |</a>
				<a href="javascript:checkAndSubmit(2)" > Show Transaction Changes Only |</a>
				<a href="javascript:checkAndSubmit(3)" > Compare Two Transactions |</a>
				<a href="javascript:checkAndSubmit(4)" > Compare Two Transactions Changes Only </a>
          	</td>
          </tr>
     </table>
     <input type="hidden" name="ViewPreference" id="ViewPreference" value="<%=viewPref%>">
     <input type="hidden" name="tableId" id="tableId" value="<%=tableId%>">
     <input type="hidden" name="asOfTid" id="asOfTid" value="-1">
     <input type="hidden" name="compTid" id="compTid" value="-1">
     <input type="hidden" name="action" id="action" value="editTable">
     <input type="hidden" name="showChangesOnly" id="showChangesOnly" value="false">
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
<%@include file='/jsp/common/footer.jsp' %>
