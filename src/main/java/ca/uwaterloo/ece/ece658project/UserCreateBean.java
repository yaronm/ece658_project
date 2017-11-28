package ca.uwaterloo.ece.ece658project;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class UserCreateBean extends UserLoginBean {

	@NotNull
	private String name;

	public UserCreateBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String create() {
		try {
			User user = userManager.createUser(getName(), getEmail());
			sessionBean.setUser(user);
			return "success";
		} catch (LoginException e) {
			error(e.getMessage());
			return null;
		}
	}

}
