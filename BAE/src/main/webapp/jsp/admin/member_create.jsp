<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.member.Member,com.boardwalk.user.User, java.io.*, java.util.*" %>


<%
int selNhid = Integer.parseInt(request.getParameter("selNhid"));
String checkaccess = request.getParameter("checkaccess");
//Vector userList = (Vector)request.getAttribute("userList");
String nhName = (String)request.getAttribute("nhName");
request.setAttribute("heading", "Add Members to Neighborhood " + nhName);
Vector nhUsers = (Vector)request.getAttribute("nhUsers");
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<script>
function updateSelected()
{
   var uList = eval("document.forms[0].userList");
   var mydList = eval("document.forms[0].dList");
   uList.value="";
   for (var i=0;i<mydList.length;i++)
   {
	if (i > 0)
          uList.value=uList.value+"+";
	uList.value=uList.value+mydList.options[i].value;
   }
}


function selectItems()
{
    var form = document.forms[0];
    var myoList = eval("document.forms[0].oList");
    var mydList = eval("document.forms[0].dList");
	
    var dLen = mydList.length;
    for (var i=0;i<myoList.length;i++)
    {
		if (myoList.options[i].selected) 
		{
			mydList.options[dLen++] = new Option(myoList.options[i].text,myoList.options[i].value);
			myoList.options[i]= null;
			i=i-1;
		}
    }
    updateSelected();
}

function IsActiveUser(emailAdd)
{
	var url = "jsp/admin/ValidateActiveUser.jsp?emailAddress="+emailAdd;
	xmlHttp = GetXmlHttpObject(stateChanged); 
	xmlHttp.open("GET", url , true) ;
	xmlHttp.send(null) ;
}

function stateChanged() 
{ 
	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
	{ 
		callBackIsActiveUser(xmlHttp.responseText);
	} 
} 

function callBackIsActiveUser(asResponse)
{
	
	var retvalues = asResponse.split("^");
	if(retvalues[1] == 1) 
	{
		selectItems()
	}
	else 
	{
	    var args = new Object();
		var query = showModalDialog("html/isActiveUser.html",args,"dialogHeight:15;dialogWidth:15");
		var retvalue = query.args;
		if(retvalue)
		{
			selectItems()
		}	
	}
}

function addUser()
{
	   var myoList = eval("document.forms[0].oList");
	   for (var i=0;i<myoList.length;i++)
		{
			if (myoList.options[i].selected) 
			{
			IsActiveUser(myoList.options[i].text);
			}
		}
}
	
//function trim(str) 
//{ 
	//str = str.replace(/^\s*/, '').replace(/\s*$/, ''); 
	//return str;
//} 

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


function deselectItems()
{

    var form = document.forms[0];
    var myoList = eval("document.forms[0].oList");
    var mydList = eval("document.forms[0].dList");

    var oLen = myoList.length;
    for (var i=0;i<mydList.length;i++)
    {
        if (mydList.options[i].selected) {
           myoList.options[oLen++] = new Option(mydList.options[i].text,mydList.options[i].value);
           mydList.options[i]= null;
           i=i-1;
        }
    }
    updateSelected();
}
</script>
<form method="get" action="BW_Neighborhoods">


<tr>
    <td>
        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0"  class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->

		<table width="500" border="0" cellspacing="0" cellpadding="0" valign="top">
                    <tr valign="top" bgcolor="#FFCC66">
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>

                    <tr valign="top">
                        <td class="body">
                        <table width="450" border="0" cellspacing="2" cellpadding="2" align="center" class="body">
<tr class="body"  >
<td>
Select Members:
</td>
<td>
</td>
<td>
</td>
<td>
Selected Members
</td>
</tr>
<tr class="body" >
<td>
<%
  System.out.println("usersize" + nhUsers.size() );

%>
<SELECT NAME="oList" size=5 MULTIPLE>
<%
if ( nhUsers.size() > 0 )
{
	for(int userIndex = 0; userIndex <  nhUsers.size();userIndex++ )
	{
      		User u = (User)nhUsers.elementAt(userIndex);
      		String userAddress =u.getAddress();
      		int userId = u.getId();

%>
                      <option class="body"  value="<%=userId%>"> <%=userAddress%>
<%
   } //for
   }// if
%></SELECT>
</td>
<% if(checkaccess.equalsIgnoreCase("TRUE")) {%>
	<td>
	<input class="body" type="button" name="add" value=">>ADD" onClick="javascript:addUser()">
	</td>
<%}
	else
	{%>
	<td>
	<input class="body" type="button" name="add" value=">>ADD" onClick="javascript:selectItems()">
	</td>
<%}%>

<td>
<input class="body" type="button" name="remove" value="REMOVE<<" onClick="javascript:deselectItems()">
</td>
<td>
<SELECT NAME="dList" size=5 MULTIPLE>
</SELECT>
</td>
</tr>
<tr bgcolor="#cccccc" colspan="4" >
<td colspan="4"  align="left">
           <a href="javascript:setActionAndSubmit('memberCommit')"> Add Members </a>
                        </td>
            </tr>
            <input type="hidden" name="action" value="">
	<input type="hidden" name="selNhid" value="<%=selNhid%>">
	<input type="hidden" name="userList" value="">
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


