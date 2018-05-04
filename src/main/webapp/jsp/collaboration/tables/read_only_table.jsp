<%@ page import ="com.boardwalk.database.Transaction"%>
<script language="javascript">
<%@ include file="/jscript/sortTable.js" %>
</script>

<div align="left" >
<b><u><%=tbi.getTableName()%></u></b>
<br><br>
Description: <%=tbi.getTablePurpose()%>
<br><br>
</div>

        <!--start main page table-->
        <table border="0" cellspacing="0" cellpadding="0">

         <thead>
	<tr name="bwHeadings">


       		<%


       							Transaction maxRowTransaction = null;
					    		String versionCellUserName  = "";
					    		String lastUpdate = "";
					    		int userRows = 0;
		                                   Iterator j = columnNames.iterator();
		                                   int i = 0;
		                                    while (j.hasNext())
		                                    {
		                                            String columnName = (String)j.next();
		                                            %>
		                                            <th name="<%=columnName%>" class="BWTableHead"
		                                            ondblclick="this.blur(); return sortTable('bwTable',  <%=i %>, true);" ><%=columnName%>
		                                            </th>
		                                            <%
		                                            i++;
		                                     }

		                                     %>

		                                     		<th name="User" class="BWTableHeadUserDetails"
						     		                                            ondblclick="this.blur(); return sortTable('bwTable',  <%=i %>, true);" >User
		                                            </th>
		                                            <th name="Last Update" class="BWTableHeadUserDetails"
													    ondblclick="this.blur(); return sortTable('bwTable',  <%=i %>, true);" >Last Update
							    </th>








               </tr>
	       </thead>
	       <tbody id="bwTable">
		<%


						for (  i = 0 ; i < rowIds.size(); i++ )
					    {
					    		Integer rowId = (Integer)rowIds.elementAt(i);
					    		Vector vectorOfRowsForRowid =        (Vector) cellsByRowId.get(rowId);
							System.out.println("no of rows " + rowIds.size());
						

					    		for ( userRows = 0; userRows< vectorOfRowsForRowid.size() ; userRows++ )
					    		{

					    		Vector cellsInARow = (Vector)vectorOfRowsForRowid.elementAt(userRows);
                                            %>
                                            		<tr>
									    <%


											    for ( int c = 0 ;c < cellsInARow.size(); c++ )
											    {
											    	
											    
												VersionedCell vcell = (VersionedCell)cellsInARow.elementAt(c);




											//	System.out.println( "cell value " + vcell.getValueAsString() );

												String value =  " ";


												if ( vcell.getType().equals("TABLE") )
												{
													System.out.println("Table id = " + vcell.getValueAsString() );
													value = vcell.getTableName();
													System.out.println("Table Name = " + value );
												}
												else
												{
													value = vcell.getValueAsString();
												}

												if ( value != null && ( value.equals("") || value.equals(" ")  ) )
												{
													value = "_";
												}
												else
												if ( value == null )
												{
													value = "_";
												}


					    						if (maxRowTransaction == null)
					    						{
					    							maxRowTransaction = vcell.getTransaction();
					    						}
												if ( maxRowTransaction.getId() < vcell.getTransaction().getId() )
												{
													maxRowTransaction = vcell.getTransaction();

												}




									    %>
											<td class="InputBox"  ><%=value%> </td>
									<%
                                          						}


									  %>
														<td class="InputBox"  ><%=maxRowTransaction.getCreatedByUserAddress()%> </td>
														<td class="InputBox"  ><%=maxRowTransaction.getCreatedOn()%> </td>

									<%
												maxRowTransaction = null;


                                        				%>
                                       				  </tr>

                                       <%
                                       		  }




                                       	    }
                                        %>

            				 </tbody>


        </table>



