package ca.uwaterloo.ece.ece658project;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class LoginBean implements Serializable {

	private static final Logger logger = Logger.getLogger("ca.uwaterloo.ece.ece658project.LoginBean");

	@NotNull
	private String name;

	@NotNull
	@Pattern(regexp="^[A-Za-z0-9._]+@[A-Za-z0-9.-]+$")
	private String email;

	public LoginBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		logger.info("name set: " + name);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		logger.info("email set: " + email);
	}

	public String login() {
		logger.info("login");
		return null;
	}

}
