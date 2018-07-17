//<!--
// Ultimate client-side JavaScript client sniff. Version 3.03
// (C) Netscape Communications 1999-2001.  Permission granted to reuse and distribute.
// Revised 17 May 99 to add is_nav5up and is_ie5up (see below).
// Revised 20 Dec 00 to add is_gecko and change is_nav5up to is_nav6up
//                      also added support for IE5.5 Opera4&5 HotJava3 AOLTV
// Revised 22 Feb 01 to correct Javascript Detection for IE 5.x, Opera 4, 
//                      correct Opera 5 detection
//                      add support for winME and win2k
//                      synch with browser-type-oo.js
// Revised 26 Mar 01 to correct Opera detection
// Revised 02 Oct 01 to add IE6 detection

// Everything you always wanted to know about your JavaScript client
// but were afraid to ask. Creates "is_" variables indicating:
// (1) browser vendor:
//     is_nav, is_ie, is_opera, is_hotjava, is_webtv, is_TVNavigator, is_AOLTV
// (2) browser version number:
//     is_major (integer indicating major version number: 2, 3, 4 ...)
//     is_minor (float   indicating full  version number: 2.02, 3.01, 4.04 ...)
// (3) browser vendor AND major version number
//     is_nav2, is_nav3, is_nav4, is_nav4up, is_nav6, is_nav6up, is_gecko, is_ie3,
//     is_ie4, is_ie4up, is_ie5, is_ie5up, is_ie5_5, is_ie5_5up, is_ie6, is_ie6up, is_hotjava3, is_hotjava3up,
//     is_opera2, is_opera3, is_opera4, is_opera5, is_opera5up
// (4) JavaScript version number:
//     is_js (float indicating full JavaScript version number: 1, 1.1, 1.2 ...)
// (5) OS platform and version:
//     is_win, is_win16, is_win32, is_win31, is_win95, is_winnt, is_win98, is_winme, is_win2k
//     is_os2
//     is_mac, is_mac68k, is_macppc
//     is_unix
//     is_sun, is_sun4, is_sun5, is_suni86
//     is_irix, is_irix5, is_irix6
//     is_hpux, is_hpux9, is_hpux10
//     is_aix, is_aix1, is_aix2, is_aix3, is_aix4
//     is_linux, is_sco, is_unixware, is_mpras, is_reliant
//     is_dec, is_sinix, is_freebsd, is_bsd
//     is_vms
//
// See http://www.it97.de/JavaScript/JS_tutorial/bstat/navobj.html and
// http://www.it97.de/JavaScript/JS_tutorial/bstat/Browseraol.html
// for detailed lists of userAgent strings.
//
// Note: you don't want your Nav4 or IE4 code to "turn off" or
// stop working when new versions of browsers are released, so
// in conditional code forks, use is_ie5up ("IE 5.0 or greater") 
// is_opera5up ("Opera 5.0 or greater") instead of is_ie5 or is_opera5
// to check version in code which you want to work on future
// versions.

    // convert all characters to lowercase to simplify testing
    var agt=navigator.userAgent.toLowerCase();

    // *** BROWSER VERSION ***
    // Note: On IE5, these return 4, so use is_ie5up to detect IE5.
    var is_major = parseInt(navigator.appVersion);
    var is_minor = parseFloat(navigator.appVersion);

    // Note: Opera and WebTV spoof Navigator.  We do strict client detection.
    // If you want to allow spoofing, take out the tests for opera and webtv.
    var is_nav  = ((agt.indexOf('mozilla')!=-1) && (agt.indexOf('spoofer')==-1)
                && (agt.indexOf('compatible') == -1) && (agt.indexOf('opera')==-1)
                && (agt.indexOf('webtv')==-1) && (agt.indexOf('hotjava')==-1));
    var is_nav2 = (is_nav && (is_major == 2));
    var is_nav3 = (is_nav && (is_major == 3));
    var is_nav4 = (is_nav && (is_major == 4));
    var is_nav4up = (is_nav && (is_major >= 4));
    var is_navonly      = (is_nav && ((agt.indexOf(";nav") != -1) ||
                          (agt.indexOf("; nav") != -1)) );
    var is_nav6 = (is_nav && (is_major == 5));
    var is_nav6up = (is_nav && (is_major >= 5));
    var is_gecko = (agt.indexOf('gecko') != -1);


    var is_ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
    var is_ie3    = (is_ie && (is_major < 4));
    var is_ie4    = (is_ie && (is_major == 4) && (agt.indexOf("msie 4")!=-1) );
    var is_ie4up  = (is_ie && (is_major >= 4));
    var is_ie5    = (is_ie && (is_major == 4) && (agt.indexOf("msie 5.0")!=-1) );
    var is_ie5_5  = (is_ie && (is_major == 4) && (agt.indexOf("msie 5.5") !=-1));
    var is_ie5up  = (is_ie && !is_ie3 && !is_ie4);
    var is_ie5_5up =(is_ie && !is_ie3 && !is_ie4 && !is_ie5);
    var is_ie6    = (is_ie && (is_major == 4) && (agt.indexOf("msie 6.")!=-1) );
    var is_ie6up  = (is_ie && !is_ie3 && !is_ie4 && !is_ie5 && !is_ie5_5);

    // KNOWN BUG: On AOL4, returns false if IE3 is embedded browser
    // or if this is the first browser window opened.  Thus the
    // variables is_aol, is_aol3, and is_aol4 aren't 100% reliable.
    var is_aol   = (agt.indexOf("aol") != -1);
    var is_aol3  = (is_aol && is_ie3);
    var is_aol4  = (is_aol && is_ie4);
    var is_aol5  = (agt.indexOf("aol 5") != -1);
    var is_aol6  = (agt.indexOf("aol 6") != -1);

    var is_opera = (agt.indexOf("opera") != -1);
    var is_opera2 = (agt.indexOf("opera 2") != -1 || agt.indexOf("opera/2") != -1);
    var is_opera3 = (agt.indexOf("opera 3") != -1 || agt.indexOf("opera/3") != -1);
    var is_opera4 = (agt.indexOf("opera 4") != -1 || agt.indexOf("opera/4") != -1);
    var is_opera5 = (agt.indexOf("opera 5") != -1 || agt.indexOf("opera/5") != -1);
    var is_opera5up = (is_opera && !is_opera2 && !is_opera3 && !is_opera4);

    var is_webtv = (agt.indexOf("webtv") != -1); 

    var is_TVNavigator = ((agt.indexOf("navio") != -1) || (agt.indexOf("navio_aoltv") != -1)); 
    var is_AOLTV = is_TVNavigator;

    var is_hotjava = (agt.indexOf("hotjava") != -1);
    var is_hotjava3 = (is_hotjava && (is_major == 3));
    var is_hotjava3up = (is_hotjava && (is_major >= 3));

    // *** JAVASCRIPT VERSION CHECK ***
    var is_js;
    if (is_nav2 || is_ie3) is_js = 1.0;
    else if (is_nav3) is_js = 1.1;
    else if (is_opera5up) is_js = 1.3;
    else if (is_opera) is_js = 1.1;
    else if ((is_nav4 && (is_minor <= 4.05)) || is_ie4) is_js = 1.2;
    else if ((is_nav4 && (is_minor > 4.05)) || is_ie5) is_js = 1.3;
    else if (is_hotjava3up) is_js = 1.4;
    else if (is_nav6 || is_gecko) is_js = 1.5;
    // NOTE: In the future, update this code when newer versions of JS
    // are released. For now, we try to provide some upward compatibility
    // so that future versions of Nav and IE will show they are at
    // *least* JS 1.x capable. Always check for JS version compatibility
    // with > or >=.
    else if (is_nav6up) is_js = 1.5;
    // NOTE: ie5up on mac is 1.4
    else if (is_ie5up) is_js = 1.3

    // HACK: no idea for other browsers; always check for JS version with > or >=
    else is_js = 0.0;

    // *** PLATFORM ***
    var is_win   = ( (agt.indexOf("win")!=-1) || (agt.indexOf("16bit")!=-1) );
    // NOTE: On Opera 3.0, the userAgent string includes "Windows 95/NT4" on all
    //        Win32, so you can't distinguish between Win95 and WinNT.
    var is_win95 = ((agt.indexOf("win95")!=-1) || (agt.indexOf("windows 95")!=-1));

    // is this a 16 bit compiled version?
    var is_win16 = ((agt.indexOf("win16")!=-1) || 
               (agt.indexOf("16bit")!=-1) || (agt.indexOf("windows 3.1")!=-1) || 
               (agt.indexOf("windows 16-bit")!=-1) );  

    var is_win31 = ((agt.indexOf("windows 3.1")!=-1) || (agt.indexOf("win16")!=-1) ||
                    (agt.indexOf("windows 16-bit")!=-1));

    var is_winme = ((agt.indexOf("win 9x 4.90")!=-1));
    var is_win2k = ((agt.indexOf("windows nt 5.0")!=-1));

    // NOTE: Reliable detection of Win98 may not be possible. It appears that:
    //       - On Nav 4.x and before you'll get plain "Windows" in userAgent.
    //       - On Mercury client, the 32-bit version will return "Win98", but
    //         the 16-bit version running on Win98 will still return "Win95".
    var is_win98 = ((agt.indexOf("win98")!=-1) || (agt.indexOf("windows 98")!=-1));
    var is_winnt = ((agt.indexOf("winnt")!=-1) || (agt.indexOf("windows nt")!=-1));
    var is_win32 = (is_win95 || is_winnt || is_win98 || 
                    ((is_major >= 4) && (navigator.platform == "Win32")) ||
                    (agt.indexOf("win32")!=-1) || (agt.indexOf("32bit")!=-1));

    var is_os2   = ((agt.indexOf("os/2")!=-1) || 
                    (navigator.appVersion.indexOf("OS/2")!=-1) ||   
                    (agt.indexOf("ibm-webexplorer")!=-1));

    var is_mac    = (agt.indexOf("mac")!=-1);
    // hack ie5 js version for mac
    if (is_mac && is_ie5up) is_js = 1.4;
    var is_mac68k = (is_mac && ((agt.indexOf("68k")!=-1) || 
                               (agt.indexOf("68000")!=-1)));
    var is_macppc = (is_mac && ((agt.indexOf("ppc")!=-1) || 
                                (agt.indexOf("powerpc")!=-1)));

    var is_sun   = (agt.indexOf("sunos")!=-1);
    var is_sun4  = (agt.indexOf("sunos 4")!=-1);
    var is_sun5  = (agt.indexOf("sunos 5")!=-1);
    var is_suni86= (is_sun && (agt.indexOf("i86")!=-1));
    var is_irix  = (agt.indexOf("irix") !=-1);    // SGI
    var is_irix5 = (agt.indexOf("irix 5") !=-1);
    var is_irix6 = ((agt.indexOf("irix 6") !=-1) || (agt.indexOf("irix6") !=-1));
    var is_hpux  = (agt.indexOf("hp-ux")!=-1);
    var is_hpux9 = (is_hpux && (agt.indexOf("09.")!=-1));
    var is_hpux10= (is_hpux && (agt.indexOf("10.")!=-1));
    var is_aix   = (agt.indexOf("aix") !=-1);      // IBM
    var is_aix1  = (agt.indexOf("aix 1") !=-1);    
    var is_aix2  = (agt.indexOf("aix 2") !=-1);    
    var is_aix3  = (agt.indexOf("aix 3") !=-1);    
    var is_aix4  = (agt.indexOf("aix 4") !=-1);    
    var is_linux = (agt.indexOf("inux")!=-1);
    var is_sco   = (agt.indexOf("sco")!=-1) || (agt.indexOf("unix_sv")!=-1);
    var is_unixware = (agt.indexOf("unix_system_v")!=-1); 
    var is_mpras    = (agt.indexOf("ncr")!=-1); 
    var is_reliant  = (agt.indexOf("reliantunix")!=-1);
    var is_dec   = ((agt.indexOf("dec")!=-1) || (agt.indexOf("osf1")!=-1) || 
           (agt.indexOf("dec_alpha")!=-1) || (agt.indexOf("alphaserver")!=-1) || 
           (agt.indexOf("ultrix")!=-1) || (agt.indexOf("alphastation")!=-1)); 
    var is_sinix = (agt.indexOf("sinix")!=-1);
    var is_freebsd = (agt.indexOf("freebsd")!=-1);
    var is_bsd = (agt.indexOf("bsd")!=-1);
    var is_unix  = ((agt.indexOf("x11")!=-1) || is_sun || is_irix || is_hpux || 
                 is_sco ||is_unixware || is_mpras || is_reliant || 
                 is_dec || is_sinix || is_aix || is_linux || is_bsd || is_freebsd);

    var is_vms   = ((agt.indexOf("vax")!=-1) || (agt.indexOf("openvms")!=-1));

//--> end hide JavaScript


if ( !is_ie5_5up )
{
	alert( "To update the data using the browser you will need Internet Explorer Browser Version 5.5 or above, Please download from http://www.microsoft.com/windows/ie/downloads/default.mspx")
	
}

var TableForLookup;

 
var currentcell;    
var currentcelldiv;
var currentcelldiv_txtbox;
var currenthighlighted_td;
var currentSelectedType = 'TABLE';
var currentSelectedcell = null;
var currentSelectedRow= null;
var currentSelectedColumn= null;
var myCells = null;


var filterArray = null;
var currentValue;
var altkey = false;
var cellDirtyFlag  = false;
var changedcells = new Array(2);
var index = 0;


NS4 = (document.layers) ? true : false;
IE4 = (document.all)? true:false


var prevTD, prevTDValue, click;
click = false;


function dataForm()
{	
	var url = location.protocol + "//" +
			  location.hostname + ":"+
			  location.port +
			  location.pathname + "?" +
			  "action=" + "editTable" + "&" +
			  "tableId=" + eval("document.all.tableId").value + "&" +
			  "ViewPreference=" + eval("document.all.ViewPreference").value + "&" +
			  "bucketSize=" + eval("document.all.currentBucketSize").value + "&" +
			  "bucketNum=" + eval("document.all.bucketNumber").value + "&" +
			  "formMode=true&"
			  "formTableId=" + eval("document.all.formTableId").value;

	location.replace(url);
}

function getTableUpdates(tableId, tableName, viewPref)
{
  var action = 'getTransactions';
  var args = new Object();
  var query = showModalDialog("html/activityPeriodDlg.html", args, "dialogHeight:12;dialogWidth:22;status:no");

  var url = location.protocol + "//" +
      location.hostname + ":"+
      location.port +
      location.pathname + "?" +
      "action=" + action + "&" +
      "period=" + query.period + "&" +
      "startDate=" + query.startDate + "&" +
      "endDate=" + query.endDate + "&" +
      "tableId=" + tableId + "&" +
      "ViewPreference=" + viewPref + "&" +
      "tableName=" + tableName;

  location.assign(url)
}

function getRowUpdates(selectedRowId, tableId, tableName)
{
	if ( currentCheckboxCount  < 1 )
	{
		alert ( " Please select 1 or more rows to list the updates" ) 	;
	}
	else
	{	
		var rowCheckboxes = eval("document.all.rowId");
		rowId = "";
    	if(rowCheckboxes != null && rowCheckboxes.length > 0)
		{
			for ( countCheckBox = 0 ; countCheckBox < rowCheckboxes.length; countCheckBox++ )
			{
				var rowCheckBox = rowCheckboxes[countCheckBox];
				if ( rowCheckBox.checked == true  )
				{
					if(rowId.length > 0)
						rowId += ",";
						
					rowId += rowCheckBox.value;
				}
			}
		}
		else
		{
			if ( document.all.rowId.checked == true  )
			{
				rowId = document.all.rowId.value;
			}
		}

	  var action = 'getRowVersions';
	  var args = new Object();
	  var query = showModalDialog("jsp/collaboration/tables/ReportGenerator.jsp?tableid="+tableId, args, "dialogHeight:29;dialogWidth:25");
	  // window.open("jsp/collaboration/tables/ReportGenerator.jsp?tableid="+tableId,'','height=300,width=450,left=0,top=0,menubar=yes,status=yes,toolbar=yes,scrollbars=yes,location=yes,directories=yes,resizable=yes');
	if(query != null)
		{
		  var url = location.protocol + "//" +
			  location.hostname + ":"+
			  location.port +
			  location.pathname + "?" +
			  "action=" + action + "&" +
			  "period=" + query.period + "&" +
			  "startDate=" + query.startDate + "&" +
			  "endDate=" + query.endDate + "&" +
			  "tableId=" + tableId + "&" +
			  "rowIds=" + rowId + "&" +
			  "baseline=" + query.baseline + "&" +
			  "execlDump=HTML&" +
			  "columnids=" + query.columnids ;
		  location.assign(url)
		}
	}
}

function getCellUpdates(selectedCellId, tableId, tableName)
{
  if (selectedCellId == -1 || selectedCellId == 'undefined')
  {
  	alert("Please select a cell to list updates");
  	return;
  }
  var action = 'getCellVersions';
  var args = new Object();
  var query = showModalDialog("html/activityPeriodDlg.html", args, "dialogHeight:12;dialogWidth:22");

  var url = location.protocol + "//" +
      location.hostname + ":"+
      location.port +
      location.pathname + "?" +
      "action=" + action + "&" +
      "period=" + query.period + "&" +
      "startDate=" + query.startDate + "&" +
      "endDate=" + query.endDate + "&" +
      "tableId=" + tableId + "&" +
      "tableName=" + tableName + "&" +
      "selectedCellId=" + selectedCellId;

  location.assign(url)
}

function GetScriptEngineInfo(){
   var s;
   s = ""; // Build string with necessary info.
   s += ScriptEngine() + " Version ";
   s += ScriptEngineMajorVersion() + ".";
   s += ScriptEngineMinorVersion() + ".";
   s += ScriptEngineBuildVersion();
  return(s);
}

function addHyperlink()
{
	addHyperlinkToCell()
}

var recIndex = 0;
var currRec;
var numRecords = 0;
function updateMainTable()
{
	var j;
	for (j= 1; j < currRec.cells.length - 4; j++) 
	{
		var tdCell = currRec.cells(j);
		var columnId = tdCell.columnId;
		var frmTD = eval("document.all.frc"+columnId);
		if (frmTD != null)
		{
			if ( tdCell.hasChildNodes() && tdCell.children(0).tagName == "DIV")
			{
				if (frmTD.value != tdCell.children(0).innerText)
				{	
					tdCell.children(0).innerText = frmTD.value;
					changeCell(tdCell);
				}
			}
			else if (tdCell.hasChildNodes() && tdCell.children(0).tagName == "SELECT")
			{
				if (tdCell.children(0).selectedIndex != frmTD.selectedIndex)
				{
					tdCell.children(0).selectedIndex= frmTD.selectedIndex;
					lookupValueSelected(tdCell.children(0));
				}
			}
		}
	}
}
function saveAllRecordChanges()
{
	updateMainTable()
	
	commitToBoardwalk('commitCells');

}

function getRow()
{
	var j;
	numRecords = document.all.bwTableContents.rows.length;
	currRec = document.all.bwTableContents.rows(recIndex);
	for (j= 1; j < currRec.cells.length - 4; j++) 
	{
		var tdCell = currRec.cells(j)
		var columnId = tdCell.columnId;
		var frmTD = eval("document.all.frc"+columnId);
		if (frmTD != null)
		{
			if ( tdCell.hasChildNodes() && tdCell.children(0).tagName == "DIV")
			{		
				frmTD.innerText = tdCell.innerText;
			}
			else if (tdCell.hasChildNodes() && tdCell.children(0).tagName == "SELECT")
			{
				var options = tdCell.children(0).options
				var selectedindex = 0

				// find the selection
				for ( var op  = 0; op < options.length; op++ )
				{
						var optionElemFromTable = options.item(op);
						if ( optionElemFromTable.selected == true )
						{
							selectedindex =  op;
							break;
						}
				}

				frmTD.options.item(selectedindex).selected = true
			}
		}
	}
	if (numRecords < 2) 
	{
		frmPrevRec.style.display='none';
		frmNextRec.style.display='none';
	}
}

function nextRow()
{
	updateMainTable();
	recIndex = recIndex + 1;
	
	frmPrevRec.disabled = false;
	if (numRecords == recIndex + 1)
	{
		frmNextRec.disabled=true;
	}
	else
	{
		frmNextRec.disabled=false;
	}
	getRow();
}

function prevRow()
{
	updateMainTable();
	recIndex = recIndex - 1;
	
	frmNextRec.disabled=false;
	if (recIndex <= 0)
	{
		frmPrevRec.disabled = true;
	}
	else
	{
		frmPrevRec.disabled = false;
	}
	getRow();
}


function editRow()
{
	var args = new Object();
	var currentRow = currentSelectedRow.parentElement.parentElement
	if ( currentSelectedRow == null )
	{
		alert("Please select a row to edit")
		return;
	}

	args.editRow = currentRow

	args.thead =  eval("document.all.bwHeadings")

	var query = showModalDialog("html/edit_row.html", args, "dialogHeight:30;dialogWidth:25;resizable:yes;scroll:yes;status:no");



	if ( currentRow.children  != null  )
	{
		var cellTags = currentRow.children.tags("TD")
		//alert( cellTags.length  )
		if ( cellTags.length > 0 )
		{
			for ( var cellIndex = 0; cellIndex< cellTags.length; cellIndex++ )
			{
				var cellTD = cellTags.item(cellIndex)

				if ( cellTD.rowId != null )
				{

					changeCell(cellTD)
				}
			}

		}			
	}

	if ( query == null )
	{
		return;
	}


}
  
function addRowViaFormRequest(addRowViaFormRequest) 
{    		
	document.forms[0].action.value = 'addRowViaFormRequest'	
	document.forms[0].submit()	
}


function addRow(addRowCmd) 
{    		
	
	if ( currentCheckboxCount > 1 )
	{
		alert ( " You have selected more then one rows, Please select 0 or 1 rows to add a new Row" ) 	
	}
	else
	{
		 document.forms[0].action.value =addRowCmd
		 
		 document.forms[0].submit()	
	}
	 
}

function addDocumentToCell()
{
	var selectedCellId = eval("document.all.selectedCellId").value;
	if (selectedCellId == -1 || selectedCellId == 'undefined')
	{
		alert("Please select a cell first");
		return;
	}
	document.forms[0].action.value = "addDocumentToCell"
	document.forms[0].submit()
}

function  addHyperlinkToCell()
{
	if ( currentSelectedcell != null )
	{
		var CellTypeId  = "CellType" + currentSelectedcell.name
		
		if ( eval("document.all."+CellTypeId).value == "STRING" )
		{
			var urlName=""
			var url=""
			var screentip=""
									
			if (currentSelectedcell.children(1).children.length == 1 )
			{
				if (currentSelectedcell.children(1).children(0).tagName == "A")
				{

					urlName = currentSelectedcell.innerText
					url = currentSelectedcell.children(1).children(0).href
					url = encodeURIComponent(url)
					screentip = currentSelectedcell.children(1).children(0).title						
				}
				else
				{
					urlName = currentSelectedcell.innerText
				}
			}
			else
			{
					urlName = currentSelectedcell.innerText
			}
			
			var urlObj = new Object();
			urlObj.urlName = urlName;
			urlObj.url = url;
			urlObj.screentip = screentip;
			
			var rUrlObj = showModalDialog("jsp/collaboration/tables/addHyperlinkDlg.html", urlObj, "dialogHeight:20;dialogWidth:25;status:no")
			
			if (rUrlObj == null )
			{
				return;
			}
			
			urlName = rUrlObj.urlName
			url = rUrlObj.url
			screentip = rUrlObj.screentip
			
			

			dirtyCellid = "dirtyCell"+currentSelectedcell.name
			inputCellid = "Cell"+currentSelectedcell.name
			prevCellid  = "pCell"+currentSelectedcell.name

			if (  url == null || url == "")
			{
				document.all.item(currentSelectedcell.id).children(1).innerHTML =document.all.item(currentSelectedcell.id).children(1).innerText
				document.all.item(inputCellid).value =document.all.item(currentSelectedcell.id).children(1).innerText
				document.all.item(dirtyCellid).value=true	
				cellDirtyFlag = true  
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
			document.all.item(currentSelectedcell.id).children(1).innerHTML = ATag
			document.all.item(inputCellid).value =ATag
			document.all.item(dirtyCellid).value="true"
			cellDirtyFlag = true 
			
			//var openloc = 'jsp/collaboration/tables/add_hyperlink.jsp?'+'urlName='+urlName+'&url='+url+'&screentip='+screentip+'&bwcellId='+currentSelectedcell.name+'&htmlid='+currentSelectedcell.id
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

function trim(strText) { 
    // this will get rid of leading spaces 
    while (strText.substring(0,1) == ' ') 
        strText = strText.substring(1, strText.length);

    // this will get rid of trailing spaces 
    while (strText.substring(strText.length-1,strText.length) == ' ')
        strText = strText.substring(0, strText.length-1);

   return strText;
} 

function HideLookUp()
{
	if(SelCellId  > 0)
		if(document.getElementById('div'+SelCellId) != null && document.getElementById('div'+SelCellId).style.visibility == 'visible')
			document.getElementById('div'+SelCellId).style.visibility = 'hidden';
}

function changeCell(currTD)
{
	
	// change value for previous cell
	// alert ("click = " + click)
	
	if (prevTD != null) 
	{
		prevTD.style.backgroundColor="white"
			
	}
	if (currTD != null) 
	{
		
		
		highlightRow(null)
		currTD.style.backgroundColor="#C0C0C0"
		currentSelectedType = "CELL"
		currentSelectedRow = null
		currentSelectedColumn = null		

		
	}
	
	
	if (click == true &&  prevTD != null ) 
	{ 
		// not the first click, set previous cell
		prevHdnCell = "Cell" + prevTD.name
		prevHdnCellDirtyFlag = "dirtyCell" + prevTD.name
		
		
		oldValue = prevTDValue
		var newValue = trim(prevTD.innerText)

		if (prevTD.children(1).children.length == 1 )
		{
			if (prevTD.children(1).children(0).tagName == "A")
			{
				newValue = trim(prevTD.children(1).innerHTML)
			}
		}
		
		
		
		//  alert("'"+oldValue+"'"+"=="+"'"+newValue+"'")
		
		if ( oldValue != newValue )
		{
			prevHdnCellType = document.all.item("CellType" + prevTD.name).value 
			if ( ! datatypeCheck( newValue, prevHdnCellType) )
			{
				alert ( " Not a valid entry for the Column data type " +  prevHdnCellType)
				
				return false
			}
			//alert(" Setting Previous Cell to " + newValue);
			
			changedcells[index] = newValue
			index++
			
			document.all.item(prevHdnCell).value = newValue
			document.all.item(prevHdnCellDirtyFlag).value = "true"
			prevTD.style.color = "red"
			cellDirtyFlag = true
		}
		else
		{
			document.all.item(prevHdnCellDirtyFlag).value = "false"
			prevTD.style.color = "black"
		
		}
	}
	
	if (currTD != null) 
	{
		currentSelectedcell = currTD
		document.all.item("selectedCellId").value  = currentSelectedcell.name
		prevTD =  currTD
		prevTDValue = document.all.item("PCell" + prevTD.name).value 
	}		

	click = true;
	// alert ("click = " + click)
	return true
}


function selectTableForCell( cellIdField, cellFieldName ) 
{
   var  TableForCell = window.open('MyTables?action=selectTable&cellForUpdateField='+cellIdField+'&cellForUpdateFieldName='+cellFieldName+'&rowId=1&tableId=1','newin','toolbar=yes,location=yes,scrollbar=yes,resizable=yes,menubar=no,height=500,width=600')
   
  
}




function lookupTableValueSelected(lookupCellList) 
{    		
   lookup_cell_href  = "lookupcell_href" + lookupCellList.name
   document.all.item(lookup_cell_href).href = lookupCellList.options[lookupCellList.selectedIndex].href 		
   lookupValueSelected(lookupCellList)
}



function lookupCellClicked(lookupCellList)
{
	currentSelectedcell = lookupCellList;
	lookupValueSelected(lookupCellList)
}


function updateSelected()
{
   var uList = eval("document.forms[0].userList");
   var mydList = eval("document.forms[0].dList");
   uList.value="";
   for (var i=0;i<mydList.length;i++)
   {
	if (i > 0)
          uList.value=uList.value+"+";
	uList.value=uList.value+mydList.options[i].value;
   }
}

function lookupValueSelected( lookupCellList )
{
	if ( currentcell != null )
	{
		 if ( ! updateCellValue() )
		 {
			return;
		 }

		if ( currenthighlighted_td != null )
		 {
			currenthighlighted_td.style.backgroundColor ="#ffffff";
		 }

		 if ( currentcelldiv != null )
		 {
			currentcelldiv.style.visibility="hidden"
		 }
	 }

	currentlookupCell = lookupCellList;


	newvalue = lookupCellList.options[lookupCellList.selectedIndex].value   	

	CellNewValue = "Cell" + currentlookupCell.name
	CellDirtyFlag= "dirtyCell" + currentlookupCell.name
	CellPreviousValue = "PCell" + currentlookupCell.name
	CellType = "CellType" + currentlookupCell.name


	if ( document.all.item(CellPreviousValue).value != newvalue )
	{
		document.all.item(CellNewValue).value = newvalue
		document.all.item(CellDirtyFlag).value = "true"
		cellDirtyFlag = true					
	}
	else
	{
		document.all.item(CellDirtyFlag).value = "false"
	}

	return true

}



 function highlightcell( tdfield )
 {
 	if ( currentcell != null )
	{
	 	 if ( ! updateCellValue() )
		 {
			return;
		 }
	 	
		 if ( currenthighlighted_td != null )
		 {
			currenthighlighted_td.style.backgroundColor ="#ffffff";
		 }
		 
		 if ( currentcelldiv != null )
		 {
		 	currentcelldiv.style.visibility="hidden"
		 }
   	}
   	 
   	tdfield.style.backgroundColor ="#C0C0C0";
	currenthighlighted_td = tdfield;
	currentcell = tdfield;
	currentSelectedcell = tdfield;	
   	cell_div_id = tdfield.id + "_div";   	
   	cell_div_txtbox_id = cell_div_id + "_txtbox";			
	currentValue = document.all.item(cell_div_txtbox_id).value
	currentcelldiv = document.all.item(cell_div_id);
	currentcelldiv_txtbox = document.all.item(cell_div_txtbox_id);
   	 
   	 
 }

function query(doNext)
{
    document.all.item('action').value =  "sendQuery"
    eval("document.all.queryType").value =doNext
    document.forms[0].submit()    
    
}


function commitToBoardwalk(doNext)
{    
    document.forms[0].action.value = doNext
	// update the last cell
	
	changeCell(null)
	//var x=0; 
	//for (x=0; x<2; x++) 
	//{ 
	//alert(changedcells[x]); 
	//} 
	
	document.forms[0].submit()
}

function addColumn(doNext)
{
    document.forms[0].action.value = doNext
    
    if ( cellDirtyFlag == true )
	{
		alert(" Please commit before adding new Column")
	}
	else
	{
		document.forms[0].submit()
	}
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

function getLatestValues(doNext){

    document.forms[0].action.value = doNext
    document.forms[0].submit()
}


function getDesignValues(doNext){

     document.all.item('ViewPreference').value ="DESIGN"
    document.forms[0].action.value = doNext
    document.forms[0].submit()
}

function getTableView(viewPreference) 
{    		
	document.forms[0].action.value = 'editTable'
	 document.all.item('ViewPreference').value = viewPreference
	document.forms[0].submit()
}

function getTableViewWithQueryPreference(viewPreference,queryPreference) 
{    		
	document.forms[0].action.value = 'editTable'
	document.all.item('ViewPreference').value = viewPreference
	document.all.item('QueryPreference').value = queryPreference
	document.forms[0].submit()
}


function getTableQuery(queryType) 
{    		
	document.forms[0].action.value = 'editTable'
	 document.all.item('QueryPreference').value = queryType
	document.forms[0].submit()
}


function getTableReport(reportType) 
{    		
	 document.forms[0].action.value = 'displayInPlainHTML'
	
	 document.all.item('ViewPreference').value = reportType
	  document.all.item('QueryPreference').value = ' '	
	 document.forms[0].submit()
}

function getTableReportByNh(reportType,showChildrenNhsOnly ) 
{    		
	  document.forms[0].action.value = 'chooseNhForTableContents'	  
	  document.all.item('ViewPreference').value = reportType
	  document.all.item('showChildrenNhsOnly').value = showChildrenNhsOnly	 
	 document.forms[0].submit()
}



function deactivateRow(deactivateRowCmd) 
{    		
	
	if ( currentCheckboxCount  < 1 )
	{
		alert ( " Please select 1 or more rows to deactivate a  Row" ) 	
	}
	else
	{
		 document.forms[0].action.value =deactivateRowCmd
		 document.forms[0].submit()	
	}
	 
}

function changeRowOwner(changeRowOwnerCmd) 
{    		
	
	if ( currentCheckboxCount  < 1 )
	{
		alert ( " Please select 1 or more rows to assign a new owner" ) 	
	}
	else
	{
		 document.forms[0].action.value =changeRowOwnerCmd
		 document.forms[0].submit()	
	}
	 
}

function compareTable()
{
  var action = 'editTable';
  var args = new Object();
  //alert(eval("document.all.selectedColumnId").value)
  args.selectedColumnId = eval("document.all.selectedColumnId").value;
  var query = showModalDialog("html/tableCompareDlg.html", args, "dialogHeight:14;dialogWidth:24;status:no");

  if (query != null)
  {
  
    	var url;
    	var i;
    	
    	url = location.protocol + "//" +
    	      location.hostname + ":"+
    	      location.port +
    	      location.pathname + "?" +
    	      "action=editTable&" +
    	      "tableId=" + eval("document.all.tableId").value + "&" +
    	      "ViewPreference=" + eval("document.all.ViewPreference").value + "&" +
	      "period=" + query.period + "&" +
	      "asOfDate=" + query.endDate + "&" +
	      "compDate=" + query.startDate+ "&trackState=true"    	      
	if (query.showDiffSideBySide != null)
	{
		url = url + "&showDiffSideBySide=true";
	}
	if (query.numericDiff != null)
	{
		url = url + "&" + query.numericDiff + "=true";
	}
	if (query.reqCols != null)
	{
		url = url + "&reqCols=" + query.reqCols;
	}
	if (query.reqColsComp != null)
	{
		url = url + "&reqColsComp=" + query.reqColsComp;
	}	
    	if ( filterArray == null )
	{
		filterArray = new Array();
	}
    	for (i = 0; i < filterArray.length; i++)
    	{
    		url = url + "&cc" + filterArray[i].columnId + "=" + filterArray[i].filter;
    	}
	if (query.hideUnchangedRows != null)
	{
		url = url + "&hideUnchangedRows=true";
	}
	if (query.hideChangedRows != null)
	{
		url = url + "&hideChangedRows=true";
	}
	if (query.hideNewRows != null)
	{
		url = url + "&hideNewRows=true";
	}
	if (query.hideDeletedRows != null)
	{
		url = url + "&hideDeletedRows=true";
	}	
    	
    	location.assign(url)
  }
  
}

  
 
 
 function getAdminPage() 
 {    		
 	document.forms[0].action.value = 'editTableAdmin'
 	document.forms[0].submit()
 }
  
  
  var systemColumnsShown = false
  function showSystemColumns()
  {
  	if (systemColumnsShown == false)
  	{
  		eval("document.all.SystemColumns").style.display=""
  		eval("document.all.sysColShowImg").src="images/collapse.gif"
  		eval("document.all.sysColShowImg").title="Hide System Columns"
  		eval("document.all.ViewSysColMenu").innerText="Hide System Columns"
  		
  		systemColumnsShown = true
  	}
  	else
  	{
  		eval("document.all.SystemColumns").style.display="none"
  		eval("document.all.sysColShowImg").src="images/expand.gif"
  		eval("document.all.sysColShowImg").title="Show System Columns"
  		eval("document.all.ViewSysColMenu").innerText="Show System Columns"
  
  		systemColumnsShown = false
  	}
  
  }
  var showTableUpdateStatus = true
  function expandTableUpdateStatus()
  {
  	if (showTableUpdateStatus == false)
  	{
  		tableUpdateStatus.style.display=""
  		eval("document.all.tableUpdateStatusImg").src="images/collapse.gif"
  		eval("document.all.tableUpdateStatusImg").title="Hide Table Update Status"
  		showTableUpdateStatus = true
  	}
  	else
  	{
  		tableUpdateStatus.style.display="none"
  		eval("document.all.tableUpdateStatusImg").src="images/expand.gif"
  		eval("document.all.tableUpdateStatusImg").title="Show Table Update Status"
  
  		showTableUpdateStatus = false
  	}
  
  }
  function makeContentEditable()
  {
  	var divObj = window.event.srcElement;
  	divObj.contentEditable = "true"
  
  }
  function checkForURL(divObj)
  {
  	var val = divObj.innerHTML
  	// make sure it is a well formed url with protocol
  	//var urlRegPattern = /(\w+):\/\/(\S*)/;
  	var urlRegPattern = /<A.*<\/A>/;
  	var result = val.match(urlRegPattern);
  
  	if (result != null ) // no protocol defined
  	{
  		divObj.contentEditable = "false"
  		divObj.attachEvent("ondblclick",makeContentEditable);
  	}
  
  }
  
  var checkedRows  = new Array();
  function flipAllCheckBoxes(checkAll)
  {
  
  
  	var rowCheckboxes = eval("document.all.rowId")
  	var c =0
  	
  	currentCheckboxCount = 0  
  	currentSelectedType = "TABLE"
  	currentSelectedRow = null
  	currentSelectedCell = null
  	currentSelectedColumn = null
  	
  	
  		
  		var noOfRows = rowCheckboxes.length
  		if ( noOfRows != null && checkAll.checked == true )
  		{
  			
				for (  c; c < rowCheckboxes.length; c++ )
				{
							var rowCheckBox = rowCheckboxes[c]
							


							if ( rowCheckBox.checked == false  )
							{
								rowCheckBox.checked = true
								var trObj = eval("document.all.row" + rowCheckBox.value)

								var rowCells = trObj.children
								for (i = 0; i < rowCells.length; i++)
								{
									if(rowCells[i].tagName == "TD")
									{
										rowCells[i].style.backgroundColor="#C0C0C0"
									}
								}
							}
							
							currentCheckboxCount++
				}// for
  		
  		}
  		else if (noOfRows == null && checkAll.checked == true )
  		{
  			var trObj = eval("document.all.row" + rowCheckboxes.value)
  			var rowCells = trObj.children
			for (i = 0; i < rowCells.length; i++)
			{
				if(rowCells[i].tagName == "TD")
				{
					rowCells[i].style.backgroundColor="#C0C0C0"
				}
			}
			
  		}
  		else if (noOfRows == null && checkAll.checked == false )
		  		{
		  			var trObj = eval("document.all.row" + rowCheckboxes.value)
		  			var rowCells = trObj.children
					for (i = 0; i < rowCells.length; i++)
					{
						if(rowCells[i].tagName == "TD")
						{
							rowCells[i].style.backgroundColor="white"
						}
					}
					
  		}
  		else if (noOfRows != null && checkAll.checked == false )
		{
			for (  c; c < rowCheckboxes.length; c++ )
			{
					var rowCheckBox = rowCheckboxes[c]
						

					 if ( rowCheckBox.checked == true  )
					{
						rowCheckBox.checked = false
						var trObj = eval("document.all.row" + rowCheckBox.value)
						var rowCells = trObj.children
						for (i = 0; i < rowCells.length; i++)
						{
							if(rowCells[i].tagName == "TD")
							{
								rowCells[i].style.backgroundColor="white"
							}
						}
					}
			}// for
							
  		}
  	
  
  	
  	
  
  
  	
  
  
  	if ( checkAll.checked == true  )
  	{
  			var checkAllCbox = eval("document.all.checkAll")
  			checkAllCbox[1].checked = true
  			// checkAllCbox[1].checked = true
  
  	}
  	else
  	{
  			var checkAllCbox = eval("document.all.checkAll")
  			checkAllCbox[1].checked = false
  			// checkAllCbox[1].checked = false
  			currentCheckboxCount = 0
  	}
  
  
  }
  
  
    function highlightRow(cb)
    {
    	var ctrlKeyPressed =  false
    	if ( window.event != null )
    	{
    		 ctrlKeyPressed = window.event.ctrlKey
    	}
    	
    	
    	if ( cb == null )
    	{
    		var rowCheckboxes = eval("document.all.rowId")
    		var c = 0
    		for (  c; c < rowCheckboxes.length; c++ )
    		{
    			var rowCheckBox = rowCheckboxes[c]
    
    
    			if ( rowCheckBox.checked == true  )
    			{
    				rowCheckBox.checked = false
    				var trObj = eval("document.all.row" + rowCheckBox.value)
    				var rowCells = trObj.children
    				for (i = 0; i < rowCells.length; i++)
    				{
    					if(rowCells[i].tagName == "TD")
    					{
    						rowCells[i].style.backgroundColor="white"
    					}
    				}
    			}
    		}
    
    
    
    		checkedRows  = new Array()
    		currentCheckboxCount = 0
    		var checkAllCbox = eval("document.all.checkAll")
    		
    		checkAllCbox[1].checked = false
    		// checkAllCbox[1].checked = false
    
    	} // if  ( cb == null )
    	else
    	{
    
    			var rowId = cb.value
    			var trObj = eval("document.all.row" + rowId)
    
    			if (cb.checked)
    			{
    				// unhighlight the column
    				highlightColumn('bwTableContents', -1, null)
    				if ( currentSelectedType != "ROW" )
    					changeCell(null)
    
    				
    
    
    
    				var rowCells = trObj.children
    				for (i = 0; i < rowCells.length; i++)
    				{
    					if(rowCells[i].tagName == "TD")
    					{
    						rowCells[i].style.backgroundColor="#C0C0C0"
    					}
    				}
    				
    				currentSelectedRow = cb
    				//currentSelectedType = "ROW"    				
    				//currentSelectedCell = null
    				//currentSelectedColumn = null
    
    
    				currentCheckboxCount++
    				checkedRows[""+rowId] = rowId
    				// Highlight all the checkboxes above me til I find a checkbox that was checked if ctrlkey was  pressed
    				if ( ctrlKeyPressed == true )
    				{
    
    					var foundCheckedBox = false
    					var rowCheckboxes = eval("document.all.rowId")
    					var c = 0
    					for (  c; c < rowCheckboxes.length; c++ )
    					{
    						var rowCheckBox = rowCheckboxes[c]
    						// alert (" foundCheckedBox " + foundCheckedBox +" cb.value = " + cb.value + " rowCheckBox = "  + rowCheckBox.checked +  rowCheckBox.value )
    						if ( rowCheckBox. checked == true && rowCheckBox.value != cb.value )
    						{
    							foundCheckedBox = true
    						}
    						else
    						{
    
    							if ( foundCheckedBox == true && rowCheckBox. checked == false && rowCheckBox.value != cb.value )
    							{
    								var trObj = eval("document.all.row" + rowCheckBox.value)
    								var rowCells = trObj.children
    								for (i = 0; i < rowCells.length; i++)
    								{
    									if(rowCells[i].tagName == "TD")
    									{
    										rowCells[i].style.backgroundColor="#C0C0C0"
    									}
    								}
    								rowCheckBox. checked = true
    								currentCheckboxCount++
    							}
    
    							if (foundCheckedBox == true && rowCheckBox. checked == true  && rowCheckBox.value== cb.value )
    							{
    									break
    							}
    						}
    
    					}
    				}
    
    
    
    			}
    			else //if (cb.checked)
    			{
    				currentCheckboxCount--
    				var rowCells = trObj.children
    				for (i = 0; i < rowCells.length; i++)
    				{
    					if(rowCells[i].tagName == "TD")
    					{
    						rowCells[i].style.backgroundColor="white"
    					}
    				}
    
    				delete checkedRows[""+rowId]
    			}// if (cb.checked = false)
    	} //  ( cb != null )
    
  }
  var prevCol = null;
  function highlightColumn(tableId, colIdx, th)
  {
  
  	var tableEl = eval("document.all."+tableId);
  	var tableRows = tableEl.rows
  
  	if (colIdx != -1 )
  	{
  		var sameCol = false;
  		if (prevCol != null && prevCol.id == th.id)
  		{
  			sameCol = true;
  		}
  		
  		if (sameCol != true)
  		{
  			// highlight current column
  			if ( tableRows != null && tableRows.length > 0 )
  			{
  				// unhighlight the row
  
  				highlightRow(null)
  
  				for (i = 0; i < tableRows.length; i++)
  				{
  					if (tableRows[i].cells[colIdx].tagName == "TD")
  					{
  							tableRows[i].cells[colIdx].style.backgroundColor ="#C0C0C0"
  					}
  				}
  			}
  			eval("document.all.selectedColumnId").value = th.id.substring(6)
  			eval("document.all.selectedColumnIdx").value = th.selectIndex
  			eval("document.all.selectedColumnType").value = th.columnType
  
  			th.style.backgroundColor ="#C0C0C0"
  
  			if (th.orderType == "STATE")
  			{
  				currentSelectedType = "COLUMN"
  				document.all.item("selectedColumnId").value  = th.id.substring(6)
  				currentSelectedColumn = th
  				currentSelectedCell = null
  				currentSelectedColumn = null
  
  			}
  
  			if ( th.orderType == "NORMAL")
  			{
  
  				currentSelectedType = "TABLE"
  				document.all.item("selectedColumnId").value  = th.id.substring(6)
  
  				currentSelectedColumn = th
  				currentSelectedCell = null
  				currentSelectedColumn = null			
  				// eval("document.all.trackChangeObject").value = "TABLE_DELTA_COLUMN"
  
  			}
  
  		}// same col
  	}// if colIdx == -1
  
  	//unhighlight the previous column
  	if (prevCol != null) 
  	{
  		if ( tableRows != null && tableRows.length > 0 )
  		{
  			for (i = 0; i < tableRows.length; i++) 
  			{
  				if (tableRows[i].cells[prevCol.selectIndex].tagName == "TD") 
  				{
  					tableRows[i].cells[prevCol.selectIndex].style.backgroundColor ="white"
  				}
  			}
  		} 
  		prevCol.style.backgroundColor ="#fddeb9"
  		
  	}
  
  	if (prevCol != null && prevCol.selectIndex == colIdx ) 
  	{
  		prevCol = null
  		eval("document.all.selectedColumnId").value = -1
  		eval("document.all.selectedColumnIdx").value = -1
  		eval("document.all.selectedColumnType").value = 'undefined'
  
  	} else 
  	{
  		prevCol = th
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
  
  function updateColumn()
  {
  	if (document.forms[0].selectedColumnId.value == -1){
  		alert ("No Column Selected")
  	} else {
  		document.forms[0].action.value = 'updateColumn'
  		document.forms[0].submit()
  
  	}
  }
  
  
  function changeQSVar(variable,value) {
  	// var to hold the final string
  	var string = "";
  	// vars for use in the loop
  	var i = 1;
  	var thisVar = "";
  	var thisIndex = "";
  	var qsArray = "";
  	var changedIt = false;
  	// if there is a third argument, use that as the query string
  	// otherwise default to location.search
  	var qs = location.search;
  	if (arguments.length > 2) qs = arguments[2];
  	// cut off everything before question mark
  	if (qs.indexOf("?")!=-1) qs = qs.slice(qs.indexOf("?")+1);
  	// unescape any qs ampersands
  	qs = qs.replace(/&amp;/gi, "&");
  	// put the query string into an array for easier looping
  	qsArray = qs.split("&");
  	// now, loop over the array and rebuild the string
  	for (i=0; i<qsArray.length; i++) {
  		thisIndex = qsArray[i].split("=");
  		thisVar = thisIndex[0];
  		// if this is the var, edit it to the value, otherwise, just append
  		if (thisVar == variable){
  			string = string+thisVar+"="+value+"&";
  			changedIt = true;
  		} else {
  			string = string+thisIndex.join("=")+"&";
  		}
  	}
  
  
  
  
  	// if it was not changed, add it!
  	if (!changedIt) string = string+variable+"="+value;
  	// trim trailing ampersand if necessary
  	if (string.charAt(string.length-1)=="&") string = string.slice(0,string.length-1);
  	// return the string
  	return string;
  }
  
  
  function hideColumn()
    {
    	if (document.forms[0].selectedColumnId.value == -1){
    		alert ("No Column Selected")
    	} else {
    		//var qs = changeQSVar("cc"+document.forms[0].selectedColumnId.value, "__hidden__")
    		// alert(qs)
    		//location.search = qs
    		eval("document.all.Col"+document.forms[0].selectedColumnIdx.value).style.display="none"
    		
    		// add the filter to the form
    		
    		var columnId = document.forms[0].selectedColumnId.value
    		//var newFCO = document.createElement("input");
    		//newFCO.type="hidden"
    		//EditTable.appendChild(newFCO);
    		//newFCO.name="cc"+columnId;
    		//newFCO.id="cc"+columnId;
    		//newFCO.value = "__hidden__"
    		if (filterArray == null)
    		{ 
    			filterArray = new Array();
    		}
    		var filterObj = new Object();
    		filterObj.columnId = columnId;
    		filterObj.filter = "__hidden__";
    		
    		var nFil = filterArray.length;
    		filterArray[nFil] = filterObj;
    	}
  }
  
  function unhideColumns()
  {
  	var i;
  	var anyColumnHidden = <%=anyColumnHidden%>
  	if (anyColumnHidden = true)
  	{
  		url = location.protocol + "//" +
  			      location.hostname + ":"+
  			      location.port +
  			      location.pathname + "?" +
  			      "action=editTable&" +
  			      "tableId=" + eval("document.all.tableId").value + "&" +
  	      		     "ViewPreference=" + eval("document.all.ViewPreference").value 
  	      	location.assign(url);
  	}
  	else
  	{
  		for (i = 0; i < eval("document.all.bwColumns").childNodes.length; i++)
  		{
  			eval("document.all.bwColumns").childNodes(i).style.display=""
  		}
  	}
  
  }
  
  function addComment()
  {
  	var existingComment = eval("document.all.tableComment").value;
  	var newComment = showModalDialog("jsp/collaboration/tables/addCommentDlg.html", existingComment, "dialogHeight:15;dialogWidth:27;status:no")
  	if (newComment == null)
  		return
  	else
  		eval("document.all.tableComment").value = newComment;
  
  }
  //obsolete//////////
  function filterColumn()
  {
  	if (document.forms[0].selectedColumnId.value == -1){
  		alert ("No Column Selected")
  	} else {
  		var qs = changeQSVar("cc"+document.forms[0].selectedColumnId.value, document.forms[0].columnDefaultValue.value)
  		// alert(qs)
  		location.search = qs
  	}
  }
  /////////////////
  
  function filterColumn2()
    {
    
    	var columnId = eval("document.all.selectedColumnId").value;
    	var columnIdx = eval("document.all.selectedColumnIdx").value;
    	var colTH = eval("document.all.Column"+columnId);
    	var columnName = colTH.name;
    	var columnType = colTH.columnType
    	var column = new Object();
    	column.columnName = columnName;
    	column.columnType = columnType;
    
    
    	var filterObj = null;
    	var filterCondition;
    	var filterExpr;
    	if (columnName != null && columnName != "")
    	{
    		var filterObj = showModalDialog("jsp/collaboration/tables/filterDlg.html", column, "dialogHeight:12;dialogWidth:25;status:no")
    		//alert("Dialog Done")
    	}
    	else
    	{
    		alert ("No Column selected for filtering")
    		return
    	}
    
    	if (filterObj == null)
    	{
    		//alert("filterObj is null")
    		return
    	}
    
    	filterCondition = trim(filterObj.filterCondition)
    	filterExpr = trim(filterObj.filterExpr)
    
    	_filterColumn(columnType, columnIdx, filterCondition, filterExpr)
    
    	// add the filter to the form
    	//var newFCO = document.createElement("input");
    	//newFCO.type="hidden"
    	//EditTable.appendChild(newFCO);
    	//newFCO.name="cc"+columnId;
    	//newFCO.id="cc"+columnId;
    	//newFCO.value = filterCondition+":"+filterExpr
    	if (filterArray == null)
  	{ 
  		filterArray = new Array();
  	}
  	var filterObj = new Object();
  	filterObj.columnId = columnId;
  	filterObj.filter = filterCondition+":"+filterExpr;
  
  	var nFil = filterArray.length;
  	filterArray[nFil] = filterObj;
    
  }
  
  function _filterColumn(columnType, columnIdx, filterCondition, filterExpr)
  {
  	var bwTableContents = eval("document.all.bwTableContents")
  	var bwTableContentRows = bwTableContents.rows
  
  	var i
  
  	if (columnType == "STRING" || columnType == "TABLE")
  	{
  		var regExp = /(\w+):\/\/(\S*)/;
  		var negativeMatch;
  
  		if (filterCondition == "equals")
  		{
  			regExp = new RegExp(filterExpr);
  			negativeMatch = false
  		}
  		else if (filterCondition == "doesNotEqual")
  		{
  			regExp = new RegExp(filterExpr);
  			negativeMatch = true;
  
  		}
  		else if (filterCondition == "beginsWith")
  		{
  			regExp = new RegExp("^"+filterExpr+"[\s\S]*");
  			negativeMatch = false
  		}
  		else if (filterCondition == "doesNotBeginWith")
  		{
  			regExp = new RegExp("^"+filterExpr+"[\s\S]*");
  			negativeMatch = true
  		}
  		else if (filterCondition == "endsWith")
  		{
  			regExp = new RegExp("[\s\S]*"+filterExpr+"$");
  			negativeMatch = false
  		}
  		else if (filterCondition == "doesNotEndWith")
  		{
  			regExp = new RegExp("[\s\S]*"+filterExpr+"$");
  			negativeMatch = true
  		}
  		else if (filterCondition == "contains")
  		{
  			regExp = new RegExp("[\s\S]*"+filterExpr+"[\s\S]*");
  			negativeMatch = false
  		}
  		else if (filterCondition == "doesNotContain")
  		{
  			regExp = new RegExp("[\s\S]*"+filterExpr+"[\s\S]*");
  			negativeMatch = true
  		}
  
  
  		for(i = 0; i < bwTableContentRows.length; i++)
  		{
  			var cellsForRows = bwTableContentRows[i].cells
  			if (cellsForRows[columnIdx].tagName == "TD")
  			{
  				var cellValue;
  				if(cellsForRows[columnIdx].children(0).tagName == "SELECT")
  				{
  				    var selObj = cellsForRows[columnIdx].children(0)
  			 	    cellValue = trim(selObj.options[selObj.selectedIndex].value)
  				}
  				else
  				{
  				   cellValue = trim(cellsForRows[columnIdx].innerText)
  				}
  				var result = cellValue.match(regExp);
  				if (negativeMatch == false)
  				{
  					if (result == null )
  					{
  						bwTableContentRows[i].style.display="none"
  					}
  				}
  				else
  				{
  					if (result != null )
  					{
  						bwTableContentRows[i].style.display="none"
  					}
  
  				}
  
  			}
  		}
  	}
  	else if (columnType == "INTEGER" || columnType == "FLOAT")
  	{
  
  
  		for(i = 0; i < bwTableContentRows.length; i++)
  		{
  			var cellsForRows = bwTableContentRows[i].cells
  			if (cellsForRows[columnIdx].tagName == "TD")
  			{
  				var cellValue
  				if (columnType == "INTEGER")
  				{
  				    if(cellsForRows[columnIdx].children(0).tagName == "SELECT")
				    {
				        var selObj = cellsForRows[columnIdx].children(0)
				        cellValue = parseInt(selObj.options[selObj.selectedIndex].value)
				        filterExpr = parseInt(filterExpr)
				     }
				     else
  				     {
  					cellValue = parseInt(cellsForRows[columnIdx].innerText)
  					filterExpr = parseInt(filterExpr)
  				     }
  				}
  				else
  				{
				    if(cellsForRows[columnIdx].children(0).tagName == "SELECT")
				    {
					var selObj = cellsForRows[columnIdx].children(0)
					cellValue = parseFloat(selObj.options[selObj.selectedIndex].value)
					filterExpr = parseFloat(filterExpr)
				     }
				     else
  				     {
  					cellValue = parseFloat(cellsForRows[columnIdx].innerText)
  					filterExpr = parseFloat(filterExpr)
  				     }
  				}
  
  				//alert(cellValue+":"+filterExpr)
  
  				if (filterCondition == "equals")
  				{
  					if (cellValue != filterExpr)
  					{
  						bwTableContentRows[i].style.display="none"
  					}
  				}
  				else if (filterCondition == "doesNotEqual")
  				{
  					if (cellValue == filterExpr)
  					{
  						bwTableContentRows[i].style.display="none"
  					}
  
  				}
  				else if (filterCondition == "isGreaterThan")
  				{
  					if (cellValue <= filterExpr)
  					{
  						bwTableContentRows[i].style.display="none"
  					}
  				}
  				else if (filterCondition == "isGreaterThanOrEqualTo")
  				{
  					if (cellValue < filterExpr)
  					{
  						bwTableContentRows[i].style.display="none"
  					}
  				}
  				else if (filterCondition == "isLessThan")
  				{
  					if (cellValue >= filterExpr)
  					{
  						bwTableContentRows[i].style.display="none"
  					}
  				}
  				else if (filterCondition == "isLessThanOrEqualTo")
  				{
  					if (cellValue > filterExpr)
  					{
  						bwTableContentRows[i].style.display="none"
  					}
  				}
  			} // if
  		} //for
  	}// else if
  }

  function openInExcel()
  {
    	var url;
    	var i;
    	
    	url = location.protocol + "//" +
    	      location.hostname + ":"+
    	      location.port +
    	      location.pathname + "?" +
    	      "action=editTable&" +
    	      "excelDump=true&" +
    	      "tableId=" + eval("document.all.tableId").value + "&" +
    	      "ViewPreference=" + eval("document.all.ViewPreference").value + "&" +
    	     "period=" + eval("document.all.period").value + "&" +
    	      "asOfDate=" + eval("document.all.asOfDate").value + "&" +
    	      "compDate=" + eval("document.all.compDate").value ;
    	      
    	// add the column filters
    	//var formElements = EditTable.elements;
    	//for (i = 0; i < formElements.length; i++)
    	//{
    	//	formElementId = formElements(i).id
    	//	regExp = new RegExp("^cc");
    	//	if (formElementId.match(regExp) != null)
    	//	{
    	//		url = url + "&" + formElementId + "=" + formElements(i).value
    	//	}
    	//}
		if (eval("document.all.showDiffSideBySide").value == 'true')
		{
			url = url + "&showDiffSideBySide=true";
		}
		if (eval("document.all.showDiffAbsolute").value == 'true')
		{
			url = url + "&showDiffAbsolute=true";
		}
		if (eval("document.all.showDiffPercent").value == 'true')
		{
			url = url + "&showDiffPercent=true";
		}
    	if ( filterArray == null )
	  	{
	  		filterArray = new Array();
	  	}
		if (eval("document.all.hideUnchangedRows").value == 'true')
		{
			url = url + "&hideUnchangedRows=true";
		}

		if (eval("document.all.hideChangedRows").value == 'true')
		{
			url = url + "&hideChangedRows=true";
		}
		if (eval("document.all.hideNewRows").value == 'true')
		{
			url = url + "&hideNewRows=true";
		}
		if (eval("document.all.hideDeletedRows").value == 'true')
		{
			url = url + "&hideDeletedRows=true";
		}	
    	
		for (i = 0; i < filterArray.length; i++)
    	{
    		url = url + "&cc" + filterArray[i].columnId + "=" + filterArray[i].filter;
    	}
    	
    	var baselineVar = eval("document.all.baselineId").value
    	// alert( baselineVar) 
    	if ( baselineVar > 0 )
    	{
    		url = url + "&baselineId=" + baselineVar
    	
    	}
    	
    	var trackState = eval("document.all.trackState").value
    	// alert( baselineVar) 
    	if ( trackState != null )
    	{
    		url = url + "&trackState=" + true
    	}

    	window.open(url);
    
  }
  
  
  function openInBwsFormat()
    {
      	var url;
      	var i;
      	
      	url = location.protocol + "//" +
      	      location.hostname + ":"+
      	      location.port +
      	      location.pathname + "?" +
      	      "action=bwsFormat&" +
      	      "excelDump=true&" +
      	      "tableId=" + eval("document.all.tableId").value + "&" +
      	      "ViewPreference=" + eval("document.all.ViewPreference").value 
      	      
      	
      	var baselineVar = eval("document.all.baselineId").value
      	// alert( baselineVar) 
      	if ( baselineVar > 0 )
      	{
      		url = url + "&baselineId=" + baselineVar
      	
      	}
      	
      	
      	window.open(url);
      
    }
  
  
  function addBookmark()
    {
    	var url;
    	var i;
    	
    	url = location.protocol + "//" +
    	      location.hostname + ":"+
    	      location.port +
    	      location.pathname + "?" +
    	      "action=editTable&" +
    	      "tableId=" + eval("document.all.tableId").value + "&" +
    	      "ViewPreference=" + eval("document.all.ViewPreference").value 
    	      
    	// add the column filters
    	//var formElements = EditTable.elements;
    	//for (i = 0; i < formElements.length; i++)
    	//{
    	//	formElementId = formElements(i).id
    	//	regExp = new RegExp("^cc");
    	//	if (formElementId.match(regExp) != null)
    	//	{
    	//		url = url + "&" + formElementId + "=" + formElements(i).value
    	//	}
    	//}
    	
    	if ( filterArray == null )
	{
		filterArray = new Array();
	}

    	
    	for (i = 0; i < filterArray.length; i++)
    	{
    		url = url + "&cc" + filterArray[i].columnId + "=" + filterArray[i].filter;
    	}
    	
    	var baselineVar = eval("document.all.baselineId").value
    	// alert( baselineVar) 
    	if ( baselineVar > 0 )
    	{
    		url = url + "&baselineId=" + baselineVar
    	}
    	

	var period = eval("document.all.period").value
	if (period != "null")
	  url = url + "&period=" + period
	var asOfTid = eval("document.all.asOfTid").value
	if (asOfTid != "null")
	  url = url + "&asOfTid=" + asOfTid
	var compTid = eval("document.all.compTid").value
	if (compTid != "null")
	  url = url + "&compTid=" + compTid
	var asOfDate = eval("document.all.asOfDate").value
	if (asOfDate != "null")
	  url = url + "&asOfDate=" + asOfDate
	var compDate = eval("document.all.compDate").value
	if (compDate != "null")
	  url = url + "&compDate=" + compDate
	var trackState = eval("document.all.trackState").value
	if (trackState != "null")
	  url = url + "&trackState=" + trackState
	var showDiffAbsolute = eval("document.all.showDiffAbsolute").value
	if (showDiffAbsolute != "null")
	  url = url + "&showDiffAbsolute=" + showDiffAbsolute	  
	var showDiffPercent = eval("document.all.showDiffPercent").value
	if (showDiffPercent != "null")
	  url = url + "&showDiffPercent=" + showDiffPercent
	var showChangesOnly = eval("document.all.showChangesOnly").value	  
	if (showChangesOnly != "null")
	  url = url + "&showChangesOnly=" + showChangesOnly	
	var showDiffSideBySide = eval("document.all.showDiffSideBySide").value	  
	if (showDiffSideBySide != "null")
	  url = url + "&showDiffSideBySide=" + showDiffSideBySide
	  
    	window.external.AddFavorite(url, document.title)
  }
  
function isInteger(myString) {
    var isInteger = new Boolean()
    isInteger=true
    var myChar=""
    var myInt=0

    if (myString!="" && typeof(myString)=="string") {
	for (i=0;i<myString.length;i++) {
	    myChar=myString.charAt(i)
	    myInt=parseInt(myChar)

	    if (isNaN(myInt)) {
		isInteger=false
	    }
	}

    } else {
	isInteger=false
    }

    return isInteger
}

  function setBucketSize()
  {
 
  	var url;
  	var i;
  	
  	var bucketSize = eval("document.all.bucketSize").value
  	if (!isInteger(bucketSize))
  	{
  		alert ("Please enter a valid number");
  		return;
  	}
  	
  	// set cookie
  	var expdate = new Date();
	expdate.setTime(expdate.getTime()+(9999*24*60*60*1000));
	var c = "boardwalktable"+eval("document.all.tableId").value+"bucketSize=" + bucketSize+"; expires="+expdate.toUTCString(); 
	// write cookie
	document.cookie = c;

  	
  	url = location.protocol + "//" +
  	      location.hostname + ":"+
  	      location.port +
  	      location.pathname + "?" +
  	      "action=editTable&" +
  	      "tableId=" + eval("document.all.tableId").value + "&" +
  	      "ViewPreference=" + eval("document.all.ViewPreference").value 
  	// add the column filters
  	
    	if ( filterArray == null )
	{
		filterArray = new Array();
	}

    	
    	for (i = 0; i < filterArray.length; i++)
    	{
    		url = url + "&cc" + filterArray[i].columnId + "=" + filterArray[i].filter;
    	}
    	
    	var baselineVar = eval("document.all.baselineId").value
    	// alert( baselineVar) 
    	if ( baselineVar > 0 )
    	{
    		url = url + "&baselineId=" + baselineVar
    	}
    	

	var period = eval("document.all.period").value
	if (period != "null")
	  url = url + "&period=" + period
	var asOfTid = eval("document.all.asOfTid").value
	if (asOfTid != "null")
	  url = url + "&asOfTid=" + asOfTid
	var compTid = eval("document.all.compTid").value
	if (compTid != "null")
	  url = url + "&compTid=" + compTid
	var asOfDate = eval("document.all.asOfDate").value
	if (asOfDate != "null")
	  url = url + "&asOfDate=" + asOfDate
	var compDate = eval("document.all.compDate").value
	if (compDate != "null")
	  url = url + "&compDate=" + compDate
	var trackState = eval("document.all.trackState").value
	if (trackState != "null")
	  url = url + "&trackState=" + trackState
	var showDiffAbsolute = eval("document.all.showDiffAbsolute").value
	if (showDiffAbsolute != "null")
	  url = url + "&showDiffAbsolute=" + showDiffAbsolute	  
	var showDiffPercent = eval("document.all.showDiffPercent").value
	if (showDiffPercent != "null")
	  url = url + "&showDiffPercent=" + showDiffPercent
	var showChangesOnly = eval("document.all.showChangesOnly").value	  
	if (showChangesOnly != "null")
	  url = url + "&showChangesOnly=" + showChangesOnly	
	var showDiffSideBySide = eval("document.all.showDiffSideBySide").value	  
	if (showDiffSideBySide != "null")
	  url = url + "&showDiffSideBySide=" + showDiffSideBySide

    	
    	// add the bucket number
    	url = url + "&bucketNum=0" + "&bucketSize=" + bucketSize
    	
    	location = url
  }

  function setBucket(bno)
  {
  	var currentBNO = eval("document.all.bucketNumber").value;
  	var bucketSize = eval("document.all.currentBucketSize").value
  	if (bno == -1)
  	{
  		bno = parseInt(currentBNO) - 1;
  	}
  	else if (bno == -2)
  	{
  		bno = parseInt(currentBNO) + 1;
  	}
  	var url;
  	var i;
  	
  	url = location.protocol + "//" +
  	      location.hostname + ":"+
  	      location.port +
  	      location.pathname + "?" +
  	      "action=editTable&" +
  	      "tableId=" + eval("document.all.tableId").value + "&" +
  	      "ViewPreference=" + eval("document.all.ViewPreference").value 
  	// add the column filters
    	if ( filterArray == null )
	{
		filterArray = new Array();
	}

    	
    	for (i = 0; i < filterArray.length; i++)
    	{
    		url = url + "&cc" + filterArray[i].columnId + "=" + filterArray[i].filter;
    	}
    	
    	var baselineVar = eval("document.all.baselineId").value
    	// alert( baselineVar) 
    	if ( baselineVar > 0 )
    	{
    		url = url + "&baselineId=" + baselineVar
    	}
    	

	var period = eval("document.all.period").value
	if (period != "null")
	  url = url + "&period=" + period
	var asOfTid = eval("document.all.asOfTid").value
	if (asOfTid != "null")
	  url = url + "&asOfTid=" + asOfTid
	var compTid = eval("document.all.compTid").value
	if (compTid != "null")
	  url = url + "&compTid=" + compTid
	var asOfDate = eval("document.all.asOfDate").value
	if (asOfDate != "null")
	  url = url + "&asOfDate=" + asOfDate
	var compDate = eval("document.all.compDate").value
	if (compDate != "null")
	  url = url + "&compDate=" + compDate
	var trackState = eval("document.all.trackState").value
	if (trackState != "null")
	  url = url + "&trackState=" + trackState
	var showDiffAbsolute = eval("document.all.showDiffAbsolute").value
	if (showDiffAbsolute != "null")
	  url = url + "&showDiffAbsolute=" + showDiffAbsolute	  
	var showDiffPercent = eval("document.all.showDiffPercent").value
	if (showDiffPercent != "null")
	  url = url + "&showDiffPercent=" + showDiffPercent
	var showChangesOnly = eval("document.all.showChangesOnly").value	  
	if (showChangesOnly != "null")
	  url = url + "&showChangesOnly=" + showChangesOnly	
	var showDiffSideBySide = eval("document.all.showDiffSideBySide").value	  
	if (showDiffSideBySide != "null")
	  url = url + "&showDiffSideBySide=" + showDiffSideBySide

    	
    	
    	// add the bucket number
    	url = url + "&bucketNum=" + bno+"&bucketSize=" + bucketSize
    	
    	location = url
}

function sendEmail(to, cc, bcc, subject, body) 
{
   
   
   
    // BUILD MAIL MESSAGE COMPONENTS 
    var doc = "mailto:" + to + 
        "?cc=" + cc + 
        "&bcc=" + bcc + 
        "&subject=" + escape(subject) + 
        "&body=" + encodeURIComponent(body);
  	
    // POP UP EMAIL MESSAGE WINDOW
    window.location = doc;
}
  function emailURL()
    {
    	var url;
    	var i;
    	
    	url = location.protocol + "//" +
    	      location.hostname + ":"+
    	      location.port +
    	      location.pathname + "?" +
    	      "action=editTable&" +
    	      "tableId=" + eval("document.all.tableId").value + "&" +
    	      "ViewPreference=" + eval("document.all.ViewPreference").value 
    	      
    	// add the column filters
    	var formElements = EditTable.elements;
    	for (i = 0; i < formElements.length; i++)
    	{
    		formElementId = formElements(i).id
    		regExp = new RegExp("^cc");
    		if (formElementId.match(regExp) != null)
    		{
    			url = url + "&" + formElementId + "=" + formElements(i).value
    		}
    	}
    	var tableName = eval("document.all.tableName").value
    	var message =  ' Use the following link to access the table \n ' + '<a href="'+ url+'">'+tableName+'</a>';
    	sendEmail('', '<%=userName%>', ' ', 'Boardwalk Table Link', message)
	
    	
    
  }
  
  function logout()
  {
  	location.assign('MyCollaborations?action=logout');
  }
  
  function showTableProperties()
  {
  
  	var props = new Object();
  	props.tableName = '<%=tbi.getTableName()%>';
  	props.description = '<%=tbi.getTablePurpose()%>';
  	props.collab =  '<%=tbi.getCollaborationName()%>';
  	props.whiteboard = '<%=tbi.getWhiteboardName()%>';
  	props.neighborhood = '<%=tbi.getNeighborhood()%>';
  	props.formTableId = '<%=formTableId%>';
  	props.criteriaTableId = '<%=criteriaTableId%>';
  	var ret = showModalDialog("html/propertiesDlg.html", props, "dialogHeight:20;dialogWidth:25;status:no")
  	
  	if (ret == null)
  	{
  			return
  	}
  	else
  	{
  		if ( props.tableName != ret.tableName || props.description != ret.description || props.formTableId != ret.formTableId || props.criteriaTableId != ret.criteriaTableId)
  		{
  				
  
  			var url = location.protocol + "//" +
  				      location.hostname + ":"+
  				      location.port +
  				      location.pathname + "?" +
  				      "action=" + "commitTableProperties" + "&" +
  				      "tableId=" + eval("document.all.tableId").value + "&" +
  				      "tableName=" + ret.tableName + "&" +
  				      "tableDescr=" +  ret.description + "&" +
  				      "ViewPreference=" + eval("document.all.ViewPreference").value + "&" +
  				      "formTableId=" + ret.formTableId + "&" + 
  				      "criteriaTableId=" + ret.criteriaTableId;
  			
  				      
  			location.replace(url);
  		}		
  	}
  
  }
  
  
  function showRecordsReferingToSelectedCell()
  {
  	var selectedCellIdForQuery = eval("document.all.selectedCellId").value;
  	
  	if ( currentSelectedType == "CELL" )
  	{
  			
			var cellId = "document.all.cell_"+selectedCellIdForQuery;
			var selectedCellTd = eval(cellId);
			var cellColumnId = selectedCellTd.columnId;
			var colTH = eval("document.all.Column"+cellColumnId);
  			var columnName = colTH.name;
  			var cellValue = eval("document.all.Cell"+eval("document.all.selectedCellId").value).value;
  			var url = location.protocol + "//" +
						      location.hostname + ":"+
						      location.port +
						      location.pathname + "?" +
						      "action=" + "showLkpReferences" + "&" +
						      "tableId=" + eval("document.all.tableId").value + "&" +
						      "lkpColumnName=" + columnName + "&" +
						      "lkpColumnId=" +  cellColumnId + "&" +
						      "lkpValue=" + cellValue;
			location.replace(url);
	}
  
  }
  
    function showRecordsThisCellisReferingTo()
    {
        
    	var selectedCellIdForQuery =eval("document.all.selectedCellId").value;      	
      	if ( currentSelectedType == "CELL" )
      	{
      			
    			var selectedCellTd = eval("document.all."+selectedCellIdForQuery);
    			var cellColumnId = selectedCellTd.columnId;
    			var colTH = eval("document.all.Column"+cellColumnId);
      			var columnName = colTH.name;
      			var cellValue = eval("document.all.Cell"+eval("document.all.selectedCellId").value).value;
      			
    			var url = location.protocol + "//" +
			      				      location.hostname + ":"+
			      				      location.port +
			      				      location.pathname + "?" +
			      				      "action=" + "editTable" + "&" +
			      				      "tableId=" + colTH.getLookupTableId + "&" +
			      				      "cc"+ colTH.getLookupColumnId +"=equals:"+   cellValue;
				      
    			location.replace(url);
	}    
  }
  
  
  
  function insertColumn(forUpdate)
  {	
  	var action = 'commitColumnAndDone';
  	var props = new Object();
  	var selectedColumnId = eval("document.all.selectedColumnId").value;
  	var previousColumnDefaultValue;
  	if (forUpdate == true)
  	{
  		action = 'updateColumn';
  		if (selectedColumnId < 0 )
  		{
  			alert("Please select a column to update");
  			return;
  		}
  		var colTH = eval("document.all.Column"+selectedColumnId);
  		props.forUpdate = true;
  		props.columnName = colTH.name;
  		props.columnType = colTH.columnType;
  		props.columnDefaultValue = colTH.defaultValue;
  		props.orderedTableName = colTH.orderedTableName;
  		props.orderedTableId = colTH.orderedTableId;
  		props.orderColumnId = colTH.orderColumnId;
  		props.lookupTableId = colTH.lookupTableId;
  		props.lookupTableName = colTH.lookupTableName;
  		props.lookupColumnId = colTH.lookupColumnId;
  		previousColumnDefaultValue = colTH.defaultValue;
  
  	}
  	else
  	{
  		props.forUpdate = false;		
  	}
  	
  	if ( cellDirtyFlag == true )
  	{
  		alert(" You have made some changes that have not been saved. \n If you wish to save these changes, cancel out of the column dialog box and save your changes");
  	}
  	
  	
  	var col = showModalDialog("html/updateColumnDlg.html", props, "dialogHeight:17;dialogWidth:22");
  	
  	if ( col == null )
  	{
  		return;
  	}
  	
  	var columnName = col.columnName;
  	var columnType = col.columnType;
  	var columnDefaultValue = col.columnDefaultValue;
  	var lookupTableId = col.lookupTableId;
  	var lookupTableColumnId = col.lookupTableColumnId;
  	var trackLevelTableId = col.trackLevelTableId;
  	var trackLevelTableColumnId = col.trackLevelTableColumnId;
  	
  	
  	
  	
  	var url = location.protocol + "//" +
  	      location.hostname + ":"+
  	      location.port +
  	      location.pathname + "?" +
  	      "action=" + action + "&" +
  	      "tableId=" + eval("document.all.tableId").value + "&" +
  	      "columnName=" + columnName + "&" +
  	      "columnType=" + columnType + "&" +
  	      "columnDefaultValue=" + columnDefaultValue + "&" +		     
  	      "selectedColumnId=" + selectedColumnId + "&" +
  	      "lookupTableId=" + lookupTableId + "&" +
  	      "lookupTableColumnId=" + lookupTableColumnId + "&" +
  	      "trackLevelTableId=" + trackLevelTableId + "&" +
  	      "trackLevelTableColumnId=" + trackLevelTableColumnId + "&" +
  	      "ViewPreference=" + eval("document.all.ViewPreference").value;
  	if ( forUpdate == true )
  			url = url + "&previousColumnDefaultValue=" + trim ( previousColumnDefaultValue );			      
  	url = trim(url)
  	location.replace(url);
  	
  
  }
  
  function trackChanges()
  {
  	var action = 'sendQuery';
  	var args = new Object();
  	args.currentSelectedType = currentSelectedType;
  	args.selectedColumnId = eval("document.all.selectedColumnId").value;
  	
  	var query = showModalDialog("html/trackChangeDlg.html", args, "dialogHeight:17;dialogWidth:20;status:no");
  	
  	var trackChangeObject = currentSelectedType
  	
  	if ( query == null )
  	{
  		return;
  	}
  	
  	
  	if ( trackChangeObject == "TABLE" &&  eval("document.all.selectedColumnId").value != -1 )
  		{
  			if ( eval("document.all.Column" + eval("document.all.selectedColumnId").value ).orderType == "NORMAL" )
  			{
  				trackChangeObject = "TABLE_DELTA_COLUMN"
  			}
  	}
  	
  	
  	var url = location.protocol + "//" +
  	      location.hostname + ":"+
  	      location.port +
  	      location.pathname + "?" +
  	      "action=" + action + "&" +
  	      "tableId=" + eval("document.all.tableId").value + "&" +
  	      "startDate=" + query.startDate + "&" +
  	      "endDate=" + query.endDate + "&" +
  	      "trackingPeriod=" + query.trackingPeriod + "&" +
  	      "cellValueLike=" + query.cellValueLike + "&" +
  	      "trackChangeObject=" + trackChangeObject + "&" +
  	      "ViewPreference=" + eval("document.all.ViewPreference").value + "&" +
  	      "QueryPreference=" + eval("document.all.QueryPreference").value + "&" +
  	      "selectedCellId=" + eval("document.all.selectedCellId").value + "&" +
  	      "selectedColumnId=" + eval("document.all.selectedColumnId").value 
       
  	location.assign(url)
  }


  
  var zoom = 100;
  function zoomTable(in_out)
  {
  	if (in_out == 'in') 
  	{	
  		zoom = zoom +10
  	}
  	else
  	{
  		zoom = zoom - 10
  	}
  	
  	eval("document.all.bwTableContents").style.zoom = zoom +"%"
  	eval("document.all.bwHeadings").style.zoom = zoom +"%"	
  
  }

	var SelCellId = 0;
	var allValues ;

	function selectNewValue(columnId,cellId)
	{
		HideLookUp();
		var cellValueFromTable = '';
		var frmTD = eval("document.all.cell_"+cellId);
		cellValue = document.getElementById('tdcelldiv'+cellId).innerText;

		SelCellId		= cellId;
		var objName		= 'strVal'+columnId;
		var tableValues = document.EditTable.elements['strVal'+columnId].value;

		allValues	= tableValues.split("^");
	
		document.getElementById('div'+cellId).style.visibility = 'visible';

		var liSelSize	= allValues.length;

		if(liSelSize  > '10')
			liSelSize = '10';
		else if(liSelSize  < '2')
			liSelSize = '2';

//		liSelSize = liSelSize - 1

		document.EditTable.elements['selcellId'+cellId].size = liSelSize;
		document.EditTable.elements['selcellId'+cellId].options.length = 0;
		CurrentSelection = -1;

		for(i=0; i<allValues.length; i++)
		{ 
			if(trim(cellValue) == allValues[i])
				CurrentSelection = i
			document.EditTable.elements['selcellId'+cellId].options[i] = new Option(allValues[i],i);
		}

		document.EditTable.elements['selcellId'+cellId].selectedIndex=CurrentSelection;
	}

	function setInJsp()
	{
		var SelIndexValue = document.EditTable.elements['selcellId'+SelCellId].selectedIndex;
		var SelectedValue = document.EditTable.elements['selcellId'+SelCellId].options[SelIndexValue].value;

		var frmTD = eval("document.all.cell_"+SelCellId);
		document.getElementById('tdcelldiv'+SelCellId).innerText = allValues[SelectedValue];

		SelectedCellId			= "Cell" + SelCellId
		SelectedCellIdDirtyFlag = "dirtyCell" + SelCellId

		document.all.item(SelectedCellId).value = allValues[SelectedValue]
		document.all.item(SelectedCellIdDirtyFlag).value = "true"

		document.getElementById('div'+SelCellId).style.visibility = 'hidden';
	}

