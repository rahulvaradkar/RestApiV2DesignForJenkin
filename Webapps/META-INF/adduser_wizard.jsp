<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.neighborhood.*" %> 

<%
// get the wbid parameter
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>



<script language="javascript">

function RunWizard(doNext)
{
   if ( eval("document.all.tableId").value  != -1 )
   {
   	 document.forms[0].action.value = doNext
   	 document.forms[0].submit()
   }
   else
   {
   	alert(" Please select a table before using the wizard")
   }
}

function selectTableForWizard( ) 
{
  var TableForLookup = window.open('MyCollaborations','newin','toolbar=yes,location=yes,scrollbar=yes,resizable=yes,menubar=no', false)
 alert( 'In the new boardwalk window that was opened select a table populated with users and click OK')
if ( TableForLookup )
{   
	if ( TableForLookup.document.all.item("tableId").value != -1 )
	{
		   eval("document.all.tableName").innerText  = TableForLookup.document.all.item("tableName").value
		   eval("document.all.tableId").value  = TableForLookup.document.all.item("tableId").value   
		   eval("document.all.tableName").href  = TableForLookup.location.href
		   TableForLookup.close()
	   }
	   else
	   {
	   		alert( 'Boardwalk failed to get the user table')
			if ( TableForLookup )
				   TableForLookup.close()
	   }
}
else
{
	alert( 'Boardwalk failed to get the user  table as the window was closed')
   }
}


</script>

<tr> 
    <td>

        <!--start main page table-->
        <table border="0" cellspacing="0" cellpadding="0" align="center" class="body">

          <tr> 
            <td valign="top">
                <!--start main page table 2 -->

		<table border="0" cellspacing="0" cellpadding="0" align="center" valign="top">
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" ><img src="images/clear.gif"  height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                                
                            <br>
                  <table border="0" cellspacing="2" cellpadding="2" align="center">
       <form method="post" action="AddUsers?">
          <tr valign="top"> 
            <td>
              
              <table class="body" border="0" cellspacing="2" cellpadding="2">
                
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Choose User List Table</b>
                  <a id="tableName"  href="javascript:selectTableForWizard()" > Not set </a>
                  		  <a class="body" href="javascript:selectTableForWizard()" > <img src="images/open.jpg" height=20></a>
		  </td>
                </tr>                
       </table>       
       </td>
          </tr>
   
          
          
          <tr bgcolor="white"> 
            <td> 
              <div align="center"><b> 
                <a href="javascript:RunWizard('commitAddUsers')">Create Users and Memberships</a></b></div>
            </td>
          </tr>
	
	<input type="hidden" name="action" value="AddUsers">
	<input type="hidden" name="tableId" value="-1">
          <%@include file='/jsp/common/commonparameters.jsp' %> 
          
          
          </form>
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

