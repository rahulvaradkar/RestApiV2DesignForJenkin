<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.table.*" %> 
	

<%
    Vector vtulc = (Vector)request.getAttribute("tablesUsingLkpColumn");
    String lkpColumnName = (String)request.getAttribute("lkpColumnName");
    int lkpColumnId = Integer.parseInt(request.getParameter("lkpColumnId"));
    String lkpValue = (String)request.getParameter("lkpValue");
    request.setAttribute("heading", "The following tables reference the column \"" + lkpColumnName + "\" for lookup");
    System.out.println("lookup columns for column I d" + lkpColumnId);
    System.out.println("lookup columns for vtulc size" + vtulc.size() );
    
  
    
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>

<tr> 
  <td>

	<!--start main page table-->
	<table width="680" border="0" cellspacing="0" cellpadding="0" align="center" class="body">

	  <tr> 
	    <td valign="top">
		<!--start main page table 2 -->
		<%@include file='/jsp/common/error_message.jsp' %>
 		
	        
		<table width="500" border="0" cellspacing="0" cellpadding="0" align="center" valign="top">
	         <br>
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
		<td class="body"> <b>Lookup Tables  </b></td>
	
		
		      </tr>
<!-- Get all the tables in the database and list them here--> 

                            
<%

if ( vtulc != null  )
{
	boolean grey = false;
	Iterator tables = vtulc.iterator();
	while (tables.hasNext())
	{
		TablesUsingLkpColumn table = (TablesUsingLkpColumn)tables.next();
		String name = table.getTableUsingLookup_Name();
		int id = table.getTableUsingLookup_Id();
		String description = table.getTableUsingLookup_Purpose();
		int columnUsingLookupId = table.getColumnUsingLookup();
		String columnUsingLookupName =  table.getColumnUsingLookupName();
		
		
		
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
		
		String href = "";
		String display = "";
		
		if ( lkpValue != null && !lkpValue.trim().equals("") )
		{
			href = "MyTables?tableId="+id+"&action=editTable"+"&cc"+columnUsingLookupId+"=equals:"+lkpValue;
			display = "Table "  + name + ":Where "+ columnUsingLookupName + " is equal to " +lkpValue;
		}
		else
		{
			href = "MyTables?tableId="+id+"&action=editTable";
			display = "Table "  + name ;
		}
		
%>


		<tr  bgcolor="<%=rowColorCode%>" > 
		<td class="body"><a href="<%=href%>" ><%=display%> </a></td>
		</tr>
	
<%
	} 
} 

%>                    
            
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






