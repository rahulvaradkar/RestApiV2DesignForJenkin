function populateFromTo() {
    for (var Current=0;Current < document.formName.nonmembers.options.length;Current++)
    {
        if (document.formName.nonmembers.options[Current].selected)
        {
            var defaultSelected = false, selected = false;

            var optionName = new Option(document.formName.nonmembers.options[Current].text, document.formName.nonmembers.options[Current].value, defaultSelected, selected)
	    
	    if (replacedfirst)
	            var length = document.formName.PARAM_USER_IDS.length;
            else
                var length = 0;
                
            document.formName.PARAM_USER_IDS.options[length] = optionName;

            document.formName.nonmembers.options[Current]=null;

            replacedfirst = true;
        }
    }
    
    
}


function populateToFrom()
{
    for (var Current=0;Current < document.formName.PARAM_USER_IDS.options.length;Current++)
    {
        if (document.formName.PARAM_USER_IDS.options[Current].selected)
        {
            var defaultSelected = false, selected = false;

            var optionName = new Option(document.formName.PARAM_USER_IDS.options[Current].text, document.formName.PARAM_USER_IDS.options[Current].value, defaultSelected, selected)

            if (replacedfirst)
                var length = document.formName.nonmembers.length;
            else
                var length = 0;
            document.formName.nonmembers.options[length] = optionName;

            document.formName.PARAM_USER_IDS.options[Current]=null;

            replacedfirst = true;
        }
    }
}

function selectAll()
{
	for (var Current=0;Current < document.formName.PARAM_USER_IDS.options.length;Current++)
	{
		if ( document.formName.users.value == "" )
		{
			document.formName.users.value = document.formName.PARAM_USER_IDS.options[Current].value;
		}
		else
		{
			document.formName.users.value = document.formName.users.value + "," + document.formName.PARAM_USER_IDS.options[Current].value;
		}
	}
	
	
	document.formName.CMD.value = "CMD_SAVE_NEW_MEMBERS";
}

var replacedfirst = false;

function updateUserList()
{
    winodw.location.href="http://64.188.40.10:8080/boardwalk/servlets/createMemberServlet"
}
