/*
 * BWServlet.java
 *
 * Created on June 17, 2002, 7:48 PM
 */

package servlets;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;
import java.lang.Exception;

import javax.mail.*;

import java.util.zip.*;

import com.boardwalk.database.*;
import com.boardwalk.exception.*;
import com.boardwalk.user.*;
import com.boardwalk.member.*;
import com.boardwalk.table.*;
import com.boardwalk.query.*; //Modified by Lakshman on 20171108 to avoid self joins on BW_CELL for performance gain

import java.sql.*;                  // JDBC package

import javax.sql.*;                 // extended JDBC package

import com.boardwalk.util.*;
import com.boardwalk.neighborhood.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.*;

/**
 *
 * @author  Sarang Kulkarni
 */
public abstract class xlService extends HttpServlet implements SingleThreadModel
{
	public static final String Seperator = new Character ((char)1).toString ();
	public static final String ContentDelimeter = new Character((char)2).toString();
	public static final  String Diff_Seperator = new Character((char)3).toString();
	static boolean formatofrequest;
	boolean dbTblLockObtained = false;
	private String sRequestBufferType;
	long prevTime = -1;
	public float getElapsedTime()
	{
		if (prevTime == -1)
			prevTime = System.currentTimeMillis();
		// Get elapsed time in seconds
		float elapsedTimeSec = (System.currentTimeMillis() - prevTime) / 1000F;

		// reset time
		prevTime = System.currentTimeMillis();

		return elapsedTimeSec;
	}

	protected void oldSheetCheck(Connection connection, int tableId, int memberId, int userId, int exportTid, String view)	throws BoardwalkException
	{
		Boolean oldSheetCheckVar = true;
		int exportTidMax = -1;
		try
		{
			System.out.println("Performing old sheet check using BW_USER_EXPORT_TID**************");
			System.out.println("tableId = " + tableId);
			System.out.println("userId = " + userId);
			System.out.println("exportTid = " + exportTid);
			PreparedStatement stmt = connection.prepareStatement(
									" SELECT TX_ID " +
									" FROM   BW_USER_EXPORT_TID " +
									" WHERE  BW_TBL_ID = ? " +
									" AND	 BW_USER_ID = ? " +
									" AND	 TX_ID > ? " 
			);
			stmt.setInt(1, tableId);
			stmt.setInt(2, userId);
			stmt.setInt(3, exportTid);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
			{
				exportTidMax = rs.getInt(1);
				System.out.println("exportTidMax " + exportTidMax);
				if (!(view == null || view.trim().equals("") || view.equalsIgnoreCase("Latest")))
				{
					//Modified by Lakshman on 20171108 to avoid self joins on BW_CELL for performance gain - START
					String lsSql = QueryMaker.getTransactionsForUserView(view);
					stmt = connection.prepareStatement(lsSql);
					
					System.out.println("g2...... view query " + view);
					System.out.println("getTransactionsForUserView Query:" + lsSql);

					stmt.setInt(1, userId);
					stmt.setInt(2, userId);
					stmt.setInt(3, tableId);
					stmt.setInt(4, exportTid);
					
					stmt.setInt(5, tableId);
					stmt.setInt(6, userId);
					stmt.setInt(7, memberId);
					//Modified by Lakshman on 20171108 to avoid self joins on BW_CELL for performance gain - END
					ResultSet rs1 = stmt.executeQuery();
					if (rs1.next())
					{
						exportTidMax = rs1.getInt(1);
						System.out.println("IN NEW BLOCK BY G2 exportTidMax " + exportTidMax);
						//throw new BoardwalkException(15003);
						oldSheetCheckVar = false;
					}
				}
				else 
				{
					oldSheetCheckVar = false;
				}
			}
			//else
			//{
			//	System.out.println("Old Sheet Check Passed **********************");
			//}
			stmt.close();
			stmt = null;
			rs.close();
			rs = null;
		}
		catch (SQLException e)
		{
			oldSheetCheckVar = false;
			e.printStackTrace();
		}
		if (oldSheetCheckVar == false)
		{
			System.out.println("Old Sheet Check Failed : Last Export Tid = " + exportTidMax);
			throw new BoardwalkException(15003);
		}
		else 
		{
			System.out.println("Old Sheet Check Passed **********************");
		}
	}

/* Old method without unicode support Sanjeev 5/2/2012
	protected StringBuffer getRequestBuffer(ServletRequest request) throws IOException 
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = request.getReader();
		String line = new String();
		line = br.readLine();
		while (line != null)
		{
			sb.append(line);
			line = br.readLine();
			if (line != null)
			{
				sb.append("\n");
			}
		}

		ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(Base64.decodeBase64(sb.toString().getBytes())));
		zipIn.getNextEntry();

		byte[] buffer = new byte[512];
		int len;
		sb = new StringBuffer(); // clear string buffer
		while ((len = zipIn.read(buffer)) > 0)
		{
			sb.append(new String(buffer, 0, len));
		}

		zipIn.closeEntry();
		zipIn.close();
		
		return sb;
	} */

/* Old method without unicode support Sanjeev 5/2/2012
	protected void commitResponseBuffer(String buffer, ServletResponse response) throws IOException 
	{
		response.setContentType("text/plain");
		System.out.println("Uncompressed Response Buffer : Size = " + buffer.length());
		// write to ZipOutputStream
		ByteArrayOutputStream bos = new ByteArrayOutputStream ();
		ZipOutputStream out = new ZipOutputStream(bos);
		out.putNextEntry(new ZipEntry("response.txt"));
		out.write(buffer.getBytes("ISO-8859-1"));
		//out.flush();
		out.closeEntry();
		out.close ();
		bos.close();
		// encode to Base64 string
		String b64String = Base64.encodeBase64String(bos.toByteArray());
		// set the response
		ServletOutputStream servletOut = response.getOutputStream();
		response.setContentLength(b64String.length());
		System.out.println("Compressed Response Buffer : Size = " + b64String.length());
		servletOut.print(b64String);
		servletOut.close();
	} */
	protected BoardwalkRequestReader getRequestReader(ServletRequest request) throws IOException, DataFormatException
	{
		/*
		 * Modified : 25-09-2017
		 * BoardwalkRequestReader class needs old zip stream so uncompressed zlib and zip into zipstream format
		 */
		String sRequestBufferType = request.getParameter("buffertype"); //Mckensey Fix for uncompressed buffer
		StringBuffer sb=getRequestBuffer(request);
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(bos);
		out.putNextEntry(new ZipEntry("response.txt"));
		out.write(sb.toString().getBytes());
		out.closeEntry();
		out.close ();
		bos.close();
    	//  encode to Base64 string
		String b64String = Base64.encodeBase64String(bos.toByteArray());
		return new BoardwalkRequestReader(b64String,1);
		//return new BoardwalkRequestReader(request);
	}

	protected void lockTableForUpdate(Connection connection, int tableId) throws BoardwalkException
	{
		if (dbTblLockObtained == false)
		{
			try
			{
				TableManager.lockTableForUpdate(connection, tableId);
			}
			catch (SQLException sq)
			{
				//BoardwalkMessage bwMsg = bwMsgs.getBoardwalkMessage(12008 );
				//xlErrorCells.add( new xlError( tableId,-1,-1,-1,-1,  12008, "TABLE UPDATE EXCEPTION", bwMsg.getCause() + "," +  bwMsg.getPotentialSolution()));
				throw new BoardwalkException(12011);
			}
			dbTblLockObtained = true;
		}
	}

	protected StringBuffer getRequestBuffer(ServletRequest request) throws IOException 
	{
		sRequestBufferType=request.getParameter("buffertype");
		StringBuffer sb = null;
		try
		{
			sb = new StringBuffer();
			BufferedReader br = request.getReader();
			String line = new String();
			line = br.readLine();

			while (line != null)
			{
				sb.append(line);
				line = br.readLine();
				if (line != null)
				{
					sb.append("\n");
				}
			}
			
			String bw_client = ((HttpServletRequest)request).getHeader("X-client");
			formatofrequest =isValidZlibFormat(Base64.decodeBase64(sb.toString().getBytes()));

			if(formatofrequest)
			{
				byte[] UncompressedbyteArray=decompress(Base64.decodeBase64(sb.toString().getBytes()));
				System.out.println("UnCompressed Byte Array :"+UncompressedbyteArray);
				String d=new String(UncompressedbyteArray,"UTF-16LE");
				System.out.println("UnCompressed String :"+d);
				sb.delete(0, sb.length());
				sb.append(d);
			}
			else
			{
				ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(Base64.decodeBase64(sb.toString().getBytes())));
				zipIn.getNextEntry();
				System.out.println("string " + new String(Base64.decodeBase64(sb.toString().getBytes())));			
				com.boardwalk.util.UnicodeInputStream uis = null;
				if (bw_client != null && !bw_client.equals("MacExcel"))
				{				
					 uis = new com.boardwalk.util.UnicodeInputStream(zipIn);
					sb = null;
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				IOUtils.copy(zipIn, out);
				sb = new StringBuffer();
				sb.append(out.toString("UTF-8"));
				zipIn.closeEntry();
				zipIn.close();
				if (uis != null) uis.close();
				out.close();
			}		

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb;
	}

	protected void commitResponseBuffer(String buffer, ServletResponse response) throws IOException 
	{
		if (formatofrequest)
		{		
			PrintWriter pw = null;
			response.setContentType("text/plain");
			System.out.println("Uncompressed Response Buffer : Size = " + buffer.length());			
			System.out.println("the buffer type is"+sRequestBufferType);
			// Mckensey Fix for uncompressed buffer
			if (sRequestBufferType!=null && sRequestBufferType.equals("uncompressed")) {
				PrintWriter outputStream  = null;
				outputStream = response.getWriter();
				outputStream.print(buffer);
				System.out.println("creating response for uncompressed buffer");
				return;
			}
			byte[] bufferbytearray = buffer.getBytes();
			System.out.println("bufferbytearray :"+bufferbytearray);
			byte[] compressedbytearray=compress(bufferbytearray);
			// write to ZipOutputStream
			ByteArrayOutputStream bos = new ByteArrayOutputStream ();		
			String b64String = Base64.encodeBase64String(compressedbytearray);
			b64String = b64String + "," + bufferbytearray.length;	
			response.setContentLength(b64String.length());
			System.out.println("Compressed Response Buffer : Size = " + b64String.length());
			pw = response.getWriter();
			pw.print(b64String);
		}	
		else
		{
			response.setContentType("text/plain");
			System.out.println("Uncompressed Response Buffer : Size = " + buffer.length());
			// write to ZipOutputStream
			ByteArrayOutputStream bos = new ByteArrayOutputStream ();
			ZipOutputStream out = new ZipOutputStream(bos);
			out.putNextEntry(new ZipEntry("response.txt"));
			out.write(buffer.getBytes("UTF-8"));
			//out.flush();
			out.closeEntry();
			out.close ();
			bos.close();
			// encode to Base64 string
			String b64String = Base64.encodeBase64String(bos.toByteArray());
			// set the response
			ServletOutputStream servletOut = response.getOutputStream();
			response.setContentLength(b64String.length());
			System.out.println("Compressed Response Buffer : Size = " + b64String.length());
			servletOut.print(b64String);
			servletOut.close();
		}
	}

	public static byte[] compress(byte[] data) throws IOException
 	{  
	 	System.out.println("Byte Array Length :"+data.length);
	 	double a= data.length+((data.length*0.001)+15);
	 	ByteArrayOutputStream baos=new ByteArrayOutputStream();
	 	int b=(int)a;
	 	byte[] output = new byte[b];
	 	Deflater compresser = new Deflater();
	 	compresser.setInput(data);
	 	compresser.finish();
	 	int compressedDataLength = compresser.deflate(output);
	 	compresser.end();
	 	System.out.println("compressedDataLength :" +compressedDataLength);
	 	byte[] output1=new byte[compressedDataLength];
	 	baos.write(output,0,compressedDataLength);
	 	output1=baos.toByteArray();
	 	System.out.println("Original: " + data.length / 1024 + " Kb");  
	 	System.out.println("Compressed: " + output.length / 1024 + " Kb");  
              return output1;  
 	}

 	public static byte[] decompress(byte[] data) throws IOException, DataFormatException 
 	{  
		   Inflater inflater = new Inflater();   
		   inflater.setInput(data);  
		   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
		   byte[] buffer = new byte[1024];  
		   while (!inflater.finished()) 
		   {  
		 	 int count = inflater.inflate(buffer);  
		    	outputStream.write(buffer, 0, count);  
		   }  
		   outputStream.close();  
		   byte[] output = outputStream.toByteArray();  
		   System.out.println("Original: " + data.length);  
		   System.out.println("Compressed: " + output.length);  
		   return output;  
 	}
 	
 	//this function checks the request come from client is it zlib format or shell format
	public boolean isValidZlibFormat(byte getServerCompressedArray[]){
    
		int i;
		int firstbytevalue;
		int secondbytevalue;
		int validResult1;
		int validResult2;

		firstbytevalue = getServerCompressedArray[0];
		secondbytevalue = getServerCompressedArray[1];

		int a=( (((((firstbytevalue*256)+(secondbytevalue)) % (31))) & ((firstbytevalue) & (127))) )	;		
		if( ((((firstbytevalue*256)+(secondbytevalue)) % (31)) & ((firstbytevalue & (127)))) == (16))			
		{
			System.out.println("This is a valid zlib compressed format");
			return true;
		}		
		else
		{
			System.out.println("This is not valid zlib compressed format");
			return false;
		}
	}

}