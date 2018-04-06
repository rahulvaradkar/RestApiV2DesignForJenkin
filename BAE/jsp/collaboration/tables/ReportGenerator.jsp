<%@ page import ="com.boardwalk.table.*,java.util.*,java.sql.*,com.boardwalk.database.*"%>

<%
	String lsTableId = request.getParameter("tableid");

	ArrayList lsaBaseLine = new ArrayList(); //TableManager.getBaseLineForTableId(Integer.parseInt(lsTableId), "","");	
	Vector columnList			= new Vector();
	HttpSession hs = request.getSession(true);
	int userId = ((Integer)hs.getAttribute("userId")).intValue();
	int	memberid = ((Integer)hs.getAttribute("memberId")).intValue();

	Connection	connectionJsp = null;

	try
	{
		DatabaseLoader databaseloader = new DatabaseLoader(new Properties());
		connectionJsp = databaseloader.getConnection();
		TableColumnInfo tci = ColumnManager.getTableColumnInfo(connectionJsp,Integer.parseInt(lsTableId),-1,userId,memberid,-1,"");
		columnList = tci.getColumnVector();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
%>

<html>
<head>
<title>Select Activity Period</title>
<META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="javascript" src="../../../jscript/datetimepicker.js"></script>
<script language="javascript" src="../../../jscript/dateutil.js"></script>
<script language="javascript">
	var startDate;
	var endDate;

	function initPage()
	{
		var sel = eval("document.all.trackingPeriod");
		sel.selectedIndex = 1
		setStartEndDates (sel);
		chagePeriod(sel)
	}

	function chagePeriod(select)
	{
		setStartEndDates(select);
		var trackingPeriod = select.options[select.selectedIndex].value;

		if( trackingPeriod != "Custom" )
		{
			LoadBaseLineList();
		}
	}

	function setStartEndDates(select)
	{
		var trackingPeriod = select.options[select.selectedIndex].value;
		if ( trackingPeriod == "Week" )
		{
			endDate = new Date();
			startDate = new Date();
			startDate.setDate(endDate.getDate() - 7);
			eval("document.all.endDate").value  = formatDateTime(endDate, "dd-NNN-yyyy hh:mm:ss a");
			eval("document.all.startDate").value  = formatDateTime(startDate, "dd-NNN-yyyy hh:mm:ss a");
			eval("document.all.endDate").disabled  = true
			eval("document.all.startDate").disabled  = true
			eval("document.all.calStart").disabled  = true
			eval("document.all.calEnd").disabled  = true
		}
		else if ( trackingPeriod == "Month" )
		{
			endDate = new Date();
			startDate = new Date();
			startDate.setDate(endDate.getDate() - 30);
			eval("document.all.endDate").value  = formatDateTime(endDate, "dd-NNN-yyyy hh:mm:ss a");
			eval("document.all.startDate").value  = formatDateTime(startDate, "dd-NNN-yyyy hh:mm:ss a");
			eval("document.all.endDate").disabled  = true
			eval("document.all.startDate").disabled  = true
			eval("document.all.calStart").disabled  = true
			eval("document.all.calEnd").disabled  = true
		}
		else if ( trackingPeriod == "Quarter" )
		{
			endDate = new Date();
			startDate = new Date();
			startDate.setDate(endDate.getDate() - 90);
			eval("document.all.endDate").value  = formatDateTime(endDate, "dd-NNN-yyyy hh:mm:ss a");
			eval("document.all.startDate").value  = formatDateTime(startDate, "dd-NNN-yyyy hh:mm:ss a");
			eval("document.all.endDate").disabled  = true
			eval("document.all.startDate").disabled  = true
			eval("document.all.calStart").disabled  = true
			eval("document.all.calEnd").disabled  = true
		}
		else if ( trackingPeriod == "Year" )
		{
			endDate = new Date();
			startDate = new Date();
			startDate.setDate(endDate.getDate() - 365);
			eval("document.all.endDate").value  = formatDateTime(endDate, "dd-NNN-yyyy hh:mm:ss a");
			eval("document.all.startDate").value  = formatDateTime(startDate, "dd-NNN-yyyy hh:mm:ss a");
			eval("document.all.endDate").disabled  = true
			eval("document.all.startDate").disabled  = true
			eval("document.all.calStart").disabled  = true
			eval("document.all.calEnd").disabled  = true
		}
		else if ( trackingPeriod == "Custom" )
		{
			eval("document.all.endDate").disabled  = true
			eval("document.all.startDate").disabled  = true
			eval("document.all.calStart").disabled  = false
			eval("document.all.calEnd").disabled  = false
		}
	}

	function stateChanged() 
	{ 
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
		{ 
			callBackBaseLine(xmlHttp.responseText);
		} 
	} 

	function Notify()
	{
		setTimeout("LoadBaseLineList();",100);
	}

	//////////// Fucntion to request a list of baselines from backend ///////////////////
	function LoadBaseLineList()
	{
		var tableId = '<%=lsTableId%>';
		var startDate = eval("document.all.startDate").value;
		var endDate = eval("document.all.endDate").value;
		var url = "getBaseLineList.jsp?tableid="+tableId+"&startdate="+startDate+"&enddate="+endDate;
		//parent.calcFrame.location.href = url;
		//Use AJAX
		xmlHttp = GetXmlHttpObject(stateChanged); 
		xmlHttp.open("GET", url , true) ;
		xmlHttp.send(null) ;
	}

	function callBackBaseLine(asResponse)
	{
		var str			= trim(asResponse);
		str				= str.substring(0,str.length-1);
		var records		= str.split("|");
		var liSelSize	= records.length;

		document.Form.baseline.size = liSelSize;
		document.Form.baseline.options.length = 0;

		for(i=0;i<records.length;i++)
		{ 
			t=records[i].split("^");
			id		= trim(t[0]);
			name	= trim(t[1]);
			document.Form.baseline.options[i] = new Option(name, id); 
		}
		document.Form.baseline.options.selectedIndex = 0;
		document.Form.baseline.style.height="100px";
		document.Form.baseline.style.width="120px";
	}

	function trim(str) 
	{ 
		str = str.replace(/^\s*/, '').replace(/\s*$/, ''); 
		return str;
	} 

	function GetXmlHttpObject(handler)
	{ 
		//alert('GetXmlHttpObject');
		var objXmlHttp=null;

		if (navigator.userAgent.indexOf("Opera")>=0)
		{
			alert("This example doesn't work in Opera") ;
			return ;
		}
		if (navigator.userAgent.indexOf("MSIE")>=0)
		{ 
			var strName="Msxml2.XMLHTTP";
			if (navigator.appVersion.indexOf("MSIE 5.5")>=0)
			{
				strName="Microsoft.XMLHTTP";
			} 
			try
			{ 
				objXmlHttp=new ActiveXObject(strName);
				objXmlHttp.onreadystatechange=handler ;
				return objXmlHttp;
			} 
			catch(e)
			{ 
				alert("Error. Scripting for ActiveX might be disabled") 
				return 
			} 
		} 
		if (navigator.userAgent.indexOf("Mozilla")>=0)
		{
			objXmlHttp=new XMLHttpRequest();
			objXmlHttp.onload=handler;
			objXmlHttp.onerror=handler ;
			return objXmlHttp;
		}
	} 

	function dlgOK()
	{
		var q = new Object();
		q.period = eval("document.all.trackingPeriod").value;
		q.baseline = "-1";
		if (q.period != "Custom")
		{
			q.startDate = startDate.getTime();
			q.endDate = endDate.getTime();			
		}
		else
		{
			var startDateStr = eval("document.all.startDate").value;
			var endDateStr = eval("document.all.endDate").value;
			
			var startTime;
			var endTime;
			startTime = getDateFromFormat(startDateStr, "d-NNN-yyyy hh:mm:ss a");
			endTime = getDateFromFormat(endDateStr, "d-NNN-yyyy hh:mm:ss a");

			if (startTime == 0 || endTime == 0)
			{
				alert ("The dates you have entered are not in the correct format")
				return
			}

			if (startTime >= endTime)
			{
				alert ("The start date is greater than or same as the end date")
				return
			}
			
			q.startDate = startTime;
			q.endDate = endTime;
		}
		if(eval("document.all.withBaseline").checked == true)
		{			
			q.baseline = getSelectedBaseline(document.all.baseline);
			if(q.baseline == "")
			{
				q.baseline = "-1";
			}
		}
		else
		{
			q.baseline = "-1";
		}
		q.columnids = getSelectedColumn(document.all.columns)
		

		window.returnValue = q;

		window.close();
	}

	function getSelectedColumn(opt) 
	{
		selected = "";
		for (var intLoop=0; intLoop < opt.length; intLoop++) 
		{
			if (opt[intLoop].selected) 
			{
				if(opt[intLoop].value == "ALL")
				{
					selected = "";
					return selected;
				}
				if(selected != "")
					selected += ",";
				selected += opt[intLoop].value;
			}
        }
		
      return selected;
   }

   function getSelectedBaseline(opt) 
	{
		selected = "";
		for (var intLoop=0; intLoop < opt.length; intLoop++) 
		{
			if (opt[intLoop].selected) 
			{
				if(opt[intLoop].value == "ALL")
				{
					selected = "ALL";
					return selected;
				}
				if(selected != "")
					selected += ",";
				selected += opt[intLoop].value;
			}
        }
		return selected;
   }


</script>

<link rel="stylesheet" type="text/css" href="../../../css/stylesheet.css">

</head>
<body onload="javascript:initPage()">
<form action="ReportGenerator.jsp" method="post" name="Form"  >
<input type="hidden" id="tableId" name="tableid" value="<%=lsTableId%>">

<table border=0>
	<tr>
	<td nowrap >Activity Period</td>
	<td colspan=3>
		<select   onchange="javascript:chagePeriod(this)" id="trackingPeriod" >
		<option value="Week">Past Week</option>
		<option value="Month">Past Month</option>
		<option value="Quarter">Past Quarter</option>
		<option value="Year">Past Year</option>
		<option  value="Custom"  >Custom</option>
		</select>
	</td>
	</tr>


	  <tr>
	    <td>
	      Start Date 
	    </td>
	  	<td colspan=3>
	  		<input type="Text" id="startDate" maxlength="25" size="30">
	  		<img id="calStart" disabled="true" onclick="javascript:NewCal('startDate','ddmmmyyyy',true,12)" src="../../../images/cal.gif">
	  	</td>
	  </tr>


	  <tr>
	    <td>
	      End Date 
	    </td>
	  	<td colspan=3>
	  		<input type="Text" id="endDate" maxlength="25" size="30">
	  		<img id="calEnd" disabled="true" onclick="javascript:NewCal('endDate','ddmmmyyyy',true,12)" src="../../../images/cal.gif">
	  	</td>
	  </tr>
	 <tr>
	   <td valign=top>
	      Columns 
	    </td>
	  	<td colspan=3>
	  		<select id="columns" MULTIPLE  size="10" STYLE="width:200px">
				<option  value="ALL"  SELECTED>ALL</option>
				<%
					for(int i = 0; i < columnList.size() ; i++)
					{
						Column bc = (Column)columnList.get(i);
				%>
						<option  value="<%=bc.getId()%>"  ><%=bc.getColumnName()%></option>
				<%
					}	
				%>
			</select>
	  	</td>
	  </tr>
		<tr>
			<td nowrap width="2%">
			  With BaseLine 
			</td>
			<td width="1%">
				<input name="withBaseline" type="checkbox" width="1%" checked>
			</td>
			<td width="2%">
				Select :
			</td>
	  		<td>
				<select name="baseline" id="baseline" MULTIPLE  size="5"></select>
			</td>
		</tr>

	<tr>
		<td colspan=4>
		<center>
<br>
			<input type='button' id="okBtn" onclick="dlgOK()" value=" OK ">
			<input type='button' id="cancelBtn" onclick="window.close()" value="Cancel ">
		</center>
		</td>
	</tr>
	<input type="hidden" value="" name="hdnrefresh">
</table>
</form>
</body>
</html>

<%  
	try
	{
		System.out.println(" in Try JSP block");
		connectionJsp.close();
		connectionJsp = null;
	}
	catch ( SQLException sql )
	{
		sql.printStackTrace();
	}
%>

