package ca.uwaterloo.ece.ece658project.managed;

public interface IUserCreate {

	String getName();

	void setName(String name);

	String getPassword();

	void setPassword(String password);

	String create();

}