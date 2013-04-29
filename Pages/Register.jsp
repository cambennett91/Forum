<HTML>
<HEAD>
	<TITLE>Forum - Register</TITLE>
	<LINK rel="stylesheet" href="mystyle.css">
</HEAD>
	<%
	String regUserName = (String)session.GetAttribute("regUserName");
	if(regUserName == null) regUserName = "";
	String regUserNameErrorMessage = (String)session.GetAttribute("regUserNameErrorMessage");
	if(regUserNameErrorMessage == null) regUserNameErrorMessage = "";
	String regPasswordErrorMessage = (String)session.GetAttribute("regPasswordErrorMessage");
	if(regPasswordErrorMessage == null) regPasswordErrorMessage = "";
	%>
	<BODY>
	<CENTER>
	    <FORM action='./servlet/Controller?action=register' method='post'>
		<H3>Forum - Register User</H3></P>
		<IMG src='underline.png' alt='underline' height='26' width='242' /></P>
		
		<TABLE width='35%' border='0' bgcolor='#A8A8A8'>
			<TR><TD>&nbsp;</TD></TR>
			<TR>
				<TD align='right'>User name:&nbsp;</TD>
				<TD><INPUT type='text' name='regUserName' value='<%=regUserName%>' size='30' maxlength='30' /></TD>
			</TR>
			<TR><TD>&nbsp;</TD><TD><FONT color='red'><%=regUserNameErrorMessage%></FONT></TD></TR>
			<TR>
				<TD align='right'>Password:&nbsp;</TD>
				<TD><INPUT type='text' name='regPassword' value='' size ='30' maxlength='30'/></TD>
			</TR>
			<TR><TD>&nbsp;</TD><TD><FONT color='red'><%=regPasswordErrorMessage%></FONT></TD></TR>
			<TR>
				<TD></TD>
				<TD><INPUT type='reset' value='Clear'/>&nbsp;<INPUT type='submit' value='Register'/></TD>
			</TR>
		</TABLE>
		
		<IMG src='underline.png' alt='underline' height='26' width='242' /></P>
		<A href="./Login.jsp">Back</A>
		</FORM>
</BODY>
</HTML>