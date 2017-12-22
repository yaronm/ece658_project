package ca.uwaterloo.ece.ece658project.managed;

public interface IUserLogin {

	String getEmail();

	void setEmail(String email);

	String getPassword();

	void setPassword(String password);

	String login();

}