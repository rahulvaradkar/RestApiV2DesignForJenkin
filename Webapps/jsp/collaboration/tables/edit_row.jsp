
<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer,java.util.regex.*, com.boardwalk.database.Transaction, com.boardwalk.table.*,java.io.*, java.util.*,com.boardwalk.query.*"%>



<%
int tableId = ((Integer)request.getAttribute("TableId")).intValue();
String tableName = (String)request.getAttribute("TableName");
String ViewPreference = (String)request.getAttribute("ViewPreference");

String rowId = (String)request.getAttribute("rowId");
TableColumnInfo tbcInfo = (TableColumnInfo) request.getAttribute("TableColumnInfo");
%>



<script language="javascript">
var selectedcell = null;

function commitNewRow()
{
	// get the field values and update the hidden fields and submit

	document.forms[0].action.value = 'commitNewRow'
    	document.forms[0].submit()
}

function saveCellData(div)
{
	var cellId = "Cell"+div.name
	document.all.item(cellId).value = div.innerText
}


function saveLookupValue( lookupCellList )
{
	var cellId = "Cell"+lookupCellList.name
	document.all.item(cellId).value = lookupCellList.options[lookupCellList.selectedIndex].value
}

function selectCell(divTag)
{
	selectedcell = divTag;
}

  function datatypeCheck (cellvalue, celltype)
  {
  	if (celltype == "INTEGER")
  	{
  		if (isNaN (cellvalue) || parseInt (cellvalue,10) != cellvalue)
  		{
  			return false
  		}
  	}
  	else if (celltype == "FLOAT")
  	{
  		if (isNaN (cellvalue) || parseFloat(cellvalue) != cellvalue )
  		{
  			return false
  		}
  	}
  	return true

}

function trim(strText) {
    // this will get rid of leading spaces
    while (strText.substring(0,1) == ' ')
        strText = strText.substring(1, strText.length);

    // this will get rid of trailing spaces
    while (strText.substring(strText.length-1,strText.length) == ' ')
        strText = strText.substring(0, strText.length-1);

   return strText;
}

function  addHyperlink( )
{
	editHyperlink(selectedcell )
}


function editHyperlink(selectedCell )
{

	if ( selectedCell != null )
	{
		var CellTypeId  = "Type" + selectedCell.name

		if ( selectedCell.columnType == "STRING" )
		{
			var urlName=""
			var url=""
			var screentip=""

			if (selectedCell.children(0).children.length == 1 )
			{
					if (selectedCell.children(0).children(0).tagName == "A")
					{

						urlName = selectedCell.innerText
						url = selectedCell.children(0).children(0).href
						url = encodeURIComponent(url)
						screentip = selectedCell.children(0).children(0).title

					}
					else
					{
						urlName = selectedCell.innerText
					}
			}
			else
			{
					urlName = selectedCell.innerText
			}

			var urlObj = new Object();
			urlObj.urlName = urlName;
			urlObj.url = url;
			urlObj.screentip = screentip;

			var rUrlObj = showModalDialog("jsp/collaboration/tables/addHyperlinkDlg.html", urlObj, "dialogHeight:20;dialogWidth:25")

			if (rUrlObj == null )
			{
				return;
			}

			urlName = rUrlObj.urlName
			url = rUrlObj.url
			screentip = rUrlObj.screentip



			if (  url == null || url == "")
			{
				selectedCell.innerText = ""
				return
			}


			if ( urlName == null)
			{
				urlName = url
			}

			if ( screentip == null )
				screentip = urlName;

			// make sure it is a well formed url with protocol
			var urlRegPattern = /(\w+):\/\/(\S*)/;
			var result = url.match(urlRegPattern);

			if (result == null ) // no protocol defined
			{
				// see if this is a mailto url
				var urlRegPattern = /mailto:(\w+)@(\S*)/;
				result = url.match(urlRegPattern);
				if (result == null) // not a mailto url, see if it is a file
				{
					var urlRegPattern = /(\w+)@(\S*)/;
					result = url.match(urlRegPattern);
					if (result != null)
					{
						url = "mailto:"+url;
					}
					else
					{
						url = "http://"+url;
					}
				}
			}


			var ATag  = '<a  href=\"'+url +'\" title='+'\"'+screentip+'\"  target=javascript:openWindow()>'+urlName+'</a>'

			selectedCell.children(0).innerHTML = ATag
			//var openloc = 'jsp/collaboration/tables/add_hyperlink.jsp?'+'urlName='+urlName+'&url='+url+'&screentip='+screentip+'&bwcellId='+selectedCell.name+'&htmlid='+selectedCell.id
			//var  TableForCell = window.open(openloc,null,'toolbar=yes,location=yes,scrollbar=yes,resizable=yes,menubar=no,height=300,width=450')

		}
		else
		{
			alert( "You can create a hyperlink only for a STRING type" )
		}


	}
	else
	{
		alert( "Please select a cell before adding a hyperlink" )

	}


}

 function addComment()
  {
  	var existingComment = eval("document.all.tableComment").value;
  	var newComment = showModalDialog("jsp/collaboration/tables/addCommentDlg.html", existingComment, "dialogHeight:12;dialogWidth:27")
  	if (newComment == null)
  		return
  	else
  		eval("document.all.tableComment").value = newComment;

  }




</script>

<LINK REL=STYLESHEET TYPE="text/css" HREF="../css/stylesheet.css" TITLE="stylesheet">
<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>




</td>
</tr>
</table>

 <form name="editrow" method="get" action="MyTables">
<hr color="#fddeb9">
 <small>
 Table: <b><%=tableName%></b><br>

 Please update the fields and save the row
 </small>
 <br>
<br>

&nbsp;&nbsp;<a href="javascript:commitNewRow()" title="Save New Row"><img src="images/save.gif" width="16" height="16"></a>
<a href="javascript:addHyperlink()" title="Insert Hyperlink"><img src="images/hyperlink.gif" width="16" height="16"></a>
 <a href="javascript:addComment()" title="Insert Comment"><img src="images/comment.gif" width="16" height="16"></a>&nbsp;
<table >
<tr>
<td>
 <table name="editForm"  id="editForm" class="body" border="1" bordercolor="#eeeeee"  cellpadding="0" align="left" valign="top">
 <tbody name="editFormBody"  id="editFormBody"  >

<tr>
	<td class="BWTableHead" > Field </td>
	<td class="BWTableHead" > Value </td>
</tr>
<%
	for ( int c = 0; c < tbcInfo.getColumnVector().size(); c++ )
	{
		Column col = (Column)tbcInfo.getColumnVector().elementAt(c);
%>
	<tr>

<%
	if ( col.canWrite() == true  )
	{
%>
			<td> <%=col.getColumnName()%></td><td>
<%
	}
	else
	{
%>
			<td bgcolor="lightblue"> <%=col.getColumnName()%></td><td bgcolor="lightblue">

<%
	}
			if ( col.getIsEnumerated() == true && col.canWrite() == true )
				{
%>
					<select     name="<%=col.getId()%>" class="LookupList"  onChange="saveLookupValue(this)" >
<%
							Vector enumeratedValues = col.getEnumerations();
							for ( int index = 0; index < enumeratedValues.size(); index++ )
							{
								String optionvalue = (enumeratedValues.elementAt(index)).toString();

								if ( optionvalue.trim().indexOf("<a") == 0 ||   optionvalue.trim().indexOf("<A") ==0 )
								{
										// optionvalue is a http link we need to parse it
										int indexendofATag = optionvalue.trim().indexOf(">") ;
										int indexstartofSlashATag = optionvalue.trim().indexOf("<", indexendofATag) ;
										optionvalue = optionvalue.trim().substring( indexendofATag+1, indexstartofSlashATag ).trim();
								}
%>
								 <option value="<%=optionvalue%>" > <%=optionvalue%> </option>
<%
								}

%>
					</select>
<%
				}
				else
				{
					if ( col.canWrite() == true )
					{
	%>
						<div  name="<%=col.getId()%>"  columnType=<%=col.getType()%> onclick="selectCell(this)"   onblur="saveCellData(this)"  contentEditable="true"  >
								<%=col.getDefaultValueAsString()%>
						</div>
	<%
					}
					else
					{
	%>
						<div  name="<%=col.getId()%>"  columnType=<%=col.getType()%> onclick="selectCell(this)"   onblur="saveCellData(this)"  bgcolor="lightblue" contentEditable="false"  >
													<%=col.getDefaultValueAsString()%>
						</div>
    <%
					}


				}
%>

				<input type="hidden" name="Cell<%=col.getId()%>" value=" " />
				<input type="hidden" name="Type<%=col.getId()%>" value="<%=col.getType()%>" />
				<input type="hidden" name="ColumnName<%=col.getId()%>" value="<%=col.getColumnName()%>" />


		</td>
	</tr>
<%
	}
%>



 </tbody>
  </table>
</td>
</tr>
<tr>
<td>

<input type="hidden" name="tableComment" value="New row added" />
<input type="hidden" name="rowId" value="<%=rowId%>" />
<input type="hidden" name="tableId" value="<%=tableId%>" />
<input type="hidden" name="tableName" value="<%=tableName%>" />
<input type="hidden" name="ViewPreference" value="<%=ViewPreference%>" />
<input type="hidden" id="action" name="action" value="commitNewRow">


<br>
&nbsp;&nbsp;<a href="javascript:commitNewRow()" title="Save New Row"><img src="images/save.gif" width="16" height="16"></a>
<a href="javascript:addHyperlink()" title="Insert Hyperlink"><img src="images/hyperlink.gif" width="16" height="16"></a>
<a href="javascript:addComment()" title="Insert Comment"><img src="images/comment.gif" width="16" height="16"></a>&nbsp;
</td>
</tr>
</table>
</form>
<SCRIPT src="/jsp/common/footer.jsp"> </SCRIPT>

</body>
</html>