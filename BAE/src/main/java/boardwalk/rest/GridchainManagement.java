package boardwalk.rest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.boardwalk.database.TransactionManager;
import com.boardwalk.excel.xlColumn_import;
import com.boardwalk.excel.xlErrorNew;
import com.boardwalk.exception.BoardwalkException;
import com.boardwalk.table.ColumnManager;
import com.boardwalk.table.RowManager;
import com.boardwalk.table.TableAccessList;
import com.boardwalk.table.TableAccessRequest;
import com.boardwalk.table.TableInfo;
import com.boardwalk.table.TableManager;
import com.boardwalk.table.TableRowInfo;
import com.boardwalk.table.TableViewManager;

import boardwalk.connection.BoardwalkConnection;
import io.swagger.model.Column;
import io.swagger.model.ColumnChain;
import io.swagger.model.ErrorRequestObject;
import io.swagger.model.FormulaValue;
import io.swagger.model.GridChain;
import io.swagger.model.GridInfo;
import io.swagger.model.Row;
import io.swagger.model.RowChain;
import io.swagger.model.TransactionChain;

public class GridchainManagement {

	
	public GridchainManagement()
	{	
	}

    //@GET
    //@Path("/{gridId}")
	public static GridChain gridchainGridIdGet(int gridId,  ArrayList<ErrorRequestObject> ErrResps, String authBase64String)
	{
		GridChain gc = new GridChain();
/*
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
			return gc;
		}

		connection = bwcon.getConnection();
		memberId = memberNh.get(0);
		nhId = memberNh.get(1);

    	Vector xlErrorCells = new Vector(); 
    	try
    	{
    		
    		int userId = bwcon.getUserId();
    		
    		//System.out.println("cellBufferRequest.toString() as follows...................");
    		//System.out.println(cellBufferRequest.toString());

    		GridInfo ginfo = new GridInfo();

    		int wbId;
    		int collabId;
		
    		ArrayList<RowChain> rca = new ArrayList<RowChain>();
    		ArrayList<ColumnChain> cca = new ArrayList<ColumnChain>();
    		ArrayList<TransactionChain> tca = new ArrayList<TransactionChain>();
    		ArrayList<FormulaValue> fva = new ArrayList<FormulaValue>();
    		
    		PreparedStatement stmt		= null;
    		TransactionManager tm = null;
    		String query = null;

			//	Access control checks
			TableInfo tinfo = TableManager.getTableInfo(connection, userId, gridId);
			TableAccessList ftal = TableViewManager.getSuggestedAccess(connection, tinfo, userId, memberId, nhId);
			collabId = tinfo.getCollaborationId();
			wbId = tinfo.getWhiteboardId();

			String view;
			
			view = ftal.getSuggestedViewPreferenceBasedOnAccess();
			System.out.println("Suggested view pref = " + view);
			if(view == null)
				view = "None";


			// Check access control :: TBD
			int raccess = 1;
			int ACLFromDB = ftal.getACL();
			TableAccessRequest wAccess = new TableAccessRequest(gridId, view, true);
			int wACL = wAccess.getACL();
			int awACL = wACL & ACLFromDB;
			if (awACL == wACL)
			{
				raccess = 2;
				System.out.println("Rows have write access");
			}
			else
			{
				System.out.println("Rows are readonly");
			}

			// This happens only when the user has no access to the said table
			// But in this case the user does not have provision to select the table
			if(view.equals("None"))
			{
				xlErrorCells.add( new xlErrorNew(gridId, 0, 0, 10005));
				throw new BoardwalkException(10005);		//it was 11005.
			}			

			int criteriaTableId = TableViewManager.getCriteriaTable(connection, gridId, userId);
			System.out.println("Using criterea table id = " + criteriaTableId);
			int accessTableId = TableViewManager.getAccessTable(connection, gridId, userId);
			HashSet restrColumnList = new HashSet();
			if (accessTableId > 0) 
			{
				System.out.println("Using access table id = " + accessTableId);
				// read the access for the user
				restrColumnList = TableViewManager.getRestrictedColumnsForImport(connection, gridId, accessTableId, userId);
			}
			// Get the columns
			Vector colv = ColumnManager.getXlColumnsForImport(connection, gridId, userId, memberId);
			Iterator ci = colv.iterator();

			// columns
			float previousColumnSequence = -1;
			int previousColumnid = -1;
			gridCols = new ArrayList <Column>();
			columnArray = new ArrayList <Integer>();
			
			for (int c = 0; c < colv.size(); c++) 
			{
				xlColumn_import col = (xlColumn_import) colv.elementAt(c);
				if (restrColumnList != null && restrColumnList.size() > 0) 	{
					if (restrColumnList.contains(new Integer(col.getId()))) 
					{
						System.out.println("Skip restricted column " + col.getName());
						continue;
					}
				}
				if (maxTransactionId < col.getCreationTid()) {
					maxTransactionId = col.getCreationTid();
				}

				if (maxTransactionId < col.getAccessTid()) {
					maxTransactionId = col.getAccessTid();
				}
				gridCol = new Column();
				gridCol.setActive(true);
				gridCol.setId(col.getId()); 
				gridCol.setName(col.getName()); 
				gridCol.setPreviousColumnid(previousColumnid);
				gridCol.setPreviousColumnSequence( new BigDecimal(previousColumnSequence));
				gridCol.setSeqNo(  new BigDecimal(col.getSequenceNumber())); 
				gridCol.setTid(col.getCreationTid());
				gridCols.add(gridCol);
				columnArray.add(col.getId());
				
				previousColumnSequence = col.getSequenceNumber();
				previousColumnid = col.getId();
			}
			gcb.setNewColumnArray( gridCols);				//gcb is part of LINK IMPORT CELL BUFFER
    		//cellBufferRequest.setColumns(gridCols);
    		//cellBufferRequest.setColumnArray(columnArray);

			
			// Get the rows
			String lsRowQuery = ""; // Row query String
			// is the view dynamic (not in the criteria table), starts with ?
			boolean viewIsDynamic = false;
			if (view.indexOf("?") == 0) {
				System.out.println("View is dynamic = " + view);
				viewIsDynamic = true;
			}
			TableRowInfo tbrowInfo = null;
			
			//System.out.println("RowManager.getTableRows....userId:" + userId + "nhId:" + nhId + "baselineId:" + baselineId + "view:"+ view);
			 Condition Added By Asfak - START - 29 Jun 2014 
			if (criteriaTableId == -1 && !viewIsDynamic) {
				System.out.println("RowManager.getTableRows....1");
				tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
			} else if (criteriaTableId > 0 && viewIsDynamic) {
				System.out.println("RowManager.getTableRows....2");
				tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
			} else {
				System.out.println("RowManager.getTableRows....3");
				tbrowInfo = RowManager.getTableRows(connection, gridId, userId, nhId, baselineId, view, 1, -1, -1);
			}
			Vector rowv = tbrowInfo.getRowVector();
			// rows
			gridRows = new ArrayList <Row>();
			rowArray = new ArrayList <Integer>();
			
			int previousRowid = -1;
			int previousRowSequence = -1;
			for (int r = 0; r < rowv.size(); r++) 
			{
				com.boardwalk.table.Row rowObject = (com.boardwalk.table.Row) rowv.elementAt(r);
				rowArray.add(rowObject.getId());

				gridRow = new Row();
				gridRow.setCreationTid(rowObject.getCreationTid());
				gridRow.setCreaterId(rowObject.getCreatorUserId());
				gridRow.setOwnerName(rowObject.getOwnerName());
				gridRow.setOwnershipAssignedTid(rowObject.getOwnershipAssignedTid());
				gridRow.setOwnerName(rowObject.getOwnerName());
				gridRow.setOwnerId(rowObject.getOwnerUserId());
				gridRow.setActive((rowObject.getIsActive()== 1? true : false));
				gridRow.setRowName(rowObject.getName()); 	
	    		gridRow.setId(rowObject.getId()); 
	    		gridRow.setPreviousRowid(  previousRowid);
	    		gridRow.setPreviousRowSequence(previousRowSequence); 
	    		Float obj = new Float(rowObject.getSequenceNumber());
	    		gridRow.setSeqNo(obj.intValue()); 
	    		
	    		previousRowid = rowObject.getId() ;
	    		previousRowSequence = obj.intValue();
				
				if (maxTransactionId < rowObject.getCreationTid()) {
					maxTransactionId = rowObject.getCreationTid();
				}

				if (maxTransactionId < rowObject.getOwnershipAssignedTid()) {
					maxTransactionId = rowObject.getOwnershipAssignedTid();
				}
	    		gridRow.setTid(maxTransactionId);

	    		gridRows.add(gridRow);
			}
			gcb.setNewRowArray(gridRows);
    		//cellBufferRequest.setRows(gridRows);
    		//cellBufferRequest.setRowArray(rowArray);
    		
			
			
		gc.setCreationDatetime(creationDatetime);
		gc.setCreationTxId(creationTxId);
		gc.setCreatorId(creatorId);
		gc.setCreatorName(creatorName);
		gc.setGridCells(gridCells);
		gc.setGridColumns(gridColumns);
		gc.setGridFormulas(gridFormulas);
		gc.setGridRows(gridRows);
		gc.setGridTransactions(gridTransactions);
*/
		
		return gc;
		
	}
}
