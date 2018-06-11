<%@ page isThreadSafe="false" %>
<%@ page import ="com.boardwalk.exception.*, java.io.*, java.util.*" %> 

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<%
if (bwe == null ) 
{

		Integer memberId = 	( Integer)request.getAttribute("memberId");
		String nhName = (String)request.getAttribute("nhName");

		Integer  nhId = (Integer)request.getAttribute("nhId");
		System.out.println(" nhid = " + nhId );

%>

<html>
<head>

<script language="javascript">

function createCollab(action) 
{
	var name = trim(document.forms[0].collabName.value);
	if(name == '')
	{
		alert("Please enter Name of the Collaboration to be created");
		document.forms[0].collabName.focus();
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
        <table  border="0" cellspacing="0" cellpadding="0" align="center" class="body">
		<div class=body><b>Create new collaboration</b></div>
          <tr> 
            <td valign="top">
                <!--start main page table 2 -->

		<table  border="0" cellspacing="0" cellpadding="0" align="center" valign="top">
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                               
                            <br>
                  <table  border="0" cellspacing="2" cellpadding="2" align="center">
        <form name="createCollab" method="post" action="MyCollaborations">
          <tr valign="top"> 
            <td>
              <table class="body"  border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Collaboration</b></td>
                </tr>
                <tr bgcolor="white"> 
                  <td  width="200">
                    Neighborhood:</td>
                  <td > 
                  <%=nhName%>
                  <input type="hidden" name="memberId" value='<%=memberId.intValue()%>' />
                  
                    </td>
                </tr>
                <tr bgcolor="#eeeeee"> 
                  <td width="200">
                    Name:</td>
                  <td >  
                    <input type="text" name="collabName" value="default">
                    </td>
                </tr>
                <tr bgcolor="white"> 
                  <td width="200" valign="top"> 
                    Description </td>
                  <td>  
                    <textarea name="collabDesc" rows="3" cols="40"></textarea>
                    </td>
                </tr>
              </table>
              <b> 
              </b></td>
          </tr>
             </table>
              <p>&nbsp;</p>
            </td>
          </tr>
          <tr bgcolor="white"> 
            <td> 
              <div align="center"><b> 
                <a href="javascript:createCollab('commitCollab')">Create </a></b></div>
            </td>
          </tr>
         <input type="hidden" name="action" value="commitCollab">
          <%@include file='/jsp/common/commonparameters.jsp' %> </form>

        </table>        
 
            <td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td> 
        <!--end table for table-->
        </tr>
      
      <%
      		}
      	%>

     </table>
              <!--end table for main page table 2 -->
              
			  
            </td>
   </tr><%@include file='/jsp/common/footer.jsp' %>
        </table>

        <!--start main page table -->
    </td>

  </tr>

