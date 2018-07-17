<%@ page isThreadSafe="false" %>
<%@ page import ="com.boardwalk.exception.*,java.io.*, java.util.*" %> 

<html>
<head>
<title>Create Baseline</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript">
    function setActionAndSubmit(action) {
	    document.forms[0].action.value = action;
	    document.forms[0].submit();
    }
</script>
</head>
<%
int   collabId   = ((Integer)request.getAttribute("collabId")).intValue();
request.setAttribute("heading", "Create Collaboration Baseline");
%>

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
                <!--start main page table 2 -->

		<table width="500" border="0" cellspacing="0" cellpadding="0" align="center" valign="top">
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" width="500">
                      <img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                               
                            <br>                    
                  <table width="500" border="0" cellspacing="2" cellpadding="2" align="center">
     <form name="x" method="post" action="MyCollaborations">

          <tr valign="top"> 
            <td>
              <table class="body" width="500" border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Collaboration Baseline</b></td>
                </tr>
                <tr bgcolor="#eeeeee"> 
                  <td>Name</td>
                  <td width="300">Short Description</td>
                  
                </tr>
                <tr > 
                  <td valign="top">
                    <input type="text" name="baselineName" value="default">
                    </td>
                  <td>
                    <input type="text" name="baselineDesc" value="default" size="50">
		    <input type="hidden" name="action" value="commitBaseline">
                    </td>
                </tr>
              </table>
              <p>&nbsp;</p>
            </td>
          </tr>
          <tr bgcolor="white"> 
            <td> 
              <div align="center"><b> 
                <a href="javascript:setActionAndSubmit('commitBaseline')">Add Baseline</a></b></div>
            </td>
          </tr>
<input type="hidden" name="collabId" value="<%=collabId%>">
          <%@include file='/jsp/common/commonparameters.jsp' %> </form>
        </table>        
        <!--end table for table-->
        </tr>
    
     </table>
              <!--end table for main page table 2 -->
              
			  
            </td>
            
            
            <td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td>



                   </tr>
        </table>

        <!--start main page table -->
        
    <br>
    </td>

  </tr>
  <tr bgcolor="#000000"> 
    <td colspan="2" height="10"><img src="images/clear.gif" width="2" height="10"></td>
  </tr>
<br>
<%@include file='/jsp/common/footer.jsp' %>