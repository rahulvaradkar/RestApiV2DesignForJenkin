<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.whiteboard.*,com.boardwalk.table.*" %> 

<%
    int wbid = Integer.parseInt (request.getParameter("wbid"));
    int baselineId = Integer.parseInt (request.getParameter("baselineId"));

    // wb w = wbappl.getWbById(wbid);
    String wName = "Unknown Whiteboard To be changed";
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
		<b>Whiteboard Tables</b>
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                               
                            <br>
                        <table width="450" border="0" cellspacing="2" cellpadding="2" align="center" class="body">
                                <form method="get" action="MyTables?wbid=<%=wbid%>">
                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20> 
       		<td class="body"> <b>Select </b></td>
                <td class="body"> <b>Name </b></td>
                <td class="body"> <b>Description</b> </td>
                
                      </tr>
<!-- Get all the tables in the database and list them here--> 

                        


     

<%
boolean grey = false;
    Vector tableList = (Vector)request.getAttribute("tableList");
    Iterator tables = tableList.iterator();
    while (tables.hasNext()) {
	TableTreeNode table = (TableTreeNode)tables.next();
	String name = table.getName();
        String description = table.getPurpose();
	int id = table.getId();
                                             String rowColorCode = "white";
                                              if ( grey == true )
                                                {
                                                    rowColorCode = "#eeeeee";
                                                    grey = false;
                                                }
                                              else
                                                {
                                                    grey = true;
                                                }
                                    %>


                                            <tr  bgcolor="<%=rowColorCode%>" > 
                                              <td class="body">  
                                                <input type="checkbox" name="tableId" onClick="javascript:checkBoxCheck(this)" value="<%=id%>">
                                                </td>
                                              <td class="body"><a href="MyTables?baselineId=<%=baselineId%>&tableId=<%=id%>&action=editTable&wbid=<%=wbid%>&ViewPreference=<%=table.getDefaultViewPreference()%>"><%=name%></a></td>
                                              <td class="body"><%=description%></td>
                                            

                                            </tr>
                                        <%
                                        } // while there are more collaborations
                                        %>                    
            <tr bgcolor="#cccccc"> 
                        <td class="body" colspan="4" align="center"> 
                          <b>
                <a href="javascript:checkCheckBoxsetActionAndSubmit('openTableBaseline')">Open</a> 
                        </td>
            </tr> 
             <input type="hidden" name="baselineId" value="<%=baselineId%>">
             <input type="hidden" name="action" value="openTableBaseline">
             
             
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
  <tr bgcolor="#000000"> 
    <td colspan="2" height="10"><img src="images/clear.gif" width="2" height="10"></td>
  </tr>
<br>
<%@include file='/jsp/common/footer.jsp' %>



