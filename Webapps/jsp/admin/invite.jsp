<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.distribution.DistributionUser" %>

<%
	ArrayList memberOf = (ArrayList)request.getAttribute("memberList");
	ArrayList inviteeList = (ArrayList)request.getAttribute("inviteeList");
	String  membership = (String)request.getAttribute("membership");
	String managedBy = (String)request.getAttribute("managedList");

	int nhId = -1;
	int memberId = -1;
	Integer NHID = (Integer)request.getAttribute("nhId");
	Integer MEMBERID = (Integer)request.getAttribute("memberId");
	String emailstatus = (String)request.getAttribute("emailstatus");
	String sendurl = (String)request.getAttribute("sendurl");
	String statictemplate = (String)request.getAttribute("statictemplate");
	String staticnhid = (String)request.getAttribute("staticnhid");
	String emailMessage = (String)request.getAttribute("bodyMessage");

	if(statictemplate == null)
	{
		statictemplate = "";			
	}
	if(staticnhid == null)
	{
		staticnhid = "";			
	}	
	if ( emailstatus == null )
	{
		emailstatus = "";
	}
	if ( sendurl == null )
	{
		sendurl = "";
	}	
	
	if ( NHID != null )
	{
		nhId = NHID.intValue();
	}
	if ( MEMBERID != null )
	{
		memberId = MEMBERID.intValue();
	}
	
%>


<%@ page import ="java.util.*,com.boardwalk.util.*" %>


<%


response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server



String title = new String();
String heading = new String();

title = (String)request.getAttribute("title");
if (title == null){
    title = "BOARDWALK";
}
heading = (String)request.getAttribute("heading");
if (heading == null){
    heading = "";
}


String userName = (String)session.getAttribute("userEmailAddress");
// Hashtable memberships =  (Hashtable)session.getAttribute("neighborhoods");

BoardwalkSession bws = (BoardwalkSession) session.getAttribute("bwSession");

System.out.println("bwSession  %%%%%%%%%%%%%%%%%%%" + bws  );
Hashtable memberships = null;

if ( bws != null )
{
	memberships =bws.memberIdToMember;
	//System.out.println("memberships size %%%%%%%%%%%%%%%%%%%" + memberships.size()  );


}
%>
<html>
<head>
<title><%=title%></title>
<script language="javascript">
<%@ include file="/jscript/collab.js" %>
</script>
<SCRIPT language="JavaScript">

function viewCuboidsForTemplate(asTemplate,asType)
{
	document.invite.Templates.value = asTemplate;
	document.invite.FileName.value = asType;
	validateIsSessionTrue();
}

function validateIsSessionTrue()
{
	var url = "jsp/admin/getErrorsForNHCollabWB.jsp?typeNumber=4";
	xmlHttp = GetXmlHttpObject(stateChangedForSession);
	xmlHttp.open("GET", url , true) ;
	xmlHttp.send(null) ;
}
function stateChangedForSession() 
{ 
	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
	{ 
		callBackIsSessionTrue(xmlHttp.responseText);
	} 
} 

function callBackIsSessionTrue(asResponse)
{
	var retvalues = asResponse;
	if(trim(retvalues) == "true") 
	{
		var asTemplate = document.invite.Templates.value;
		var asType = document.invite.FileName.value;

		//url="InvitationManager?action=getCuboids&template="+asTemplate+"&memberId="+document.
		//forms[0].memberId.value+"&Type="+asType;
	
		//var args = new Object();
		//var retObject = showModalDialog(url, args, "dialogHeight:32;dialogWidth:30");
		//alert(retObject.value); //window.open(url,'','height=300,width=450,left=0,top=0,menubar=yes,status=yes,toolbar=yes,scrollbars=yes,location=yes,directories=yes,resizable=yes');
		
		
		//if(retObject!=null)
		//{
			if(asType == 'email')
			{
				url = "InvitationManager?send="+asType+"&selNhid="+document.forms[0].selNhid.value+"&memberId="+document.forms[0].memberId.value+"&template="+asTemplate+"&emailmessage="+<%=emailMessage%>;
			}
			else
			{
				url = "InvitationManager?send="+asType+"&selNhid="+document.forms[0].selNhid.value+"&memberId="+document.forms[0].memberId.value+"&template="+asTemplate;
			}
		//alert(url);
			location.assign(url);
		//}

	}
	else 
	{
		url = "InvitationManager";
		location.assign(url);
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
function viewStatusReport()
{
	url="InvitationManager?action=statusReport";
	//alert(url);
	var args = new Object();
	showModalDialog(url, args, "dialogHeight:32;dialogWidth:30");	
	//window.open(url,'','height=300,width=450,left=0,top=0,menubar=yes,status=yes,toolbar=yes,scrollbars=yes,location=yes,directories=yes,resizable=yes');
}

function onLoad()
{
	<%
		if(emailstatus.equals("Success") || sendurl.equals("Success"))	
		{
	%>
			alert("Email has been sent successfully");
			location.assign("InvitationManager");
	<%
		}
		else if(emailstatus.equals("Failure") || sendurl.equals("Failure"))	
		{
	%>
			alert("Some error was encountered while sending mail please try again or else contact Admin.");
			location.assign("InvitationManager");
	<%		
		}
	%>
		if(document.invite.okBtn != null)
		   document.invite.okBtn.focus();

}

userList = "";
function addDelUser(chkobj, user, template, nhName)
{
	found = false;
	action = "add";
	if(chkobj.checked == false)
		action = "del";
	//alert(action);
	if(action == "add")
	{
		newUser = user + " | " + template + " | " + nhName;
		arrUserList = userList.split(",");
		if(arrUserList.length > 0 && userList != "")
		{
			for(i = 0 ; i < arrUserList.length ; i++)	
			{
				if(newUser == arrUserList[i])
				{
					found = true;
				}
			}
			if(!found)
			{
				userList += ","+newUser;
			}
		}
		else
		{
			userList = newUser;
		}
	}
	else
	{
		newUser = user + " | " + template + " | " + nhName;
		arrUserList = userList.split(",");
		userList = "";
		if(arrUserList.length > 0)
		{
			for(i = 0 ; i < arrUserList.length ; i++)	
			{
				if(newUser == arrUserList[i])
				{
					//Skip the record
				}
				else
				{
					if(userList != "")
						userList += ",";
					userList += arrUserList[i];
				}
			}
		}
		else
		{
			userList = "";
		}
	}
	//alert(userList);
}

function dlgOK()
{
	cbCount = document.forms[0].cbcount.value;
	//alert(cbCount);
	
	template = "";
	nhname = "";
	sendTo = "";
	for( i = 0 ; i < cbCount ; i++)
	{
		if(document.forms[0].elements['chk'+i].checked == true)
		{
			var detail = document.forms[0].elements['hidd'+i].value;
			detailList = detail.split(",");
			if(template != detailList[0])
			{
				template = detailList[0];				
				nhname = detailList[1];
				if(sendTo != "")
					sendTo += "^";
				sendTo += template+"|"+nhname;
			}
			if(nhname != detailList[1])
			{
				nhname = detailList[1];
				sendTo += "|"+nhname;				
			}
			sendTo += ","+detailList[2];
		}
	}
	
	if(sendTo == "")
	{
		alert("Please make a selection to continue")
		return
	}
	//alert(sendTo);
	
	url = "InvitationManager?action=sendMail&memberId="+document.forms[0].memberId.value+"&selNhid="+document.forms[0].selNhid.value+"&sendTo="+sendTo;
	//alert(url);
	location.assign(url)
}

</SCRIPT> 

<META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=UTF-8">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<META HTTP-EQUIV="EXPIRES" CONTENT="-1">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<style>
   /* styles for the tree */
   SPAN.TreeviewSpanArea A {
        font-size: 10pt;
        font-family: verdana,helvetica;
        text-decoration: none;
        color: black
   }
   SPAN.TreeviewSpanArea A:hover {
        color: '#820082';
   }
   /* rest of the document */
   BODY {background-color: white}
   TD {
        font-size: 10pt;
        font-family: verdana,helvetica;
   }
</style>

</head>

    <LINK REL=STYLESHEET TYPE="text/css"
      HREF="css/stylesheet.css" TITLE="stylesheet">


<body onload="onLoad()" bgcolor="#FFFFFF" marginheight="0" marginwidth="10" leftmargin="10" topmargin="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="body" align="top">

<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<tr>
<td>
<table width="600" class="body" >
  <tr class="body" >
    <td height="100%" class="body" >
      <div align="center"><br>
        <br>
	<form method="post" name="invite" action="InvitationManager">

<input type="hidden" name="memberId" value="<%=memberId%>">
<input type="hidden" name="selNhid" value="<%=nhId%>">
<input type="hidden" name="action" value="">
<input type="hidden" name="statictemplate" value="<%=statictemplate%>">
<input type="hidden" name="staticnhid" value="<%=staticnhid%>">
<input type="hidden" name="Templates" value="">
<input type="hidden" name="FileName" value="">


<%
	if(membership != null && membership.equals("none"))
	{
%>
		<table  border="1" cellpadding=1 cellspacing=1 align="center" class="body"  >
        <tr  >
            <td > <font color=red ><B> You have no memberships available </B> </font></td>
        </tr>
		</table>

<%
	}
	else
	{
%>
        <table  cellpadding=0 cellspacing=0>
        <tr>
            <td > <B> My Templates &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</B><a href="javascript:viewStatusReport()" ><B> Status Report</B></a> </td>
        </tr>
	 <tr align="left"   valign="top" bgcolor="#FFCC66"> 
	  <td bgcolor="#FFCC66" height="2" ><img src="images/clear.gif" height="2"></td>
	</tr>
	<tr><td></td></tr>
		<%
		if(memberOf != null  && memberOf.size() > 0)
		{
		%>
		<tr>
		<td>
		<table border="0" width="100%">
			<tr bgcolor="#fddeb9">
				<td  colspan="3"> <B>Templates I contribute to:</B></td>
			</tr>
			<%
				int liCount = memberOf.size();
				if(memberOf != null && memberOf.size() > 0)
				{
					for(int i = 0 ; i/2 < liCount/2 ; i+=2)
					{
						ArrayList Distribution = (ArrayList)memberOf.get(i);
						ArrayList DistributionTemp = (ArrayList)memberOf.get(i+1);
						if(Distribution != null && Distribution.size() > 0)
						{
							for(int j = 0 ; j < Distribution.size() ; j++)
							{
					%>
								<tr>
									<td  colspan="3"><%=Distribution.get(j)%></td>
								</tr>

					<%
								ArrayList Templates = (ArrayList)DistributionTemp.get(j);
								if(Templates != null && Templates.size() > 0)
								{
									for(int k = 0 ; k < Templates.size() ; k++)
									{
							%>
									<tr>
										<td nowrap >&nbsp&nbsp&nbsp <%=Templates.get(k)%>  </td>
										<td nowrap ><a href="javascript:viewCuboidsForTemplate('<%=Templates.get(k)%>','email')" >Email</a> </td>
										<td nowrap ><a href="javascript:viewCuboidsForTemplate('<%=Templates.get(k)%>','file')" >Download</a></td>
									</tr>
							<%
									}
								}
							}
						}
					}
				}
				%>
		</table>
		</td>
		</tr>
		<%
		}
		if(managedBy != null && managedBy.equals("true"))
		{
		%>
		<tr>
		<td>
		<table width="100%">
			<tr bgcolor="#fddeb9">
				<td colspan="3"><B> Templates I manage:</B></td>
			</tr>

		</table>
		</td>
		</tr>
		<%
		}
		if(inviteeList != null && inviteeList.size() > 0 && managedBy != null && managedBy.equals("true"))
		{
		%>
		<tr>
		<td>
		<table>
			<tr>
				<td> List of invitees:</td>
			</tr>
			<tr>
				<td><hr></td>
			</tr>
			<%
				int liChkBoxCount = 0;
				if(inviteeList != null && inviteeList.size() > 0)
				{
					for(int i = 0 ; i/2 < inviteeList.size()/2 ; i+=2)
					{
						String Distribution = (String)inviteeList.get(i);
						ArrayList DistributionTemp = (ArrayList)inviteeList.get(i+1);
				%>
					<tr>
						<td><%=Distribution%></td>
					</tr>
				<%
						if(DistributionTemp != null && DistributionTemp.size() > 0)
						{
							for(int j = 0 ; j/2 < DistributionTemp.size()/2 ; j+=2)
							{
								String lsTemplate = (String)DistributionTemp.get(j);
								ArrayList TemplatesUsers = (ArrayList)DistributionTemp.get(j+1);
					%>
								<tr>
									<td>&nbsp&nbsp&nbsp <%=lsTemplate%></td>
								</tr>
					<%
								
								if(TemplatesUsers != null && TemplatesUsers.size() > 0)
								{
									for(int k = 0 ; k < TemplatesUsers.size() ; k++)
									{
										String[] DistributionUserObj = (String[])TemplatesUsers.get(k);
							%>
									<tr>
										<td nowrap >&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp <input name="chk<%=liChkBoxCount%>" type="checkbox"> <%=DistributionUserObj[0]%> | <%=DistributionUserObj[2]%>  </td>
										<input type="hidden" name="hidd<%=liChkBoxCount%>" value="<%=lsTemplate%>,<%=DistributionUserObj[3]%>,<%=DistributionUserObj[1]%>">			
									</tr>
							<%
										liChkBoxCount++;
									}
								}
							}
						}
					}
				}
			%>
			<input type="hidden" name="cbcount" value="<%=liChkBoxCount%>">			
			<tr>
				<td align=center >
					<input type='button' id="okBtn" onclick="dlgOK()" value="   Send Mail   ">
				</td>
			</tr>
		</table>
		</td>
		</tr>
		<%
		}
		%>
        </table>
        <%@include file='/jsp/common/commonparameters.jsp' %> 
<%
	}
%>
		</form>
        <br>
        <br>
</tr>
</td>
<%@include file='/jsp/common/footer.jsp' %>

