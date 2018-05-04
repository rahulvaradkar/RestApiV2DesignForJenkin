<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.whiteboard.*, com.boardwalk.table.*, java.io.*" %>

<%
Hashtable ht = (Hashtable)request.getAttribute("wbTables");
int   collabId   = ((Integer)request.getAttribute("collabId")).intValue();
int   baselineId = ((Integer)request.getAttribute("baselineId")).intValue(); 
String collabName = "To be Changed";
String collabPurpose = "To be Changed";
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
		<b>Whiteboard List</b>
		<br>
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                               
                            <br>
                        <table width="450" border="0" cellspacing="2" cellpadding="2" align="center" class="body">
                              <form name="CollabBaselineForm" method="get" action="Whiteboard">


                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20> 
       		<td class="body"> <b>Select </b></td>
                <td class="body"> <b>Whiteboard </b></td>

                <td class="body"> <b>Table</b> </td>
                      </tr>
<!-- Get all the tables in the database and list them here--> 

                


<%
     boolean grey = false;
	Enumeration wbs = ht.keys();
	while (wbs.hasMoreElements()) 
        {
	    Whiteboard w = (Whiteboard)wbs.nextElement();
	    int wbid = w.getId();
            String wbname =w.getName();
	    Vector tables = (Vector)ht.get(w);
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

                <tr bgcolor="<%=rowColorCode%>"> 
                  <td valign="top" width="30" rowspan="<%=tables.size()%>">
                    <input onClick="javascript:checkBoxCheck(this)" type="checkbox" name="wbid" value="<%=wbid%>">
                  </td>
                  <td valign="top" rowspan="<%=tables.size()%>"><a href="Whiteboard?baselineId=<%=baselineId%>&wbid=<%=wbid%>&action=openWhiteboardBaseline"><%=wbname%></a>
                    </font></td>
<%
// loop over the tables in the whiteboard
System.out.println("baseline table list size = " + tables.size() );


Iterator i = tables.iterator ();
int count=0;
while (i.hasNext()) {
   TableTreeNode t = (TableTreeNode)i.next();
   if (count != 0) {
%>

                <tr bgcolor="<%=rowColorCode%>"> 
<%
} // if count != 0
%>
                  <td><a href="MyTables?baselineId=<%=baselineId%>&tableId=<%=t.getId()%>&action=editTable&wbid=<%=wbid%>&ViewPreference=<%=t.getDefaultViewPreference()%>"><%=t.getName()%></a></td>
                </tr>
<%
    count++;
    } // end loop over tables
} // end loop over whiteboards
%>
                                                            
            <tr bgcolor="#cccccc"> 
                        <td class="body" colspan="3" align="center"> 
                          <b>
              
                
               <a href="javascript:checkCheckBoxsetActionAndSubmit('openWhiteboardBaseline')">Open </a>
                
                        </td>
            </tr> 
<input type="hidden" name="baselineId" value="<%=baselineId%>">	
<input type="hidden" name="action" value="editCollab">	
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


