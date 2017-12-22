package ca.uwaterloo.ece.ece658project.managed;

import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;

import ca.uwaterloo.ece.ece658project.UserManagerBean;
import ca.uwaterloo.ece.ece658project.exception.LoginException;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class UserCreateBean extends UserLoginBean {
	private static final Logger logger = Logger.getLogger(UserCreateBean.class.getName());
	@NotNull
	private String name;

	private String password;

	
	public UserCreateBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		
		this.password = password;
	}

	public String create() {
		try {
			userManager.createUser(getName(), getEmail(), getPassword());
			sessionBean.setEmail(getEmail());
			return "success";
		} catch (LoginException e) {
			error(e.getMessage());
			return null;
		}
	}

}
