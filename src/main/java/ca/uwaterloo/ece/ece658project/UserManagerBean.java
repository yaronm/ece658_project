package ca.uwaterloo.ece.ece658project;

import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class UserManagerBean {

	private static final Logger logger = Logger.getLogger(UserManagerBean.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	public User createUser(String name, String email) throws LoginException {
		logger.info("Create user: " + name + " <" + email + ">");

		// check if a user already exists with the given email
		User existingUser = entityManager.find(User.class, email);
		if (existingUser != null) {
			throw new LoginException("A user with that email already exists");
		}

		// if not, create a new user
		User user = new User(name, email);
		entityManager.persist(user);
		return user;
	}

	public User login(String email) throws LoginException {
		logger.info("Login: " + email);

		// check that a user exists with the given email
		User user = entityManager.find(User.class, email);
		if (user == null) {
			throw new LoginException("Invalid login credentials");
		}
		
		// if so, return that user
		return user;
	}

}
