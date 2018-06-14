<%@ page isThreadSafe="false" %>
<%@ page import ="java.util.*,com.boardwalk.collaboration.*, com.boardwalk.whiteboard.*, com.boardwalk.table.*,java.io.*" %>

<%
Vector wbTableList = (Vector)request.getAttribute("wbTables");
int   collabId   = Integer.parseInt(request.getParameter("collabId"));
CollaborationTreeNode ct = (CollaborationTreeNode)wbTableList.elementAt(0);
//request.setAttribute("heading", ct.getName() );
%>

<%@include file='/jsp/common/header.jsp' %>
<%@include file='/jsp/common/menubar.jsp' %>
<%@include file='/jsp/common/heading.jsp' %>
<%@include file='/jsp/common/error_message.jsp' %>

<script>

	function checkCheckBoxsetActionAndSubmit(doNext)
	{
		
		var count1=document.forms[0].WBCount.value;
		var foundChecked = false;
		
		for(i= 0; i<=count1; i++)
		{
			
			var obj = eval("document.forms[0].wbid"+i);
			

			if (obj != null && obj.checked ) 
				{
					foundChecked = true;
					document.forms[0].wbid.value = obj.value;
					document.forms[0].action.value = doNext;
					if(doNext == "delete")
					{
						if(MsgOkCancel() == true)
							validateWBid(obj.value)

						else
							return;

					}
					else
					{
						document.forms[0].submit();
						return;	
					}
				}		
		}

		if ( doNext !='addNewWhiteboard'   &&  foundChecked == false )
			{
				alert( 'Please make a selection before making a request')
			}
			else
		{
				if(doNext != "delete")
				{
				document.forms[0].wbid.value = -1;
				document.forms[0].action.value = doNext;
				document.forms[0].submit();
				}
		}
	}


	function validateWBid(wbid)
	{

		var url = "jsp/admin/getErrorsForNHCollabWB.jsp?wbid="+wbid+"&typeNumber=1";
		xmlHttp = GetXmlHttpObject(stateChangedForWB); 
		xmlHttp.open("GET", url , true) ;
		xmlHttp.send(null) ;
	}


	function stateChangedForWB() 
	{ 
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
		{ 
			callBackvalidateWBid(xmlHttp.responseText);
		} 
	} 


	function callBackvalidateWBid(asResponse)
	{
	
		retvalues=asResponse.split("^");
		if(trim(retvalues[1]) == "delete" ) 
		{
			
			document.forms[0].submit();
			return;
		}
		else 
		{
				if(trim(retvalues[0]) == "wb")
					{
						alert("You do not have the Access to Delete Whiteboard")
						return;
					}
		}
		
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
</script>

<tr> 

    <td>

        <!--start main page table-->
        <table  border="0" cellspacing="0" cellpadding="0"  class="body">

          <tr> 
            <td valign="top">
                <!--start main page table 2 -->

		<table border="0" cellspacing="0" cellpadding="0"  valign="top">
	         <b><%=ct.getName()%></b>
	         <br>
	         Description: <%=ct.getPurpose()%>
	         
	          <tr valign="top" bgcolor="#FFCC66"> 
                      <td bgcolor="#FFCC66" height="2" ><img src="images/clear.gif" height="2"></td>
                    </tr>	
                    <tr valign="top"> 
                        <td class="body" align=center> 
                            <br>
                            <br>
                        <table  border="0" cellspacing="2" cellpadding="2" align="center" class="body">
                               <form method="get" action="Whiteboard">

                                <!--start table for table-->
	<tr bgcolor="#fddeb9" height=20> 
       		<td class="body"> <b>Select </b></td>
                <td class="body"> <b>Whiteboard </b></td>

                <td class="body"> <b>Table</b> </td>
             
                      </tr>
<!-- Get all the tables in the database and list them here--> 

                


<%
     boolean grey = false;

Vector Whiteboards = (Vector) ct.getWhiteboards();

int wbCount=Whiteboards.size();
System.out.println("value of whiteboard@@@@@@@@@@2" +wbCount);



if ( Whiteboards.size() > 0 )
{
	
	
	for (int w=0; w < Whiteboards.size(); w++ )
	{

	    WhiteboardTreeNode wb = (WhiteboardTreeNode)Whiteboards.elementAt(w);
		
	    int wbid = wb.getId();
            String wbname =wb.getName();
            
            Vector tables = (Vector)wb.getTables();


         String rowColorCode = "white";
          if ( grey == true )
            {
                rowColorCode = "#eeeeee";
                grey = false;
            }
          else
            {
                grey = true;
            }
%>

<tr bgcolor="<%=rowColorCode%>" > 
                  <td class="body" width="30" rowspan="<%=tables.size()%>">

                    <input id="wbid<%=w%>" onClick="javascript:checkBoxCheck(this)" type="checkbox" name="wbid" value="<%=wbid%>">
                  </td>

                  <td valign="top" rowspan="<%=tables.size()%>">
                   <a href="Whiteboard?wbid=<%=wbid%>&action=edit&collabId=<%=collabId%>"> 
                    <%=wbname%></a>
                    </td>
<%
// loop over the tables in the whiteboard

Iterator i = tables.iterator ();

int count=0;

while (i.hasNext()) 
{
   TableTreeNode  t = (TableTreeNode)i.next();
   if (count != 0) {
    
   
%>

                <tr bgcolor="<%=rowColorCode%>" > 
<%
} // if count != 0
%>
                  <td>
               <a href="MyTables?action=editTable&tableId=<%=t.getId()%>&wbid=<%=wbid%>&ViewPreference=<%=t.getDefaultViewPreference()%>" ><%=t.getName()%></a></td>

                 
                </tr>

<%
    count++;
    } // end loop over tables
} // end loop over whiteboards

}
%>
</tr>
                                                            
            <tr bgcolor="#cccccc"> 
                        <td class="body" colspan="4" align="center"> 
							<b>          
                
                <a href="javascript:setActionAndSubmit('addNewWhiteboard')">Add Whiteboard</a> |
                <a href="javascript:checkCheckBoxsetActionAndSubmit('edit')">Edit </a> |
                <!--- <a href="javascript:checkCheckBoxsetActionAndSubmit('bwsFormat')"> Link To Excel  </a> | -->
                <a href="javascript:checkCheckBoxsetActionAndSubmit('delete')">Delete</a> |
                   
                <a href="javascript:setActionAndSubmit('createBaseline')">Create Baseline</a>
                
                        </td>
            </tr> 
			<tr>
		<td>
			<input type="hidden" name="WBCount" value="<%=wbCount%>">
		</td>
	</tr>
<input type="hidden" name="action">
<input type="hidden" name="collabId" value=<%=collabId%>>


             <%@include file='/jsp/common/commonparameters.jsp' %> </form>
        </table>        
        <!--end table for table-->
        
             <td width="2" valign="top" bgcolor="#FFCC66"><img src="images/clear.gif" width="2" height="2"></td>
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
<%@include file='/jsp/common/footer.jsp' %>
