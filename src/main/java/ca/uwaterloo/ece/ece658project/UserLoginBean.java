package ca.uwaterloo.ece.ece658project;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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

	public UserLoginBean() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String login() {
		try {
			User user;
			user = userManager.login(getEmail());
			sessionBean.setUser(user);
			System.out.println(sessionBean);
			System.out.println(sessionBean.getUser());
			return "potlucklisting";
		} catch (LoginException e) {
			error(e.getMessage());
			return null;
		}
	}

}
