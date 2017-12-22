package ca.uwaterloo.ece.ece658project.managed;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import ca.uwaterloo.ece.ece658project.UserManagerBean;
import ca.uwaterloo.ece.ece658project.exception.LoginException;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class UserLoginBean extends AbstractBean {

	@EJB
	protected UserManagerBean userManager;

	@Inject
	protected SessionBean sessionBean;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9._]+@[A-Za-z0-9.-]+$")
	private String email;
	
	private String password;

	public UserLoginBean() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String login() {
		try {
			userManager.login(getEmail(), getPassword());
			sessionBean.setEmail(getEmail());
			return "success";
		} catch (LoginException e) {
			error(e.getMessage());
			return null;
		}
	}

}
