package benne.forum;

import java.sql.*;
import java.util.*;


public class Model
{
	String errorMessage;
	String driverClass;
	String databaseURI;
	
	// Constructor
	public Model()
	{ }
	
	// Setters
	public void setDriver( String driver )
	{
		driverClass = driver;
	}
	
	public void setConnectionString( String constr )
	{
		databaseURI = constr;
	}
	
	// Getters
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	
//-------------------------------------------------------  
/**
 * A utility method holding basic JDBC code.
 * The incoming SQL query is executed on the database
 * and the result set returned to the caller. If an error
 * occurs during the access, the method sets the errorMessage
 * attribute and returns a null result set.
 **/
  public ResultSet JDBCQuery( String sql )
  {
    try
    {
      Class.forName( driverClass );
    }
    catch( ClassNotFoundException cnfex )
    {
      errorMessage = "JDBC: Cannot find database driver";
      return null;
    }
    
    Connection connect = null;
    try
    {
      connect = DriverManager.getConnection( databaseURI );
    }
    catch( SQLException sqlx )
    {
      errorMessage = "JDBC: Can't create a connection using " + databaseURI;
      return null;
    }
    
    Statement statement = null;
    try
    {
      statement = connect.createStatement();
    }
    catch( SQLException sqlx )
    {
      errorMessage = "JDBC Can't create a statement";
      return null;
    }
    
    ResultSet rs = null;
    try
    {
      rs = statement.executeQuery( sql );
    }
    catch( SQLException sqlx )
    {
      errorMessage = "JDBC: Can't collect results";
      return null;
    }
    
    return rs;
  }// query
  
  
	public int JDBCInsert( String insert )
	{
	try
	{
		Class.forName( driverClass );
	}
	catch( ClassNotFoundException cnfex )
	{
		errorMessage = "JDBC: Cannot find database driver";
		return -1;
	}
	
	Connection connect = null;
	try
	{
		connect = DriverManager.getConnection( databaseURI );
	}
	catch( SQLException sqlx )
	{
		errorMessage = "JDBC: Can't create a connection using " + databaseURI;
		return -1;
	}
	
	Statement statement = null;
	try
	{
		statement = connect.createStatement();
	}
	catch( SQLException sqlx )
	{
		errorMessage = "JDBC Can't create a statement";
		return -1;
	}
	
	int numRows = 0;
	try
	{
		numRows = statement.executeUpdate( insert );
		statement.close();
	}
	catch( SQLException sqlx )
	{
		errorMessage = "JDBC: Can't collect results";
		return -1;
	}
	
	return numRows;
	
	}// insert
  
  
  	/*
  	 * Collect all movies that match the given keyword
  	 **/
	public List searchTopics( String keyword, String sort )
	{
		String sql = "SELECT * from Topics";
		if( keyword != null && keyword.trim().length() > 0 )
			sql += " WHERE Title LIKE '%" + keyword + "%'";
		
		try
		{
			ResultSet rs = JDBCQuery( sql );
	
			// Copy all records into objects and stick
			// the objects into a Collection...
			List topicList = new ArrayList();
			while( rs.next() )
			{
				TopicBean bean = new TopicBean();
				bean.setId( "" + rs.getInt( "ID" ) );
				bean.setTitle( rs.getString( "Title" ) );
				bean.setCreator( rs.getString( "Creator" ) );
				bean.setCreated( rs.getString( "Created" ) );
				
				topicList.add( bean );
			}//while
			
			rs.close();
			return topicList;
		}
		catch( Exception ex )
		{
			return null;
		}
	}// searchTopics

	public Boolean searchUsers( String name, String password )
	{
		String sql = "SELECT * from Users";

		try
		{
			ResultSet rs = JDBCQuery( sql );
			
			while( rs.next() )
			{
				if(rs.getString( "Username" ).equals(name) &&
				   rs.getString( "Password" ).equals(password)
				   )
				return true;
			}//while
			rs.close();
			return false;
		}
		catch( Exception ex )
		{
			return false;
		}
	}// searchUsers
	
	public String searchUserpicture( String name )
	{
		String sql = "SELECT * from Users";

		try
		{
			ResultSet rs = JDBCQuery( sql );
			
			while( rs.next() )
			{
				if(rs.getString( "Username" ).equals(name) )
					return rs.getString("Picture");
			}//while
			rs.close();
			return "";
		}
		catch( Exception ex )
		{
			return "";
		}
	}// searchUsers
	
	/* Fetch a movie given its ID and collection all its comments.
	 * Return movie to client or null if an error.
	 **/
	public TopicBean getTopic( String TopicID )
	{
		
		// Load the movie details...
		String sql = "SELECT * from Topics WHERE ID=" + TopicID;
		ResultSet rs = JDBCQuery( sql );
		
		TopicBean bean = new TopicBean();
		try
		{
			if( rs.next() )
			{
				bean.setId( "" + rs.getInt( "ID" ) );
				bean.setTitle( rs.getString( "Title" ) );
				bean.setCreator( rs.getString( "Creator" ) );
				bean.setCreated( rs.getString( "Created" ) );
				rs.close();
			}// if

			// Now collect any comments for this movie
			rs = JDBCQuery( "SELECT * from Comments WHERE TopicID=" + TopicID );
		
			while( rs.next() )
      		{
		        CommentBean comment = new CommentBean();
		        comment.setId( "" + rs.getInt( "ID" ) );
		        comment.setName( rs.getString( "Name" ) );
		        comment.setDate(rs.getString("Created"));
		        comment.setPicture(rs.getString("Picture"));
		        String temp = rs.getString( "Comment" );
		        comment.setComment( temp );
		        bean.addComment( comment );
      		}
      		
      		return bean;  // with compliments
      
      
		}catch( Exception ex )
		{
			return null;  
		}
	}//getMovie
	
	
	public void addTopicComment(String name, String comment, String picture, String date, TopicBean bean )
	{
		String sql = "INSERT INTO Comments (Name, Comment, Picture, Created, TopicID) "
                  + "VALUES ('"+name+"', '"+comment+"', '"+picture+"', '"+date+"'," + bean.getId() + ")";
      	
      	JDBCInsert( sql );
	}
	
	public void addTopic(String title, String creator, String date)
	{
		String sql = "INSERT INTO Topics (Title, Creator, Created) "
                  + "VALUES ('"+title+"', '"+creator+"', '"+date+"')";
      	
      	JDBCInsert( sql );
	}
	
	public String createUser(String name, String password)
	{
		String sql = "SELECT * from Users";
		String error = "";
		// Check username isn't currently taken
		try
		{
			ResultSet rs = JDBCQuery( sql );
			while( rs.next() )
			{
				if(rs.getString( "Username" ).equals(name))
				{
					error = "username";
					return error;
				}
			}//while
			rs.close();
		}
		catch( Exception ex )
		{
			error = "username";
			return error;
		}
		
		// Check to make sure there is actually a username
		if(name.trim().length() == 0)
		{
			error = "username";
			return error;
		}
		// Check to make sure there is actually a password
		else if(password.trim().length() == 0)
		{
			error = "password";
			return error;
		}	
		else
		{
			String insert = "INSERT INTO Users (Username, Password) "
	            + "VALUES ('"+name+"', '"+password+"')";
			JDBCInsert( insert );
			
			if (searchUsers(name, password) == true)
				return "";
			else
				return "both";
		}
	}
	
}//model