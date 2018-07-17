<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.database.Transaction, com.boardwalk.collaboration.*, com.boardwalk.whiteboard.*, com.boardwalk.table.*,java.io.*" %>

<%
Hashtable tList = (Hashtable)request.getAttribute("cellVersions");
String tableId = request.getParameter("tableId");
String tableName = request.getParameter("tableName");
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
		<form name="f1" method="get" action="MyTables"  >
        <!--start main page table-->
        <table   border="0" cellspacing="0" cellpadding="5"  class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->
		<table  border="0" cellspacing="1" cellpadding="2">
		 <tr>
		   <td colspan="4" align="left">
			<b class="page-heading"> List of Updates on selected cell

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
		   <td colspan="5" height="15">
		   </td>
		 </tr>

		<!--start table for table-->
		<tr bgcolor="gray" style='color:white;' align="left" >
		  <td class="body"> <b>Updated Value</b> </td>
		  <td class="body"> <b>Fx</b> </td>
		  <td class="body"> <b>Formulae Changed</b> </td>
		  <td class="body"> <b>Updated By</b> </td>
		  <td class="body"> <b>Updated On</b> </td>
		  <td class="body"> <b>Comment</b> </td>
		</tr>


		<%
			Vector tvec = new Vector(tList.keySet());
			Collections.sort(tvec);
		    Iterator i = tvec.iterator();
		    boolean grey = false;
		    String currFormula = null;
		    while (i.hasNext())
		    {
		            Integer tid = (Integer)i.next();
			    VersionedCell  vc = (VersionedCell)tList.get(tid);
			    Transaction t = vc.getTransaction();
			    String vcFormula = vc.getFormula();
			    boolean frmupd = false;

			    if (currFormula == null )
			    {
			    	if ( vcFormula != null )
			    	{
			    	    frmupd = true;
			    	    currFormula = vcFormula;
			    	}
			    	else
			    	{
			    	    frmupd = false;
			    	}
			    }
			    else
			    {
			    	if ( !vcFormula.equals(currFormula) )
			    	{
			    		frmupd = true;
			    	    	currFormula = vcFormula;
			    	}
			    	else
			    	{
			    		frmupd = false;
			    		currFormula = vcFormula;

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
		    <td class="body"><%=vc.getValueAsString()%> </td>

			  <% 	if (frmupd == true)
				{
			   %>
				  <td class="body"><span><img  title="<%=vc.getFormula()%>" style="display:inline;"  src="images/formula.gif"  height=18 width=18 ></span> </td>
			   <%
				}
				else
				{
			      %>
				    <td class="body"> </td>

			    <%
			        }
			     %>

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
			<td class="body"><%=updatedBy%> </td>
			<td class="body" id="updatedOn<%=id%>"></td>
<script language="javascript">
var d = new Date (<%=t.getCreatedOnTime()%>)
updatedOn<%=id%>.innerText = formatDateTime(d,"NNN dd, yyyy hh:mm:ssa");
</script>
			<td class="body"><%=comment%> </td>

		    </tr>
		<%
		     } // transactions
		 %>

		</table>
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
<%@include file='/jsp/common/footer.jsp' %>
