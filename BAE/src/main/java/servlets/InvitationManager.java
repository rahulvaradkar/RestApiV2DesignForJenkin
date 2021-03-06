package servlets;
/*
 *  This presents a list of collaboration available to a user
 */
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.Runtime;

import com.boardwalk.database.DatabaseLoader;
import com.boardwalk.util.BoardwalkSession;
import com.boardwalk.distribution.*;
import com.boardwalk.member.*;
import com.boardwalk.collaboration.*;
import com.boardwalk.whiteboard.*;
import com.boardwalk.table.TableTreeNode;

import boardwalk.connection.*;
import boardwalk.common.BoardwalkUtility;
import boardwalk.table.BoardwalkInviteManager;

public class InvitationManager extends BWServlet {

		String sourcexml	= "";
		String targetxml	= "";

    public void service (HttpServletRequest request,
	    HttpServletResponse response)throws ServletException, IOException
    {
		ServletContext sc = getServletContext();
        HttpServletRequest req = request;
		HttpServletResponse res = response;
		Connection connection = null;
		BoardwalkConnection BoardwalkConnectionObj = null;

		System.out.println("Inside InvitationManager ");
		userName = (String)request.getParameter("userName");
		HttpSession hs = request.getSession(true);
		if ((userName != null) && (!userName.equals("")))
		{
			System.out.println("Authenticating a Non Browser request");
			// set the session
			boolean isValidUser = authenticateNonBrowserRequest(request, response);
			if (!isValidUser)
			{
				ServletOutputStream servletOut = response.getOutputStream();
				String responseToRequest = "Invalid User";
				response.setContentLength(responseToRequest.length());
				servletOut.println(responseToRequest);
				servletOut.close();
				return;
			}

			hs = request.getSession(true);
			String referer = (String)hs.getAttribute("Referer");
		}
		else
		{
			// authenticate user
			boolean status = authenticate(request, response);
			if (status == false)
				return;
			hs = request.getSession(true);
			Integer userIdIntr = (Integer)hs.getAttribute("userId");
			Integer memberIdIntr = (Integer)hs.getAttribute("memberId");

			if (userIdIntr != null)
				userId = userIdIntr.intValue();

			if (memberIdIntr != null)
				memberid = memberIdIntr.intValue();


			BoardwalkSession bws = (BoardwalkSession)hs.getAttribute("bwSession");
			if (bws != null)
				req.setAttribute("bwSession", bws);

		}

        System.out.println("In InvitationManager");
		String action = request.getParameter("action");
		System.out.println("action -> " +action);
		if(action == null)
		{
			//Do nothing
		}
		else if (action.equalsIgnoreCase("switchCurrentMembership"))
	    {
			switchCurrentMembership(req,res);
        }
		else if (action.equalsIgnoreCase("getCuboids"))
	    {
			getCuboidsForMember(req,res);
			return;
        }
		else if (action.equalsIgnoreCase("sendMail"))
	    {
			sendMails(req,res);
        }
		else if (action.equalsIgnoreCase("statusReport"))
	    {
			getStatusReport(req,res);
			return;
        }

		BoardwalkSession bws = (BoardwalkSession)hs.getAttribute("bwSession");
		int   selNhid   = -1;
		String lsUserName = bws.userEmailAddress;
		String selNhidParam = req.getParameter("selNhid");

		if ( selNhidParam != null && ! selNhidParam.trim().equals("") )
		{
			selNhid   = Integer.parseInt(selNhidParam);
		}
		else
		{
			selNhid   = bws.nhId.intValue();
		}

		sourcexml	= getServletConfig().getInitParameter("sourcexml");
		targetxml	= getServletConfig().getInitParameter("targetxml");

		try
		{
			DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
			connection = databaseloader.getConnection();
		}
		catch(Exception e)
		{
			System.out.println("Error while getting boardwalk connection");
		}

		String lsstatictemplate = req.getParameter("statictemplate");
		String lsstaticnhid = req.getParameter("staticnhid");
		//System.out.println("%%%%%% statictemplate"+lsstatictemplate);
		//System.out.println("%%%%%% staticnhid"+lsstaticnhid);
		if ( lsstaticnhid != null && !lsstaticnhid.trim().equals(""))
		{
			selNhid   = Integer.parseInt(lsstaticnhid);
			memberid  = getMemberIdForNh(connection, selNhid, userId);
		}

		try
		{
			//Get boardwalk Connection
			BoardwalkConnectionObj = BoardwalkConnectionManager.getBoardwalkConnection( connection,userId,lsUserName,memberid);
		}
		catch(Exception e)
		{
			System.out.println("Error while getting boardwalk connection");
		}
		
		String lsSend = request.getParameter("send");
		if(lsSend != null)
		{
			try
			{
				//Get Application level variables.
				String smtpserver		= getServletConfig().getInitParameter("smptserver");
				String smtpfrom			= getServletConfig().getInitParameter("smtpfrom");
				String smtpport			= getServletConfig().getInitParameter("port");
				String smtppassword     = getServletConfig().getInitParameter("password");
				String serveraddress    = getServletConfig().getInitParameter("serveraddress");
				String serverport		= getServletConfig().getInitParameter("serverport");

				String lsTemplate	= req.getParameter("template");
				//String lsCuboids	= req.getParameter("cuboids");
				
				String lsemailmessage	= req.getParameter("emailmessage");
				System.out.println("~~~~~emailmessage - > "+lsemailmessage);
				System.out.println("For Template - > "+lsTemplate);
				//System.out.println("On Cuboids - > "+lsCuboids);

				ArrayList DistributionObjs = DistributionList.getObjects(sourcexml,targetxml);

				BoardwalkInviteManager BoardwalkInviteManagerObj = new BoardwalkInviteManager(BoardwalkConnectionObj, smtpserver, smtpfrom, lsUserName, DistributionObjs);

				BoardwalkInviteManagerObj.msport			= smtpport;
				BoardwalkInviteManagerObj.mspassword		= smtppassword;
				BoardwalkInviteManagerObj.msserveraddress	= serveraddress;
				BoardwalkInviteManagerObj.msserverport		= serverport;

				// get cuboids
				getCuboidsForMember(request, response);
				ArrayList worksheetTables = (ArrayList)request.getAttribute("worksheetTables");
				String lsCuboids = "";
				String lsTablesPresent = "";
				if (worksheetTables != null && worksheetTables.size() > 0)
				{
					for (int j = 0; j / 2 < worksheetTables.size() / 2; j += 2)
					{
						System.out.println("Worksheet name = " + (String)worksheetTables.get(j));
						//lsCuboids = lsCuboids + (String)dispTables.get(j) + "|";
						ArrayList Tables = (ArrayList)(worksheetTables.get(j + 1));
						for (int k = 0; k / 2 < Tables.size() / 2; k += 2)
						{
							String tName = (String)Tables.get(k);
							System.out.println("table name = " + tName);
							lsCuboids = lsCuboids + tName + "|";
							Hashtable tHash = (Hashtable)Tables.get(k + 1);
							Enumeration e = tHash.keys();
							if (e.hasMoreElements())
							{
								TableTreeNode tbtn = (TableTreeNode)tHash.get(e.nextElement());
								lsCuboids = lsCuboids + tbtn.getId();
								System.out.println("table id = " + tbtn.getId());
							}
							if (k < Tables.size() - 1)
								lsCuboids = lsCuboids + ",";
						}
					}
				}
				if(lsSend.equals("email"))
				{
					System.out.println("Send option - > email");
					System.out.println("Send lsTemplate - >"+lsTemplate);
					System.out.println("Send lsCuboids - >"+lsCuboids);
					System.out.println("Send selNhid - >"+selNhid);
					System.out.println("Send memberid - >"+memberid);
					System.out.println("Send userId - >"+userId);
					System.out.println("Send lsemailmessage - >"+lsemailmessage);
					String lsSuccess = BoardwalkInviteManagerObj.sendInvite(lsTemplate, lsCuboids, selNhid, memberid, userId,lsemailmessage);
					req.setAttribute("emailstatus",lsSuccess);
				}
				else
				{
					System.out.println("Send option - > file");
					System.out.println("lsTemplate - > "+lsTemplate);
					System.out.println("lsCuboids - > "+lsCuboids);
					System.out.println("selNhid - > "+selNhid);
					System.out.println("memberid - > "+memberid);
					System.out.println("userId - > "+userId);
					res.setContentType("application/octet-stream");
					res.setHeader("Content-Disposition", "attachment; filename="+lsTemplate+".bws");
					java.io.InputStream  fileio = BoardwalkInviteManagerObj.sendFile(lsTemplate, lsCuboids, selNhid, memberid, userId);
					PrintWriter pw = res.getWriter();
					int c=-1;
					// Loop to read and write bytes.
					while ((c = fileio.read()) != -1)
					{
						pw.print((char)c);
					}
					// Close output and input resources.
					fileio.close();
					pw.flush();
					BoardwalkInviteManagerObj.deleteBwsFile();
					pw=null;
				}
			}
			catch ( Exception e1 )
			{
				e1.printStackTrace();
			}
			/*commented by shirish on 6/28/2007 since the connection was not available for the later method calls
			finally 
			{
			  try
			  {
				//connection.close();
			  }
			  catch ( SQLException sql )
			  {
				sql.printStackTrace();
			  }
			}*/
		}
		if(BoardwalkConnectionObj == null)
		{
			req.setAttribute("membership","none");
		}
		else
		{
			req.setAttribute("memberList",memberOf(lsUserName, BoardwalkConnectionObj.getNeighborhoodName(),sourcexml,targetxml,lsstatictemplate));
			req.setAttribute("managedList",managedBy(lsUserName, BoardwalkConnectionObj.getNeighborhoodName(),sourcexml,targetxml,lsstatictemplate));
			req.setAttribute("inviteeList",inviteeList(BoardwalkConnectionObj.getConnection(),sourcexml, targetxml,lsstatictemplate));
		}
		req.setAttribute("nhId",new Integer(selNhid));
		req.setAttribute("memberId",new Integer(memberid));
		req.setAttribute("statictemplate",lsstatictemplate);
		req.setAttribute("staticnhid",lsstaticnhid);

		/*Added by Manish Dt 29th April 2010 to close the connection object */

	  try

	  {
		  if (connection != null)
		  {
		connection.close();
		connection = null;

		  }
	  }
	  catch ( SQLException sql )
	  {
		sql.printStackTrace();
	  }

	/*Till here by Manish*/

		// forward the request to the jsp page
		sc.getRequestDispatcher("/jsp/admin/invite.jsp").forward(req,res);
    }

	public ArrayList memberOf(String asUserId, String asNhName, String sourcexml, String targetxml, String asThisTempOnly)
	{
		ArrayList laMemberOf = new ArrayList();
		ArrayList laDistribution = new ArrayList();
		ArrayList laDistributionTemp = new ArrayList();
		ArrayList laApplicableTemp = new ArrayList();
		boolean lbDistributionApplicable = false;
		ArrayList laDistributionList = DistributionList.getObjects(sourcexml,targetxml);
		for(int liDbCount = 0 ; liDbCount < laDistributionList.size() ; liDbCount++)
		{
			Distribution DistributionObj = (Distribution)laDistributionList.get(liDbCount);
			ArrayList laTemplates = DistributionObj.getTemplates();
			if(laTemplates != null && laTemplates.size() > 0)
			{
				for(int liTempCount = 0 ; liTempCount <  laTemplates.size() ; liTempCount++)
				{
					DistributionTemplate DistributionTemplateObj = (DistributionTemplate)laTemplates.get(liTempCount);
					//System.out.println("%%%% memberOf asThisTempOnly -> "+asThisTempOnly+ " == "+DistributionTemplateObj.getmsTemplateName());
					if ( asThisTempOnly != null && ! asThisTempOnly.trim().equals("") && !asThisTempOnly.equals(DistributionTemplateObj.getmsTemplateName()))
						continue;
					ArrayList laTempUsers = DistributionTemplateObj.getmALTemplateUsers();
					if(laTempUsers != null && laTempUsers.size() > 0)
					{
						//System.out.println("%%%% memberOf laTempUsers.size() - > "+laTempUsers.size());
						for(int liUserCount = 0 ; liUserCount <  laTempUsers.size() ; liUserCount++)
						{
							DistributionUser DistributionUserObj = (DistributionUser)laTempUsers.get(liUserCount);
							String lsUserName = DistributionUserObj.getmsUserEmail();
							String lsNhName = DistributionUserObj.getmsNeighbourhood();
							Pattern pattern = Pattern.compile(lsUserName);
							Matcher matcher = pattern.matcher(asUserId);
							//System.out.println("%%%% memberOf user -> '"+lsUserName+ "' == '"+asUserId+"' >>"+matcher.find(0));
							//System.out.println("%%%% memberOf NH -> "+lsNhName+ " == "+asNhName);
							if(matcher.find(0) && lsNhName.equals(asNhName))
							{
								lbDistributionApplicable = true;
								laApplicableTemp.add(DistributionTemplateObj.getmsTemplateName());
								break;
							}
						}
					}
				}
			}
			if(lbDistributionApplicable)
			{
				laDistribution.add(DistributionObj.getmsName());
				laDistributionTemp.add(laApplicableTemp);
			}
			laApplicableTemp = new ArrayList();
		}
		if(laDistribution.size() > 0)
		{
			laMemberOf.add(laDistribution);
			laMemberOf.add(laDistributionTemp);
		}
		return laMemberOf;
	}

	public String managedBy(String asUserId, String asNhName, String sourcexml, String targetxml, String asThisTempOnly)
	{
		String lsIsManagedBy = "false";
		ArrayList laDistributionList = DistributionList.getObjects(sourcexml,targetxml);
		for(int liDbCount = 0 ; liDbCount < laDistributionList.size() ; liDbCount++)
		{
			Distribution DistributionObj = (Distribution)laDistributionList.get(liDbCount);
			ArrayList laManagers = DistributionObj.getManagers();
			if(laManagers != null && laManagers.size() > 0)
			{
				for(int liMgrCount = 0 ; liMgrCount <  laManagers.size() ; liMgrCount++)
				{
					DistributionAdmin DistributionAdminObj = (DistributionAdmin)laManagers.get(liMgrCount);
					String lsUserName = DistributionAdminObj.getmsUserEmail();
					String lsNhName = DistributionAdminObj.getmsNeighbourhood();

					Pattern pattern = Pattern.compile(lsUserName);
					Matcher matcher = pattern.matcher(asUserId);
					//System.out.println("%%%% managedBy user -> '"+lsUserName+ "' == '"+asUserId+"' >>"+matcher.find(0));
					//System.out.println("%%%% managedBy NH -> "+lsNhName+ " == "+asNhName);
					if(matcher.find(0) && lsNhName.equals(asNhName))
					{
						lsIsManagedBy = "true";
					}
				}
			}

		}

		return lsIsManagedBy;
	}

	public ArrayList inviteeList(Connection aconnection, String sourcexml, String targetxml, String asThisTempOnly)
	{
		//System.out.println("!!!! Connection aconnection!!!!"+aconnection);
		ArrayList laInvitees = new ArrayList();
		ArrayList laDistributionTemp = new ArrayList();
		ArrayList laApplicableUsers = new ArrayList();
		boolean lbDistributionApplicable = false;
		ArrayList laDistributionList = DistributionList.getObjects(sourcexml,targetxml);
		for(int liDbCount = 0 ; liDbCount < laDistributionList.size() ; liDbCount++)
		{
			Distribution DistributionObj = (Distribution)laDistributionList.get(liDbCount);
			ArrayList laTemplates = DistributionObj.getTemplates();
			if(laTemplates != null && laTemplates.size() > 0)
			{
				for(int liTempCount = 0 ; liTempCount <  laTemplates.size() ; liTempCount++)
				{
					laApplicableUsers = new ArrayList();
					DistributionTemplate DistributionTemplateObj = (DistributionTemplate)laTemplates.get(liTempCount);
					if ( asThisTempOnly != null && ! asThisTempOnly.trim().equals("") && !asThisTempOnly.equals(DistributionTemplateObj.getmsTemplateName()))
									continue;
					ArrayList laTempUsers = DistributionTemplateObj.getmALTemplateUsers();
					if(laTempUsers != null && laTempUsers.size() > 0)
					{
						//System.out.println("laTempUsers.size() - > "+laTempUsers.size());
						for(int liUserCount = 0 ; liUserCount <  laTempUsers.size() ; liUserCount++)
						{
							DistributionUser DistributionUserObj = (DistributionUser)laTempUsers.get(liUserCount);
							String lsUser = DistributionUserObj.getmsUserEmail();
							String lsNhName = DistributionUserObj.getmsNeighbourhood();
		//System.out.println("!!!! 22222222Connection aconnection!!!!"+aconnection);
							String lsNhId = getNhId(lsNhName, aconnection);
							if(!lsNhId.equals(""))
							{
								ArrayList UserList = getUserListForPattern(aconnection, lsUser);
								for(int liCount = 0 ; liCount < UserList.size() ; liCount++)
								{
									String[] lsUserDetail = (String[])UserList.get(liCount);
									if(isUserNhMember(lsUserDetail[1],lsNhId,aconnection))
									{
										//System.out.println(" user -> "+lsUserDetail[0]+" id -> "+lsUserDetail[1]+" NH -> "+lsNhName+" id -> "+lsNhId);
										String[] lsaUser = {lsUserDetail[0], lsUserDetail[1], lsNhName, lsNhId};
										laApplicableUsers.add(lsaUser);
									}
								}
							}
						}
					}
					if(laApplicableUsers.size() > 0)
					{
						laDistributionTemp.add(DistributionTemplateObj.getmsTemplateName());
						laDistributionTemp.add(laApplicableUsers);
						laApplicableUsers = new ArrayList();
					}
				}
				laInvitees.add(DistributionObj.getmsName());
				laInvitees.add(laDistributionTemp);
				laDistributionTemp = new ArrayList();
			}
		}
		return laInvitees;
	}

	public int getMemberIdForNh(Connection aConnection, int selNhid, int userId)
	{
		PreparedStatement statement = null;
		ResultSet rs				= null;
		String lsSql				= "";
		int liMemberId				= 0;
		try
		{
			lsSql = " SELECT ID FROM BW_MEMBER WHERE USER_ID = ? AND NEIGHBORHOOD_ID = ?";
			System.out.println("######### getMemberIdForNh() " +lsSql);
			statement = aConnection.prepareStatement(lsSql);
			statement.setInt(1, userId);
			statement.setInt(2, selNhid);
			rs = statement.executeQuery();

			if(rs.next())
			{
				liMemberId = rs.getInt(1);
			}
			else
				System.out.println("\n\n ######### member with User id '"+userId+"' and Nh id '"+selNhid+"' is not present #########");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if ( rs != null ) {
					rs.close();
				}
				if ( statement != null ) {
					statement.close();
				}
			}
			catch(SQLException sqlexception1)
			{
				sqlexception1.printStackTrace();
			}
		}
		return liMemberId;
	}

	public boolean isUserNhMember(String asuserId, String asnhid,Connection aconnection)
	{
		PreparedStatement newstatement	= null;
		ResultSet rset					= null;
		String lsSql					= "";
		String lsmemberId				= "";
		boolean flag					= false;
		try
		{
			lsSql = " SELECT ID FROM BW_MEMBER WHERE USER_ID = ? AND NEIGHBORHOOD_ID =?";
			//lsSql = " SELECT ID FROM BW_MEMBER WHERE USER_ID ="+ asuserId +" AND NEIGHBORHOOD_ID ="+ asnhid ;
			//System.out.println("######### getNhId() " +lsSql);
			Connection con = aconnection;
			newstatement = con.prepareStatement(lsSql);
			newstatement.setString(1, asuserId);
			newstatement.setString(2, asnhid);
			rset = newstatement.executeQuery();

			while(rset.next())
				lsmemberId = rset.getString(1);
			if(lsmemberId != null && lsmemberId.length() > 0 )
				flag = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if ( rset != null ) {
					rset.close();
				}
				if ( newstatement != null ) {
					newstatement.close();
				}
			}
			catch(SQLException sqlexception1)
			{
				sqlexception1.printStackTrace();
			}
		}
		
		return flag;
	}


	public String getNhId(String asNhName, Connection aConnection)
	{
		//System.out.println("!!!! 33333Connection aConnection!!!!"+aConnection);
		PreparedStatement newstatement 	= null;
		ResultSet rset					= null;
		String lsSql					= "";
		String lsNhId					= "";
		try
		{
			lsSql = " SELECT ID FROM BW_NH WHERE NAME = ?";
			//lsSql = " SELECT ID FROM BW_NH WHERE NAME = '"+asNhName+"'";
			//System.out.println("######### getNhId() " +lsSql);
			Connection con = aConnection;
			newstatement = con.prepareStatement(lsSql);
			newstatement.setString(1, asNhName);
			rset = newstatement.executeQuery();
			if(rset.next())
			{
				lsNhId = rset.getString(1);
			}
			else
				System.out.println("\n\n ######### Neighbourhood with name '"+asNhName+"' is not present #########");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if ( rset != null ) {
					rset.close();
				}
				if ( newstatement != null ) {
					newstatement.close();
				}
			}
			catch(SQLException sqlexception1)
			{
				sqlexception1.printStackTrace();
			}
		}
		return lsNhId;
	}

	public void switchCurrentMembership(HttpServletRequest req, HttpServletResponse res)
	throws ServletException
	{
		String switchMembershipToParam = req.getParameter("switchMembershipTo");
		if ( switchMembershipToParam != null && !switchMembershipToParam.trim().equalsIgnoreCase("") )
		{
			Integer switchMembershipToId = new Integer(switchMembershipToParam);
			HttpSession hs = req.getSession(true);
			BoardwalkSession bws = (BoardwalkSession)hs.getAttribute("bwSession");
			Member  mb = (Member)bws.memberIdToMember.get(switchMembershipToId );
			//System.out.println(" Inside switchCurrentMembership " + mb.getNeighborhoodId() + " " + mb.getNeighborhoodName() + " " + mb.getNeighborhoodId() );
			bws.memberId = switchMembershipToId;
			bws.nhId = new Integer(mb.getNeighborhoodId());
			bws.nhName =mb.getNeighborhoodName();
			bws.selNhid = new Integer(mb.getNeighborhoodId());

			hs.setAttribute("nhId", new Integer(mb.getNeighborhoodId()));
			hs.setAttribute("memberId", new Integer(mb.getId())  );
			hs.setAttribute("nhName", mb.getNeighborhoodName());
		}
	}






	public void sendMails(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{
		String lsSendTo			= req.getParameter("sendTo");
		String lsTemplatename	= lsSendTo.substring(0,lsSendTo.indexOf("|"));
		System.out.println("##### lsSendTo "+lsSendTo);
		System.out.println("##### lsTemplatename "+lsTemplatename);
		
		BoardwalkConnection BoardwalkConnectionObj = null;
		String smtpserver		= getServletConfig().getInitParameter("smptserver");
		String smtpfrom			= getServletConfig().getInitParameter("smtpfrom");
		String smtpport			= getServletConfig().getInitParameter("port");
		String smtppassword     = getServletConfig().getInitParameter("password");
		String serveraddress    = getServletConfig().getInitParameter("serveraddress");
		String serverport		= getServletConfig().getInitParameter("serverport");

		Connection connection = null;
		try
		{
			DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
			connection = databaseloader.getConnection();
		}
		catch(Exception e)
		{
			System.out.println("Error while getting boardwalk connection");
		}

		ArrayList DistributionObjs = DistributionList.getObjects(sourcexml,targetxml);
		BoardwalkInviteManager BoardwalkInviteManagerObj = new BoardwalkInviteManager(connection, smtpserver, smtpfrom,DistributionObjs);
		
		String msMsg = getMessage(lsTemplatename,sourcexml,targetxml);
		BoardwalkInviteManagerObj.msMessagePath		= msMsg;
		BoardwalkInviteManagerObj.msport			= smtpport;
		BoardwalkInviteManagerObj.mspassword		= smtppassword;
		BoardwalkInviteManagerObj.msserveraddress	= serveraddress;
		BoardwalkInviteManagerObj.msserverport		= serverport;
		
		/*
		String lsSendToUrls				= selectUsersforUrl(lsSendTo, msMsg);
		System.out.println("$$$		LSSENDTOURLS	:"+lsSendToUrls);
		if( !lsSendToUrls.equals("") && lsSendToUrls.charAt(lsSendToUrls.length()-1) != '^' )
			lsSendToUrls					= lsSendToUrls.substring(0,lsSendToUrls.length()-1);
		System.out.println("\n\n$$$$	URLS SEPARATED FROM THOSE TO WHCH ATTACHMENTS IS TO BE SEND :"+lsSendToUrls);
		*/
		String lsSendToUrls = lsSendTo;
		String lsSuccess = BoardwalkInviteManagerObj.sendUrlToUsers(lsSendToUrls);


		req.setAttribute("sendurl",lsSuccess);
	}

	public String selectUsersforUrl(String asSendTo)
	{
		System.out.println("$$$		LSSENDTO	:"+asSendTo);
		PreparedStatement stmt 								= null;
		Connection connection								= null;
		BoardwalkConnection BoardwalkConnectionObj			= null;
		BoardwalkInviteManager BoardwalkInviteManagerObj	= null;
		
		try
		{
			DatabaseLoader databaseloader	= new DatabaseLoader(new Properties());
			connection						= databaseloader.getConnection();
		}
		catch(Exception e)
		{
			System.out.println("Error while getting boardwalk connection");
		}

		String smtpserver		= getServletConfig().getInitParameter("smptserver");
		String smtpfrom			= getServletConfig().getInitParameter("smtpfrom");
		String smtpport			= getServletConfig().getInitParameter("port");
		String smtppassword     = getServletConfig().getInitParameter("password");
		String serveraddress    = getServletConfig().getInitParameter("serveraddress");
		String serverport		= getServletConfig().getInitParameter("serverport");

		ArrayList DistributionObjs = DistributionList.getObjects(sourcexml,targetxml);

		String lsretUserUrl		= "";
		int litblCounter		= -1;
		String[] lsaTemplates	= BoardwalkUtility.getArrayFromStrTok(asSendTo, "^");
		
		for(int liTempCount = 0 ; liTempCount < lsaTemplates.length ; liTempCount++)
		{
			String[] lsaTempDetail = BoardwalkUtility.getArrayFromStrTok(lsaTemplates[liTempCount], "|");
			String lsTemplate = lsaTempDetail[0];
			for(int liNhCount = 1 ; liNhCount < lsaTempDetail.length ; liNhCount++)
			{
				String[] lsaNhandUserDetail = BoardwalkUtility.getArrayFromStrTok(lsaTempDetail[liNhCount], ",");//nhid,userid
				System.out.println("######### lsaNhandUserDetail >> " + lsaTempDetail[liNhCount]);//Debug
				String lsNhId = lsaNhandUserDetail[0];//nhid
				System.out.println("######### lsNhId >> " + lsNhId);//Debug
				for(int liUserCount = 1; liUserCount < lsaNhandUserDetail.length; liUserCount++)
				{	
					String lsuserId = lsaNhandUserDetail[liUserCount];//lsaNhandUserDetail[liUserCount]);userid
					//String lsSql =  " SELECT ID FROM BW_MEMBER WHERE USER_ID = "+lsuserId+
					//				" AND NEIGHBORHOOD_ID = "+lsNhId;
					String lsSql =  " SELECT ID FROM BW_MEMBER WHERE USER_ID = ? AND NEIGHBORHOOD_ID = ?";
					System.out.println("######### lsSql >> " + lsSql);					
					int memId	= -1;
					try
					{
						stmt = connection.prepareStatement(lsSql);
						stmt.setString(1, lsuserId);
						stmt.setString(2, lsNhId);
						ResultSet rs = stmt.executeQuery();	
						
						while(rs.next())
						{
							memId = rs.getInt(1);	
							System.out.println("@@@@@ memId >> " + memId);
						}
					
						//String lsUserName		= BoardwalkInviteManagerObj.getEmailIdOnUserId(lsuserId);
						String lsUserName = "";
						try
						{
							lsSql = " SELECT EMAIL_ADDRESS FROM BW_USER WHERE ID = ?";
							stmt = connection.prepareStatement(lsSql);
							stmt.setString(1, lsuserId);
							rs = stmt.executeQuery();	
							
							if(rs.next())
							{
								lsUserName = rs.getString(1);
							}
							else
								System.out.println("\n\n ######### User with Id '"+lsuserId+"' is not present #########");
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}

						BoardwalkConnectionObj	= BoardwalkConnectionManager.getBoardwalkConnection( connection,Integer.parseInt(lsuserId),lsUserName,memId);
						BoardwalkInviteManagerObj = new BoardwalkInviteManager(BoardwalkConnectionObj, smtpserver, smtpfrom, lsUserName, DistributionObjs);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					

					BoardwalkInviteManagerObj.msport			= smtpport;
					BoardwalkInviteManagerObj.mspassword		= smtppassword;
					BoardwalkInviteManagerObj.msserveraddress	= serveraddress;
					BoardwalkInviteManagerObj.msserverport		= serverport;

					ArrayList worksheetTables = new ArrayList();
					String lsbodyMessage	= "";

					try
					{
						DatabaseLoader databaseloader	= new DatabaseLoader(new Properties());
						connection						= databaseloader.getConnection();
						String lsMsgpath				= getMessage(lsTemplate, sourcexml, targetxml);//addded by shirish on 13/04/07
						lsbodyMessage					= BoardwalkInviteManager.getBodyText(lsMsgpath);
						//System.out.println("!!!lsbodyMessage - > "+lsbodyMessage);
						ArrayList xmlWorksheetTables	= getWorkSheetTablesForTemplate(lsTemplate, sourcexml, targetxml);
						System.out.println("Found worksheet count - > "+xmlWorksheetTables.size()/2);
						if(xmlWorksheetTables != null && xmlWorksheetTables.size() > 0)
						{
							if(xmlWorksheetTables.size()/2 == 1)
							{
								for(int liWorksheetCount = 0; liWorksheetCount <= xmlWorksheetTables.size()/2; liWorksheetCount +=2)
								{
									ArrayList dispTables = new ArrayList();
									System.out.println("Process WorkSheet - > "+xmlWorksheetTables.get(liWorksheetCount));
									worksheetTables.add((String)xmlWorksheetTables.get(liWorksheetCount));
									ArrayList xmlTables = (ArrayList)xmlWorksheetTables.get(liWorksheetCount+1);
									if(xmlWorksheetTables != null && xmlWorksheetTables.size() > 0)
									{
										for(int liTblCount = 0; liTblCount < xmlTables.size(); liTblCount++)
										{
											litblCounter				= 0;
											String lsPrevTemplate		= "";
											DistributionTableDisplay xmlTableObj = (DistributionTableDisplay)xmlTables.get(liTblCount);
											
											if(!xmlTableObj.getmsHidden().equalsIgnoreCase("yes"))
											{
												Hashtable Table = getTablesByCollabAndWbAndTableName( connection, xmlTableObj.getmsCollabrationName(), xmlTableObj.getmsWhiteBoardName(), xmlTableObj.getmsTableName(), memId);
												System.out.println("Process Display Table - > "+(String)xmlTableObj.getmsTableDisplayName()+" count "+Table.size());
												
												if( xmlWorksheetTables.size()/2 == 1 && Table.size() == 1 )
												{
													litblCounter++;
													Integer lIntCuboidid = (Integer)Table.keys().nextElement();
													String lsCuboids = xmlTableObj.getmsTableDisplayName()+"|"+lIntCuboidid;
													//System.out.println("~`~~```````~lsTemplate:"+lsTemplate);
													//System.out.println("~`~~```````~lsCuboids:"+lsCuboids);
													//System.out.println("~`~~```````~Integer.parseInt(lsNhId):"+Integer.parseInt(lsNhId));
													//System.out.println("~`~~```````~memId:"+memId);
													//System.out.println("~`~~```````~Integer.parseInt(lsuserId):"+Integer.parseInt(lsuserId));
													//System.out.println("~`~~```````~lsbodyMessage:"+lsbodyMessage);
													String lsSuccess = BoardwalkInviteManagerObj.sendInvite(lsTemplate, lsCuboids, Integer.parseInt(lsNhId), memId, Integer.parseInt(lsuserId),lsbodyMessage);
												}
												
												if (litblCounter == 0)
												{
													if(lsPrevTemplate == lsTemplate)
														lsretUserUrl += "|"+lsaTempDetail[liNhCount];
													else
														lsretUserUrl += lsTemplate+"|"+lsaTempDetail[liNhCount];
													//System.out.println("=================lsretUserUrl:"+lsretUserUrl);
													lsPrevTemplate	= lsTemplate;
												}
											}
										}
									}
								}
							}

							else
								lsretUserUrl += lsTemplate+"|"+lsaTempDetail[liNhCount];
						}
					}
					catch( Exception e ) {
						e.printStackTrace();
					}
					/*finally {
						try {
							connection.close();
						}
						catch( SQLException sql ) {
							sql.printStackTrace();
						}
					}*/
				
				}
			}
			//System.out.println("=================lsretUserUrl.charAt(lsretUserUrl.length()-1):"+lsretUserUrl.charAt(lsretUserUrl.length()-1));
			if( !lsretUserUrl.equals("") &&  lsretUserUrl.charAt(lsretUserUrl.length()-1) != '^')
				lsretUserUrl += "^"; 
		}
		//System.out.println("=================LSRETUSERURL:"+lsretUserUrl);
		return lsretUserUrl;
	}



	public void getCuboidsForMember(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{
		String memberidParam	= req.getParameter("memberId");
		String lsTemplate		= req.getParameter("template");
		String lsType			= req.getParameter("Type");
		
		System.out.println("Member Id - > "+memberidParam);
		System.out.println("Template name - > "+lsTemplate);

		String sourcexml	= getServletConfig().getInitParameter("sourcexml");
		String targetxml	= getServletConfig().getInitParameter("targetxml");
		String lsstatictemplate = req.getParameter("statictemplate");
		String lsstaticnhid = req.getParameter("staticnhid");
		//System.out.println("%%%%%% statictemplate"+lsstatictemplate);
		//System.out.println("%%%%%% staticnhid"+lsstaticnhid);



		int memberId = -1;

		if ( memberidParam != null )
		{
			memberId = Integer.parseInt(memberidParam);
		}


        ArrayList worksheetTables = new ArrayList();
        String lsbodyMessage	= "";

		Connection connection = null;

        try
        {
            DatabaseLoader databaseloader	= new DatabaseLoader(new Properties());
            connection						= databaseloader.getConnection();
			String lsMsgpath				= getMessage(lsTemplate, sourcexml, targetxml);//addded by shirish on 13/04/07
			lsbodyMessage					=BoardwalkInviteManager.getBodyText(lsMsgpath);
			System.out.println("!!!lsbodyMessage - > "+lsbodyMessage);
			ArrayList xmlWorksheetTables	= getWorkSheetTablesForTemplate(lsTemplate, sourcexml, targetxml);
			System.out.println("Found worksheet count - > "+xmlWorksheetTables.size()/2);
			if(xmlWorksheetTables != null && xmlWorksheetTables.size() > 0)
			{
				for(int liWorksheetCount = 0; liWorksheetCount < xmlWorksheetTables.size(); liWorksheetCount +=2)
				{
					ArrayList dispTables = new ArrayList();
					System.out.println("Process WorkSheet - > "+xmlWorksheetTables.get(liWorksheetCount));
					worksheetTables.add((String)xmlWorksheetTables.get(liWorksheetCount));
					ArrayList xmlTables = (ArrayList)xmlWorksheetTables.get(liWorksheetCount+1);
					if(xmlWorksheetTables != null && xmlWorksheetTables.size() > 0)
					{
						for(int liTblCount = 0; liTblCount < xmlTables.size(); liTblCount++)
						{
							DistributionTableDisplay xmlTableObj = (DistributionTableDisplay)xmlTables.get(liTblCount);
							if(!xmlTableObj.getmsHidden().equalsIgnoreCase("yes"))
							{
								Hashtable Table = getTablesByCollabAndWbAndTableName( connection, xmlTableObj.getmsCollabrationName(), xmlTableObj.getmsWhiteBoardName(), xmlTableObj.getmsTableName(), memberId);
								System.out.println("Process Display Table - > "+(String)xmlTableObj.getmsTableDisplayName()+" count "+Table.size());
								dispTables.add((String)xmlTableObj.getmsTableDisplayName());
								dispTables.add(Table);
							}
						}
					}
					worksheetTables.add(dispTables);
				}
			}
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            }
            catch( SQLException sql ) {
                sql.printStackTrace();
            }

        }

        req.setAttribute("worksheetTables", worksheetTables );
		req.setAttribute("statictemplate",lsstatictemplate);
		req.setAttribute("staticnhid",lsstaticnhid);
		req.setAttribute("bodyMessage",lsbodyMessage);
		req.setAttribute("Type",lsType);

        // forward the request to the jsp page
		//ServletContext sc = getServletContext();
        //sc.getRequestDispatcher("/jsp/admin/cuboid_list.jsp").forward(req,res);
	}

	public String getMessage(String asTemplate,String assourcexml, String astargetxml)
	{
		if(assourcexml.equals(""))
			sourcexml	= assourcexml;
		if(assourcexml.equals(""))
			targetxml	= astargetxml;
		boolean lbFound = false;
		String msMessagePath = "";
		ArrayList laDistributionList = DistributionList.getObjects(assourcexml,astargetxml);
		for(int liDbCount = 0 ; liDbCount < laDistributionList.size() ; liDbCount++)
		{
			Distribution DistributionObj = (Distribution)laDistributionList.get(liDbCount);
			ArrayList laTemplates = DistributionObj.getTemplates();
			for(int liTempCount = 0 ; liTempCount <  laTemplates.size() ; liTempCount++)
			{
				DistributionTemplate DistributionTemplateObj = (DistributionTemplate)laTemplates.get(liTempCount);
				//if(asTemplate.equals(DistributionTemplateObj.getmsTemplateName()))
				//{
					msMessagePath	= DistributionTemplateObj.getmsmessage();
					System.out.println("@@In getMessage(String asTemplate)  msMessagePath >> " + msMessagePath);
					lbFound = true;
					break;
				//}
			}
			if(lbFound)
				break;
		}
		return msMessagePath;
	}

	public ArrayList getWorkSheetTablesForTemplate(String asTemplate, String sourcexml, String targetxml)
	{
		ArrayList Worksheet = new ArrayList();
		ArrayList Tables = new ArrayList();
		boolean lbTemplateFound = false;
		ArrayList laDistributionList = DistributionList.getObjects(sourcexml,targetxml);
		for(int liDbCount = 0 ; liDbCount < laDistributionList.size() ; liDbCount++)
		{
			Distribution DistributionObj = (Distribution)laDistributionList.get(liDbCount);
			ArrayList laTemplates = DistributionObj.getTemplates();
			if(laTemplates != null && laTemplates.size() > 0)
			{
				for(int liTempCount = 0 ; liTempCount <  laTemplates.size() ; liTempCount++)
				{
					DistributionTemplate DistributionTemplateObj = (DistributionTemplate)laTemplates.get(liTempCount);
					//System.out.println(" Template - > "+DistributionTemplateObj.getmsTemplateName());
					if(DistributionTemplateObj.getmsTemplateName().equals(asTemplate))
					{
						ArrayList workBook = DistributionTemplateObj.getmALTemplateWorkBook();
						if(workBook != null && workBook.size() > 0)
						{
							for(int liWBCount = 0 ; liWBCount <  workBook.size() ; liWBCount++)
							{
								DistributionWorkBook DistributionWorkBookObj = (DistributionWorkBook)workBook.get(liWBCount);
								//System.out.println(" WorkBook - > "+DistributionWorkBookObj.getmsName());
								ArrayList laWorkSheets = DistributionWorkBookObj.getmsWorkSheets();
								for(int liSheetCount = 0 ; liSheetCount < laWorkSheets.size() ; liSheetCount++)
								{
									DistributionTemplateWorkSheet WorkSheetObj = (DistributionTemplateWorkSheet)laWorkSheets.get(liSheetCount);
									//System.out.println(" WorkSheet - > "+WorkSheetObj.getmsWorkSheetName());
									ArrayList TableDisplay = WorkSheetObj.getmALTableDisplay();
									for(int liTblCount = 0 ; liTblCount < TableDisplay.size() ; liTblCount++)
									{
										DistributionTableDisplay TableDisplayObj = (DistributionTableDisplay)TableDisplay.get(liTblCount);
										System.out.println(" Table - > "+TableDisplayObj.getmsTableDisplayName());
										Tables.add(TableDisplayObj);
									}
									if(Tables != null && Tables.size() > 0)
									{
										//System.out.println(" Adding worksheet - > "+(String)WorkSheetObj.getmsWorkSheetName()+" with "+Tables.size()+" tables");
										Worksheet.add((String)WorkSheetObj.getmsWorkSheetName());
										Worksheet.add(Tables);
										Tables = new ArrayList();
									}
								}
							}
						}
					}
				}
			}
		}
		return Worksheet;
	}

	public Hashtable getTablesByCollabAndWbAndTableName( Connection connection, String asCollabrationName, String asWhiteBoardName, String asTableName, int aimemberId)
	{
		
		ResultSet resultset = null;
		PreparedStatement preparedstatement = null;

		Hashtable Tables = new Hashtable();

		TableTreeNode currentTable = null;

		try {
				System.out.println("{CALL BW_GET_COLLAB_TBLS_FOR_MEMBER_USING_REGEX('"+asCollabrationName+"','"+asWhiteBoardName+"','"+asTableName+"',"+aimemberId+")}");
				preparedstatement = connection.prepareCall("{CALL BW_GET_COLLAB_TBLS_FOR_MEMBER_USING_REGEX(?,?,?,?)}");
				preparedstatement.setString(1,asCollabrationName);
				preparedstatement.setString(2,asWhiteBoardName);
				preparedstatement.setString(3,asTableName);
				preparedstatement.setInt(4,aimemberId);

				resultset = preparedstatement.executeQuery();

			while ( resultset.next() )
			{
				int a_collaboration_id;
				String a_collab_name;
				String a_collab_purpose;

				int a_wb_id;
				String a_wb_name;

				int a_table_id;
				String a_table_name;
				String a_table_purpose;

				String a_relationship;
				int a_access;

				a_collaboration_id = resultset.getInt("COLLAB_ID");
				a_collab_name = resultset.getString("COLLAB_NAME");
				a_collab_purpose = resultset.getString("COLLAB_PURPOSE");
				a_wb_id = resultset.getInt("WB_ID");
				a_wb_name = resultset.getString("WB_NAME");
				a_table_name= resultset.getString("TABLE_NAME");
				a_table_purpose= resultset.getString("TABLE_PURPOSE");
				a_relationship= resultset.getString("REL");
				a_table_id = resultset.getInt("TABLE_ID");
				a_access= resultset.getInt("ACCESS_");
				if(a_table_id > 0)
				{
					TableTreeNode tbtn = new TableTreeNode ( a_table_id, a_wb_id, a_table_name ,a_table_purpose , a_access);
					Tables.put(new Integer(a_table_id),tbtn);
				}
			}
		}
		catch(SQLException sqlexception) {
			sqlexception.printStackTrace();
		}
		finally {
			try {

				if ( resultset != null )
						resultset.close();
			   if ( preparedstatement != null )
					preparedstatement.close();
			}
			catch(SQLException sqlexception1) {
				sqlexception1.printStackTrace();
			}
		}
		return Tables;

	}

	public ArrayList getUserListForPattern(Connection aconnection, String aspattern)
	{
		ArrayList laUserList		= new ArrayList();
		PreparedStatement statement	= null;
		ResultSet rs				= null;
		String lsSql				= "";
		try
		{
			//lsSql = " SELECT EMAIL_ADDRESS, ID "+
			//		" FROM BW_USER "+
			//		" WHERE EMAIL_ADDRESS LIKE '%"+aspattern+"%' ";
			//System.out.println("######### getUserListForPattern() " +lsSql);

			lsSql = " SELECT EMAIL_ADDRESS, ID FROM BW_USER WHERE EMAIL_ADDRESS LIKE ? ";

			statement = aconnection.prepareStatement(lsSql);
			statement.setString(1, "%"+aspattern+"%");
			rs = statement.executeQuery();
			while(rs.next())
			{
				String[] lsUserDetail	= new String[2];
				lsUserDetail[0] = rs.getString(1);
				lsUserDetail[1] = rs.getString(2);
				laUserList.add(lsUserDetail);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if ( rs != null ) {
					rs.close();
				}
				if ( statement != null ) {
					statement.close();
				}
			}
			catch(SQLException sqlexception1)
			{
				sqlexception1.printStackTrace();
			}
		}
		return laUserList;
	}

	public void getStatusReport(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{
		String sourcexml	= getServletConfig().getInitParameter("sourcexml");
		String targetxml	= getServletConfig().getInitParameter("targetxml");

		Connection connection = null;

        try
        {
            DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
            connection = databaseloader.getConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		ArrayList laStatusReport = new ArrayList();
		ArrayList laDistributionList = DistributionList.getObjects(sourcexml,targetxml);
		for(int liDbCount = 0 ; liDbCount < laDistributionList.size() ; liDbCount++)
		{
			Distribution DistributionObj = (Distribution)laDistributionList.get(liDbCount);
			ArrayList laTemplates = DistributionObj.getTemplates();
			if(laTemplates != null && laTemplates.size() > 0)
			{
				for(int liTempCount = 0 ; liTempCount <  laTemplates.size() ; liTempCount++)
				{
					DistributionTemplate DistributionTemplateObj = (DistributionTemplate)laTemplates.get(liTempCount);
					String lsTemplateName = DistributionTemplateObj.getmsTemplateName();
					System.out.println("##### lsTemplateName "+lsTemplateName);
					ArrayList laTempCouboids = new ArrayList();
					ArrayList laTempUsers = DistributionTemplateObj.getmALTemplateUsers();
					ArrayList laTemplateWorkBook = DistributionTemplateObj.getmALTemplateWorkBook();
					if(laTemplateWorkBook != null && laTemplateWorkBook.size() > 0)
					{
						//Since in an template there can be only one Workbook
						DistributionWorkBook workBook = (DistributionWorkBook)laTemplateWorkBook.get(0);
						ArrayList laWorkSheets = workBook.getmsWorkSheets();
						//Here an assumption is made tht the first worksheet always has the primary table present
						DistributionTemplateWorkSheet WorkSheetObj = (DistributionTemplateWorkSheet)laWorkSheets.get(0);
						ArrayList TableDisplay = WorkSheetObj.getmALTableDisplay();
						//Here assumption is made tht the first table is the primary table
						DistributionTableDisplay TableDisplayObj = (DistributionTableDisplay)TableDisplay.get(0);
						String lsCuboidName = TableDisplayObj.getmsTableName();
						String lsWbName = TableDisplayObj.getmsWhiteBoardName();
						String lsCollabName = TableDisplayObj.getmsCollabrationName();
						System.out.println("##### lsCuboidName "+lsCuboidName);
						ArrayList laCuboidList = getCuboidListForPattern(connection, lsCuboidName, lsWbName, lsCollabName);
						ArrayList laDispCuboidTransList = new ArrayList();
						for(int liCuboidCount = 0 ; liCuboidCount < laCuboidList.size() ; liCuboidCount++)
						{
							String[] lsCiboidDetail = (String[])laCuboidList.get(liCuboidCount);
							System.out.println("##### lsCiboidDetail "+lsCiboidDetail[0]);
							int liCuboidId = Integer.parseInt(lsCiboidDetail[1]);
							Hashtable UserDetailForCuboid = new Hashtable();
							for(int liUserCount = 0 ; liUserCount <  laTempUsers.size() ; liUserCount++)
							{
								DistributionUser DistributionUserObj = (DistributionUser)laTempUsers.get(liUserCount);
								String lsUserName = DistributionUserObj.getmsUserEmail();
								//System.out.println("##### lsUserName "+lsUserName);
								ArrayList UsersWorkedOnCuboid = getUsersWorkedOnTbl(connection , lsUserName, liCuboidId);
								for(int liCount = 0; liCount < UsersWorkedOnCuboid.size(); liCount++)
								{
									int[] liUserDetail = (int[])UsersWorkedOnCuboid.get(liCount);
									UserDetailForCuboid.put(new Integer(liUserDetail[1]), getTransactionDetailwithUser(connection, liUserDetail[0], liUserDetail[1]));
								}
							}
							laDispCuboidTransList.add(lsCiboidDetail[0]);
							laDispCuboidTransList.add(UserDetailForCuboid);
						}
						laStatusReport.add(lsTemplateName);
						laStatusReport.add(laDispCuboidTransList);
					}
				}
			}
		}

		req.setAttribute("statusreport", laStatusReport );

        // forward the request to the jsp page
		ServletContext sc = getServletContext();
        sc.getRequestDispatcher("/jsp/admin/status_report.jsp").forward(req,res);
	}

	public ArrayList getCuboidListForPattern(Connection aconnection, String asCuboidpattern, String asWbpattern, String asCollabpattern)
	{
		ArrayList laCuboidList		= new ArrayList();
		PreparedStatement statement	= null;
		ResultSet rs				= null;
		String lsSql				= "";
		try
		{
			//lsSql = " SELECT BW_TBL.NAME, BW_TBL.ID "+
			//		" FROM BW_TBL, BW_WB, BW_COLLAB "+
			//		" WHERE BW_TBL.NAME LIKE '%"+asCuboidpattern+"%' "+
			//		" AND BW_WB.NAME LIKE '%"+asWbpattern+"%' "+
			//		" AND BW_COLLAB.NAME LIKE '%"+asCollabpattern+"%' "+
			//		" AND BW_TBL.BW_WB_ID = BW_WB.ID "+
			//		" AND BW_WB.BW_COLLAB_ID = BW_COLLAB.ID ";

			lsSql = " SELECT BW_TBL.NAME, BW_TBL.ID "+
					" FROM BW_TBL, BW_WB, BW_COLLAB "+
					" WHERE BW_TBL.NAME LIKE ? AND BW_WB.NAME LIKE ? AND BW_COLLAB.NAME LIKE ? AND BW_TBL.BW_WB_ID = BW_WB.ID AND BW_WB.BW_COLLAB_ID = BW_COLLAB.ID ";
			//System.out.println("######### getCuboidListForPattern() " +lsSql);

			statement = aconnection.prepareStatement(lsSql);
			statement.setString(1, "%"+asCuboidpattern+"%");
			statement.setString(2, "%"+asWbpattern+"%");
			statement.setString(3, "%"+asCollabpattern+"%");
			
			rs = statement.executeQuery();
			while(rs.next())
			{
				String[] lsCuboidDetail	= new String[2];
				lsCuboidDetail[0] = rs.getString(1);
				lsCuboidDetail[1] = rs.getString(2);
				laCuboidList.add(lsCuboidDetail);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if ( rs != null ) {
					rs.close();
				}
				if ( statement != null ) {
					statement.close();
				}
			}
			catch(SQLException sqlexception1)
			{
				sqlexception1.printStackTrace();
			}
		}
		return laCuboidList;
	}

	public ArrayList getUsersWorkedOnTbl(Connection aconnection, String aspattern, int aiCuboidId)
	{
		ArrayList laCuboidList	= new ArrayList();
		PreparedStatement preparedstatement		= null;
		ResultSet resultset			= null;
		String lsSql			= "";
		try
		{
			System.out.println("##### getUsersWorkedOnTbl() {CALL BW_GET_LATEST_TRANS_ON_TBL_FOR_USER('"+aspattern+"',"+aiCuboidId+")}");
			preparedstatement = aconnection.prepareCall("{CALL BW_GET_LATEST_TRANS_ON_TBL_FOR_USER(?,?)}");
			preparedstatement.setString(1,aspattern);
			preparedstatement.setInt(2,aiCuboidId);

			resultset = preparedstatement.executeQuery();

			while ( resultset.next() )
			{
				int[] liUserDetail = new int[2];

				liUserDetail[0] = resultset.getInt(1);
				liUserDetail[1] = resultset.getInt(2);
				laCuboidList.add(liUserDetail);
			}
		}
		catch(SQLException sqlexception) {
			sqlexception.printStackTrace();
		}
		finally {
			try {

				if ( resultset != null )
						resultset.close();
			   if ( preparedstatement != null )
					preparedstatement.close();
			}
			catch(SQLException sqlexception1) {
				sqlexception1.printStackTrace();
			}
		}
		return laCuboidList;
	}

	public String[] getTransactionDetailwithUser(Connection aconnection, int aiTxId, int aiUserId)
	{
		String[] lsTransactionDetail	= new String[4];
		PreparedStatement statement		= null;
		ResultSet rs					= null;
		String lsSql					= "";
		try
		{
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			//lsSql = " SELECT TX_ID, EMAIL_ADDRESS, CREATED_ON, COMMENT_ "+
			//		" FROM BW_USER,BW_TXS "+
			//		" WHERE BW_TXS.CREATED_BY = BW_USER.ID "+
			//		" AND BW_USER.ID = "+aiUserId+
			//		" AND BW_TXS.TX_ID = "+aiTxId;

			lsSql = " SELECT TX_ID, EMAIL_ADDRESS, CREATED_ON, COMMENT_ "+
					" FROM BW_USER,BW_TXS "+
					" WHERE BW_TXS.CREATED_BY = BW_USER.ID "+
					" AND BW_USER.ID = ? AND BW_TXS.TX_ID = ?";
		
			System.out.println("##### getTransactionDetailwithUser() " +lsSql);
			statement = aconnection.prepareStatement(lsSql);
			statement.setInt(1, aiUserId);
			statement.setInt(2, aiTxId);
			rs = statement.executeQuery();

			while(rs.next())
			{

				lsTransactionDetail[0]	= rs.getString(1);
				lsTransactionDetail[1]	= rs.getString(2);
				//System.out.println("####### EMAIL_ADDRESS -> " +lsTransactionDetail[1]);
				java.sql.Timestamp createdOnDate = rs.getTimestamp(3,cal);
				SimpleDateFormat df		= new SimpleDateFormat("MMM dd, yyyy hh:mm:ssa");
				lsTransactionDetail[2]	= df.format(new java.sql.Timestamp(createdOnDate.getTime()));
				lsTransactionDetail[3]	= rs.getString(4);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if ( rs != null ) {
					rs.close();
				}
				if ( statement != null ) {
					statement.close();
				}
			}
			catch(SQLException sqlexception1)
			{
				sqlexception1.printStackTrace();
			}
		}
		return lsTransactionDetail;
	}

}
