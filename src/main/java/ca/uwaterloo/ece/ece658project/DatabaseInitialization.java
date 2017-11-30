package ca.uwaterloo.ece.ece658project;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;

@Singleton
@Startup
public class DatabaseInitialization {

	private static final Logger logger = Logger.getLogger(DatabaseInitialization.class.getName());

	@EJB
	private UserManagerBean userManager;
	
	@EJB
	private PotluckInterface potluckFacade;
	
	@PostConstruct
	public void initializeDatabase() {
		try {
			logger.info("initializing database");
			// create users
			String ansel, user1;
			ansel = "ansel@horn.name";
			user1 = "user1@example.com";
			userManager.createUser("Ansel Horn", ansel);
			userManager.createUser("User1", user1);
			// create potlucks
			Long potluck1, potluck2;
			potluck1 = potluckFacade.createPotluck("First Potluck", ansel, "My first potluck!");
//			potluckFacade.schedule(new Event("Setup", new Date(117, 12, 1, 18, 0), "Setup for the potluck."));
//			potluckFacade.schedule(new Event("Potluck", new Date(117, 12, 1, 19, 0), "My house"));
			potluckFacade.acceptInvitation(ansel);
			potluckFacade.invite(user1);
			potluckFacade.addItem("Hamburgers");
			potluckFacade.addItem("Cheese");
			potluckFacade.commitToItem("Cheese", ansel);
			potluckFacade.addRestriction("Vegitarian food only, please!");
			potluckFacade.createPotluck("Second Potluck", ansel, "My second potluck...");
		} catch (LoginException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
