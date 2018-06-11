<%@ page import ="com.boardwalk.table.*,java.io.*"%>
<%@ page import ="boardwalk.common.*,servlets.*"%>
<%! 
	String sourcexml  = null; 
	String targetxml = null; 
	public void jspInit() 
	{    
		ServletConfig config = getServletConfig();
		sourcexml  = config.getInitParameter("sourcexml"); 
		targetxml  = config.getInitParameter("targetxml");
		System.out.println(" sourcexml "+sourcexml);
	}
%>
<%
	String typeNumber	= request.getParameter("typeNumber");
	int typeNum			=Integer.parseInt(typeNumber);
	switch(typeNum)
	{
		 case 1: String Nhid			= request.getParameter("nhid");
				 String Collabid		= request.getParameter("collabid");
				 String WBid			= request.getParameter("wbid");
				 HttpSession hs			= request.getSession(true);
				 boolean IsDeleteAllow	= false;
				 String type			= "";
				 int userId				= ((Integer)hs.getAttribute("userId")).intValue();
				 ValidateAdminTableAccess objVal = new  ValidateAdminTableAccess();
				 if(Nhid != null || Collabid != null || WBid != null)
				 {
					try
					{
						if(!BoardwalkUtility.checkIfNullOrBlank(Nhid))
						{
							type="nh";
							IsDeleteAllow = objVal.canDeleteNH(Integer.parseInt(Nhid),userId);
						}
						else if(!BoardwalkUtility.checkIfNullOrBlank(Collabid))
						{
							System.out.println("----------value of collab id-----"+Collabid);
							type="collab";
							IsDeleteAllow = objVal.canDeleteCollab(Integer.parseInt(Collabid),userId);
						}
						else if(!BoardwalkUtility.checkIfNullOrBlank(WBid))
						{
							type="wb";
							IsDeleteAllow = objVal.canDeleteWB(Integer.parseInt(WBid),userId);
						}

					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					String lsRetValue= "nodelete";
					if(IsDeleteAllow)
					{
						lsRetValue="delete";
					}	
					%><%=type.trim() +"^"+lsRetValue.trim()%>

				<%} 
		 break;
			
         case 2:	String userID		= request.getParameter("userID");
					boolean IsCreatorOfCollab		= false;
					if(!BoardwalkUtility.checkIfNullOrBlank(userID))
					{
						String retVal = "false";
						IsCreatorOfCollab = BW_Users.IsCreatorOfCollab(Integer.parseInt(userID));

					%><%=IsCreatorOfCollab%>

					
				<%	}
		 break;
		
         case 3:  boolean IsInvitationPresent		= false;
				  try
				  {
					if(sourcexml != null && targetxml!= null) //Checks for entry in web.xml
					{
						File fileObj = new File(sourcexml);
						if(fileObj.exists())		//checks whether the file exists
							IsInvitationPresent = true;
					 }

				   }
					catch(Exception e)
					{
						e.printStackTrace();
					}

			%><%=IsInvitationPresent%>
	<% break;
	
	   case 4: boolean isSessionValid = request.isRequestedSessionIdValid();

	 %>
	   <%=isSessionValid%>
			   
	<%}%>