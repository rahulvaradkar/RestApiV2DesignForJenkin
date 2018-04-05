<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.table.TableTreeNode" %>

<%
	ArrayList worksheetTables	= (ArrayList)request.getAttribute("worksheetTables");
	String lsbodytext			= (String)request.getAttribute("bodyMessage");
	String lsType				= (String)request.getAttribute("Type");
	lsbodytext					= lsbodytext.trim();
%>


<%@include file='/jsp/common/header.jsp' %>

<SCRIPT language="JavaScript">

	function getRbValue(grp)
	{
		a = document.forms[0].elements[grp];
		e = valButton(a);
		return e;
	}

	function valButton(btn) 
	{
		var cnt = -1;
		if(btn.length > 1)
		{
			for (var i=btn.length-1; i > -1; i--) 
			{
				if (btn[i].checked) {cnt = i; i = -1;}
			}
			if (cnt > -1) 
				return btn[cnt].value;
			else 
				return 0;
		}
		else
		{
			if (btn.checked)
				return btn.value;
			else
				return 0;
		}

	}


	function dlgOK()
	{
		var retObject = new Object();
		var lsCuboidIds = "";
		lsTablesPresent = document.all.TablesPresent.value;
		if(lsTablesPresent.indexOf(',') != -1)
		{
			array = lsTablesPresent.split(",");
			for (i=0; i<array.length; i++) 
			{		
				if(lsCuboidIds != "")
					lsCuboidIds += ",";
				lsCuboidIds += array[i]+"|"+getRbValue(array[i]);
			}
		}
		else
		{
			lsCuboidIds = lsTablesPresent+"|"+getRbValue(lsTablesPresent);
		}
		if(document.all.bodyText != null)
		{
			emailbody = document.all.bodyText.value
			retObject.emailmessage = emailbody;
		}
		retObject.cuboids = lsCuboidIds;
		window.returnValue = retObject;
		window.close();
	}

	function setBodyTxt()
	{
		if(document.all.bodyText != null)
			document.all.bodyText.value = document.all.body.value;
	}

</SCRIPT> 

<tr >
<td align=LEFT valign=LEFT  class="body" width="100%">
<img src="images/logo-boardwalk.gif" width="175" height="34"></td>
</tr>

<tr>
<td>
<body onload="setBodyTxt()">
<table width="100%" class="body" >
  <tr class="body" >
    <td height="100%" class="body" >
      <div align="left"><br>
        <br>
	<form method="post" name="invite" action="InvitationManager">

	<input type="hidden" name="action" value="">
	<input type="hidden" name="body" value="<%=lsbodytext%>">

        <table  border="1" cellpadding=1 cellspacing=1 align="center" class="body"  >
        <tr  >
            <td > <B> My Tables</B> </td>
        </tr>
		<%
		if(worksheetTables != null && worksheetTables.size() > 0)
		{
		%>
		<tr>
		<td>
		<%if(lsType.equals("email"))
			{%>
			<div align="left" style="height:150px; width=400px; overflow:auto">
			<%}
		  else
			{%>
			<div align="left" style="height:248px; width=400px; overflow:auto">
			<%}%>
		 <table>
			<%
				String lsTablesPresent = "";
				for(int i = 0 ; i/2 < worksheetTables.size()/2 ; i+=2)
				{
					String lsWorkSheetName = (String)worksheetTables.get(i);
			%>
				<tr bgcolor="#cee7ff">
					<td nowrap>Worksheet - <B><%=lsWorkSheetName%></B></td>
				</tr>
			<%					
					ArrayList dispTables = (ArrayList)worksheetTables.get(i+1);
					if(dispTables != null && dispTables.size() > 0)
					{
						for(int j = 0 ; j/2 < dispTables.size()/2 ; j+=2)
						{
							String lsDispTableName = (String)dispTables.get(j);
							if(!lsTablesPresent.equals(""))
								lsTablesPresent += ",";
							lsTablesPresent += lsDispTableName;
				%>						
						<tr>
							<td nowrap>&nbsp&nbsp&nbsp<%=lsDispTableName%></td>
						</tr>
				<%	
							Hashtable Tables = (Hashtable)dispTables.get(j+1);
							if(Tables != null && Tables.size() > 0)
							{
								Enumeration e = Tables.keys();
								boolean lbChecked = false;
								while(e.hasMoreElements())
								{
									TableTreeNode tbtn = (TableTreeNode)Tables.get(e.nextElement());
									String lsChecked  = "";
									if(!lbChecked)
									{
										lsChecked  = "checked";
										lbChecked = true;
									}
						%>
								<tr>
									<td nowrap >&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input name="<%=lsDispTableName%>" type="radio" value="<%=tbtn.getId()%>" <%=lsChecked%>><%=tbtn.getName()%> </td>
								</tr>
						<%
								}
							}
							else
							{
							%>
							<tr>
								<td nowrap><font color=red >&nbsp&nbsp&nbsp <I>No tables present</I></font></td>
							</tr>	
							<%
							}
						}
					}
				}
			%>
		</table>
		</div>
			</td>
		</tr>
		<tr>
		<input type="hidden" name="TablesPresent" value="<%=lsTablesPresent%>">
			<td align=center >
				<input type='button' id="okBtn" onclick="dlgOK()" value="    OK    ">
			</td>
		</tr>
		<%
		}
		else
		{
		%>
			<tr>
			<td align=center >
				<font color=red >No Worksheets defined.</font>
			</td>
		</tr>
		<%}%>
        </table>
		<br>

<%if(lsType.equals("email")){%>
		<table border="1" cellpadding=0 cellspacing=0 align="center" class="body">
		<tr>
			<td>
				<LABEL><b>Body Text for e-mail</b></LABEL>
			</td>
		</tr>
		<tr>
			<td>
			<div align="center" >
				<textarea id="txtMsg" cols="48" rows="4" name="bodyText">
				</textarea>
			</div>
			</td>
		</tr>
		</table>
<%}%>
 </form>
</tr>
</td>
</div>
<%@include file='/jsp/common/footer.jsp' %>

