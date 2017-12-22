package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.LinkedList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.uwaterloo.ece.ece658project.entity.UserEntity;


@Stateless
public class realmUserBean {

	@PersistenceContext
	private EntityManager entityManager;

	public Collection<String> getUserInfo(String username){
		UserEntity user = entityManager.find(UserEntity.class, username);
		if (user == null)
			return null;
		Collection<String> users = new LinkedList<String>();
		users.add(user.getPassword());
		users.add(user.getSalt());
		entityManager.merge(user);
		return users;
	}
}
