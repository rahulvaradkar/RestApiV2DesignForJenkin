<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.neighborhood.*" %> 

<%
// get the wbid parameter
int wbid = Integer.parseInt(request.getParameter("wbid"));
//request.setAttribute("heading", "Create Table");

%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


<script LANGUAGE=JAVASCRIPT TYPE="TEXT/JAVASCRIPT">
function createTable(action) 
{
	var name = trim(document.forms[0].tableName.value);

	if(name == '')
	{
		alert("Please enter Name of the Table to be created");
		document.forms[0].tableName.focus();
		return;
	}
	else
	{
		document.forms[0].action.value = action;
		document.forms[0].submit();
	}
}

</script>

<tr> 
    <td>

        <!--start main page table-->
        <table border="0" cellspacing="0" cellpadding="0"  class="body">

          <tr> 
            <td valign="top">
                <!--start main page table 2 -->

		<table border="0" cellspacing="0" cellpadding="0"  valign="top">
		   <b>Create Table</b>
                    <tr valign="top" bgcolor="#FFCC66"> 
                      <td height="2" ><img src="images/clear.gif"  height="2"></td>
                    </tr>
				
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                                
                            <br>
                  <table border="0" cellspacing="2" cellpadding="2" align="center">
       <form method="post" action="MyTables?wbid=<%=wbid%>">
          <tr valign="top"> 
            <td>
              <table class="body" border="0" cellspacing="2" cellpadding="2">
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2"><b>Table</b></td>
                </tr>
                <tr bgcolor="#eeeeee"> 
                  <td width="200" valign="center" >
                    Name:</td>
                  <td >  
                    <input type="text" name="tableName" value="default">
                    </td>
                </tr>
                <tr bgcolor="white"> 
                  <td width="200" valign="center"> 
                    Description </td>
                  <td>  
                    <textarea name="tableDescr" rows="3" cols=70></textarea>
                    </td>
                </tr>
                       
              </table>
              <b> 
              </b></td>
          </tr>
   
          
          
          <tr bgcolor="white"> 
            <td> 
              <div align="center"><b> 
                <a href="javascript:createTable('commitTable')">Create </a></b></div>
            </td>
          </tr>
<input type="hidden" name="action" value="commitTable">
<input type="hidden" name="wbid" value="<%=wbid%>">
<input type="hidden" name="ViewPreference" value="LATEST" >
          <%@include file='/jsp/common/commonparameters.jsp' %> 
          
          
   </form>
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

