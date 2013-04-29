<HTML>
<HEAD>
	<TITLE>Forum - List Topics</TITLE>
	<LINK rel="stylesheet" href="mystyle.css">
</HEAD>
<BODY>
	<%@page import="java.util.*"%>
	<%@page import="benne.forum.*"%>
	<%
	String userName = (String)session.GetAttribute("userName");
	String userPicture = (String)session.GetAttribute("userPicture");
	%>
	<CENTER><img src='underline.png' alt='underline' height='26' width='242'/>
	<H3><FONT SIZE =+3><B>Topics</B></FONT></H3><P>

	<TABLE BORDER=1 ALIGN="RIGHT">
		<TR><TD>Logged in as:&nbsp;<%=userName%></BR><IMG SRC="./images/<%=userPicture%>"></TD></TR>
		<TR><TD><A HREF="./servlet/Controller?action=logout">Log Out</A></TD></TR>
	</TABLE>
	</BR>
	<FORM ACTION="./servlet/Controller?action=search" METHOD=POST>
		Search for a topic:<INPUT NAME=keyword></BR>
		Sort by:<select name=sort>
		<option value=none>-Default-</option>
		<option value=Id>ID</option>
		<option value=Title>Title</option>
		<option value=Creator>Creator</option>
		</select>
		<INPUT VALUE="Search" TYPE=SUBMIT></BR>
		<A HREF="./servlet/Controller?action=listall">Show all topics</A></BR>
		Select a topic from the list by clicking on its title!
	</FORM>
	<TABLE BORDER=1>
		<TR>
			<TD>ID</TD><TD>Title</TD><TD>Created by</TD><TD>Created on</TD></TR>
			<%List list = (List)session.GetAttribute( "topicList" );
			if( list.size() == 0 )
			{%>
			<FONT COLOR=RED>Sorry, no titles match your request</FONT>
			<%
			return;}
			for( int i = 0; i < list.size(); i++ ) {
			  TopicBean bean = (TopicBean)list.get(i);
			  out.println("<TR align=center>");
			  out.println("<TD>" + bean.GetId() + "</TD>");
			%>
			<TD><A HREF="./servlet/Controller?action=detail&id=<%=bean.GetId()%>"><%=bean.GetTitle()%></A></TD>
			<TD></BR><%=bean.GetCreator()%></TD>
			<TD></BR><%=bean.GetCreated()%></TD>
		</TR>
		<%
		} // End of the FOR loop
		%>
	</TABLE>
	Add a topic to the forum:</BR>
	<FORM ACTION="./servlet/Controller?action=create" METHOD=POST>
		Title:  <INPUT NAME=title value=""><INPUT VALUE="Create!" TYPE=SUBMIT></BR></BR>
		<A HREF="./servlet/Controller?action=listall">(Need to refresh page to see it)</A>
	</FORM>
	<img src='underline.png' alt='underline' height='26' width='242' />
	</CENTER>
</BODY>
</HTML>