<script>
// Functionality Added for Validation of Access Rights

function checkCheckBoxsetActionAndSubmit(doNext)
{
	var count = document.forms[0].CollabCount.value;
	var foundChecked = false;

	for(i=1; i <= count; i++)
	{
		var obj = eval("document.forms[0].collabId"+i);
		
		if (obj != null && obj.checked ) 
		{
			foundChecked = true;
			document.forms[0].collabId.value = obj.value;
			document.forms[0].action.value = doNext;
			if(doNext == "removeCollab")
			{
				if(MsgOkCancel() == true)
					validateCollabid(obj.value)

				else
					return;

			}
			else
			{
				document.forms[0].submit();
				return;
			}
		}
	}

	if ( doNext !='createCollab'   &&  foundChecked == false )
	{
		alert( 'Please make a selection before making a request')
	}
	else
	{
		if(doNext != "removeCollab")
		{
			document.forms[0].collabId.value = -1;
			document.forms[0].action.value = doNext;
			document.forms[0].submit(); 
		}
	}
	
}

function validateCollabid(collabid)
{
	
	var url = "jsp/admin/getErrorsForNHCollabWB.jsp?collabid="+collabid+"&typeNumber=1";
	xmlHttp = GetXmlHttpObject(stateChangedForCollab); 
	xmlHttp.open("GET", url , true) ;
	xmlHttp.send(null) ;
}

function stateChangedForCollab() 
{ 

	if (xmlHttp.readyState==4 || xmlHttp.readyState==200)
	{ 
		callBackvalidateCollabid(xmlHttp.responseText);
	
	} 
} 

function callBackvalidateCollabid(asResponse)
{
	
	var retvalues = asResponse.split("^");
	
	if(trim(retvalues[1]) == "delete") 
	{
		document.forms[0].submit();
		return;
	}
	else 
	{
		if(trim(retvalues[0]) == "collab")
		{
			alert("You do not have the Admin Access to Delete the Collab")
			return;
		}
	}
}
	
function trim(str) 
{ 
	str = str.replace(/^\s*/, '').replace(/\s*$/, ''); 
	return str;
} 

function GetXmlHttpObject(handler)
{ 
	var objXmlHttp=null;
	if (navigator.userAgent.indexOf("Opera")>=0)
	{
		alert("This example doesn't work in Opera") ;
		return ;
	}
	if (navigator.userAgent.indexOf("MSIE")>=0)
	{ 
		var strName="Msxml2.XMLHTTP";
		if (navigator.appVersion.indexOf("MSIE 5.5")>=0)
		{
			strName="Microsoft.XMLHTTP";
		} 
		try
		{ 
			
			objXmlHttp=new ActiveXObject(strName);
			objXmlHttp.onreadystatechange=handler ;
			return objXmlHttp;
		} 
		catch(e)
		{ 
			alert("Error. Scripting for ActiveX might be disabled") 
			return 
		} 
	} 
	if (navigator.userAgent.indexOf("Mozilla")>=0)
	{
		objXmlHttp=new XMLHttpRequest();
		objXmlHttp.onload=handler;
		objXmlHttp.onerror=handler ;
		return objXmlHttp;
	}
}



</script>



<%
Vector collabList = (Vector)request.getAttribute("collabList");
	
if ( collabList != null )
{  
%>
	<tr align="left"  valign="top">
		<td class="body" align=center>
			<table  border="0" cellspacing="2" cellpadding="0" class="body">
			<tr valign="top" >
				<td  height="2" ><img src="images/clear.gif" height="2"></td>
				<br>
			</tr>
			
			<!--start table for table-->
			<tr bgcolor="#fddeb9" height=20>
				<td class="body"> <b>Select </b></td>
				<td class="body"> <b>Name </b></td>
				<td class="body"> <b>Description</b> </td>
			</tr>
<%
		int collabCount=0;
        Iterator i = collabList.iterator();
        boolean grey = false;
        while (i.hasNext()) {
			CollaborationTreeNode  c = (CollaborationTreeNode)i.next();
			collabCount++;
            int id = c.getId();
            String name = c.getName();
            String desc = c.getPurpose();
			System.out.println(" collab = " + name );
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
				<td class="body"><input id="collabId<%=collabCount%>" type="checkbox" onClick="javascript:checkBoxCheck(this)" name="collabId" value="<%=id%>"> </td>
                <td class="body"><a href="MyCollaborations?action=editCollab&collabId=<%=id%>"><%=name%> </a> </td>
                <td class="body"><%=desc%> </td>
			</tr>
<%
} // while there are more collaborations
                                        %>
		<tr bgcolor="#cccccc">
			<td class="body" colspan="4" align="center">
			<b>
				<a href="javascript:setActionAndSubmit('createCollab')">New Collaboration</a> |
				<a href="javascript:checkCheckBoxsetActionAndSubmit('editCollab')">Edit </a> |
				<a href="javascript:checkCheckBoxsetActionAndSubmit('showBaselineList')">Show Baselines </a> |
				<a href="javascript:checkCheckBoxsetActionAndSubmit('removeCollab')">Delete</a>
			</td>
        </tr>
            <input type="hidden" name="action" value="editCollab">
        </table>

        </tr>
        <tr height="10">
          <td>&nbsp</td>
        </tr>

        <tr>
          <td><b>Reports<b></td>
        </tr>

		<tr align="left"   valign="top" bgcolor="#FFCC66">
			<td bgcolor="#FFCC66" height="2" ><img src="images/clear.gif" height="2"></td>
		</tr>
        <tr height="5">
          <td>&nbsp</td>
        </tr>
	
		<tr>
			<td class="body" height="2"><a href="javascript:getStatusPeriod()">Status Report</a></td>
        </tr>
		<tr>
			<td class="body" height="2"><a href="javascript:getActivityPeriod()">Activity Report</a></td>
        </tr>
	<tr>
		<td>
			<input type="hidden" name="CollabCount" value="<%=collabCount%>">
		</td>

	</tr>
     </table>
	<!--end table for main page table 2 -->
	</td>
	</tr>
<%}%>
