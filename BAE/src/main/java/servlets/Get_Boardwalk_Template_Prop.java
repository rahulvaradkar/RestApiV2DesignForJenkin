package servlets;

/* *
	This servlet will check if there is any valid significant update after this particular Tx_id.
 */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.boardwalk.util.*;
import com.boardwalk.database.*;
import com.boardwalk.table.*;
import boardwalk.table.*;
import com.boardwalk.exception.*;
import boardwalk.connection.*;
import com.boardwalk.query.*;
import boardwalk.common.*;
import java.util.regex.Matcher;

import java.sql.*;      // JDBC package
import javax.sql.*;     // extended JDBC package
import java.text.*;

public class Get_Boardwalk_Template_Prop  extends xlService implements SingleThreadModel
{
	private static String CALL_BW_EXTERNAL_QUERY="{CALL BW_EXTERNAL_QUERY(?)}";

	Connection			connection			= null;
	PreparedStatement	preparedstatement	= null;
	Statement			statementOne		= null;
	Statement			statement			= null;
	ResultSet			resultset			= null;
	ResultSet			resultsetOne		= null;

	String	lsReportTitle	= "";
	String	lsQueryString	= "";
	String	lsQueryID		= "";
	String	lsParameters	= "";
	int		liUserId		= -1;
	long	liDateTime		= -1;
	String	lsUserName		= "";

	public Get_Boardwalk_Template_Prop()
	{
		System.out.println("Inside Get_Boardwalk_Template_Prop" );
		liDateTime = Calendar.getInstance().getTimeInMillis();
	}

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		// Single instance of Connection object
	}

    public void doPost (HttpServletRequest request,	HttpServletResponse response)   throws ServletException, IOException
    {
		try
		{
			String fullString = getRequestBuffer(request).toString();
			//System.out.println(" fullString "+fullString);

			String[] receivedTokens = fullString.split(Seperator);

			liUserId		= Integer.parseInt(receivedTokens[0]); // User Id
			lsUserName		= receivedTokens[1]; // User Name
			lsReportTitle	= receivedTokens[2]; // Report Title
			lsQueryID		= receivedTokens[3]; // SQL query ID 
			lsParameters	= receivedTokens[4]; // Query Parameters

			//System.out.println("Get_Boardwalk_Template_Prop: userId			: "+ liUserId);
			//System.out.println("Get_Boardwalk_Template_Prop: lsUserName		: "+ lsUserName);
			//System.out.println("Get_Boardwalk_Template_Prop: lsReportTitle	: "+ lsReportTitle);
			//System.out.println("Get_Boardwalk_Template_Prop: lsQueryID		: "+ lsQueryID);
			//System.out.println("Get_Boardwalk_Template_Prop: lsParameters		: "+ lsParameters);

			//Modified by Lakshman on 20180314 to fix SQL Injection - START
			/*
			lsQueryString = getExternalDefinedQuery(Integer.parseInt(lsQueryID), lsParameters);
			System.out.println(" In servlet Get_Boardwalk_Template_Prop: lsQueryString: "+ lsQueryString);

			String lsResponseStr = getPartValidationBuffer(lsReportTitle, lsQueryString, liUserId);
			System.out.println(" lsResponseStr = "+lsResponseStr);
			*/
			String lsResponseStr = getPartValidationBuffer(lsReportTitle, liUserId, Integer.parseInt(lsQueryID), lsParameters);
			//Modified by Lakshman on 20180314 to fix SQL Injection - END

			commitResponseBuffer(lsResponseStr, response);
		}
		catch (SQLException sql)
		{
			sql.printStackTrace();
		   return;
		}
		finally
		{
			//
		}
	}

    public void doGet (HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException
    {
		doPost(request, response);
    }

	//Commented by Lakshman on 20180314 to fix SQL Injection - START
	/*
	public String getExternalDefinedQuery(int QueryId, String lsParameters) throws SQLException
	{
		System.out.println("inside getExternalDefinedQuery");
		Statement		  statement		= null;
		ResultSet		  resultset		= null;
		PreparedStatement ps = null;
		String queryCall = null;
		String queryParam = null;

		//System.out.println("lsParameters = " + lsParameters);
		StringTokenizer prmtr = new StringTokenizer(lsParameters, "|");


		try
		{
			DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
			connection = databaseloader.getConnection();

			statement = null;
			resultset = null;
			ps = connection.prepareStatement(CALL_BW_EXTERNAL_QUERY);
			ps.setInt(1, QueryId);
			resultset = ps.executeQuery();

			while (resultset.next())
			{
				queryCall = resultset.getString(3);
				queryParam = resultset.getString(4);
			}

			ps.close();
			ps = null;
			resultset.close();
			resultset = null;

			//System.out.println("queryCall : " + queryCall);
			//System.out.println("queryParam : " + queryParam);
			//System.out.println("queryCall before insert param : " + queryCall);

			if (queryParam != null)
			{

				StringTokenizer qryprmtr = new StringTokenizer(queryParam, "|");

				String paramType=null;
				String paramValue=null;
				String dQuote = "\'";	//mod on 20171402 by Sohum Team to accept huge string as parameters

				while (prmtr.hasMoreElements())
				{	
					paramType = qryprmtr.nextToken();
					paramValue = prmtr.nextToken();
					//System.out.println("parameter name: " + paramType);
					//System.out.println("parameter value: " + paramValue);

					
					if (paramType.equalsIgnoreCase("string"))
					{
						paramValue = dQuote.concat(paramValue).concat(dQuote);
						queryCall = queryCall.replaceFirst("\\?", Matcher.quoteReplacement(paramValue) );
					}
					else if (paramType.equalsIgnoreCase("int"))
					{
						queryCall = queryCall.replaceFirst("\\?", Matcher.quoteReplacement(paramValue));
					}
					//System.out.println("queryCall during insert param : " + queryCall);
				}
			}
		
			//System.out.println("queryCall after insert param : " + queryCall);

		}

		catch( Exception e ) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (resultset != null)
					resultset.close();
				ps = null;
				resultset = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
   		}

		return queryCall;
	}
	*/
	//Commented by Lakshman on 20180314 to fix SQL Injection - END

	//Method is modified by Lakshman on 20180314 to fix SQL Injection
	public String getPartValidationBuffer(String asRepTitle, int aiUserId, int QueryId, String lsParameters)throws SQLException
	{
		try
		{
			DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
			connection = databaseloader.getConnection();
			
			if(connection == null)
				System.out.println(" Connection is Null" );
		}
		catch (SQLException sql)
		{
			sql.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
        }

		Statement		  	statement			= null;
		ResultSet		  	resultSetExtQuery	= null;
		PreparedStatement 	ps 					= null;
		String 				queryCall 			= null;
		String 				queryParam 			= null;
		ResultSetMetaData	rsMetaData			= null;

		int liColCount				= -1;
		int liRowCount				= 0;

		String colName				= "";
		String colType				= "";
		String lsRowCountQuery		= "";

		HashMap colTypeCollection	= new HashMap();

		StringBuffer resData		= new StringBuffer(10000000);
		StringBuffer resDataTemp	= new StringBuffer(10000000);
		StringBuffer accData		= new StringBuffer();
		StringBuffer fmlData		= new StringBuffer();
		StringBuffer resHeader		= new StringBuffer();

		try
		{
			if(connection == null)
			{
				System.out.println("Connection is Closed " + connection);
			}
			else
			{
				StringTokenizer prmtr = new StringTokenizer(lsParameters, "|");

				resultSetExtQuery	= null;
				preparedstatement 	= null;
				resultset 			= null;

				ps = connection.prepareStatement(CALL_BW_EXTERNAL_QUERY);
				ps.setInt(1, QueryId);
				resultSetExtQuery = ps.executeQuery();

				while (resultSetExtQuery.next())
				{
					queryCall	= resultSetExtQuery.getString(3);
					queryParam	= resultSetExtQuery.getString(4);
				}

				ps.close();
				ps = null;
				resultSetExtQuery.close();
				resultSetExtQuery = null;

				System.out.println("Get_Boardwalk_Template_Prop:queryCall: " + queryCall);
				//System.out.println("Get_Boardwalk_Template_Prop:queryParam: " + queryParam);

				preparedstatement = connection.prepareStatement(queryCall);

				if (queryParam != null)
				{
					StringTokenizer qryprmtr = new StringTokenizer(queryParam, "|");

					int		loopNumber	= 1;
					String	paramValue	= null;
					String	paramType	= null;
					String	dQuote 		= "\'";	//mod on 20171402 by Sohum Team to accept huge string as parameters

					while (qryprmtr.hasMoreElements())
					{
						paramType	= qryprmtr.nextToken();
						//System.out.println("Get_Boardwalk_Template_Prop:getExternalDefinedQuery:paramType	: " + paramType);

						paramValue 	= prmtr.nextToken();
						//System.out.println("Get_Boardwalk_Template_Prop:getExternalDefinedQuery:paramValue	: " + paramValue);

						if (paramType.equalsIgnoreCase("string"))
						{
							//paramValue = dQuote.concat(paramValue).concat(dQuote);
							//queryCall = queryCall.replaceFirst("\\?", Matcher.quoteReplacement(paramValue) );
							preparedstatement.setString(loopNumber, paramValue);
						}
						else if (paramType.equalsIgnoreCase("int"))
						{
							//queryCall = queryCall.replaceFirst("\\?", Matcher.quoteReplacement(paramValue));
							preparedstatement.setInt(loopNumber, Integer.parseInt(paramValue));
						}

						loopNumber++;
					}
				}

				resultset 	= preparedstatement.executeQuery();
				rsMetaData	= resultset.getMetaData();
				liColCount	= rsMetaData.getColumnCount();

				for(int i=1; i <= liColCount  ; i++)
				{
					colName = rsMetaData.getColumnName(i);
					colType = rsMetaData.getColumnTypeName(i);

					colTypeCollection.put(new Integer(i) , colType);
					resData.append(colName + Seperator); // Only column names will be sufficent
				}
				resData.append(ContentDelimeter);

				String lsColType	= "";
				String cellval		= "";
				String cellFormula	= "";
				int	cellAccess		= 2;

				//String lsCellRC[][] = new String[liRowCount][liColCount];
				int CurrRow			= -1;

				ArrayList RepRows = new ArrayList(1000);
				ArrayList SingleRow = null; 

				while(resultset.next())
				{
					CurrRow	++;
					SingleRow = new ArrayList(liColCount);

					for(int i = 1 ; i <= liColCount; i++)
					{
						lsColType = (String) colTypeCollection.get(i);
						cellval = "";
						//System.out.println(" lsColType of the coumns  "+lsColType+" and value is "+resultset.getString(i));

						if(lsColType.equals("int identity") || lsColType.equals("int") || lsColType.equals("bit") || lsColType.equals("bigint")) //for getting int type
						{
							cellval = resultset.getInt(i)+"";
							if(BoardwalkUtility.checkIfNullOrBlank(cellval) || cellval.equals("null"))
								cellval = "";
						}
						else if(lsColType.equals("datetime")) //for getting date time type
						{
							if(resultset.getDate(i) == null)
								cellval = "";
							else
								cellval = resultset.getDate(i)+"";
						}
						else if(lsColType.equals("varchar") || lsColType.equals("nvarchar") || lsColType.equals("text")) //for getting vharchar type
						{
							if(BoardwalkUtility.checkIfNullOrBlank(resultset.getString(i)))
								cellval = "";
							else
								cellval = resultset.getString(i);
						}
						else if(lsColType.equals("double")) //for getting double type
						{
							cellval = resultset.getDouble(i)+"";
							if(BoardwalkUtility.checkIfNullOrBlank(cellval) || cellval.equals("null"))
								cellval = "";
						}
						else if(lsColType.equals("float")) //for getting float type
						{
							cellval = resultset.getFloat(i)+"";
							if(BoardwalkUtility.checkIfNullOrBlank(cellval) || cellval.equals("null"))
								cellval = "";
						}
						else if(lsColType.equals("image")) //for getting image type
						{
							//Will this case be required
							//BufferedInputStream in = new BufferedInputStream(resultset.getBinaryStream("DOC")
							cellval = "image";
						}

						if(BoardwalkUtility.checkIfNullOrBlank(cellval))
							cellval = "";

						//lsCellRC[CurrRow][i-1]= cellval;
						SingleRow.add(cellval);
					}
					RepRows.add(SingleRow);
				}

				liRowCount = CurrRow+1;
				String lsCellRC[][] = new String[liRowCount][liColCount];
				ArrayList ReadRow = null;

				for(int rowCount = 0 ; rowCount < liRowCount ; rowCount++)
				{
					ReadRow =  (ArrayList)	RepRows.get(rowCount); 
					for( int colCount = 0 ; colCount < liColCount ; colCount++)
					{
						lsCellRC[rowCount][colCount] = (String) ReadRow.get(colCount);
					}
				}

				System.out.println("Get_Boardwalk_Template_Prop:Total Selected Rows: "+liRowCount);

				cellFormula = "";
				cellAccess	= 2; // DefaultWrite

				// prepare the buffer
				for(int colindex=0 ; colindex < liColCount; colindex++)
				{
					for(int rowindex=0; rowindex < liRowCount; rowindex++)
					{
						cellval = lsCellRC[rowindex][colindex];
						cellFormula = cellval;

						//System.out.println(" cellval  "+cellval);

						if (rowindex == liRowCount-1) // last cell of the column
						{
							resDataTemp.append(cellval);
							fmlData.append(cellFormula);
							accData.append(cellAccess);
						}
						else
						{
							resDataTemp.append(cellval + Seperator);
							fmlData.append(cellFormula + Seperator);
							accData.append(cellAccess + Seperator);
						}
					}
					resDataTemp.append(ContentDelimeter);
					fmlData.append(ContentDelimeter);
					accData.append(ContentDelimeter);
				}

				// now prepare the contents of the rowid
				for(int i=0; i < liRowCount ; i++)
					resData.append(i+1 + Seperator);

				resData.append(ContentDelimeter);

				// Tempdata which holds the cell data
				resData.append(resDataTemp.toString());
				// Formula Data
				resData.append(fmlData.toString());
				// access
				resData.append(accData.toString());

				resHeader.append("Success" + Seperator);
				resHeader.append(asRepTitle + Seperator);
				resHeader.append("For Report Sub title " + Seperator);
				resHeader.append(liDateTime+ Seperator);  // Report Date
				resHeader.append(aiUserId + Seperator); // Report Created by
				resHeader.append(liRowCount + Seperator); // number of Rows
				resHeader.append(colTypeCollection.size() + Seperator); // number of column
			}
		}
		catch(SQLException sqlexception)
		{
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (ps != null)
					ps.close();
				if (resultSetExtQuery != null)
					resultSetExtQuery.close();

				ps = null;
				resultSetExtQuery = null;

				if(resultsetOne != null)
					resultsetOne.close();
				if(statementOne != null)
					statementOne.close();
				if(resultset != null)
					resultset.close();
				if(preparedstatement != null)
					preparedstatement.close();
				if(statement != null)
					statement.close();
				if(connection != null)
					connection.close();
			}
			catch( SQLException sql )
			{
					sql.printStackTrace();
			}
		}

		//System.out.println(" resData.toString() "+resHeader.toString() + ContentDelimeter + resData.toString());

		return resHeader.toString() + ContentDelimeter + resData.toString();
	}
}
