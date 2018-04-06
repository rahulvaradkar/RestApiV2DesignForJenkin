<%@ page isThreadSafe="false" %>
<%@ page import ="java.lang.Integer, java.io.*, java.util.*, com.boardwalk.whiteboard.Whiteboard, com.boardwalk.table.Table" %>

<%
String cellForUpdateField = request.getParameter("bwcellId");
String htmlId  = request.getParameter("htmlid");
String urlName  = request.getParameter("urlName");
String url  = request.getParameter("url");


String screentip  = request.getParameter("screentip");


String title = "Create a Hyperlink";
String heading = " Create a Hyperlink";

%>



<html>
<head>

<title><%=title%></title>



<script language="javascript">


    function setTableAndSubmit(urlName,url,screentip) {


    	  urlName = eval("document.all.urlName").value
    	  url = eval("document.all.url").value
    	  screentip = eval("document.all.screentip").value

    	  dirtyCellid = "dirtyCell"+"<%=cellForUpdateField%>"
    	  inputCellid = "Cell"+"<%=cellForUpdateField%>"
    	  prevCellid  = "pCell"+"<%=cellForUpdateField%>"




	if (  url == null || url == "")
	{
		  opener.document.all.item("<%=htmlId%>").children(0).innerHTML =opener.document.all.item("<%=htmlId%>").children(0).innerText
		  opener.document.all.item(inputCellid).value =opener.document.all.item("<%=htmlId%>").children(0).innerText
		  opener.document.all.item(dirtyCellid).value=true
		  opener.cellDirtyFlag = true
		  self.close()
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

	  opener.document.all.item("<%=htmlId%>").children(0).innerHTML = ATag
	  opener.document.all.item(inputCellid).value =ATag
	  opener.document.all.item(dirtyCellid).value=true
	  opener.cellDirtyFlag = true
	  self.close()
    }




        function closeWindow() {

	        	  self.close()
        }


function browseURLS( )
{
  var browseURL
  var url = eval("document.all.url").value
  if ( url == null || url =="")
  {
   	browseURL = window.open('../../../MyCollaborations','chooseURL','toolbar=yes,location=yes,scrollbar=yes,resizable=yes,menubar=no', false)
   }
   else
   {
   	browseURL= window.open (url,'chooseURL','toolbar=yes,location=yes,scrollbar=yes,resizable=yes,menubar=no', false)
   }

   alert( 'In the  window that was opened select a web address and click OK')

   if ( browseURL )
   {
		   eval("document.all.url").value  = browseURL.location.href
		   browseURL.close()
   }
   else
   {
   	alert( 'Boardwalk failed to get the web address as the window is closed')
   }
}


</script>

<meta http-equiv="Content-Type" content="text/html;">
<META HTTP-EQUIV="EXPIRES"  CONTENT="0">
<META HTTP-EQUIV="PRAGMAS" CONTENT="NO-CACHE">

</head>




    <LINK REL=STYLESHEET TYPE="text/css"
      HREF="css/stylesheet.css" TITLE="stylesheet">



<body bgcolor="#FFFFFF">


<table border="0" cellspacing="0" cellpadding="0" class="body">
  <tr>
  <td valign="top">

<tr>
  <br>
  <td colspan="2" height="20"  class="page-heading"><b> <%=heading%></b></td>
  </tr>





<tr>

    <td>

        <!--start main page table-->
        <table width="680" border="0" cellspacing="0" cellpadding="0" align="left" class="body">

          <tr>
            <td valign="top">
                <!--start main page table 2 -->

		<table width="500" border="0" cellspacing="0" cellpadding="0" align="left" valign="top">
                    <tr valign="top" bgcolor="#FFCC66">
                      <td height="2" width="500"><img src="images/clear.gif" width="500" height="2"></td>
                    </tr>

                    <tr valign="top">
                        <td class="body" align=left>
                            <br>

                            <br>
                        <table width="450" border="0" cellspacing="2" cellpadding="2" align="left" class="body">
                              <form method="post" action="MyTables">


                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20>
       		<td class="body"> <b> Fields </b></td>
                <td class="body"> <b>Values </b></td>


                 </tr>


                <tr bgcolor="#eeeeee">
                  <td valign="top" >URL Name</td><td> <input type=text id="urlName" value="<%=urlName%>" ></input></td>

                  </tr>
                <tr bgcolor="#eeeeee">
		                  <td valign="top" >Screentip</td>
		                  <td><input type=text id="screentip"   value="<%=screentip%>"  ></input></td>
                  </tr>

		       <tr  bgcolor="#eeeeee">
		       		 <td valign="top"  >Hyperlink</td>
		  		  <td >
		  		  <input type="text" value="<%=url%>" id="url" ></input>
		  		  <input type="button" value="Web Page..." id="browseURL" onClick="javascript:browseURLS()" ></input>
		  		  </td>
		  	</tr>
		  	<tr   bgcolor="#eeeeee" >
		  		<td colspan=3>
		  		<small> If you are entering a file here, please make sure you enter file:// before the file path <small>
		  		</td>
		  	</tr>
    <tr   align=center bgcolor="#cccccc">
	 <td colspan=2>
	  	 <a href="javascript:setTableAndSubmit()">OK</a> |
	  	  <a href='javascript:document.all.item("url").value=""; setTableAndSubmit()'>Remove Link</a> |
	  	  <a href="javascript:closeWindow()">Cancel</a>
	  </td>

	  </tr>







        </table>
        <td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td>
        <!--end table for table-->
        </tr>

     </table>
              <!--end table for main page table 2 -->


            </td>





                   </tr>
        </table>

        <!--start main page table -->

    <br>
    </td>

  </tr>
<br>
<tr><td height="20">&nbsp;</td></tr>
<tr>
    <td align="left" height="18" bgcolor="#999999" valign="top">
      <font size="1" face="Verdana,Arial, Helvetica, sans-serif" color="white"><b>Powered by boardwalk Copyright Boardwalktech Inc.</b></font>
    </td>
  </tr>
</td>
</tr>


















