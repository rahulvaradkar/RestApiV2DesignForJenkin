<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.user.NewUser, java.io.*, java.util.*" %>

<%
	request.setAttribute("heading", "Password Change");
%>


<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<script>
	function commitPassword()
	{
		if (document.forms[0].newPassword.value == document.forms[0].retypeNewPassword.value)
		{
			document.forms[0].action.value="commitPassword";
			document.forms[0].submit();
		}
		else
		{
			alert(" The retyped password does not match the new password you entered");
		}
	}
</script>
 <form method="get" action="BW_Users">
 <tr>

    <td>
        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0" align="left" class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->
				<table width="500" border="0" cellspacing="0" cellpadding="0" align="left" valign="top">
                    <tr valign="left" bgcolor="#FFCC66">
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>
                    <tr valign="left">
                        <td class="body" align="left">
                            <br>
                            <br>


   <table border="0" cellspacing="2" cellpadding="2" align="center" class="body">
       <tr bgcolor="#eeeeee" valign="top" align="left" >
			<td valign="top">
			  	Old Password
			</td>
			<td valign="top">
				<input  type="password" name="oldPassword" value="">
			</td>
        </tr>

		<tr bgcolor="#eeeeee" valign="top" align="left" >
			<td valign="top">
			  	New Password
			</td>
			<td valign="top">
				<input  type="password" name="newPassword" value="">
			</td>
        </tr>

        <tr bgcolor="#eeeeee" valign="top" align="left" >
			<td valign="top">
				Retype New Password
			</td>
			<td valign="top" >
				<input  type="password" name="retypeNewPassword" value="">
			</td>
        </tr>

  		<tr bgcolor="#cccccc" >
			<td class="body" colspan="2" align="left">
			  <a href="javascript:commitPassword()">Submit</a>
			</td>
         </tr>
         <input type="hidden" name="action" value="commitPassword">

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



