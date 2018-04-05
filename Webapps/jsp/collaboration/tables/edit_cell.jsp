<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer,com.boardwalk.table.*, java.io.*, java.util.*" %> 

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<tr> 
   <td>
     <!--start main page table-->
        <table width="650" border="0" cellspacing="0" cellpadding="0" align="left" class="body">
          <tr> 
            	<td valign="top">
		<!--start main page table 2 -->

		<table width="500" border="0" cellspacing="0" cellpadding="0" align="left" valign="top">
					
                <tr valign="top" align=CENTER> 
                  <td class="body"> 
                    <form name="editcell" method="get" action="">
		    <input align="left" type="text" name="cell"  value="Edit cell">
		    <%@include file='/jsp/common/commonparameters.jsp' %> </form>
		  </td>
                </tr>
              </table>
              <!--end table for main page table 2 -->			  
            </td>
    	  </tr>
        </table>
      <br>
    </td>
  </tr>
<br>