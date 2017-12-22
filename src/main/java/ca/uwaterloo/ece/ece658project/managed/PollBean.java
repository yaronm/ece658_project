package ca.uwaterloo.ece.ece658project.managed;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.AuthenticatedPotluckInterface;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PollBean implements Serializable {
	private static final Logger logger = Logger.getLogger(PotluckBean.class.getName());
	/*@EJB
	protected PollInterface pollManager;
	
	@EJB
	protected PollOptionInterface optionManager;
	 */
	
	@EJB
	protected AuthenticatedPotluckInterface potluckManager;
	
	@Inject
	protected SessionBean sessionBean;
	
	@NotNull
	private Long poll;

	public PollBean() {
	}

	public Long getPoll() {
		return poll;
	}


	public void setPoll(String id_s) throws LoginException {
		this.poll = Long.valueOf(id_s);
		potluckManager.select_poll(this.poll);
	}

	public String getName() throws LoginException {
		return potluckManager.getPollName();
	}

	public String getDescription() throws LoginException {
		return potluckManager.getPollDescription();
	}

	private Collection<String> add_as_collection(String to_Add) {
		Collection<String> col_to_add = new LinkedList<String>();
		col_to_add.add(to_Add);
		return col_to_add;
	}
	
	public Collection<Map<String, Collection<String>>> getOptions(String id) throws LoginException{
		this.poll = Long.valueOf(id);
		potluckManager.select_poll(this.poll);
		logger.info("id = "+this.poll.toString());
		Collection<Map<String, Collection<String>>> options = new LinkedList<>();
		Collection<Long> option_ids = potluckManager.getPollOptions();
		for (Long option : option_ids) {
			potluckManager.select_option(option);
			Map<String,Collection<String>> curr_option = new HashMap<>();
			curr_option.put("id", add_as_collection(option.toString()));
			curr_option.put("description", add_as_collection(potluckManager.getOptionDescription()));
			curr_option.put("voters", potluckManager.getRespondents());
			options.add(curr_option);
		}
		return options;
	}
	

	@NotNull
	private String newPollName;

	@NotNull
	private String newPollDescription;
	

	@NotNull
	private String newPollOption;

	public String getNewPollName() {
		return newPollName;
	}

	public void setNewPollName(String newPollName) {
		this.newPollName = newPollName;
	}

	public String getNewPollDescription() {
		return newPollDescription;
	}

	public void setNewPollDescription(String newPollDescription) {
		this.newPollDescription = newPollDescription;
	}
	
	public String getNewPollOption() {
		return newPollOption;
	}

	public void setNewPollOption(String newPollOption) {
		this.newPollOption = newPollOption;
	}
	
	public void addNewPollOption() throws LoginException {
		potluckManager.addOption(this.newPollDescription);
	}
	
	public void removePollOption() {
		//TODO
	}
	
	public void voteForPollOption(Long option) throws LoginException {
		potluckManager.select_option(option);
		potluckManager.addRespondent(this.sessionBean.getEmail());
	}
	
	public void unvoteForPollOption(Long option) throws LoginException {
		potluckManager.select_option(option);
		potluckManager.removeRespondent(this.sessionBean.getEmail());
	}

}
