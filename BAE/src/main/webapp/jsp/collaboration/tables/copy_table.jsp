<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.neighborhood.*" %> 

<%
// get the wbid parameter




TableInfo tbi =(TableInfo)request.getAttribute("tableInfo");
TableAccessList tbACL =  (TableAccessList)request.getAttribute("tableAccessList");


int wbid = tbi.getWhiteboardId();
String collabName = tbi.getCollaborationName();
String  wbName = tbi.getWhiteboardName();
int source_table_id = tbi.getTableId();
String  tableDescr = tbi.getTablePurpose();
String  tableName = tbi.getTableName();
String  ViewPreference = tbi.getTableDefaultViewPreference();
int baselineId= ((Integer)request.getAttribute("baselineId")).intValue();



tableName = "Copy of  " + tableName;




request.setAttribute("heading", "Copy Table");

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
                  <td class="body"  colspan="2"><b>Copy Table To</b>
			&nbsp;
			       <a id="copyToWbURL" href="Whiteboard?wbid=<%=wbid%>"> <%=collabName%>:<%=wbName%>		</a>
			       &nbsp;
	<%
		if ( false )
		{
	%>
		
			<a href="javascript:selectWbForCopy( )" ><img  src="images/open.jpg" height=21></a>
	<%
	}
	%>
	
			<input type="hidden" name="wbid" value=<%=wbid%> >
		</td>
		 </tr>  
                
                
                
                
                
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Table</b></td>
                </tr>
                
                
                
                <tr bgcolor="#eeeeee"> 
                  <td width="200" valign="center" >
                    Name:</td>
                  <td >  
                    <input type="text" name="tableName" value="<%=tableName%>">
                    </td>
                </tr>
                <tr bgcolor="white"> 
                  <td width="200" valign="center"> 
                    Description </td>
                  <td>  
                    <textarea name="tableDescr" rows="3" cols=70><%=tableDescr%> </textarea>
                    </td>
       </table>       
       <table>
                    
                </tr>
                       <tr align="left" class="AccessTable"  bgcolor="white"> 
				  <td > 
				     Table Type  &nbsp;&nbsp;
				     </td>
				     <td>
				     <%
				     	if ( ViewPreference.equals("LOOKUP") )
				     	{
				     	%>
				     		Lookup Table
				     <%
				     	
				     	}
				     	else
				     	{
				     		%> 
				     		Collaborative Table <%
				     	
				     	}
				     	%>
				  
				      </td>
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
						    <%
						    	
							if ( tbACL.canReadLatestOfTable() )
							{
							
							%>
								checked
							<%
							}
							else
							{
								%>
										disabled
								<%
							
							}

						    
						    %>
						    
						    >							     
					    </td>
		  </tr>
		  
		   <tr align="left" class="AccessTable"  bgcolor="white"> 
		  					  <td > 
		  					     Copy Deactivated Content &nbsp;&nbsp;
		  					     				     </td>
		  				     <td>
		  						    <input onclick="if(!document.all.copyStructure.checked){alert('Cannot copy latest content w/o structure');this.checked=false}"
		  						    id="copyDeactivatedContent" type="checkbox" name="copyDeactivatedContent"   
		  						    <%
		  						    	
		  							if ( tbACL.canReadLatestOfTable() )
		  							{
		  							
		  							%>
		  								checked
		  							<%
		  							}
		  							else
		  							{
		  								%>
		  										disabled
		  								<%
		  							
		  							}
		  
		  						    
		  						    %>
		  						    
		  						    >							     
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
                
                
                
                
                 
		<input type="hidden" name="ViewPreference" value="LATEST"  >					     
		<input type="hidden" name="source_table_id" value=<%=source_table_id%>  >
		<input type="hidden" name="baselineId" value=<%=baselineId%>  >
		
		

              <b> 
              </b></td>
          </tr>
   
          
          
          <tr bgcolor="white"> 
            <td> 
              <div align="center"><b> 
                <a href="javascript:setActionAndSubmit('commitCopyTable')">Copy Table</a></b></div>
            </td>
          </tr>
<input type="hidden" name="action" value="commitCopyTable">

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

<br>
<%@include file='/jsp/common/footer.jsp' %>

