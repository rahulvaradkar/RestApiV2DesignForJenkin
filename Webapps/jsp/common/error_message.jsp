<%@ page import ="java.util.*, com.boardwalk.exception.BoardwalkException, java.io.*" %>

<%
BoardwalkException bwe = (BoardwalkException)request.getAttribute("com.boardwalk.exception.BoardwalkException");

//System.out.println("getting the exception if any ");

String errorMessage = "";

if  (bwe!= null)
{
%>
<script>
	alert("<%=bwe.getMessage()%>\n<%=bwe.getPotentialSolution()%>");
</script>
<%
}
%>
