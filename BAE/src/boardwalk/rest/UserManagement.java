package boardwalk.rest;

import io.swagger.model.ErrorRequestObject;
import io.swagger.model.User;
//import io.swagger.model.UserList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.Iterator;
//import java.lang.*;
import java.util.*;				//for Properties


import com.boardwalk.exception.BoardwalkException;
import com.boardwalk.exception.SystemException;

import boardwalk.connection.BoardwalkConnection;
import boardwalk.connection.BoardwalkConnectionManager;
import boardwalk.neighborhood.BoardwalkUser;
import boardwalk.neighborhood.BoardwalkUserManager;
//import com.boardwalk.user.*;
import com.boardwalk.member.*;
import com.boardwalk.database.*;

public class UserManagement {

	public UserManagement()
	{	
	}

	//DELETE	....DONE ACORDING TO TEMPLATE
	public static String userUserIdDelete(int userId, ArrayList <ErrorRequestObject> ErrResps, String authBase64String)
	{
		String retMsg = null;
        // get the connection
    	ErrorRequestObject erb;

		// get the connection
    	Connection connection = null;
		BoardwalkConnection bwcon = null;
		
		int nhId = -1;
		int memberId = -1;
		
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
				
		if (!ErrResps.isEmpty())
		{
			return retMsg;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

		System.out.println("MemberNode -> nhId :" + nhId);
		System.out.println("MemberNode -> memberId :" + memberId);
    	
        try
        {
            BoardwalkUser bwUser = null ;
			bwUser = BoardwalkUserManager.getUser(bwcon, userId );
			if (bwUser != null)
			{
				MemberManager.deactivateUser(  connection, userId);
	            System.out.println("Successfully desactivated userId  = " + userId);
	            retMsg = "User de-activated successfully. UserId:" + userId;
			}
			else
			{
				erb = new ErrorRequestObject();
				erb.setError("404 - User not found");
				erb.setPath("UserManagement.userUserIdDelete::getUser");
				erb.setProposedSolution("The requested user does not exist. Enter correct UserId.");
				ErrResps.add(erb);
				System.out.println("The requested user does not exist. Enter correct UserId.");				
	            retMsg = "User profile does not exists for UserId:" + userId;
			}
		}
        catch (BoardwalkException bwe)
        {
			System.out.println("Failed to deactivate User.  ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage());
			erb = new ErrorRequestObject();
			erb.setError("BoardwalkException: Contact Boardwak Support.");
			erb.setPath("UserManagement.userUserIdDelete::BoardwalkUserManager.getUser");
			erb.setProposedSolution("Boardwalk Exception. ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() + ", Solution:" +bwe.getPotentialSolution());
			ErrResps.add(erb);
            retMsg = "Failed to deactivate User:" + userId + ", Boardwalk-ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage();
        }
        catch (SystemException se)
        {
            System.out.println("Failed to deactivate User.  Error Msg:" + se.getErrorMessage()  + ", Potential Solution:" + se.getPotentialSolution());
			erb = new ErrorRequestObject();
			erb.setError("SystemException: Contact Boardwak Support.");
			erb.setPath("UserManagement.userUserIdDelete::MemberManager.deactivateUser");
			erb.setProposedSolution("Boardwalk Exception. Error Msg:" + se.getErrorMessage() + ", Solution:" +se.getPotentialSolution());
			ErrResps.add(erb);
            retMsg = "Failed to deactivate User:" + userId + ",  Error Msg:" + se.getErrorMessage()  + ", Potential Solution:" + se.getPotentialSolution();
        }
        catch (NullPointerException npe)
        {
            System.out.println("UserID not found.  Error Msg:" + npe.getMessage()   + ", Cause:" + npe.getCause());
			erb = new ErrorRequestObject();
			erb.setError("NullPointerException: Contact Boardwak Support.");
			erb.setPath("UserManagement.userUserIdDelete::BoardwalkUserManager.getUser");
			erb.setProposedSolution("NullPointerException.  Error Msg:" + npe.getMessage()  + ", Cause:" + npe.getCause());
			ErrResps.add(erb);
            retMsg = "Failed to deactivate User:" + userId + ",  Error Msg:" + npe.getMessage()  + ", Cause:" + npe.getCause();
        }
    	finally
    	{
    		try
    		{
    			connection.close();
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
        return retMsg;
	}
	
	//PUT		....DONE ACORDING TO TEMPLATE
	public static String userPut(User u, ArrayList <ErrorRequestObject> ErrResps, String authBase64String)
	{
		String retMsg = null;
		ErrorRequestObject erb;
		Connection connection = null;
		BoardwalkConnection bwcon = null;

		int nhId = -1;
		int memberId = -1;
		
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
				
		if (!ErrResps.isEmpty())
		{
			return retMsg;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

		System.out.println("MemberNode -> nhId :" + nhId);
		System.out.println("MemberNode -> memberId :" + memberId);
		
		/// CUSTOM CODE START
		int uId = u.getId().intValue();
        try
        {
            BoardwalkUser bwUser = null ;
			bwUser  = BoardwalkUserManager.getUser(bwcon, uId );

			if (bwUser != null)
			{
	        	BoardwalkUserManager.updateProfile( bwcon, uId ,  u.getEmail(), u.getFirstName(), u.getLastName() );
	            System.out.println("Successfully updated profile of  = " + u.getId());
	            retMsg = "Successfully updated profile of  = " + u.getId();
			}
			else
			{
				erb = new ErrorRequestObject();
				erb.setError("User profile does not exists for UserId:" + uId);
				erb.setPath("UserManagement.userPut::BoardwalkUserManager.getUser");
				erb.setProposedSolution("Enter Valid UserId");
				ErrResps.add(erb);
	            retMsg = "User profile does not exists for UserId:" + uId;
			}
        }
        catch (BoardwalkException bwe)
        {
			erb = new ErrorRequestObject();
			erb.setError("Failed to update user profile for UserId:" + uId);
			erb.setPath("UserManagement.userPut::BoardwalkUserManager.updateProfile");
			erb.setProposedSolution("Check User Details you are trying to Update.  ErrorCode: " + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() + ", Solution:" +bwe.getPotentialSolution() );
			ErrResps.add(erb);
            System.out.println("Failed to update user profile. ErrorCode: " + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() );
            retMsg = "Failed to update user profile. ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage();
        }
		/// CUSTOM CODE ENDS
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}        
        return retMsg;
	}
	
	//POST		....DONE ACORDING TO TEMPLATE
	public static ArrayList<User> userPost(User u, ArrayList <ErrorRequestObject> ErrResps, String authBase64String )
	{
        ArrayList <User> uList = new ArrayList<User>();
        ErrorRequestObject erb;
		// get the connection
    	Connection connection = null;
		BoardwalkConnection bwcon = null;
		
		int nhId = -1;
		int memberId = -1;
		
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
				
		if (!ErrResps.isEmpty())
		{
			return uList;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

		System.out.println("MemberNode -> nhId :" + nhId);
		System.out.println("MemberNode -> memberId :" + memberId);

		// custom logic starts
        int uId;
        try
        {
        	uId = BoardwalkUserManager.createUser( bwcon, u.getEmail(), u.getExternalId() ,u.getPassword(), u.getFirstName(), u.getLastName(),  1 );
            if (uId == -1)
            {
                System.out.println("Failed to created user with = " + u.getEmail());
            	erb = new ErrorRequestObject();
            	erb.setError("Failed to created user with = " + u.getEmail());
            	erb.setPath("UserManagement.userPost::BoardwalkUserManager.createUser");
            	erb.setProposedSolution("Status: 422. Check User Details Posted.");
            	ErrResps.add(erb);
            }
            else
            {
                System.out.println("Successfully created user with id = " + uId);
	            BoardwalkUser bwUser = null ;
				bwUser  = BoardwalkUserManager.getUser(bwcon, uId );
	
	    		io.swagger.model.User user = new User();
	    		System.out.println(bwUser.getFirstName() + "......" + bwUser.getLastName());
	    		Integer obj = new Integer(uId);
	    		user.setId(obj.longValue());
	    		user.setFirstName(bwUser.getFirstName());
	    		user.setLastName(bwUser.getLastName());
	    		user.setEmail(u.getEmail());  
	    		user.setExternalId(bwUser.getExtUserName());
				uList.add(user);
            }
        }
        catch (BoardwalkException bwe)
        {
        	erb = new ErrorRequestObject();
        	erb.setError("BoardwalkException Occured. Failed to created user with = " + u.getEmail());
        	erb.setPath("UserManagement.userPost::BoardwalkUserManager.createUser");
			erb.setProposedSolution("Boardwalk Exception. ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() + ", Solution:" +bwe.getPotentialSolution());
        	ErrResps.add(erb);
            System.out.println("BoardwalkException Occured. Failed to created user with = " + u.getEmail() + ". ErrorCode:" + bwe.getErrorCode() + ", Error Msg:" + bwe.getMessage() + ", Solution:" +bwe.getPotentialSolution());
        }
        	///////// custom code ends
	    finally
    	{
    		try
    		{
    			connection.close();
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
        return uList;
	}
	
	//GET		....DONE ACORDING TO TEMPLATE
	public static ArrayList<User> userGet(boolean active, ArrayList <ErrorRequestObject> ErrResps, String authBase64String)
	{
        ArrayList <User> uList = new ArrayList<User>();
        ErrorRequestObject erb;
        
		// get the connection
    	Connection connection = null;
		BoardwalkConnection bwcon = null;
		
		int nhId = -1;
		int memberId = -1;
		
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
				
		if (!ErrResps.isEmpty())
		{
			return uList;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

		System.out.println("MemberNode -> nhId :" + nhId);
		System.out.println("MemberNode -> memberId :" + memberId);

        int intActive;
        intActive = (active == true ? 1 : 0);
		System.out.println("active:" + active + "..........intActive:" + intActive);
		try
		{
			io.swagger.model.User user ;
			Vector userList = BoardwalkUserManager.getUserList(bwcon);
			Iterator ui = userList.iterator();
			while (ui.hasNext())
			{
				BoardwalkUser bu = (BoardwalkUser)ui.next();
				System.out.println(bu.getId() + ":" + bu.getUserName() + ":" + bu.getFirstName() + ":" + bu.getLastName());
				user = new User();
				if (bu.getActive() == intActive )
				{
					Integer obj = new Integer(bu.getId());
					user.setId(obj.longValue());
					user.setFirstName(bu.getFirstName());
					user.setLastName(bu.getLastName());
					user.setEmail(bu.getUserName());
					user.setExternalId(bu.getExtUserName());
					user.setPassword("*****");
					uList.add(user);
				}
			}
		}
		catch (BoardwalkException bwe)
		{
        	erb = new ErrorRequestObject();
        	erb.setError("getUserList_Failed");
        	erb.setPath("UserManagement.userGet::getUserList");
        	erb.setProposedSolution("Error fetching User List. Contact Boardwalk System Administrator");
        	ErrResps.add(erb);
			System.out.println("Error fetching User List");
            return uList;
		}
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
        return uList;
	}
	
	//GET       ....DONE ACORDING TO TEMPLATE
	public static User userUserIdGet(int userId, ArrayList <ErrorRequestObject> ErrResps, String authBase64String)
	{
		io.swagger.model.User user = null;
		ErrorRequestObject erb;
		
		// get the connection
    	Connection connection = null;
		BoardwalkConnection bwcon = null;
		
		int nhId = -1;
		int memberId = -1;
		
		ArrayList<Integer> memberNh = new ArrayList<Integer>();
		bwcon = bwAuthorization.AuthenticateUser(authBase64String, memberNh, ErrResps);
				
		if (!ErrResps.isEmpty())
		{
			return user;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

		System.out.println("MemberNode -> nhId :" + nhId);
		System.out.println("MemberNode -> memberId :" + memberId);

		//Custom code starts
        BoardwalkUser bwUser = null ;
		try
		{
			bwUser  = BoardwalkUserManager.getUser(bwcon, userId );
			if (bwUser != null)
			{
				user = new User();
				System.out.println(bwUser.getFirstName() + "......" + bwUser.getLastName());
				Integer obj = new Integer(userId);
				user.setId(obj.longValue());
				user.setFirstName(bwUser.getFirstName());
				user.setLastName(bwUser.getLastName());
				user.setEmail(bwUser.getUserName());
				user.setExternalId(bwUser.getExtUserName());
				user.setPassword("*********");
			}
			else
			{
				erb = new ErrorRequestObject();
				erb.setError("404 - User not found");
				erb.setPath("UserManagement.userUserIdGet::getUser");
				erb.setProposedSolution("The requested user does not exist. Enter correct UserId.");
				ErrResps.add(erb);
				System.out.println("The requested user does not exist. Enter correct UserId.");
			}
		}
		catch (BoardwalkException bwe)
		{
			erb = new ErrorRequestObject();
			erb.setError("userUserIdGet_Failed");
			erb.setPath("UserManagement.userUserIdGet::getUser");
			erb.setProposedSolution("Error fetching User. Contact Boardwalk System Administrator");
			ErrResps.add(erb);
			System.out.println("Error fetching neighborhood");
		}
		catch (NullPointerException npe)
		{
			System.out.println("Error fetching neighborhood");
		}
		//Custom code ends
		finally
		{
			try
			{
				connection.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}			
		return user;
	}
}