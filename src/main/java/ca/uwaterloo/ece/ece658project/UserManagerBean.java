package ca.uwaterloo.ece.ece658project;

import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class UserManagerBean {

	private static final Logger logger = Logger.getLogger(UserBean.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	private User user = null;

	public User createUser(String name, String email) throws LoginException {
		logger.info("create new user: " + name + " <" + email + ">");

		// check if a user already exists with the given email
		User existingUser = entityManager.find(User.class, email);
		if (existingUser != null) {
			throw new LoginException("A user with that email already exists");
		}

		// if not, create a new user
		User newUser = new User(name, email);
		entityManager.persist(newUser);
		return newUser;
	}

	public User getUser() {
		return user;
	}

	public boolean isLoggedIn() {
		return user != null;
	}

	public void login(String email) throws LoginException {
		logger.info("login: " + email);

		// check that a user exists with the given email
		user = entityManager.find(User.class, email);
		if (user == null) {
			throw new LoginException("Invalid login credentials");
		}
	}

}
