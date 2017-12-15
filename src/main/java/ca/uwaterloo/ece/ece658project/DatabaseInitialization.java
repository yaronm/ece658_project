package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.PollInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PollOptionInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;

@Singleton
@Startup
public class DatabaseInitialization {

	private static final Logger logger = Logger.getLogger(DatabaseInitialization.class.getName());

	@EJB
	private UserManagerBean userManager;
	
	@EJB
	private PotluckInterface potluckManager;
	
	@EJB
	private PollInterface pollManager;
	
	@EJB
	private PollOptionInterface optionManager;
	
	@PostConstruct
	public void initializeDatabase() {
		try {
			logger.info("initializing database");
			// create users
			String yaron, user1;
			yaron = "yaronm93@gmail.com";
			userManager.createUser("yaron milwid", yaron);
			// create potlucks
			Long potluck1, potluck2;
			potluck1 = potluckManager.createPotluck("First Potluck", yaron, "My first potluck!");
			System.out.println(potluck1);
			System.out.println(potluckManager.getMetadata());
			potluckManager.schedule(new Event("Setup", new Date(117, 12, 1, 18, 0), "Setup for the potluck."));
			potluckManager.schedule(new Event("Potluck", new Date(117, 12, 1, 19, 0), "My house"));
			potluckManager.acceptInvitation(yaron);
			potluckManager.addItem("Hamburgers");
			potluckManager.addItem("Cheese");
			potluckManager.commitToItem("Cheese", yaron);
			potluckManager.addRestriction("Vegitarian food only, please!");
			
			Long poll_id = potluckManager.addPoll("test", "test_poll");
			potluckManager.select_poll(poll_id);
			Long op1 = potluckManager.addOption("test_option1");
			Long op2 = potluckManager.addOption("test_option2");
			Long op3 = potluckManager.addOption("test_option3");
			potluckManager.select_option(op1);
			potluckManager.addRespondent("yaronm93@gmail.com");
			potluckManager.select_option(op2);
			potluckManager.addRespondent("yaronm93@gmail.com");
			potluckManager.addRespondent("yaronm@rogers.com");
			potluckManager.removeRespondent("yaronm93@gmail.com");
			Collection<Long> ops = potluckManager.getPollOptions();
			for (Long op: ops) {
				potluckManager.select_option(op);
				logger.info(op.toString()+":"+potluckManager.getOptionDescription()+":\n");
				Collection<String> resp = potluckManager.getRespondents();
				for (String r :resp) {
					logger.info(r+"\n");
				}
			}
			potluckManager.removePoll("yaronm93@gmail.com");
			for (Long poll : potluckManager.getPolls()) {
				potluckManager.select_poll(poll);
				logger.info(potluckManager.getPollName()+"\n");
			}
			potluckManager.deletePotluck("yaronm93@gmail.com");
			//potluckManager.duplicatePotluck("yaronm93@gmail.com");
			//potluckManager.duplicatePotluck("yaronm@rogers.com");
		} catch (LoginException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
