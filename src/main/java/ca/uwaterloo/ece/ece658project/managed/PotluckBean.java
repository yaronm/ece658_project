package ca.uwaterloo.ece.ece658project.managed;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import ca.uwaterloo.ece.ece658project.UserManagerBean;
import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PotluckBean implements Serializable {

	@EJB
	protected PotluckInterface potluckManager;

	@EJB
	protected UserManagerBean userManager;

	@Inject
	protected SessionBean sessionBean;

	@NotNull
	private Long potluck;

	public PotluckBean() {
	}

	public Long getPotluck() {
		return potluck;
	}

	public void setPotluck(Long id) {
		this.potluck = id;
		potluckManager.selectPotluck(id);
	}

	public String getName() {
		return potluckManager.getMetadata().getName();
	}

	public String getDescription() {
		return potluckManager.getMetadata().getDescription();
	}

	public Collection<Event> getEvents() {
		return potluckManager.getEvents();
	}

	public Collection<User> getAttending() {
		return potluckManager.getAttending();
	}

	public Collection<User> getInvited() {
		return potluckManager.getInvited();
	}

	public Collection<String> getItems() {
		return potluckManager.getItems();
	}

	public Map<String, User> getCommitments() {
		return potluckManager.getCommitments();
	}

	public Collection<User> getUncommitted() {
		Set<User> uncommitted = new HashSet<>(getAttending());
		uncommitted.removeAll(getCommitments().values());
		return uncommitted;
	}

	public Collection<String> getRestrictions() {
		return potluckManager.getRestrictions();
	}

	public boolean userIsInvited() {
		for (User user : potluckManager.getInvited()) {
			if (user.getEmail().equals(sessionBean.getEmail())) {
				return true;
			}
		}
		return false;
	}

	public String acceptInvitation() {
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

	public String invite() {
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

	public String schedule() {
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

	public String addItem() {
		potluckManager.addItem(getNewItem());
		return null;
	}

	public String commit(String item) {
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

	public String addRestriction() {
		potluckManager.addRestriction(getNewRestriction());
		return null;
	}

}
