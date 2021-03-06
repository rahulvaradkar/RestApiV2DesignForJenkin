
<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer,java.util.regex.*, com.boardwalk.database.Transaction, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.query.*"%>

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

FormDefinition fd = (FormDefinition) request.getAttribute("FormDefinition");
int formTableId = -1;
Integer formTableIdInt = (Integer)request.getAttribute("formTableId");
if (formTableIdInt != null)
{
   formTableId = formTableIdInt.intValue();
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


		System.out.println("NumLevels = " + numLevels);
		System.out.println("horizGrid = " + horizGrid);

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

System.out.println("edit_table.jsp:tableIsReadOnly=" + tableIsReadOnly + " isTableLocked = " +isTableLocked );
%>
<link rel="stylesheet" type="text/css" href="css/menu.css">
<script language="javascript">
	<%@ include file="/jscript/menu.js" %>
	menuArrow = new Image(10, 12);
	menuArrow.src = "images/arrow.gif";
</script>

<script language="javascript">
<%@ include file="/jscript/editTable.js" %>
<%@ include file="/jscript/sortTable.js" %>
<%@ include file="/jscript/validate.js" %>

</script>

</td>
</tr>
</table>


<%
if (portlet == false)
{
%>


<a href="MyCollaborations"><img src="images/logo-boardwalk.gif" /></a>
<a class="largetext" href="MyCollaborations?action=editCollab&collabId=<%=tbi.getCollaborationId()%>"> <%=tbi.getCollaborationName()%> :</a>
<a class="largetext" href="Whiteboard?wbid=<%=tbi.getWhiteboardId()%>&action=edit&collabId=<%=tbi.getCollaborationId()%>"><%=tbi.getWhiteboardName()%>: </a>
<b><a class="largetext"  href="MyTables?tableId=<%=tbi.getTableId()%>&action=editTable&ViewPreference=<%=tbi.getTableDefaultViewPreference()%>">
    <%=tbi.getTableName()%>
   </a>
   <br>
</b> <%=tbi.getTablePurpose()%>


<%
	if ( isTableLocked )
	{
%>
	<image src="images/lock.gif"><small>Locked by user <%=tbi.getLockedByuser()%>  since <%=tbi.getLockTime()%><small>
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
		System.out.println("Processing level : " + (iL + 1));
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
			System.out.println(" sections = " + sections);
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
				System.out.println("fieldsByLevel = " + section.getFields());
				Hashtable fieldsByLevel = section.getFields();
				System.out.println("fieldsByLevel = " + fieldsByLevel);
				int maxFieldsAtLevel = section.getMaxFieldsAtLevel();
				System.out.println("maxFieldsAtLevel = " + maxFieldsAtLevel);

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
					System.out.println("OPening tr tag");
%>

		<tr>


<%
					System.out.println("Processing field level : " + (iLF+1));
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
						System.out.println("Number of fields at this level = " + fields.size());
					}
					for (int iHF = 0; iHF < maxFieldsAtLevel; iHF++)
					{
						System.out.println("Processing field number " + iHF);
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
							System.out.println("Field Name = " + field.getName());
							System.out.println("Field label = " + field.getLabel());

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
									System.out.println("MinLength not valid in form definition");
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
							System.out.println("Opening non blank td tag");

%>
					<td class="InputBoxSmallReadonly"><%=field.getLabel()%></td>
<%
			Column fCol = (Column)columns.get(new Integer(cid));
			if (! fCol.getIsEnumerated())
			{

%>
					<td ><input class="InputBoxSmall" type="text"   name="frc<%=cid%>" id="frc<%=cid%>"></td>
<%
				System.out.println("not a lookup");
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
			<a style="display:inline;" onClick='this.href="MyTables?action=editTable&tableId=<%=fCol.getLookupTableId()%>&ViewPreference=LATEST&cc<%=fCol.getLookupColumnId()%>=equals:"+frc<%=cid%>.value'    >
			 <img  style="display:inline;" height="12" width="12" src="images/refersto.gif"   onmouseover ="this.title='Show Lookup Table'" >
		 	</a>
<%
			System.out.println("lookup");
			} // lookup

			} // if there is field
			else
			{
			System.out.println("Opening blank td");
%>
		<td>&nbsp;
<%
			} // blank field
%>
</td>

<%
			System.out.println("starting with new field at this level");
				} // iHF
			System.out.println("Done with level = " );
%>
</tr>
<%
			System.out.println("Starting a new level");
			} // iLF
			System.out.println("Done with all levels" );
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
}// not in design mode
else
{
%>
<!-- Save Default Values (design mode) ------------------------------------------------------------------------->

                        <div class="box"><a href="javascript:commitToBoardwalk('commitCells')">Save Default Values</a></div>
			<div class="box"><a href="javascript:getTableView('<%=tbACL.getSuggestedViewPreferenceBasedOnAccess()%>')">Reopen Table</a></div>
<%
}// in design mode
%>
<!-- End Save Default Values (design mode) ------------------------------------------------------------------------->

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

                        <div class="box"><a href="javascript:trackChanges()">Track Changes</a></div>
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


<table style="table-layout=auto;" width="100%" cellspacing="0" cellpadding="2" border="1" bordercolor="#666666">
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
<col id="SystemColumns" span="4" STYLE="display:none">
</colgroup>
<thead>
<tr>
			<td  colspan="30"  align="left">
			  <b>

				<input name="checkAll" onClick="flipAllCheckBoxes(this)" type="checkbox" width="1%" value="checkAll" >
				<%
				if ( isInDesignMode )
				{
					    System.out.println("In design mode ***************************" );
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

				else
				{
					 System.out.println("Not In design mode " );

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

						}
					 	if ( tbACL.canDeleteRow() )
					   {
					  %>
					   <a href="javascript:deactivateRow('deleteRow')" title="Delete Selected Row(s)"><img src="images/deleterow.gif" width="16" height="16"></a>&nbsp;

						<%
						}

					}
		%>


    </b>

   <img id="sysColShowImg" valign="bottom" onclick="showSystemColumns()" src="images/expand.gif" title="Show System Columns">
   </td>

  </tr>

</div>
<%
if ( rowids.size() > 0 )
{
%>
<img id="tableUpdateStatusImg" src="images/collapse.gif" onClick="expandTableUpdateStatus()" title="Hide Table Update Status">
<div>

<div  align="top" id="tableUpdateStatus">
Last updated by <u><b id="tableUpdatedBy"></b></u>&nbsp;  on &nbsp;<b id="tableUpdatedOn"> </b>
<br>
<div id="tableUpdateComment"></div>
</div>
<%
}
%>
<tr name="bwHeadings" id="bwHeadings">
<th width="1%">

</th>
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


				   <th name="User" class="BWTableHeadUserDetails"
							ondblclick="this.blur(); return sortTable('bwTableContents',  <%=visibleColumnIndex++ %>, true);" > Updated By
				    </th>

				    <th name="Last Update" class="BWTableHeadUserDetails"
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
	<td>
   			<input name="rowId" onClick="highlightRow(this)" type="checkbox" width="1%" value="<%=rowId%>">
   	</td>
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

	if ( ! cell.getType().equals("TABLE")  && ! ((Column)columns.get(  new Integer(cell.getColumnId()))).getIsEnumerated()   )
	{
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
	//System.out.println(" for rowid " + rowId + " cellid = " +cell.getId()  + " string for display  " + cellValueForDisplay);
	//System.out.println("cell.getColumnId() " + cell.getColumnId() );
	//System.out.println("deltaColumnId.intValue() " + deltaColumnId.intValue());
	String color = "black";

	//System.out.println("uprow" + upRow + " downrow " + downRow );

	if  ( cell.getColumnId() == deltaColumnId.intValue() )
	{
		System.out.println("cell is in delta column" );
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

		if(tableIsReadOnly == false && canWrite == true)
		   {
		   %>

				<DIV onmouseover="checkForURL(this)" contentEditable="true" onfocus="changeCell(this.parentElement)"  STYLE="height: 100%; width: 100%;">
					 <%=cell.getValueAsString()%>
				</DIV>

			<%
		}
		else
		{
					%><%=cell.getValueAsString()%><%
		}
	}
 }
 else
 {				   // ondblclick="javascript:this.contentEditable=true; "
	if(tableIsReadOnly == false && canWrite == true)
	{
	   	%>

		<DIV onmouseover="checkForURL(this)" contentEditable="true" onfocus="changeCell(this.parentElement)"  STYLE="height: 100%; width: 80%;">
			 <%=cell.getValueAsString()%>
		</DIV>

		<%
	}
	else
	{
		%>
		<div bgcolor="black"><%=cell.getValueAsString()%></div>
		<%
	}
}


  %>
</td>
  <input type="hidden" name="pCell<%=cell.getId()%>" value='<%=cell.getValueAsString()%>'>
  <input type="hidden" name="CellType<%=cell.getId()%>" value='<%=cell.getType()%>'>
  <input type="hidden" name="Cell<%=cell.getId()%>" value='<%=cell.getValueAsString()%>'>
  <input type="hidden" name='dirtyCell<%=cell.getId()%>' value=false>




<%
	}
	else if ( !cell.getType().equals("TABLE")  &&    ( (Column)columns.get(new Integer(cell.getColumnId()))).getIsEnumerated()   )
	{

%>


 <td  title="<%=titleString%>"  ID="cell_<%=cell.getId()%>" name="<%=cell.getId()%>"
      columnId="<%=cell.getColumnId()%>" rowId="<%=cell.getRowId()%>">
 <%
	if ( showTableChanges  || showTableColumnDelta )
	{

 	 	oldCellValue = (VersionedCell)tableConf.getCellsByCellId().get(  new Integer ( cell.getId() )    );

 	 	if  ( oldCellValue != null )
 	 	{
  %>
 			<img  style="display:inline;"  src="images/track.gif"  height=12 width=12 >
 <%
 		}
   }

 Column cellColumn = (Column)columns.get(new Integer(cell.getColumnId()));
 if(tableIsReadOnly == false) {

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
<%=cell.getValueAsString() %>
<%
} // table is read only

%>
 <a style="display:inline;" href="MyTables?action=editTable&tableId=<%=cellColumn.getLookupTableId()%>&ViewPreference=LATEST&cc<%=cellColumn.getLookupColumnId()%>=equals:<%=cell.getValueAsString()%>"    >
 <img  style="display:inline;" height="12" width="12" src="images/refersto.gif"   onmouseover ="this.title='Show Lookup Table'" >
 </a>

<%
if (tablesUsingLkpForColumn != null)
	{
		Vector vtulc = (Vector)tablesUsingLkpForColumn.get(new Integer(cell.getColumnId()));
		if (vtulc != null)
		{
			System.out.println("Column with id : " + cell.getColumnId() + " is being used as lookup column");


  %>
	   <a style="display:inline;" href="MyTables?action=showLkpReferences&tableId=<%=tableId%>&lkpColumnName=<%=cell.getColumnName()%>">
	   	   	   <img  style="display:inline;" height="12" width="12" src="images/referedby.gif"   onmouseover ="this.title='Show Tables referring this column for lookup'" >
	   </a>
  <%
		}
	}



  %>

 </td>
 <input type="hidden" name="pCell<%=cell.getId()%>" value="<%=cell.getValueAsString()%>">
 <input type="hidden" name="CellType<%=cell.getId()%>" value="<%=cell.getType()%>">
 <input type="hidden" name="Cell<%=cell.getId()%>" value="<%=cell.getValueAsString()%>">
 <input type="hidden" name="dirtyCell<%=cell.getId()%>" value=false>

<%
}


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

<%
	if(maxTableTransaction != null)
	{
%>
<script>
	tableUpdatedBy.innerText = trim("<%=maxTableTransaction.getCreatedByUserAddress()%>");
	tableUpdatedOn.innerText = trim("<%=maxTableTransaction.getCreatedOn()%>");
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

<%
	}
%>

<tr></tr>
<tr></tr>
<tr></tr>


<tr >
			<td  colspan="30"  align="left">
			  <b>

<input name="checkAll" onClick="flipAllCheckBoxes(this)" type="checkbox" width="1%" value="checkAll" >
<%
		if ( isInDesignMode )
		{
			    System.out.println("In design mode ***************************" );
			   if ( tbACL.canAdministerColumn() )
			   {

		%>
			    <a href="javascript:commitToBoardwalk('commitCells')" title="Save Default Values"><img src="images/save.gif" width="16" height="16"></a> |
			   <a href="javascript:addRow('addRow')" title="Insert New Row"><img src="images/insertrow.gif" width="16" height="16"></a>&nbsp;
			   <a href="javascript:deactivateRow('deleteRow')" title="Delete Selected Row(s)"><img src="images/deleterow.gif" width="16" height="16"></a>&nbsp;
			    <%

			   }
		}

		else
		{
			 System.out.println("Not In design mode " );

			if (        (        ViewPreference.equals("LATEST_BY_USER") && tbACL.canReadWriteOnMyLatestView() )
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

					}
					if ( tbACL.canDeleteRow() )
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
</tr>
<tr>
</tr>


<tr>
<td colspan="30"  >
To select multiple rows: Select starting row, with Ctrl Key down select last row
</td>
</tr>
<tr>
<%
System.out.println("PROCESSEDROWS=" + processedRows);
System.out.println("DISPROWS=" + dispRows);
double numBuckets = (double)processedRows / (double)dispRows;
numBuckets = java.lang.Math.ceil(numBuckets);
if (numBuckets > 1)
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
%>
<a href="javascript:setBucket(<%=i%>)" ><%=i%></a>
<%
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
<input type="hidden" id="formTableId" name="formTableId" value="<%=formTableId%>">

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



