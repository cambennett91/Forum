package benne.forum;
import java.util.*;

public class TopicBean
{
  String ID, Title, Creator, Created;
  List Comments;
  
  public TopicBean()
  {
    ID = Title = Creator = Created = "[na]";
    Comments = new ArrayList();
  }
  
  public void addComment( CommentBean comment )
  {
    Comments.add( comment );
  }
  
  public CommentBean getComment( int i )
  {
    return (CommentBean)Comments.get( i );
  }
  
  public int getCommentcount()
  {
    return Comments.size();
  }
  
  public void setId( String id )
  {
    this.ID = id;
  }
  
  public String getId()
  {
    return ID;
  }
  
  public void setTitle( String title )
  {
    this.Title = title;
  }
  
  public String getTitle()
  {
    return Title;
  }
  
  public void setCreator( String creator )
  {
    this.Creator = creator;
  }
  
  public String getCreator()
  {
    return Creator;
  }

  public void setCreated(String date)
  {
	  this.Created = date;
  }
  public String getCreated()
  {
	  return Created;
  }
}