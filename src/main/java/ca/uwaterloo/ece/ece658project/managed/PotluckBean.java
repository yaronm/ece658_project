package ca.uwaterloo.ece.ece658project.managed;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

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
	
	private String inviteEmail;
	
	public String getInviteEmail() {
		return inviteEmail;
	}

	public void setInviteEmail(String inviteEmail) {
		this.inviteEmail = inviteEmail;
	}

	public String invite() {
		if (getInviteEmail() == null) {
			return null;
		}
		User user = userManager.getUser(getInviteEmail());
		if (user == null) {
			return null;
		}
		potluckManager.invite(getInviteEmail());
		return null;
	}
	
	private String newItem;

	public String getNewItem() {
		return newItem;
	}

	public void setNewItem(String newItem) {
		this.newItem = newItem;
	}
	
	public String addItem() {
		if (getNewItem() == null) {
			return null;
		}
		potluckManager.addItem(getNewItem());
		return null;
	}
	
	public String commit(String item) {
		potluckManager.commitToItem(item, sessionBean.getEmail());
		return null;
	}
	
	private String newRestriction;

	public String getNewRestriction() {
		return newRestriction;
	}

	public void setNewRestriction(String newRestriction) {
		this.newRestriction = newRestriction;
	}
	
	public String addRestriction() {
		if (getNewRestriction() == null) {
			return null;
		}
		potluckManager.addRestriction(getNewRestriction());
		return null;
	}

}
