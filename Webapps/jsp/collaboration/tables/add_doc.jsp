<%@ page isThreadSafe="false" %>
<%@ page import ="com.boardwalk.exception.*, java.io.*, java.util.*" %>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<%
	int selectedCellId = Integer.parseInt(request.getParameter("selectedCellId"));
	int tableId = Integer.parseInt(request.getParameter("tableId"));
	int wbid = Integer.parseInt(request.getParameter("wbid"));
	String ViewPreference = request.getParameter("ViewPreference");
%>
<tr>
    <td>

        <!--start main page table-->
        <table  border="0" cellspacing="0" cellpadding="0" class="body">
        <div class=body><b>Add Document</b></div>
          <tr>
            <td valign="top">
                <!--start main page table 2 -->

        <table  border="0" cellspacing="0" cellpadding="0" align="center" valign="top">
                    <tr valign="top" bgcolor="#FFCC66">
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>

                    <tr valign="top">
                        <td class="body" align=center>
                            <br>

                            <br>
                  <table  border="0" cellspacing="2" cellpadding="2" >
					 <FORM  NAME="addDocument" ENCTYPE="multipart/form-data"
					 		ACTION="MyTables"
					 		METHOD=POST>
					 <TABLE BORDER=0 CELLPADDING=1>
					 <tr>
					   <td> Text to display </td>
					   <td colspan=3>
					     <input type=text name="textToDisplay" size=50>
					   </td>
					 </tr>
					 <tr>
					   <td> Screen Tip </td>
					   <td colspan=3>
					     <input type=text name="screenTip" size=50>
					   </td>
					 </tr>
					 <TR>
					 <TD> File Name </TD>
					 <TD colspan=2>
					 <input type="hidden" name="action" value="commitDocumentToCell">
					 <input type="hidden" name="selectedCellId" value="<%=selectedCellId%>">
					 <input type="hidden" name="tableId" value="<%=tableId%>">
					 <input type="hidden" name="wbid" value="<%=wbid%>">
					 <input type="hidden" name="ViewPreference" value="<%=ViewPreference%>">

					 <INPUT TYPE=FILE NAME="uploadfile" size=36></TD>
					 <td></td>

					 </TR>
					 </TABLE>
					 <br>
					 <INPUT TYPE="submit" VALUE="Continue">
				 </FORM>
        </table>

            <td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td>
        <!--end table for table-->
        </tr>
     </table>
              <!--end table for main page table 2 -->


            </td>
   </tr><%@include file='/jsp/common/footer.jsp' %>
        </table>

        <!--start main page table -->
    </td>

  </tr>

