<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.*,java.util.*, com.boardwalk.collaboration.*,  java.io.*" %>

<%
System.out.println("Inside the collablist.jsp");
Vector collabList = (Vector)request.getAttribute("collabList");
//String userName = (String)session.getAttribute("userName");
//Vector memberNhList = (Vector)request.getAttribute("memberNhList");
Vector memberNhList = new Vector();
request.setAttribute("heading", "Collaboration List");
if (memberNhList != null) {
Iterator nhIter = memberNhList.iterator();
if (nhIter.hasNext()) {
    String nhName = (String)nhIter.next();
}// while
}//if

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
                            
                        <table width="450" border="0" cellspacing="2" cellpadding="2" align="center" class="body">
                                <form name="CollabListForm" method="get" action="MyCollaborations">
                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20> 
       		<td class="body"> <b>Select </b></td>
                <td class="body"> <b>Name </b></td>
                <td class="body"> <b>Description</b> </td>
                      </tr>

                            
                                    <%
                                    Iterator i = collabList.iterator();
                                    boolean grey = false;
                                             
                                    while (i.hasNext()) {
                                            Collaboration c = (Collaboration)i.next();
                                            int id = c.getId();
                                            String name = c.getName();
                                            String desc = c.getPurpose();

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
                                            
                                                <td class="body"><input type="checkbox" onClick="javascript:checkBoxCheck(this)" name="collabId" value="<%=id%>"> </td>
                                                <td class="body"><a href="MyCollaborations?action=editCollab&collabId=<%=id%>"><%=name%> </a> </td>
                                                <td class="body"><%=desc%> </td>
                                            </tr>
                                        <%
                                        } // while there are more collaborations
                                        %>                    
            <tr bgcolor="#cccccc"> 
                        <td class="body" colspan="4" align="center"> 
                          <b>

                <a href="javascript:setActionAndSubmit('createCollab')">New Collaboration</a> |
             <!--   <a href="javascript:checkCheckBoxsetActionAndSubmit('bwsFormat')"> Link To Excel  </a> |
		<a href="javascript:checkCheckBoxsetActionAndSubmit('copyCollab')">Copy </a> | --> 
                <a href="javascript:checkCheckBoxsetActionAndSubmit('editCollab')">Edit </a> |
                <a href="javascript:checkCheckBoxsetActionAndSubmit('showBaselineList')">Show Baselines </a> | 
                <a href="javascript:checkCheckBoxsetActionAndSubmit('removeCollab')">Delete </a> 

                        </td>
            </tr> 
            <input type="hidden" name="action" value="editCollab">	
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



