<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*, com.boardwalk.table.Columntype, com.boardwalk.collaboration.*,  java.io.*" %>
<%
int tableId = ((Integer)request.getAttribute("TableId")).intValue();
Vector columnTypes = (Vector)request.getAttribute("columnTypes");
String columnName = (String)request.getAttribute("columnName");
request.setAttribute("heading", "Add Column");
%>
<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<br>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


<form method="get" action="MyTables">
<tr>
<td>
<table width="773" border="0">
  <tr> 
    <td body="class" height="100%" valign="top"> 
      <div align="center">
        <table width="500" border="0" cellspacing="2" cellpadding="2" align="center">
          <tr body="class" valign="top"> 
            <td body="class">
              <table width="200" border="0" cellspacing="2" cellpadding="2" align="center">
                <tr body="class" > 
                  <td body="class" bgcolor="#eeeeee" width="180" height="30"> 
                    <input type="text" name="columnName" value="<%=columnName%>">
                  </td>
                </tr>
                <tr> 
                  <td body="class" bgcolor="white" width="180" height="30"> <b> 
                    <select name="columnType">
<%
// get the type of columns available
Iterator i = columnTypes.iterator();
while (i.hasNext()){
    Columntype type = (Columntype)i.next();
    String option = type.getType();   

%>
                        <option selected><%=option%></option>
<%
}// while column types
%>
                      </select>
                    </b> </td>
                </tr>
              </table>
              </b></td>
          </tr>
          <tr body="class" bgcolor="e7e7e7" valign="top"> 
            <td body="class" >  
              <div align="center"><font face="Verdana,Arial, Helvetica, sans-serif" size="2" color="#000000"><b> 
                <a href="javascript:setActionAndSubmit('commitColumnAndDone')">Done adding columns</a> | 
                <a href="javascript:setActionAndSubmit('commitColumnAndAddAnother')">Add another  Column</a> 
                </b></font></div>
            </td>
          </tr>
        </table>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
      </div>
    </td>
  </tr>
</table>
<input type="hidden" name="action" value="">
<%@include file='/jsp/common/commonparameters.jsp' %> </form>
<br>
</tr>
</td>
<%@include file='/jsp/common/footer.jsp' %>
