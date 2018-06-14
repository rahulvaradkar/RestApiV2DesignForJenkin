<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.user.NewUser, java.io.*, java.util.*" %>

<%
	NewUser nu = (NewUser)request.getAttribute("UserProfile");
	request.setAttribute("heading", "User Profile");
%>


<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>
<script>
	function updateProfile()
	{
		document.forms[0].action.value="updateProfile";
		document.forms[0].submit();
	}
	function changePassword()
	{
		document.forms[0].action.value="changePassword";
		document.forms[0].submit();
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
			  	First Name
			</td>
			<td valign="top">
				<input  type="test" name="firstName" value="<%=nu.getFirstName()%>">
			</td>
        </tr>

		<tr bgcolor="#eeeeee" valign="top" align="left" >
			<td valign="top">
			  	Last Name
			</td>
			<td valign="top">
				<input  type="test" name="lastName" value="<%=nu.getLastName()%>">
			</td>
        </tr>

        <tr bgcolor="#eeeeee" valign="top" align="left" >
					<td valign="top">
					  	Email Address
					</td>
					<td valign="top" >
						<input  type="test" name="emailAddress" value="<%=nu.getAddress()%>">
					</td>
        </tr>

        <tr bgcolor="#eeeeee" valign="top" align="left" >
					<td valign="top">
					  	Alias
					</td>
					<td valign="top" >
						<input  type="test" name="alias" value="<%=nu.getExternalUserId()%>">
					</td>
        </tr>

  		<tr bgcolor="#cccccc" >
			<td class="body" colspan="2" align="left">
			  <a href="javascript:updateProfile()">Update Profile</a> |
			  <a href="javascript:changePassword()">Change Password</a>
			</td>
         </tr> <input type="hidden" name="action" value="">

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



