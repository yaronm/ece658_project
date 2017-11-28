package ca.uwaterloo.ece.ece658project;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class DatabaseInitialization {

	private static final Logger logger = Logger.getLogger(DatabaseInitialization.class.getName());

	@EJB
	private UserManagerBean userManager;
	
	@EJB
	private PotluckFacadeBean potluckFacade;
	
	@PostConstruct
	public void initializeDatabase() {
		try {
			logger.info("initializing database");
			User ansel = userManager.createUser("Ansel Horn", "ansel@horn.name");
			Potluck potluck = potluckFacade.createPotluck(ansel);
			potluck.setDescription("My first potluck!");
			Potluck potluck2 = potluckFacade.createPotluck(ansel);
			potluck2.setDescription("My second potluck...");
		} catch (LoginException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
