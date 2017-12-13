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
			pollManager.createPoll("test", "test_poll", potluck1);
			Long op1 = pollManager.addOption("test_option1");
			Long op2 = pollManager.addOption("test_option2");
			Long op3 = pollManager.addOption("test_option3");
			optionManager.selectOption(op1);
			optionManager.addRespondent("yaronm93@gmail.com");
			optionManager.selectOption(op2);
			optionManager.addRespondent("yaronm93@gmail.com");
			optionManager.addRespondent("yaronm@rogers.com");
			optionManager.removeRespondent("yaronm93@gmail.com");
			Collection<Long> ops = pollManager.getOptions();
			for (Long op: ops) {
				optionManager.selectOption(op);
				logger.info(op.toString()+":"+optionManager.getDescription()+":\n");
				Collection<String> resp = optionManager.getRespondents();
				for (String r :resp) {
					logger.info(r+"\n");
				}
			}
			
			//potluckManager.duplicatePotluck("yaronm93@gmail.com");
			//potluckManager.duplicatePotluck("yaronm@rogers.com");
		} catch (LoginException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
