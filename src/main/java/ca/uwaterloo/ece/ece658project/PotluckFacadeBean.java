package ca.uwaterloo.ece.ece658project;

import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class PotluckFacadeBean {

	private static final Logger logger = Logger.getLogger(PotluckFacadeBean.class.getName());
	
	@PersistenceContext
	private EntityManager entityManager;

	public Potluck createPotluck(User owner) {
		logger.info("create new potluck for owner: " + owner.getName());
		Potluck newPotluck = new Potluck(owner);
		owner.getPotlucks().add(newPotluck);
		return newPotluck;
	}
	
	public Potluck getPotluck(Long id) throws IllegalArgumentException {
		logger.info("retrieve potluck: " + id);
		Potluck potluck = entityManager.find(Potluck.class, id);
		if (potluck == null) {
			throw new IllegalArgumentException("No such potluck: " + id);
		}
		return potluck;
	}

}
