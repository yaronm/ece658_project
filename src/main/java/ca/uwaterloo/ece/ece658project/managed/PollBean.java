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
import ca.uwaterloo.ece.ece658project.interfaces.PollInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PollOptionInterface;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PollBean implements Serializable {
	private static final Logger logger = Logger.getLogger(PotluckBean.class.getName());
	@EJB
	protected PollInterface pollManager;
	
	@EJB
	protected PollOptionInterface optionManager;

	@Inject
	protected SessionBean sessionBean;
	
	@NotNull
	private Long poll;

	public PollBean() {
	}

	public Long getPoll() {
		return poll;
	}


	public void setPoll(String id_s) {
		this.poll = Long.valueOf(id_s);
		pollManager.selectPoll(this.poll);
	}

	public String getName() {
		return pollManager.getPollName();
	}

	public String getDescription() {
		return pollManager.getPollDescription();
	}

	private Collection<String> add_as_collection(String to_Add) {
		Collection<String> col_to_add = new LinkedList<String>();
		col_to_add.add(to_Add);
		return col_to_add;
	}
	
	public Collection<Map<String, Collection<String>>> getOptions(String id){
		this.poll = Long.valueOf(id);
		pollManager.selectPoll(this.poll);
		logger.info("id = "+this.poll.toString());
		Collection<Map<String, Collection<String>>> options = new LinkedList<>();
		Collection<Long> option_ids = pollManager.getOptions();
		for (Long option : option_ids) {
			optionManager.selectOption(option);
			Map<String,Collection<String>> curr_option = new HashMap<>();
			curr_option.put("id", add_as_collection(option.toString()));
			curr_option.put("description", add_as_collection(optionManager.getDescription()));
			curr_option.put("voters", optionManager.getRespondents());
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
	
	public void addNewPollOption() {
		optionManager.newOption(this.newPollDescription, this.poll);
	}
	
	public void removePollOption() {
		//TODO
	}
	
	public void voteForPollOption(Long option) {
		optionManager.selectOption(option);
		optionManager.addRespondent(this.sessionBean.getEmail());
	}
	
	public void unvoteForPollOption(Long option) {
		optionManager.selectOption(option);
		optionManager.removeRespondent(this.sessionBean.getEmail());
	}

}
