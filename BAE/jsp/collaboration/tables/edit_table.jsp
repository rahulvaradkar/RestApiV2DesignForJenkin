
<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer,java.util.regex.*, com.boardwalk.database.Transaction, com.boardwalk.table.*,java.io.*,servlets.*, java.util.*,com.boardwalk.query.*,boardwalk.common.*,java.text.*"%>

<%
   String Exceldump = (String) request.getAttribute("ShowinExcel");
   System.out.println(" Exceldump JSP "+Exceldump);
	if(Exceldump != null) 
	{
		if(Exceldump.equals("true"))
			response.setContentType("application/vnd.ms-excel");
		else
			response.setContentType("text/html");
	}

boolean tableIsReadOnly = false;
boolean isTableLocked = false;
boolean portlet = false;
boolean showFormulae = false;
boolean showTableChanges = false;
boolean hideUnchangedRows = false;
boolean hideNewRows = false;
boolean hideDeletedRows = false;
boolean hideChangedRows = false;
boolean showDropDowns = false;

String showDropDownsStr = request.getParameter("showDropDowns");
if (showDropDownsStr != null && showDropDownsStr.equals("true"))
{
  showDropDowns = true;
}
DeltaColumnConfiguration dcc = new DeltaColumnConfiguration();

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

String hideUnchangedRowsStr = request.getParameter("hideUnchangedRows");
if (hideUnchangedRowsStr != null && hideUnchangedRowsStr.equals("true"))
{
	hideUnchangedRows = true;
}
String hideNewRowsStr = request.getParameter("hideNewRows");
if (hideNewRowsStr != null && hideNewRowsStr.equals("true"))
{
	hideNewRows = true;
}
String hideDeletedRowsStr = request.getParameter("hideDeletedRows");
if (hideDeletedRowsStr != null && hideDeletedRowsStr.equals("true"))
{
	hideDeletedRows = true;
}
String hideChangedRowsStr = request.getParameter("hideChangedRows");
if (hideChangedRowsStr != null && hideChangedRowsStr.equals("true"))
{
	hideChangedRows = true;
}

boolean showDiffSideBySide = false;
String showDiffSideBySideStr = request.getParameter("showDiffSideBySide");
if (showDiffSideBySideStr != null && showDiffSideBySideStr.equals("true"))
{
	showDiffSideBySide = true;
	////System.out.println("show diff side by side");
}

boolean showStateChange = false;
String showStateChangeStr = request.getParameter("showStateChangeStr");
if (showStateChangeStr != null && showStateChangeStr.equals("true"))
{
	showStateChange = true;
}

//System.out.println("baselineId = " + baselineId);
String asOfTidStr = request.getParameter("asOfTid");
System.out.println("asOfTidStr=" + asOfTidStr);
String compTidStr = request.getParameter("compTid");
String asOfDateStr = request.getParameter("asOfDate");
String compDateStr = request.getParameter("compDate");
String periodStr = request.getParameter("period");
String trackStateStr = request.getParameter("trackState");

int asOfTid = -1;
if (asOfTidStr != null && !asOfTidStr.equals("null"))
{
	
	asOfTid = Integer.parseInt(asOfTidStr);
	if (asOfTid > 0)
	{
		tableIsReadOnly = true;
	}
}

String portletStr = request.getParameter("portlet");
if (portletStr != null)
{
	portlet = true;
	//System.out.println("Portal Based Access");
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

boolean showChangesOnly = false;
TableContents tbchg = (TableContents)request.getAttribute("TableChanges");
Hashtable cellsByRowId_tbchg = new Hashtable();
Hashtable columns_tbchg = new Hashtable();
if (tbchg != null)
{
	cellsByRowId_tbchg = tbchg.getCellsByRowId();
	columns_tbchg = tbchg.getColumnsByColumnId();
	tableIsReadOnly = true;
	showTableChanges = true;
}

String showChangesOnlyStr = request.getParameter("showChangesOnly");
if (showChangesOnlyStr != null)
{
	if (showChangesOnlyStr.equals("true"))
	{
		showChangesOnly = true;
	}
}
String showDiffPercent = request.getParameter("showDiffPercent");
String showDiffAbsolute = request.getParameter("showDiffAbsolute");
String showDiffNone = request.getParameter("showDiffNone");
TableInfo tbi = (TableInfo)request.getAttribute("TableInfo");

Transaction maxTableTransaction = null;
String tableUpdatedBy = "";
String tableUpdatedOn = "";
String tableUpdatedComment = "No Comment";
int bucketSize = 50;

// Find the cookie that pertains to that book
Cookie[] cookies = request.getCookies();
if (cookies != null)
{
for(int i=0; i < cookies.length; i++) {
	Cookie thisCookie = cookies[i];
	if (thisCookie.getName().equals("boardwalktable"+tableId+"bucketSize")){
	    bucketSize = Integer.parseInt(thisCookie.getValue());
	}
}
}


String bucketSizeStr = request.getParameter("bucketSize");
if (bucketSizeStr != null)
{
	bucketSize = Integer.parseInt(bucketSizeStr);
}
int bucketNum = 0;
String bucketNumStr = request.getParameter("bucketNum");
int displayPageNumber = 0;

if (bucketNumStr != null)
{
	bucketNum = Integer.parseInt(bucketNumStr);
}
displayPageNumber = bucketNum+1;

//System.out.println("Now rendering the the table;");

String ViewPreference = (String)request.getAttribute("ViewPreference");
String QueryPreference =(String)request.getAttribute("QueryPreference");

Hashtable tablesUsingLkpForColumn = (Hashtable)request.getAttribute("TablesUsingLkpForCol");
////System.out.println("edit_table.jsp:: tablesUsingLkpForColumn = " + tablesUsingLkpForColumn);

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
HashMap lookupcolumns = new HashMap();


if ( tbc != null )
{
	tbACL = tbc.getTableAccessList();
	columns = tbc.getColumnsByColumnId();
	columnNames = tbc.getColumnNames();
	columnsSortedBySeqNum = tbc.getColumnsSortedBySeqNum();

	//System.out.println(" column size " + columns.size() + " " + columnNames.size() + " " + columnNames.size() + " " );


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
	//System.out.println("edit_table.jsp::tbcl is null");
}

// columns filters
//System.out.println("Finding Columns to Hide");
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
				//System.out.println("Hide Column: " + ccId + colPreference);
			}
			else
			{
				colFilters.addElement(colPreference);
				//System.out.println("Filter Column: " + ccId + colPreference);
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

FormDefinition fd = (FormDefinition) request.getAttribute("FormDefinition");
int formTableId = -1;
Integer formTableIdInt = (Integer)request.getAttribute("formTableId");
if (formTableIdInt != null)
{
   formTableId = formTableIdInt.intValue();
}
int criteriaTableId = -1;
Integer criteriaTableIdInt = (Integer)request.getAttribute("criteriaTableId");
if (criteriaTableIdInt != null)
{
   criteriaTableId = criteriaTableIdInt.intValue();
}
boolean formInterface = false;
String formTitle = null;
String instr = null;
Hashtable sectionsByLevel = null;
int numLevels = 0;
int horizGrid = 0;
Hashtable columnIdsByColumnName = null;
String formMode = request.getParameter("formMode");
if (formMode!=null && formMode.equals("true"))
{
	if (fd != null)
	{
		formInterface = true;
		formTitle = fd.getTitle();
		instr = fd.getInstructions();

		sectionsByLevel = fd.getSections();
		numLevels = sectionsByLevel.size();
		horizGrid = fd.getMaxSectionsAtLevel();


		//System.out.println("NumLevels = " + numLevels);
		//System.out.println("horizGrid = " + horizGrid);

		columnIdsByColumnName = new Hashtable();
		if (columnsSortedBySeqNum.size() > 0)
		{
			for ( int columnIndex = 0; columnIndex< columnsSortedBySeqNum.size(); columnIndex++ )
			{
				Column c = (Column)columnsSortedBySeqNum.elementAt(columnIndex);
				columnIdsByColumnName.put(c.getColumnName(), new Integer(c.getId()));
			}
		}
	}
	else
	{
		formInterface = false;
%>
	<script>
		alert("No Form Definition for this table")
	</script>

<%
	}
}
%>
<%@include file='/jsp/common/header.jsp' %>
<%//@include file='/jsp/common/menubar.jsp' %>
<%//@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<%
  if ( isTableLocked && tbi. getLockedByUserId() != bws.userId.intValue()         )
  {
		tableIsReadOnly = true;
  }

//System.out.println("edit_table.jsp:tableIsReadOnly=" + tableIsReadOnly + " isTableLocked = " +isTableLocked );
%>
<link rel="stylesheet" type="text/css" href="css/menu.css">
<link rel=stylesheet type="text/css" href="css/stylesheet.css">

<script language="javascript">
	<%@ include file="/jscript/menu.js" %>
	<%@ include file="/jscript/editTable.js" %>
	<%@ include file="/jscript/sortTable.js" %>
	menuArrow = new Image(10, 12);
	menuArrow.src = "images/arrow.gif";
</script>
<%if(Exceldump != null && Exceldump.equals("false")){%>
<script language="javascript">
<%@ include file="/jscript/validate.js" %>
<%@ include file="/jscript/dateutil.js" %>
</script>

</td>
</tr>
</table>


<%}%>

<% if (portlet == false) { %>
<%if(Exceldump != null && Exceldump.equals("false")){%>
<a href="MyCollaborations"> <img src="images/logo-boardwalk.gif"/> </a>
<a class="largetext" href="MyCollaborations?action=editCollab&collabId=<%=tbi.getCollaborationId()%>"> <%=tbi.getCollaborationName()%> :</a>
<a class="largetext" href="Whiteboard?wbid=<%=tbi.getWhiteboardId()%>&action=edit&collabId=<%=tbi.getCollaborationId()%>"><%=tbi.getWhiteboardName()%>: </a>
<b><a class="largetext"  href="MyTables?tableId=<%=tbi.getTableId()%>&action=editTable&ViewPreference=<%=tbi.getTableDefaultViewPreference()%>">
    <%=tbi.getTableName()%>
   </a>
   <br>
</b> <%=tbi.getTablePurpose()%>
   <%}%>

<%
	if ( isTableLocked )
	{
%>
	<image src="images/lock.gif"><small>Locked by user <%=tbi.getLockedByuser()%>  since <%
	//	"NNN dd, yyyy hh:mm:ssa"
		SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ssa");
		Date DateObj = new Date(tbi.getLockTime());
		String lsReturnDate =  df.format(DateObj);
	%><%=lsReturnDate%><small>
<%
	}
} // if (portlet == false)
%>

<%
if (fd != null)
{
%>
<div name="formInterface" id="formInterface">
<table class="body" border="0">
<!--
  <tr>
    <td>Logo image</td>
  </tr>
 -->
  <tr>
    <td><br>
      <b><%=formTitle%><b>
      <br> </td>
  </tr>
  <tr>
    <td> <small><%=instr%> <small><br> </td>
  </tr>
  <tr>
    <td><table border="0">
<%
	int iL;
	int iH;
	int iS = 0;
	boolean sectionLevelsUndefined = false;
	Vector sections = null;
	for (iL = 0; iL < numLevels; iL++)
	{
		//System.out.println("Processing level : " + (iL + 1));
		if (!sectionLevelsUndefined)
		{
			sections = (Vector)sectionsByLevel.get(new Integer(iL + 1));

			if (sections == null && iL == 0) // sections undefined
			{
				sections = (Vector)sectionsByLevel.get(new Integer(0));
				if (sections != null)
				{
					numLevels = sections.size();
					horizGrid = 1;
					sectionLevelsUndefined = true;
				}
			}
			//System.out.println(" sections = " + sections);
		}
%>
	<tr>

<%
		for (iH = 0; iH < horizGrid; iH++)
		{
			FormSection section = null;
			if (sectionLevelsUndefined)
			{
				section = (FormSection)sections.elementAt(iL);
			}
			else if (iH < sections.size())
			{
				section = (FormSection)sections.elementAt(iH);
			}
			if (section != null)
			{
				//System.out.println("fieldsByLevel = " + section.getFields());
				Hashtable fieldsByLevel = section.getFields();
				//System.out.println("fieldsByLevel = " + fieldsByLevel);
				int maxFieldsAtLevel = section.getMaxFieldsAtLevel();
				//System.out.println("maxFieldsAtLevel = " + maxFieldsAtLevel);

%>
	<td class="InputBoxSmall" valign="top" >
	<table width="100%" border="1" cellpadding="2" cellspacing="0" bordercolor="#eeeeee">
		<tr bgcolor="#eeeeee">
			<td class="BWTableGreyHead" colspan=2*maxFieldsAtLevel><%=section.getName()%></td>
		</tr>
<%
				boolean fieldLevelsUndefined = false;
				int numFieldLevels = fieldsByLevel.size();
				Vector fields = null;
				for (int iLF =0 ; iLF < numFieldLevels; iLF ++)
				{
					//System.out.println("OPening tr tag");
%>

		<tr>


<%
					//System.out.println("Processing field level : " + (iLF+1));
					if (!fieldLevelsUndefined)
					{
						fields = (Vector)fieldsByLevel.get(new Integer(iLF + 1));
						if (fields == null && iLF == 0)
						{
							fields = (Vector)fieldsByLevel.get(new Integer(0));
							if (fields != null)
							{
								numFieldLevels = fields.size();
								maxFieldsAtLevel = 1;
								fieldLevelsUndefined = true;
							}
						}
						//System.out.println("Number of fields at this level = " + fields.size());
					}
					for (int iHF = 0; iHF < maxFieldsAtLevel; iHF++)
					{
						//System.out.println("Processing field number " + iHF);
						FormField field = null;
						if (fieldLevelsUndefined)
						{
							field = (FormField)fields.elementAt(iLF);
						}
						else if (iHF < fields.size())
						{
							field = (FormField)fields.elementAt(iHF);
						}

						if (field != null)
						{
							//System.out.println("Field Name = " + field.getName());
							//System.out.println("Field label = " + field.getLabel());

							int cid = ((Integer)columnIdsByColumnName.get(field.getName())).intValue();

							String valFuncs = new String();
							FormFieldProperty prop = null;
							String val = null;
		/*
							// empty check
							prop = (FormFieldProperty)field.getPropertyByName().get("bw:NoBlank");
							if (prop != null)
							{
								valFuncs = valFuncs + "validateNotEmpty(\""
														+label+ "\","+
														"this.value)";
							}

							// min length check
							prop = (FormFieldProperty)field.getPropertyByName().get("bw:MinLength");
							if (prop != null)
							{
								try
								{
									val = prop.getValue();
									size = Integer.parseInt(val);
								}
								catch (Exception e)
								{
									//System.out.println("MinLength not valid in form definition");
								}
							}


							valFuncs = "\'" + valFuncs + "\'";*/
		/* TBD***********************
							bw:NoBlank
							bw:MinLength
							bw:MaxLength
							bw:MinRange
							bw:MaxRange
							bw:Email
							bw:USZipCode
							bw:Date
							bw:USPhone */
							//System.out.println("Opening non blank td tag");

%>
					<td class="InputBoxSmallReadonly"><%=field.getLabel()%></td>
<%
			Column fCol = (Column)columns.get(new Integer(cid));
			if (! fCol.getIsEnumerated())
			{

%>
					<td ><input class="InputBoxSmall" type="text"   name="frc<%=cid%>" id="frc<%=cid%>"></td>
<%
				//System.out.println("not a lookup");
			}
			else // lookup
			{
%>
				 	<td ><select class="InputBoxSmall" name="frc<%=cid%>" id="frc<%=cid%>">

<%
				Vector enumeratedValues = fCol.getEnumerations();
				for ( int index = 0; index < enumeratedValues.size(); index++ )
				{
					String originalvalue = (enumeratedValues.elementAt(index)).toString().trim();
					String optionvalue = (enumeratedValues.elementAt(index)).toString();

					if ( optionvalue.trim().indexOf("<a") == 0 ||   optionvalue.trim().indexOf("<A") ==0 )
					{
							// optionvalue is a http link we need to parse it
							int indexendofATag = optionvalue.trim().indexOf(">") ;
							int indexstartofSlashATag = optionvalue.trim().indexOf("<", indexendofATag) ;
							optionvalue = optionvalue.trim().substring( indexendofATag+1, indexstartofSlashATag ).trim();

					}

%>
						 <option value="<%=optionvalue%>" > <%=originalvalue%> </option>
<%

				} //for
%>
			</select>
			<%	if(Exceldump.equals("false")) {%>
			<a style="display:inline;" onClick='this.href="MyTables?action=editTable&tableId=<%=fCol.getLookupTableId()%>&ViewPreference=LATEST&cc<%=fCol.getLookupColumnId()%>=equals:"+frc<%=cid%>.value'>
			 <img  style="display:inline;" height="12" width="12" src="images/refersto.gif"   onmouseover ="this.title='Lookup Column''" >
		 	</a>
			<%	} %>
<%
			//System.out.println("lookup");
			} // lookup

			} // if there is field
			else
			{
			//System.out.println("Opening blank td");
%>
		<td>&nbsp;
<%
			} // blank field
%>
</td>

<%
			//System.out.println("starting with new field at this level");
				} // iHF
			//System.out.println("Done with level = " );
%>
</tr>
<%
			//System.out.println("Starting a new level");
			} // iLF
			//System.out.println("Done with all levels" );
%>
			</table>
<%
			} // if there is section
			else // nothing
			{
%>
			<td>&nbsp;
<%

			}// blank section
%>
			</td>
<%
		}// for iH
%>
	</tr>
<%
	}// for iL
%>

  <tr>
    <td><input type="button" name="frmSave" value="Save" onClick="saveAllRecordChanges()">
      &nbsp;
      <input type="button" name="frmPrevRec" id="frmPrevRec" value="Previous" onClick="prevRow()">
      &nbsp;
      <input type="button" name="frmNextRec" id="frmNextRec" value="Next" onClick="nextRow()">
      </td>
  </tr>
</table>

</div>
<%
 } // if (fd != null)

%>
<% if(Exceldump != null && Exceldump.equals("false")) { %>
<div name="tableInterface" id="tableInterface" <%=fd!=null?"style='display:none'":""%>>
<table border="1" bordercolor="#666666" cellspacing="0" cellpadding="0"  width="100%">
<tr>
    <td>
        <div id="menu">
            <table cellspacing="0" cellpadding="0">
            <tr>
                <td>
<!-- FILE ------------------------------------------------------------------------------------------------------->

                    <div class="top"><a href="#">File</a></div>
                    <div class="section-top">
<!-- Save ------------------------------------------------------------------------------------------------------->
<%
if ( !isInDesignMode )
{
	if (   (ViewPreference.equals("LATEST_BY_USER") && tbACL.canReadWriteOnMyLatestView() )
		||    ( ViewPreference.equals("LATEST") && tbACL.canWriteLatestOfTable() )
		||    ( ViewPreference.equals("MY_ROWS") && tbACL.canReadWriteLatestOfMyRows())
		||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH") && tbACL.canReadWriteLatestofMyGroup())
		||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD") && tbACL.canReadWriteLatestofMyGroupAndImmediateChildren())
		||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD") && tbACL.canReadWriteLatestofMyGroupAndAllChildren())


	 )


	{
		if ( tableIsReadOnly == false )
		{
%>

                        <div class="box"><a href="javascript:commitToBoardwalk('commitCells')">Save</a></div>
<%
		}
	}// allowed to save table

%>
<!-- End of Save -------------------------------------------------------------------------------------------------->
<!-- Open in Excel ------------------------------------------------------------------------------------------------>
<div class="box"><a href="javascript:openInExcel()">Open In Excel</a></div>
<!-- End of Open ---------------------------------------------------------------------------------------------->
<!-- Copy Table --------------------------------------------------------------------------------------------------->
<%
	if ( tbACL.getACL() > 0  && baselineId < 1 )
	{
%>
                        <div class="box"><a href="javascript:setActionAndSubmit('copyTable')">Copy</a></div>
<%
	}
%>
<!-- End of copy table -------------------------------------------------------------------------------------------->



                        <div class="box"><a href="javascript:window.print()">Print</a></div>
<!-- Lock Table --------------------------------------------------------------------------------------------------->
<%
	if ( tbACL.canAdministerTable() && baselineId < 1 )
	{

		if (!isTableLocked )
		{
%>
                        <div class="box"><a href="javascript:setActionAndSubmit('lockTable')">Lock</a></div>
<%
		} // table is not locked
		else
		{
%>
                        <div class="box"><a href="javascript:setActionAndSubmit('unlockTable')">Unlock</a></div>
<%
		}// table is already locked
	} // if user can administer table
%>

<!-- End of Lock table -------------------------------------------------------------------------------------------->

<%
}
	}// not in design mode
else if(Exceldump != null && Exceldump.equals("false"))
{
%>
<!-- Save Default Values (design mode) ------------------------------------------------------------------------->

                        <div class="box"><a href="javascript:commitToBoardwalk('commitCells')">Save Default Values</a></div>
			<div class="box"><a href="javascript:getTableView('<%=tbACL.getSuggestedViewPreferenceBasedOnAccess()%>')">Reopen Table</a></div>
<%}// in design mode
%>
<!-- End Save Default Values (design mode) ------------------------------------------------------------------------->
<%
if(Exceldump != null && Exceldump.equals("false")){%>
 <!-- Email URL -------------------------------------------------------------------------------------------->
                         <div class="box"><a href="javascript:emailURL()">Send to Mail Recipient</a></div>
 <!-- Email URL-------------------------------------------------------------------------------------------->
 <%
 	if ( tbACL.canAdministerTable() && baselineId < 1 )
 	{
 %>
<!-- Properties -------------------------------------------------------------------------------------------->
                        <div class="box"><a href="javascript:showTableProperties()">Properties</a></div>
<!-- End of Properties -------------------------------------------------------------------------------------------->
<%
}
%>

 <!-- Logout -------------------------------------------------------------------------------------------->
                         <div class="box"><a href="javascript:logout()">Logout</a></div>
 <!-- End of Logout -------------------------------------------------------------------------------------------->

                    </div>
<!-- End of FILE MENU -------------------------------------------------------------------------------------------->

                </td>
                <td>



<!-- EDIT MENU -------------------------------------------------------------------------------------------->

                    <div class="top"><a href="#">Edit</a></div>
                    <div class="section-top">
 <%
 if ( ( tbACL.canDeleteRow()  || tbACL.canAdministerColumn() ) && tableIsReadOnly == false)
 {
%>
                        <div class="sub">
                            <div class="box-right"><a href="#">Delete</a><img src="images/arrow.gif" width="10" height="12"></div>
                            <div class="section">
<!-- Delete Row ---------------------------------------------------------------------------------------------------------------------->
<%
if ( tbACL.canDeleteRow()  && tableIsReadOnly == false)
{
%>
                                <div class="box"><a href="javascript:deactivateRow('deleteRow')">Row</a></div>
<%
} // tbACL.canDeleteRow()
%>
<!-- End Delete Row ------------------------------------------------------------------------------------------------------------------>
<%
if ( tbACL.canAdministerColumn()  && tableIsReadOnly == false)
{
%>
                                <div class="box"><a href="javascript:deleteColumn()">Column</a></div>
<%
} // tbACL.canAdministerColumn()
%>
                            </div>
                        </div>
   <%
   } // tbACL.canDeleteRow()  || tbACL.canAdministerColumn()
%>

<%
 if ( tbACL.canAdministerColumn()  && tableIsReadOnly == false)
{
%>
                        <div class="box"><a href="javascript:insertColumn(true)">Column</a></div>
 <%
} // tbACL.canAdministerColumn()
%>
<!-- Edit Design Vaues ------------------------------------------------------------------------------------------------------------------->
<%
if ( !isInDesignMode && tbACL.canAdministerTable() && baselineId < 1 )
{
%>
                        <div class="box"><a href="javascript:getDesignValues('getDesignValues')">Design Values</a></div>
<%
}//!isInDesignMode && tbACL.canAdministerTable()
%>
<!-- End Edit Design Values --------------------------------------------------------------------------------------------------------------->

<%
 if ( tableIsReadOnly == false)
{
%>
                        <div class="box"><a href="javascript:editRow()">Row</a></div>
 <%
}
%>
<!-- Change Row Owner---------------------------------------------------------------------------------------------------------------------->
<%
if ( tableIsReadOnly == false)
{
%>
                        <div class="box"><a href="javascript:changeRowOwner('changeOwnershipForm')">Change Row Owner</a></div>
<%
} //
%>
<!-- End Change Row Owner------------------------------------------------------------------------------------------------------------------>
<!-- Find Lookup ---------------------------------------------------------------------------------------------------------------------->

    <div class="sub">
    <div class="box-right"><a href="#">Find</a><img src="images/arrow.gif" width="10" height="12"></div>
    <div class="section">
		<div class="box"><a href="javascript:showRecordsReferingToSelectedCell()"> Records Refering To Cell </a></div>
		<div class="box"><a href="javascript:showRecordsThisCellisReferingTo()">Records Refered By Cell </a></div>
    </div>
     </div>

<!-- End Change Row Owner------------------------------------------------------------------------------------------------------------------>

                    </div>
<!-- End of EDIT MENU ----------------------------------------------------------------------------------------------------------------->

                </td>
                <td>

                    <div class="top"><a href="#">View</a></div>
                    <div class="section-top">
                        <div  class="box"><a id="ViewSysColMenu" href="javascript:showSystemColumns()">Show System Columns</a></div>
                        <div  class="box"><a  href="javascript:zoomTable('in')">Zoom In</a></div>
                        <div  class="box"><a  href="javascript:zoomTable('out')">Zoom Out</a></div>
                    </div>
                </td>
                <td>

<!-- INSERT MENU --------------------------------------------------------------------------------------------------------------------->
                    <div class="top"><a href="#">Insert</a></div>
                    <div class="section-top">

<!-- Insert Row ---------------------------------------------------------------------------------------------------------------------->
<%
if ( tbACL.canAddRow()  && tableIsReadOnly == false)
{
%>
                        <div class="box"><a href="javascript:addRow('addRow')">Row</a></div>
<%
} // tbACL.canAddRow()
%>
<!-- End Insert Row ------------------------------------------------------------------------------------------------------------------>

<%
if ( tbACL.canAddRow()  && tableIsReadOnly == false)
{
%>
                        <div class="box"><a href="javascript:addRowViaFormRequest('addRowViaFormRequest')">Row Using Form</a></div>
<%
} // tbACL.canAddRow()
%>
<!-- End Insert Row Form------------------------------------------------------------------------------------------------------------------>







<%
if ( tbACL.canAdministerColumn()  && tableIsReadOnly == false)
{
%>
                        <div class="box"><a href="javascript:insertColumn(false)">Column</a></div>
  <%
  } // tbACL.canAdministerColumn()
%>
<!-- Insert Hyperlink ------------------------------------------------------------------------------------------------------------------>
<%
if ( tableIsReadOnly == false)
{
%>
                        <div class="box"><a href="javascript:addHyperlink()">Hyperlink</a></div>
                        <div class="box"><a href="javascript:addDocumentToCell()">Document</a></div>

<%
} //tableIsReadOnly == false
%>
<!-- End Insert Hyperlink -------------------------------------------------------------------------------------------------------------->

<!-- Insert Comment ------------------------------------------------------------------------------------------------------------------>
<%
if ( tableIsReadOnly == false)
{
%>
                        <div class="box"><a href="javascript:addComment()">Comment</a></div>
<%
} //tableIsReadOnly == false
%>
<!-- End Insert Comment -------------------------------------------------------------------------------------------------------------->

                    </div>
                </td>
<!- END OF INSERT MENU --------------------------------------------------------------------------------------------------------------->


<!- FORMAT MENU --------------------------------------------------------------------------------------------------------------->
                <td>
		                    <div class="top"><a href="#">Format</a></div>
		                    <div class="section-top">
		                        <div class="box"><a href="javascript:hideColumn()">Hide Column</a></div>
					<div class="box"><a href="javascript:unhideColumns()">Unhide All Column</a></div>

		                    </div>
                </td>
<!- END FORMAT MENU --------------------------------------------------------------------------------------------------------------->

<!- TOOLS MENU --------------------------------------------------------------------------------------------------------------->

                <td>
                    <div class="top"><a href="#">Tools</a></div>
                    <div class="section-top">
  <%
if ( baselineId < 1 )
{
%>
           				<div class="box"><a href="javascript:compareTable()">Compare Table</a></div>
                        <div class="sub">
                            <div class="box-right"><a href="#">List Updates</a><img src="images/arrow.gif" width="10" height="12"></div>
                            <div class="section">
							  <div class="box"><a href="javascript:getCellUpdates(eval('document.all.selectedCellId').value, <%=tableId%>, '<%=tbi.getTableName()%>')">Selected Cell</a></div>
							  <div class="box"><a href="javascript:getRowUpdates(eval('document.all.selectedCellId').value, <%=tableId%>, '<%=tbi.getTableName()%>')">Selected Row</a></div>
							  <div class="box"><a href="javascript:getTableUpdates(<%=tableId%>, '<%=tbi.getTableName()%>','<%=ViewPreference%>')">Entire Table</a></div>
						</div>
						</div>
<%
}
%>

<%
if ( tbACL.canAdministerTable()  && tableIsReadOnly == false)
{
%>
			<div class="sub">
            <div class="box-right"><a href="#">Access Control </a><img src="images/arrow.gif" width="10" height="12"></div>
			<div class="section">
			  <div class="box"><a href="javascript:setActionAndSubmit('editTableAccess')">Rows</a></div>
			  <div class="box"><a href="javascript:setActionAndSubmit('editColumnAccess')">Columns</a></div>
			</div>
			</div>
  <%
  } //canAdministerTable == false
%>
                        <div class="box"><a href="javascript:addBookmark()">Add Bookmark</a></div>

                    </div>
                </td>
<!- END TOOLS MENU --------------------------------------------------------------------------------------------------------------->

<!- DATA MENU --------------------------------------------------------------------------------------------------------------->
              <td>

                    <div class="top"><a href="#">Data</a></div>
                    <div class="section-top">
                    	<div class="box"><a href="javascript:dataForm()" >Form</a></div>
                        <div class="box"><a href="javascript:filterColumn2()">Filter column</a></div>
                        <div class="box"><a href="javascript:sortTable('bwTableContents',  eval('document.all.selectedColumnIdx').value, true)">Sort Table</a></div>

                        <div class="sub">
<!- Row Query --------------------------------------------------------------------------------------------------------------->

			    <div class="box-right"><a href="#">Rows </a><img src="images/arrow.gif" width="10" height="12"></div>
			    <div class="section">
<%
if ( tbACL!= null && tbACL.canReadLatestOfTable())
{
%>
				<div class="box"><a href="javascript:getTableView('LATEST')">All</a></div>
				<div class="box"><a href="javascript:getTableViewWithQueryPreference('LATEST','ROWS_BY_USER')">Sorted By Owner</a></div>
				<div class="box"><a href="javascript:getTableReportByNh('LATEST_ROWS_OF_ALL_USERS_IN_ANY_NH')">For a Neighborhood...</a></div>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH')">My Neighborhood</a></div>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD')">Direct Reports </a></div>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD')">All My Reports</a></div>


<%
}
else if (tbACL!= null &&  tbACL.canReadLatestofMyGroupAndAllChildren())
{
%>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH')">My Neighborhood</a></div>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD')">Direct Reports </a></div>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD')">All My Reports</a></div>


<%
}
else if (tbACL!= null &&  tbACL.canReadLatestofMyGroupAndImmediateChildren())
{
%>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH')">My Neighborhood</a></div>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD')">Direct Reports</a></div>



<%
}
else if (tbACL!= null &&  tbACL.canReadLatestofMyGroup())
{
%>
				<div class="box"><a href="javascript:getTableView('LATEST_ROWS_OF_ALL_USERS_IN_MY_NH')">My Neighborhood</a></div>



<%
}

boolean renderTablebasedQueries = false;
if (tbACL!= null &&  tbACL.canReadWriteLatestOfMyRows())
{
%>
				<div class="box"><a href="javascript:getTableView('MY_ROWS')">Owned By Me</a></div>
<%
}
%>
			    </div>
<!-End Row Query --------------------------------------------------------------------------------------------------------------->

                        </div>
<!-Table Query --------------------------------------------------------------------------------------------------------------->
<%
if  (tbACL != null   &&
    (tbACL.canReadWriteOnMyLatestView() || tbACL.canReadLatestViewOfAll() || tbACL.canReadLatestViewOfAllChildren()))

{
%>
			<div class="sub">
			    <div class="box-right"><a href="#">Table</a><img src="images/arrow.gif" width="10" height="12"></div>
			    <div class="section">
<%
if ( tbACL!= null && tbACL.canReadWriteOnMyLatestView())
{
%>
				<div class="box"><a href="javascript:getTableView('LATEST_BY_USER')">Show My Latest</a></div>

<%
} // can read/write my latest stuff
if ( tbACL!= null && tbACL.canReadLatestViewOfAll())
{
%>
				<div class="box"><a href="javascript:getTableReport('LATEST_VIEW_OF_ALL_USERS')">All</a></div>
				<div class="box"><a href="javascript:getTableReportByNh('LATEST_VIEW_OF_ALL_USERS_IN_ANY_NEIGHBORHOOD')">For a Neighborhood..</a></div>
<%
} // can read/write other people's latest stuff
if ( tbACL!= null && tbACL.canReadLatestViewOfAllChildren())
{
%>
				<div class="box"><a href="javascript:getTableReport('LATEST_VIEW_OF_ALL_CHILDREN')">All Reports</a></div>
				<div class="box"><a href="javascript:getTableReportByNh('LATEST_VIEW_OF_ALL_USERS_IN_ANY_CHILDREN_NEIGHBORHOOD')">For a specific report</a></div>
<%
}// can read write children's latest stuff

%>
			    </div>
                        </div>
<%
}
%>
                    </div>
<!-End Table Query --------------------------------------------------------------------------------------------------------------->

                </td>
<!- END DATA MENU --------------------------------------------------------------------------------------------------------------->

            </tr>
            </table>
        </div>
    </td>
</tr>
</table>
<div align="right">








</div>
<script language="javascript">
menuInit();
</script>
<%}%>
<%if(Exceldump != null && Exceldump.equals("false")){%>
<table style="table-layout=auto;" width="100%" cellspacing="0" cellpadding="2" border="1" bordercolor="#666666">
<%}else{%>
<table  style="table-layout=auto;" width="100%" cellspacing="0" cellpadding="2" border="0" bordercolor="#666666">
<%}%>
<tr>
<td class="body">
<!--start main page-->
<form name="EditTable" method="post" action="MyTables"  >

<!---------The main table ----------------------------------------------------------------------------------->
<b>
<%
				if(Exceldump != null && Exceldump.equals("false"))
				{%>

				<input name="checkAll" onClick="flipAllCheckBoxes(this)" type="checkbox" width="1%" value="checkAll" >
					<a href="javascript:openInExcel()" title="View the table in Excel"><img src="images/icon-excel.gif" width="16" height="16"></a>
					<!-- <a href="javascript:openInBwsFormat()" title="Import Linked table in Excel"><img src="images/icon-excel.gif" width="16" height="16"></a> -->
				<%}
				if ( isInDesignMode )
				{
					    //System.out.println("In design mode ***************************" );
					   if ( tbACL.canAdministerColumn() )
					   {





				%>
					   <a href="javascript:commitToBoardwalk('commitCells')" title="Save Default Values"><img src="images/save.gif" width="16" height="16"></a> |
					   <a href="javascript:addRow('addRow')" title="Insert New Row"><img src="images/insertrow.gif" width="16" height="16"></a>&nbsp;
					   <a href="javascript:addRowViaFormRequest('addRowViaFormRequest')" title="Insert New Row Using Form"><img src="images/form.jpg" width="20" height="16"></a>&nbsp;
					   <a href="javascript:editRow()" title="Edit Row"><img src="images/editrow.jpg" width="18" height="18"></a>&nbsp;
					   <a href="javascript:deactivateRow('deleteRow')" title="Delete Selected Row(s)"><img src="images/deleterow.gif" width="16" height="16"></a>&nbsp;

					    <%
					 }
				}

				else if(Exceldump != null && Exceldump.equals("false"))
				{
					 ////System.out.println("Not In design mode " );

					if (        (ViewPreference.equals("LATEST_BY_USER") && tbACL.canReadWriteOnMyLatestView() )
								||    ( ViewPreference.equals("LATEST") && tbACL.canWriteLatestOfTable() )
								||    ( ViewPreference.equals("MY_ROWS") && tbACL.canReadWriteLatestOfMyRows())
								||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH") &&  tbACL.canReadWriteLatestofMyGroup())
								||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD") &&  tbACL.canReadWriteLatestofMyGroupAndImmediateChildren())
								||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD") &&  tbACL.canReadWriteLatestofMyGroupAndAllChildren()
							)

					)
					{
						if ( tableIsReadOnly == false )
						{
					%>
						<a href="javascript:commitToBoardwalk('commitCells')" title="Save Table"><img src="images/save.gif" width="16" height="16"></a>&nbsp;
						<a href="javascript:addHyperlink()" title="Insert Hyperlink"><img src="images/hyperlink.gif" width="16" height="16"></a>&nbsp;
						<a href="javascript:addComment()" title="Insert Comment"><img src="images/comment.gif" width="16" height="16"></a>&nbsp;
					<%
						}
					  }


					   if ( tbACL.canAddRow() )
					   {
					 	 %>
					    <a href="javascript:addRow('addRow')" title="Insert New Row"><img src="images/insertrow.gif" width="16" height="16"></a>&nbsp;
					   <a href="javascript:addRowViaFormRequest('addRowViaFormRequest')" title="Insert New Row Using Form"><img src="images/form.jpg" width="20" height="16"></a>&nbsp;
					   <%
				   		}

				   	  	if ( tableIsReadOnly == false)
				   	  	{
						%>
					   <a href="javascript:editRow()" title="Edit Row"><img src="images/editrow.jpg" width="18" height="18"></a>&nbsp;
				   		<%
				   		}


					 	if ( tbACL.canDeleteRow() )
					   {
					  %>
					   <a href="javascript:deactivateRow('deleteRow')" title="Delete Selected Row(s)"><img src="images/deleterow.gif" width="16" height="16"></a>&nbsp;

						<%
						}
					

		%>
    </b>

   <img id="sysColShowImg" valign="bottom" onclick="showSystemColumns()" src="images/expand.gif" title="Show System Columns">
</div>
<%}
if ( rowids.size() > 0 && Exceldump != null && Exceldump.equals("false"))
{
%>

<img id="tableUpdateStatusImg" src="images/collapse.gif" onClick="expandTableUpdateStatus()" title="Hide Table Update Status">
<div>

<div  align="top" id="tableUpdateStatus">
	Last updated by <u><b id="tableUpdatedBy"></b></u>&nbsp;  on &nbsp;<span id="tableUpdatedOn"> </span>

<div id="tableUpdateComment"></div>
</div>

<%
	if ( bucketSize > 0 )

	{
%>
			Displaying page <%=displayPageNumber%> of <%=(rowids.size()/bucketSize)+1%> page(s)
<%
	}
}
%>
<table  id="bwTable" class="body" border="1" bordercolor="#eeeeee" cellspacing="0" cellpadding="0">
<colgroup id="bwColumns">
<%

if (columnsSortedBySeqNum.size() > 0)
{
	int addlColumns = 0;
	if (showTableChanges == true)
	{
		if (showDiffSideBySide == true)
		{
			addlColumns = tbchg.getColumnsByColumnId().size() + 1;
		}
		else
		{
			addlColumns = 1;
		}
	}
	for ( int columnIndex = 0;
	      columnIndex< columnsSortedBySeqNum.size()+1 - hiddenColumnIds.size() + addlColumns;
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
<col id="SystemColumns" span="4" STYLE="display:none">
</colgroup>
<thead>



<tr name="bwHeadings" id="bwHeadings">
<%if(Exceldump != null && Exceldump.equals("false"))
	{%>
<th width="1%">
</th>
<%}
int visibleColumnIndex = 0;
if (showTableChanges == true)
{
visibleColumnIndex++;
%>
<th width="10px">
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
			//System.out.println("Hiding column heading for column " + c.getId());
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
		//c.print();

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
			<%	if(Exceldump.equals("false")) {%>
			<a style="display:inline;" href="MyTables?action=editTable&tableId=<%=c.getLookupTableId()%>&ViewPreference=LATEST">
			   <img  style="display:inline;" height="12" width="12" src="images/refersto.gif"   onmouseover ="this.title='Show Lookup Table'" > 
		   </a>
<% 					if (lookupcolumns.get(new Integer(c.getLookupColumnId())) == null)
					{
						lookupcolumns.put(new Integer(c.getLookupColumnId()), new Integer(c.getLookupColumnId()));
					%>
						<input type="hidden" name="strVal<%=c.getLookupColumnId()%>" id="<%=c.getLookupTableId()%>" value="<%=MyTables.getValuesOfLookUpTable(c.getLookupTableId(), c.getLookupColumnId())%>">
					<%
					}
%>
			<%	} %>

	  <%
	  	}

	  	if (tablesUsingLkpForColumn != null && Exceldump.equals("false"))
	  	{
			Vector vtulc = (Vector)tablesUsingLkpForColumn.get(new Integer(c.getId()));
			if (vtulc != null)
			{
				//System.out.println("Column with id : " + c.getId() + " is being used as lookup column");



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
	if (showTableChanges == true && showDiffSideBySide == true)
	{
		Column c_tbchg = (Column)columns_tbchg.get(new Integer(c.getId()));
		if (c_tbchg != null)
		{
		%>
		<th class="BWTableHead"><%=visibleColumnName%>(old)</th>
		<%
		visibleColumnIndex++;
		}
	}
  } // end for

%>


   <th name="User" class="BWTableHeadUserDetails"
			ondblclick="this.blur(); return sortTable('bwTableContents',  <%=visibleColumnIndex %>, true);" >Owned By
	</th>


   <th name="User" class="BWTableHeadUserDetails"
			ondblclick="this.blur(); return sortTable('bwTableContents',  <%=visibleColumnIndex++ %>, true);" > Updated By
	</th>

	<th name="Last Update" class="BWTableHeadUserDetails"
			ondblclick="this.blur(); return sortTable('bwTableContents',  <%=visibleColumnIndex++ %>, true);" >Updated On
	</th>
	<th name="Comments"class="BWTableHeadUserDetails"
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
		System.out.println("Now of rows to process = " + rowids.size() );
		//for ( int rowIndex=0; rowIndex < rowids.size(); rowIndex++ )
		for ( int rowIndex=0; rowIndex < rowids.size() ; rowIndex++ )
		{
			//System.out.println("Processing next row");
			boolean upRow = false;
			boolean downRow = false;
			boolean noChangeRow = false;
			boolean deletedRow = false;
			boolean displayRow = true;
			boolean newRow = false;
			boolean changedRow = false;
			Integer a_rowIntegerId = (Integer)rowids.elementAt( rowIndex);
			Vector cv = null;
			Vector cells = null;
			//System.out.println("Processing row Id " + a_rowIntegerId);


			// see if the row is blank
			try
			{
				cv = (Vector)cellsByRowId.get(a_rowIntegerId);
				if (cv.size() == 0)
				{
					//System.out.println("Row Deactivated in current view");
					deletedRow = true;
				}
				else
				{
					//System.out.println("Row not deactivated in current view");
					cells = (Vector)cv.elementAt(0);
				}
			}
			catch (Exception e)
			{
				//e.printStackTrace();
			}


			// table changes
			Vector cv_tbchg = null;
			Vector cells_tbchg = null;
			boolean rowUnchanged = false;
			if (showTableChanges == true)
			{
				cv_tbchg = (Vector)cellsByRowId_tbchg.get(a_rowIntegerId);
				if (cv_tbchg != null)
				{
					//System.out.println("cv_tbchg != null");
					if (cv_tbchg.size() > 0) // status/value diff found in this row
					{
						//System.out.println("status/value diff found in this row");
						if (deletedRow == true)
						{
							if (hideDeletedRows == true)
							{
								displayRow = false;
								continue;
							}
							cells_tbchg = (Vector)cv_tbchg.elementAt(0);
							//System.out.println("Row Active in earlier view.. was deleted along the way");
							cells = new Vector();
							//System.out.println("Need to add deleted for for " + cells.size() + "cells");
							if (columnsSortedBySeqNum.size() > 0)
							{
								//System.out.println("start creating deleted row for display");
								for ( int columnIndex = 0; columnIndex< columnsSortedBySeqNum.size(); columnIndex++ )
								{
									//System.out.println("Next column index");
									Column c = (Column)columnsSortedBySeqNum.elementAt(columnIndex);
									//try to find cell in old row with this column id
									VersionedCell foundCell = null;
									for (int cidx = 0; cidx < columns_tbchg.size(); cidx++)
									{
										//System.out.println("trying to find cell");
										try
										{
											VersionedCell cl = (VersionedCell)cells_tbchg.elementAt(cidx);
											if (cl.getColumnId() == c.getId())
											{
												//System.out.println("found one cell");
												foundCell = cl;
												break;
											}
										}
										catch (Exception e)
										{
											// do nothing, means cell was not changed , should catch ClassCastException
											// since the placeholder is new Integer(0)
											//e.printStackTrace();
										}
									}
									if (foundCell != null)
									{
										cells.addElement(foundCell);
										//System.out.println("added found cell to row");
									}
									else
									{
										cells.addElement(new Integer(0));
										//System.out.println("added dummy cell to row");
									}
								}

								//System.out.println("Finished creating deleted row for display purposes");
							}
						}
						else // change detected inside a row
						{
							//System.out.println("This row has changed");
							changedRow = true;
							if (hideChangedRows == true)
							{
								displayRow = false;
								continue;
							}
							cells_tbchg = (Vector)cv_tbchg.elementAt(0);
						}
					}
					else
					{
						if (deletedRow == true)
						{
							//System.out.println("Row not in earlier view either .. don't display");
							displayRow = false;
							continue;
						}
						else
						{
							//System.out.println("Row is unchanged");
							rowUnchanged = true;
							if (hideUnchangedRows == true)
							{
								displayRow = false;
								continue;
							}
						}
					}
				}
				else // row not present in earlier view
				{
					newRow = true;
					//System.out.println("Row not in earlier view");
					if (hideNewRows == true)
					{
						displayRow = false;
						continue;
					}
				}

				if (rowUnchanged == true && showChangesOnly == true)
				{
					//System.out.println("Row Unchanged");
					displayRow = false;
					continue;
				}
			}
			if (cells == null)
			{
				displayRow = false;
				continue;
			}
			int rowId = ((Integer)rowids.elementAt(rowIndex)).intValue();
			Row row = (Row)rowObjsById.get( a_rowIntegerId );
			rowOwner = row.getOwnerName();
			// check if you need to filter out the row
			for ( int cellIndex=0; cellIndex < cells.size(); cellIndex++ )
			{
				VersionedCell cell = null;
				try
				{
					cell = (VersionedCell) cells.elementAt( cellIndex );
				}
				catch (Exception e)
				{
					continue;
				}
				//System.out.println("Processing filters");
				// if the column is filtered
				Vector colFilters = (Vector)filterColumnIds.get(new Integer(cell.getColumnId()));
				if ( colFilters != null)
				{
					for (int iFil = 0; iFil < colFilters.size() ; iFil++)
					{
						String colFilterStr = (String)colFilters.elementAt(iFil);
						//System.out.println("Column id=" + cell.getColumnId() + " is filtered by the criterion : " + colFilterStr );
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

							//System.out.println("Checking if cell Value : " + cellValue + " is " + colFilterCondition + " " + cellFilterValue);

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

			if (!displayRow) {
				//System.out.println("Hiding row " + a_rowIntegerId);
				continue;
			}
			//System.out.println("Display this row");




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
<tr  id="row<%=rowId%>"  name="row<%=rowId%>"
<%
	if (newRow == true)
	{
	%>
	style="color=green;"
	<%
	}
	else if (deletedRow == true)
	{
	%>
	style="color=red;text-decoration:line-through"
	<%
	}
	else if (changedRow == true)
	{
	%>
	style="color=Brown;"
	<%
	}
%>
>
<%if(Exceldump != null && Exceldump.equals("false"))
	{%>

	<td>
   			<input name="rowId" onClick="highlightRow(this)" type="checkbox" width="1%" value="<%=rowId%>">
   	</td>
   	<%}
	 	if ( showTableChanges )
	 	{

	 %>
	 <td>
	 <%
	 		if ( newRow == true )
			{
			%>
			N
			<%
			}
			else if (deletedRow == true)
			{
			%>
			D
			<%
   			}
			else if (changedRow == true)
			{
			%>
			U
			<%
   			}
   			else
   			{
   			%>
   			-
   			<%
   			}
   	%>
   	</td>
   	<%
   		}

for ( int cellIndex=0; cellIndex < cells.size(); cellIndex++ )
{
	//System.out.println("Processing Next Cell");
	VersionedCell cell = null;
	try
	{
		cell = (VersionedCell) cells.elementAt( cellIndex );
	}
	catch(Exception e)
	{
	%>
		<td>&nbsp;</td>
	<%
		continue;
	}
	VersionedCell oldCellValue = null;
	String oldFormula = null;
	String titleString = "";
	String cellFormula = cell.getFormula();
	boolean hasFormula = false;
	
	// System.out.println("FFFFFFFFFFFFFFFormula=" + cellFormula + ":" );
	
	if (cellFormula != null && !cellFormula.trim().equals(""))
		hasFormula = true;
	else
		cellFormula = null;
	
	boolean formulaChanged = false;
	boolean valueChanged = false;
	Hashtable ordColValues = null;


	if ( showTableChanges )
	{
		//oldCellValue = (VersionedCell)tableConf.getCellsByCellId().get(  new Integer ( cell.getId() )    );
		if (changedRow == true)
		{
			try
			{
				oldCellValue = (VersionedCell)cells_tbchg.elementAt(cellIndex);
				oldFormula = oldCellValue.getFormula();
				String comp1 = cellFormula;
				String comp2 = oldFormula;
				if (comp1 == null)
					comp1 = "";
				if (comp2 == null)
					comp2 = "";
				
				
				//System.out.println("Comparing formulae " + comp1 + ":" + comp2);
				if (!comp1.equals(comp2))
				{
					formulaChanged = true;
					//System.out.println("Formula has changed");
				}
				else
				{
					formulaChanged = false;
					//System.out.println("Formula has not changed");
				}
				
				comp1 = oldCellValue.getValueAsString();
				comp2 = cell.getValueAsString();
				if (comp1 == null)
					comp1 = "";
				else
					comp1 = comp1.trim();
				
				if (comp2 == null)
					comp2 = "";
				else
					comp2.trim();
				
				
				//System.out.println("Comparing formulae " + comp1 + ":" + comp2);
				if (!comp1.equals(comp2))
				{
					valueChanged = true;
					//System.out.println("Formula has changed");
				}
				else
				{
					valueChanged = false;
					//System.out.println("Formula has not changed");
				}
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				// do nothing, means cell was not changed , should catch ClassCastException
				// since the placeholder is new Integer(0)
			}
		}

		ordColValues = (Hashtable)request.getAttribute("OrdCol" + cell.getColumnId());
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
		//System.out.println("Hiding cell for column " + cell.getColumnId());
		continue;
	}

	if (! ((Column)columns.get(  new Integer(cell.getColumnId()))).getIsEnumerated()   )
	{
		boolean canWrite = ((Column)columns.get(  new Integer(cell.getColumnId()))).canWrite();
		//System.out.println("canWrite = " + canWrite);

	%>
	<td  title='<%=titleString%>'  ID="cell_<%=cell.getId()%>" name="<%=cell.getId()%>"
		 columnId="<%=cell.getColumnId()%>" rowId="<%=cell.getRowId()%>" 
		<%=valueChanged?"bgcolor='pink'":""%>  <%=canWrite?"":"bgcolor='lightblue' "%> <%=hasFormula?"bgcolor='#ffffff' ":""%>>
	<%
	if ( showTableChanges )
	{
		if  ( oldCellValue != null &  valueChanged == true )
		{
	 %>
	 <% if(Exceldump != null && Exceldump.equals("false")) { %>
<script language="javascript">
var d = new Date (<%=cell.getTransaction().getCreatedOnTime()%>)
cell_<%=cell.getId()%>.title =
"<%=cell.getTransaction().getCreatedByUserAddress()%>, "+formatDateTime(d,"NNN dd, yyyy hh:mm:ssa")+ ":\n" +
"changed value from <%=oldCellValue.getValueAsString()%> to <%=cell.getValueAsString()%>" ;
</script>
	 <% } %>
		<%
			if (formulaChanged == false)
			{
				//System.out.println("Formula unchanged but cell value changed");
		%>
		<img  style="display:inline;"  src="images/track.gif"  height=12 width=12 >
		<%
			}
			else
			{
				if ( oldCellValue.getFormula() != null )
				{
					//System.out.println("Formula changed and (maybe) cell value changed");
					%>
					 <% if(Exceldump != null && Exceldump.equals("false")) { %>
					<script language="javascript">
						cell_<%=cell.getId()%>.title = cell_<%=cell.getId()%>.title + "\n" + "and the formula changed from " + "<%=oldCellValue.getFormula()%>" + " to " +  "<%=cellFormula%>"
					</script>
					<img  style="display:inline;"  src="images/trackf.gif"  height=12 width=12 >
					 <% } %>
					<%
				}
				else
				{
					//System.out.println("Formula changed and (maybe) cell value changed");
					%>
					<% if(Exceldump != null && Exceldump.equals("false")) { %>
					<script language="javascript">
						cell_<%=cell.getId()%>.title = cell_<%=cell.getId()%>.title + "\n" + "and a new formula = " +  "<%=cellFormula%>"
					</script>
					<img  style="display:inline;"  src="images/trackf.gif"  height=12 width=12 >
					<% } %>
					<%
				}
			}
		%>
	<%
		}
		else
		{
		%>
			<!---->
		<%
		}
	}
	else
	{
	%>
		<!---->
	<%
	}

	if(tableIsReadOnly == false && canWrite == true )
	{
	  	String editable = "true";
		  if (cellFormula != null )
		  {
			//System.out.println("Cell has a formula, indicate this graphically");
		    	editable = "false";
		    	%>
			<DIV  style="display:inline;" onmouseover="checkForURL(this)" contentEditable="<%=editable%>" onfocus="changeCell(this.parentElement)"  STYLE="height: 100%; width: 80%;">
				<span><img  title="<%=BoardwalkUtility.getHTMLWithEcsapeSequence(cellFormula)%>" style="display:inline;"  src="images/formula.gif"  height=18 width=18 ></span><%=cell.getValueAsHtmlString()%>
			</DIV>
			<%
		  }
			else
			{
	%>
			<DIV  style="display:inline;" onmouseover="checkForURL(this)" contentEditable="<%=editable%>" onfocus="changeCell(this.parentElement)"  STYLE="height: 100%; width: 80%;">
				<%=cell.getValueAsHtmlString()%>
			</DIV>
			<% } %>
		<%
	}
	else
	{
	%>
	     <%=cell.getValueAsString()%>
	<%

		if (oldCellValue != null )
		{

			if (showDiffNone == null)
			{
				//System.out.println(" See if you need to show numeric differences");
				DeltaValue dv = dcc.getDiff(cell,oldCellValue);
				if (dv != null && !dv.difference.equals(""))
				{
					//System.out.println("Found numeric difference " + dv.difference);
					double dd = dv.getNewDblValue();
					double dp = 0;
					String diffcolor = "green";
					if (dv.getOldDblValue() != 0)
					{
						if ( (dv.getNewDblValue() - dv.getOldDblValue()) < 0.0)
						{
							diffcolor = "red";
							dp = ( dv.getOldDblValue() - dv.getNewDblValue())/dv.getOldDblValue();
						}
						else
						{
							if ( dv.getNewDblValue() != 0 )
							{
							     dp = dd/dv.getOldDblValue();
							}
						}
					}


					if (  dv.getNewDblValue() < 0 && dv.getOldDblValue() == 0 )
					{
						diffcolor = "red";
					}

					if ( showDiffPercent!=null && showDiffPercent.equals("true"))
					{
						String dpStr = "";
						if ( dv.getOldDblValue() != 0 && dv.getNewDblValue() != 0 )
						{
							java.text.NumberFormat nf = java.text.NumberFormat.getPercentInstance();
							nf.setMaximumFractionDigits(2);
							if ( dp < 0 )
							{
							  dp = dp-dp-dp;
							}
							dpStr = nf.format(dp);
						}
						else if ( dv.getNewDblValue() == 0 )
						{
							dpStr = "previous value "+dv.getOldDblValue();
						}
						else
						{
							dpStr = "previous value 0";
						}
					%>
						<font color="<%=diffcolor%>"><small><b>(<%=dpStr%>)</small></b></font>
					<%
					}
					else if (showDiffAbsolute!=null && showDiffAbsolute.equals("true"))
					{
					%>
						<font color="<%=diffcolor%>"><small><b>(<%=dv.difference%>)</small></b></font>
					<%
					}
				}
				//System.out.println("showed changes inline");
			}

			if (ordColValues != null)// track state
			{
				//System.out.println("Show lifecycle changes");
				Integer seq1 = (Integer)ordColValues.get(oldCellValue.getStringValue());
				Integer seq2 = (Integer)ordColValues.get(cell.getStringValue());
				if (seq1 != null && seq2 != null)
				{
					if (seq1.intValue() > seq2.intValue()) // up
					{
					%>
					<img  style="display:inline;"  src="images/green-dot.jpg"   onmouseover ="this.title='State moved up'">
					<%
					}
					else // down
					{
					%>
					<img  style="display:inline;"  src="images/red-dot.jpg"   onmouseover ="this.title='State moved up'">
					<%
					}
				}
			}

		} // show inline changes

		if (showTableChanges == true && showDiffSideBySide == true)
		{
			//System.out.println("show differences side by side");
			// see if the column is required to display the changed cells
			if (oldCellValue != null)
			{
			%>
			<td bgcolor="pink" > <%=oldCellValue.getValueAsString()%></td>
			<%
			}
			else
			{
			%>
			<td>&nbsp;</td>
			<%
			}
		}
	}

  %>
</td>
<%if(Exceldump != null && Exceldump.equals("false")){%>
  <input type="hidden" name="pCell<%=cell.getId()%>" value='<%=cell.getValueAsString()%>'>
  <input type="hidden" name="CellType<%=cell.getId()%>" value='<%=cell.getType()%>'>
  <input type="hidden" name="Cell<%=cell.getId()%>" value='<%=cell.getValueAsDivString()%>'>
  <input type="hidden" name='dirtyCell<%=cell.getId()%>' value=false>
<% } %>
<%
	}
	else if (((Column)columns.get(new Integer(cell.getColumnId()))).getIsEnumerated())
	{

%>


 <td  title="<%=titleString%>"  ID="cell_<%=cell.getId()%>" name="<%=cell.getId()%>"
      columnId="<%=cell.getColumnId()%>" rowId="<%=cell.getRowId()%>">
 <%
	if ( showTableChanges  )
	{

 	 	//oldCellValue = (VersionedCell)tableConf.getCellsByCellId().get(  new Integer ( cell.getId() )    );

 	 	if  ( oldCellValue != null )
 	 	{
  %>
 			<img  style="display:inline;"  src="images/track.gif"  height=12 width=12 >
 <%
 		}
   }

 Column cellColumn = (Column)columns.get(new Integer(cell.getColumnId()));
 if(tableIsReadOnly == false && showDropDowns == true) {

%>
 	<select onclick="lookupCellClicked(this)" ID="cell_<%=cellId%>" name="<%=cell.getId()%>" onChange="lookupValueSelected(this)" class="LookupList"   >
<%


Vector enumeratedValues = cellColumn.getEnumerations();
for ( int index = 0; index < enumeratedValues.size(); index++ )
{
	String originalvalue = (enumeratedValues.elementAt(index)).toString().trim();
	String optionvalue = (enumeratedValues.elementAt(index)).toString();

	if ( optionvalue.trim().indexOf("<a") == 0 ||   optionvalue.trim().indexOf("<A") ==0 )
	{
			// optionvalue is a http link we need to parse it
			int indexendofATag = optionvalue.trim().indexOf(">") ;
			int indexstartofSlashATag = optionvalue.trim().indexOf("<", indexendofATag) ;
			optionvalue = optionvalue.trim().substring( indexendofATag+1, indexstartofSlashATag ).trim();

	}



	if ( originalvalue.equals( cell.getValueAsString() ) )
	{

	%>

		<option value="<%=optionvalue%>" selected ><%=originalvalue%></option>
	<%
	}
	else
	{
	%>
		 <option value="<%=optionvalue%>" > <%=originalvalue%> </option>
	<%

	}
}

%>
	</select>
<%
} // if table is not read only
else
{
%>

<%	if(Exceldump.equals("false")) {%>

<div id="div<%=cell.getId()%>" style="position:absolute; z-index:1; visibility: hidden" valign="bottom" >
<table>
	<tr> 
		<td>
			<select name="selcellId<%=cell.getId()%>" onclick="javascript:setInJsp()" >
			</select>
		</td>
	</tr>
</table>
</div>

<div ID="tdcelldiv<%=cell.getId()%>" onClick="changeCellLookup(this.parentElement)">
<%=cell.getValueAsString()%>&nbsp;
</div>

<% } else { %>
<!-- style="position:absolute; z-index:1; left: 315px; top: 88px; width: 300px; height: 200px; visibility: hidden" -->
<%=cell.getValueAsString()%> 

<% } %>

<%
} // table is read only

%>
<%	if(Exceldump.equals("false")) {%>
 <!--<a style="display:inline;" href="MyTables?action=editTable&tableId=<%=cellColumn.getLookupTableId()%>&ViewPreference=LATEST&cc<%=cellColumn.getLookupColumnId()%>=equals:<%=cell.getValueAsString()%>"    >
 <img  style="display:inline;" height="12" width="12" src="images/refersto.gif"   onmouseover ="this.title='Show Lookup Table'" >
 </a>-->

 <a style="display:inline;" >
 <img  style="display:inline;" onClick ="selectNewValue('<%=cellColumn.getLookupColumnId()%>','<%=cell.getId()%>')" height="12" width="12" src="images/refersto.gif"   onmouseover ="this.title='Show Lookup Values'" >
 </a>

<%	} %>

<%
	if (tablesUsingLkpForColumn != null && Exceldump.equals("false") )
	{
		Vector vtulc = (Vector)tablesUsingLkpForColumn.get(new Integer(cell.getColumnId()));
		if (vtulc != null)
		{
			//System.out.println("Column with id : " + cell.getColumnId() + " is being used as lookup column");


  %>
	   <a style="display:inline;" href="MyTables?action=showLkpReferences&tableId=<%=tableId%>&lkpColumnName=<%=cell.getColumnName()%>">
	   	   	   <img  style="display:inline;" height="12" width="12" src="images/referedby.gif"   onmouseover ="this.title='Show Tables referring this column for lookup'" >
	   </a>
  <%
		}
	}

	if (showTableChanges == true && showDiffSideBySide == true)
	{
		//System.out.println("show differences side by side");
		// see if the column is required to display the changed cells

		if (oldCellValue != null && !oldCellValue.getValueAsString().equals(""))
		{
		%>
		<td bgcolor="pink" > <%=oldCellValue.getValueAsString()%></td>
		<%
		}
		else
		{
		%>
		<td>&nbsp;</td>
		<%
		}
	}

  %>

 </td>
 <%if(Exceldump != null && Exceldump.equals("false")){%>
 <input type="hidden" name="pCell<%=cell.getId()%>" value="<%=cell.getValueAsString()%>">
 <input type="hidden" name="CellType<%=cell.getId()%>" value="<%=cell.getType()%>">
 <input type="hidden" name="Cell<%=cell.getId()%>" value="<%=cell.getValueAsString()%>">
 <input type="hidden" name="dirtyCell<%=cell.getId()%>" value=false>
 <% } %>
<%
}

//System.out.println("Finished Processing this cell");
cellId++;

}//for ( int cellIndex=0; ....

	 %>
	 		<td ><%=rowOwner%> </td>
			<td ><%=maxRowTransaction.getCreatedByUserAddress()%> </td>
			<td id="upd<%=rowId%>"> </td>
<% if(Exceldump != null && Exceldump.equals("false")) { %>
<script language="javascript">
var d = new Date(<%=maxRowTransaction.getCreatedOnTime()%>)
upd<%=rowId%>.innerText = formatDateTime(d,"NNN dd, yyyy hh:mm:ssa")
</script>
<% } %>
	<%
		if (maxRowTransaction.getComment()!=null)
		{
	%>
			<td><%=maxRowTransaction.getComment()%> </td>
	<%
		}
	%>



	<%
	maxRowTransaction = null;

  } // for int rowIndex=0 ....

}//end if rowids.size() > 0

System.out.println("Done processing rows");

%>


</tbody>

<%
	if(maxTableTransaction != null)
	{
%>
<% if(Exceldump != null && Exceldump.equals("false")) { %>
<script>
	tableUpdatedBy.innerText = trim("<%=maxTableTransaction.getCreatedByUserAddress()%>");
	var d = new Date(<%=maxTableTransaction.getCreatedOnTime()%>)
	tableUpdatedOn.innerText = formatDateTime(d,"NNN dd, yyyy hh:mm:ssa")
	tableUpdateComment.innerText =trim("<%=maxTableTransaction.getComment()!=null?maxTableTransaction.getComment():""%>");
<%
	if (fd != null)
	{
%>
	getRow()
<%
	}
%>
</script>
<% } %>
<%
	}
%>


</table>








<input type="hidden" id="tableId" name="tableId" value="<%=tableId%>">
<input type="hidden" id="selectedTableId" name="selectedTableId" value="<%=tableId%>">
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
<input type="hidden" id="formTableId" name="formTableId" value="<%=formTableId%>">
<input type="hidden" id="criteriaTableId" name="formTableId" value="<%=criteriaTableId%>">
<input type="hidden" id="membershipId" name="membershipId" value="<%=bws.memberId.intValue()%>">
<input type="hidden" id="neighborhoodId" name="neighborhoodId" value="<%=bws.nhId.intValue()%>">
<input type="hidden" id="neighborhoodName" name="neighborhoodName" value="<%=bws.nhName%>">

<input type="hidden" id="period" name="period" value="<%=periodStr%>">
<input type="hidden" id="asOfTid" name="asOfTid" value="<%=asOfTidStr%>">
<input type="hidden" id="compTid" name="compTid" value="<%=compTidStr%>">
<input type="hidden" id="asOfDate" name="asOfDate" value="<%=asOfDateStr%>">
<input type="hidden" id="compDate" name="compDate" value="<%=compDateStr%>">
<input type="hidden" id="trackState" name="trackState" value="<%=trackStateStr%>">
<input type="hidden" id="showDiffAbsolute" name="showDiffAbsolute" value="<%=showDiffAbsolute%>">
<input type="hidden" id="showDiffPercent" name="showDiffPercent" value="<%=showDiffPercent%>">
<input type="hidden" id="showChangesOnly" name="showChangesOnly" value="<%=showChangesOnlyStr%>">
<input type="hidden" id="showDiffSideBySide" name="showDiffSideBySide" value="<%=showDiffSideBySideStr%>">
<input type="hidden" id="hideUnchangedRows" name="hideUnchangedRows" value="<%=hideUnchangedRows%>">
<input type="hidden" id="hideChangedRows" name="hideChangedRows" value="<%=hideChangedRows%>">
<input type="hidden" id="hideNewRows" name="hideNewRows" value="<%=hideNewRows%>">
<input type="hidden" id="hideDeletedRows" name="hideDeletedRows" value="<%=hideDeletedRows%>">
<!--end table for table-----------------------------------------------------------------------------------------------------
</td>
<td width="5" valign="top" ><img src="images/clear.gif" width="5" height="2">
</td>
-->
</tr>
 </tr>

</table>

<tr >
			<td  colspan="30"  align="left">
			  <b>
<%if(Exceldump != null && Exceldump.equals("false"))
	{%>
		<input name="checkAll" onClick="flipAllCheckBoxes(this)" type="checkbox" width="1%" value="checkAll" >
		<a href="javascript:openInExcel()" title="View the table in Excel"><img src="images/icon-excel.gif" width="16" height="16"></a>
	<%}
		if ( isInDesignMode )
		{
			    //System.out.println("In design mode ***************************" );
			   if ( tbACL.canAdministerColumn() )
			   {%>
					<a href="javascript:commitToBoardwalk('commitCells')" title="Save Default Values"><img src="images/save.gif" width="16" height="16"></a> |
					<a href="javascript:addRow('addRow')" title="Insert New Row"><img src="images/insertrow.gif" width="16" height="16"></a>&nbsp;
					<a href="javascript:deactivateRow('deleteRow')" title="Delete Selected Row(s)"><img src="images/deleterow.gif" width="16" height="16"></a>&nbsp;
					<%
			   }
		}

		else
		{
			 //System.out.println("Not In design mode " );

			if (        (        ViewPreference.equals("LATEST_BY_USER") && tbACL.canReadWriteOnMyLatestView() )
						||    ( ViewPreference.equals("LATEST") && tbACL.canWriteLatestOfTable() )
						||    ( ViewPreference.equals("MY_ROWS") && tbACL.canReadWriteLatestOfMyRows())
						||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH") &&  tbACL.canReadWriteLatestofMyGroup())
						||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_IMM_CHD") &&  tbACL.canReadWriteLatestofMyGroupAndImmediateChildren())
						||    ( ViewPreference.equals("LATEST_ROWS_OF_ALL_USERS_IN_MY_NH_AND_ALL_CHD") &&  tbACL.canReadWriteLatestofMyGroupAndAllChildren()
					)
			)
			{
				if ( tableIsReadOnly == false && Exceldump != null && Exceldump.equals("false"))
				{
			%>

			     <a href="javascript:commitToBoardwalk('commitCells')" title="Save Table"><img src="images/save.gif" width="16" height="16"></a>&nbsp;
			     <a href="javascript:addHyperlink()" title="Insert Hyperlink"><img src="images/hyperlink.gif" width="16" height="16"></a>&nbsp;

			     <a href="javascript:addComment()" title="Insert Comment"><img src="images/comment.gif" width="16" height="16"></a>&nbsp;
			<%
				}
			  }



				   if ( tbACL.canAddRow() && Exceldump != null && Exceldump.equals("false"))
				   {
					 %>
					<a href="javascript:addRow('addRow')" title="Insert New Row"><img src="images/insertrow.gif" width="16" height="16"></a>&nbsp;
				   <a href="javascript:addRowViaFormRequest('addRowViaFormRequest')" title="Insert New Row Using Form"><img src="images/form.jpg" width="20" height="16"></a>&nbsp;
				   <%
					}

					if ( tableIsReadOnly == false && Exceldump != null && Exceldump.equals("false"))
					{
					%>
				   <a href="javascript:editRow()" title="Edit Row"><img src="images/editrow.jpg" width="18" height="18"></a>&nbsp;
					<%
					}


					if ( tbACL.canDeleteRow() && Exceldump != null && Exceldump.equals("false"))
				   {
				  %>
				   <a href="javascript:deactivateRow('deleteRow')" title="Delete Selected Row(s)"><img src="images/deleterow.gif" width="16" height="16"></a>&nbsp;

					<%
					}
	}
%>


			    </b>
			</td>

</tr>



<tr>
<%
System.out.println("PROCESSEDROWS=" + processedRows);
System.out.println("DISPROWS=" + dispRows);

double numBuckets = (double)processedRows / (double)bucketSize;
numBuckets = java.lang.Math.ceil(numBuckets);

if (numBuckets > 1 && Exceldump.equals("false"))
{

%>
<td colspan="30"  >
<%
	if (bucketNum > 0)
	{

%>
<a href="javascript:setBucket(-1)" title="Previous Set">Prev</a>
<%
	}
%>
<%
	for (int i = 0; i < numBuckets; i++)
	{


if ( bucketNum == i )
{
	%><B><U><%
}

%>
<a href="javascript:setBucket(<%=i%>)" ><%=i+1%></a>
<%

if ( bucketNum == i )
{
	%></U></B><%
}



	}
	if (bucketNum < numBuckets - 1)
	{
%>
<a href="javascript:setBucket(-2)" title="Next Set">Next</a>
<%
	}
%>
<br>
<small>Display <input type="text" id="bucketSize" name="bucketSize" value="<%=bucketSize%>" style="height:20px;width:40px"> records</small>
 <a href="javascript:setBucketSize()">Go</a>
</td>
<%
}
%>
</tr>

<tr>


</div>
</form>

<!--end table for main table  ---------------------------------------------------------------------------------------------->
<br>
</td>
</tr>

<%
	if(portlet == false && Exceldump != null && Exceldump.equals("false"))
	{
%>

<%@include file='/jsp/common/footer.jsp' %>

<%
	} // portlet == false
%>



