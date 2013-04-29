import java.io.*;            // for IOException and PrintWriter
import javax.servlet.*;      // for general servlets
import javax.servlet.http.*; // for http (web) servlets
import java.sql.*;           // for the JDBC
import java.util.Date;		 // to use dates
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import benne.forum.*;		// Custom package for this project
import java.util.*;			// Extra bits and pieces

/**
 * This servlet provides the Controller for
 * a web application. It catches user requests,
 * calls the database for information, then redirects
 * to JSP or HTML pages for presentation of that
 * information.
 * @author Errol Chopping, Cameron Bennett
*/

public class Controller extends HttpServlet
{
	// Accessing the database, and error checking
    ServletContext context;
    String dataBaseName,driver, driverClass ,directory,databaseURI;
    String errorMessage;
	// Our model for the Model/View/Controller display system
    Model model;


//--------------------------------------------------------
/**
 * Sets values for database access; the driver,
 * the application path, the database filename etc.
 */
  public void init()
  {
  	// Build database connection string...
    context = getServletContext();
    driverClass = "sun.jdbc.odbc.JdbcOdbcDriver";
    dataBaseName = "Forum.mdb";
    driver = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)}";
    directory = context.getRealPath("/");
    databaseURI = driver + ";DBQ=" + directory + "\\" + dataBaseName;
    errorMessage = "";
    
    // Create and initialise Model
    model = new Model();
    model.setDriver( driverClass );
    model.setConnectionString( databaseURI );
  }
  

//--------------------------------------------------------    
/**
 * The service is the 'conductor of the orchestra'
 * it catches and distributes the work to other
 * methods. All requests come here first.
 */
  public void service(HttpServletRequest request,
                      HttpServletResponse response)
                      throws IOException, ServletException
  {
	HttpSession session = request.getSession( true );
	
    session.setAttribute("login_errormsg", "");// reset the errormsg for use
	
	String action = request.getParameter( "action" );
    if( action == null )
      action = "home";
    action = action.toLowerCase();
    
    Boolean loggedin = (Boolean)session.getAttribute("loggedin");
    if(loggedin == null)
    	loggedin = false;
    session.setAttribute("loggedin", loggedin);
    
    if(loggedin.equals(false))
    	goHome( request, response, session );
    
    // Default login page
    else if( action.equals( "home" ))
      goHome( request, response, session );
    
    // Clears session data and goes back to the login page
    else if(action.equals("logout"))
    	doLogout(request, response, session);
    
    // adds a new user to the database
    else if(action.equals("register"))
    	doRegister(request, response, session);
    
    // used by the search algorithm
    else if( action.equals( "home" ))
    	doSearch( request, response, session );
    
    // search comments / list all topics
    else if( action.equals( "search" ) || action.equals( "listall" ) )
      doSearch( request, response, session );
    
    // Creates a topic/thread for comments to be added
    else if(action.equals("create"))
    	doCreateTopic(request, response, session);
    
    // view comments in a topic
    else if( action.equals( "detail" ) )
      doDetail( request, response );
    
    else if(action.equals("postcomment"))
    	postComment(request, response, session);
    
    // clicked on "Submit comment"
    else if( action.equals( "addcomment" ) )
      addComment( request, response );
    else
      showError( request, response, session, "Illegal action '" + action + "'" );
  }// end service
  
  /*
   * Takes the user to a Confirm page where they can confirm or cancel posting their comment
  */
//--------------------------------------------------------
  public void postComment(HttpServletRequest request,
                      HttpServletResponse response,
                      HttpSession session)
                      throws IOException, ServletException
  {   
	  // Get the topic that we are currently in
	  TopicBean detailTopic = (TopicBean)session.getAttribute("detailTopic");
	  String comment = request.getParameter("comment");
	  //  Write the comment and topic ID to the session for JSP use
	  session.setAttribute("comment", comment);
	  session.setAttribute("topic", detailTopic.getId());
	  // Redirect to the JSP
	  response.sendRedirect("../Post.jsp");
  }
  //--------------------------------------------------------
  public void addComment(HttpServletRequest request,
                      HttpServletResponse response)
                      throws IOException, ServletException
  {   
	HttpSession session = request.getSession( true );  
    
	String comment = (String)session.getAttribute( "comment" );
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy K:mm:ss a");
	String date = dateFormat.format(new Date());
	String picture = (String)session.getAttribute("Userpicture");
	
    if( comment != null && comment.trim().length() != 0 )
    {
    	String Username = (String)session.getAttribute("Username");
    	if(Username == null)
    	{
    		response.sendRedirect("../Login.jsp");
    		session.setAttribute("login_errormsg", "Error - Not logged in");
    	}
      TopicBean bean = (TopicBean)session.getAttribute( "detailTopic" );
      
      
      model.addTopicComment( Username, comment, picture, date, bean );
      
      session.setAttribute("comment", "");
      response.sendRedirect( "../Topics.jsp");
      return;
    }
    showError( request, response, session, "No comment was added." );
  } 
//--------------------------------------------------------
/**
 * Fetches a single Topic from the database,
 * and all the comments for this thread
 * stores the Topic in a bean and redirects to the JSP
 * which shows the bean detail.
 */
  public void doDetail(HttpServletRequest request,
                      HttpServletResponse response )
                      throws IOException, ServletException
  {
    // Make SQL from parameter...
    String TopicID = request.getParameter( "id" );
    HttpSession session = request.getSession( true );
    
    // if no new id, see if there's a previous one for this client...
    if( TopicID == null )
    {
      TopicBean bean = (TopicBean)session.getAttribute( "detailTopic" );
      if( bean != null )
        TopicID = bean.getId();
      else
      {
        showError( request, response, session, "Can't detail a movie without the ID" );
        return;
      }
    }
	
    // Delegate to model
    TopicBean bean = model.getTopic( TopicID );
    
   
    // Place bean in memory...
    session.setAttribute( "detailTopic", bean );
    
    // Hand over to the JSP
    response.sendRedirect("../Messages.jsp" );
    
  }
  /**
   * Searches through the topics
   */
//-------------------------------------------------------- 
  public void doSearch(HttpServletRequest request,
                      HttpServletResponse response,
                      HttpSession session )
                      throws IOException, ServletException
  {

    String keyword = (String)request.getParameter( "keyword" );
    String sort = (String)request.getParameter("sort");
    // if the sorting keyword is not one of 3 predefined sorts, set it to the default
    if(sort == null) sort = "Id";
    if(sort.equals("Title") == false && sort.equals("Creator") == false)
    	sort = "Id";
    
    if(keyword == null || keyword.equals("null"))
    {
    	keyword = "";
    }
      
    // Delegate low-level work to model...
    List topicList = model.searchTopics( keyword, sort );
    
    
    // Place the collection into memory...
    session.setAttribute( "topiclist", topicList );
    
    // Forward to JSP for the presentation...
    response.sendRedirect("../Topics.jsp" );
    
  }
 
//--------------------------------------------------------
/**
 * Forward request to application home page
 **/
  public void goHome( HttpServletRequest request,
                      HttpServletResponse response,
                      HttpSession session )
                      throws IOException, ServletException
  {
	  String name = (String)request.getParameter("login_name");
	  String password = (String)request.getParameter("login_password");
	  session.setAttribute("login_name", name);
	  
	  Boolean valid = false;
	  String errormsg = "";
	  session.setAttribute("login_errormsg", errormsg);
	  
	  valid = model.searchUsers( name, password );
	  
	  
	  if(valid == false)
	  {
		  session.setAttribute("loggedin", false);
		  session.setAttribute("login_errormsg", "Data is invalid, try again");
		  response.sendRedirect("../Login.jsp" );
	  }
	  else if(valid == true)
	  {
		  session.setAttribute("Username", name);
		  session.setAttribute("loggedin", true);
		  session.setAttribute("Userpicture", model.searchUserpicture(name));
		  session.setAttribute("login_errormsg", "");
		  doSearch(request, response, session);
	  }
  }
  /**
   * Logs the user out of the forum
   */
  public void doLogout( HttpServletRequest request,
          HttpServletResponse response,
          HttpSession session )
          throws IOException, ServletException
  {
	  session.setAttribute("Username", null);
	  session.setAttribute("login_errormsg","Successfully logged out");
	  session.setAttribute("loggedin", false);
	  response.sendRedirect("../Login.jsp" );
  }
  /**
   * Create a  topic that will hold messages
   */
  public void doCreateTopic( HttpServletRequest request,
          HttpServletResponse response,
          HttpSession session )
          throws IOException, ServletException
  {
	  String title = (String)request.getParameter("title");
	  String creator = (String)session.getAttribute("Username");
	  
	  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy K:mm:ss a");
	  String date = dateFormat.format(new Date());

	  // If there is no title, reload the page and don't create a topic
	  if( title == null || title.trim().length() == 0 )
		  response.sendRedirect("../Topics.jsp" );
	  else
		  model.addTopic( title, creator, date.toString() );
	    
	  response.sendRedirect("../servlet/Controller?action=listall" );
  }
  
  // Register a user into the database so that they can view topics/messages and create new topics/messages
  public void doRegister( HttpServletRequest request,
		  HttpServletResponse response,
		  HttpSession session )
		  throws IOException, ServletException
{
	String Username = (String)request.getParameter("reg_username");
	String Password = (String)request.getParameter("reg_password");
	String addUser = ""; //error checking string
	session.setAttribute("reg_username", Username);
	
	addUser = model.createUser(Username, Password);
	
	if(addUser.equals("username"))
	{
		session.setAttribute("reg_errormsg_user", "User name invalid or taken");
		session.setAttribute("reg_errormsg_password", "");
	}
	else if(addUser.equals("password"))
	{
		session.setAttribute("reg_errormsg_user", "");
		session.setAttribute("reg_errormsg_password", "Passowrd invalid");
	}
	else if(addUser.equals("both"))
	{
		session.setAttribute("reg_errormsg_user", "User name invalid or taken");
		session.setAttribute("reg_errormsg_password", "Passowrd invalid");
	}
	else// if(addUser.equals(""))
	{
		session.setAttribute("reg_errormsg_user", "User added!");
		session.setAttribute("reg_errormsg_password", "");
	}
	response.sendRedirect("../Register.jsp");
}
//--------------------------------------------------------
/**
 * Very rudimentary error delivery method!
 */
  public void showError( HttpServletRequest request,
                      HttpServletResponse response,
                      HttpSession session, String msg )
                      throws IOException
  {
    response.setContentType( "text/html" );
    PrintWriter out = response.getWriter();
    out.println( "<HTML><HEAD><TITLE>Error</TITLE></HEAD><BODY>" );
    out.println( msg );
    out.println( "</BODY></HTML>" );
  }  
}// end servlet
