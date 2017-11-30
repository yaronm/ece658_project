package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.UserEntity;
import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Stateful
public class UserManagerBean {

	private static final Logger logger = Logger.getLogger(UserManagerBean.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	public void createUser(String name, String email) throws LoginException {
		logger.info("Create user: " + name + " <" + email + ">");

		// check if a user already exists with the given email
		UserEntity existingUser = entityManager.find(UserEntity.class, email);
		if (existingUser != null) {
			throw new LoginException("A user with that email already exists");
		}

		// if not, create a new user
		UserEntity user = new UserEntity(name, email);
		entityManager.persist(user);
	}

	public User getUser(String email) {
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (user == null) {
			return null;
		}
		Collection<Long> potlucks = new LinkedList<>();
		for (PotluckEntity potluck : user.getPotlucks()) {
			potlucks.add(potluck.getId());
		}
		return new User(user.getName(), user.getEmail(), potlucks);
	}

	public void login(String email) throws LoginException {
		logger.info("Login: " + email);

		// check that a user exists with the given email
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (user == null) {
			throw new LoginException("Invalid login credentials");
		}
	}
	
}
