<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.neighborhood.*" %> 

<%
// get the wbid parameter

System.out.println("Assign Row Ownership Form");
String tableId = request.getParameter("tableId");
String wbid = request.getParameter("wbid");
String ViewPreference = request.getParameter("ViewPreference");
String designValues = request.getParameter("designValues");

String[] rowIds = (String[])request.getAttribute("rowId");

request.setAttribute("heading", "Assign Row Ownership");


%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


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
																						       <form method="post" action="MyTables?">
																							  <tr valign="top"> 
																							    <td>






																									      <table class="body" border="0" cellspacing="2" cellpadding="2">

																										<tr bgcolor="#fddeb9"> 
																										  <td class="body"  colspan="2"><b>Assign Ownership To</b>
																										</td>
																										 </tr>  




																										  <tr align="left" class="InputBox"  bgcolor="white"> 
																															  <td > 
																															  User Email Address &nbsp;&nbsp;
																																			     </td>
																														     <td>
																																    <input type="text" name="emailAddress"  >							     
																															    </td>
																												</tr>


																										</table>      


																					<input type="hidden" name="ViewPreference" value=<%=ViewPreference%>  >					     
																					<input type="hidden" name="tableId" value=<%=tableId%>  >
																					<input type="hidden" name="designValues" value=<%=designValues%>  >
																					<input type="hidden" name="wbid" value=<%=wbid%> >
																						<input type="hidden" name="action" value="ChangeRowOwnership">
																					<%
																					
																					if ( rowIds.length > 0 )
																					{
																						for ( int r = 0; r < rowIds.length; r++ )
																						{
																						
																					%>	
																						
																					<input type="hidden" name="rowId" value="<%=rowIds[r]%>" >
																					
																					<%	
																						}
																						
																					}
																					%>	
																					

																				      <b> 
																				      </b>
																				      </td>
																				  </tr>



																				  <tr bgcolor="white"> 
																				    <td> 
																				      <div align="center"><b> 
																					<a href="javascript:setActionAndSubmit('ChangeRowOwnership')">Assign Ownership</a></b></div>
																				    </td>
																				  </tr>



																				  <%@include file='/jsp/common/commonparameters.jsp' %> </form>

																				</table>        
																				<!--end table for table-->
				</td>
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

