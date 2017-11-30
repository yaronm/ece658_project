package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.uwaterloo.ece.ece658project.entity.PotluckEntity;
import ca.uwaterloo.ece.ece658project.entity.UserEntity;
import ca.uwaterloo.ece.ece658project.interfaces.Event;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckInterface;
import ca.uwaterloo.ece.ece658project.interfaces.PotluckMetadata;
import ca.uwaterloo.ece.ece658project.interfaces.User;

@Stateful
public class PotluckFacadeBean implements PotluckInterface {

	@PersistenceContext
	private EntityManager entityManager;

	@EJB
	private UserManagerBean userManager;

	private PotluckEntity potluck;

	@Override
	public Long createPotluck(String name, String owner, String description) {
		potluck = new PotluckEntity();
		potluck.setName(name);
		potluck.setDescription(description);
		potluck.setOwnerEmail(owner);
		UserEntity user = entityManager.find(UserEntity.class, owner);
		System.out.println("User: " + user);
		potluck.getUsers().put(owner, user);
		user.getPotlucks().add(potluck);
		entityManager.persist(potluck);
		return potluck.getId();
	}

	@Override
	public void selectPotluck(Long id) {
		potluck = entityManager.find(PotluckEntity.class, id);
	}

	@Override
	public void deletePotluck(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void schedule(Event event) {
		potluck.getEventTimes().put(event.getTitle(), event.getDate());
		potluck.getEventDescriptions().put(event.getTitle(), event.getDescription());
	}

	@Override
	public void invite(String email) {
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (!user.getPotlucks().contains(potluck)) {
			user.getPotlucks().add(potluck);
		}
		potluck.getUsers().put(user.getEmail(), user);
		potluck.getAttendingEmails().remove(user.getEmail());
		potluck.getInvitedEmails().add(user.getEmail());
	}

	@Override
	public void acceptInvitation(String email) {
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (!user.getPotlucks().contains(potluck)) {
			user.getPotlucks().add(potluck);
		}
		potluck.getUsers().put(user.getEmail(), user);
		potluck.getAttendingEmails().add(user.getEmail());
		potluck.getInvitedEmails().remove(user.getEmail());
	}

	@Override
	public void rejectInvitation(String email) {
		UserEntity user = entityManager.find(UserEntity.class, email);
		if (potluck.getOwnerEmail() != user.getEmail()) {
			user.getPotlucks().remove(potluck);
			potluck.getUsers().remove(user.getEmail());
		}
		potluck.getAttendingEmails().remove(user.getEmail());
		potluck.getInvitedEmails().remove(user.getEmail());
	}

	@Override
	public void addItem(String item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void commitToItem(String item, String user) {
		// TODO Auto-generated method stub

	}

	@Override
	public PotluckMetadata getMetadata() {
		User owner = userManager.getUser(potluck.getOwnerEmail());
		return new PotluckMetadata(potluck.getName(), owner, potluck.getDescription());
	}

	@Override
	public Collection<Event> getEvents() {
		Collection<Event> events = new LinkedList<>();
		for (String title : potluck.getEventTimes().keySet()) {
			events.add(new Event(title, potluck.getEventTimes().get(title), potluck.getEventDescriptions().get(title)));
		}
		return Collections.unmodifiableCollection(events);
	}

	@Override
	public Collection<User> getAttending() {
		Collection<User> attending = new LinkedList<>();
		for (String email : potluck.getAttendingEmails()) {
			User user = userManager.getUser(email);
			attending.add(user);
		}
		return Collections.unmodifiableCollection(attending);

	}

	@Override
	public Collection<User> getInvited() {
		Collection<User> invited = new LinkedList<>();
		for (String email : potluck.getInvitedEmails()) {
			User user = userManager.getUser(email);
			invited.add(user);
		}
		return Collections.unmodifiableCollection(invited);
	}

	@Override
	public Collection<String> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getCommitments() {
		// TODO Auto-generated method stub
		return null;
	}

}
