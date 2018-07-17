<%@ page isThreadSafe="false" %>
<%@ page contentType="application/vnd.ms-excel" import ="java.lang.Integer,java.util.regex.*, com.boardwalk.database.Transaction, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.query.*"%>

<%

boolean tableIsReadOnly = false;
boolean isTableLocked = false;
boolean portlet = false;

int tableId = ((Integer)request.getAttribute("TableId")).intValue();
int noOfColumns = 0;

Integer baselineIdInteger = (Integer)request.getAttribute("baselineId");
int baselineId = -1;

if ( baselineIdInteger != null )
{
	baselineId = baselineIdInteger.intValue();
	if ( baselineId > 0 )
	{
		tableIsReadOnly = true;
	}
}


System.out.println("baselineId = " + baselineId);

String portletStr = request.getParameter("portlet");
if (portletStr != null)
{
	portlet = true;
	System.out.println("Portal Based Access");
}



String tableName = (String)request.getAttribute("TableName");
TableContents tbc = (TableContents)request.getAttribute("TableContents");
if ( tbc != null )
{
	Vector vecNoOfcolumns = tbc.getColumnNames();
	if ( vecNoOfcolumns != null )
	{
		noOfColumns = vecNoOfcolumns.size();
	}
}

TableInfo tbi = (TableInfo)request.getAttribute("TableInfo");
TableConfiguration tableConf = (TableConfiguration)request.getAttribute("tableConfiguration");
ColumnConfiguration columnConf = (ColumnConfiguration)request.getAttribute("columnConfiguration");
Hashtable upRows = null;
Hashtable downRows = null;
Hashtable noChangeRows = null;
Hashtable newRows = null;
Hashtable noValueRows = null;
String hide_upRows = null;
String hide_downRows = null;
String hide_noChangeRows= null ;
String hide_newRows = null;
String hide_existRows = null;
String hide_noValueRows  = null;
boolean showTableChanges = false;
boolean showTableChangesForOrderedColumn = false;
Transaction maxTableTransaction = null;
String tableUpdatedBy = "";
String tableUpdatedOn = "";
String tableUpdatedComment = "No Comment";
Integer deltaColumnId = null;
DeltaColumnConfiguration deltaConf = null;

boolean showTableColumnDelta = false;
int bucketSize = 50;

// Find the cookie that pertains to that book
Cookie[] cookies = request.getCookies();
for(int i=0; i < cookies.length; i++) {
	Cookie thisCookie = cookies[i];
	if (thisCookie.getName().equals("boardwalktable"+tableId+"bucketSize")){
	    bucketSize = Integer.parseInt(thisCookie.getValue());
	}
}


String bucketSizeStr = request.getParameter("bucketSize");
if (bucketSizeStr != null)
{
	bucketSize = Integer.parseInt(bucketSizeStr);
}
int bucketNum = 0;
String bucketNumStr = request.getParameter("bucketNum");
if (bucketNumStr != null)
{
	bucketNum = Integer.parseInt(bucketNumStr);
}

System.out.println("Now rendering the jsp;");
if ( tableConf != null  )
{

	if ( request.getAttribute("showTableColumnDelta") != null )
	{
		if ( ((String)request.getAttribute("showTableColumnDelta")).trim().equalsIgnoreCase("true") )
		showTableColumnDelta = true;
		deltaColumnId = (Integer)request.getAttribute("deltaColumnId");
		System.out.println("showTableColumnDelta = true;");
		deltaConf = (DeltaColumnConfiguration) request.getAttribute("deltaColumnConf");
		upRows = deltaConf.getUpRows();
		downRows = deltaConf.getDownRows();
		noChangeRows = deltaConf.getNoChangeRows();
		newRows = deltaConf.getNewRows();
		noValueRows = deltaConf.getNoValueRows();

		hide_upRows = request.getParameter("hide_upRows");
		hide_downRows = request.getParameter("hide_downRows");
		hide_noChangeRows = request.getParameter("hide_noChangeRows");
		hide_newRows = request.getParameter("hide_newRows");
		hide_noValueRows = request.getParameter("show_noValueRows");
		System.out.println(hide_upRows+","+ hide_downRows+","+ hide_downRows+","+ hide_noChangeRows+","+ hide_newRows+","+ hide_noValueRows);
	}
	else
	{
			showTableChanges = true;
			hide_newRows = request.getParameter("hide_newRows");
			hide_existRows = request.getParameter("hide_existRows");
			System.out.println("showTableChanges = true;");
	}
}

if ( columnConf != null  )
{
	showTableChangesForOrderedColumn = true;

	upRows = columnConf.getUpRows();
	downRows = columnConf.getDownRows();
	noChangeRows = columnConf.getNoChangeRows();
	newRows = columnConf.getNewRows();
	noValueRows = columnConf.getNoValueRows();

	System.out.println("showTableChangesForOrderedColumn = true;");
	System.out.println("uprows = " + upRows);
	hide_upRows = request.getParameter("hide_upRows");
	hide_downRows = request.getParameter("hide_downRows");
	hide_noChangeRows = request.getParameter("hide_noChangeRows");
	hide_newRows = request.getParameter("hide_newRows");
	hide_noValueRows = request.getParameter("show_noValueRows");
}




String ViewPreference = (String)request.getAttribute("ViewPreference");
String QueryPreference =(String)request.getAttribute("QueryPreference");

Hashtable  UIPreferences = (Hashtable)request.getAttribute("UIPreferences");
Hashtable tablesUsingLkpForColumn = (Hashtable)request.getAttribute("TablesUsingLkpForCol");
//System.out.println("edit_table.jsp:: tablesUsingLkpForColumn = " + tablesUsingLkpForColumn);

boolean isInDesignMode = false;
if ( ViewPreference.equals(ViewPreferenceType.DESIGN))
isInDesignMode = true;

TableAccessList tbACL = null;
Hashtable columns = new Hashtable();
Vector columnNames = new Vector();
Hashtable cellsByRowId =new Hashtable();
Vector columnsSortedBySeqNum = new Vector();
Vector   rowids =new Vector();
Hashtable rowObjsById = null;



if ( tbc != null )
{
	tbACL = tbc.getTableAccessList();
	columns = tbc.getColumnsByColumnId();
	columnNames = tbc.getColumnNames();
	columnsSortedBySeqNum = tbc.getColumnsSortedBySeqNum();

	System.out.println(" column size " + columns.size() + " " + columnNames.size() + " " + columnNames.size() + " " );


	cellsByRowId = tbc.getCellsByRowId();
	rowids = tbc.getRowIds();
	//request.setAttribute("heading", tbi.getTableName());


	if (ViewPreference.equals("LATEST") &&  !tbACL.canWriteLatestOfTable() && tbACL.canReadLatestOfTable() )
	{
		tableIsReadOnly = true;
	}
	else
	if (ViewPreference.equals("LATEST_VIEW_OF_ALL_USERS") && tbACL.canReadLatestViewOfAll() )
	{
		tableIsReadOnly = true;
	}
	else
	if (ViewPreference.equals("LATEST_VIEW_OF_ALL_CHILDREN") && tbACL.canReadLatestViewOfAllChildren() )
	{
		tableIsReadOnly = true;
	}
	else
	if (ViewPreference.equals("LOOKUP")  &&  !tbACL.canWriteLatestOfTable() && tbACL.canReadLatestOfTable() )
	{
		tableIsReadOnly = true;
	} // merge
	else
	if (ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH")  &&  !tbACL.canReadWriteLatestofMyGroup() && tbACL.canReadLatestofMyGroup() )
	{
		tableIsReadOnly = true;
	}
	else
	if (ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD")  &&  !tbACL.canReadWriteLatestofMyGroupAndImmediateChildren() && tbACL.canReadLatestofMyGroupAndImmediateChildren() )
	{
		tableIsReadOnly = true;
	}
	else
	if (ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD")  &&  !tbACL.canReadWriteLatestofMyGroupAndAllChildren() && tbACL.canReadLatestofMyGroupAndAllChildren() )
	{
		tableIsReadOnly = true;
	}

	rowObjsById = tbc.getRowObjsByRowId();
	isTableLocked =  tbi.isLocked();
}
else
{
	System.out.println("edit_table.jsp::tbcl is null");
}

// columns filters
System.out.println("Finding Columns to Hide");
Hashtable hiddenColumnIds = new Hashtable();
Hashtable filterColumnIds = new Hashtable();
Enumeration paramNames = request.getParameterNames();
while( paramNames.hasMoreElements())
{
	String paramName = (String)paramNames.nextElement();
	if ( paramName.startsWith("cc") )
	{
		String ccIdStr  = paramName.substring(2);
		int ccId = Integer.parseInt(ccIdStr);
		String [] colPreferences = request.getParameterValues(paramName);
		Vector colFilters = new Vector();
		for (int i = 0; i < colPreferences.length; i++)
		{
			String colPreference = colPreferences[i];
			if (colPreference.equals("__hidden__")){
				hiddenColumnIds.put(new Integer(ccId), colPreference);
				System.out.println("Hide Column: " + ccId + colPreference);
			}
			else
			{
				colFilters.addElement(colPreference);
				System.out.println("Filter Column: " + ccId + colPreference);
			}
		}

		if (colFilters.size() > 0)
		{
			filterColumnIds.put(new Integer(ccId), colFilters);
		}

	}
}
boolean anyColumnHidden = false;
if (hiddenColumnIds.size() > 0)
{
	anyColumnHidden=true;
}

%>

<html>

<LINK REL=STYLESHEET type="text/css" HREF="css/stylesheet.css" TITLE="stylesheet">

<body>
<table style="table-layout=auto;" width="100%" >
<tr>
<td class="body">
<!--start main page-->
<form name="EditTable" method="post" action="MyTables"  >

<!---------The main table ----------------------------------------------------------------------------------->


<table  id="bwTable"  class="body" border="1" bordercolor="#eeeeee" cellspacing="0" cellpadding="0">
<colgroup id="bwColumns">
<%
if (columnsSortedBySeqNum.size() > 0)
{
	for ( int columnIndex = 0;
	      columnIndex< columnsSortedBySeqNum.size()+1 - hiddenColumnIds.size() +
	      ((showTableChanges == true ||  showTableChangesForOrderedColumn == true)?1:0);
	      columnIndex++ )
	{
%>
<col id="Col<%=columnIndex%>">
<%
	}
}
%>
</colgroup>
<colgroup>
<col id="SystemColumns" span="4">
</colgroup>
<thead>

<tr name="bwHeadings" id="bwHeadings">
<%
int visibleColumnIndex = 0;
if (showTableChanges == true ||  showTableChangesForOrderedColumn == true)
{
visibleColumnIndex++;
%>
<th width="1%">
<img  style="display:inline;"  src="images/alert.gif"   onmouseover ="this.title='Change State'" >
</th>

<%
} // if show state changes

if (columnsSortedBySeqNum.size() > 0)
{
	for ( int columnIndex = 0; columnIndex< columnsSortedBySeqNum.size(); columnIndex++ )
	{
		Column c = (Column)columnsSortedBySeqNum.elementAt(columnIndex);;

		String columnName = c.getColumnName();
		String visibleColumnName = columnName;

		// if the column is hidden...
		String colPreference = (String)hiddenColumnIds.get(new Integer(c.getId()));
		if ( colPreference != null && colPreference.equals("__hidden__"))
		{

			// add hidden field for hiding columns
			%>

			<input type=hidden name="cc<%=c.getId()%>" id="cc<%=c.getId()%>" value="__hidden__">

			<%
			System.out.println("Hiding column heading for column " + c.getId());
			continue;
		}
		// add hidden fields for the column filters, if any exist
		Vector colFilters = (Vector)filterColumnIds.get(new Integer(c.getId()));
		if ( colFilters != null)
		{
			for (int iFil = 0; iFil < colFilters.size() ; iFil++)
			{
				String colFilterStr = (String)colFilters.elementAt(iFil);
				%>
						<input type=hidden name="cc<%=c.getId()%>" id="cc<%=c.getId()%>" value="<%=colFilterStr%>">
				<%
			}
		}
		c.print();

	%>

  <th id="Column<%=c.getId()%>"
		defaultValue="<%=c.getDefaultValueAsString()%>" columnType="<%=c.getType()%>"
		name="<%=visibleColumnName%>"
		class="BWTableHead"
		onclick="this.blur(); return highlightColumn('bwTableContents', <%=visibleColumnIndex + 1%>, this);"
		orderedTableName = "<%=c.getOrderedTableName()%>"
		orderedTableId = "<%=c.getOrderedTableId()%>"
		orderColumnId = "<%=c.getOrderedColumnId()%>"
		orderType="<%=c.getOrderType()%>"
		lookupTableId  = "<%=c.getLookupTableId()%>"
		lookupTableName = "<%=c.getLookupTableName()%>"
		lookupColumnId = "<%=c.getLookupColumnId()%>"
		selectIndex="<%=visibleColumnIndex + 1%>"
		canWrite="<%=c.canWrite()%>"

		<%
		if ( c.getOrderType().equals("STATE" ) )
		{
		%>
			title="This is an ordered column with stage  levels"
		<%
		}
		%>


	  >
	  <%=visibleColumnName%>
	  <%
	  	if ( c.getOrderType().equals("STATE" ) )
	        {
	  %>
         <a href="MyTables?action=editTable&tableId=<%=c.getOrderedTableId()%>&ViewPreference=LATEST"    ><img  style="display:inline;"  src="images/alert.jpg"   onmouseover ="this.title='Change State'" > </a>
	  <%
	  }
	  %>
	  <%
		if ( c.getLookupTableId() > 0 && c.getLookupColumnId() > 0)
		{
	  %>
		   <a style="display:inline;" href="MyTables?action=editTable&tableId=<%=c.getLookupTableId()%>&ViewPreference=LATEST"    >
		   <img  style="display:inline;" height="12" width="12" src="images/refersto.gif"   onmouseover ="this.title='Show Lookup Table'" > </a>
	  <%
	  	}

	  	if (tablesUsingLkpForColumn != null)
	  	{
			Vector vtulc = (Vector)tablesUsingLkpForColumn.get(new Integer(c.getId()));
			if (vtulc != null)
			{
				System.out.println("Column with id : " + c.getId() + " is being used as lookup column");


	  %>
		   <a style="display:inline;" href="MyTables?action=showLkpReferences&tableId=<%=tableId%>&lkpColumnId=<%=c.getId()%>">
		   <img  style="display:inline;" height="12" width="12" src="images/referdby.gif"   onmouseover ="this.title='Show Tables referring this column for lookup'" >
		   </a>
	  <%
	  		}
	  	}
	  %>


  </th>
<%
	visibleColumnIndex++;
	} // end for

%>


   <th name="User" class="BWTableHeadUserDetails"
		    ondblclick="this.blur(); return sortTable('bwTableContents',  <%=visibleColumnIndex %>, true);" >Owned By
    </th>


   <th name="UpdatedBy" class="BWTableHeadUserDetails"
			ondblclick="this.blur(); return sortTable('bwTableContents',  <%=visibleColumnIndex++ %>, true);" > Updated By
    </th>

    <th name="UpdatedOn" class="BWTableHeadUserDetails"
			ondblclick="this.blur(); return sortTable('bwTableContents',  <%=visibleColumnIndex++ %>, true);" >Updated On
    </th>
	<th name="Comments" class="BWTableHeadUserDetails"
			ondblclick="this.blur(); return sortTable('bwTableContents',  <%=visibleColumnIndex %>, true);" >Comments
    </th>

<%

} // end if
%>
</tr>
</thead>


<tbody  id="bwTableContents"   >
<%
	int cellId = 0;
	int dispRows = 0;
	int processedRows = 0;
	if ( rowids.size() > 0 )
	{

		Transaction maxRowTransaction = null;
		String versionCellUserName  = "";
		String lastUpdate = "";
		String rowOwner = "";

		int rowsToSkip = bucketNum * bucketSize;

		//for ( int rowIndex=0; rowIndex < rowids.size(); rowIndex++ )
		for ( int rowIndex=0; rowIndex < rowids.size() ; rowIndex++ )
		{
			boolean upRow = false;
			boolean downRow = false;
			boolean noChangeRow = false;


			Integer a_rowIntegerId = (Integer)rowids.elementAt( rowIndex);
			// System.out.println("Displaying row Id " + a_rowIntegerId);
			Vector cells = (Vector) ( (Vector)cellsByRowId.get(a_rowIntegerId)).elementAt(0);
			int rowId = ((Integer)rowids.elementAt(rowIndex)).intValue();
			Row row = (Row)rowObjsById.get( a_rowIntegerId );
			rowOwner = row.getOwnerName();

			// check if you need to filter out the row
			boolean displayRow = true;
			for ( int cellIndex=0; cellIndex < cells.size(); cellIndex++ )
			{
				VersionedCell cell = (VersionedCell) cells.elementAt( cellIndex );
				// if the column is filtered
				Vector colFilters = (Vector)filterColumnIds.get(new Integer(cell.getColumnId()));
				if ( colFilters != null)
				{
					for (int iFil = 0; iFil < colFilters.size() ; iFil++)
					{
						String colFilterStr = (String)colFilters.elementAt(iFil);
						System.out.println("Column id=" + cell.getColumnId() + " is filtered by the criterion : " + colFilterStr );
						StringTokenizer st = new StringTokenizer(colFilterStr, ":");
						String colFilterCondition = st.nextToken();
						String colFilterStrVal = st.nextToken();
						String columnType = cell.getType();
						if (columnType.equals("STRING") || columnType.equals("TABLE"))
						{
							boolean negativeMatch = false;
							Pattern pattern = null;

							if (colFilterCondition.equals("equals"))
							{
								pattern = Pattern.compile(colFilterStrVal);
								negativeMatch = false;
							}
							else if (colFilterCondition.equals("doesNotEqual"))
							{
								pattern = Pattern.compile(colFilterStrVal);
								negativeMatch = true;

							}
							else if (colFilterCondition.equals("beginsWith"))
							{
								pattern = Pattern.compile("^"+colFilterStrVal+"[\\s\\S]*");
								negativeMatch = false;
							}
							else if (colFilterCondition.equals("doesNotBeginWith"))
							{
								pattern = Pattern.compile("^"+colFilterStrVal+"[\\s\\S]*");
								negativeMatch = true;
							}
							else if (colFilterCondition.equals("endsWith"))
							{
								pattern = Pattern.compile("[\\s\\S]*"+colFilterStrVal+"$");
								negativeMatch = false;
							}
							else if (colFilterCondition.equals("doesNotEndWith"))
							{
								pattern = Pattern.compile("[\\s\\S]*"+colFilterStrVal+"$");
								negativeMatch = true;;
							}
							else if (colFilterCondition.equals("contains"))
							{
								pattern = Pattern.compile("[\\s\\S]*"+colFilterStrVal+"[\\s\\S]*");
								negativeMatch = false;
							}
							else if (colFilterCondition.equals("doesNotContain"))
							{
								pattern = Pattern.compile("[\\s\\S]*"+colFilterStrVal+"[\\s\\S]*");
								negativeMatch = true;
							}

							String cellValue = cell.getValueAsString();
							Matcher m = pattern.matcher(cellValue);
							boolean matchFound = m.find();
							if (negativeMatch == false)
							{
								if (matchFound == false )
								{
									displayRow = false;
								}
							}
							else
							{
								if (matchFound == true )
								{
									displayRow = false;
								}

							}

						}
						else if (columnType.equals("INTEGER") || columnType.equals("FLOAT"))
						{

							double cellValue ;
							double cellFilterValue;
							if (columnType.equals("INTEGER"))
							{
								Integer cellIntValue = new Integer(cell.getIntValue());
								cellValue = cellIntValue.doubleValue();
								Integer cellFilterIntValue = new Integer(colFilterStrVal);
								cellFilterValue = cellFilterIntValue.doubleValue();
							}
							else
							{
								Double cellDblValue = new Double(cell.getDoubleValue());
								cellValue = cellDblValue.doubleValue();
								Double cellFilterDblValue = new Double(colFilterStrVal);
								cellFilterValue = cellFilterDblValue.doubleValue();
							}

							System.out.println("Checking if cell Value : " + cellValue + " is " + colFilterCondition +
										" " + cellFilterValue);

							if (colFilterCondition.equals("equals"))
							{
								if (cellValue != cellFilterValue)
								{
									displayRow = false;
								}
							}
							else if (colFilterCondition.equals("doesNotEqual"))
							{
								if (cellValue == cellFilterValue)
								{
									displayRow = false;
								}

							}
							else if (colFilterCondition.equals("isGreaterThan"))
							{
								if (cellValue <= cellFilterValue)
								{
									displayRow = false;
								}
							}
							else if (colFilterCondition.equals("isGreaterThanOrEqualTo"))
							{
								if (cellValue < cellFilterValue)
								{
									displayRow = false;
								}
							}
							else if (colFilterCondition.equals("isLessThan"))
							{
								if (cellValue >= cellFilterValue)
								{
									displayRow = false;
								}
							}
							else if (colFilterCondition.equals("isLessThanOrEqualTo"))
							{
								if (cellValue > cellFilterValue)
								{
									displayRow = false;
								}
							}
						}// else if
					}

					//see if the cell is OK or needs to be filtered out
					//if (!cell.getValueAsString().equals(colPreference)) {
					//	displayRow = false;
					//} //end if
				}// end if
			}// end for

			// check for filtering in state column
			if (showTableChanges)
			{
				if (tableConf.getRows( ).get( new Integer(rowId) )  == null )
				{
					if (hide_newRows != null )
					{
						displayRow = false;
					}
				}
				else
				{
					if (hide_existRows != null )
					{
						displayRow = false;
					}

				}
			}

			if ( showTableChangesForOrderedColumn || showTableColumnDelta )
			{
				if ( upRows.get( new Integer(rowId) )  != null )
				{
					if (hide_upRows != null )
					{
						displayRow = false;
					}
				}
				else
				if ( downRows.get( new Integer(rowId) )  != null )
				{
					if (hide_downRows != null )
					{
						displayRow = false;
					}
				}
				else
				if ( noChangeRows.get( new Integer(rowId) )  != null )
				{
					if (hide_noChangeRows != null )
					{
						displayRow = false;
					}
				}
				else
				if ( newRows.get( new Integer(rowId) )  != null )
				{
					if (hide_newRows != null )
					{
						displayRow = false;
					}
				}
				else
				{
					if (hide_noValueRows != null )
					{
						displayRow = false;
					}
				}

			}



			if (!displayRow) {
				System.out.println("Hiding row " + a_rowIntegerId);
				continue;
			}



			processedRows++;
			// show only the next bucket
			if (processedRows < rowsToSkip + 1){
				continue;
			}

			if (dispRows >= bucketSize)
			{
				continue;
			}

			dispRows++;
%>
<tr  id="row<%=rowId%>"  name="row<%=rowId%>" >

   	<%
	 	if ( showTableChanges )
	 	{

	 %>
	 <td>
	 <%
	 		if ( tableConf.getRows( ).get( new Integer(rowId) )  == null )
				{

			 %>
				<img  style="display:inline;"  src="images/new.jpg"  onmouseover ="this.title='New row'"  name="rowId"  value="<%=rowId%>">
			 <%
				}
				else
				{
				%>
				<img  style="display:inline;"  src="images/grey-dot.jpg"   onmouseover ="this.title='Existing row'"  name="rowId"  value="<%=rowId%>">
				<%

   				}
   	%>
   	</td>
   	<%
   		}
   		else
		if ( showTableColumnDelta   )
		{

			if ( upRows.get( new Integer(rowId) )  != null )
			{
				upRow = true;
			}
			else
			if ( downRows.get( new Integer(rowId) )  != null )
			{
				downRow = true;
			}
			else
			{
				noChangeRow = true;
			}
   		}
   		else
   		if ( showTableChangesForOrderedColumn )
		{
	%>
	<td>
	<%
			if ( upRows.get( new Integer(rowId) )  != null )
				{
			 %>
				<img  style="display:inline;"  src="images/green-dot.jpg"   onmouseover ="this.title='State moved up'"  name="rowId"  value="<%=rowId%>">
			 <%
				}
			else
			if ( downRows.get( new Integer(rowId) )  != null )
				{
			 %>
				<img  style="display:inline;"  src="images/red-dot.jpg"   onmouseover ="this.title='State moved down'"  name="rowId"  value="<%=rowId%>">
			 <%
				}
			else
			if ( noChangeRows.get( new Integer(rowId) )  != null )
				{
			 %>
				<img  style="display:inline;"  src="images/grey-dot.jpg"   onmouseover ="this.title='State is unchanged'"  name="rowId"  value="<%=rowId%>">
			 <%
				}
			else
			if ( newRows.get( new Integer(rowId) )  != null )
				{
			 %>
				<img  style="display:inline;"  src="images/new.jpg"   onmouseover ="this.title='State is new'"  name="rowId"  value="<%=rowId%>">
			 <%
			}
			else
			{
			%>
				<img  style="display:inline;"  src="images/clear.gif"   onmouseover ="this.title='The cell does not have a valid ordered value'"  name="rowId"  value="<%=rowId%>">
			<%

			}
	%>
	</td>
	<%

   	}

for ( int cellIndex=0; cellIndex < cells.size(); cellIndex++ )
{
	VersionedCell cell = (VersionedCell) cells.elementAt( cellIndex );
	VersionedCell oldCellValue = null;
	String titleString = "";
	if ( showTableChanges || showTableColumnDelta  )
	{
		oldCellValue = (VersionedCell)tableConf.getCellsByCellId().get(  new Integer ( cell.getId() )    );

		if  ( oldCellValue != null )
		{
			//oldCellValue.printCell();
			titleString = cell.getTransaction().getCreatedByUserAddress()+ "," +  cell.getTransaction().getCreatedOn() + ":\n" +
			" Changed the  value from  " +oldCellValue.getValueAsString() +"  to " + cell.getValueAsString() ;
		}
		else
		{
			//System.out.println("oldCellValue is null ");
			titleString = "Value changed by " +
							cell.getTransaction().getCreatedByUserAddress() +
							" on " + cell.getTransaction().getCreatedOn();

		}

		//System.out.println(" for rowid " + rowId + " cellid = " +cell.getId()  + " titlestring " + titleString);
	}
	else
	{
		titleString = "Value changed by " + cell.getTransaction().getCreatedByUserAddress() +
						" on " + cell.getTransaction().getCreatedOn();


	}
	if (maxRowTransaction == null)
	{
		maxRowTransaction = cell.getTransaction();
	}
	else if ( maxRowTransaction.getId() < cell.getTransaction().getId() )
	{
		maxRowTransaction = cell.getTransaction();
	}

			// max table transaction
	if (maxTableTransaction == null)
	{
		maxTableTransaction = cell.getTransaction();
	}
	else if ( maxTableTransaction.getId() < cell.getTransaction().getId() )
	{
		maxTableTransaction = cell.getTransaction();
	}


	// if the column is hidden...
	String colPreference = (String)hiddenColumnIds.get(new Integer(cell.getColumnId()));
	if ( colPreference != null && colPreference.equals("__hidden__"))
	{
		System.out.println("Hiding cell for column " + cell.getColumnId());
		continue;
	}

	boolean canWrite = ((Column)columns.get(  new Integer(cell.getColumnId()))).canWrite();
	//System.out.println("canWrite = " + canWrite);

%>
	<td  title='<%=titleString%>'  ID="cell_<%=cell.getId()%>" name="<%=cell.getId()%>"
		 columnId="<%=cell.getColumnId()%>" rowId="<%=cell.getRowId()%>"
		 <%=canWrite?"":"bgcolor='lightblue' "%>>
	<%
	if ( showTableChanges || showTableColumnDelta )
	{

		oldCellValue = (VersionedCell)tableConf.getCellsByCellId().get(  new Integer ( cell.getId() )    );

		if  ( oldCellValue != null )
		{
	 %>
		<img  style="display:inline;"  src="images/track.gif"  height=12 width=12 >
	 <%
		}
	}

	if ( showTableColumnDelta )
	{
		String cellValueForDisplay  = cell.getValueAsString();
		String color = "black";
		if  ( cell.getColumnId() == deltaColumnId.intValue() )
		{
			if ( upRow == true )
			{
				color="darkgreen";

			}
			else
			if ( downRow == true )
			{
				color="darkred";
			}

			if ( deltaConf.getDifferenceValues().get(new Integer ( cell.getId() ) ) != null )
			{
					cellValueForDisplay = (String)deltaConf.getDifferenceValues().get(new Integer ( cell.getId() )  );
			}

		%>

			<DIV  contentEditable="false"  STYLE="height: 100%; width: 100%;">
									<b><font color="<%=color%>">	 <%=cellValueForDisplay%> </font></b>
				</DIV>
		<%


		}
		else
		{
		%>

			<%=cell.getValueAsString()%>
		<%
		}
	}
	else
	{
	%>
	<div bgcolor="black"><%=cell.getValueAsString()%></div>
	<%
	}
  %>
</td>

<%
cellId++;

}//for ( int cellIndex=0; ....

	 %>
	 		<td ><%=rowOwner%> </td>
			<td ><%=maxRowTransaction.getCreatedByUserAddress()%> </td>
			<td  ><%=maxRowTransaction.getCreatedOn()%> </td>
	<%
		if (maxRowTransaction.getComment()!=null)
		{
	%>
			<td  ><%=maxRowTransaction.getComment()%> </td>
	<%
		}
	%>

	<%
	maxRowTransaction = null;

  } // for int rowIndex=0 ....

}//end if rowids.size() > 0

%>


</tbody>


<tr>


<input type="hidden" id="tableId" name="tableId" value="<%=tableId%>">
<input type="hidden" id="noOfColumns" name="noOfColumns" value="<%=noOfColumns%>">
<input type="hidden" id="tableName" name="tableName" value="<%=tbi.getTableName()%>" >
<input type="hidden" id="tableDescription" name="tableDescription" value="<%=tbi.getTablePurpose()%>" >
<input type="hidden" id="action" name="action" value="commitCells">
<input type="hidden" id="ViewPreference" name="ViewPreference" value="<%=ViewPreference%>">
<input type="hidden" id="QueryPreference" name="QueryPreference" value="<%=QueryPreference%>">
<input type="hidden" id="wbid" name="wbid" value="<%=tbi.getWhiteboardId()%>" >
<input type="hidden" id="selectedCellId" name="selectedCellId" value="-1">
<input type="hidden" id="selectedColumnId" name="selectedColumnId" value="-1">
<input type="hidden" id="selectedColumnIdx" name="selectedColumnIdx" value="-1">
<input type="hidden" id="selectedColumnType" name="selectedColumnType" value="-1">
<input type="hidden" id="tableComment" name="tableComment" value="">
<input type="hidden" name="showChildrenNhsOnly" value="false">
<input type="hidden" id="bucketNumber" name="bucketNumber" value="<%=bucketNum%>">
<input type="hidden" id="currentBucketSize" name="currentBucketSize" value="<%=bucketSize%>">
<input type="hidden" id="baselineId" name="baselineId"  value="<%=baselineId%>">
<input type="hidden" id="queryType" name="queryType" value="TABLE">
<input type="hidden" id="formMode" name="formMode" value="false">

</table>




</form>
<!--end table for table-----------------------------------------------------------------------------------------------------
</td>
<td width="5" valign="top" ><img src="images/clear.gif" width="5" height="2">
</td>
-->
</tr>
 </tr>

</table>
</div>


<!--end table for main table  ---------------------------------------------------------------------------------------------->
<br>
</td>
</tr>

<%
	if(portlet == false)
	{
%>

<%@include file='/jsp/common/footer.jsp' %>

<%
	} // portlet == false
%>



