<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.member.Member, com.boardwalk.user.*,java.io.*, java.util.*,com.boardwalk.database.*,java.sql.*" %>

<%
Hashtable nhMembers = (Hashtable)request.getAttribute("nhMembers");
String nhName = (String)session.getAttribute("nhName");
int selNhid = Integer.parseInt(request.getParameter("selNhid"));
boolean isSecure = ((Boolean)request.getAttribute("isSecure")).booleanValue();
boolean isMbrofNH = ((Boolean)request.getAttribute("isMbrofNH")).booleanValue();
boolean isManager = ((Boolean)request.getAttribute("isManager")).booleanValue();
int userId =  ((Integer)request.getAttribute("userId")).intValue();
int memberId =  ((Integer)request.getAttribute("memberId")).intValue();
String managedbyEmail = (String)request.getAttribute("managedbyEmail");
String UserName = (String)session.getAttribute("userEmailAddress");
//System.out.println("User Email address"+UserName);
System.out.println("Userid = " + userId + "isManager " + isManager+ "mId " + memberId+ " nhName " + nhName + " selNhid " + selNhid
			+ " isSecure " + isSecure+" isMbrofNH " + isMbrofNH);
request.setAttribute("heading", "Membership List");
boolean check_access = false;

Connection	connectionJsp = null;

try
{
	DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
	connectionJsp = databaseloader.getConnection();
	check_access = UserManager.isUserAccessPresent(connectionJsp);
}
catch(Exception e)
{
	e.printStackTrace();
}

 boolean addNewUser = true;
 boolean deactivateuser = true;
 boolean addMember = true;
 boolean deleteMember = true;
if(check_access == true)
{
	HashMap userMgmtAccess = UserManager.get_priviliges_for_user(UserName,nhName);
	if (userMgmtAccess.size() > 0 )
	{
		addNewUser = ((Boolean)userMgmtAccess.get("Add New User")).booleanValue();
		deactivateuser = ((Boolean)userMgmtAccess.get("Deactivate User")).booleanValue();
		System.out.println("---------Value of deactivateuser--------"+deactivateuser);
		addMember = ((Boolean)userMgmtAccess.get("Add Member")).booleanValue();
		System.out.println("---------Value of addMember--------"+addMember);
		deleteMember = ((Boolean)userMgmtAccess.get("Delete Member")).booleanValue();
	}
	else
	{
		addNewUser = false;
		deactivateuser = false;
		addMember = false;
		deleteMember = false;
	}
	
}



// Get the status of session variable check status if true then read values for login users(Procedure will be called)
%>


<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<script>
var isSecure = <%=isSecure%>
var isMbrofNH = <%=isMbrofNH%>
var isManager = <%=isManager%>
var userId = <%=userId%>
function setforJoin()
{
    if ((isSecure == true) && (!isManager))
    {
        javascript:popupEmailClient('<%=managedbyEmail%>', '<%=userName%>', '', 'Please Add me to Neighborhood '+'<%=nhName%>', 'Use this URL : ' );
    }

    else
    {
	document.forms[0].action.value="memberCommit";
	document.forms[0].submit();
    }


}
function setforAddMembers()
{	
	    if(document.forms[0].AddMember.value == 'true')
		{
           document.forms[0].action.value = "joinMembers";
           //alert (document.forms[0].action.value)
           document.forms[0].submit();
		}
		else
			alert("You are not authorized to Add Members");

}

function setforAddUser()
{
	    if(document.forms[0].AddUser.value == 'true')
		{ 
           document.forms[0].action.value = "addUser";
           document.forms[0].submit();
		}
		else
			alert("You are not authorized to Add User");
}

function setforDeactivateUser()
{
  var foundChecked = false;
	var single = false;
        var obj = eval("document.forms[0].memberId");
        var mid = 0;
        var len = obj.length;
        if (len == null) {
            if (obj == null)
                len = 0;
            else {
                single = true;
                len=1;
            }
	}
	var o;
        for (i=0;i<len;i++)
        {
            if (single) {
                o = obj;
                if (o == null)
                    break;
            }
            else
               o = obj[i];
            if (o.checked) {
                foundChecked = true;
				  myuid = o.uid;
			}
		}
		if (foundChecked == false)
        	alert( 'Please make a selection before making a request');
        else
      	{
			if(document.forms[0].checkaccess.value == 'true')
			{
				if(document.forms[0].DeactivateUser.value == 'true')
				{
						document.forms[0].checkAction.value = "DeactivateUser";
						validateUserId(myuid);
				}
				else
					alert("You are not authorized to Deactivate User");
			}
			else
			{
				document.forms[0].checkAction.value = "DeactivateUser";
				validateUserId(myuid);
			}
        }


}
function setforDelete() {
        var foundChecked = false;
	var single = false;
	var myuid = -1;
        var obj = eval("document.forms[0].memberId");
        var mid = 0;
        var len = obj.length;
        if (len == null) {
            if (obj == null)
                len = 0;
            else {
                single = true;
                len=1;
            }
	}
	var o;
        for (i=0;i<len;i++)
        {
            if (single) {
                o = obj;
                if (o == null)
                    break;
            }
            else
               o = obj[i];
				
            if (o.checked) {
                foundChecked = true;
                myuid = o.uid;
				

			if(document.forms[0].checkaccess.value == 'false')
			{  
               if ((!isManager) && (myuid != userId))
                {
                    alert("You can delete only yourself from the Neighborhood");
                    o.checked = false;
                    o.focus();
                    return;
                 }
			}
                // else if ((isManager) && (myuid == userId))
                // {
                //     alert("You cannot delete Manager of the Neighborhood");
                //     o.checked = false;
                //     o.focus();
                //     return;
                //  }
            }
        }
	if (foundChecked == false)
        	alert( 'Please make a selection before making a request');
        else
      	{
			if(document.forms[0].checkaccess.value == 'true')
			{
				if(document.forms[0].DeleteMember.value == 'true')
				{
					document.forms[0].checkAction.value = "DeleteMember";
					validateUserId(myuid);
				}
				else
				{
					alert("You are not authorized to Delete Members");
				}
			}
			else
			{
				document.forms[0].checkAction.value = "DeleteMember";
				validateUserId(myuid);
			}
        }
}
function validateUserId(userId)
{
	var url = "jsp/admin/getErrorsForNHCollabWB.jsp?userID="+userId+"&typeNumber=2";
	xmlHttp = GetXmlHttpObject(stateChangedForIsOwnerOfCollab);
	xmlHttp.open("GET", url , true) ;
	xmlHttp.send(null) ;
}
function stateChangedForIsOwnerOfCollab() 
{ 
	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
	{ 
		callBackvalidateUserId(xmlHttp.responseText);
	} 
} 
function callBackvalidateUserId(asResponse)
{
	var retvalues = asResponse;
	if(trim(retvalues) == "true") 
	{
		alert("The selected Member is owner of some Collaboration")
		return;
	}
	else 
	{
		if(document.forms[0].checkAction.value == "DeleteMember")
		{
			document.forms[0].action.value = "memberDelete";
			document.forms[0].submit();
			return;	
		}
		else
			if(document.forms[0].checkAction.value == "DeactivateUser")
			{
				document.forms[0].action.value = "deactivateUser";
				document.forms[0].submit();
				return;	
			}
	}
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
function trim(str) 
{ 
	str = str.replace(/^\s*/, '').replace(/\s*$/, ''); 
	return str;
} 


</script>

 <form method="post" action="BW_Neighborhoods">
 <tr>
 <input type="hidden" name = "userList" value ="<%=userId%>">
 <input type="hidden" name="nhName" value="<%=nhName%>">

 <input type="hidden" name="AddUser" value="<%=addNewUser%>">
<input type="hidden" name="DeactivateUser" value="<%=deactivateuser%>">
<input type="hidden" name="AddMember" value="<%=addMember%>">
<input type="hidden" name="DeleteMember" value="<%=deleteMember%>">
<input type="hidden" name="checkaccess" value="<%=check_access%>">
<input type = "hidden" name="checkAction" value="">




    <td>
        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0" align="left" class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->
				<table width="500" border="0" cellspacing="0" cellpadding="0" align="left" valign="top">

				 <tr class="body"   >
						       	<td>
						    				   <b>Neighborhood: </b><%=nhName%> <br>
						    				   <b>Managed by     : </b><%=managedbyEmail%> <br>
						    				   <b>Membership    : </b><% if ( isSecure ){ %> By Approval of the Manager <% } else { %> Open to public <%}%><br>
						    	</td>

						       </tr>

                    <tr valign="left" bgcolor="#FFCC66">
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>




                    <tr valign="left">
                        <td class="body" align="left">
                            <br>
                            <br>
<%

if ( !isMbrofNH )
{

%>
	Please click on "Join" button to become a member of this neighborhood

<%
}

%>


<%



int noMembers = 0;
   if (isManager|| isMbrofNH || !isSecure)
{
%>

   <table border="0" cellspacing="2" cellpadding="2" align="left" class="body">

                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20 align="left" >
       		<td class="body"> <b>Select </b></td>
                <td class="body"> <b>Name </b></td>
                      </tr>


<%
  System.out.println("membesize" + nhMembers.size() );
  Enumeration members = nhMembers.keys();

  while(members.hasMoreElements())
  {
      Member m = (Member)members.nextElement();
      String userAddress = (String)nhMembers.get(m);
      int mId = m.getId();
      int uid=m.getUserId();
      noMembers++;
%>
       <tr bgcolor="#eeeeee" valign="top" align="left" >
                  <td valign="top" width="30">
                      <input  type="checkbox" name="memberId" value="<%=mId%>" uid="<%=uid%>">
					  <input type = "hidden" name ="userId" value = "<%=uid%>">
                  </td>
                  <td valign="top"  width="470">
		  <%=userAddress%>
		  </td>
                </tr>
<%
   } // while
%>


<%
}  // endif ifMbrofNH
%>
<%if(check_access == true)
  {%>
	  <tr bgcolor="#cccccc" >
		  <td class="body" colspan="2" align="left">
		  <% if ( !isMbrofNH)  
		  {%>
			  <a href="javascript:setforJoin()">Join</a>
		  <%}%>
				| <a href="javascript:setforAddMembers()">Add Members</a>
				| <a href="javascript:setforAddUser()">Add User</a>
	<%
	if (noMembers > 0) {
	%>
				  |  <a href="javascript:setforDelete()">Remove Member</a>
				  | <a href="javascript:setforDeactivateUser()">Deactivate User</a>
	<%}
}
%>
                        </td>
<%if(check_access == false)
{
%>
  <tr bgcolor="#cccccc" >
                        <td class="body" colspan="2" align="left">
<% if ( !isMbrofNH)  {%>
          <a href="javascript:setforJoin()">Join</a>

<% } if  ((isManager) || (isMbrofNH)) {  %>
 | <a href="javascript:setforAddMembers()">Add Members</a>
<%
}
if (noMembers > 0) {
%>
              |  <a href="javascript:setforDelete()">Remove</a>
<%
        }
}
%>
			 </td>
            </tr> <input type="hidden" name="action" value="">
<input type="hidden" name="selNhid" value="<%=selNhid%>">
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

<%  try
  {
		System.out.println(" in Try JSP block");
		connectionJsp.close();
		connectionJsp = null;
  }
  catch ( SQLException sql )
  {
	sql.printStackTrace();
  }
%>

<%@include file='/jsp/common/footer.jsp' %>



