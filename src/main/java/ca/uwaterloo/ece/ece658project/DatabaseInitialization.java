package ca.uwaterloo.ece.ece658project;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class DatabaseInitialization {

	private static final Logger logger = Logger.getLogger(UserBean.class.getName());

	@EJB
	private UserManagerBean userManager;
	
	@PostConstruct
	public void initializeDatabase() {
		try {
			logger.info("create user ansel");
			userManager.createUser("ansel", "ansel@horn.name");
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

}
