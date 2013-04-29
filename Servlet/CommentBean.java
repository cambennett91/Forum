package benne.forum;

public class CommentBean
{
  String ID, TopicID, Comment, Name, Date, Picture;
  
  public CommentBean()
  {
    ID = TopicID = Name = Comment = Date = Picture = "[na]";
  }
  
  public void setName( String name )
  {
    this.Name = name;
  }
  
  public String getName()
  {
    return Name;
  }
  
  public void setPicture( String pic )
  {
    this.Picture = pic;
  }
  
  public String getPicture()
  {
    return Picture;
  }
  
  public void setDate( String date )
  {
    this.Date = date;
  }
  
  public String getDate()
  {
    return Date;
  }
  
  public void setComment( String comment )
  {
    this.Comment = comment;
  }
  
  public String getComment()
  {
    return Comment;
  }
  
  public void setId( String id )
  {
    this.ID = id;
  }
  
  public String getId()
  {
    return ID;
  }
  
  public void setTopicId( String id )
  {
    this.TopicID = id;
  }
  
  public String getTopicId()
  {
    return TopicID;
  }
  
}