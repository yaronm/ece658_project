package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.EJB;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.UserEntity;
import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.User;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;


@Stateful
public class UserManagerBean {

	private static final Logger logger = Logger.getLogger(UserManagerBean.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private NotificationSystemBean emailBean;
	
	public UserManagerBean() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}

	public void createUser(String name, String email, String password) throws LoginException {
		logger.info("Create user: " + name + " <" + email + ">");

		// check if a user already exists with the given email
		UserEntity existingUser = entityManager.find(UserEntity.class, email);
		if (existingUser != null) {
			throw new LoginException("A user with that email already exists");
		}

		// if not, create a new user
		
		RandomNumberGenerator rng = new SecureRandomNumberGenerator();
		Object salt = rng.nextBytes();
		String hashedPasswordBase64 = new Sha256Hash(password, salt, 1024).toBase64();
		
		UserEntity user = new UserEntity(name, email, hashedPasswordBase64, salt);
		entityManager.persist(user);
		entityManager.flush();
		String message = "Hello " + name
				+ "\n\nYou have created a new potluck account. We hope you have great get togethers. \n\nWarm regards, \nThe PotLucky Team";
		emailBean.sendEmail(email, "Potluck user created", message);
		logger.info("USER created, now trying to login");
		this.login(email, password);

	}

	public User getUser(String email) {
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (user == null) {
			email = (String)SecurityUtils.getSubject().getSession().getAttribute("username");
			user = entityManager.find(UserEntity.class, email);
			if (user == null)
				return null;
		}
		Collection<Long> potlucks = new LinkedList<>();
		for (PotluckEntity potluck : user.getPotlucks()) {
			potlucks.add(potluck.getId());
		}
		return new User(user.getName(), user.getEmail(), potlucks);
	}

	public void login(String email, String password) throws LoginException {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(email);
		token.setPassword(password.toCharArray());
		token.setRememberMe(true);
	
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			logger.info("not_logged_in");
			try {
				currentUser.login(token);
				logger.info("logged in successfuly: ");
				currentUser.getSession().setAttribute("username", email);

			}catch(Exception ex) {
				throw new LoginException("Invalid login credentials");
			}
		}
		currentUser.getSession().setAttribute("username", currentUser.getPrincipal().toString());
	}

}
