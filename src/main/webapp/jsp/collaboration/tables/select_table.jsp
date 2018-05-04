<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, java.io.*, java.util.*, com.boardwalk.whiteboard.Whiteboard, com.boardwalk.table.Table" %> 

<%
int tableId = Integer.parseInt(request.getParameter("tableId"));
int rowId = Integer.parseInt(request.getParameter("rowId"));
String cellForUpdateFieldName = request.getParameter("cellForUpdateFieldName");
String cellForUpdateField = request.getParameter("cellForUpdateField");

Hashtable ht = (Hashtable)request.getAttribute("wbTables");
request.setAttribute("heading", "Link Cell to Table");
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>

<%@include file='/jsp/common/error_message.jsp' %>
<script language="javascript">

    function setTableAndSubmit(tableId,tableName,viewPreference ) {
    	  
    	  dirtyCellid = "dirtyCell"+"<%=cellForUpdateField%>"
    	  inputCellid = "Cell"+"<%=cellForUpdateField%>"
    	  prevCellid  = "pCell"+"<%=cellForUpdateField%>"
    	  browseCellId = "browse"+"<%=cellForUpdateField%>"
    	  visibleTableName = ""
    	  
    	  
    	  if ( opener.document.all.item(prevCellid).value != tableId )
    	  {
    	  
    	  
		  if ( tableName.length > 8 )
		  {
			visibleTableName = tableName.substring(0,7)+".."
		  }
		  else
		  {
			visibleTableName = tableName
		  }




		  opener.document.all.item("<%=cellForUpdateField%>").innerHTML =visibleTableName
		  opener.document.all.item("<%=cellForUpdateField%>").href="MyTables?tableId="+tableId+"&action=editTable&ViewPreference="+viewPreference;

		  opener.document.all.item(dirtyCellid).value=true
		  opener.document.all.item(inputCellid).value=tableId
		  opener.cellDirtyFlag = true
	   }
	   else
	   {
	   	opener.document.all.item(dirtyCellid).value=false
	   }
	 self.close()

    }
    
    
    function resetTableAndSubmit() {
        	  
        	  dirtyCellid = "dirtyCell"+"<%=cellForUpdateField%>"
        	  inputCellid = "Cell"+"<%=cellForUpdateField%>"
        	  browseCellId = "browse"+"<%=cellForUpdateField%>"
        	  prevCellid  = "pCell"+"<%=cellForUpdateField%>"
        	  
        	  visibleTableName = "Not Set"
        	  
        	if ( opener.document.all.item(prevCellid).value != -1 )
    	  	{
			  opener.document.all.item("<%=cellForUpdateField%>").innerHTML =visibleTableName
			  opener.document.all.item("<%=cellForUpdateField%>").href="javascript:selectTableForCell('<%=cellForUpdateField%>', 'cellName<%=cellForUpdateField%>')"
			  opener.document.all.item(dirtyCellid).value=true
			  opener.document.all.item(inputCellid).value=-1
			  opener.cellDirtyFlag = true
    	  	  }
    	  	 
    	  	  self.close()
        }
        
        function closeWindow() {
	        	  
	        	  self.close()
        }


</script>




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
                        <table width="450" border="0" cellspacing="2" cellpadding="2" align="center" class="body">
                              <form method="post" action="MyTables">


                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20> 
       		<td class="body"> <b>Whiteboard/Folders </b></td>
                <td class="body"> <b>Tables </b></td>

                      </tr>

<!-- Get all the tables in the database and list them here--> 

                
<%
    Enumeration wbs = ht.keys();
    while (wbs.hasMoreElements()) {
	Whiteboard w = (Whiteboard)wbs.nextElement();
	int wbid = w.getId();
	String wbname =w.getName();
	Vector tables = (Vector)ht.get(w);
%>
                <tr bgcolor="#eeeeee"> 
                  <td valign="top" rowspan="<%=tables.size()%>"><%=wbname%></td>

<%
// loop over the tables in the whiteboard
Iterator i = tables.iterator ();
int count=0;
while (i.hasNext()) {
   Table t = (Table)i.next();
if (count != 0){ 
%>
		  
    <tr bgcolor="#eeeeee"> 
<%
 } // end if 
%>
	  <td>
	 
	  <a href="javascript:setTableAndSubmit('<%=t.getId()%>','<%=t.getName()%>','<%=t.getDefaultViewPreference()%>')"><%=t.getName()%></a>
	  </td>
	  </tr>
<%
	count++;
    }
} 
%>                                                            
            <tr bgcolor="#cccccc"> 
                        <td class="body" colspan="4" align="center"> 
                          <b>
                          		<a href="javascript:resetTableAndSubmit()">Reset Table Cell </a> |
             				 <a href="javascript:closeWindow()">Close Window</a>
                
                        </td>
            </tr> 
<input type="hidden" name="rowId" value="<%=rowId%>">
<input type="hidden" name="tableId" value="<%=tableId%>">
<input type="hidden" name="selectedTable" value="">
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



















