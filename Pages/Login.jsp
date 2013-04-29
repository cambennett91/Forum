<HTML>
	<%
		String loginName = (String)session.getAttribute("loginName");
		String loginError = (String)session.getAttribute("loginError");
		if(loginName == null)
			loginName = "";
		if(loginError == null)
			loginError = "";
	%>
<HEAD>
	<TITLE>ITC357 Assessment 2 - Forum</title>
	<LINK rel="stylesheet" href="mystyle.css">
</HEAD>
<BODY>
	<CENTER>
	<FORM action='./servlet/Controller?action=home' method='post'>
	<IMG src='underline.png' alt='underline' height='26' width='242' /></p>
	<TABLE width='20%' border='0' bgcolor='#A8A8A8'>
	<TR>
		<TD align='right'>Name:&nbsp;</td>
		<TD>
			<INPUT type='text' name='loginName' value='<%=loginName%>' size='16' maxlength='20'/>
		</TD>
	</TR>
	<TR>
		<TD align='right'>Password:&nbsp;</td>
		<TD>
			<INPUT type='password' name='loginPassword' value='' size ='16' maxlength='20'/>
		</TD>
	</TR>
	</TABLE>
	
	<FONT color='red'><%=loginError%></FONT></BR>
	<INPUT type='submit' value='Login'/> </BR>
	<IMG src='underline.png' alt='underline' height='26' width='242' />
	</P>
	<A href="./Register.jsp">Register</a>
	</FORM>
</BODY>
</HTML>