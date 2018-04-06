package servlets;
/*
 *  This presents a list of collaboration available to a user
 */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.boardwalk.database.DatabaseLoader;
import com.boardwalk.database.*;
import com.boardwalk.user.UserManager;
import com.boardwalk.user.User;
import com.boardwalk.member.Member;
import com.boardwalk.exception.*;

import java.sql.*;                  // JDBC package
import javax.sql.*;                 // extended JDBC packa

public class httpt_vb_Login extends xlService implements SingleThreadModel {

    int userId;
    String userName;
    //String userPassword; // auth fix -shirish 20150716
	String templateMode;
	String nhHierarchy = null;
	String manifestId = null;
    int memberId = -1;
    int nhid = -1;
	String nhName = "";

    public void service (HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
		//System.out.println( "came to httpt login servlet");
		userName = (String)request.getAttribute("userName");
		//userPassword = (String)request.getAttribute("userPassword");
		templateMode = (String)request.getAttribute("templateMode"); //Added to get the Membership ID in case of Multiple Membership
		nhHierarchy = (String)request.getAttribute("nhHierarchy"); //Added to get the Membership ID in case of Multiple Membership
		manifestId = (String)request.getAttribute("manifestId"); //Added to get the Membership ID in case of Multiple Membership
		
		if (manifestId == null)
		{
			manifestId = "-1";
		}

		System.out.println("httpt_vb_Login : templateMode 	= " + templateMode);
		System.out.println("httpt_vb_Login : nhHierarchy 	= " + nhHierarchy);
		System.out.println("httpt_vb_Login : manifestId 	= " + manifestId);

        // auth fix shirish 20150716
		//System.out.println("httpt_vb_Login : userPassword = " + userPassword);
		if (  userName == null  || userName == "" 
				/*|| userPassword ==null || userPassword == "" */)
		{
			commitResponseBuffer("failure", response);
		}
		else
		{
			Connection connection = null;
			Connection connection1 = null;

            try
            {
                DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
                connection = databaseloader.getConnection();

/*				userId = -1;
                userId = UserManager.authenticateUser(connection, userName,userPassword);
*/

                userId = -1;
                User u = UserManager.getUser(connection, userName);

                if (u != null)
				{
					System.out.println(" check before ");
	                userId = u.getId();
					// auth fix shirish 20150716
     				HttpSession hs = request.getSession(true);
        			hs.setAttribute("userId", userId);
				}

				System.out.println(" check before "+userId);

                if ( userId > 0 )
                {
					DatabaseLoader databaseloader1 = new DatabaseLoader(new Properties());
					connection1 = databaseloader1.getConnection();

					//Changes related to Login Enhancements for Password Complexity and User Authentication on 20170524 (And fix for Issue Id: 14200)
					UserManager.userLoginSuccess(connection1, userId);
					
					System.out.println(" Hashtable memberships check before "+userId);
					Hashtable memberships  = UserManager.getMembershipsForUser( connection1, userId );
					System.out.println(" Hashtable memberships check Aftr "+userId);
					Enumeration memberIds = memberships.keys();

					if (  memberships.size() == 1 )
					{

						memberId =((Integer) memberIds.nextElement()).intValue();
						System.out.println("httpt_vb_Login: Single Member Id: "+memberId);
						
						nhid =((Member) memberships.get( new Integer(memberId) )).getNeighborhoodId();
						nhName = ((Member) memberships.get( new Integer(memberId) )).getNeighborhoodName();

						commitResponseBuffer("Success:" + userId + xlService.Seperator + memberId + xlService.Seperator + nhid + xlService.Seperator + nhName + xlService.Seperator , response);
						System.out.println("Success:" + userId + xlService.Seperator + memberId + xlService.Seperator + nhid + xlService.Seperator + nhName + xlService.Seperator);
					}
					else
					if (  memberships.size() > 1 )
					{
						//Added to get the Membership ID in case of Multiple Membership
						memberId = UserManager.checkMembershipStatus(connection, userId, templateMode, nhHierarchy, Integer.parseInt(manifestId));
						System.out.println("httpt_vb_Login: Multiple Member Id: "+memberId);

						if (memberId != -1)
						{
							nhid =((Member) memberships.get(new Integer(memberId))).getNeighborhoodId();
							nhName = ((Member) memberships.get(new Integer(memberId))).getNeighborhoodName();
							
							commitResponseBuffer("Success:" + userId + xlService.Seperator + memberId + xlService.Seperator + nhid + xlService.Seperator + nhName + xlService.Seperator, response);
							System.out.println("Success:" + userId + xlService.Seperator + memberId + xlService.Seperator + nhid + xlService.Seperator + nhName + xlService.Seperator);
						}
						else
						{
							BoardwalkMessage bwmsg= new BoardwalkMessages().getBoardwalkMessage( 11007 );
							commitResponseBuffer("failure" + xlService.ContentDelimeter + bwmsg.getCause() + "," + bwmsg.getPotentialSolution(), response );
						}
					}
					else
					if (  memberships.size() == 0 )
					{
						// User isn't a member of any neighborhood ...if the refering URL allows access to public users then we can show it
						// if  not the user has to join a relevant neighborhood
						 //servletOut.println("Success:"+userId+xlService.Seperator);
						 BoardwalkMessage bwmsg= new BoardwalkMessages().getBoardwalkMessage( 11001 );
						 commitResponseBuffer("failure"
						 					+ xlService.ContentDelimeter
											+ bwmsg.getCause() + "," + bwmsg.getPotentialSolution(), response );
					}

                }
                else
                {
					BoardwalkMessage bwmsg = null;
					System.out.println(" Check here userId >>>>>>>>>"+userId);

					if (userId == -1)
					{
						bwmsg= new BoardwalkMessages().getBoardwalkMessage( 11004 );
					}
					if (userId == 0)
					{
						bwmsg= new BoardwalkMessages().getBoardwalkMessage( 11006 );
					}

					 commitResponseBuffer("failure"
                    										+ xlService.ContentDelimeter
                    										+ bwmsg.getCause() + "," + bwmsg.getPotentialSolution(),
                    									response);
				}

            }
            catch ( Exception e )
            {
               e.printStackTrace();
            }
            finally
            {
              try
              {
				  if ( connection != null )
                		connection.close();
               	if ( connection1 != null )
                connection1.close();
              }
              catch ( SQLException sql )
              {
                sql.printStackTrace();
              }
            }
    	}
    }
}
