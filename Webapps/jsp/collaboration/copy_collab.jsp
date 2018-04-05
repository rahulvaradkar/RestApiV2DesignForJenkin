<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, java.io.*" %>
<%
int   origCollabId   = ((Integer)request.getAttribute("origCollabId")).intValue();
//request.setAttribute("heading", "Copy Collaboration");
%>


<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<tr> 
    <td>

        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0" class="body">
	<b>Copy collaboration</b>
          <tr> 
            <td valign="top">
                <!--start main page table 2 -->

		<table border="0" cellspacing="0" cellpadding="0"  valign="top">
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" width="500">
                      <img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                            <br>                    
                  <table  border="0" cellspacing="2" cellpadding="2" align="center">
     <form name="x" method="post" action="MyCollaborations">
          <tr valign="top"> 
            <td>
              <table class="body" border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Collaboration</b></td>
                </tr>
                
                
                <tr bgcolor="white"> 
                  <td>Name</td>
                  <td valign="top">
                    <input type="text" name="collabName" value="default">
                    </td>
                   </tr>
                  <tr>
                  <td width="300">Short Description</td>
                  <td valign="top">
                    <input type="text" name="collabDesc" value="default" size="50">                    
		    </td>
		    <tr>
		    
                 
                
                 <tr bgcolor="#fddeb9"> 
		                  <td class="body"  colspan="2"><b>Depending on the Table Access </b></td>
                </tr>
                
                 <tr align="left" class="AccessTable"  bgcolor="white"> 
					  <td > 
					     Copy Structure &nbsp;&nbsp;
					     				     </td>
						     <td>
						    <input id="copyStructure"  onclick="if(!this.checked) {document.all.copyLatestContent.checked=false;document.all.copyDesignValues.checked=false;}"
						    type="checkbox"  name="copyStructure"   checked >							     
					    </td>
		                </tr>
		                
		                
		                <tr align="left" class="AccessTable"  bgcolor="white"> 
							  <td > 
							     Copy Access &nbsp;&nbsp;
							     				     </td>
						     <td>
								    <input type="checkbox" name="copyAccess"  checked  >							     
							    </td>
				                </tr>
		                
		                
		                <tr align="left" class="AccessTable"  bgcolor="white"> 
							  <td > 
							     Copy Latest Content &nbsp;&nbsp;
							     				     </td>
						     <td>
								    <input onclick="if(!document.all.copyStructure.checked){alert('Cannot copy latest content w/o structure');this.checked=false}"
								    id="copyLatestContent" type="checkbox" name="copyLatestContent"   
								    checked				     
							    </td>
				                </tr>
		                
		                <tr align="left" class="AccessTable"  bgcolor="white"> 
							  <td > 
							  Copy User Interface Preferences &nbsp;&nbsp;
							  				     </td>
						     <td>
								    <input type="checkbox" name="copyUIPreferences"  checked >							     
							    </td>
				                </tr>
		                
		                <tr align="left" class="AccessTable"  bgcolor="white"> 
							  <td > 
							     Copy Design Values &nbsp;&nbsp;
							     				     </td>
						     <td>
								    <input onclick="if(!document.all.copyStructure.checked){alert('Cannot copy design values w/o structure');this.checked=false}" 
								    id="copyDesignValues" type="checkbox" name="copyDesignValues"     checked >							     
							    </td>
		                </tr>
              </table>
              <p>&nbsp;</p>
            </td>
          </tr>
          <tr bgcolor="white"> 
            <td> 
              <div align="center"><b> 
                <a href="javascript:setActionAndSubmit('commitCopy')">Copy</a></b></div>
            </td>
          </tr>
<input type="hidden" name="origCollabId" value="<%=origCollabId%>">
<input type="hidden" name="action" value="commitCopy">
                    
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

