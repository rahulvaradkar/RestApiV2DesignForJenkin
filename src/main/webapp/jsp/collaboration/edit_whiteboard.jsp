<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.whiteboard.*,com.boardwalk.collaboration.*,com.boardwalk.table.*" %>


<%
	String QueryPreference =(String)request.getAttribute("QueryPreference");
    Vector wbtableList = (Vector)request.getAttribute("tableList");
    int wbid = Integer.parseInt (request.getParameter("wbid"));
    CollaborationTreeNode ct = null;
     WhiteboardTreeNode wbt = null;

    if ( wbtableList != null && wbtableList.size() > 0 )
    {
	    ct = (CollaborationTreeNode)wbtableList.elementAt(0);
	    if ( ct != null )
	    {
		Vector Whiteboards = (Vector) ct.getWhiteboards();

		if ( Whiteboards.size() > 0 )
		{
			wbt = (WhiteboardTreeNode)Whiteboards.elementAt(0);
		}
	 }
    }
    //request.setAttribute("heading", wbt.getName());
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>

<tr>
  <td>

	<!--start main page table-->
	<table width="680" border="0" cellspacing="0" cellpadding="0" class="body">

	  <tr>
	    <td valign="top">
		<!--start main page table 2 -->
		<%@include file='/jsp/common/error_message.jsp' %>


		<table width="500" border="0" cellspacing="0" cellpadding="0"  valign="top">
		<b><%=wbt.getName()%></b>
	         <br>
		    <tr valign="top" bgcolor="#FFCC66">
		      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
		    </tr>

		    <tr valign="top">
			<td class="body" align=center>
			    <br>

			    <br>
			<table width="450" border="0" cellspacing="2" cellpadding="2" align="center" class="body">
				<form method="post" action="MyTables?wbid=<%=wbid%>">
				<!--start table for table-->
	<tr bgcolor="#fddeb9" height=20>
		<td class="body"> <b>Select </b></td>
		<td class="body"> <b>Name </b></td>
		<td class="body"> <b>Description</b> </td>

		      </tr>
<!-- Get all the tables in the database and list them here-->


                                    <%	int id = 0;
									

		if ( wbt != null  )
		{
			Vector tableList = (Vector)wbt.getTables();

			boolean grey = false;

			Iterator tables = tableList.iterator();
			while (tables.hasNext()) {
			TableTreeNode table = (TableTreeNode)tables.next();
			String name = table.getName();
			id = table.getId();
			String description = table.getPurpose();
			String viewPreferenceType = table.getDefaultViewPreference();



			String rowColorCode = "white";
			if ( grey == true )
			{
				rowColorCode = "#eeeeee";
				grey = false;
			}
			else
			{
				grey = true;
			}
			%>


			<tr  bgcolor="<%=rowColorCode%>" >
			<td class="body">
			<input onClick="tableId.value=<%=id%>;javascript:checkBoxCheck(this)" type="checkbox" name="tableId" value="<%=id%>">
			</td>
			<td class="body"><a href="MyTables?tableId=<%=id%>&wbid=<%=wbid%>&ViewPreference=<%=viewPreferenceType%>&action=editTable"><%=name%></a></td>
			<td class="body"><%=description%></td>
			<input type="hidden" name="ViewPreference" value="<%=viewPreferenceType%>">


			</tr>
			<%
			} // while there are wbs
		} // if there are any tables

		%>
            <tr bgcolor="#cccccc">
				<td class="body" colspan="4" align="center">
				<b>
				<a href="javascript:setActionAndSubmit('createTable')"> New Table  </a>|
				<a href="javascript:checkCheckBoxsetActionAndSubmit('editTable')">Edit  </a> |
				<a href="javascript:checkCheckBoxsetActionAndSubmit('removeTable')">Delete   </a>
				</td>
			</tr>
                <input type="hidden" name="wbid" value="<%=wbid%>">
                <input type="hidden" name="tableId" value="<%=id%>">
				<input type="hidden" name="selectedTableId" value="<%=id%>">
                <input type="hidden" name="action" value="createTable">
				<input type="hidden" id="membershipId" name="membershipId" value="<%=bws.memberId.intValue()%>">
				<input type="hidden" id="neighborhoodId" name="neighborhoodId" value="<%=bws.nhId.intValue()%>">
				<input type="hidden" id="QueryPreference" name="QueryPreference" value="<%=QueryPreference%>">
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






