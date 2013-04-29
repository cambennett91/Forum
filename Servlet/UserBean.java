package benne.forum;

public class UserBean {
	String ID, userName, userPassword, name, userPicture;
	// account ID, account name, password, person name, account picture
	public UserBean() {
		ID = userName = userPassword = Name = userPicture = "[na]";
	}
	public void SetName( String name ) {
		this.Name = name;
	}
	public String GetName() {
		return Name;
	}
	public void SetPicture( String picture ) {
		this.Picture = picture;
	}
	public String GetPicture() {
		return Picture;
	}
	public void SetUserName( String username ) {
		this.userName = username;
	}
	public String GetUserName() {
		return userName;
	}
	public void SetId( String id ) {
		this.ID = id;
	}
	public String GetId() {
		return ID;
	}
	public void SetPassword( String password ) {
		this.userPassword = password;
	}
	public String GetPassword() {
		return userPassword;
	}
}