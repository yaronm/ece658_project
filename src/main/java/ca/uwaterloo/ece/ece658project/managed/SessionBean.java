package ca.uwaterloo.ece.ece658project.managed;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import ca.uwaterloo.ece.ece658project.UserManagerBean;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class SessionBean implements Serializable {

	@EJB
	private UserManagerBean userManager;

	private String email;
	public SessionBean() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}
	
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
		return SecurityUtils.getSubject().isAuthenticated();
	}

	public String logout() {
		setEmail(null);
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		
		return "/index";
	}

}
