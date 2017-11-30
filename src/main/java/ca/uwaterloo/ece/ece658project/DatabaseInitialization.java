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
	private PotluckInterface potluckManager;
	
	@PostConstruct
	public void initializeDatabase() {
		try {
			logger.info("initializing database");
			// create users
			String ansel, user1;
			ansel = "ansel@horn.name";
			userManager.createUser("Ansel Horn", ansel);
			// create potlucks
			Long potluck1, potluck2;
			potluck1 = potluckManager.createPotluck("First Potluck", ansel, "My first potluck!");
			System.out.println(potluck1);
			System.out.println(potluckManager.getMetadata());
			potluckManager.schedule(new Event("Setup", new Date(117, 12, 1, 18, 0), "Setup for the potluck."));
			potluckManager.schedule(new Event("Potluck", new Date(117, 12, 1, 19, 0), "My house"));
			potluckManager.acceptInvitation(ansel);
			potluckManager.addItem("Hamburgers");
			potluckManager.addItem("Cheese");
			potluckManager.commitToItem("Cheese", ansel);
			potluckManager.addRestriction("Vegitarian food only, please!");
		} catch (LoginException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
