//added by shirish on 2/29/08
function redirectPath(path)
{
	var context = path.split("/");
	url = "MyCollaborations?action=logout&context="+context[context.length-1];
	//alert(url);
    location.assign(url);
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

function getStatusPeriod()
{
	nhid=document.all.selNhid.value;	
	url="MyCollaborations?action=statusReport&selNhid="+nhid+" ";
    location.assign(url);
}
function getActivityPeriod()
{
  nhid=document.all.selNhid.value;
  var action = 'activityReport';
  var args = new Object();
  //args.context = "collabActivity"
    args.context = "tableCompare"
  var query = showModalDialog("html/activityPeriodDlg.html", args, "dialogHeight:12;dialogWidth:22");

  var url = location.protocol + "//" +
      location.hostname + ":"+
      location.port +
      location.pathname + "?" +
      "action=" + action + "&" +
      "period=" + query.period + "&" +
      "startDate=" + query.startDate + "&" +
      "endDate=" + query.endDate + "&" +
	  "selNhid=" + nhid		
  location.assign(url)
}

function MsgOkCancel() 
{ 
	var fRet; 
	fRet = confirm('Are you sure?'); 
	return fRet;
} 
function makeLogin()
{
document.forms[0].action.value="login"
document.forms[0].submit()
}
function setActionAndSubmit(doNext)
{
    document.forms[0].action.value = doNext
    document.forms[0].submit()
}

function checkCheckBoxsetActionAndSubmit(doNext)
{
    if ( currentCheckboxCount == 1 )
    {

    	document.forms[0].action.value = doNext
    	document.forms[0].submit()
    }
    else
    {
    	alert("Please make a selection before executing this function")
    
    }
    	
}


function switchCurrentMembership(memberId)
{
	document.forms[0].action.value ="switchCurrentMembership"
	document.forms[0].switchMembershipTo.value =memberId	
	document.forms[0].submit()
}

function popupAboutUs()
{

	alert( "Boardwalk Application Engine \nVersion 4.1 \nCopyright Boardwalktech, Inc \nAll Rights Reserved \n \nFor additional information, please contact: \ninfo@boardwalktech.com \n \nFor support issues, please contact \nsupport@boardwalktech.com");
}

function invokeInvitationManager()
{
	checkisInvitationPresent();
}

function checkisInvitationPresent()
{
	var url = "jsp/admin/getErrorsForNHCollabWB.jsp?typeNumber=3";
	xmlHttp = GetXmlHttpObject(stateChangedForInvitationManager);
	xmlHttp.open("GET", url , true) ;
	xmlHttp.send(null) ;
}
function stateChangedForInvitationManager() 
{ 
	if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
	{ 
		callBackisInvitationPresent(xmlHttp.responseText);
	} 
} 
function callBackisInvitationPresent(asResponse)
{
	var retval = asResponse ;
	if(trim(retval) == "true") 
	{
		url = "InvitationManager";
		parent.location.href = url;
		return;	
	}
	else 
	{
			alert("No Templates have been set up")
				return;	
			
	}
}
function trim(str) 
{ 
	str = str.replace(/^\s*/, '').replace(/\s*$/, ''); 
	return str;
} 

function GetXmlHttpObject(handler)
{ 
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

// POP UP A PREFORMATTED EMAIL MESSAGE WINDOW
function popupEmailClient(to, cc, bcc, subject, body) {
 
 var bwlink = "<a href=" + document.URL + " >Boardwalk Link</a>";


  // BUILD MAIL MESSAGE COMPONENTS 
  var doc = "mailto:" + to + 
      "?cc=" + cc + 
      "&bcc=" + bcc + 
      "&subject=" + escape(subject) + 
      "&body=" + encodeURIComponent(body) + encodeURIComponent( bwlink);


  // POP UP EMAIL MESSAGE WINDOW
  window.location = doc;
}






var previousCheckbox = null;
var currentCheckboxCount = 0;


function checkBoxCheck( currentCheckbox )
{
	if(document.all.selectedTableId != null)
	{
		document.all.selectedTableId.value = currentCheckbox.value
	}
		
		//alert(document.all.wbid.value)
		//alert(document.all.neighborhoodId.value)
		//alert(document.all.ViewPreference.value)
	if( previousCheckbox != null )
	{
		
		if ( previousCheckbox.value != currentCheckbox.value )	
		{
			previousCheckbox.checked = false
			previousCheckbox = currentCheckbox
			currentCheckboxCount = 1
			
			
		}
		else
		{
			if ( currentCheckbox.checked == false )
			{
			    previousCheckbox = null
			    currentCheckboxCount = 0
			}
			
			
		}
	}
	else
	{
		previousCheckbox = currentCheckbox
		currentCheckboxCount = 1
		
	}
	
}



function checkViewPreference( currentCheckbox )
{
	if ( currentCheckbox.name == "ViewLatest" )
	{
		document.all.Item("ViewLatestByUser").checked = false
	}
	else
	{
		document.all.Item("ViewLatest").checked = false
	}	
}