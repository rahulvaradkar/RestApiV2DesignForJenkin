<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, java.io.*, java.util.*" %> 

<%
int parentLevel = -1;
if (request.getAttribute("parentLevel") != null)
    parentLevel = ((Integer)request.getAttribute("parentLevel")).intValue();
int parentId = -1;
String parentName = "";
if (parentLevel >= 0) {
    parentId = Integer.parseInt(request.getParameter("selNhid"));
    parentName = (String)request.getAttribute("parentName");
}
//request.setAttribute("heading", "Create Neighborhood");
%>

<html>
<head>
<script language="javascript">

function createNH(action) 
{
	var name = trim(document.forms[0].nhName.value);
	if(name == '')
	{
		alert("Please enter Name of the NH to be created");
		document.forms[0].nhName.focus();
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

<form method="post" action="BW_Neighborhoods">





<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


<tr> 
    <td>
        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0" align="center" class="body">

          <tr> 
            <td valign="top">

		<table width="500" border="0" cellspacing="0" cellpadding="0" align="center" valign="top">

                            <br>
                            	<div class=body>
                                <b>Create a  Neighborhood 
                                <%
	    if (parentLevel >= 0) 
	    {
	     %>
		    under <%=parentName%>
		  <%}%>  
		    </b></div>
			 <tr valign="top" bgcolor="#FFCC66"> 
					<td  height="2" width="100%"><img src="images/clear.gif" width="100%" height="2"></td>
                    	</tr>
                    <tr valign="top"> 
                        <td class="body" align=center> 


                        <table width="450" border="0" cellspacing="0" cellpadding="0" align="center" class="body">
                                
<tr><td height=10 colspan=3></td></tr>			   
<tr bgcolor="#eeeeee" valign="top"> 
	
            <td colspan="2" class="body"  >Name</td>
            <td width="250" class="body"> 
              <input type="text" name="nhName">
              </td>
          </tr>
          <tr bgcolor="white" valign="top"> 
            <td colspan="2" class="body"> 
              <p>Secure<br>
                </p>
            </td>
            <td class="body" width="250"> 
              <input type="checkbox" name="secure" value="">
              </td>
          </tr>

            <tr bgcolor="#cccccc"> 
                        <td class="body" colspan="3" align="center"> 
                          <b>

            <a href="javascript:createNH('commitNH')">OK</a>
                        </td>
            </tr> 
            <input type="hidden" name="action" value="">
<input type="hidden" name="parentId" value="<%=parentId%>">
<input type="hidden" name="parentLevel" value="<%=parentLevel%>">
             <%@include file='/jsp/common/commonparameters.jsp' %> </form>
            
        </table>     	
        <td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td>		
        <!--end table for table-->
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

