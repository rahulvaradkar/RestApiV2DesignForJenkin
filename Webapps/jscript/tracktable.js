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