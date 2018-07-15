/*
 * BoardwalkRequestReader.java
 *
 * Created on Nov 25, 2010
 */

package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.binary.Base64;
/**
 *
 * @author  Pavan Divekar
 */
public class BoardwalkRequestReaderTest
{
	int _pos = 0;
	ZipInputStream zipIn = null;
	BufferedReader reader = null;
	com.boardwalk.util.UnicodeInputStream uis = null;
	
	public BoardwalkRequestReaderTest() throws IOException
	{
		try
		{
			StringBuffer sb = new StringBuffer("UEsDBBQAAAAIADSJmUzUykAzKQAAADUAAAASAAAAYndjQnVmZmVyMF83MDU1NDc1e797v6GB\r\n" + 
					"gSFjFiOYMjIAAgszRiMQZErON2RMzjdiMmQ0BmMjRhMwBgBQSwECFAAUAAAACAA0iZlM1MpA\r\n" + 
					"MykAAAA1AAAAEgAAAAAAAAAAACAAAAAAAAAAYndjQnVmZmVyMF83MDU1NDc1UEsFBgAAAAAB\r\n" + 
					"AAEAQAAAAFkAAAAAAA==");
			
			zipIn = new ZipInputStream(new ByteArrayInputStream(Base64.decodeBase64(sb.toString().getBytes())));
			zipIn.getNextEntry();
			// wrap around UnicodeInputStream to take care of Java bug related to BOM mark in UTF-8 encoded files
//			String bw_client = ((HttpServletRequest)request).getHeader("X-client");
//			if (bw_client != null && !bw_client.equals("MacExcel"))
//			{
//				uis = new com.boardwalk.util.UnicodeInputStream(zipIn);
//				sb = null;
//			}

			reader = new BufferedReader(new InputStreamReader(zipIn, "UTF-8"));
			//reader = new BufferedReader(new InputStreamReader(zipIn, "ISO-8859-1")); 
			System.out.println("reader = " + reader);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	// returns data upto next content delimeter
	// returns null if reached end of request buffer
	public String getNextContent() throws IOException
	{
		String retString = null;
		StringBuffer sb = null;
		int ch;

		boolean foundContent = false;
		while ((ch = reader.read()) > -1)
		{
			if (sb == null)
			{
				sb = new StringBuffer();
			}

			if (ch == 2)
			{
				foundContent = true;
				//System.out.println("Found next content = " + sb.toString ());
				break;
			}
			else
			{

				sb.append((char)ch);
			}
		}

		if (sb == null) // no more content
		{
			return null;
		}
		else
		{
			//System.out.println("getNextContent >> " + sb.toString ());
			return sb.toString();
		}
	}

	public void close() throws IOException
	{
		try
		{
			zipIn.closeEntry();
			zipIn.close();
			reader.close();
			//uis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
