package ca.uwaterloo.ece.ece658project;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

	@EJB
 	private NotificationSystemBean emailBean;

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
		for (UserEntity user : potluck.getusers.values()()){
			String message = "Hello " + user.getName() +"\n Your potluck "+potluck.getName()+" has a new time and description."+
				"The new description is: \n "+event.getTitle()"+: "+ event.getDescription()+
				"\n and the new time is: \n" + event.getDate())+"\n Warm regards, \n The potLucky team";
			emailBean.sendEmail(user.getEmail(), "Potluck time and description added", message);
		}
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
		String message = "Hello " + user.getName() +"\n You have been invited to a new potluck!\n"+
			"the potluck was created by " + entityManager.find(UserEntity.class, potluck.getOwnerEmail()).getName() +
			"please log in to your account to view the potluck details.\nWarm regards, \n The potLucky team";
		emailBean.sendEmail(email, "Potluck invitation", message);

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

		String message = "Hello " + entityManager.find(UserEntity.class, potluck.getOwnerEmail()).getName() +
			"\n" + user.getName()+" is now attending your potluck"+potluck.getName()+"!\n"+
			"Warm regards, \n The potLucky team";
		emailBean.sendEmail(potluck.getOwnerEmail(), "Potluck invitation accepted", message);
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

		String message = "Hello " + entityManager.find(UserEntity.class, potluck.getOwnerEmail()).getName() +
			"\n" + user.getName()+" will not be attending your potluck"+potluck.getName()+".\n"+
			"Warm regards, \n The potLucky team";
		emailBean.sendEmail(potluck.getOwnerEmail(), "Potluck invitation rejected", message);
	}

	@Override
	public void addItem(String item) {
		potluck.getNecessaryItems().add(item);
	}

	@Override
	public void commitToItem(String item, String email) {
		potluck.getCommitedItems().put(item, email);
		
		String message = "Hello " + entityManager.find(UserEntity.class, potluck.getOwnerEmail()).getName() +
			"\n" + entityManager.find(UserEntity.class, email).getName()+
			" will bring "+ item + "to your potluck"+potluck.getName()+"!\n"+
			"Warm regards, \n The potLucky team";
		emailBean.sendEmail(potluck.getOwnerEmail(), "Potluck invitation accepted", message);
	}

	@Override
	public void addRestriction(String restriction) {
		potluck.getRestrictions().add(restriction);
		for (UserEntity user : potluck.getusers.values()()){
			String message = "Hello " + user.getName() +"\n Your potluck "+potluck.getName()+" has a new dietary restriction."+
				"The new restriction is: " + restriction + "\n Warm regards, \n The potLucky team";
			emailBean.sendEmail(user.getEmail(), "Potluck time and description added", message);
		}
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
		return potluck.getNecessaryItems();
	}

	@Override
	public Map<String, User> getCommitments() {
		Map<String, User> commitments = new HashMap<>();
		for (Map.Entry<String, String> entry : potluck.getCommitedItems().entrySet()) {
			User user = userManager.getUser(entry.getValue());
			commitments.put(entry.getKey(), user);
		}
		return commitments;
	}

	@Override
	public Collection<String> getRestrictions() {
		return potluck.getRestrictions();
	}

}
