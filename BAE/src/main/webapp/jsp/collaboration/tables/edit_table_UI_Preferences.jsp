<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.neighborhood.*" %> 

<%
	// get the wbid parameter
	
	int tableId = ((Integer)request.getAttribute("tableId")).intValue();
	String tableName = (String)request.getAttribute("tableName");

	request.setAttribute("heading", tableName + " User Interface Preferences ");
	
	Hashtable UIPreferences =(Hashtable) request.getAttribute("UIpreferences");
	System.out.println("UIPreferences size " + UIPreferences.size() );

	String  tabledescription =(String) request.getAttribute("tabledescription");
	System.out.println("tabledescription  " + tabledescription );
	
	String ViewPreference = (String)request.getAttribute("ViewPreference");	
	System.out.println("ViewPreference" + ViewPreference );
	
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
       <form method="post" action="MyTables?tableid=<%=tableId%>">
          <tr valign="top"> 
            <td>
              <table class="body" border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Customize Table Page</b></td>
                </tr>
                </table>
              <b> 
              </b></td>
          </tr>
          
          
           <tr valign="top"> 
	              <td>
	                <table class="body" border="0" cellspacing="2" cellpadding="2">
	                <tr bgcolor="#fddeb9"> 
			                  <td class="body"  colspan="2"><b>Edit Description</b></td>
                	</tr>
                	<tr>
	                  	<td width="200" valign="top"> 
			                      Description </td>
			                    <td>  
			                      <textarea name="tableDescr" rows="3" cols="40"><%=tabledescription%></textarea>
                    		</td>
                    	</tr>
	                  </table>
	                <b> 
	                </b></td>
          </tr>
          
          
          <tr bgcolor="#fddeb9"> 
	  			                  <td class="body"  colspan="2"><b>Edit Action Strings</b></td>
                	</tr>
   
          <tr bgcolor="#eeeeee" valign="top"> 
            <td>
              <table class="AccessTable" border="0" cellspacing="2" cellpadding="2" align="left" valign="top" >
                <tr> 
                  <td><b>Action</b></td>
                  <td><b>Personalized value</b></td>                  
                </tr>
                <%
                
                	if ( UIPreferences.size() > 0 )
                	{
                	
                		Enumeration actionNames = UIPreferences.keys();
                	
                		while ( actionNames.hasMoreElements() )
                		{
                	
                				TableActionUIPreference tbUIP= (TableActionUIPreference)UIPreferences.get((String)actionNames.nextElement());
                				String action = tbUIP.getAction();
                				String actionUIPreference = tbUIP.getActionUIString();
                				int actionUI_Id = tbUIP.getId();
                				int actionId = tbUIP.getActionId();
                				tbUIP.print();
                				if ( actionUI_Id == -1 )
                				{
                					actionUIPreference = (String)tbUIP.getActionToDefaultValues().get(action);
                					
                				
                				}
                				
                				System.out.println(" actionId " + actionId);
                				System.out.println(" actionUIPreference " + actionUIPreference);
                				
                				
                				
                				
                				
                				
                			%>
                			                			
                				<tr> 
                						 <input type="hidden" name=<%=action%>UI_id value="<%=actionUI_Id%>"></input>  
                						 <input type="hidden" name=<%=action%>id  value="<%=actionId%>"></input>                  						 
								  <td><b><%=action%></b></td>
								  <td><input type="text" name=<%=action%>UI_String  value="<%=actionUIPreference%>"></input</td>                  
                			      </tr>
		<%

					}

			}

		%>
   		
              
              </table>
              <p>&nbsp;</p>
            </td>
          </tr>
          <tr bgcolor="white"> 
            <td> 
              <div align="center"><b> 
                <a href="javascript:setActionAndSubmit('editTableAdmin')"><img src="images/admin1.jpg" width="15" height="15" > Admin </a>    |             
                <a href="javascript:setActionAndSubmit('commitTableUIPreferences')"><img src="images/design.jpg" width="15" height="15">  Save</a>
                
                
                </b></div>
            </td>
          </tr>
<input type="hidden" name="action" value="commitTableUiPreferences">
<input type="hidden" name="tableId" value="<%=tableId%>">
<input type="hidden" name="tableName" value="<%=tableName%>">
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
  <tr bgcolor="#000000"> 
    <td colspan="2" height="10"><img src="images/clear.gif" width="2" height="10"></td>
  </tr>
<br>
<%@include file='/jsp/common/footer.jsp' %>

