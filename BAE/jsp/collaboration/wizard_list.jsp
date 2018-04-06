<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.whiteboard.*,com.boardwalk.collaboration.*,com.boardwalk.table.*" %> 
	

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>

<tr> 
  <td>

	<!--start main page table-->
	<table width="680" border="0" cellspacing="0" cellpadding="0" class="body">

	  <tr> 
	    <td valign="top">
		<!--start main page table 2 -->
		
	        
		<table width="500" border="0" cellspacing="0" cellpadding="0"  valign="top">
		<b>Boardwalk Wizards</b>
	         <br>
		    <tr valign="top" bgcolor="#FFCC66"> 
		      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
		    </tr>

		    <tr valign="top"> 
			<td class="body" align=center> 
			    <br>

			    <br>
			<table width="450" border="0" cellspacing="2" cellpadding="2" align="center" class="body">
			
				<!--start table for table-->
	<tr bgcolor="#fddeb9" height=20> 
		<td class="body"> <b>Wizard </b></td>
		<td class="body"> <b>Description </b></td>
		<td class="body"> <b>Template</b> </td>
		
		      </tr>


                            

                                            <tr  bgcolor="white" > 
                                              <td class="body">  
                                                <a href="adduser_wizard.jsp" > Neighborhood and User Wizard </a>
                                                </td>
                                              <td class="body">Description</td>
                                              <td class="body">Link to an Excel template</td>
                                              

            
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






