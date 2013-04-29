<HTML>
<HEAD>
	<TITLE>Forum - Confirm Message</TITLE>
	<LINK rel="stylesheet" href="mystyle.css">
</HEAD>
<BODY>
	<%
	String userName = (String)session.GetAttribute("userName");
	String userPicture = (String)session.GetAttribute("userPicture");
	String userComment = (String)session.GetAttribute("userComment");
	String threadTopic = (String)session.GetAttribute("threadTopic");
	%>
	<CENTER><img src='underline.png' alt='underline' height='26' width='242'/>
	<H3><FONT SIZE =+3><B>Confirm Message</B></FONT></H3><P>

	<TABLE BORDER=1 ALIGN="RIGHT">
		<TR><TD>Logged in as:&nbsp;<%=userName%></BR><IMG SRC="./images/<%=userPicture%>"></TD></TR>
		<TR><TD><A HREF="./servlet/Controller?action=logout">Log Out</A></TD></TR></TABLE>
		<FONT SIZE=+1 COLOR=GREEN>Comment:</FONT></A></BR>
		<%=userComment%>
	<!--</BR></BR></BR></BR></BR></BR></BR></BR></BR></BR></BR></BR>-->
	<TABLE BORDER=0>
		<TR>
			<FORM METHOD=POST ACTION="./servlet/Controller?action=addcomment">
				<TD align=center><INPUT TYPE=SUBMIT VALUE="Add Comment"></TD>
			</FORM>
			<FORM METHOD=POST ACTION="./servlet/Controller?action=detail&id=<%=threadTopic%>">
				<TD align=center><INPUT TYPE=SUBMIT VALUE="Edit Comment"></TD>
			</FORM>
		</TR>
		<TR>
			<FORM METHOD=POST ACTION="./servlet/Controller?action=detail&id=<%=threadTopic%>">
			<TD align=right><INPUT TYPE=SUBMIT VALUE="Back To Message List"></TD>
			</FORM>
		</TR>
	</TABLE>
	<img src='underline.png' alt='underline' height='26' width='242'/>
	</CENTER>
</BODY>
</HTML>

