<HTML>
<HEAD>
	<TITLE>Forum - Create Message</TITLE>
	<LINK rel="stylesheet" href="mystyle.css">
</HEAD>
<BODY>
	<%@page import="benne.forum.*"%>
	<%
	String userName = (String)session.GetAttribute("userName");
	String userPicture = (String)session.GetAttribute("userPicture");
	TopicBean detailTopic = (TopicBean)session.GetAttribute("detailTopic");
	String userComment = (String)session.GetAttribute("userComment");
	if(userComment == null) message = "";
	%>

	<CENTER><img src='underline.png' alt='underline' height='26' width='242'/>
	<H3><FONT SIZE =+3><B><%=detailTopic.getTitle()%></B></FONT></H3><P>
	<TABLE BORDER=1 ALIGN="RIGHT">
	<TR><TD>Logged in as:&nbsp;<%=userName%></BR><IMG SRC="./images/<%=userPicture%>"></TD></TR>
	<TR><TD><A HREF="./servlet/Controller?action=logout">Log Out</A></TD></TR></TABLE>

	<FONT COLOR=BLUE>
	<TABLE BORDER=1 CELLPADDING=10 WIDTH="80%">
	Created by:&nbsp;<%=detailTopic.GetCreator()%> on&nbsp;<%=detailTopic.GetCreated()%></BR>
	Post count:&nbsp;<%=detailTopic.GetCommentCount()%>
	</FONT>
	</P>
	<A HREF="./servlet/Controller?action=listall"><FONT COLOR=RED>Back to topics list</FONT></A>
	<HR>
	<FONT SIZE=+1 COLOR=GREEN>Comments:</FONT></A>
	<%String messageError = (String)session.GetAttribute("messageError");
	if(messageError == null) messageError = "";
	for( int i = 0; i < detailTopic.GetCommentCount();i++ )
	{
		CommentBean comment = detailTopic.GetComment(i);
	%>
	<TR>
		<TD><FONT COLOR=BLUE><%=comment.GetName()%></FONT></BR><%=com.getDate()%></BR>
			<IMG SRC="./images/<%=comment.GetPicture()%>">
		</TD>
		<TD>
			<FONT SIZE=-1><%=comment.GetComment()%><BR></FONT>
		</TD>
	</TR>
	<%
	} // End of the FOR loop for comments
	%>
	</TABLE>
	<FORM METHOD=POST ACTION="./servlet/Controller?action=postcomment">
	<TABLE BORDER=0>
		<FONT SIZE=+1 COLOR=GREEN>Add a comment:</FONT>
		</BR><FONT COLOR=RED SIZE=-1><%=messageError%></FONT>
		<TR>
			<TD align=right>Comment:<TD align=center><TEXTAREA NAME=comment ROWS=7 COLS=34><%=userComment%></TEXTAREA>
		</TR>
		<TR>
			<TD><TD align=center><INPUT TYPE=SUBMIT VALUE="Add Comment">
			</FORM>
			<FORM METHOD=POST ACTION="./Topics.jsp">
			<INPUT TYPE=SUBMIT VALUE="Back To Topics"></TD>
			</FORM>
		</TR>
	</TABLE>
	<img src='underline.png' alt='underline' height='26' width='242'/>
	</CENTER>
</BODY>
</HTML>

