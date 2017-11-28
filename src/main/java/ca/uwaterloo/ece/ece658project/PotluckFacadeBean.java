package ca.uwaterloo.ece.ece658project;

import java.util.logging.Logger;

import javax.ejb.Stateless;

@Stateless
public class PotluckFacadeBean {

	private static final Logger logger = Logger.getLogger(PotluckFacadeBean.class.getName());

	public Potluck createPotluck(User owner) {
		logger.info("create new potluck for owner: " + owner.getName());
		Potluck newPotluck = new Potluck(owner);
		owner.getPotlucks().add(newPotluck);
		return newPotluck;
	}

}
