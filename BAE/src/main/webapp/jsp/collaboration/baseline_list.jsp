<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.collaboration.*, com.boardwalk.table.Baseline,java.io.*" %>

<%

int collabId = ((Integer)request.getAttribute("collabId")).intValue();
Vector BaselineList = (Vector)request.getAttribute("baselineList");
Collaboration collab = (Collaboration)request.getAttribute("collaboration");

Iterator ii = BaselineList.iterator();
while (ii.hasNext()) {
	Object o = ii.next();
	System.out.println("CLASS-NAME:::::::::::"+o.getClass().toString());
}


%>


<script language="javascript">
<%@ include file="/jscript/dateutil.js" %>


</script>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


<tr>
    <td>
        <!--start main page table-->
        <table border="0" cellspacing="0" cellpadding="0" align="left" class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->

		<table border="0" cellspacing="0" cellpadding="0" align="left" valign="top">
		<b>Baselines for Collaboration: <%=collab.getName()%> </b>

                    <tr valign="top" bgcolor="#FFCC66">
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>

                    <tr valign="top">
                        <td class="body" align=center>
                            <br>

                            <br>
                        <table  border="0" cellspacing="2" cellpadding="2" align="center" class="body">
                                <form name="BaselineListForm" method="get" action="MyCollaborations">

                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20>
       		<td body="class" width="30">Select</td>
                  <td body="class" >Name</td>
                  <td body="class" >Short Description</td>
                  <td body="class" >Created By</td>
                  <td body="class" >Created On</td>
                      </tr>


                                    <%
                                    boolean grey = false;

                                    Iterator i = BaselineList.iterator();
                                    while (i.hasNext()) {
                                            Baseline b = (Baseline)i.next();
                                            int id = b.getId();
                                            String name = b.getName();
                                            String desc = b.getDescription();
                                    		String createdBy = b.getCreatedBy();
                                    		java.sql.Timestamp t = b.getCreatedOn();
                                             String rowColorCode = "silver";
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

                                               <td>
                    <input onClick="javascript:checkBoxCheck(this)" type="checkbox" name="baselineId" value="<%=id%>">
                  </td>
                  <td><a href="MyCollaborations?baselineId=<%=id%>&action=openCollabBaseline&collabId=<%=collabId%>"><%=name%></a></td>
                  <td><%=desc%></td>
                  <td><%=createdBy%></td>
                  <td id="bas<%=id%>" ></td>
                  <script language="javascript">
						  var d = new Date(<%=t.getTime()%>)
						  bas<%=id%>.innerText = formatDateTime(d,"NNN dd, yyyy hh:mm:ssa")
				 </script>

                                            </tr>
                                        <%
                                        } // while there are more collaborations
                                        %>
            <tr bgcolor="#cccccc">
                        <td class="body" colspan="6" align="center">
                          <b>

               <a href="javascript:checkCheckBoxsetActionAndSubmit('openCollabBaseline')">Open</a>
		|      <a href="javascript:checkCheckBoxsetActionAndSubmit('removeCollabBaseline')">Delete</a>


                        </td>
            </tr>
<input type="hidden" name="action" value="editCollab">
<input type="hidden" name="collabId" value="<%=collabId%>">
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




