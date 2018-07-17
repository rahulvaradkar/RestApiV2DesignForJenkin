<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.neighborhood.*" %> 

<%

String result = (String)request.getAttribute("result");
String tableId =(String) request.getAttribute("tableId");
String url = "MyTables?action=editTable&tableId="+tableId+"&ViewPreference=LATEST";

// get the wbid parameter
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>



<script language="javascript">




</script>

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

          <tr valign="top"> 
            <td>
              
              <table class="body" border="0" cellspacing="2" cellpadding="2">
                
                <tr bgcolor="#fddeb9"> 
                  <td class="body"  colspan="2">
                  <b> Processed the table </b>
                  <a id="tableName"  href="<%=url%>" > User Table </a>
                  <b> with result: "<%=result%>" 		  
		  </td>
                </tr>                
       </table>       
       </td>
          </tr>
          
          
          

          <%@include file='/jsp/common/commonparameters.jsp' %> 
          
          

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

