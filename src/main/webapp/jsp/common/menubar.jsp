
<!-- start masthead table-->
<tr>
<td>
<table  border="0" cellspacing="0" cellpadding="0" class="body">

<tr>
	<td colspan="3" height="10" bgcolor="#FF8C00"><img src="images/clear.gif" height="5"></td>

</tr>

<tr>
	<td colspan="3" height="3" bgcolor="#ffffff"><img src="images/clear.gif"  height="3"></td>

</tr>
<tr>
	<td><img src="images/logo-boardwalk.gif"></td>
	<td width="500" bgcolor="#CCCCCC" align="center" valign="center" class="body">
<img src="images/clear.gif"  height="1" width="500">
	<a href="MyCollaborations">My Collaborations </a> <b>|</b>
	<a href="BW_Neighborhoods">Neighborhoods</a> <b>|</b>
	<a href="javascript:invokeInvitationManager()">Templates</a> <b>|</b>
	<a href="javascript:popupEmailClient('', '<%=userName%>', '', 'Boardwalk Message', 'Please click on this URL to access Boardwalk : ' )">Email </a> <b>|</b>
	<a href="javascript:redirectPath(location.pathname)">Logout</a> <b>|</b> <!--modified by shirish on 2/29/08-->
	<a href="wizard_list.jsp">Wizards</a> <b>|</b>
	<a href="javascript:popupAboutUs()">About us</a>
	</td>
	<td width="100%" bgcolor="#CCCCCC">&nbsp;</td>
 </tr>

 <tr>

 	<td height="3" bgcolor="#ffffff"><img src="images/clear.gif" width="1" height="3"></td>
	<td  bgcolor="white" align="left" valign="center" class="body">
  				Logged in as  <a href="BW_Users?action=editProfile"><%=bws.userEmailAddress%></a>
	<input type="hidden" name = "EmailId" value = "<%=bws.userEmailAddress%>">

		<%
		if ( memberships!= null && memberships.size() > 1 )
		{
			System.out.println("memberships size %%%%%%%%%%%%%%%%%%%" + memberships.size()  );

		%>

		and as member of  <select  onchange="javascript:switchCurrentMembership(this.value)" class="InputBoxSmall"  height="14" >

			<%

			Enumeration keys = memberships.keys();
			while ( keys.hasMoreElements() )
			{

				com.boardwalk.member.Member mem =	(com.boardwalk.member.Member)memberships.get(keys.nextElement() );
				String optionvalue = mem.getNeighborhoodName();
				if ( bws.memberId.intValue() == mem.getId() )
				{

		%>
						<option  value="<%=mem.getId()%>" selected  ><%=optionvalue%></option>

		<%
				}
				else
				{
		%>

					<option value="<%=mem.getId()%>"  ><%=optionvalue%></option>

		<%
					}
			}

				%>
				</select> Neighborhood
				<%

		 }





		%>

 		</td>
 		<td bgcolor="white">&nbsp;</td>

 </tr>

</table>
</td>
</tr>
<!--end masthead-->
