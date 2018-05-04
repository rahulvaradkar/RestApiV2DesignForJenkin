
<%@ page isThreadSafe="false" buffer="300k" %>
<%@ page import ="java.lang.Integer, com.boardwalk.table.*,java.io.*, java.util.*" %>

<%
int tableId = ((Integer)request.getAttribute("TableId")).intValue();
String tableName = (String)request.getAttribute("TableName");
TableContents tbc = (TableContents)request.getAttribute("TableContents");
TableInfo tbi = (TableInfo)request.getAttribute("TableInfo");

String ViewPreference = (String)request.getAttribute("ViewPreference");
String QueryPreference =(String)request.getAttribute("QueryPreference");


Hashtable  UIPreferences = (Hashtable)request.getAttribute("UIPreferences");

boolean isInDesignMode = false;
if ( ViewPreference.equals(ViewPreferenceType.DESIGN))
isInDesignMode = true;



TableAccessList tbACL = null;
Hashtable columns = new Hashtable();
Vector columnNames = new Vector();
Hashtable cellsByRowId =new Hashtable();
Vector   rowids =new Vector();




boolean tableIsReadOnly = false;

                if ( tbc != null )
                {
                	tbACL = tbc.getTableAccessList();
                	columns = tbc.getColumns();
                	columnNames = tbc.getColumnNames();
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
                	}
                }
                else
                {
                	System.out.println("edit_table.jsp::tbcl is null");
                }

System.out.println("edit_table.jsp:tableIsReadOnly=" + tableIsReadOnly);
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>


<script language="javascript">
<%@ include file="/jscript/editTable.js" %>
<%@ include file="/jscript/sortTable.js" %>
var prevRow = null;
var prevCB = null;
function highlightRow(cb)
{

	if (cb !=null) {
		// unhighlight the column
		highlightColumn('bwTableContents', -1, null)


		// get the <tr> object using the rowId
		var rowId = cb.value
		var trObj = eval("document.all.row" + rowId)
		// get the children of this row which are the <td> elements
		var rowCells = trObj.children
		for (i = 0; i < rowCells.length; i++) {
			if(rowCells[i].tagName == "TD"){
				rowCells[i].style.backgroundColor="#eeeeee"
			}
		}

		checkBoxCheck(cb)
	} else {
		if (prevCB != null)
			prevCB.checked = false
	}
	// unhighlight the earlier row
	if (prevRow != null) {
		// get the children of this row which are the <td> elements
		var rowCells = prevRow.children
		for (i = 0; i < rowCells.length; i++) {
			if(rowCells[i].tagName == "TD"){
				rowCells[i].style.backgroundColor="white"
			}
		}
	}

	prevRow = trObj
	prevCB = cb

}

var prevCol = -1;
function highlightColumn(tableId, colIdx, th)
{

	var tableEl = eval("document.all."+tableId);
	var tableRows = tableEl.rows

	if (colIdx != -1) {
		// unhighlight the row
		highlightRow(null)
		// highlight current column
		for (i = 0; i < tableRows.length; i++) {
			if (tableRows[i].cells[colIdx].tagName == "TD") {
				tableRows[i].cells[colIdx].style.backgroundColor ="#eeeeee"
			}

		}

		// update the column form with the selected column info
	if ( document.forms[0].columnName != null )
	{

		document.forms[0].columnName.innerText = th.innerText
		document.forms[0].columnDefaultValue.innerText = th.defaultValue
		if (th.columnType == "STRING")
			document.forms[0].columnType[0].selected = true
		else if (th.columnType == "INTEGER")
			document.forms[0].columnType[1].selected = true
		else if (th.columnType == "FLOAT")
			document.forms[0].columnType[2].selected = true
		else if (th.columnType == "TABLE")
			document.forms[0].columnType[3].selected = true
		else if (th.columnType == "LOOKUP")
			document.forms[0].columnType[4].selected = true

		document.forms[0].selectedColumnId.value = th.id
	}
	}

	//unhighlight the previous column
	if (prevCol != -1) {
		for (i = 0; i < tableRows.length; i++) {
			if (tableRows[i].cells[prevCol].tagName == "TD") {
				tableRows[i].cells[prevCol].style.backgroundColor ="white"
			}
		}
	}

	if (prevCol == colIdx || colIdx == -1) {
		prevCol = -1

		// update the column form with defaults
	    if ( document.forms[0].columnName != null )
	    {
		document.forms[0].columnName.innerText = ""
		document.forms[0].columnDefaultValue.innerText = ""
		document.forms[0].columnType[0].selected = true
		document.forms[0].selectedColumnId.value = -1
		}

	} else {
		prevCol = colIdx
	}

}

function deleteColumn()
{
	if (document.forms[0].selectedColumnId.value == -1){
		alert ("No Column Selected")
	} else {

		document.forms[0].action.value = 'deleteColumn'
		document.forms[0].submit()
	}
}

function renameColumn()
{
	if (document.forms[0].selectedColumnId.value == -1){
		alert ("No Column Selected")
	} else {
		document.forms[0].action.value = 'renameColumn'
		document.forms[0].submit()

	}
}
</script>

</tr>
</td>

<tr>
    <td>
<!--start main page-->
    <form name="EditTable" method="post" action="MyTables"  >
<!-- The table on the left that contains column creation and view generator ---->
	 <table class="body" name="t2"  border="0" bordercolor="red"  cellspacing="0" cellpadding="0" align="left" valign="top">

		<tr border=2 valign="top">
		  <td height="2">  <img src="images/clear.gif" height="2"></td>
		  <td height="2" > <img src="images/clear.gif" height="2"></td>
		  <td height="2" > <img src="images/clear.gif" height="2"></td>
		</tr>


	<tr  valign="top" >
		  <td  width="120"  align="left">
		  <table border="0" align="left">
		    <!--start add - delete-rename-column-->

		     <%
	if ( tbACL != null &&  (  tbACL.canAdministerColumn() || tbACL.canAdministerTable()  ) && tableIsReadOnly == false )
	{
		    			         		System.out.println("Now rendering the add column section " );

		    			  %>
		    			     <tr> <td>
		  <table  bgcolor="#FFEEDD" class="body2" border="1" cellspacing="0" cellpadding="1" align="center" >

			<% System.out.println("Start rendering the add column section " ); %>
			 <tr>

			   <td align="left"  ><img src="images/column.jpg" width="20" height="20"><b>Column&nbsp;Name:</b>
			   <input class="InputBoxSmall" type="text" name="columnName" value="">
			   </td>

			 </tr>

			 <tr >
			   <td  align="left" height="10">Default&nbsp;Value:
			   <input   type="text" class="InputBoxSmall" name="columnDefaultValue" value="">
			   </td>
			 </tr>
			 <tr  height="14" >
				<td valign="middle">
				<a ID="lookupTableURL" href="javascript:selectTableForLookup( )" >Lookup Table</a>
				<a href="javascript:selectTableForLookup( )" ><img  src="images/open.jpg" height=21></a>
				<input type="hidden" name="lookupTableId" value="-1" >
				</td>
			 </tr>

			 <tr >
			   <td align="left" height="14">Column&nbsp;Type:
			   <select  width=80 class="InputBoxSmall"  height="14" name="columnType">
				<option value="STRING" selected >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;STRING&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
				<option value="INTEGER">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INTEGER&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
				<option value="FLOAT">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FLOAT&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
				<option value="TABLE">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;TABLE&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
				<option value="CELL">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LOOKUP&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
			   </select>
			   </td>
			  </tr>

			  <tr height="14" >
				<td align="center" >
				<a bgcolor="grey"href="javascript:addColumn('commitColumnAndDone')"><%=((TableActionUIPreference)UIPreferences.get("ADD_COLUMN")).getActionUIString()%>	</a> |
				<a bgcolor="grey"href="javascript:deleteColumn()"> Delete	</a> |
				<a bgcolor="grey"href="javascript:renameColumn()"> Rename	</a>
				<input type="hidden" name="selectedColumnId" value="-1">
				</td>
			  </tr>
			  </table>
			    </tr> </td>
			   <%
					}

			%>

			  <!--end add- delete-rename-column-->

			  <!--start self querytable-->

			  <%

					if ( tbACL != null && (     tbACL.canReadWriteOnMyLatestView()   ||
											tbACL.canReadLatestViewOfAll()    ||
											tbACL.canReadLatestViewOfAllChildren()  ||
											tbACL.canReadLatestOfTable()  ||
											tbACL.canWriteLatestOfTable()  ||
											tbACL.canReadWriteLatestOfMyRows()
										  )

					  )

					{

			    %>

			    <!--start query table-->

<%
	if ( !tableIsReadOnly )
	{
%>
<tr><td>
 <table  bgcolor="#eeeeee" class="body2"  border="1" cellspacing="0" cellpadding="1" align="center" >
<% System.out.println("Start rendering the cell query section " ); %>
			  <tr height="14" >

				<td align="left"  height="14"><b><img src="images/cell.jpg" width="20" height="20">View Type: </b>
				<select  width=80 class="InputBoxSmall"  height="14" name="queryType">
				  <option value="CELL" selected >Cell &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
				</select>
				</td>
			  </tr>

			  <tr height="30" >
				<td align="left" height="14" >
				<input align="left"  type="checkbox" name="allversions" >All Versions
				</td>
			  </tr>

			  <tr >
				<td align="left" height="14">Start Date: <br>
				<input   type="text" class="InputBoxSmall" name="startDate" value="">
				</td>
			  </tr>
			  <tr>
				<td  align="left" height="14">End Date: 	 <br>
				<input   type="text" class="InputBoxSmall" name="endDate" value="">
				</td>
			  </tr>
			  <tr>
				<td  align="left" height="14">CellValue Like: 	 <br>
				<input   type="text" class="InputBoxSmall" name="cellValueLike" value="">
				</td>
			  </tr>
			  <tr>
				<td  align="center" >
				<a bgcolor="grey"href="javascript:query('sendQuery')"><%=((TableActionUIPreference)UIPreferences.get("GET_QUERY_RESULTS")).getActionUIString()%></a>
				</td>
			  </tr>

</table>
</tr></td>
<% } // if ( !tableIsReadOnly )

} //if ( queriable )  %>
<!--end query table-->
<br>
<!--start miscellaneous reports-->
<tr><td align="left">
<table bgcolor="#FFEEDD" class="body2" border="1" cellspacing="0" cellpadding="1" align="center" >


			  <tr >
				<td>
				<a href="javascript:setActionAndSubmit('displayInPlainHTML')"><img src="images/printer.jpg" width="15" height="15">Printer Friendly Page</a>
				</td>
			</tr>

</table>
<br>


<%

if ( tbACL != null && (     					tbACL.canReadLatestOfTable()  ||
											tbACL.canWriteLatestOfTable()  ||
											tbACL.canReadWriteLatestOfMyRows()
						) )
{


%>


<table bgcolor="#eeeeee" class="body2" border="1" cellspacing="0" cellpadding="1" align="center" >
<tr>
<td>
		<b><img src="images/row.jpg" width="20" height="20"> Row Based Queries</b>
</td>
</tr>


				<%



			   if ( tbACL!= null && tbACL.canReadLatestOfTable())
			   {
							System.out.println("Now rendering the query  section " );
							System.out.println("Now rendering query 1 " );


					%>



					<tr>
							<td>
									<a  href="javascript:getTableView('LATEST')"><%=((TableActionUIPreference)UIPreferences.get("LATEST_OF_TABLE")).getActionUIString()%> </a>
							 </td>
							 </tr>


							  <tr>
							 	<td>
							 		<a href="javascript:getTableViewWithQueryPreference('LATEST','ROWS_BY_ROW_SEQ_ID')"><%=((TableActionUIPreference)UIPreferences.get("LATEST_OF_TABLE")).getActionUIString()%>  sorted by users</a>
							 	</td>
							</tr>

							 <tr>
								<td>
									<a  href="javascript:getTableReportByNh('LATEST_ROWS_OF_ALL_USERS_IN_ANY_NH')"><%=((TableActionUIPreference)UIPreferences.get("LATEST_ROWS_OF_ALL_USERS_IN_ANY_NEIGHBORHOOD")).getActionUIString()%> </a>
								 </td>
							 </tr>


						<%
								}

							System.out.println("Now rendering query 2 " );

							boolean renderTablebasedQueries = false;




							if (tbACL!= null &&  tbACL.canReadWriteLatestOfMyRows())
										   {




									%>



										<tr>
										<td>
												<a href="javascript:getTableView('MY_ROWS')"><%=((TableActionUIPreference)UIPreferences.get("LATEST_OF_MY_ROWS")).getActionUIString()%></a>
										 </td>
										 </tr>



									<%
								}

%>


</table>
<br>
<%

}


if  ( tbACL != null && (     					tbACL.canReadWriteOnMyLatestView()   ||
											tbACL.canReadLatestViewOfAll()    ||
											tbACL.canReadLatestViewOfAllChildren()
						))
{


%>


<table bgcolor="#FFEEDD" class="body2" border="1" cellspacing="0" cellpadding="1" align="center" >
<tr>
<td>
		<b><img src="images/table.jpg" width="20" height="20"> Table Based Queries</b>
</td>
</tr>





<%




							if ( tbACL!= null && tbACL.canReadWriteOnMyLatestView())
													   {



												%>


													<tr>
													<td>
															<a href="javascript:getTableView('LATEST_BY_USER')"><%=((TableActionUIPreference)UIPreferences.get("MY_LATEST")).getActionUIString()%></a>
													 </td>
													 </tr>
												<%
								}
						System.out.println("Now rendering query 4 " );

							if ( tbACL!= null && tbACL.canReadLatestViewOfAll())
							   {



						%>


							<tr>
							<td>
									<a  href="javascript:getTableReport('LATEST_VIEW_OF_ALL_USERS')"><%=((TableActionUIPreference)UIPreferences.get("LATEST_OF_ALL")).getActionUIString()%></a>
							 </td>
							 </tr>

							 <tr>
								<td>
									<a  href="javascript:getTableReportByNh('LATEST_VIEW_OF_ALL_USERS_IN_ANY_NEIGHBORHOOD')"><%=((TableActionUIPreference)UIPreferences.get("LATEST_VIEW_OF_ALL_USERS_IN_ANY_NEIGHBORHOOD")).getActionUIString()%> </a>
								 </td>
							 </tr>

						<%
								}


								System.out.println("Now rendering query 5 " );

							if ( tbACL!= null && tbACL.canReadLatestViewOfAllChildren())
										   {




									%>


										<tr>
										<td>
												<a href="javascript:getTableReport('LATEST_VIEW_OF_ALL_CHILDREN')"><%=((TableActionUIPreference)UIPreferences.get("LATEST_OF_CHILDREN")).getActionUIString()%></a>
										 </td>
										 </tr>

										 <tr>
											<td>
													<a href="javascript:getTableReportByNh('LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NEIGHBORHOOD')"><%=((TableActionUIPreference)UIPreferences.get("LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NEIGHBORHOOD")).getActionUIString()%>  </a>
											 </td>
										 </tr>

									<%
								}

							%>





		  </table>

	<%


	}



	if ( tbACL!= null && tbACL.canAdministerTable())
											   {



										%>
	<br>
	<table bgcolor="#eeeeee" class="body2" border="1" cellspacing="0" cellpadding="1" align="center" >

											<tr>
											<td>
													<a href="javascript:getAdminPage()"><img src="images/admin1.jpg" width="15" height="15"><b><%=((TableActionUIPreference)UIPreferences.get("ADMINISTER_TABLE")).getActionUIString()%></b></a>
											 </td>
											 </tr>
											 </table>
										<%
								}
		  %>






		  		  <!--end miscellaneous reports-->
		  </tr></td>
		  </table>

		  </td>

		  <!--spacer column-->
		  <td width="20" ><img="images/clear.gif" width="20" height="1"></td>
		  <!--end spacer column-->

		  <td class="body" >
<!---------The main table ----------------------------------------------------------------------------------->
<%
System.out.println("Now rendering the Table Info  section " );
%>
<div align="left" >
<b><u><%=tbi.getTableName()%></u></b>
<br><br>
Description: <%=tbi.getTablePurpose()%>
</div>
<br><br>

<table id="bwTable" class="body" border="1" bordercolor="#eeeeee" cellspacing="0" cellpadding="0">
<thead>
<tr name="bwHeadings">
<td width="1%">
<img onmouseover="javascript:blockDiv.style.visibility='visible'" onmouseout="javascript:blockDiv.style.visibility='hidden'" src="images/info.gif">
<DIV ID="blockDiv"  STYLE="font-family:verdana,arial,helvetica,sans serif; font-size:8pt;z-order=3; visibility:hidden; position:absolute;  width:auto; height:auto; background-color:lightgrey; layer-background-color:red;">
<!--- Table info -------------------------------------------------------------------------------------------->
<table class="small-text">
<Tr Bgcolor="#Eeeeee">
	<td><b>Login:</b></td>
	<td align="right"><%=userName%></td>
</tr>
<tr>
	<td><b>Collaboration :</b></td>
	<td align="right"><a href="MyCollaborations?action=editCollab&collabId=<%=tbi.getCollaborationId()%>"> <%=tbi.getCollaborationName()%> </a></td>
</tr>
<tr>
	<td><b>Whiteboard:</b></td>
	<td align="right"><a href="Whiteboard?wbid=<%=tbi.getWhiteboardId()%>&action=edit&collabId=<%=tbi.getCollaborationId()%>"><%=tbi.getWhiteboardName()%> </a></td>
</tr>
<tr>
	<td><b>Table Purpose:</b></td>
	<td align="right"><%=tbi.getTablePurpose()%></td>
</tr>
<tr>
	<td><b>Neighborhood:</b></td>
	<td align="right"><%=tbi.getNeighborhood()%></td>
</tr>
</table>
<!-- End Table Info ------------------------------------------------------------------------------------------->
</DIV>
</td>
<%

if ( 	columnNames.size() > 0 )
{
	for ( int columnIndex = 0; columnIndex< columnNames.size(); columnIndex++ )
	{
		String columnName = (String)columnNames.elementAt(columnIndex);
		String visibleColumnName = columnName;
		Column c = null;
		// get the column id
		Enumeration keys = columns.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			c = (Column)columns.get(key);

			if (c.getColumnName().equals(visibleColumnName)) {
				break;
			}
		}

%>
  <th id="<%=c.getId()%>"
      defaultValue="<%=c.getDefaultValueAsString()%>"
      columnType="<%=c.getType()%>"
  	  name="<%=visibleColumnName%>" class="BWTableHead"
  	  onclick="this.blur(); return highlightColumn('bwTableContents', <%=columnIndex + 1%>, this);"
 	  ondblclick="this.blur(); return sortTable('bwTableContents',  <%=columnIndex + 1%>, true);" ><%=visibleColumnName%>
  </th>
<%
	} // end for
} // end if
%>
</tr>
</thead>

<%
	out.flush();
%>

<tbody id="bwTableContents">

<%

	StringBuffer tableData = new StringBuffer("");

	int cellId = 0;
	if ( rowids.size() > 0 )
	{
		for ( int rowIndex=0; rowIndex < rowids.size(); rowIndex++ )
		{

			Integer a_rowIntegerId = (Integer)rowids.elementAt( rowIndex);
			Vector cells = (Vector) ( (Vector)cellsByRowId.get(a_rowIntegerId)).elementAt(0);
			int rowId = ((Integer)rowids.elementAt(rowIndex)).intValue();


			    tableData.append(" <tr  id=\"row"+rowId+"\"  name=row"+rowId+"\"  >  <td>    	<input name=\"rowId\" onClick=\"highlightRow(this)\" type=\"checkbox\" width=\"1%\" value=\""+rowId+"\" >		</td>");










				for ( int cellIndex=0; cellIndex < cells.size(); cellIndex++ )
				{
					VersionedCell cell = (VersionedCell) cells.elementAt( cellIndex );

					if ( ! cell.getType().equals("TABLE")  && ! ((Column)columns.get(  new Integer(cell.getColumnId()))).getIsEnumerated()   )
					{

							tableData.append(" <td  ID=\"cell_"+cellId+"\" name=\""+cell.getId()+"\">  ");

							if(tableIsReadOnly == false)
							{
									tableData.append(" <DIV CONTENTEDITABLE  onfocus=\"changeCell(this.parentElement)\"  STYLE=\"height: 100%; width: 100%;\"> "+cell.getValueAsString() +"</DIV> ");
							}
							else
							{
									tableData.append(cell.getValueAsString());
							}


							  tableData.append(" </td> <input type=\"hidden\" name=\"pCell"+cell.getId()+"\" value=\""+cell.getValueAsString()+"\" >" );
							  tableData.append(" <input type=\"hidden\" name=\"CellType"+cell.getId() +"\" value=\""+cell.getType()+"\" >" );
							  tableData.append(" <input type=\"hidden\" name=\"Cell"+cell.getId() +" value=\""+cell.getValueAsString()+"\"> ");
							  tableData.append(" <input type=\"hidden\" name=\"dirtyCell"+cell.getId()+"\" value=false> " );

					}
					else if ( !cell.getType().equals("TABLE")  &&    ( (Column)columns.get(new Integer(cell.getColumnId()))).getIsEnumerated()   )
					{



							tableData.append("  <td> ");

							if(tableIsReadOnly == false)
							{

										       tableData.append(" <select onclick=\"lookupCellClicked(this)\" ID=\"cell_"+cellId+" name=\""+cell.getId()+" onChange=\"lookupValueSelected(this)\" class=\"LookupList\"   > ");


											Vector enumeratedValues = ((Column)      columns.get(new Integer(cell.getColumnId() ))    ).getEnumerations();
											for ( int index = 0; index < enumeratedValues.size(); index++ )
											{
												String optionvalue = (enumeratedValues.elementAt(index)).toString();
												if ( optionvalue.equals( cell.getValueAsString() ) )
												{
													tableData.append(" <option value=\"" + optionvalue + "\" selected > " + optionvalue + " </option> ");
												}
												else
												{

													tableData.append("  <option value=\"" + optionvalue+ "\" > " + optionvalue + "  </option> ");
												}
											}

												tableData.append("</select> " );

							} // if table is not read only
							else
							{
								tableData.append(cell.getValueAsString());
							} // table is read only

						  	  tableData.append(" </td> <input type=\"hidden\" name=\"pCell"+cell.getId()+"\" value=\""+cell.getValueAsString()+"\" >" );
							  tableData.append(" <input type=\"hidden\" name=\"CellType"+cell.getId() +"\" value=\""+cell.getType()+"\" >" );
							  tableData.append(" <input type=\"hidden\" name=\"Cell"+cell.getId() +" value=\""+cell.getValueAsString()+"\"> ");
							  tableData.append(" <input type=\"hidden\" name=\"dirtyCell"+cell.getId()+"\" value=false> " );

						}
				else if ( cell.getType().equals("TABLE")  &&    !( (Column)columns.get(new Integer(cell.getColumnId()))).getIsEnumerated()   )
				{


					String tableHref = "";

					String visibleTableName = "";
					System.out.println("TABLE cells ****************");
					cell.printCell();
					if ( cell.getTableValue() == -1 )
					{
						visibleTableName = "Not Set";
						tableHref="javascript:selectTableForCell('"+ cell.getId() + "', 'Cell"+cell.getId()+"' )";
					}
					else
					{
						visibleTableName =cell.getTableName();
						tableHref="MyTables?tableId="+cell.getTableValue()+"&action=editTable";
					}

					tableData.append(" <td ID=\"cell_"+ cellId+ "\"> " );

					 if(tableIsReadOnly ==  false)
					 {

							tableData.append(" <a class=\"body\" ID=\""+cell.getId()+"\"   href=\""+tableHref+"\"  > " + visibleTableName + " </a> ");
							tableData.append(" <a href=\"javascript:selectTableForCell( '"+cell.getId()+"', 'Cell"+cell.getId() + "' ) > ");
							tableData.append(" <img src=\"images/open.jpg\" height=15></a> " );

					}
					else if (cell.getTableValue() == -1)
					{
						tableData.append(visibleTableName);
					}
					else
					{
						tableData.append("<a ID=\""+cell.getId()+"\" class=\"body\"   href=\""+tableHref+"\" > " +visibleTableName  +" </a> " );
					}


					tableData.append(" </td> <input type=\"hidden\" name=\"pCell"+cell.getId()+"\" value=\""+cell.getValueAsString()+"\" >" );
					tableData.append(" <input type=\"hidden\" name=\"CellType"+cell.getId() +"\" value=\""+cell.getType()+"\" >" );
					tableData.append(" <input type=\"hidden\" name=\"Cell"+cell.getId() +" value=\""+cell.getValueAsString()+"\"> ");
					tableData.append(" <input type=\"hidden\" name=\"dirtyCell"+cell.getId()+"\" value=false> " );

				}
				else if ( cell.getType().equals("TABLE")  &&    ( (Column)columns.get(new Integer(cell.getColumnId()))).getIsEnumerated()   )
				{


					tableData.append(" <td> " );

					if(tableIsReadOnly == false)
					{

						tableData.append(" <select   onclick=\"lookupCellClicked(this)\" ID=\"cell_"+cellId+" name=\""+cell.getId() + " onChange=\"lookupTableValueSelected(this)\" class=\"LookupList\"  > " );



						Vector enumeratedValues = ((Column)      columns.get(new Integer(cell.getColumnId() ))    ).getEnumerations();
						// System.out.println("In enumerated cell**************************************  of enum size " + enumeratedValues.size() );
						String selectedTablehref = "";

						for ( int index = 0; index < enumeratedValues.size(); index++ )
						{


							TableCellValue tbl_cell_value = (TableCellValue)enumeratedValues.elementAt(index);
							String enumeratedTableHref="MyTables?tableId="+tbl_cell_value.getId()+"&action=editTable&ViewPreference="+tbl_cell_value.getViewPreference();
							if ( index == 0 )
							{
								selectedTablehref = enumeratedTableHref;
							}


							if ( tbl_cell_value.getId() == cell.getTableValue()  )
							{
								tableData.append(" <option href=\""+enumeratedTableHref+"\" value=\""+tbl_cell_value.getId()+"\" selected > "+tbl_cell_value.getName() +" </option> ");
								selectedTablehref = enumeratedTableHref;
							}
							else
							{
								tableData.append("  <option href=\""+enumeratedTableHref+ "\"  value=\""+tbl_cell_value.getId()+"\" > "+tbl_cell_value.getName() + "  </option> ");
							} // end else
					}// end for

					tableData.append("</select> ");
					tableData.append("<a name=\"lookupcell_href"+cell.getId()+"\"  href=\""+selectedTablehref+"\"> <img  src=\"images/open.jpg\" height=21> </a> ");

					} // if table is not read only
					else if (cell.getTableValue() == -1)
					{
							tableData.append("Not Set ");
					}
					else


					{
						String thref = "MyTables?tableId="+cell.getTableValue()+"&action=editTable";
						tableData.append("<a ID=\""+cell.getId()+"\"   href=\""+thref+"\" > "+cell.getTableName() + " </a> ");
					}
						tableData.append(" </td> <input type=\"hidden\" name=\"pCell"+cell.getId()+"\" value=\""+cell.getValueAsString()+"\" >" );
						tableData.append(" <input type=\"hidden\" name=\"CellType"+cell.getId() +"\" value=\""+cell.getType()+"\" >" );
						tableData.append(" <input type=\"hidden\" name=\"Cell"+cell.getId() +" value=\""+cell.getValueAsString()+"\"> ");
						tableData.append(" <input type=\"hidden\" name=\"dirtyCell"+cell.getId()+"\" value=false> " );
					} // end enumerated table
					cellId++;

	}//for ( int cellIndex=0; ....

	tableData.append("</tr>");

  } // for int rowIndex=0 ....

}//end if rowids.size() > 0

String toString = tableData.toString();
%>
	<%=toString%>
</tbody>

<tr></tr>
<tr></tr>
<tr></tr>


<tr>
			<td  colspan="30"  align="left">
			  <b>

		<%
					boolean firstActionCommandRendered  = false;
					if ( isInDesignMode )
					{
					    System.out.println("In design mode ***************************" );
					   if ( tbACL.canAdministerColumn() )
					   {

				%>
					    <a href="javascript:commitToBoardwalk('commitCells')">Save Default Values</a> |
					    <a href="javascript:setActionAndSubmit('addRow')">Add Row</a> |
					    <a href="javascript:checkCheckBoxsetActionAndSubmit('deleteRow')">Delete Row</a> |
					    <a href="javascript:getTableView('<%=tbACL.getSuggestedViewPreferenceBasedOnAccess()%>')"> Open Table </a> |





				<%
					}
				}
					else
				{
					 System.out.println("Not In design mode " );

					if (        (ViewPreference.equals("LATEST_BY_USER") && tbACL.canReadWriteOnMyLatestView() )
								||    ( ViewPreference.equals("LATEST") && tbACL.canWriteLatestOfTable() )
								||    ( ViewPreference.equals("MY_ROWS") && tbACL.canReadWriteLatestOfMyRows())
								||    ( ViewPreference.equals("LOOKUP") && tbACL.canWriteLatestOfTable())
					)
					{
					%>
					     | <a href="javascript:commitToBoardwalk('commitCells')"><%=((TableActionUIPreference)UIPreferences.get("SAVE_TABLE")).getActionUIString()%></a>
					<%
					  }


					   if ( tbACL.canAdministerRow()  && tableIsReadOnly == false)
					   {
					  %>
					 |    <a href="javascript:setActionAndSubmit('addRow')"><%=((TableActionUIPreference)UIPreferences.get("ADD_ROW")).getActionUIString()%></a>
					|     <a href="javascript:checkCheckBoxsetActionAndSubmit('deleteRow')"><%=((TableActionUIPreference)UIPreferences.get("DELETE_ROW")).getActionUIString()%></a>
					<%

					}

					if ( tbACL.getACL() > 0 )
					{
					%>


					  |   <a href="javascript:setActionAndSubmit('copyTable')">Copy Table</a>

				<%
					}

				 if ( tbACL.canAdministerColumn() )
					   {

				%>

					| 	 <a href="javascript:getDesignValues('getDesignValues')"><%=((TableActionUIPreference)UIPreferences.get("SHOW_DEFAULT_VALUES")).getActionUIString()%></a>
				<%
						}
				}
		%>











			    </b>
			</td>
  </tr>

<input type="hidden" name="tableId" value="<%=tableId%>">
<input type="hidden" name="tableName" value="<%=tbi.getTableName()%>" >
<input type="hidden" name="tableDescription" value="<%=tbi.getTablePurpose()%>" >
<input type="hidden" name="baselineId" value=-1 >


<input type="hidden" name="action" value="commitCells">
<input type="hidden" name="ViewPreference" value="<%=ViewPreference%>">
<input type="hidden" name="QueryPreference" value="<%=QueryPreference%>">
<input type="hidden" name="wbid" value="<%=tbi.getWhiteboardId()%>" >
<input type="hidden" name="selectedCellId" value="notset">


</table>





<%@include file='/jsp/common/commonparameters.jsp' %> </form>
<!--end table for table-----------------------------------------------------------------------------------------------------
</td>
<td width="5" valign="top" ><img src="images/clear.gif" width="5" height="2">
</td>
-->
</tr>
</table>
<!--end table for main table  ---------------------------------------------------------------------------------------------->
<br>
</td>
</tr>


<%@include file='/jsp/common/footer.jsp' %>




