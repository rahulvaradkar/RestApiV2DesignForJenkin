<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.table.TableTreeNode" %>

<%
	ArrayList laStatusReport = (ArrayList)request.getAttribute("statusreport");
%>


<%@include file='/jsp/common/header.jsp' %>

<tr >
<td align=LEFT valign=LEFT  class="body" width="100%">
<img src="images/logo-boardwalk.gif" width="175" height="34"></td>
</tr>

<tr>
<td>
<table width="100%" class="body" >
  <tr class="body" >
    <td height="100%" class="body" >
      <div align="left"><br>
        <br>
	<form method="post" name="invite" action="InvitationManager">

<input type="hidden" name="action" value="">

        <table  border="1" cellpadding=1 cellspacing=1 align="center" class="body"  >
        <tr  >
            <td > <B> Status Report</B> </td>
        </tr>
		<%
		if(laStatusReport != null && laStatusReport.size() > 0)
		{
		%>
		<tr>
			<td>
				<table>			
				<%
					for(int i = 0 ; i/2 < laStatusReport.size()/2 ; i+=2)
					{
						String lsTemplateName = (String)laStatusReport.get(i);
				%>
					<tr>
						<td nowrap>Template - <B><%=lsTemplateName%></B></td>
					</tr>
				<%		
					ArrayList laDispCuboidTransList = (ArrayList)laStatusReport.get(i+1);
					if(laDispCuboidTransList != null && laDispCuboidTransList.size() > 0)
					{
						int rowCount = 0;	
						String rowColorCode = "#cee7ff";
						%>
						<tr>
							<td>
							<table>
							<tr  bgcolor="#CCCCCC">
								<td>
									Table
								</td>
								<td>
									Contributor
								</td>
								<td>
									Last Update On
								</td>
								<td>
									Comment
								</td>
							<tr>
						<%
						for(int j = 0 ; j/2 < laDispCuboidTransList.size()/2 ; j+=2)
						{
							String lsCuboidName = (String)laDispCuboidTransList.get(j);
							Hashtable UserDetailForCuboid = (Hashtable)laDispCuboidTransList.get(j+1);
							Enumeration e = UserDetailForCuboid.keys();
							boolean firstElement = true;
							while(e.hasMoreElements())
							{
								if(rowCount%2 == 0)
									rowColorCode = "#cee7ff";
								else
									rowColorCode = "#ffdfef";
								rowCount++;
								%>
									<tr bgcolor="<%=rowColorCode%>">
								<%
								String[] lsUserDetail = (String[])UserDetailForCuboid.get(e.nextElement());
								if(firstElement)
								{
									%>
									<td  bgcolor="#c4ffc4" rowspan='<%=UserDetailForCuboid.size()%>'><%=lsCuboidName%></td>
									<%
								}
									%>
									<td><%=lsUserDetail[1]%></td>
									<td><%=lsUserDetail[2]%></td>
									<td><%=lsUserDetail[3]%></td>
									</tr>
								<%
									firstElement = false;
							}	
						}					
					%>
							</table>
							</td>
						</tr>
						<%
					}
					}
				%>
				</table>
			</td>
		</tr>
		<%
		}
		%>
        </table>
 </form>
        <br>
        <br>
</tr>
</td>
<%@include file='/jsp/common/footer.jsp' %>

