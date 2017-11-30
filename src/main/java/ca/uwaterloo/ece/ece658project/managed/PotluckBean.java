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

import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class PotluckBean implements Serializable {

	@EJB
	protected PotluckInterface potluckManager;

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

}
