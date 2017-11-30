package ca.uwaterloo.ece.ece658project.managed;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import ca.uwaterloo.ece.ece658project.UserManagerBean;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class SessionBean implements Serializable {

	@EJB
	private UserManagerBean userManager;

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public User getUser() {
		return userManager.getUser(getEmail());
	}

	public boolean isLoggedIn() {
		return getEmail() != null;
	}

	public String logout() {
		setEmail(null);
		return "index";
	}

}
