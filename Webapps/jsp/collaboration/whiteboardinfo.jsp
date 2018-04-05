<td width="200" valign="top">
            
             
             <!--start table for info-->
              <table width="160" border="1" cellspacing="0" cellpadding="5" align="center" class="small-text"  bgcolor="#eeeeee">
                <tr valign="top"> 
                  <td class="small-text"><b>Login: </b>
                    <%=userName%>
                    <br>
                    <br>
                    <b>Collaboration :</b> <a href="MyCollaborations?action=editCollab&collabId=<%=wbInfo.getCollaborationId()%>"><%=wbInfo.getCollaborationName()%></a>
                    <br>
                    <br>
                 
                    <b>Purpose:</b> <%=wbInfo.getCollaborationPurpose()%>
                    <br><br>                     
                    <b>Whiteboard :</b> <a href="Whiteboard?wbid=<%=wbInfo.getWhiteboardId()%>&action=edit&collabId=<%=wbInfo.getCollaborationId()%>"> <%=wbInfo.getWhiteboardName()%> </a>
                    <br>
                    <br>
                 
                     <b>Access:</b>
                            <br><b>Private access :</b> <%=wbInfo.getCollaborationPrivateAccessLevel()%>  
                            <br><b>Peer Access: </b><%=wbInfo.getCollaborationPeerAccessLevel()%> 
                            <br><b>Friend Access: </b><%=wbInfo.getCollaborationFriendAccessLevel()%>                      
                     <br><br>
                 
                     <b>Your Access:</b> <%=wbInfo.getCollaborationAccessLevel()%>
                    
                      <br><br>
                 
                      <b>Neighborhood:</b> <%=wbInfo.getNeighborhood()%>
					 
              <br>
                    </td>
                </tr>
              </table>
              <!--end table for info-->
            <br>
              </td>