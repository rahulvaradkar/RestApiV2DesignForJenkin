<%@ page isThreadSafe="false" %>
<%

int tableId = ((Integer)request.getAttribute("tableId")).intValue();
String tableName = (String)request.getAttribute("tableName");
String ViewPreference = (String)request.getAttribute("ViewPreference");

System.out.println("ViewPreference" + ViewPreference );

request.setAttribute("heading", tableName + " Table Administration");
	

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
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                                
                            <br>
                  <table width="500" border="0" cellspacing="2" cellpadding="2" align="center">
       <form method="post" action="MyTables?">
          <tr valign="top"> 
            <td>
              <table class="body" width="500" border="0" cellspacing="2" cellpadding="2">
               
               <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Adminstrative Controls for <a href="javascript:setActionAndSubmit('editTable')" ><%=tableName%> </a> </b></td>
                </tr>
                
                <tr bgcolor="#eeeeee"> 
                  <td  valign="center" class="OptionListTDField"  > 
			<a href="javascript:setActionAndSubmit('editTableAccess')" ><img src="images/security.gif" width="25" height="25">    Table Access Control</a> 
		 </td>
						  		
                  
                </tr>
                
                <!-- Hello
                
                <tr bgcolor="white"> 
                   <td  valign="center" class="OptionListTDField"  > 
		  	  <a href="javascript:setActionAndSubmit('editRowColumnAccess')"><img src="images/secure.jpg" width="25" height="25">   Row And Column Access Control</a> 
		 </td>
                </tr>
                 --> 
                
                <tr bgcolor="white"> 
		   <td  valign="center"  class="OptionListTDField"  > 
		  	<a valign="center"  href="javascript:setActionAndSubmit('editTableUIPreferences')"><img src="images/design.jpg" width="25" height="25">    Custom User Interface Preferences</a> 
		 </td>
		  
		  </tr>
		  
		  
              </table>
              </td>
          </tr>
          

<input type="hidden" name="tableId" value="<%=tableId%>">
<input type="hidden" name="tableName" value="<%=tableName%>">
<input type="hidden" name="action" value="editTableAccess">
<input type="hidden" name="ViewPreference" value="<%=ViewPreference%>">


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

<br>
<%@include file='/jsp/common/footer.jsp' %>

