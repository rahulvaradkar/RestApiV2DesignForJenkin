<%@ page isThreadSafe="false" %>
<%@ page import ="java.io.*, java.util.*" %> 

<%
int   collabId   = Integer.parseInt(request.getParameter("collabId"));
//request.setAttribute("heading", "Create Whiteboard");
%>
<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<html>
<head>
<script language="javascript">

function createWB(action) 
{
	var name = trim(document.forms[0].wbName.value);

	if(name == '')
	{
		alert("Please enter Name of the WhiteBoard to be created");
		document.forms[0].wbName.focus();
		return;
	}
	else
	{
		document.forms[0].action.value = action;
		document.forms[0].submit();
	}
}

</script>
</head>

<tr> 
    <td>

        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0" align="center" class="body">

          <tr> 
            <td valign="top">
                <!--start main page table 2 -->
		<table width="500" border="0" cellspacing="0" cellpadding="0" align="center" valign="top">
		
		<b>Create new whiteboard</b>
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                               
                            <br>
                  <table width="500" border="0" cellspacing="2" cellpadding="2" align="center">
        <form METHOD="post" action="Whiteboard">
          <tr valign="top"> 
            <td>
              <table class="body" width="500" border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Whiteboard</b></td>
                </tr>
                <tr bgcolor="#eeeeee"> 
                  <td width="200">
                    Name:</td>
                  <td >  
                    <input type="text" name="wbName" value="default">
                    </td>
                </tr>
                <tr bgcolor="white"> 
                  <td width="200" valign="top"> 
                    Description </td>
                  <td>  
                    <textarea name="wbDesc" rows="3" cols="40"></textarea>
                    </td>
                </tr>
              </table>
              <b> 
              </b></td>
          </tr>
          <tr bgcolor="white"> 
            <td> 
              <div align="center"><b> 
                <a href="javascript:createWB('commitWhiteboard')">Create </a></b></div>
            </td>
          </tr>
<input type="hidden" name="collabId" value=<%=collabId%>>
<input type="hidden" name="action" >
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



