package ca.uwaterloo.ece.ece658project.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import ca.uwaterloo.ece.ece658project.DatabaseInitialization;
import ca.uwaterloo.ece.ece658project.UserManagerBean;
import ca.uwaterloo.ece.ece658project.exception.LoginException;
import ca.uwaterloo.ece.ece658project.interfaces.AuthenticatedPotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.PollInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PollOptionInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PotluckBean implements Serializable {
	private static final Logger logger = Logger.getLogger(PotluckBean.class.getName());
	@EJB
	protected AuthenticatedPotluckInterface potluckManager;

	@EJB
	protected UserManagerBean userManager;
/*	
	@EJB
	protected PollInterface pollManager;
	
	@EJB
	protected PollOptionInterface optionManager;
*/
	
	@Inject
	protected SessionBean sessionBean;

	@NotNull
	private Long potluck;

	public PotluckBean() {
	}

	public Long getPotluck() {
		return potluck;
	}
	
	public void duplicate() throws LoginException {
		potluckManager.duplicatePotluck(sessionBean.getEmail());
	}

	public void setPotluck(Long id) throws LoginException {
		this.potluck = id;
		potluckManager.selectPotluck(id);
	}

	public String getName() throws LoginException {
		return potluckManager.getMetadata().getName();
	}

	public String getDescription() throws LoginException {
		return potluckManager.getMetadata().getDescription();
	}

	public Collection<Map<String, String>> getPolls() throws LoginException{
		Collection<Long> polls = potluckManager.getPolls();
		Collection<Map<String,String>> poll_info = new LinkedList<Map<String,String>>();
		for (Long poll : polls) {
			Map<String,String> curr_poll = new HashMap<String, String>();
			
			//pollManager.selectPoll(poll);
			potluckManager.select_poll(poll);
			curr_poll.put("id", poll.toString());
			curr_poll.put("Name", potluckManager.getPollName());
			curr_poll.put("Description", potluckManager.getPollDescription());
			poll_info.add(curr_poll);
		}
		return poll_info;
	}
	
	public Collection<Event> getEvents() throws LoginException {
		return potluckManager.getEvents();
	}

	public Collection<User> getAttending() throws LoginException {
		return potluckManager.getAttending();
	}

	public Collection<User> getInvited() throws LoginException {
		return potluckManager.getInvited();
	}

	public Collection<String> getItems() throws LoginException {
		return potluckManager.getItems();
	}

	public Map<String, User> getCommitments() throws LoginException {
		return potluckManager.getCommitments();
	}

	public Collection<User> getUncommitted() throws LoginException {
		Set<User> uncommitted = new HashSet<>(getAttending());
		uncommitted.removeAll(getCommitments().values());
		return uncommitted;
	}

	public Collection<String> getRestrictions() throws LoginException {
		return potluckManager.getRestrictions();
	}

	public boolean userIsInvited() throws LoginException {
		for (User user : potluckManager.getInvited()) {
			if (user.getEmail().equals(sessionBean.getEmail())) {
				return true;
			}
		}
		return false;
	}

	public String acceptInvitation() throws LoginException {
		potluckManager.acceptInvitation(sessionBean.getEmail());
		return null;
	}

	@Pattern(regexp = "^[A-Za-z0-9._]+@[A-Za-z0-9.-]+$")
	@NotNull
	private String inviteEmail;

	public String getInviteEmail() {
		return inviteEmail;
	}

	public void setInviteEmail(String inviteEmail) {
		this.inviteEmail = inviteEmail;
	}

	public String invite() throws LoginException {
		User user = userManager.getUser(getInviteEmail());
		if (user == null) {
			return null;
		}
		potluckManager.invite(getInviteEmail());
		return null;
	}

	@NotNull
	private String newEventTitle;

	@Pattern(regexp = "^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d$")
	@NotNull
	private String newEventDate;

	@NotNull
	private String newEventDescription;

	public String getNewEventTitle() {
		return newEventTitle;
	}

	public void setNewEventTitle(String newEventTitle) {
		this.newEventTitle = newEventTitle;
	}

	public String getNewEventDate() {
		return newEventDate;
	}

	public void setNewEventDate(String newEventDate) {
		this.newEventDate = newEventDate;
	}

	public String getNewEventDescription() {
		return newEventDescription;
	}

	public void setNewEventDescription(String newEventDescription) {
		this.newEventDescription = newEventDescription;
	}

	public String schedule() throws NumberFormatException, LoginException {
		String[] dateAndTime = getNewEventDate().split(" ");
		String[] date = dateAndTime[0].split("-");
		String[] time = dateAndTime[1].split(":");
		potluckManager.schedule(new Event(
				getNewEventTitle(), new Date(Integer.valueOf(date[0]) - 1901, Integer.valueOf(date[1]),
						Integer.valueOf(date[2]), Integer.valueOf(time[0]), Integer.valueOf(time[1])),
				getNewEventDescription()));
		return null;
	}

	@NotNull
	private String newItem;

	public String getNewItem() {
		return newItem;
	}

	public void setNewItem(String newItem) {
		this.newItem = newItem;
	}

	public String addItem() throws LoginException {
		potluckManager.addItem(getNewItem());
		return null;
	}

	public String commit(String item) throws LoginException {
		potluckManager.commitToItem(item, sessionBean.getEmail());
		return null;
	}

	@NotNull
	private String newRestriction;

	public String getNewRestriction() {
		return newRestriction;
	}

	public void setNewRestriction(String newRestriction) {
		this.newRestriction = newRestriction;
	}

	public String addRestriction() throws LoginException {
		potluckManager.addRestriction(getNewRestriction());
		return null;
	}
	
	@NotNull
	private String newName;
	
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public void changeName() throws LoginException {
		potluckManager.changePotluckName(sessionBean.getEmail(),getNewName());
	}
	
	@NotNull
	private String newDescription;
	
	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}
	
	public String getnewDescription() {
		return newDescription;
	}
	
	public void changeDescription() throws LoginException {
		potluckManager.changePotluckDescription(sessionBean.getEmail(),getnewDescription());
	}
	

	

}
