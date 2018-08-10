<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, com.boardwalk.user.NewUser, java.io.*, java.util.*,com.boardwalk.exception.*,com.boardwalk.user.*,com.boardwalk.database.*,java.sql.*" %>

<%
	request.setAttribute("heading", "Password Change");

	Connection				connectionJsp	= null;	
	String					emailAddress	= request.getParameter("emailAddress");
	HashMap<String,String>	result			= new HashMap<String,String>();

	try
	{
		System.out.println("Inside change_password.jsp");

		DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
		connectionJsp = databaseloader.getConnection();
	    result = UserManager.getSystemConfiguration(connectionJsp);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<script>
	function commitPassword()
	{
		var newPassword = document.forms[0].newPassword.value;
		//alert("Password entered is : " + newPassword);

		if (newPassword == document.forms[0].retypeNewPassword.value)
		{
			var minimumPasswordLength		= "<%=result.get("Minimum Password Length")%>";
			var specialCharacterRequired	= "<%=result.get("Special Character Required")%>";
			var numberRequired				= "<%=result.get("Number Required")%>";
			var upperCaseRequired			= "<%=result.get("Upper Case Required")%>";
			var lowerCaseRequired			= "<%=result.get("Lower Case Required")%>";
			var restrictRepeatCharacters	= "<%=result.get("Restrict Repeat Characters")%>";
			var allowUserNameInPassword		= "<%=result.get("Allow User Name in Password")%>";
			var emailAddress				= "<%=request.getParameter("emailAddress")%>";

			//Check for Minimum Length
			if (minimumPasswordLength > 0){
				if (newPassword.length < minimumPasswordLength){
					alert("The length of new password must be atleast " + minimumPasswordLength + ". Please try again.");
					return;
				}
			}
			
			//Check for Special Characters
			if (specialCharacterRequired == "Y"){
				var pattern = new RegExp(/[~.`!@#$%\^&*+=_\-\[\]\\';,/{}|\\":<>\?]/);

				if (!pattern.test(newPassword)) {
					alert("There must be at least one Special Character in the new password. Please try again.");
					return;
				}
			}
			
			//Check for Numeric Characters
			if (numberRequired == "Y"){
				if (newPassword.match(/\d+/g) == null){
					alert("There must be at least one Numeric Character in the new password. Please try again.");
					return;
				}
			}
			
			//Check for Upper Case Characters
			if (upperCaseRequired == "Y"){
				if (newPassword.match(/[A-Z]/) == null){
					alert("There must be at least one Upper Case Character in the new password. Please try again.");
					return;
				}
			}

			//Check for Lower Case Characters
			if (lowerCaseRequired == "Y"){
				if (newPassword.match(/[a-z]/) == null){
					alert("There must be at least one Lower Case Character in the new password. Please try again.");
					return;
				}
			}
			
			//Check for Repeated Characters
			if (restrictRepeatCharacters == "Y"){
				for (var i=0; i<newPassword.length-1; i++){
					if(newPassword.charAt(i) == newPassword.charAt(i+1)){
						alert("Repeated Characters are not allowed in the new password. Please try again.");
						return;
					}
				}
			}
			
			//Check if Password is Matching with User Name
			if (allowUserNameInPassword == "N"){
				if(emailAddress.includes(newPassword)){
					alert("Any part of your User Name is not allowed in the new password. Please try again.");
					return;
				}
			}

			document.forms[0].action.value="commitPassword";
			document.forms[0].submit();
		}
		else
		{
			alert(" The retyped password does not match the new password you entered.");
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
